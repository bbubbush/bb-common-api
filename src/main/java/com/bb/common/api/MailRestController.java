package com.bb.common.api;

import com.bb.common.service.KafkaMailService;
import com.bb.common.util.ApiResponse;
import com.bb.common.vo.common.ResponseVO;
import com.bb.common.vo.req.MailReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailRestController {
  private final KafkaMailService kafkaMailService;

  @PostMapping("/send")
  public ResponseVO<String> sendMail(@RequestBody MailReqVO mailReqVO) {
    kafkaMailService.send(mailReqVO);
    return ApiResponse.success("");
  }
}
