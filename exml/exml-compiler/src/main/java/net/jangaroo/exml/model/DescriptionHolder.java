package net.jangaroo.exml.model;

public class DescriptionHolder {
  private String description;

  public DescriptionHolder() {
  }

  public DescriptionHolder(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    if (description != null) {
      description = description.trim();
      if (description.length() == 0) {
        description = null; // suppress empty descriptions
      }
    }
    this.description = description;
  }

  public String getEscapedDescription() {
    if (description == null) {
      return null;
    }
    StringBuilder builder = new StringBuilder();
    int firstAtPos = description.indexOf('@');
    int length = firstAtPos == -1 ? description.length() : firstAtPos;
    for (int i = 0; i < length; i++) {
      char c = description.charAt(i);
      switch (c) {
        case '*': builder.append("&#42;"); break;
        default: builder.append(c);
      }
    }
    return builder.toString().trim();
  }

  public String getEscapedDescriptionAts() {
    if (description != null) {
      int firstAtPos = description.indexOf('@');
      if (firstAtPos != -1) {
        return description.substring(firstAtPos).trim();
      }
    }
    return null;  //To change body of created methods use File | Settings | File Templates.
  }
}
