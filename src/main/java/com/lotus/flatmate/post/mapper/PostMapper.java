package com.lotus.flatmate.post.mapper;

import org.springframework.data.domain.Page;

import com.lotus.flatmate.post.dto.AllPostDto;
import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.post.dto.SavedPostDto;
import com.lotus.flatmate.post.entity.Post;
import com.lotus.flatmate.post.entity.SavedPost;
import com.lotus.flatmate.post.request.PostRequest;
import com.lotus.flatmate.post.response.PostDetailsResponse;
import com.lotus.flatmate.post.response.PostPaginationResponse;
import com.lotus.flatmate.post.response.PostResponse;
import com.lotus.flatmate.user.dto.UserDto;

public interface PostMapper {

	PostDto mapToDto(PostRequest request);

	Post mapToPostEntity(PostDto postDto);

	PostDto mapToDto(Post post);

	PostDetailsResponse mapToResponse(PostDto postDto, UserDto userDto);

	PostPaginationResponse mapToPostPageResponse(Page<AllPostDto> postPage, UserDto userDto);

	PostResponse mapToPostResponse(PostDto postDto);

	SavedPostDto mapToSavedPostDto(SavedPost savePost);
}
