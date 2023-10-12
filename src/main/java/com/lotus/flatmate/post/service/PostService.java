package com.lotus.flatmate.post.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.imaging.ImageReadException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.lotus.flatmate.post.dto.AllPostDto;
import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.post.request.PostUpdateRequest;
import com.lotus.flatmate.user.dto.UserDto;

public interface PostService {

	PostDto createPost(PostDto postDto, Long userId);

	List<PostDto> getUserPosts(Long userId);

	PostDto getPostDetails(Long id);

	Page<AllPostDto> getAllPosts(Long cursor, int limit, Long userId);

	UserDto getUserFromPost(Long id);

	void deletePost(Long id, Long userId);

	PostDto updatePost(Long id, Long userId,  MultipartFile[] images, PostUpdateRequest request) throws IOException, ImageReadException;

	PostDto savePost(Long id, Long userId);

	boolean isPostSaved(Long id, Long userId);

	List<PostDto> getUserSavedPost(Long userId);

	PostDto unsavePost(Long id, Long userId);

	PostDto updateTenant(int tenant, Long id, Long userId);

}
