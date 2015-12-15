package com.seanchenxi.gwt.uri.template;

import static com.seanchenxi.gwt.uri.template.StringPool.EMPTY;
import static com.seanchenxi.gwt.uri.template.StringPool.MODIFIER_EXPLODE;
import static com.seanchenxi.gwt.uri.template.StringPool.MODIFIER_PREFIX;

/**
 * <p>
 * URI Template Value Modifiers.
 * </p>
 *
 * @author Xi CHEN
 * @since 13/12/15
 * @see <a href="http://tools.ietf.org/html/rfc6570#section-2.4">Section 2.4 | RFC 6570</a>
 */
public enum Modifier {

  /**
   * Prefix Values
   * <pre>
   *   prefix        =  ":" max-length
   *   max-length    =  %x31-39 0*3DIGIT   ; positive integer < 10000
   * </pre>
   * @see <a href="http://tools.ietf.org/html/rfc6570#section-2.4.1">Section 2.4.1 | RFC 6570</a>
   */
  PREFIX(MODIFIER_PREFIX){
    @Override
    public boolean isPresent(String raw){
      return raw != null && raw.contains(getValue());
    }
  },

  /**
   * Composite Values
   * <pre>
   *   explode       =  "*"
   * </pre>
   * @see <a href="http://tools.ietf.org/html/rfc6570#section-2.4.2">Section 2.4.2 | RFC 6570</a>
   */
  EXPLODE(MODIFIER_EXPLODE){
    @Override
    public boolean isPresent(String raw){
      return raw != null && raw.trim().endsWith(getValue());
    }
  },

  NONE(EMPTY){
    @Override
    public boolean isPresent(String raw){
      for(Modifier m : values()){
        if(!m.equals(this) && m.isPresent(raw)){
          return false;
        }
      }
      return true;
    }
  };

  public static Modifier parseValue(String modifier){
    if(modifier == null){
      return NONE;
    }
    for(Modifier m : values()){
      if(m.value.equals(modifier)){
        return m;
      }
    }
    throw new IllegalArgumentException(modifier + " is not a valide modifier");
  }

  public static Modifier find(String raw){
    for(Modifier m : values()){
      if(m.isPresent(raw)){
        return m;
      }
    }
    return NONE;
  }

  private String value;

  Modifier(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  public abstract boolean isPresent(String raw);

}
