package com.seanchenxi.gwt.uri.template;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.seanchenxi.gwt.uri.template.StringPool.COMMA;
import static com.seanchenxi.gwt.uri.template.StringPool.EQUAL;
import static com.seanchenxi.gwt.uri.template.VarSpec.Expansion.Type.LIST;
import static com.seanchenxi.gwt.uri.template.VarSpec.Expansion.Type.PAIR;
import static com.seanchenxi.gwt.uri.template.VarSpec.Expansion.Type.STRING;

/**
 * <p>
 * Template expressions are the parameterized parts of a URI Template.
 * </p>
 * <p/>
 * <p>
 * Each expression contains an optional operator, which defines the
 * expression type and its corresponding expansion process, followed by
 * a comma-separated list of variable specifiers (variable names and optional value modifiers)
 * </p>
 * <p/>
 * <p>
 * If no operator is provided,
 * the expression defaults to simple variable expansion of unreserved values.
 * </p>
 * <p/>
 * <pre>
 * http://www.example.com/foo{?query,number}
 *                           \_____________/
 *                                 |
 *                                 |
 *          For each defined variable in [ 'query', 'number' ],
 *          substitute "?" if it is the first substitution or "&"
 *          thereafter, followed by the variable name, '=', and the
 *          variable's value.
 * </pre>
 * <p/>
 * <p>
 * This class models this representation and adds helper functions for replacement and reverse matching.
 * </p>
 *
 * @author Xi CHEN
 * @since 13/12/15
 */
public class Expression extends TemplatePartial {

  public static final String OPEN = "{";

  public static final String CLOSE = "}";

  /**
   * {@link Operator} defines the expression type and its corresponding expansion process
   */
  private Operator operator;

  /**
   * List of variable specifiers ({@link VarSpec}, variable names and optional value modifiers)
   */
  private List<VarSpec> varSpecs;

  public Expression(Operator operator, List<VarSpec> varSpecs) {
    if (varSpecs == null || varSpecs.size() < 1) {
      throw new IllegalArgumentException("Expression must contains at least one VarSpec");
    }
    this.operator = operator == null ? Operator.NUL : operator;
    this.varSpecs = new LinkedList<VarSpec>(varSpecs);
  }

  public Operator getOperator() {
    return operator;
  }

  public List<VarSpec> getVarSpecs() {
    return varSpecs;
  }

  @Override
  public String compile(Map<String, Object> values) {
    StringBuilder builder = new StringBuilder();
    boolean isFirst = true;
    for (VarSpec varSpec : varSpecs) {
      String varName = varSpec.getName();
      Object object = values.get(varName);
      if (object == null) {
        continue;
      }

      builder.append(isFirst ? operator.getFirst() : operator.getSep());

      VarSpec.Expansion expansion = varSpec.expand(object);
      if (expansion.is(STRING)) {
        if (operator.isNamed()) {
          builder.append(varName);
        }
        builder.append(expansion.isEmpty() ? operator.getIfemp() : EQUAL).append(expansion.getValue());
      } else if (!varSpec.isExplode()) {
        if (operator.isNamed()) {
          builder.append(varName);
        }
        builder.append(expansion.isEmpty() ? operator.getIfemp() : EQUAL);
        boolean isFirstSub = true;
        for (String varValue : expansion) {
          if (!isFirstSub) {
            builder.append(COMMA);
          }
          builder.append(varValue);
          isFirstSub = false;
        }
      } else {
        if (operator.isNamed()) {
          boolean isFirstSub = true;
          for (String varValue : expansion) {
            if (!isFirstSub) {
              builder.append(operator.getSep());
            }
            if (expansion.is(LIST)) {
              builder.append(varName)
                  .append(varValue.isEmpty() ? operator.getIfemp() : EQUAL)
                  .append(varValue);
            } else if (expansion.is(PAIR)) {
              builder.append(varValue);
            }
            isFirstSub = false;
          }
        } else {
          for (String varValue : expansion) {
            builder.append(varValue);
          }
        }
      }
      isFirst = false;
    }
    return builder.toString();
  }

  @Override
  public String template() {
    StringBuilder sb = new StringBuilder(Expression.OPEN).append(operator);
    Iterator<VarSpec> iterator = varSpecs.iterator();
    while (iterator.hasNext()) {
      sb.append(iterator.next());
      if (iterator.hasNext()) {
        sb.append(VarSpec.SEPARATOR);
      }
    }
    return sb.append(Expression.CLOSE).toString();
  }

}