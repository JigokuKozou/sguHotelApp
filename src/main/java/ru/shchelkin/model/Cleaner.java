package ru.shchelkin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cleaner {
    private int id;
    private int id_employee;
    private int id_room;
    private LocalDateTime clean_date;
}
