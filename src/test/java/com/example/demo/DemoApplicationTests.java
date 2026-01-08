package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@Import(DemoApplicationTests.TestcontainersConfiguration.class)
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class DemoApplicationTests {

  private static final Logger logger = LoggerFactory.getLogger(DemoApplicationTests.class);

  @Autowired
  DemoRepository repository;

  @Test
  void showLeak(CapturedOutput output) {
    Thread.ofVirtual().start(() -> {
      while (true) {
        repository.save(new DemoRepository.DemoEntity("test")).block();
      }
    });

    await().untilAsserted(() -> assertThat(output).contains("LEAK: ByteBuf.release() was not called before it's garbage-collected"));
  }

  @TestConfiguration(proxyBeanMethods = false)
  static
  class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDbContainer() {
      return new MongoDBContainer(DockerImageName.parse("mongo").withTag("7.0"));
    }

  }
}
