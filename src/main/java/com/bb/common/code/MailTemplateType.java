package com.bb.common.code;

public enum MailTemplateType {
  DEFAULT("default-template.html")
  , ERROR("error-template.html")
  ;
  private String templateFileName;

  MailTemplateType(String templateFileName) {
    this.templateFileName = templateFileName;
  }

  public String getTemplateFileName() {
    return templateFileName;
  }
}
