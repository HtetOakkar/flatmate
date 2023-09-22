package com.lotus.flatmate.post.service;

import java.util.List;

import com.lotus.flatmate.post.dto.PostDto;

public interface PostService {

	PostDto createPost(PostDto postDto, Long userId);

	List<PostDto> getUserPosts(Long userId);

	PostDto getPostDetails(Long id);

}
