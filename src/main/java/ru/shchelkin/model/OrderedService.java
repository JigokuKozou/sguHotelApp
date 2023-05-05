package ru.shchelkin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderedService {
    private int idService;
    private int idAccommodation;
    private int count;
    private LocalDateTime orderDate;
}
