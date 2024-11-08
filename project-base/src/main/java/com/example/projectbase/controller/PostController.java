package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.PostCreateDTO;
import com.example.projectbase.domain.dto.PostUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSortRequestDTO;
import com.example.projectbase.security.AuthorizationInfo;
import com.example.projectbase.security.CurrentUserLogin;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.PostService;
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
public class PostController {

    private final PostService postService;

    @Operation(summary = "API get post by id")
    @GetMapping(UrlConstant.Post.GET_POST)
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        return VsResponseUtil.ok(postService.getPost(postId));
    }

    @Tag(name = "post-controller-admin")
    @Operation(summary = "API get all post for admin")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @GetMapping(UrlConstant.Post.GET_POSTS_FOR_ADMIN)
    public ResponseEntity<?> getPosts(@Valid @ParameterObject PaginationSortRequestDTO requestDTO,
                                      @RequestParam Boolean deleteFlag) {
        return VsResponseUtil.ok(postService.getPosts(requestDTO, deleteFlag));
    }

    @Operation(summary = "API get all post for user")
    @GetMapping(UrlConstant.Post.GET_POSTS)
    public ResponseEntity<?> getPosts(@Valid @ParameterObject PaginationSortRequestDTO requestDTO) {
        return VsResponseUtil.ok(postService.getPosts(requestDTO, CommonConstant.FALSE));
    }

    @Tag(name = "post-controller-admin")
    @Operation(summary = "API create post")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PostMapping(value = UrlConstant.Post.CREATE_POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPostById(@Valid @ModelAttribute PostCreateDTO postCreateDTO,
                                            @Parameter(name = "principal", hidden = true)
                                            @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(postService.createPost(postCreateDTO, principal));
    }

    @Tag(name = "post-controller-admin")
    @Operation(summary = "API update post by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PutMapping(value = UrlConstant.Post.UPDATE_POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePostById(@PathVariable Long postId,
                                            @Valid @ModelAttribute PostUpdateDTO postUpdateDTO,
                                            @Parameter(name = "principal", hidden = true)
                                            @CurrentUserLogin UserPrincipal principal) {
        return VsResponseUtil.ok(postService.updatePost(postId, postUpdateDTO, principal));
    }

    @Tag(name = "post-controller-admin")
    @Operation(summary = "API delete post by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @DeleteMapping(UrlConstant.Post.DELETE_POST)
    public ResponseEntity<?> deletePostById(@PathVariable Long postId) {
        return VsResponseUtil.ok(postService.deletePost(postId));
    }

    @Tag(name = "post-controller-admin")
    @Operation(summary = "API delete post permanently by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @DeleteMapping(UrlConstant.Post.DELETE_POST_PERMANENTLY)
    public ResponseEntity<?> deletePostPermanentlyById(@PathVariable Long postId) {
        return VsResponseUtil.ok(postService.deletePostPermanently(postId));
    }

    @Tag(name = "post-controller-admin")
    @Operation(summary = "API restore post by id")
    @AuthorizationInfo(role = { RoleConstant.ADMIN })
    @PostMapping(UrlConstant.Post.RESTORE_POST)
    public ResponseEntity<?> restorePostById(@PathVariable Long postId) {
        return VsResponseUtil.ok(postService.restorePost(postId));
    }

}

