package com.bb.common.vo.common.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailSendVO {
  private String to;
  private String from;
  private String subject;
  private String text;
}
