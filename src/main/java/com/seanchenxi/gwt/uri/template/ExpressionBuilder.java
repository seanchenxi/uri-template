package com.seanchenxi.gwt.uri.template;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Xi CHEN
 * @since 13/12/15.
 */
public class ExpressionBuilder {

  public static ExpressionBuilder create(Operator operator, VarSpec... varSpecs) {
    if(varSpecs == null || varSpecs.length < 1){
      throw new IllegalArgumentException("Expression must contains at least one VarSpec");
    }
    ExpressionBuilder expressionBuilder = new ExpressionBuilder();
    expressionBuilder.operator = operator == null ? Operator.NUL : operator;
    expressionBuilder.varSpecs = new LinkedList<VarSpec>(Arrays.asList(varSpecs));
    return expressionBuilder;
  }

  public static ExpressionBuilder create(String expression) {
    String token = expression == null ? StringPool.EMPTY : expression.trim();
    if(!token.startsWith(Expression.OPEN) || !token.endsWith(Expression.CLOSE)) {
      throw new IllegalArgumentException("Expression \""+expression+"\" should begin with \"" + Expression.OPEN +
          "\" and close with \"" + Expression.CLOSE + "\"");
    }

    token = token.substring(1, token.length() - 1);
    ExpressionBuilder builder = new ExpressionBuilder();

    // Check for URI operators
    String firstChar = token.substring(0, 1);
    if (Operator.isValide(firstChar)) {
      builder.operator = Operator.parseValue(firstChar);
      token = token.substring(1);
    } else {
      builder.operator = Operator.NUL;
    }

    builder.varSpecs = new LinkedList<VarSpec>();
    for (String varSpecRaw : token.split(VarSpec.SEPARATOR)) {
      builder.varSpec(VarSpecBuilder.var(varSpecRaw));
    }
    return builder;
  }

  public static ExpressionBuilder simple(VarSpec... varSpec) {
    return ExpressionBuilder.create(Operator.NUL, varSpec);
  }

  public static ExpressionBuilder reserved(VarSpec... varSpec) {
    return ExpressionBuilder.create(Operator.RESERVED, varSpec);
  }

  public static ExpressionBuilder fragment(VarSpec... varSpec) {
    return ExpressionBuilder.create(Operator.FRAGMENT, varSpec);
  }

  public static ExpressionBuilder label(VarSpec... varSpec) {
    return ExpressionBuilder.create(Operator.LABEL, varSpec);
  }

  public static ExpressionBuilder path(VarSpec... varSpec) {
    return ExpressionBuilder.create(Operator.PATH, varSpec);
  }

  public static ExpressionBuilder parameter(VarSpec... varSpec) {
    return ExpressionBuilder.create(Operator.PARAMETER, varSpec);
  }

  public static ExpressionBuilder query(VarSpec... varSpec) {
    return ExpressionBuilder.create(Operator.QUERY, varSpec);
  }

  public static ExpressionBuilder continuation(VarSpec... varSpec) {
    return ExpressionBuilder.create(Operator.CONTINUATION, varSpec);
  }

  protected Operator operator;

  protected List<VarSpec> varSpecs;

  private ExpressionBuilder() {
  }

  public ExpressionBuilder varSpec(VarSpec varSpec) {
    varSpecs.add(varSpec);
    return this;
  }

  public Expression build() {
    return new Expression(operator, varSpecs);
  }

}
