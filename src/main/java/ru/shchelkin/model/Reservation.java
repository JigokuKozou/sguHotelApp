package ru.shchelkin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reservation {
    private int id;
    private int idClient;
    private int idType;
    private LocalDateTime startBooking;
    private LocalDateTime endBooking;
    private int accommodationsCount;
}
