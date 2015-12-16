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

import com.seanchenxi.gwt.uri.template.Literal;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.seanchenxi.gwt.uri.template.VarSpecBuilder.explode;
import static com.seanchenxi.gwt.uri.template.VarSpecBuilder.var;

/**
 * @author Xi CHEN
 * @since 13/12/15.
 */
public class UriTemplateBuilderTest {

  @Test
  public void testTemplate(){
    UriTemplate uriTemplate = UriTemplateBuilder.create()
        .literal("/lalala").expression("{?name:4,adr*,list*}")
        .baseUrl("http://www.google.com").literal("/ooo")
        .build();

    System.out.println(uriTemplate.template());

    Map<String, Object> values = new HashMap<String, Object>();
//    values.put("name", "XiCHEN");

    Map<String, Object> subValues = new HashMap<String, Object>();
//    subValues.put("city", "Tangshan");
//    subValues.put("country", "China");
    values.put("adr", subValues);
    values.put("list", Arrays.asList("one","true"));
    uriTemplate.setValues(values);

    String compile = uriTemplate.expand();

    System.out.println(compile);

    UriTemplate uriTemplate2 = UriTemplateBuilder.create().literal(Literal.wrap("/lalala"))
        .query(var("name", 4), explode("adr"), var("list*"))
        .baseUrl("http://www.google.com").literal("/ooo")
        .build();

    Assert.assertEquals(uriTemplate.template(), uriTemplate2.template());

    UriTemplate uriTemplate3 = UriTemplateBuilder.create("http://www.google.com/lalala/ooo{?name:4,adr*,list*}").build();

    String compile2=uriTemplate3.expand(values);
    System.out.println(compile2);

    System.out.println(uriTemplate3);
    Assert.assertNotEquals(uriTemplate3.template(), uriTemplate2.template());
  }

}