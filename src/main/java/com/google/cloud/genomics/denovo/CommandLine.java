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

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Command line options handler for GenomicsExperiment
 */
class CommandLine {

  CmdLineParser parser;

  @Argument(usage = "The stage of the calling pipeline ; usually stage1 or stage2",
      metaVar = "<stage id>", required = true)
  public String stageId = null;

  @Option(name = "--client_secrets_filename", metaVar = "<client_secrets_filename>",
      usage = "Path to client_secrets.json")
  public String clientSecretsFilename = "client_secrets.json";

  @Option(name = "--require_all_scopes",
      usage = "Uncommon. If specified, the user will be asked for all Genomics related OAuth scopes.")
  public boolean requireAllScopes = false;

  @Option(name = "--candidates_file", metaVar = "<cand file>",
      usage = "Specify the file onto which to write the candidates")
  public String candidatesFile = null;

  @Option(name = "--seq_err_rate", metaVar = "<seq_err_rate>",
      usage = "Specify the sequence error rate (default 1e-2)")
  public double sequenceErrorRate = 1e-2;

  @Option(name = "--denovo_mut_rate", metaVar = "<denovo_mut_rate>",
      usage = "Specify the denovo mutation rate (default 1e-8)")
  public double denovoMutationRate = 1e-8;

  
  public CommandLine() {
    parser = new CmdLineParser(this);
  }

  public void setArgs(String[] args) throws CmdLineException {
    parser.parseArgument(args);
  }

  public void printHelp(String headline, Appendable out) throws IOException {
    out.append(headline).append("\n").append(getUsage());
  }

  public String getUsage() {
    StringWriter sw = new StringWriter();
    sw.append("Usage: GenomicsExperiment stage_id [flags...]\n");
    parser.printUsage(sw, null);
    return sw.toString();
  }

}