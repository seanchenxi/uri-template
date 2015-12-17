/*
 * Copyright 2015 Xi CHEN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seanchenxi.gwt.uri;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeNotNull;

/**
 * @author Xi CHEN
 * @since 13/12/15.
 */
public class SpecExampleTest{

  @Test
  public void testSpecExample() throws IOException {
    Map tests = loadTestJsonSpec("/test-spec/spec-examples.json");
    assumeNotNull(tests);
    assumeFalse(tests.isEmpty());
    doTest(tests);
  }

  @Test @Ignore
  public void testSpecExampleBySection() throws IOException {
    Map tests = loadTestJsonSpec("/test-spec/spec-examples-by-section.json");
    assumeNotNull(tests);
    assumeFalse(tests.isEmpty());
    doTest(tests);
  }

  @Test @Ignore
  public void testExtendedTests() throws IOException {
    Map tests = loadTestJsonSpec("/test-spec/extended-tests.json");
    assumeNotNull(tests);
    assumeFalse(tests.isEmpty());
    doTest(tests);
  }

  private Map loadTestJsonSpec(String path) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    InputStream spec = SpecExampleTest.class.getResourceAsStream(path);
    return mapper.readValue(spec, Map.class);
  }

  @SuppressWarnings("unchecked")
  private void doTest(Map testSpec) {
    for(Object level : testSpec.keySet()){
      System.out.println(level);

      Map content = (Map)testSpec.get(level);
      assumeNotNull(content);
      printLevel(content);

      Map<String, Object> variables = printVariables(content);

      List<List<Object>> tests = (List<List<Object>>)content.get("testcases");
      int count = 0;
      System.out.println("\tTests:");
      for(List<Object> test : tests){
        String template = (String)test.get(0);
        Object expected = test.get(1);
        System.out.print("\t\t" + (++count) + ". " + template + ": ");
        UriTemplate uriTemplate = UriTemplateBuilder.create(template).build();
        assertEquals(template, uriTemplate.template());

        String expand = uriTemplate.expand(variables);
        if(expected instanceof List){
          System.out.print(String.format("%s CONTAINS %s", expected, expand));
          int i = ((List) expected).indexOf(expand);
          assertTrue("KO \n", i > -1);
          System.out.print(": POS=" + i);
        }else{
          System.out.print(String.format("%s VS %s", expected, expand));
          assertEquals("KO \n", expected, expand);
        }

        System.out.print( ": OK\n");
      }
    }
  }

  private Map<String, Object> printVariables(Map content) {
    @SuppressWarnings("unchecked")
    Map<String, Object> variables = (Map<String, Object>)content.get("variables");
    System.out.println("\tVariables: ");
    Set<Map.Entry<String, Object>> entries = variables.entrySet();
    System.out.println(String.format("\t@@.%38s.", ' ').replace(' ', '-').replace("@", ""));
    for(Map.Entry<String, Object> entry : entries){
      System.out.println(String.format("\t|  %-7s|  %-24s  |", entry.getKey(), entry.getValue()));
    }
    System.out.println(String.format("\t@@`%38s'", ' ').replace(' ', '-').replace("@", ""));
    return variables;
  }

  private void printLevel(Map content) {
    System.out.println("\tLevel: " + content.get("level"));
  }

}