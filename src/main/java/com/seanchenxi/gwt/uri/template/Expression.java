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

package com.seanchenxi.gwt.uri.template;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.seanchenxi.gwt.uri.template.StringPool.COMMA;
import static com.seanchenxi.gwt.uri.template.StringPool.EMPTY;
import static com.seanchenxi.gwt.uri.template.StringPool.EQUAL;
import static com.seanchenxi.gwt.uri.template.VarSpec.Value.Type.LIST;
import static com.seanchenxi.gwt.uri.template.VarSpec.Value.Type.PAIR;
import static com.seanchenxi.gwt.uri.template.VarSpec.Value.Type.STRING;

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
public class Expression extends TemplatePartial<String> {

  public static final String OPEN = "{";

  public static final String CLOSE = "}";

  public static final String ESCAPED_OPEN = "\\{";

  public static final String ESCAPED_CLOSE = "\\}";

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

  @Override
  public String expand(Map<String, Object> values) {
    StringBuilder builder = new StringBuilder();
    boolean isFirst = true;
    for (final VarSpec varSpec : varSpecs) {
      VarSpec.Value valueParts = varSpec.expand(values);
      if (valueParts == null) {
        continue;
      }
      boolean isNamed = operator.isNamed();
      boolean isEmpty = valueParts.isEmpty();
      boolean isExplode = varSpec.is(Modifier.EXPLODE);
      if(isNamed || !isEmpty){
        builder.append(isFirst ? operator.getFirst() : operator.getSep());
      }

      if (valueParts.is(STRING) || !isExplode) {
        if(isNamed){
          builder.append(varSpec.getName());
        }
        if(valueParts.isEmpty()){
          builder.append(operator.getIfemp());
        }else{
          builder.append(EQUAL).append(valueParts.join(COMMA));
        }
      } else {
        if (isNamed) {
          if(isEmpty){
            builder.append(varSpec.getName()).append(operator.getIfemp());
          }else if (valueParts.is(LIST)) {
            VarSpec.JoinFunction joinFn = new VarSpec.JoinFunction() {
              @Override
              public String prefix(String current) {
                return varSpec.getName() + (current.isEmpty() ? operator.getIfemp() : EQUAL);
              }
            };
            builder.append(valueParts.join(operator.getSep(), joinFn));
          }else if (valueParts.is(PAIR)) {
            builder.append(valueParts.join(operator.getSep()));
          }
        } else {
          builder.append(valueParts.join(operator.getSep()));
        }
      }
      isFirst = false;
    }

    return builder.length() > 1 ? builder.toString() : EMPTY;
  }

}