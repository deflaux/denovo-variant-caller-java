package com.google.cloud.genomics.denovo;

import com.google.api.services.genomics.Genomics;
import com.google.cloud.genomics.denovo.DenovoUtil.Caller;
import com.google.cloud.genomics.denovo.DenovoUtil.Chromosome;
import com.google.cloud.genomics.denovo.DenovoUtil.InferenceMethod;
import com.google.cloud.genomics.denovo.DenovoUtil.TrioIndividual;

import java.util.Map;
import java.util.Set;

/**
 * TODO: Insert description here. (generated by smoitra)
 */
public class DenovoShared {

  private final Genomics genomics;
  private final int numThreads;
  private final Map<TrioIndividual, String> personToCallsetIdMap;
  private final Map<TrioIndividual, String> personToReadsetIdMap;
  private final Map<TrioIndividual, String> personToCallsetNameMap;
  private final Map<String, TrioIndividual> callsetIdToPersonMap;
  private final Set<Chromosome> chromosomes;
  private final InferenceMethod inferMethod;
  private final String datasetId;
  private final Long startPosition;
  private final Long endPosition;
  private final int debugLevel;
  private final double lrtThreshold;
  private final Caller caller;
  private final String outputFileName;
  private final String inputFileName;
  private final int maxApiRetries;
  private final long maxVariantResults;
  private final double sequenceErrorRate;
  private final double denovoMutationRate;

  
  private DenovoShared(Builder builder) {
    genomics = builder.genomics;
    numThreads = builder.numThreads;
    personToCallsetIdMap = builder.personToCallsetIdMap;
    personToReadsetIdMap = builder.personToReadsetIdMap;
    personToCallsetNameMap = builder.personToCallsetNameMap;
    callsetIdToPersonMap = builder.callsetIdToPersonMap;
    chromosomes = builder.chromosomes;
    inferMethod = builder.inferMethod;
    datasetId = builder.datasetId;
    startPosition = builder.startPosition;
    endPosition = builder.endPosition;
    debugLevel = builder.debugLevel;
    lrtThreshold = builder.lrtThreshold;
    outputFileName = builder.outputFileName;
    maxApiRetries = builder.maxApiRetries;
    maxVariantResults = builder.maxVariantResults;
    inputFileName = builder.inputFileName;
    sequenceErrorRate = builder.sequenceErrorRate;
    denovoMutationRate = builder.denovoMutationRate;
    caller = builder.caller;
  }

  /**
   * @return the numThreads
   */
  public int getNumThreads() {
    return numThreads;
  }

  /**
   * @return the personToCallsetIdMap
   */
  public Map<TrioIndividual, String> getPersonToCallsetIdMap() {
    return personToCallsetIdMap;
  }

  /**
   * @return the personToReadsetIdMap
   */
  public Map<TrioIndividual, String> getPersonToReadsetIdMap() {
    return personToReadsetIdMap;
  }

  /**
   * @return the personToCallsetNameMap
   */
  public Map<TrioIndividual, String> getPersonToCallsetNameMap() {
    return personToCallsetNameMap;
  }

  /**
   * @return the callsetIdToPersonMap
   */
  public Map<String, TrioIndividual> getCallsetIdToPersonMap() {
    return callsetIdToPersonMap;
  }

  /**
   * @return the chromosomes
   */
  public Set<Chromosome> getChromosomes() {
    return chromosomes;
  }

  /**
   * @return the inferMethod
   */
  public InferenceMethod getInferMethod() {
    return inferMethod;
  }

  /**
   * @return the datasetId
   */
  public String getDatasetId() {
    return datasetId;
  }

  /**
   * @return the startPosition
   */
  public Long getStartPosition() {
    return startPosition;
  }

  /**
   * @return the endPosition
   */
  public Long getEndPosition() {
    return endPosition;
  }

  /**
   * @return the debugLevel
   */
  public int getDebugLevel() {
    return debugLevel;
  }

  /**
   * @return the lrtThreshold
   */
  public double getLrtThreshold() {
    return lrtThreshold;
  }

  /**
   * @return the stageId
   */
  public Caller getCaller() {
    return caller;
  }

  /**
   * @return the outputFileName
   */
  public String getOutputFileName() {
    return outputFileName;
  }

  /**
   * @return the inputFileName
   */
  public String getInputFileName() {
    return inputFileName;
  }

  /**
   * @return the max_api_retries
   */
  public int getMaxApiRetries() {
    return maxApiRetries;
  }

  /**
   * @return the max_variant_results
   */
  public long getMaxVariantResults() {
    return maxVariantResults;
  }

  /**
   * @return the sequenceErrorRate
   */
  public double getSequenceErrorRate() {
    return sequenceErrorRate;
  }

  /**
   * @return the denovoMutationRate
   */
  public double getDenovoMutationRate() {
    return denovoMutationRate;
  }

  /**
   * @return the genomics
   */
  public Genomics getGenomics() {
    return genomics;
  }

  public static class Builder {

    private Genomics genomics;
    private int numThreads = 1;
    private Map<TrioIndividual, String> personToCallsetIdMap;
    private Map<TrioIndividual, String> personToReadsetIdMap;
    private Map<TrioIndividual, String> personToCallsetNameMap;
    private Map<String, TrioIndividual> callsetIdToPersonMap;
    private Set<Chromosome> chromosomes;
    private InferenceMethod inferMethod;
    private String datasetId;
    private Long startPosition;
    private Long endPosition;
    private int debugLevel;
    private double lrtThreshold;
    private Caller caller;
    private String outputFileName;
    private int maxApiRetries;
    private long maxVariantResults;
    private String inputFileName;
    private double sequenceErrorRate;
    private double denovoMutationRate;


    public Builder denovoMutationRate(double denovoMutationRate) {
      this.denovoMutationRate = denovoMutationRate;
      return this;
    }

    public Builder sequenceErrorRate(double sequenceErrorRate) {
      this.sequenceErrorRate = sequenceErrorRate;
      return this;
    }

    
    public Builder max_variant_results(long max_variant_results) {
      this.maxVariantResults = max_variant_results;
      return this;
    }
    
    public Builder max_api_retries(int max_api_retries) {
      this.maxApiRetries = max_api_retries;
      return this;
    }

    public Builder caller(Caller caller) {
      this.caller = caller;
      return this;
    }

    public Builder inputFileName(String inputFileName) {
      this.inputFileName = inputFileName;
      return this;
    }
    
    public Builder outputFileName(String outputFileName) {
      this.outputFileName = outputFileName;
      return this;
    }
    
    public Builder lrtThreshold(double lrtThreshold) {
      this.lrtThreshold = lrtThreshold;
      return this;
    }

    public Builder genomics(Genomics genomics) {
      this.genomics = genomics;
      return this;
    }

    public Builder numThreads(int numThreads) {
      this.numThreads = numThreads;
      return this;
    }

    public Builder debugLevel(int debugLevel) {
      this.debugLevel = debugLevel;
      return this;
    }

    public Builder personToCallsetIdMap(Map<TrioIndividual, String> personToCallsetIdMap) {
      this.personToCallsetIdMap = personToCallsetIdMap;
      return this;
    }

    public Builder personToReadsetIdMap(Map<TrioIndividual, String> personToReadsetIdMap) {
      this.personToReadsetIdMap = personToReadsetIdMap;
      return this;
    }

    public Builder personToCallsetNameMap(Map<TrioIndividual, String> personToCallsetNameMap) {
      this.personToCallsetNameMap = personToCallsetNameMap;
      return this;
    }

    public Builder callsetIdToPersonMap(Map<String, TrioIndividual> callsetIdToPersonMap) {
      this.callsetIdToPersonMap = callsetIdToPersonMap;
      return this;
    }

    public Builder chromosomes(Set<Chromosome> chromosomes) {
      this.chromosomes = chromosomes;
      return this;
    }

    public Builder inferMethod(InferenceMethod inferMethod) {
      this.inferMethod = inferMethod;
      return this;
    }

    public Builder datasetId(String datasetId) {
      this.datasetId = datasetId;
      return this;
    }

    public Builder startPosition(Long startPosition) {
      this.startPosition = startPosition;
      return this;
    }

    public Builder endPosition(Long endPosition) {
      this.endPosition = endPosition;
      return this;
    }
    
    public DenovoShared build(){
      return new DenovoShared(this);
    }
  }


}
