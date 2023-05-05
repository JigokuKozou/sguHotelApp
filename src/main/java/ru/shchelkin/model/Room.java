package ru.shchelkin.model;

import lombok.Data;

@Data
public class Room {
    private int id;
    private int idHotel;
    private int idType;
    private int countRooms;
    private String status;
}
