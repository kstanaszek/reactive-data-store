package com.reactive.dynamo.demo;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {


  private DynamoDBConfig dbConfig;

  public AppointmentServiceImpl(DynamoDBConfig dbConfig) {
    this.dbConfig = dbConfig;
  }

  @Override
  public Flux<Appointment> getAllAppointments() {
    ScanRequest scanRequest = ScanRequest.builder().tableName("calendarAppointment").build();

    return Mono.from(dbConfig.dynamoDbAsyncClient().scanPaginator(scanRequest)).flatMapIterable(res ->
        res.items()
            .stream()
            .map(item -> map(item.get("id").s(), item))
            .collect(Collectors.toList()));
  }

  @Override
  public Mono<Appointment> getAppointment(String id) {
    Map<String, AttributeValue> map = new HashMap<>();
    map.put("id", AttributeValue.builder().s(id).build());

    GetItemRequest getItemRequest = GetItemRequest.builder().key(map)
        .tableName("calendarAppointment").build();

    CompletableFuture<GetItemResponse> response = dbConfig.dynamoDbAsyncClient()
        .getItem(getItemRequest);

    return Mono.fromCompletionStage(response)
        .map(item -> map(id, item.item()));

  }

  @Override
  public Mono<Appointment> save(Appointment appointment) {
    PutItemRequest putItemRequest = PutItemRequest.builder()
        .tableName("calendarAppointment")
        .item(map(appointment))
        .build();

    return Mono.fromCompletionStage(dbConfig.dynamoDbAsyncClient().putItem(putItemRequest))
        .flatMap(f -> this.getAppointment(appointment.getId()));
  }

  public Map<String, AttributeValue> map(Appointment appointment){
    Map<String, AttributeValue> map = new HashMap<>();
    map.put("id", AttributeValue.builder().s(appointment.getId()).build());
    map.put("place", AttributeValue.builder().s(appointment.getPlace()).build());
    return map;
  }

  public Appointment map(String key, Map<String, AttributeValue> map) {
    return new Appointment(key, map.get("place").s());
  }

}
