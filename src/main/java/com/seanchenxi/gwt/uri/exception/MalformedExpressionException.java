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

package com.seanchenxi.gwt.uri.exception;

/**
 * @author Xi CHEN
 * @since 13/12/15.
 */
public class MalformedExpressionException extends RuntimeException {

  private int position;

  public MalformedExpressionException(String message, int position, Throwable throwable) {
    super(message, throwable);
    this.position = position;
  }

  public MalformedExpressionException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public MalformedExpressionException(String message) {
    super(message);
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public int getPosition() {
    return position;
  }
}
