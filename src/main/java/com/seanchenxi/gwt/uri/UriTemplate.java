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

package com.seanchenxi.gwt.uri;

import com.seanchenxi.gwt.uri.template.TemplatePartial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Xi CHEN
 * @since 13/12/15.
 */
public class UriTemplate extends TemplatePartial<String> {

  private List<TemplatePartial> partials = new ArrayList<TemplatePartial>();

  private Map<String, Object> values = new HashMap<String, Object>();

  private String template;

  public UriTemplate(List<TemplatePartial> partials, Map<String, Object> values) {
    this.partials = partials;
    this.values = values;
  }

  public List<TemplatePartial> getPartials() {
    return partials;
  }

  public void setPartials(List<TemplatePartial> partials) {
    this.partials = partials;
  }

  public Map<String, Object> getValues() {
    return values;
  }

  public void setValues(Map<String, Object> values) {
    this.values = values;
  }

  public String expand(){
    return expand(Collections.unmodifiableMap(this.values));
  }

  @Override
  public String expand(Map<String, Object> values){
    StringBuilder sb = new StringBuilder();
    for(TemplatePartial partial : partials){
      sb.append(partial.expand(values));
    }
    return sb.toString();
  }

  @Override
  public String template(){
    if(this.template == null){
      StringBuilder sb = new StringBuilder();
      for(TemplatePartial partial : partials){
        sb.append(partial);
      }
      return this.template = sb.toString();
    }
    return this.template;
  }

}
