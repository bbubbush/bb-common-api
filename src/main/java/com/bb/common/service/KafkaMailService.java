package com.bb.common.service;

import com.bb.common.code.MailTemplateType;
import com.bb.common.code.ResponseCode;
import com.bb.common.vo.common.mail.MailSendVO;
import com.bb.common.vo.req.MailReqVO;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KafkaMailService {
  private final KafkaTemplate<String, MailSendVO> kafkaTemplate;
  private final String KAFKA_MAIL_TOPIC = "mail";

  @Value("${templates.mail.path}")
  private String mailTemplatePath;

  @KafkaListener(topics = KAFKA_MAIL_TOPIC, groupId = "bb-common-mail")
  public void send(MailReqVO mailReqVO) {
    MailSendVO mailSendVO =
      createMailSendBuilder(mailReqVO).text(getDefaultMailTemplate(mailReqVO.getText())).build();
    sendMail(mailSendVO);
  }

  @KafkaListener(topics = KAFKA_MAIL_TOPIC, groupId = "bb-common-mail")
  public void sendErrors(MailReqVO mailReqVO) {
    MailSendVO mailSendVO =
      createMailSendBuilder(mailReqVO).text(getErrorMailTemplate(mailReqVO.getText())).build();
    sendMail(mailSendVO);
  }

  private MailSendVO.MailSendVOBuilder createMailSendBuilder(MailReqVO mailReqVO) {
    return MailSendVO.builder()
      .subject(mailReqVO.getSubject())
      .from(mailReqVO.getFrom())
      .to(mailReqVO.getTo());
  }

  private String getDefaultMailTemplate(String errorMsg) {
    if (StringUtils.isEmpty(errorMsg)) {
      errorMsg = ResponseCode.SERVER_ERROR.getMessage();
    }
    return readTemplate(MailTemplateType.DEFAULT.getTemplateFileName())
      .replace("${sendMsg}", errorMsg);
  }

  private String getErrorMailTemplate(String errorMsg) {
    if (StringUtils.isEmpty(errorMsg)) {
      errorMsg = ResponseCode.SERVER_ERROR.getMessage();
    }
    return readTemplate(MailTemplateType.ERROR.getTemplateFileName())
      .replace("${errorMsg}", errorMsg);
  }

  private String readTemplate(String fileName) {
    String contents = null;
    ClassPathResource resource =
      new ClassPathResource(mailTemplatePath + File.separator + fileName);
    try {
      contents =
        Files.readAllLines(Paths.get(resource.getURI())).stream().collect(Collectors.joining(""));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return contents;
  }

  private void sendMail(MailSendVO mailReqVO) {
    kafkaTemplate.send(KAFKA_MAIL_TOPIC, mailReqVO);
  }
}
