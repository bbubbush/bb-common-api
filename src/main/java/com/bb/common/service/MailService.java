package com.bb.common.service;

import com.bb.common.vo.common.mail.MailSendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
  private final JavaMailSender mailSender;

  private final String KAFKA_MAIL_TOPIC = "mail";

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

  @KafkaListener(topics = KAFKA_MAIL_TOPIC, groupId = "bb-common-mail")
  private void sendMailForKafka(MailSendVO mailSendVO) {
    sendMail(mailSendVO);
  }
}
