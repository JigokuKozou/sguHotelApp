package ru.shchelkin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reservation {
    private int id;
    private int id_client;
    private int id_type;
    private LocalDateTime start_booking;
    private LocalDateTime end_booking;
    private int accommodations_count;
}
