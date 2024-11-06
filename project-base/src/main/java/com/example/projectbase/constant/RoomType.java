package com.example.projectbase.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RoomType {

    Standard("Standard"),
    Superior("Superior"),
    Deluxe("Deluxe"),
    Suite("Suite");

    private String value;

}
