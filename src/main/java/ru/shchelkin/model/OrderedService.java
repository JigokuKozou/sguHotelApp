package ru.shchelkin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderedService {
    private int id;
    private int id_service;
    private int id_accommodation;
    private int count;
    private LocalDateTime order_date;
}
