package ru.shchelkin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Accommodation {
    private int id;
    private int idReservations;
    private int idRoom;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
