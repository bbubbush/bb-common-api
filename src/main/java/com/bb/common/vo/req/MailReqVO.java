package com.bb.common.vo.req;

import com.bb.common.code.MailTemplateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailReqVO {
  private String to;
  private String from;
  private String subject;
  private String text;
  private MailTemplateType templateType;
}
