package com.bb.common.service;

import com.bb.common.vo.req.MailReqVO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
  private final String KAFKA_MAIL_TOPIC = "mail";
  private final KafkaTemplate<String, MailReqVO> kafkaTemplate;

  public void sendMail(MailReqVO mailReqVO) {
    kafkaTemplate.send(KAFKA_MAIL_TOPIC, mailReqVO);
  }
}
