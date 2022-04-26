package com.bb.common.service;

import com.bb.common.code.MailTemplateType;
import com.bb.common.code.ResponseCode;
import com.bb.common.vo.common.mail.MailSendVO;
import com.bb.common.vo.req.MailReqVO;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
  private final JavaMailSender mailSender;
  @Value("${templates.mail.path}")
  private String mailTemplatePath;

  public void send(MailReqVO mailReqVO) {
    MailSendVO mailSendVO =
        createMailSendBuilder(mailReqVO).text(getDefaultMailTemplate(mailReqVO.getText())).build();
    sendMail(mailSendVO);
  }

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

  public void sendMail(MailSendVO mailSendVO) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
      mimeMessage.setSubject(mailSendVO.getSubject());
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
      helper.setTo(mailSendVO.getTo());
      helper.setFrom(mailSendVO.getFrom());
      helper.setText(mailSendVO.getText(), true);
      mailSender.send(mimeMessage);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  private String getDefaultMailTemplate(String errorMsg) {
    if (StringUtils.isEmpty(errorMsg)) {
      errorMsg = ResponseCode.SERVER_ERROR.getMessage();
    }
    return readFile(MailTemplateType.DEFAULT.getTemplateFileName())
        .replace("${sendMsg}", errorMsg);
  }

  private String getErrorMailTemplate(String errorMsg) {
    if (StringUtils.isEmpty(errorMsg)) {
      errorMsg = ResponseCode.SERVER_ERROR.getMessage();
    }
    return readFile(MailTemplateType.ERROR.getTemplateFileName()).replace("${errorMsg}", errorMsg);
  }

  private String readFile(String fileName) {
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
}
