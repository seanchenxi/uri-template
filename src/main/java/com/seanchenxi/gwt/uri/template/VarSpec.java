package com.seanchenxi.gwt.uri.template;

import com.seanchenxi.gwt.uri.exception.MalformedExpressionException;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.seanchenxi.gwt.uri.template.StringPool.COMMA;

/**
 * @author Xi CHEN
 * @since 13/12/15.
 */
public class VarSpec {

  static class Expansion implements Iterable<String> {

    enum Type {
      STRING, LIST, PAIR
    }

    private Type type;
    private List<String> values;

    Expansion(Type type, List<String> values) {
      this.type = type;
      this.values = Collections.unmodifiableList(values);
    }

    public Expansion(String item) {
      this(Type.STRING, item == null ? Collections.<String>emptyList() : Collections.singletonList(item));
    }

    boolean is(Type type){
      return this.type.equals(type);
    }

    Type getType() {
      return type;
    }

    List<String> getValues() {
      return values;
    }

    public String getValue() {
      return values.isEmpty() ? null : values.get(0);
    }

    @Override
    public Iterator<String> iterator() {
      return values.iterator();
    }

    public boolean isEmpty() {
      return values.isEmpty() || (this.type.equals(Type.STRING) && values.get(0).isEmpty());
    }
  }

  static final String SEPARATOR = COMMA;

  private String name;
  private Modifier modifier = Modifier.NONE;

  public VarSpec(String name) {
    this(name, Modifier.NONE);
  }

  protected VarSpec(String name, Modifier modifier) {
    this.name = validateName(name);
    this.modifier = modifier;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Modifier getModifier() {
    return modifier;
  }

  public Expansion expand(Object value){
    if(value == null){
      return null;
    }else if(value instanceof Map){
      boolean pair = Modifier.EXPLODE.equals(this.modifier);
      return ExpansionUtils.expand((Map) value, pair);
    }else if(value instanceof Iterable){
      return ExpansionUtils.expand((Iterable) value);
    }else{
      return ExpansionUtils.print(value);
    }
  }

  public boolean isExplode() {
    return Modifier.EXPLODE.equals(this.modifier);
  }

  @Override
  public String toString() {
    return this.name + this.modifier;
  }

  private String validateName(String name) {
    String validate;
    if(name == null || (validate = name.trim()).isEmpty()){
      throw new IllegalArgumentException("The variable name \"" + name + "cannot be null nor empty");
    }
    if(validate.contains(" ")){
      throw new MalformedExpressionException("The variable name \"" + name + "\" cannot contain spaces (leading or trailing)");
    }
    if(!name.matches("([\\w\\_\\.]|%[A-Fa-f0-9]{2})+")){ //TODO make this to be GWT friendly
      throw new MalformedExpressionException("The variable name " + name + " contains invalid characters");
    }
    return validate;
  }
}
