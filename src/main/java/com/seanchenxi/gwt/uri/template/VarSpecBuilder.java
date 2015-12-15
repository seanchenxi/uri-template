package com.seanchenxi.gwt.uri.template;

import com.seanchenxi.gwt.uri.exception.MalformedExpressionException;

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
