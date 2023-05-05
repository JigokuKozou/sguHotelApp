package ru.shchelkin.model;

import lombok.Data;

@Data
public class Hotel {
    private int id;
    private String name;
    private String address;
    private String status;
}
