package com.example.projectbase.service;

import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.BookingRoomDetail;

import java.util.List;
import java.util.Set;

public interface BookingRoomDetailService {

    Set<BookingRoomDetail> getBookingRoomDetailsByBooking(Long bookingId);

    Set<BookingRoomDetail> createBookingRoomDetails(Booking booking, List<Long> roomIds);

}
