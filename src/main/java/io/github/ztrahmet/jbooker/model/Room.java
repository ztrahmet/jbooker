package io.github.ztrahmet.jbooker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private int id;
    private String roomNumber;
    private String type;
    private double price;
}
