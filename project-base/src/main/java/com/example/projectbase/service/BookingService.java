package com.example.projectbase.service;

import com.example.projectbase.domain.dto.*;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSortRequestDTO;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.BookingRoomDetail;
import com.example.projectbase.domain.entity.BookingServiceDetail;
import com.example.projectbase.projection.BookingProjection;
import com.example.projectbase.security.UserPrincipal;

import java.util.List;
import java.util.Set;

public interface BookingService {

    BookingDTO getBookingById(Long bookingId);

    PaginationResponseDTO<BookingDTO> getBookingsForUser(PaginationSortRequestDTO requestDTO, UserPrincipal principal);

    PaginationResponseDTO<BookingDTO> getBookingsForAdmin(PaginationSearchSortRequestDTO requestDTO, BookingFilterDTO bookingFilterDTO);

    BookingDTO createBooking(BookingCreateDTO bookingCreateDTO, UserPrincipal principal);

    BookingDTO updateBooking(Long bookingId, BookingUpdateDTO bookingUpdateDTO, UserPrincipal principal);

    BookingDTO checkIn(Long bookingId, UserPrincipal principal);

    BookingDTO checkOutAndPayment(Long bookingId, UserPrincipal principal);

    CommonResponseDTO cancelBooking(Long bookingId, String note, UserPrincipal principal);

    void lockUserRefuseToCheckIn();

    List<BookingDTO> mapperToBookingDTOs(List<BookingProjection> bookings);

    Long calculateTotalRoomPrice(BookingProjection booking, Set<BookingRoomDetail> bookingRoomDetails);

    Long calculateTotalRoomPrice(Booking booking, Set<BookingRoomDetail> bookingRoomDetails);

    Long calculateTotalServicePrice(BookingProjection booking, Set<BookingServiceDetail> bookingServiceDetails);

    Long calculateTotalServicePrice(Booking booking, Set<BookingServiceDetail> bookingServiceDetails);

    List<BookingSurchargeDTO> calculateSurcharge(BookingProjection booking, Set<BookingRoomDetail> bookingRoomDetails);

    List<BookingSurchargeDTO> calculateSurcharge(Booking booking, Set<BookingRoomDetail> bookingRoomDetails);

}
