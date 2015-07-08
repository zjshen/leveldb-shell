/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.zjshen.leveldb.shell;


import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import jline.console.ConsoleReader;
import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class LevelDBSehll {

  private static Splitter splitter =
      Splitter.on(" ").omitEmptyStrings().trimResults();

  private static void printScriptUsage() {
    System.out.println("Usage: LevelDB shell [path]");
    System.out.println(
        "  path - the path to the root directory of LevelDB files");
  }

  private static void printShellUsage(PrintWriter out) {
    printScreen(out,
        "Usage: LevelDB shell commands",
        "  help  - print the usage information",
        "  clear - clean the screen"
        "  quit  - leave the LevelDB shell",
        "  put   - put key value pair into the database."
            + " Usage: put [key] [value]",
        "  get  - get the value by the given key from the database."
            + " Usage: get [key]",
        "  delete - delete the entry by the given key from the database"
            + " Usage: delete [key]");
  }

  private static void printScreen(PrintWriter out, String... lines) {
    for (String line : lines) {
      out.println(line);
    }
    out.flush();
  }

  private static String[] tokenize(String line) {
    String[] parts = line.split(" ");
    for (int i = 0; i < parts.length; ++i) {
      parts[i] = parts[i].trim();
    }
    return parts;
  }

  private static DB loadDB(String path) throws IOException {
    JniDBFactory factory = new JniDBFactory();
    Options options = new Options();
    options.createIfMissing(true);
    return factory.open(new File(path), options);
  }

  public static void main(String[] args) throws IOException {
    ConsoleReader reader = new ConsoleReader();
    reader.setPrompt("leveldb> ");

    if (args == null || args.length == 0) {
      printScriptUsage();
      return;
    }
    try (DB db = loadDB(args[0])) {

      String line;
      PrintWriter out = new PrintWriter(reader.getOutput());
      while ((line = reader.readLine()) != null) {
        try {
          List<String> tokens = splitter.splitToList(line);
          if (tokens == null || tokens.size() == 0) {
            continue;
          } else if (tokens.get(0).equalsIgnoreCase("quit")) {
            break;
          } else if (tokens.get(0).equalsIgnoreCase("clear")) {
            reader.clearScreen();
          } else if (tokens.get(0).equalsIgnoreCase("help")) {
            printShellUsage(out);
          } else if (tokens.get(0).equalsIgnoreCase("put")) {
            if (tokens.size() == 3) {
              db.put(tokens.get(1).getBytes(Charsets.UTF_8),
                  tokens.get(2).getBytes(Charsets.UTF_8));
            } else {
              printScreen(out, "Illegal Argument. Usage: put [key] [value]");
            }
          } else if (tokens.get(0).equalsIgnoreCase("get")) {
            if (tokens.size() == 2) {
              byte[] bytes = db.get(tokens.get(1).getBytes(Charsets.UTF_8));
              if (bytes != null && bytes.length > 0) {
                out.println(new String(bytes, Charsets.UTF_8));
                out.flush();
              }
            } else {
              printScreen(out, "Illegal Argument. Usage: get [key]");
            }
          } else if (tokens.get(0).equalsIgnoreCase("delete")) {
            if (tokens.size() == 2) {
              if (tokens.size() == 2) {
                db.delete(tokens.get(1).getBytes(Charsets.UTF_8));
              } else {
                printScreen(out, "Illegal Argument. Usage: delete [key]");
              }
            }
          } else {
            printScreen(out,
                "Invalid command. Use help to the print usage information");
          }
        } catch (Exception e) {
          e.printStackTrace(out);
          out.flush();
        }
      }
    }
  }
}
