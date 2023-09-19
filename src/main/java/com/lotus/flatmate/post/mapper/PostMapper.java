package com.lotus.flatmate.post.mapper;

import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.post.entity.Post;
import com.lotus.flatmate.post.request.PostRequest;
import com.lotus.flatmate.post.response.PostDetailsResponse;

public interface PostMapper {

	PostDto mapToDto(PostRequest request);

	Post mapToPostEntity(PostDto postDto);

	PostDto mapToDto(Post post);

	PostDetailsResponse mapToResponse(PostDto savedPostDto);

}
