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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.seanchenxi.gwt.uri.template.StringPool.COMMA;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

/**
 * @author Xi CHEN
 * @since 13/12/15.
 */
public class VarSpec extends TemplatePartial<VarSpec.Value> {

  public static class Value<T> implements Iterable<T> {

    enum Type {
      STRING, LIST, PAIR
    }

    private Type type;
    private List<T> values;
    private String name;

    Value(String name, Type type, List<T> values) {
      this.name = name;
      this.type = type;
      this.values = unmodifiableList(values == null ? new ArrayList<T>() : values);
    }

    public Value(String name, T item) {
      this(name, Type.STRING, item == null ? null : singletonList(item));
    }

    public String getName() {
      return name;
    }

    private void setName(String name) {
      this.name = name;
    }

    boolean is(Type type){
      return this.type.equals(type);
    }

    Type getType() {
      return type;
    }

    public boolean isEmpty() {
      return values.isEmpty() || (is(Type.STRING) && String.valueOf(values.get(0)).isEmpty());
    }

    @Override
    public Iterator<T> iterator() {
      return values.iterator();
    }

    @Override
    public String toString() {
      return "Value{" +
          "type=" + type +
          ", values=" + values +
          ", name='" + name + '\'' +
          '}';
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

  public boolean is(Modifier modifier) {
    return this.modifier.equals(modifier);
  }

  @Override
  public String template() {
    return this.name + this.modifier;
  }

  @Override
  public Value expand(Map<String, Object> values) {
    Object value = values == null ? null : values.get(name);
    if (value == null) {
      return null;
    } else if (value instanceof Map) {
      boolean pair = Modifier.EXPLODE.equals(this.modifier);
      return ExpansionProcessor.expand(name, (Map) value, pair);
    } else if (value instanceof Iterable) {
      return ExpansionProcessor.expand(name, (Iterable) value);
    } else {
      return ExpansionProcessor.expand(name, value);
    }
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
