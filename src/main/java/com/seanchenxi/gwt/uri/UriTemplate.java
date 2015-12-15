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
public class UriTemplate {

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

  public String getTemplate(){
    if(this.template == null){
      StringBuilder sb = new StringBuilder();
      for(TemplatePartial partial : partials){
        sb.append(partial);
      }
      return this.template = sb.toString();
    }
    return this.template;
  }

  public String compile(){
    Map<String, Object> values = Collections.unmodifiableMap(this.values);
    StringBuilder sb = new StringBuilder();
    for(TemplatePartial partial : partials){
      sb.append(partial.compile(values));
    }
    return sb.toString();
  }

}
