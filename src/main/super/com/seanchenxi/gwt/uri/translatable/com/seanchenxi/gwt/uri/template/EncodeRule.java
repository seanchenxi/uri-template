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

import com.google.gwt.http.client.URL;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 * Expression encode rule.
 * </p>
 *
 * @author Xi CHEN
 * @since 13/12/15.
 * @see <a href="http://tools.ietf.org/html/rfc6570#appendix-A">Implementation Hints | RFC 6570</a>
 */
public enum EncodeRule {
  /**
   * means any character not in the unreserved set will be encoded
   */
  U {
    @Override
    public String encode(String input) {
      return doEncode(input, false);
    }
  },

  /**
   * means any character not in the union of (unreserved / reserved / pct-encoding) will be encoded
   */
  U_R {
    @Override
    public String encode(String input) {
      return doEncode(input, true);
    }
  };

  private static final int CASE_DIFF = ('a' - 'A');
  /**
   * a-z && A-Z && 0-9 && -._~
   */
  private static final Set<Character> UNRESERVED;
  /**
   * :/?#[]@!$&'()*+,;=
   */
  private static final Set<Character> RESERVED;

  static{
    Set<Character> set = new LinkedHashSet<Character>();
    char i;
    for (i = 'a'; i <= 'z'; i++) {
      set.add(i);
    }
    for (i = 'A'; i <= 'Z'; i++) {
      set.add(i);
    }
    for (i = '0'; i <= '9'; i++) {
      set.add(i);
    }
    set.addAll(Arrays.asList('-', '.', '_', '~'));
    UNRESERVED = Collections.unmodifiableSet(set);
    RESERVED = Collections.unmodifiableSet(new LinkedHashSet<Character>(Arrays.asList(
        ':', '/', '?', '#', '[', ']', '@',
        '!', '$', '&', '\'', '(', ')', '*',
        '+', ',', ';', '='
    )));
  }

  EncodeRule() {
  }

  public abstract String encode(String input);

  protected String doEncode(String input, boolean passReserved) {
    return passReserved ? URL.encodeQueryString(input).replace("!", "%21") : URL.encode(input);
  }

}
