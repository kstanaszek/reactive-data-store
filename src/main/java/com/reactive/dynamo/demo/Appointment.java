package com.reactive.dynamo.demo;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;


@DynamoDBTable(tableName = "calendarAppointment")
@Data
public class Appointment {
  private String id;
  private String place;

  @DynamoDBHashKey
  public String getId() {
    return id;
  }

  public Appointment(String id, String place) {
    this.id = id;
    this.place = place;
  }
}
