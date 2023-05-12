package ru.shchelkin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeCleanerDates {
    private int id;
    private String name;
    private String surname;
    private int passport_serial;
    private int passport_number;
    private int id_room;
    private LocalDateTime clean_date;
}
