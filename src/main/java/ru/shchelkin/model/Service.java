package ru.shchelkin.model;

import lombok.Data;

@Data
public class Service {
    private int id;
    private String name;
    private String description;
    private boolean isAvailable;
    private double price;
}
