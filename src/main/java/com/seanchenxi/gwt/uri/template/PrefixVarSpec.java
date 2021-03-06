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

/**
 * @author Xi CHEN
 * @since 14/12/15.
 */
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
  public String template() {
    return super.template() + maxLength;
  }

  @Override
  public Value expand(Map<String, Object> values){
    Object value = values == null ? null : values.get(getName());
    if(value == null){
      return null;
    }else if(value instanceof Map){
      return ExpansionProcessor.expand(getName(), (Map) value, maxLength);
    }else if(value instanceof Iterable){
      return ExpansionProcessor.expand(getName(),(Iterable) value, maxLength);
    }else{
      return ExpansionProcessor.print(getName(), value, maxLength);
    }
  }

}