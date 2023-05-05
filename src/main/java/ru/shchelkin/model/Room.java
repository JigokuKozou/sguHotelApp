package ru.shchelkin.model;

import lombok.Data;

@Data
public class Room {
    private int id;
    private int id_hotel;
    private int id_type;
    private int count_rooms;
    private String status;
}
