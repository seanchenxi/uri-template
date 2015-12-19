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

import static com.seanchenxi.gwt.uri.template.ExpansionProcessor.joinVar;
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
    String separator = operator.getSep();
    String whenEmpty = operator.getIfemp();
    String whenFirst = operator.getFirst();
    boolean isNamed = operator.isNamed();
    boolean isFirst = true;

    StringBuilder builder = new StringBuilder();
    for (final VarSpec varSpec : varSpecs) {
      VarSpec.Value valueParts = varSpec.expand(values);
      if (valueParts == null) {
        continue;
      }

      boolean isString = valueParts.is(STRING);
      boolean isEmpty = valueParts.isEmpty();
      boolean isExplode = varSpec.is(Modifier.EXPLODE);
      if(isNamed || !isEmpty || isString){
        builder.append(isFirst ? whenFirst : separator);
      }

      if (isString || !isExplode) {
        if(!isString && isEmpty){
          continue;
        }
        if(isNamed){
          builder.append(varSpec.getName());
        }
        if(isEmpty){
          builder.append(whenEmpty);
        }else if(isNamed){
          builder.append(EQUAL);
        }
        builder.append(joinVar(valueParts, operator, COMMA));
      } else {
        if (isNamed) {
          if(isEmpty){
            continue;
          }else if (valueParts.is(LIST)) {
            builder.append(joinVar(valueParts, operator, true));
          }else if (valueParts.is(PAIR)) {
            builder.append(joinVar(valueParts, operator, false));
          }
        } else {
          builder.append(joinVar(valueParts, operator, false));
        }
      }
      isFirst = false;
    }

    return builder.length() > 1 || !isNamed ? builder.toString() : EMPTY;
  }

}