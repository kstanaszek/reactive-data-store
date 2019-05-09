package com.reactive.mongo.demo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository()
public interface AppointmentMongoRepository extends ReactiveMongoRepository<AppointmentMongo, String> {
  @Tailable
  Flux<AppointmentMongo> findBy();
}
