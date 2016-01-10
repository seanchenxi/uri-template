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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.seanchenxi.gwt.uri.template.PrefixVarSpec.IGNORE_MAX_LENGTH;
import static com.seanchenxi.gwt.uri.template.StringPool.EMPTY;
import static com.seanchenxi.gwt.uri.template.StringPool.EQUAL;

/**
 * @author Xi CHEN
 * @since 14/12/15.
 */
class ExpansionProcessor {

  static StringBuilder joinVar(VarSpec.Value value, Operator operator, boolean named){
    return joinVar(value, operator, null, named);
  }

  static StringBuilder joinVar(VarSpec.Value value, Operator operator, String separator){
    return joinVar(value, operator, separator, false);
  }

  static StringBuilder joinVar(VarSpec.Value value, Operator operator, String separator, boolean named){
    String name = value.getName();
    String whenEmpty = operator.getIfemp();
    EncodeRule encodeRule = operator.getAllow();
    String sep = separator == null ? operator.getSep() : separator;
    StringBuilder builder = new StringBuilder();
    boolean isFirstSub = true;
    for(final Object varValue : value){
      if(!isFirstSub){
        builder.append(sep);
      }
      if(varValue instanceof List){
        @SuppressWarnings("unchecked")
        List<String> pair = (List<String>) varValue;
        assert pair.size() == 2;
        builder.append(encodeRule.encode(pair.get(0))).append(EQUAL).append(encodeRule.encode(pair.get(1)));
      }else if(varValue instanceof String){
        String varVal = (String) varValue;
        boolean empty = varVal.isEmpty();
        if(named){
          builder.append(name).append(empty ? whenEmpty : EQUAL);
        }
        builder.append(encodeRule.encode(varVal));
      }

      isFirstSub = false;
    }
    return builder;
  }

  public static VarSpec.Value expand(String name, Map value){
    return expand(name, value, IGNORE_MAX_LENGTH);
  }

  public static VarSpec.Value expand(String name, Map value, boolean pair){
    if(value != null && pair){
      List<List<String>> result = new ArrayList<List<String>>();
      @SuppressWarnings("unchecked")
      Set<Map.Entry> entrySet = value.entrySet();
      for(Map.Entry entry : entrySet){
        String key = doPrint(entry.getKey());
        String keyValue = doPrint(entry.getValue());
        result.add(Arrays.asList(key, (keyValue == null ? EMPTY : keyValue)));
      }
      return new VarSpec.Value<List<String>>(name, VarSpec.Value.Type.PAIR, result);
    }else{
      return expand(name, value);
    }
  }

  public static VarSpec.Value expand(String name, Map value, int maxLength){
    if(value == null){
      return null;
    }
    List<String> result = new ArrayList<String>();
    @SuppressWarnings("unchecked")
    Set<Map.Entry> entrySet = value.entrySet();
    for(Map.Entry entry : entrySet){
      addIfNotNull(result, doPrint(entry.getKey(), maxLength));
      addIfNotNull(result, doPrint(entry.getValue(), maxLength));
    }
    return new VarSpec.Value<String>(name, VarSpec.Value.Type.PAIR, result);
  }

  public static VarSpec.Value expand(String name, Iterable iterable){
    return expand(name, iterable, IGNORE_MAX_LENGTH);
  }


  public static VarSpec.Value expand(String name, Iterable iterable, int maxLength){
    List<String> result = new ArrayList<String>();
    for(Object item : iterable){
      addIfNotNull(result, doPrint(item, maxLength));

    }
    return new VarSpec.Value<String>(name, VarSpec.Value.Type.LIST, result);
  }

  public static VarSpec.Value expand(String name, Object value) {
    return print(name, value, IGNORE_MAX_LENGTH);
  }

  public static VarSpec.Value print(String name, Object object, int maxLength){
    return new VarSpec.Value<String>(name, doPrint(object, maxLength));
  }

  private static String doPrint(Object value) {
    return doPrint(value, IGNORE_MAX_LENGTH);
  }

  private static String doPrint(Object object, int maxLength){
    if(object == null){
      return null;
    }
    String value = String.valueOf(object);
    return maxLength > 0 ? value.substring(0, Math.min(value.length(), maxLength)) : value;
  }

  private static void addIfNotNull(List<String> result, String value) {
    if(value != null){
      result.add(value.trim());
    }
  }
}
