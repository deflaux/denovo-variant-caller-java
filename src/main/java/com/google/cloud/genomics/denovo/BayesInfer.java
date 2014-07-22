/*
 *Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.genomics.denovo;


import com.google.cloud.genomics.denovo.DenovoUtil.TrioIndividuals;
import com.google.cloud.genomics.denovo.DenovoUtil.Genotypes;
import com.google.cloud.genomics.denovo.ReadSummary;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/*
 * Performs Bayesian Inference over reads Contains logic for creating the bayes net, calculating
 * likelihoods, performing Maximum A (MAP) inference and checking whether MAP candidate is indeed a
 * denovo variant
 */
public class BayesInfer {
  private static double sequenceErrorRate;
  private static double denovoMutationRate;
  private static BayesNet bn;
  private static boolean isInitialized = false;

  private BayesInfer() {}

  // static initialization for bayes net object
  private static void Init(CommandLine cmdLine) {

    // Check if the static class has already been initialized
    if (isInitialized) {
      return;
    }

    // Set the error rates from the commandline
    sequenceErrorRate = cmdLine.sequenceErrorRate;
    denovoMutationRate = cmdLine.denovoMutationRate;

    // Create a new Bayes net and fill in the params
    bn = new BayesNet();
    bn.addNode(new Node(TrioIndividuals.DAD, null,
        createConditionalProbabilityTable(TrioIndividuals.DAD)));
    bn.addNode(new Node(TrioIndividuals.MOM, null,
        createConditionalProbabilityTable(TrioIndividuals.MOM)));
    List<Node> childParents = new ArrayList<Node>();
    childParents.add(bn.nodeMap.get(TrioIndividuals.DAD));
    childParents.add(bn.nodeMap.get(TrioIndividuals.MOM));
    bn.addNode(new Node(TrioIndividuals.CHILD, childParents,
        createConditionalProbabilityTable(TrioIndividuals.CHILD)));

    // Set the initialization flag
    isInitialized = true;
  }

  /*
   * Creates the conditional probability table to be used in the bayes net One each for mom, dad and
   * child
   */
  /**
   * @param individual
   * @return conditionalProbabilityTable
   */
  private static Map<List<Genotypes>, Double> createConditionalProbabilityTable(
      TrioIndividuals individual) {

    Map<List<Genotypes>, Double> conditionalProbabilityTable = new HashMap<>();
    if (individual == TrioIndividuals.DAD || individual == TrioIndividuals.MOM) {
      for (Genotypes genoType : Genotypes.values()) {
        conditionalProbabilityTable.put(Collections.singletonList(genoType),
            1.0 / Genotypes.values().length);
      }
    } else { // individual == TrioIndividuals.CHILD

      // Loops over parent Genotypes
      for (Genotypes genoTypeDad : Genotypes.values()) {
        for (Genotypes genoTypeMom : Genotypes.values()) {

          int validInheritanceCases = 0;
          // Initial pass
          for (Genotypes genoTypeChild : Genotypes.values()) {
            double prob;
            String dadAlleles = genoTypeDad.getAlleles();
            String momAlleles = genoTypeMom.getAlleles();
            String childAlleles = genoTypeChild.getAlleles();
            String c1 = childAlleles.substring(0, 1);
            String c2 = childAlleles.substring(1, 2);
            boolean predicate1 = momAlleles.contains(c1) & dadAlleles.contains(c2);
            boolean predicate2 = momAlleles.contains(c2) & dadAlleles.contains(c1);
            boolean predicate3 = predicate1 | predicate2;
            if (predicate3) {
              prob = 1.0;
              validInheritanceCases++;
            } else {
              prob = 0.0;
            }
            List<Genotypes> cptKey = Arrays.asList(genoTypeDad, genoTypeMom, genoTypeChild);
            conditionalProbabilityTable.put(cptKey, prob);
          }
          // Secondary Pass to normalize prob values
          for (Genotypes genoTypeChild : Genotypes.values()) {
            List<Genotypes> cptKey = Arrays.asList(genoTypeDad, genoTypeMom, genoTypeChild);

            boolean isNotInheritenceCase =
                -DenovoUtil.EPS <= conditionalProbabilityTable.get(cptKey)
                && conditionalProbabilityTable.get(cptKey) <= DenovoUtil.EPS;
            conditionalProbabilityTable.put(cptKey, isNotInheritenceCase ? denovoMutationRate
                : 1.0 / validInheritanceCases - denovoMutationRate
                    * (Genotypes.values().length - validInheritanceCases)
                    / (validInheritanceCases));
          }

          // Sanity check - probabilities should add up to 1.0 (almost)
          double totProb = 0.0;
          for (Genotypes genoTypeChild : Genotypes.values()) {
            List<Genotypes> cptKey = Arrays.asList(genoTypeDad, genoTypeMom, genoTypeChild);
            totProb += conditionalProbabilityTable.get(cptKey);
          }
          if (Math.abs(totProb - 1.0) > DenovoUtil.EPS) {
            throw new IllegalStateException(
                "cpt probabilities not adding up : " + String.valueOf(totProb));
          }
        }
      }
    }
    return conditionalProbabilityTable;
  }

  /*
   * Performs inference given a set of mom, dad and child reads to determine the most likely
   * genotype for the trio
   */
  public static boolean infer(Map<TrioIndividuals, ReadSummary> readSummaryMap,
      CommandLine cmdLine) {

    // Initialize the bayes net if not already done
    if (!isInitialized) {
      Init(cmdLine);
    }
    // Calculate Likelihoods of the different reads
    Map<TrioIndividuals, Map<Genotypes, Double>> individualLogLikelihood =
        getIndividualLogLikelihood(readSummaryMap);

    // Get the trio genotype with the max likelihood
    List<Genotypes> maxTrioGenoType = getMaxGenoType(individualLogLikelihood);

    // Check that the MAP genotype has indeed the highest likelihood
    boolean checkTrioGenoTypeIsDenovo = BayesInfer.checkTrioGenoTypeIsDenovo(maxTrioGenoType);

    // Convert to Tree Map in order to order the keys
    TreeMap<TrioIndividuals, ReadSummary> treeReadSummaryMap = new TreeMap<>();
    treeReadSummaryMap.putAll(readSummaryMap);

    String readCounts = Joiner.on(";").join(Iterables.transform(treeReadSummaryMap.entrySet(),
        new Function<Entry<TrioIndividuals, ReadSummary>, String>() {
          @Override
          public String apply(Entry<TrioIndividuals, ReadSummary> e) {
            return e.getKey().getName() + ":" + e.getValue().getCount().toString();
          }
        }));


    System.out.format("readCounts=%s,maxGenoType=%s,isDenovo=%b", readCounts,
        maxTrioGenoType.toString(), checkTrioGenoTypeIsDenovo);
    System.out.println();

    return checkTrioGenoTypeIsDenovo;
  }

  /**
   * Get the log likelihood of reads for a particular individual in a trio for all their possible
   * Genotypes
   *
   * @param readSummaryMap
   * @return individualLogLikelihood
   */
  private static Map<TrioIndividuals, Map<Genotypes, Double>> getIndividualLogLikelihood(
      Map<TrioIndividuals, ReadSummary> readSummaryMap) {
    Map<TrioIndividuals, Map<Genotypes, Double>> individualLogLikelihood = new HashMap<>();
    for (TrioIndividuals trioIndividual : TrioIndividuals.values()) {

      ReadSummary readSummary = readSummaryMap.get(trioIndividual);
      Map<Genotypes, Double> genoTypeLogLikelihood = getGenoTypeLogLikelihood(readSummary);
      individualLogLikelihood.put(trioIndividual, genoTypeLogLikelihood);
    }
    return individualLogLikelihood;
  }

  /**
   * Get the log likelihood for all possible Genotypes for a set of reads
   *
   * @param readSummary
   * @return genotypeLogLikelihood
   */
  private static Map<Genotypes, Double> getGenoTypeLogLikelihood(ReadSummary readSummary) {
    Map<Genotypes, Double> genotypeLogLikelihood = new HashMap<>();
    for (Genotypes genoType : Genotypes.values()) {
      Map<String, Integer> count = readSummary.getCount();
      boolean isHomozygous =
          genoType.getAlleles().substring(0, 1).equals(genoType.getAlleles().substring(1, 2));

      double readlogLikelihood = 0.0;
      for (String base : count.keySet()) {
        readlogLikelihood += getBaseLikelihood(genoType, isHomozygous, base);
      }
      genotypeLogLikelihood.put(genoType, readlogLikelihood);
    }
    return genotypeLogLikelihood;
  }

  /**
   * Check if the particular genotype is denovo i.e. present in kids but not in parents
   *
   * @param maxTrioGenoType
   * @return isDenovo
   */
  private static boolean checkTrioGenoTypeIsDenovo(List<Genotypes> maxTrioGenoType) {
    Genotypes genoTypeDad = maxTrioGenoType.get(0);
    Genotypes genoTypeMom = maxTrioGenoType.get(1);
    Genotypes genoTypeChild = maxTrioGenoType.get(2);

    String childAlleles = genoTypeChild.getAlleles();
    String momAlleles = genoTypeMom.getAlleles();
    String dadAlleles = genoTypeDad.getAlleles();

    String c1 = childAlleles.substring(0, 1);
    String c2 = childAlleles.substring(1, 2);
    boolean predicate1 = momAlleles.contains(c1) & dadAlleles.contains(c2);
    boolean predicate2 = momAlleles.contains(c2) & dadAlleles.contains(c1);
    boolean predicate3 = !(predicate1 | predicate2);
    return predicate3;
  }

  /**
   * Infer the most likely genotype given likelihood for all the Genotypes of the trio
   *
   * @param individualLogLikelihood
   * @return maxgenoType
   */
  private static List<Genotypes> getMaxGenoType(
      Map<TrioIndividuals, Map<Genotypes, Double>> individualLogLikelihood) {
    double maxLogLikelihood = Double.NEGATIVE_INFINITY;
    List<Genotypes> maxGenoType = null;

    // Calculate overall bayes net log likelihood
    for (Genotypes genoTypeDad : Genotypes.values()) {
      for (Genotypes genoTypeMom : Genotypes.values()) {
        for (Genotypes genoTypeChild : Genotypes.values()) {
          double logLikelihood = 0.0;

          /* Get likelihood from the reads */
          logLikelihood += individualLogLikelihood.get(TrioIndividuals.DAD).get(genoTypeDad);
          logLikelihood += individualLogLikelihood.get(TrioIndividuals.MOM).get(genoTypeMom);
          logLikelihood += individualLogLikelihood.get(TrioIndividuals.CHILD).get(genoTypeChild);

          /* Get likelihoods from the trio relationship */
          logLikelihood += bn.nodeMap.get(TrioIndividuals.DAD).conditionalProbabilityTable.get(
              Collections.singletonList(genoTypeDad));
          logLikelihood += bn.nodeMap.get(TrioIndividuals.MOM).conditionalProbabilityTable.get(
              Collections.singletonList(genoTypeMom));
          logLikelihood += bn.nodeMap.get(TrioIndividuals.CHILD).conditionalProbabilityTable.get(
              Arrays.asList(genoTypeDad, genoTypeMom, genoTypeChild));

          if (logLikelihood > maxLogLikelihood) {
            maxLogLikelihood = logLikelihood;
            maxGenoType = Arrays.asList(genoTypeDad, genoTypeMom, genoTypeChild);
          }
        }
      }
    }
    return maxGenoType;
  }

  /**
   * Get the log likelihood for a particular read base
   *
   * @param genoType
   * @param isHomozygous
   * @param base
   * @return logLikeliHood
   */
  private static double getBaseLikelihood(Genotypes genoType, boolean isHomozygous, String base) {
    if (isHomozygous) {
      if (genoType.getAlleles().contains(base)) {
        return Math.log(1 - sequenceErrorRate);
      } else {
        return Math.log(sequenceErrorRate) - Math.log(3);
      }
    } else {
      if (genoType.getAlleles().contains(base)) {
        return Math.log(1 - 2 * sequenceErrorRate / 3) - Math.log(2);
      } else {
        return Math.log(sequenceErrorRate) - Math.log(3);
      }
    }
  }

  /*
   * Bayes net data structure
   */
  static class BayesNet {
    public Map<TrioIndividuals, Node> nodeMap;

    public BayesNet() {
      nodeMap = new HashMap<TrioIndividuals, Node>();
    }

    public void addNode(Node node) {
      nodeMap.put(node.id, node);
    }
  }

  /*
   * Individual Node in the Bayes Net
   */
  static class Node {
    public TrioIndividuals id;
    public List<Node> parents;
    public Map<List<Genotypes>, Double> conditionalProbabilityTable;

    public Node(TrioIndividuals individual, List<Node> parents, Map<List<Genotypes>, Double> map) {
      this.id = individual;
      this.parents = parents;
      this.conditionalProbabilityTable = map;
    }
  }


}
