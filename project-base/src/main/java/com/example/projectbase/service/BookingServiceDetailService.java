package com.example.projectbase.service;

import com.example.projectbase.domain.dto.BookingServiceDTO;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.BookingServiceDetail;

import java.util.List;
import java.util.Set;

public interface BookingServiceDetailService {

    Set<BookingServiceDetail> getBookingServiceDetailsByBooking(Long bookingId);

    Set<BookingServiceDetail> createBookingServiceDetails(Booking booking, List<BookingServiceDTO> bookingService);

}
