package ru.shchelkin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cleaner {
    private int idEmployee;
    private int idRoom;
    private LocalDateTime cleanDate;
}
