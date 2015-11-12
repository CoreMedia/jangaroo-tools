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
    if (description != null && description.trim().isEmpty()) {
      description = null; // suppress empty descriptions
    }
    this.description = description;
  }

  public String getEscapedDescription() {
    if (description == null) {
      return null;
    }
    String description = this.description.trim();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < description.length(); i++) {
      char c = description.charAt(i);
      switch (c) {
        case '*': builder.append("&#42;"); break;
        default: builder.append(c);
      }
    }
    return builder.toString();
  }

  public String getEscapedDescriptionWithoutAts() {
    String description = getEscapedDescription();
    if (description != null) {
      int firstAtPos = description.indexOf('@');
      return firstAtPos == -1 ? description : description.substring(0, firstAtPos).trim();
    }
    return null;
  }

  public String getEscapedDescriptionAts() {
    String description = getEscapedDescription();
    if (description != null) {
      int firstAtPos = description.indexOf('@');
      if (firstAtPos != -1) {
        return description.substring(firstAtPos).trim();
      }
    }
    return null;
  }
}
