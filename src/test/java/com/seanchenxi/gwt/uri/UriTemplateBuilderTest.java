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

    System.out.println(uriTemplate.getTemplate());

    Map<String, Object> values = new HashMap<String, Object>();
    values.put("name", "XiCHEN");

    Map<String, Object> subValues = new HashMap<String, Object>();
    subValues.put("city", "Tangshan");
    subValues.put("country", "China");
    values.put("adr", subValues);
    values.put("list", Arrays.asList(""));
    uriTemplate.setValues(values);

    String compile = uriTemplate.compile();

    System.out.println(compile);

    UriTemplate uriTemplate2 = UriTemplateBuilder.create().literal(Literal.wrap("/lalala"))
        .query(var("name", 4), explode("adr"), var("list*"))
        .baseUrl("http://www.google.com").literal("/ooo")
        .build();

    Assert.assertEquals(uriTemplate.getTemplate(), uriTemplate2.getTemplate());

  }

}