package com.example.projectbase.service;

import com.example.projectbase.domain.dto.PostCreateDTO;
import com.example.projectbase.domain.dto.PostDTO;
import com.example.projectbase.domain.dto.PostUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSortRequestDTO;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.security.UserPrincipal;

public interface PostService {

    PostDTO getPost(Long postId);

    PaginationResponseDTO<PostDTO> getPosts(PaginationSortRequestDTO requestDTO, Boolean deleteFlag);

    PostDTO createPost(PostCreateDTO createDTO, UserPrincipal currentUser);

    PostDTO updatePost(Long postId, PostUpdateDTO updateDTO, UserPrincipal currentUser);

    CommonResponseDTO deletePost(Long postId);

    CommonResponseDTO deletePostPermanently(Long postId);

    CommonResponseDTO restorePost(Long postId);

    void deletePostByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

}
