package net.jangaroo.jooc.util;

/**
 * A better {@link java.text.MessageFormat} that takes an arbitrary number of parameters.
 * This is to avoid a pitfall in the original MessageFormat: if you call {@link java.text.MessageFormat#format(Object)}
 * you'd assume that the parameter is a single argument. Instead, it has to be an <code>Object[]</code> like
 * in the old times. This is unintuitive, as {@link java.text.MessageFormat#format(String, Object...)}
 * works in the modern way.
 *
 * @see java.text.MessageFormat
 */
public class MessageFormat {

  private java.text.MessageFormat messageFormat;

  public MessageFormat(String pattern) {
    messageFormat = new java.text.MessageFormat(pattern);
  }

  public String format(Object ...arguments) {
    return messageFormat.format(arguments);
  }

}
