package com.example.projectbase.service;

import com.example.projectbase.domain.dto.ProductCreateDTO;
import com.example.projectbase.domain.dto.ProductDTO;
import com.example.projectbase.domain.dto.ProductUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.security.UserPrincipal;

public interface ProductService {

    ProductDTO getProduct(Long productId);

    PaginationResponseDTO<ProductDTO> getProducts(PaginationSearchSortRequestDTO requestDTO, Boolean deleteFlag);

    PaginationResponseDTO<ProductDTO> getProductsByServiceId(Long serviceId, PaginationSearchSortRequestDTO requestDTO);

    ProductDTO createProduct(ProductCreateDTO productCreateDTO, UserPrincipal principal);

    ProductDTO updateProduct(Long productId, ProductUpdateDTO productUpdateDTO, UserPrincipal principal);

    CommonResponseDTO deleteProduct(Long productId);

    CommonResponseDTO deleteProductPermanently(Long productId);

    CommonResponseDTO restoreProduct(Long productId);

    void deleteProductByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

}