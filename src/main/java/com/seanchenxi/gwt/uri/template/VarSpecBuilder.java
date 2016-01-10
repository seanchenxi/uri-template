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

import com.seanchenxi.gwt.uri.template.exception.MalformedExpressionException;

/**
 * @author Xi CHEN
 * @since 14/12/15.
 */
public class VarSpecBuilder {

  public static VarSpec var(String raw){
    if(raw == null || raw.trim().isEmpty()){
      return null;
    }
    Modifier modifier = Modifier.find(raw);
    switch (modifier){
      case PREFIX:
        String[] pair = raw.split(modifier.getValue());
        try{
          int maxLength = Integer.parseInt(pair[1]);
          if(maxLength > 10000 || maxLength < 1){
            throw new MalformedExpressionException("Wrong prefix modifier max length, it should be positive and < 10000");
          }
          return var(pair[0], maxLength);
        }catch (NumberFormatException e){
          throw new MalformedExpressionException("Wrong prefix modifier max length", e);
        }
      case EXPLODE:
        String varName = raw.substring(0, raw.length() - 1);
        return var(varName, true);
      case NONE:
      default:
        return new VarSpec(raw, Modifier.NONE);
    }
  }

  public static VarSpec explode(String name){
    return new ExplodeVarSpec(name);
  }

  public static VarSpec var(String name, int prefix){
    return new PrefixVarSpec(name, prefix);
  }

  public static VarSpec var(String name, boolean explode){
    return explode ? explode(name) : var(name);
  }

}
