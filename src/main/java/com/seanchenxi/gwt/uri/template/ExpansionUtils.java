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
public class ExpansionUtils {

  public static VarSpec.Expansion expand(Map value){
    return expand(value, IGNORE_MAX_LENGTH);
  }

  public static VarSpec.Expansion expand(Map value, boolean pair){
    if(value != null && pair){
      List<String> result = new ArrayList<String>();
      @SuppressWarnings("unchecked")
      Set<Map.Entry> entrySet = value.entrySet();
      for(Map.Entry entry : entrySet){
        String key = doPrint(entry.getKey());
        String keyValue = doPrint(entry.getValue());
        result.add(key + EQUAL + (keyValue == null ? EMPTY : keyValue));
      }
      return new VarSpec.Expansion(VarSpec.Expansion.Type.PAIR, result);
    }else{
      return expand(value);
    }
  }

  public static VarSpec.Expansion expand(Map value, int maxLength){
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
    return new VarSpec.Expansion(VarSpec.Expansion.Type.PAIR, result);
  }

  public static VarSpec.Expansion expand(Iterable iterable){
    return expand(iterable, IGNORE_MAX_LENGTH);
  }


  public static VarSpec.Expansion expand(Iterable iterable, int maxLength){
    List<String> result = new ArrayList<String>();
    for(Object item : iterable){
      addIfNotNull(result, doPrint(item, maxLength));

    }
    return new VarSpec.Expansion(VarSpec.Expansion.Type.LIST, result);
  }

  public static VarSpec.Expansion print(Object value) {
    return print(value, IGNORE_MAX_LENGTH);
  }

  public static VarSpec.Expansion print(Object object, int maxLength){
    return new VarSpec.Expansion(doPrint(object, maxLength));
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
