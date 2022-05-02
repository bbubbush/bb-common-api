package com.bb.common.kafka;

import com.bb.common.vo.common.mail.MailSendVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
public class KafkaMailSerializer implements Serializer<MailSendVO> {
  private final ObjectMapper objectMapper;

  public KafkaMailSerializer() {
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public byte[] serialize(String topic, MailSendVO data) {
    try {
      if (data == null) return null;
      else return objectMapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);
    } catch (JsonProcessingException e) {
      throw new SerializationException("Kafka object parse error");
    }
  }
}
