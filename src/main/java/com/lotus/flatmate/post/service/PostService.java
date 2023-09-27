package com.lotus.flatmate.post.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lotus.flatmate.post.dto.AllPostDto;
import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.user.dto.UserDto;

public interface PostService {

	PostDto createPost(PostDto postDto, Long userId);

	List<PostDto> getUserPosts(Long userId);

	PostDto getPostDetails(Long id);

	Page<AllPostDto> getAllPosts(Long cursor, int limit, Long userId);

	UserDto getUserFromPost(Long id);

	void deletePost(Long id, Long userId);

}
