package org.example.utils;

public class MessageUtils {
  public static String formatMessage(String messageTemplate, Object... args) {
    return String.format(messageTemplate, args);
  }
}
