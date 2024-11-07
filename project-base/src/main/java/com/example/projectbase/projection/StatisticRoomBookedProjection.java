package com.example.projectbase.projection;

import java.time.LocalDateTime;

public interface StatisticRoomBookedProjection {

    Long getId();

    String getName();

    Long getPrice();

    String getType();

    String getBed();

    Integer getSize();

    Integer getCapacity();

    String getServices();

    String getDescription();

    LocalDateTime getCreatedDate();

    LocalDateTime getLastModifiedDate();

    Integer getValue();

}