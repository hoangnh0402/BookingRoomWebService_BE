package com.example.projectbase.service.impl;

import com.example.projectbase.constant.BookingStatus;
import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.SortByDataConstant;
import com.example.projectbase.domain.dto.*;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.domain.dto.pagination.PagingMeta;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.BookingRoomDetail;
import com.example.projectbase.domain.entity.BookingServiceDetail;
import com.example.projectbase.domain.mapper.RoomMapper;
import com.example.projectbase.domain.mapper.UserMapper;
import com.example.projectbase.projection.StatisticCustomerTopBookingProjection;
import com.example.projectbase.projection.StatisticRoomBookedProjection;
import com.example.projectbase.repository.BookingRepository;
import com.example.projectbase.repository.RoomRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.service.BookingService;
import com.example.projectbase.service.StatisticService;
import com.example.projectbase.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;

    private final BookingRepository bookingRepository;

    private final BookingService bookingService;

    private final RoomMapper roomMapper;

    private final UserMapper userMapper;

    @Override
    public PaginationResponseDTO<Map<String, Object>> statisticRoomBookedForMonth(PaginationSearchSortRequestDTO request,
                                                                                  Integer month, Integer year) {
        //Pagination
        Pageable pageable = PaginationUtil.buildPageable(request, SortByDataConstant.ROOM);
        Page<StatisticRoomBookedProjection> roomBookedMonthProjections =
                roomRepository.statisticRoomBookedForMonth(month, year, request.getKeyword(), request.getSortType(), pageable);
        //Create Output
        PagingMeta meta = PaginationUtil.buildPagingMeta(request, SortByDataConstant.ROOM, roomBookedMonthProjections);
        List<Map<String, Object>> result = new LinkedList<>();
        for (StatisticRoomBookedProjection statisticRoomBookedProjection : roomBookedMonthProjections) {
            Map<String, Object> objectMap = new HashMap<>();
            RoomSummaryDTO roomSummaryDTO = roomMapper.statisticRoomToRoomDTO(statisticRoomBookedProjection);
            objectMap.put("room", roomSummaryDTO);
            objectMap.put("value", statisticRoomBookedProjection.getValue());
            result.add(objectMap);
        }
        return new PaginationResponseDTO<Map<String, Object>>(meta, result);
    }

    @Override
    public List<Map<String, Object>> statisticCustomerTopBooking() {
        List<StatisticCustomerTopBookingProjection> customerTopBooking = userRepository.findAllCustomerTopBooking();
        List<Map<String, Object>> result = new LinkedList<>();
        for (StatisticCustomerTopBookingProjection statisticCustomerTopBooking : customerTopBooking) {
            Map<String, Object> objectMap = new HashMap<>();
            UserDTO userDTO = userMapper.statisticCustomerTopBookingProjectionToUserDTO(statisticCustomerTopBooking);
            objectMap.put("user", userDTO);
            objectMap.put("value", statisticCustomerTopBooking.getValue());
            result.add(objectMap);
        }
        return result;
    }

    @Override
    public List<RevenueMonthDTO> statisticRevenue(RevenueRequestDTO request) {
        List<RevenueMonthDTO> revenueMonthDTOs = new LinkedList<>();
        int fromMonth = request.getFromMonth(), toMonth = request.getToMonth();
        List<Booking> bookings = bookingRepository.statisticRevenue(request);
        for (int month = fromMonth; month <= toMonth; month++) {
            long totalRevenueMonth = 0;
            int totalBooking = 0;
            for (Booking booking : bookings) {
                if (booking.getCreatedDate().getMonthValue() == month) {
                    Set<BookingRoomDetail> bookingRoomDetails = booking.getBookingRoomDetails();
                    Set<BookingServiceDetail> bookingServiceDetails = booking.getBookingServiceDetails();
                    totalRevenueMonth += bookingService.calculateTotalRoomPrice(booking, bookingRoomDetails);
                    totalRevenueMonth += bookingService.calculateTotalServicePrice(booking, bookingServiceDetails);
                    List<BookingSurchargeDTO> surchargeDTOs = bookingService.calculateSurcharge(booking, bookingRoomDetails);
                    for (BookingSurchargeDTO surchargeDTO : surchargeDTOs) {
                        if (ObjectUtils.isNotEmpty(surchargeDTO)) {
                            totalRevenueMonth += surchargeDTO.getRoomSurcharge();
                        }
                    }
                    totalBooking++;
                }
            }
            RevenueMonthDTO revenueMonthDTO = new RevenueMonthDTO();
            revenueMonthDTO.setMonth(convertMonthNumberToString(month));
            revenueMonthDTO.setTotalBooking(totalBooking);
            revenueMonthDTO.setTotalRevenue(totalRevenueMonth);
            revenueMonthDTOs.add(revenueMonthDTO);
        }
        return revenueMonthDTOs;
    }

    @Override
    public BookingStatisticDTO statisticBookingForMonth(Integer month, Integer year) {
        BookingStatisticDTO bookingStatisticDTO = new BookingStatisticDTO();
        bookingStatisticDTO.setTotalBookingCheckin(bookingRepository.countBookingByStatus(month, year, BookingStatus.CHECKED_IN.toString()));
        bookingStatisticDTO.setTotalBookingCheckout(bookingRepository.countBookingByStatus(month, year, BookingStatus.CHECKED_OUT.toString()));
        bookingStatisticDTO.setTotalBookingPending(bookingRepository.countBookingByStatus(month, year, BookingStatus.PENDING.toString()));
        bookingStatisticDTO.setTotalBookingCancel(bookingRepository.countBookingByStatus(month, year, BookingStatus.CANCEL.toString()));
        return bookingStatisticDTO;
    }

    private String convertMonthNumberToString(int monthNumber) {
        switch (monthNumber) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return CommonConstant.EMPTY_STRING;
        }
    }
}
