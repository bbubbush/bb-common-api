package com.bb.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BbCommonApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(BbCommonApiApplication.class, args);
  }

}
