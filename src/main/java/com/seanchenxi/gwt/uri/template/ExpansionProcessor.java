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
public class ExpansionProcessor {

  public interface JoinFunction{
    String prefix(String current);
  }

  public static StringBuilder join(VarSpec.Value value, String separator, JoinFunction fn){
    StringBuilder builder = new StringBuilder();
    boolean isFirstSub = true;
    for(String varValue : value){
      if(!isFirstSub){
        builder.append(separator);
      }
      builder.append(fn.prefix(varValue)).append(varValue);
      isFirstSub = false;
    }
    return builder;
  }

  public static VarSpec.Value expand(Map value){
    return expand(value, IGNORE_MAX_LENGTH);
  }

  public static VarSpec.Value expand(Map value, boolean pair){
    if(value != null && pair){
      List<String> result = new ArrayList<String>();
      @SuppressWarnings("unchecked")
      Set<Map.Entry> entrySet = value.entrySet();
      for(Map.Entry entry : entrySet){
        String key = doPrint(entry.getKey());
        String keyValue = doPrint(entry.getValue());
        result.add(key + EQUAL + (keyValue == null ? EMPTY : keyValue));
      }
      return new VarSpec.Value(VarSpec.Value.Type.PAIR, result);
    }else{
      return expand(value);
    }
  }

  public static VarSpec.Value expand(Map value, int maxLength){
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
    return new VarSpec.Value(VarSpec.Value.Type.PAIR, result);
  }

  public static VarSpec.Value expand(Iterable iterable){
    return expand(iterable, IGNORE_MAX_LENGTH);
  }


  public static VarSpec.Value expand(Iterable iterable, int maxLength){
    List<String> result = new ArrayList<String>();
    for(Object item : iterable){
      addIfNotNull(result, doPrint(item, maxLength));

    }
    return new VarSpec.Value(VarSpec.Value.Type.LIST, result);
  }

  public static VarSpec.Value print(Object value) {
    return print(value, IGNORE_MAX_LENGTH);
  }

  public static VarSpec.Value print(Object object, int maxLength){
    return new VarSpec.Value(doPrint(object, maxLength));
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
