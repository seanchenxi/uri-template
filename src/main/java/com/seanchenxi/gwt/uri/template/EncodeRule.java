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

import java.io.CharArrayWriter;
import java.nio.charset.Charset;
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

  /**
   * UTF-8 charset is required for encoding
   */
  private static final Charset CHARSET;
  static{
    CHARSET = Charset.forName("UTF8");
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

    StringBuffer out = new StringBuffer(input.length());

    CharArrayWriter charArrayWriter = new CharArrayWriter();
    for (int i = 0; i < input.length();) {
      char c = input.charAt(i);
      if (!needEncode(c, passReserved)) {
        out.append(c);
        i++;
      } else {
        do {
          charArrayWriter.write(c);
          if (c >= 0xD800 && c <= 0xDBFF) {
            if ( (i+1) < input.length()) {
              int d = (int) input.charAt(i+1);
              if (d >= 0xDC00 && d <= 0xDFFF) {
                charArrayWriter.write(d);
                i++;
              }
            }
          }
          i++;
        } while (i < input.length() && needEncode((c = input.charAt(i)), passReserved));
        charArrayWriter.flush();

        byte[] byteArray = new String(charArrayWriter.toCharArray()).getBytes(CHARSET);
        for (byte onrByte : byteArray) {
          out.append('%');
          char ch = Character.forDigit((onrByte >> 4) & 0xF, 16);
          if (Character.isLetter(ch)) {
            ch -= CASE_DIFF;
          }
          out.append(ch);
          ch = Character.forDigit(onrByte & 0xF, 16);
          if (Character.isLetter(ch)) {
            ch -= CASE_DIFF;
          }
          out.append(ch);
        }
        charArrayWriter.reset();
      }
    }
    return out.toString();
  }

  protected boolean needEncode(char c, boolean passReserved){
    return !UNRESERVED.contains(c) && (!passReserved || !RESERVED.contains(c));
  }
}
