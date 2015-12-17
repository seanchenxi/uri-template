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

import java.util.HashSet;
import java.util.Set;

import static com.seanchenxi.gwt.uri.template.EncodeRule.U;
import static com.seanchenxi.gwt.uri.template.EncodeRule.U_R;
import static com.seanchenxi.gwt.uri.template.StringPool.AMPERSAND;
import static com.seanchenxi.gwt.uri.template.StringPool.CROSSHATCH;
import static com.seanchenxi.gwt.uri.template.StringPool.DOT;
import static com.seanchenxi.gwt.uri.template.StringPool.EMPTY;
import static com.seanchenxi.gwt.uri.template.StringPool.EQUAL;
import static com.seanchenxi.gwt.uri.template.StringPool.PLUS;
import static com.seanchenxi.gwt.uri.template.StringPool.QUESTION_MARK;
import static com.seanchenxi.gwt.uri.template.StringPool.SEMICOLON;
import static com.seanchenxi.gwt.uri.template.StringPool.SEPARATOR_DEFAULT;
import static com.seanchenxi.gwt.uri.template.StringPool.SLASH;

/**
 * <p>
 * URI Template Operator.
 * </p>
 * <pre>
 * .------------------------------------------------------------------.
 * |          NUL     +      .       /       ;      ?      &      #   |
 * |------------------------------------------------------------------|
 * | first |  ""     ""     "."     "/"     ";"    "?"    "&"    "#"  |
 * | sep   |  ","    ","    "."     "/"     ";"    "&"    "&"    ","  |
 * | named | false  false  false   false   true   true   true   false |
 * | ifemp |  ""     ""     ""      ""      ""     "="    "="    ""   |
 * | allow |   U     U+R     U       U       U      U      U     U+R  |
 * `------------------------------------------------------------------'
 *  </pre>
 *
 * @author Xi CHEN
 * @since 13/12/15
 * @see <a href="http://tools.ietf.org/html/rfc6570#appendix-A">Implementation Hints | RFC 6570</a>
 */
public enum Operator {

  /**
   * Simple String Expansion: {var}
   * @since Level-1
   * @see <a href="http://tools.ietf.org/html/rfc6570#section-3.2.2">Section 3.2.2 | RFC 6570</a>
   */
  NUL(EMPTY, SEPARATOR_DEFAULT, false, EMPTY, U),

  /**
   * Reserved Expansion: {+var}
   * @since Level-2
   * @see <a href="http://tools.ietf.org/html/rfc6570#section-3.2.3">Section 3.2.3 | RFC 6570</a>
   */
  RESERVED(PLUS, EMPTY, SEPARATOR_DEFAULT, false, EMPTY, U_R),

  /**
   * Fragment Expansion: {#var}
   * @since Level-2
   * @see <a href="http://tools.ietf.org/html/rfc6570#section-3.2.4">Section 3.2.4 | RFC 6570</a>
   */
  FRAGMENT(CROSSHATCH, SEPARATOR_DEFAULT, false, EMPTY, U_R),

  /**
   * Label Expansion with Dot-Prefix: {.var}
   * @since Level-3
   * @see <a href="http://tools.ietf.org/html/rfc6570#section-3.2.5">Section 3.2.5 | RFC 6570</a>
   */
  LABEL(DOT, DOT, false, EMPTY, U),

  /**
   * Path Segment Expansion: {/var}
   * @since Level-3
   * @see <a href="http://tools.ietf.org/html/rfc6570#section-3.2.6">Section 3.2.6 | RFC 6570</a>
   */
  PATH(SLASH, SLASH, false, EMPTY, U),

  /**
   * Path-Style Parameter Expansion: {;var}
   * @since Level-3
   * @see <a href="http://tools.ietf.org/html/rfc6570#section-3.2.7">Section 3.2.7 | RFC 6570</a>
   */
  PARAMETER(SEMICOLON, SEMICOLON, true, EMPTY, U),

  /**
   * Form-Style Query Expansion: {?var}
   * @since Level-3
   * @see <a href="http://tools.ietf.org/html/rfc6570#section-3.2.8">Section 3.2.8 | RFC 6570</a>
   */
  QUERY(QUESTION_MARK, AMPERSAND, true, EQUAL, U),

  /**
   * Form-Style Query Continuation: {&var}
   * @since Level-3
   * @see <a href="http://tools.ietf.org/html/rfc6570#section-3.2.9">Section 3.2.9 | RFC 6570</a>
   */
  CONTINUATION(AMPERSAND, AMPERSAND, true, EQUAL, U);

  private interface Available {
    Set<String> SET = new HashSet<String>();
  }

  public static Operator parseValue(String operator){
    if(operator != null && !operator.trim().isEmpty()){
      for(Operator op : values()){
        if(op.sign.equals(operator)){
          return op;
        }
      }
    }
    throw new IllegalArgumentException(operator + " is not a valide operator.");
  }

  public static boolean isValide(String operator){
    return Available.SET.contains(operator);
  }

  private String sign;
  private String first;
  private final String sep;
  private final boolean named;
  private final String ifemp;
  private final EncodeRule allow;

  Operator(String first, String sep, boolean named, String ifemp, EncodeRule allow) {
    this(first, first, sep, named, ifemp, allow);
  }

  Operator(String sign, String first, String sep, boolean named, String ifemp, EncodeRule allow) {
    this.sign = sign;
    this.first = first;
    this.sep = sep;
    this.named = named;
    this.ifemp = ifemp;
    this.allow = allow;
    Available.SET.add(sign);
  }

  public String getSign() {
    return sign;
  }

  public String getFirst() {
    return first;
  }

  public String getSep() {
    return sep;
  }

  public boolean isNamed() {
    return named;
  }

  public String getIfemp() {
    return ifemp;
  }

  public EncodeRule getAllow() {
    return allow;
  }

  @Override
  public String toString() {
    return this.sign;
  }
}