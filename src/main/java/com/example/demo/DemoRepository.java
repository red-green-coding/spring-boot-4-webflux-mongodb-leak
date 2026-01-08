package com.example.demo;

import com.example.demo.DemoRepository.DemoEntity;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRepository extends ReactiveMongoRepository<DemoEntity, String> {

  @Document(collection = "demo")
  class DemoEntity {

    @Id
    String id = UUID.randomUUID().toString();

    String someField;

    public DemoEntity(String someField) {
      this.someField = someField;
    }
  }
}
