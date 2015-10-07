package net.jangaroo.jooc.model;

import net.jangaroo.utils.AS3Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A model of a field of an ActionScript class.
 */
public class MethodModel extends MemberModel {

  public static final List<ParamModel> NO_PARAMS = Collections.emptyList();

  private ReturnModel returnModel;
  private List<ParamModel> params;
  private boolean isFinal = false;
  private boolean override = false;
  private MethodType methodType = null;
  private String body = null;
  private boolean hasOptional = false;

  public MethodModel() {
    this(null, null, NO_PARAMS);
  }

  public MethodModel(String name, String returnType) {
    this(name, returnType, NO_PARAMS);
  }

  @Override
  public boolean isReadable() {
    return true;
  }

  @Override
  public boolean isWritable() {
    return false;
  }

  public MethodModel(MethodType methodType, String name, String type) {
    this(methodType, name, methodType == MethodType.SET ? "void" : type,
      methodType == MethodType.SET
                    ? Arrays.asList(new ParamModel("value", type))
                    : NO_PARAMS,
            methodType == MethodType.SET
                    ? "@private"
                    : null);
  }

  public MethodModel(String name, String returnType, ParamModel... params) {
    this(null, name, returnType, Arrays.asList(params), null);
  }

  public MethodModel(String name, String returnType, List<ParamModel> params) {
    this(null, name, returnType, params, null);
  }

  private MethodModel(MethodType methodType, String name, String returnType, List<ParamModel> params, String asdoc) {
    super(name, returnType);
    this.methodType = methodType;
    setParams(params);
    setAsdoc(asdoc);
  }

  @Override
  public boolean isMethod() {
    return true;
  }

  @Override
  public boolean isAccessor() {
    return methodType != null;
  }

  @Override
  public boolean isGetter() {
    return methodType == MethodType.GET;
  }

  @Override
  public boolean isSetter() {
    return methodType == MethodType.SET;
  }

  public boolean isOverride() {
    return override;
  }

  public void setOverride(boolean override) {
    this.override = override;
  }

  public boolean isFinal() {
    return isFinal;
  }

  public void setFinal(boolean aFinal) {
    isFinal = aFinal;
  }

  public MethodType getMethodType() {
    return methodType;
  }

  public void setMethodType(MethodType methodType) {
    this.methodType = methodType;
  }

  public List<ParamModel> getParams() {
    return Collections.unmodifiableList(params);
  }

  public void setParams(List<ParamModel> params) {
    this.params = new ArrayList<ParamModel>();
    hasOptional = false;
    for (ParamModel param : params) {
      addParam(param); // patch optional!
    }
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  /**
   * Add a parameter.
   * @param paramModel must be "frozen"!
   */
  public void addParam(ParamModel paramModel) {
    if (paramModel.isOptional()) {
      hasOptional = true;
    } else if (hasOptional) {
      paramModel.setOptional(true);
    }
    params.add(paramModel);
  }

  public ReturnModel getReturnModel() {
    if (returnModel == null) {
      returnModel = new ReturnModel(this);
    }
    return returnModel;
  }

  @Override
  public void visit(ModelVisitor visitor) {
    visitor.visitMethod(this);
  }

  public MethodModel duplicate() {
    return new MethodModel(methodType, getName(), getType(), params, getAsdoc());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    MethodModel that = (MethodModel)o;
    return methodType == that.methodType && params.equals(that.params) &&
      (returnModel == null ? that.returnModel == null : returnModel.equals(that.returnModel));
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + params.hashCode();
    result = 31 * result + (methodType != null ? methodType.hashCode() : 0);
    return result;
  }

  public void addBodyCode(String code) {
    if (body == null) {
      body = code;
    } else {
      body += code;
    }
  }
}
