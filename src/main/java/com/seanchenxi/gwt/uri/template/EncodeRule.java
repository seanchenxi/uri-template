package com.seanchenxi.gwt.uri.template;

/**
 * <p>
 * Expression encode rule.
 * </p>
 *
 * @author Xi CHEN
 * @since 13/12/15.
 * @see <a href="http://tools.ietf.org/html/rfc6570#appendix-A">Implementation Hints | RFC 6570</a>
 */
public enum EncodeRule {
  /**
   * means any character not in the unreserved set will be encoded
   */
  U,

  /**
   * means any character not in the union of (unreserved / reserved / pct-encoding) will be encoded
   */
  U_R
}
