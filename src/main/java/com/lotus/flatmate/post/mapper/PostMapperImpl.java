package com.lotus.flatmate.post.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.lotus.flatmate.aparment.mapper.ApartmentMapper;
import com.lotus.flatmate.picture.mapper.PictureMapper;
import com.lotus.flatmate.post.dto.AllPostDto;
import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.post.dto.SavedPostDto;
import com.lotus.flatmate.post.entity.Post;
import com.lotus.flatmate.post.entity.SavedPost;
import com.lotus.flatmate.post.request.PostRequest;
import com.lotus.flatmate.post.response.PostDetailsResponse;
import com.lotus.flatmate.post.response.PostPaginationResponse;
import com.lotus.flatmate.post.response.PostResponse;
import com.lotus.flatmate.post.response.PostUserResponse;
import com.lotus.flatmate.user.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostMapperImpl implements PostMapper {

	private final ApartmentMapper apartmentMapper;

	private final PictureMapper pictureMapper;

	@Override
	public PostDto mapToDto(PostRequest request) {
		PostDto postDto = new PostDto();
		postDto.setContract(request.getContract());
		postDto.setDescription(request.getDescription());
		postDto.setPrice(request.getPrice());
		postDto.setTenants(request.getTenants());
		postDto.setState(request.getState());
		postDto.setTownship(request.getTownship());
		postDto.setAdditional(request.getAdditional());
		postDto.setApartment(apartmentMapper.mapToDto(request.getApartment()));
		return postDto;
	}

	@Override
	public Post mapToPostEntity(PostDto postDto) {
		Post post = new Post();
		post.setId(postDto.getId());
		post.setContract(postDto.getContract());
		post.setDescription(postDto.getDescription());
		post.setPrice(postDto.getPrice());
		post.setTenants(postDto.getTenants());
		post.setState(postDto.getState());
		post.setTownship(postDto.getTownship());
		post.setAdditional(postDto.getAdditional());
		post.setApartment(apartmentMapper.mapToEntity(postDto.getApartment()));
		post.setPictures(postDto.getPictures().stream().map(pictureMapper::mapToEntity).toList());
		return post;
	}

	@Override
	public PostDto mapToDto(Post post) {
		if (post == null) {
			return null;
		}

		PostDto postDto = new PostDto();
		postDto.setId(post.getId());
		postDto.setContract(post.getContract());
		postDto.setDescription(post.getDescription());
		postDto.setPrice(post.getPrice());
		postDto.setTenants(post.getTenants());
		postDto.setState(post.getState());
		postDto.setTownship(post.getTownship());
		postDto.setAdditional(post.getAdditional());
		postDto.setApartment(apartmentMapper.mapToDto(post.getApartment()));
		postDto.setCreatedAt(post.getCreatedAt());
		postDto.setUpdatedAt(post.getUpdatedAt());
		postDto.setPictures(post.getPictures().stream().map(pictureMapper::mapToDto).toList());
		return postDto;
	}

	@Override
	public PostDetailsResponse mapToResponse(PostDto postDto, UserDto userDto) {
		PostDetailsResponse response = new PostDetailsResponse();
		response.setId(postDto.getId());
		response.setContract(postDto.getContract());
		response.setDescription(postDto.getDescription());
		response.setPrice(postDto.getPrice());
		response.setTenants(postDto.getTenants());
		response.setState(postDto.getState());
		response.setTownship(postDto.getTownship());
		response.setAdditional(postDto.getAdditional());
		response.setUpdatedAt(postDto.getUpdatedAt());
		response.setCreatedAt(postDto.getCreatedAt());
		response.setApartment(apartmentMapper.mapToResponse(postDto.getApartment()));
		response.setPictures(postDto.getPictures().stream().map(pictureMapper::mapToResponse).toList());
		PostUserResponse postOwner = new PostUserResponse(userDto.getId(), userDto.getUsername(),
				userDto.getMobileNumber(), userDto.getProfileUrl());
		response.setPostOwner(postOwner);
		return response;
	}

	@Override
	public PostPaginationResponse mapToPostPageResponse(Page<AllPostDto> postPage, UserDto userDto) {
		List<PostDetailsResponse> postsResponses = new ArrayList<>();
		for (AllPostDto allPostDto : postPage.getContent()) {
			PostDetailsResponse response = new PostDetailsResponse();
			response.setId(allPostDto.getId());
			response.setContract(allPostDto.getContract());
			response.setDescription(allPostDto.getDescription());
			response.setPrice(allPostDto.getPrice());
			response.setTenants(allPostDto.getTenants());
			response.setState(allPostDto.getState());
			response.setTownship(allPostDto.getTownship());
			response.setAdditional(allPostDto.getAdditional());
			response.setCreatedAt(allPostDto.getCreatedAt());
			response.setUpdatedAt(allPostDto.getUpdatedAt());
			response.setApartment(apartmentMapper.mapToResponse(allPostDto.getApartment()));
			response.setPictures(allPostDto.getPictures().stream().map(pictureMapper::mapToResponse).toList());
			PostUserResponse postOwner = new PostUserResponse(allPostDto.getUser().getId(),
					allPostDto.getUser().getUsername(), allPostDto.getUser().getMobileNumber(),
					allPostDto.getUser().getProfileUrl());
			response.setPostOwner(postOwner);
			if (userDto.getSavedPostDtos() != null) {
				userDto.getSavedPostDtos().stream().forEach(s -> {
					response.setSaved(s.getPostDto().getId() == allPostDto.getId());
				});
			}
			postsResponses.add(response);
		}

		PostPaginationResponse response = new PostPaginationResponse();
		response.setPosts(postsResponses);
		if (postPage.hasNext()) {
			response.setCursor(postsResponses.get(postsResponses.size() - 1).getId());
		} else {
			response.setCursor(0L);
		}
		response.setHasNext(postPage.hasNext());
		return response;
	}

	@Override
	public PostResponse mapToPostResponse(PostDto postDto) {
		PostResponse response = new PostResponse();
		response.setId(postDto.getId());
		response.setContract(postDto.getContract());
		response.setDescription(postDto.getDescription());
		response.setPrice(postDto.getPrice());
		response.setCreatedAt(postDto.getCreatedAt());
		response.setUpdatedAt(postDto.getUpdatedAt());
		response.setState(postDto.getState());
		response.setTownship(postDto.getTownship());
		response.setAdditional(postDto.getAdditional());
		response.setTenants(postDto.getTenants());
		response.setApartment(apartmentMapper.mapToResponse(postDto.getApartment()));
		response.setPictures(postDto.getPictures().stream().map(pictureMapper::mapToResponse).toList());
		return response;
	}

	@Override
	public SavedPostDto mapToSavedPostDto(SavedPost savedPost) {
		if (savedPost == null) {
			return null;
		}
		SavedPostDto savedPostDto = new SavedPostDto();
		savedPostDto.setId(savedPost.getId());
		savedPostDto.setPostDto(mapToDto(savedPost.getPost()));
		return savedPostDto;
	}

}
