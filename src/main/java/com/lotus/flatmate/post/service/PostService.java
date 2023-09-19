package com.lotus.flatmate.post.service;

import com.lotus.flatmate.post.dto.PostDto;

public interface PostService {

	PostDto createPost(PostDto postDto, Long userId);

}
