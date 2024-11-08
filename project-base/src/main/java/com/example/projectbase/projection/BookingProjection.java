package com.example.projectbase.projection;

import com.example.projectbase.constant.BookingStatus;
import com.example.projectbase.domain.dto.UserSummaryDTO;
import com.example.projectbase.domain.dto.common.CreatedByDTO;
import com.example.projectbase.domain.dto.common.LastModifiedByDTO;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface BookingProjection {

    Long getId();

    LocalDateTime getExpectedCheckIn();

    LocalDateTime getExpectedCheckOut();

    LocalDateTime getCheckIn();

    LocalDateTime getCheckOut();

    BookingStatus getStatus();

    String getNote();

    LocalDateTime getCreatedDate();

    LocalDateTime getLastModifiedDate();

    @Value("#{new com.bookinghotel.dto.UserSummaryDTO(target.userId, target.userEmail, target.userPhoneNumber, target.userFirstName, target.userLastName, target.userAvatar)}")
    UserSummaryDTO getUser();

    @Value("#{new com.bookinghotel.dto.common.CreatedByDTO(target.createdById, target.createdByFirstName, target.createdByLastName, target.createdByAvatar)}")
    CreatedByDTO getCreatedBy();

    @Value("#{new com.bookinghotel.dto.common.LastModifiedByDTO(target.lastModifiedById, target.lastModifiedByFirstName, target.lastModifiedByLastName, target.lastModifiedByAvatar)}")
    LastModifiedByDTO getLastModifiedBy();

}
