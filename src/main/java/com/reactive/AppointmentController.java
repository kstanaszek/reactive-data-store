package com.reactive;

import com.reactive.dynamo.demo.Appointment;
import com.reactive.dynamo.demo.AppointmentService;
import com.reactive.mongo.demo.AppointmentMongo;
import com.reactive.mongo.demo.AppointmentMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller

public class AppointmentController {

  @Autowired
  private AppointmentService appointmentService;

  @Autowired
  private AppointmentMongoRepository appointmentMongoRepository;

  public AppointmentController(AppointmentService appointmentService,
      AppointmentMongoRepository appointmentMongoRepository) {
    this.appointmentService = appointmentService;
    this.appointmentMongoRepository = appointmentMongoRepository;
  }

  @GetMapping(path = "/appointments", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @ResponseBody
  Flux<Appointment> getAllAppointments() {
    return appointmentService.getAllAppointments();
  }

  @GetMapping(path = "/mongo/appointments", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @ResponseBody
  Flux<AppointmentMongo> getAllMongoAppointments() {
    return appointmentMongoRepository.findBy();
  }

  @PostMapping(value = "/mongo/appointment", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @ResponseBody
  public Mono<AppointmentMongo> createMongoAppointment(@RequestBody AppointmentMongo appointment) {
    return appointmentMongoRepository.save(appointment);
  }

  @PostMapping(value = "/appointment", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @ResponseBody
  public Mono<Appointment> createStudent(@RequestBody Appointment appointment) {
    return appointmentService.save(appointment);
  }


  @GetMapping(path = "/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @ResponseBody
  Flux<String> getTest() {
    return Flux.interval(Duration.ofMillis(500))
        .map(input -> "DATA");
  }
}
