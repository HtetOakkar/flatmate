package com.lotus.flatmate.post.mapper;

import org.springframework.stereotype.Component;

import com.lotus.flatmate.aparment.mapper.ApartmentMapper;
import com.lotus.flatmate.picture.mapper.PictureMapper;
import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.post.entity.Post;
import com.lotus.flatmate.post.request.PostRequest;
import com.lotus.flatmate.post.response.PostDetailsResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostMapperImpl implements PostMapper{
	
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
	public PostDetailsResponse mapToResponse(PostDto postDto) {
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
		return response;
	}

}
