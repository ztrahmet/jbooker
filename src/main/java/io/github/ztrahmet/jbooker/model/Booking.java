package io.github.ztrahmet.jbooker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * A Plain Old Java Object (POJO) representing a single booking reservation.
 * The @Data annotation from Lombok generates all getters, setters,
 * equals, hashCode, and a useful toString method.
 *
 * @NoArgsConstructor generates a constructor with no arguments.
 * @AllArgsConstructor generates a constructor with all fields as arguments.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private int id;
    private int roomId;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}

