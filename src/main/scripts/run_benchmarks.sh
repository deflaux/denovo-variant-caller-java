#!/bin/sh
#
#Copyright 2014 Google Inc. All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
# in compliance with the License. You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software distributed under the License
# is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
# or implied. See the License for the specific language governing permissions and limitations under
# the License.
#

# Get name of source file
script="$(readlink -f ${BASH_SOURCE[0]})"
 
# Get base directoru
base=$( dirname $( dirname $( dirname $( dirname $script ) ) ) )
 
cd "$base"
mvn package

java -cp target/classes:`find target/lib -name "*.jar" | tr "\n" ":"` com.google.cloud.genomics.denovo.RunBenchmarks
