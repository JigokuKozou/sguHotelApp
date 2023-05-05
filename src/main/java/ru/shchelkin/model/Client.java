package ru.shchelkin.model;

import lombok.Data;

@Data
public class Client {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private int passportSerial;
    private int passportNumber;
    private String phoneNumber;
}
