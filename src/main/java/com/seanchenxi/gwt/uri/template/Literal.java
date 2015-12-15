package com.seanchenxi.gwt.uri.template;

import java.util.Map;

/**
 * @author Xi CHEN
 * @since 13/12/15.
 */
public class Literal extends TemplatePartial {

  public static Literal wrap(String literal){
    return new Literal(literal);
  }

  private String value;

  public Literal(String literal) {
    this.value = literal;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String template() {
    return getValue();
  }

  @Override
  public String compile(Map<String, Object> values) {
    return getValue();
  }

}
