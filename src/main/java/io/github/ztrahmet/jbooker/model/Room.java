package io.github.ztrahmet.jbooker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Plain Old Java Object (POJO) representing a single hotel room.
 * The @Data annotation from Lombok generates all getters, setters,
 * equals, hashCode, and a useful toString method.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private int id;
    private String roomNumber;
    private String type;
    private double price;
}
