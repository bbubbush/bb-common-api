package com.bb.common.kafka;

import com.bb.common.vo.req.MailReqVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class KafkaMailDeserializer implements Deserializer<MailReqVO> {
  private final ObjectMapper objectMapper;

  public KafkaMailDeserializer() {
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public MailReqVO deserialize(String topic, byte[] data) {
    String stringValue = new String(data, StandardCharsets.UTF_8);
    MailReqVO mailSendVO;
    try {
      mailSendVO = objectMapper.readValue(stringValue, MailReqVO.class);
    } catch (JsonProcessingException e) {
      throw new SerializationException("Kafka object parse error");
    }
    return mailSendVO;
  }
}
