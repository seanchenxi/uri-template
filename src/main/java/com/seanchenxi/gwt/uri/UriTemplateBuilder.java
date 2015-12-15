package com.seanchenxi.gwt.uri;

import com.seanchenxi.gwt.uri.template.Expression;
import com.seanchenxi.gwt.uri.template.ExpressionBuilder;
import com.seanchenxi.gwt.uri.template.Literal;
import com.seanchenxi.gwt.uri.template.TemplatePartial;
import com.seanchenxi.gwt.uri.template.VarSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Xi CHEN
 * @since 14/12/15.
 */
public class UriTemplateBuilder {

  public static UriTemplateBuilder create(){
    return new UriTemplateBuilder();
  }

  private String baseUrl;

  private List<TemplatePartial> partials = new ArrayList<TemplatePartial>();

  private Map<String, Object> values = new HashMap<String, Object>();

  public UriTemplateBuilder baseUrl(String baseUrl){
    this.baseUrl = baseUrl;
    return this;
  }

  public UriTemplateBuilder expression(Expression expression) {
    return add(expression);
  }

  public UriTemplateBuilder expression(String expression) {
    return add(ExpressionBuilder.create(expression));
  }

  public UriTemplateBuilder literal(Literal literal){
    return add(literal);
  }

  public UriTemplateBuilder literal(String literal){
    String value;
    if(literal != null && !(value = literal.trim()).isEmpty()){
      if(value.startsWith(Expression.OPEN) && value.endsWith(Expression.CLOSE)) {
        return expression(value);
      }
      return literal(Literal.wrap(value));
    }
    return this;
  }

  public UriTemplateBuilder add(TemplatePartial partial){
    if(partial != null){
      int position = this.partials.size();
      partial.setPosition(position);
      this.partials.add(partial);
    }
    return this;
  }

  public UriTemplateBuilder add(ExpressionBuilder builder) {
    return add(builder.build());
  }

  public UriTemplateBuilder set(String name, Object value){
    values.put(name, value);
    return this;
  }

  public UriTemplateBuilder simple(VarSpec... varSpec) {
    return add(ExpressionBuilder.simple(varSpec));
  }

  public UriTemplateBuilder reserved(VarSpec... varSpec) {
    return add(ExpressionBuilder.reserved(varSpec));
  }

  public UriTemplateBuilder fragment(VarSpec... varSpec) {
    return add(ExpressionBuilder.fragment(varSpec));
  }

  public UriTemplateBuilder label(VarSpec... varSpec) {
    return add(ExpressionBuilder.label(varSpec));
  }

  public UriTemplateBuilder path(VarSpec... varSpec) {
    return add(ExpressionBuilder.path(varSpec));
  }

  public UriTemplateBuilder parameter(VarSpec... varSpec) {
    return add(ExpressionBuilder.parameter(varSpec));
  }

  public UriTemplateBuilder query(VarSpec... varSpec) {
    return add(ExpressionBuilder.query(varSpec));
  }

  public UriTemplateBuilder continuation(VarSpec... varSpec) {
    return add(ExpressionBuilder.continuation(varSpec));
  }

  public UriTemplate build(){
    if(baseUrl != null){
      partials.add(0, Literal.wrap(baseUrl));
    }
    return new UriTemplate(partials, values);
  }
}
