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
    for (int i = 0; i < description.length(); i++) {
      char c = description.charAt(i);
      switch (c) {
        case '*': builder.append("&#42;"); break;
        default: builder.append(c);
      }
    }
    return builder.toString();
  }
}
