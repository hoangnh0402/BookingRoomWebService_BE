package com.example.projectbase.service.impl;

import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.CommonMessage;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.SortByDataConstant;
import com.example.projectbase.domain.dto.SaleCreateDTO;
import com.example.projectbase.domain.dto.SaleDTO;
import com.example.projectbase.domain.dto.SaleUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.domain.dto.pagination.PagingMeta;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.domain.entity.Room;
import com.example.projectbase.domain.entity.Sale;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.SaleMapper;
import com.example.projectbase.exception.InvalidException;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.exception.VsException;
import com.example.projectbase.projection.SaleProjection;
import com.example.projectbase.repository.RoomRepository;
import com.example.projectbase.repository.SaleRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.SaleService;
import com.example.projectbase.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    private final SaleMapper saleMapper;

    @Override
    public SaleDTO getSale(Long saleId) {
        SaleProjection sale = saleRepository.findSaleById(saleId);
        checkNotFoundSaleById(sale, saleId);
        return saleMapper.saleProjectionToSaleDTO(sale);
    }

    @Override
    public PaginationResponseDTO<SaleDTO> getSales(PaginationSearchSortRequestDTO requestDTO, Boolean deleteFlag) {
        //Pagination
        Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.SALE);
        Page<SaleProjection> sales = saleRepository.findAllSale(requestDTO.getKeyword(), deleteFlag, pageable);
        //Create Output
        PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.SALE, sales);
        return new PaginationResponseDTO<SaleDTO>(meta, toSaleDTOs(sales));
    }

    @Override
    public SaleDTO createSale(SaleCreateDTO createDTO, UserPrincipal principal) {
        User creator = userRepository.getUser(principal);
        Sale sale = saleMapper.createDtoToSale(createDTO);
        return saleMapper.toSaleDTO(saleRepository.save(sale), creator, creator);
    }

    @Override
    public SaleDTO updateSale(Long saleId, SaleUpdateDTO updateDTO, UserPrincipal principal) {
        Optional<Sale> currentSale = saleRepository.findById(saleId);
        checkNotFoundSaleById(currentSale, saleId);
        saleMapper.updateSaleFromDTO(updateDTO, currentSale.get());
        User updater = userRepository.getUser(principal);
        User creator = userRepository.findById(currentSale.get().getCreatedBy()).get();
        return saleMapper.toSaleDTO(saleRepository.save(currentSale.get()), creator, updater);
    }

    @Override
    public CommonResponseDTO addSalesToRooms(Long saleId, List<Long> roomIds) {
        Optional<Sale> sale = saleRepository.findById(saleId);
        checkNotFoundSaleById(sale, saleId);
        List<Room> rooms = roomRepository.findAllByIds(roomIds);
        checkNotFoundRoomsByIds(rooms, roomIds);
        checkRoomsOnSale(rooms);
        for(Room room : rooms) {
            room.setSale(sale.get());
            roomRepository.save(room);
        }
        return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.ADD_SUCCESS);
    }

    @Override
    public CommonResponseDTO removeSaleFromRoom(Long saleId, Long roomId) {
        Optional<Sale> sale = saleRepository.findById(saleId);
        checkNotFoundSaleById(sale, saleId);
        Optional<Room> room = roomRepository.findById(roomId);
        checkNotFoundRoomById(room, roomId);
        checkRoomNotForSale(room.get(), sale.get());
        room.get().setSale(null);
        roomRepository.save(room.get());
        return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
    }

    @Override
    public CommonResponseDTO removeSaleFromRooms(List<Long> roomIds) {
        List<Room> rooms = roomRepository.findAllByIds(roomIds);
        checkNotFoundRoomsByIds(rooms, roomIds);
        checkRoomsNotSale(rooms);
        for(Room room : rooms) {
            room.setSale(null);
            roomRepository.save(room);
        }
        return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
    }

    @Override
    public CommonResponseDTO deleteSale(Long saleId) {
        Optional<Sale> sale = saleRepository.findById(saleId);
        checkNotFoundSaleById(sale, saleId);
        sale.get().setDeleteFlag(CommonConstant.TRUE);
        saleRepository.save(sale.get());
        return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
    }

    @Override
    public CommonResponseDTO deleteSalePermanently(Long saleId) {
        Optional<Sale> sale = saleRepository.findByIdAndIsDeleteFlag(saleId);
        checkNotFoundSaleIsDeleteFlagById(sale, saleId);
        saleRepository.delete(sale.get());
        return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
    }

    @Override
    public CommonResponseDTO restoreSale(Long saleId) {
        Optional<Sale> sale = saleRepository.findByIdAndIsDeleteFlag(saleId);
        checkNotFoundSaleIsDeleteFlagById(sale, saleId);
        sale.get().setDeleteFlag(CommonConstant.FALSE);
        saleRepository.save(sale.get());
        return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.RESTORE_SUCCESS);
    }

    @Override
    @Transactional
    public void deleteSaleByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords) {
        saleRepository.deleteByDeleteFlag(isDeleteFlag, daysToDeleteRecords);
    }

    private List<SaleDTO> toSaleDTOs(Page<SaleProjection> saleProjections) {
        List<SaleDTO> saleDTOs = new LinkedList<>();
        for(SaleProjection saleProjection : saleProjections) {
            saleDTOs.add(saleMapper.saleProjectionToSaleDTO(saleProjection));
        }
        return saleDTOs;
    }

    private void checkNotFoundSaleById(Optional<Sale> sale, Long saleId) {
        if (sale.isEmpty()) {
            throw new NotFoundException(String.format(ErrorMessage.Sale.ERR_NOT_FOUND_ID, saleId));
        }
    }

    private void checkNotFoundSaleIsDeleteFlagById(Optional<Sale> sale, Long saleId) {
        if (sale.isEmpty()) {
            throw new NotFoundException(String.format(ErrorMessage.Sale.ERR_NOT_FOUND_ID_IN_TRASH, saleId));
        }
    }

    private void checkNotFoundSaleById(SaleProjection saleProjection, Long saleId) {
        if (ObjectUtils.isEmpty(saleProjection)) {
            throw new NotFoundException(String.format(ErrorMessage.Sale.ERR_NOT_FOUND_ID, saleId));
        }
    }

    private void checkNotFoundRoomById(Optional<Room> room, Long roomId) {
        if (room.isEmpty()) {
            throw new NotFoundException(String.format(ErrorMessage.Room.ERR_NOT_FOUND_ID, roomId));
        }
    }

    private void checkRoomNotForSale(Room room, Sale sale) {
        if(ObjectUtils.isEmpty(room.getSale())) {
            throw new InvalidException(String.format(ErrorMessage.Room.ROOM_NO_SALE, room.getId()));
        } else {
            if(!room.getSale().getId().equals(sale.getId())) {
                throw new InvalidException(String.format(ErrorMessage.Room.ROOM_NOT_FOR_SALE, room.getId(), sale.getId()));
            }
        }
    }

    private void checkNotFoundRoomsByIds(List<Room> rooms, List<Long> roomIds) {
        if(rooms.size() != roomIds.size()) {
            Map<String, String> result = new HashMap<>();
            for(Long roomId : roomIds) {
                boolean isValid = false;
                for(Room room : rooms) {
                    if(room.getId().equals(roomId)) {
                        isValid = true;
                        break;
                    }
                }
                if(!isValid) {
                    result.put(roomId.toString(), String.format(ErrorMessage.Room.ERR_NOT_FOUND_ID, roomId));
                }
            }
            if(!result.isEmpty()) {
                throw new VsException(HttpStatus.BAD_REQUEST, result);
            }
        }
    }

    private void checkRoomsOnSale(List<Room> rooms) {
        Map<String, String> result = new HashMap<>();
        for(Room room : rooms) {
            if(ObjectUtils.isNotEmpty(room.getSale())) {
                result.put(room.getId().toString(), String.format(ErrorMessage.Room.ROOM_HAS_BEEN_DISCOUNTED, room.getId()));
            }
        }
        if(!result.isEmpty()) {
            throw new VsException(HttpStatus.BAD_REQUEST, result);
        }
    }

    private void checkRoomsNotSale(List<Room> rooms) {
        Map<String, String> result = new HashMap<>();
        for(Room room : rooms) {
            if(ObjectUtils.isEmpty(room.getSale())) {
                result.put(room.getId().toString(), String.format(ErrorMessage.Room.ROOM_NO_SALE, room.getId()));
            }
        }
        if(!result.isEmpty()) {
            throw new VsException(HttpStatus.BAD_REQUEST, result);
        }
    }

}
