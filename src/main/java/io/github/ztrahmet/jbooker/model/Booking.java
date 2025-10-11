package io.github.ztrahmet.jbooker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private int id;
    private String roomNumber;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
