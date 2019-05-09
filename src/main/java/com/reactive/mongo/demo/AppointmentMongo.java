package com.reactive.mongo.demo;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "appointment")
@Data
public class AppointmentMongo {
  private String id;
  private String place;

  public AppointmentMongo(String id, String place) {
    this.id = id;
    this.place = place;
  }

}
