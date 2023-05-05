package ru.shchelkin.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FormerEmployee {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private int passportSerial;
    private int passportNumber;
    private LocalDate fired_date;
}
