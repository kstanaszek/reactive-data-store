package com.reactive.dynamo.demo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppointmentService {
  Flux<Appointment> getAllAppointments();
  Mono<Appointment> getAppointment(String id);
  Mono<Appointment> save(Appointment appointment);
}
