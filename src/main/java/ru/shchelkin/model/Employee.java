package ru.shchelkin.model;

import lombok.Data;

@Data
public class Employee {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private int passportSerial;
    private int passportNumber;
    private double salary;
    private String status;
}
