package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.ServiceCreateDTO;
import com.example.projectbase.domain.dto.ServiceUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.security.AuthorizationInfo;
import com.example.projectbase.security.CurrentUserLogin;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.HotelService;
import com.example.projectbase.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestApiV1
public class HotelServiceController {

    private final HotelService hotelService;

    private final ProductService productService;

    @Operation(summary = "API get service by id")
    @GetMapping(UrlConstant.Service.GET_SERVICE)
    public ResponseEntity<?> getServiceById(@PathVariable Long serviceId) {
        return VsResponseUtil.ok(hotelService.getServiceById(serviceId));
    }

    @Tag(name = "hotel-service-controller-admin")
    @Operation(summary = "API get all service for admin")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @GetMapping(UrlConstant.Service.GET_SERVICES_FOR_ADMIN)
    public ResponseEntity<?> getServicesForAdmin(@Valid @ParameterObject PaginationSearchSortRequestDTO requestDTO,
                                                 @RequestParam Boolean deleteFlag) {
        return VsResponseUtil.ok(hotelService.getServices(requestDTO, deleteFlag));
    }

    @Operation(summary = "API get all service for user")
    @GetMapping(UrlConstant.Service.GET_SERVICES)
    public ResponseEntity<?> getServicesForUser(@Valid @ParameterObject PaginationSearchSortRequestDTO requestDTO) {
        return VsResponseUtil.ok(hotelService.getServices(requestDTO, CommonConstant.FALSE));
    }

    @Operation(summary = "API get all product by service id")
    @GetMapping(UrlConstant.Service.GET_PRODUCTS_BY_SERVICE)
    public ResponseEntity<?> getProductsByService(@PathVariable Long serviceId, @Valid @ParameterObject PaginationSearchSortRequestDTO requestDTO) {
        return VsResponseUtil.ok(productService.getProductsByServiceId(serviceId, requestDTO));
    }

    @Tag(name = "hotel-service-controller-admin")
    @Operation(summary = "API create service")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PostMapping(value = UrlConstant.Service.CREATE_SERVICE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createServiceById(@Valid @ModelAttribute ServiceCreateDTO serviceCreateDTO,
                                               @Parameter(name = "principal", hidden = true)
                                               @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(hotelService.createService(serviceCreateDTO, principal));
    }

    @Tag(name = "hotel-service-controller-admin")
    @Operation(summary = "API update service by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PutMapping(value = UrlConstant.Service.UPDATE_SERVICE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateServiceById(@PathVariable Long serviceId,
                                               @Valid @ModelAttribute ServiceUpdateDTO serviceUpdateDTO,
                                               @Parameter(name = "principal", hidden = true)
                                               @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(hotelService.updateService(serviceId, serviceUpdateDTO, principal));
    }

    @Tag(name = "hotel-service-controller-admin")
    @Operation(summary = "API delete service by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @DeleteMapping(UrlConstant.Service.DELETE_SERVICE)
    public ResponseEntity<?> deleteServiceById(@PathVariable Long serviceId) {
        return VsResponseUtil.ok(hotelService.deleteService(serviceId));
    }

    @Tag(name = "hotel-service-controller-admin")
    @Operation(summary = "API delete service permanently by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @DeleteMapping(UrlConstant.Service.DELETE_SERVICE_PERMANENTLY)
    public ResponseEntity<?> deleteServicePermanentlyById(@PathVariable Long serviceId) {
        return VsResponseUtil.ok(hotelService.deleteServicePermanently(serviceId));
    }

    @Tag(name = "hotel-service-controller-admin")
    @Operation(summary = "API restore service by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PostMapping(UrlConstant.Service.RESTORE_SERVICE)
    public ResponseEntity<?> restoreServiceById(@PathVariable Long serviceId) {
        return VsResponseUtil.ok(hotelService.restoreService(serviceId));
    }

}

