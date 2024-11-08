package com.example.projectbase.service.impl;

import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.CommonMessage;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.SortByDataConstant;
import com.example.projectbase.domain.dto.PostCreateDTO;
import com.example.projectbase.domain.dto.PostDTO;
import com.example.projectbase.domain.dto.PostUpdateDTO;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDTO;
import com.example.projectbase.domain.dto.pagination.PaginationSortRequestDTO;
import com.example.projectbase.domain.dto.pagination.PagingMeta;
import com.example.projectbase.domain.dto.response.CommonResponseDTO;
import com.example.projectbase.domain.entity.Media;
import com.example.projectbase.domain.entity.Post;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.MediaMapper;
import com.example.projectbase.domain.mapper.PostMapper;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.projection.PostProjection;
import com.example.projectbase.repository.PostRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.MediaService;
import com.example.projectbase.service.PostService;
import com.example.projectbase.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final MediaService mediaService;

    private final PostMapper postMapper;

    private final MediaMapper mediaMapper;

    @Override
    public PostDTO getPost(Long postId) {
        PostProjection postProjection = postRepository.findPostById(postId);
        checkNotFoundPostById(postProjection, postId);
        return toPostDTO(postProjection);
    }

    @Override
    public PaginationResponseDTO<PostDTO> getPosts(PaginationSortRequestDTO requestDTO, Boolean deleteFlag) {
        //Pagination
        Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.POST);
        Page<PostProjection> posts = postRepository.findAllPost(deleteFlag, pageable);
        //Create Output
        PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.POST, posts);
        return new PaginationResponseDTO<PostDTO>(meta, toPostDTOs(posts, deleteFlag));
    }

    @Override
    @Transactional
    public PostDTO createPost(PostCreateDTO createDTO, UserPrincipal currentUser) {
        User creator = userRepository.getUser(currentUser);
        Post post = postMapper.createDtoToPost(createDTO);
        post.setUser(creator);
        Post postCreate = postRepository.save(post);
        if (CollectionUtils.isNotEmpty(createDTO.getFiles())) {
            Set<Media> medias = mediaService.createMediasForPost(post, createDTO.getFiles());
            postCreate.setMedias(medias);
        }
        return postMapper.toPostDTO(postCreate, creator, creator);
    }

    @Override
    @Transactional
    public PostDTO updatePost(Long postId, PostUpdateDTO updateDTO, UserPrincipal currentUser) {
        Optional<Post> currentPost = postRepository.findById(postId);
        checkNotFoundPostById(currentPost, postId);
        postMapper.updatePostFromDTO(updateDTO, currentPost.get());
        User updater = userRepository.getUser(currentUser);
        //Delete media if not found in mediaDTO
        Post postUpdate = mediaService.deleteMediaFromPostUpdate(currentPost.get(), updateDTO);
        //add file if exist
        if (updateDTO.getFiles() != null) {
            Set<Media> medias = mediaService.createMediasForPost(currentPost.get(), updateDTO.getFiles());
            postUpdate.getMedias().addAll(medias);
            postRepository.save(postUpdate);
        }
        User creator = userRepository.findById(postUpdate.getCreatedBy()).get();
        return postMapper.toPostDTO(postUpdate, creator, updater);
    }

    @Override
    @Transactional
    public CommonResponseDTO deletePost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        checkNotFoundPostById(post, postId);
        post.get().setDeleteFlag(CommonConstant.TRUE);
        postRepository.save(post.get());
        return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
    }

    @Override
    public CommonResponseDTO deletePostPermanently(Long postId) {
        Optional<Post> room = postRepository.findByIdAndIsDeleteFlag(postId);
        checkNotFoundPostIsDeleteFlagById(room, postId);
        postRepository.delete(room.get());
        return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
    }

    @Override
    public CommonResponseDTO restorePost(Long postId) {
        Optional<Post> room = postRepository.findByIdAndIsDeleteFlag(postId);
        checkNotFoundPostIsDeleteFlagById(room, postId);
        room.get().setDeleteFlag(CommonConstant.FALSE);
        postRepository.save(room.get());
        return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.RESTORE_SUCCESS);
    }

    private List<PostDTO> toPostDTOs(Page<PostProjection> postProjections) {
        List<PostDTO> postDTOs = new LinkedList<>();
        for(PostProjection postProjection : postProjections) {
            postDTOs.add(toPostDTO(postProjection));
        }
        return postDTOs;
    }

    private List<PostDTO> toPostDTOs(Page<PostProjection> postProjections, Boolean deleteFlag) {
        List<PostDTO> postDTOs = new LinkedList<>();
        if(deleteFlag) {
            for(PostProjection postProjection : postProjections) {
                postDTOs.add(toPostDTOIsDeleteFlag(postProjection));
            }
        } else  {
            for(PostProjection postProjection : postProjections) {
                postDTOs.add(toPostDTO(postProjection));
            }
        }
        return postDTOs;
    }

    private PostDTO toPostDTO(PostProjection postProjection) {
        PostDTO postDTO = postMapper.postProjectionToPostDTO(postProjection);
        List<Media> medias = mediaService.getMediaByPost(postDTO.getId());
        postDTO.setMedias(mediaMapper.toMediaDTOs(medias));
        return postDTO;
    }

    private PostDTO toPostDTOIsDeleteFlag(PostProjection postProjection) {
        PostDTO postDTO = postMapper.postProjectionToPostDTO(postProjection);
        List<Media> medias = mediaService.getMediaByPostAndIsDeleteFlag(postDTO.getId());
        postDTO.setMedias(mediaMapper.toMediaDTOs(medias));
        return postDTO;
    }

    @Override
    @Transactional
    public void deletePostByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords) {
        postRepository.deleteByDeleteFlag(isDeleteFlag, daysToDeleteRecords);
    }

    private void checkNotFoundPostById(Optional<Post> post, Long postId) {
        if (post.isEmpty()) {
            throw new NotFoundException(String.format(ErrorMessage.Post.ERR_NOT_FOUND_ID, postId));
        }
    }

    private void checkNotFoundPostIsDeleteFlagById(Optional<Post> post, Long postId) {
        if (post.isEmpty()) {
            throw new NotFoundException(String.format(ErrorMessage.Post.ERR_NOT_FOUND_ID_IN_TRASH, postId));
        }
    }

    private void checkNotFoundPostById(PostProjection projection, Long postId) {
        if (ObjectUtils.isEmpty(projection)) {
            throw new NotFoundException(String.format(ErrorMessage.Post.ERR_NOT_FOUND_ID, postId));
        }
    }

}

