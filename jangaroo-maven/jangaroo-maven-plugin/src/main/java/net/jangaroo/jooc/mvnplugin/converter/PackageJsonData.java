package net.jangaroo.jooc.mvnplugin.converter;

public class PackageJsonData {
  private String name;
  private String version;
  private Object sencha;


  public PackageJsonData() {
  }

  public PackageJsonData(String name, String version) {
    this.name = name;
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Object getSencha() {
    return sencha;
  }

  public void setSencha(Object sencha) {
    this.sencha = sencha;
  }
}
