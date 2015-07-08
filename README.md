<!---
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. See accompanying LICENSE file.
-->

# LevelDB Shell

## Description

LevelDB Shell provides a simple command line shell to do the basic operations,
i.e., put, get and delete on a LevelDB database.

## Build

Please use the following command to build the shell tool from source code:

    gradle clean distTar

## Usage

Execute `./bin/leveldb-shell` to start the shell. One argument, which is the
path to the root directory of LevelDB database files. If LevelDB database files
doesn't exist, a fresh database will be created there.