package ru.shchelkin.model;

import lombok.Data;

@Data
public class Employee {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private int passport_serial;
    private int passport_number;
    private double salary;
    private String status;
}
