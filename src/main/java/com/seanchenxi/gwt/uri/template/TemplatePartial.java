package com.seanchenxi.gwt.uri.template;

import java.util.Map;

/**
 * @author Xi CHEN
 * @since 13/12/15.
 */
public abstract class TemplatePartial {

  /**
   * The expression position in the URI template
   */
  private int position;

  public int getPosition() {
    return this.position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return template();
  }

  public abstract String compile(Map<String, Object> values);

  public abstract String template();

}
