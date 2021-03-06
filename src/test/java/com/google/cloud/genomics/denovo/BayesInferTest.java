/*
 * Copyright 2014 Google Inc. All rights reserved.
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

import static com.google.cloud.genomics.denovo.DenovoUtil.TrioMember.CHILD;
import static com.google.cloud.genomics.denovo.DenovoUtil.TrioMember.DAD;
import static com.google.cloud.genomics.denovo.DenovoUtil.TrioMember.MOM;

import com.google.cloud.genomics.denovo.DenovoUtil.Allele;
import com.google.cloud.genomics.denovo.DenovoUtil.TrioMember;

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;


public abstract class BayesInferTest extends DenovoTest {

  BayesInfer bayesInferrer;
  
  @Before
  public void initBayesMocks() {
    bayesInferrer = new BayesInfer(shared);  
  }
  
 
  Map<TrioMember, ReadSummary> createReadSummaryMapChrXPos154226820() {
    Map<TrioMember, ReadSummary> readSummaryMap = new HashMap<>();
    for (TrioMember person : TrioMember.values()) {
      Map<Allele, Integer> baseCount = new HashMap<>();
      if (person == DAD ) baseCount.put(Allele.T,28);
      if (person == MOM ) baseCount.put(Allele.T,36);
      if (person == CHILD ) {
        baseCount.put(Allele.T,33);
        baseCount.put(Allele.C,15);
      }
      readSummaryMap.put(person, new ReadSummary().setCount(baseCount));
    }
    return readSummaryMap;
  }

  Map<TrioMember, ReadSummary> createReadSummaryMapChr1Pos816785() {
    Map<TrioMember, ReadSummary> readSummaryMap = new HashMap<>();
    for (TrioMember person : TrioMember.values()) {
      Map<Allele, Integer> baseCount = new HashMap<>();
      if (person == DAD ) {
        baseCount.put(Allele.C, 245);
        baseCount.put(Allele.A, 1);
        baseCount.put(Allele.T, 10);
      }
      if (person == MOM ) {
        baseCount.put(Allele.C, 236);
        baseCount.put(Allele.A, 4);
        baseCount.put(Allele.T, 16);
      }
      if (person == CHILD ) {
        baseCount.put(Allele.C, 223);
        baseCount.put(Allele.A, 5);
        baseCount.put(Allele.G, 1);
        baseCount.put(Allele.T, 27);
      }
      readSummaryMap.put(person, new ReadSummary().setCount(baseCount));
    }
    return readSummaryMap;
  }

  Map<TrioMember, ReadSummary> createReadSummaryMapChr1Pos846600L() {
    Map<TrioMember, ReadSummary> readSummaryMap = new HashMap<>();
    for (TrioMember person : TrioMember.values()) {
      Map<Allele, Integer> baseCount = new HashMap<>();
      if (person == DAD ) {
        baseCount.put(Allele.C, 29);
        baseCount.put(Allele.T, 1);
      }
      if (person == MOM ) {
        baseCount.put(Allele.C, 48);
      }
      if (person == CHILD ) {
        baseCount.put(Allele.C, 54);
        baseCount.put(Allele.T, 1);
      }
      readSummaryMap.put(person, new ReadSummary().setCount(baseCount));
    }
    return readSummaryMap;
  }

  Map<TrioMember, ReadSummary> createReadSummaryMapChr1Pos149035163L() {
    Map<TrioMember, ReadSummary> readSummaryMap = new HashMap<>();
    for (TrioMember person : TrioMember.values()) {
      Map<Allele, Integer> baseCount = new HashMap<>();
      if (person == DAD ) {
        baseCount.put(Allele.C, 225);
        baseCount.put(Allele.A, 2);
        baseCount.put(Allele.T, 24);
      }
      if (person == MOM ) {
        baseCount.put(Allele.C, 223);
        baseCount.put(Allele.A, 6);
        baseCount.put(Allele.T, 22);
        baseCount.put(Allele.G, 3);
      }
      if (person == CHILD ) {
        baseCount.put(Allele.C, 218);
        baseCount.put(Allele.T, 34);
        baseCount.put(Allele.G, 1);
        baseCount.put(Allele.A, 2);
      }
      readSummaryMap.put(person, new ReadSummary().setCount(baseCount));
    }
    return readSummaryMap;
  }
  
  Map<TrioMember, ReadSummary> createReadSummaryMapChr1Pos1298169() {
    Map<TrioMember, ReadSummary> readSummaryMap = new HashMap<>();
    for (TrioMember person : TrioMember.values()) {
      Map<Allele, Integer> baseCount = new HashMap<>();
      if (person == DAD ) {
        baseCount.put(Allele.A, 2);
        baseCount.put(Allele.T, 41);
      }
      if (person == MOM ) {
        baseCount.put(Allele.T, 30);
        baseCount.put(Allele.G, 3);
      }
      if (person == CHILD ) {
        baseCount.put(Allele.T, 39);
        baseCount.put(Allele.A, 6);
      }
      readSummaryMap.put(person, new ReadSummary().setCount(baseCount));
    }
    return readSummaryMap;
  }
  
  Map<TrioMember, ReadSummary> createReadSummaryMapChr170041751() {
    Map<TrioMember, ReadSummary> readSummaryMap = new HashMap<>();
    for (TrioMember person : TrioMember.values()) {
      Map<Allele, Integer> baseCount = new HashMap<>();
      if (person == DAD) {
        baseCount.put(Allele.C, 58);
        baseCount.put(Allele.T, 2);
      }
      if (person == MOM) {
        baseCount.put(Allele.T, 2);
        baseCount.put(Allele.C, 51);
      }
      if (person == CHILD) {
        baseCount.put(Allele.T, 8);
        baseCount.put(Allele.C, 28);
      }
      readSummaryMap.put(person, new ReadSummary().setCount(baseCount));
    }
    return readSummaryMap;
  }
  
  Map<TrioMember, ReadSummary> createReadSummaryMapChr1Pos149035163() {
    Map<TrioMember, ReadSummary> readSummaryMap = new HashMap<>();
    for (TrioMember person : TrioMember.values()) {
      Map<Allele, Integer> baseCount = new HashMap<>();
      if (person == DAD) {
        baseCount.put(Allele.C, 225);
        baseCount.put(Allele.A, 2);
        baseCount.put(Allele.T, 24);
      }
      if (person == MOM) {
        baseCount.put(Allele.T, 22);
        baseCount.put(Allele.C, 223);
        baseCount.put(Allele.A, 6);
        baseCount.put(Allele.G, 3);
      }
      if (person == CHILD) {
        baseCount.put(Allele.A, 2);
        baseCount.put(Allele.C, 218);
        baseCount.put(Allele.T, 34);
        baseCount.put(Allele.G, 1);
      }
      readSummaryMap.put(person, new ReadSummary().setCount(baseCount));
    }
    return readSummaryMap;
  }
  
  Map<TrioMember, ReadSummary> createReadSummaryMapChr1Pos75884343() {
    Map<TrioMember, ReadSummary> readSummaryMap = new HashMap<>();
    for (TrioMember person : TrioMember.values()) {
      Map<Allele, Integer> baseCount = new HashMap<>();
      if (person == DAD) {
        baseCount.put(Allele.G, 1);
        baseCount.put(Allele.T, 53);
      }
      if (person == MOM) {
        baseCount.put(Allele.T, 51);
      }
      if (person == CHILD) {
        baseCount.put(Allele.C, 27);
        baseCount.put(Allele.T, 26);
      }
      readSummaryMap.put(person, new ReadSummary().setCount(baseCount));
    }
    return readSummaryMap;
  }
  
  
  Map<TrioMember, ReadSummary> createReadSummaryMapChr1Pos110583335() {
    Map<TrioMember, ReadSummary> readSummaryMap = new HashMap<>();
    for (TrioMember person : TrioMember.values()) {
      Map<Allele, Integer> baseCount = new HashMap<>();
      if (person == DAD) {
        baseCount.put(Allele.G, 36);
      }
      if (person == MOM) {
        baseCount.put(Allele.G, 50);
      }
      if (person == CHILD) {
        baseCount.put(Allele.G, 21);
        baseCount.put(Allele.A, 23);
      }
      readSummaryMap.put(person, new ReadSummary().setCount(baseCount));
    }
    return readSummaryMap;
  }
}
