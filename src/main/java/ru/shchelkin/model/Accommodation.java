package ru.shchelkin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Accommodation {
    private int id;
    private int id_reservations;
    private int id_room;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
}
