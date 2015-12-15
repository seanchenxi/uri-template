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

import java.util.Map;

public class PrefixVarSpec extends VarSpec {

  static final int IGNORE_MAX_LENGTH = 0;

  private int maxLength;

  public PrefixVarSpec(String name, int maxLength) {
    super(name, Modifier.PREFIX);
    this.setMaxLength(maxLength);
  }

  public void setMaxLength(int maxLength) {
    if(maxLength < 0 || maxLength > 10000){
      throw new IllegalArgumentException("Max length must be positive and < 10000");
    }
    this.maxLength = maxLength;
  }

  public int getMaxLength() {
    return maxLength;
  }

  @Override
  public Expansion expand(Object value){
    if(value == null){
      return null;
    }else if(value instanceof Map){
      return ExpansionUtils.expand((Map) value, maxLength);
    }else if(value instanceof Iterable){
      return ExpansionUtils.expand((Iterable) value, maxLength);
    }else{
      return ExpansionUtils.print(value, maxLength);
    }
  }

  @Override
  public String toString() {
    return super.toString() + maxLength;
  }
}