package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.ProductCreateDTO;
import com.example.projectbase.domain.dto.ProductUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSearchSortRequestDTO;
import com.example.projectbase.security.AuthorizationInfo;
import com.example.projectbase.security.CurrentUserLogin;
import com.example.projectbase.security.UserPrincipal;
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
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "API get product by id")
    @GetMapping(UrlConstant.Product.GET_PRODUCT)
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        return VsResponseUtil.ok(productService.getProduct(productId));
    }

    @Tag(name = "product-controller-admin")
    @Operation(summary = "API get all product for admin")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @GetMapping(UrlConstant.Product.GET_PRODUCTS_FOR_ADMIN)
    public ResponseEntity<?> getProductsForAdmin(@Valid @ParameterObject PaginationSearchSortRequestDTO requestDTO,
                                                 @RequestParam Boolean deleteFlag) {
        return VsResponseUtil.ok(productService.getProducts(requestDTO, deleteFlag));
    }

    @Operation(summary = "API get all product for user")
    @GetMapping(UrlConstant.Product.GET_PRODUCTS)
    public ResponseEntity<?> getProductsForUser(@Valid @ParameterObject PaginationSearchSortRequestDTO requestDTO) {
        return VsResponseUtil.ok(productService.getProducts(requestDTO, CommonConstant.FALSE));
    }

    @Tag(name = "product-controller-admin")
    @Operation(summary = "API create product")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PostMapping(value = UrlConstant.Product.CREATE_PRODUCT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProductById(@Valid @ModelAttribute ProductCreateDTO productCreateDTO,
                                               @Parameter(name = "principal", hidden = true)
                                               @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(productService.createProduct(productCreateDTO, principal));
    }

    @Tag(name = "product-controller-admin")
    @Operation(summary = "API update product by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PatchMapping(value = UrlConstant.Product.UPDATE_PRODUCT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProductById(@PathVariable Long productId,
                                               @Valid @ModelAttribute ProductUpdateDTO productUpdateDTO,
                                               @Parameter(name = "principal", hidden = true)
                                               @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(productService.updateProduct(productId, productUpdateDTO, principal));
    }

    @Tag(name = "product-controller-admin")
    @Operation(summary = "API delete product by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @DeleteMapping(UrlConstant.Product.DELETE_PRODUCTS)
    public ResponseEntity<?> deleteProductById(@PathVariable Long productId) {
        return VsResponseUtil.ok(productService.deleteProduct(productId));
    }

    @Tag(name = "product-controller-admin")
    @Operation(summary = "API delete product permanently by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @DeleteMapping(UrlConstant.Product.DELETE_PRODUCT_PERMANENTLY)
    public ResponseEntity<?> deleteProductPermanentlyById(@PathVariable Long productId) {
        return VsResponseUtil.ok(productService.deleteProductPermanently(productId));
    }

    @Tag(name = "product-controller-admin")
    @Operation(summary = "API restore product by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PostMapping(UrlConstant.Product.RESTORE_PRODUCT)
    public ResponseEntity<?> restoreProductById(@PathVariable Long productId) {
        return VsResponseUtil.ok(productService.restoreProduct(productId));
    }

}
