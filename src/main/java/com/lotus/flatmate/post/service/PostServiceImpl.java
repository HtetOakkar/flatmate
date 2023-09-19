package com.lotus.flatmate.post.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lotus.flatmate.aparment.dto.ApartmentDto;
import com.lotus.flatmate.aparment.entity.Apartment;
import com.lotus.flatmate.aparment.repository.ApartmentRepository;
import com.lotus.flatmate.picture.entity.Picture;
import com.lotus.flatmate.picture.repository.PictureRepository;
import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.post.entity.Post;
import com.lotus.flatmate.post.mapper.PostMapper;
import com.lotus.flatmate.post.repository.PostRepository;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
	
	private final PostRepository postRepository;
	
	private final PostMapper postMapper;
	
	private final UserRepository userRepository;
	
	private final ApartmentRepository apartmentRepository;
	
	private final PictureRepository pictureRepository;

	@Override
	public PostDto createPost(PostDto postDto, Long userId) {
		User user = userRepository.findById(userId).get();
		Post post = new Post();
		post.setId(postDto.getId());
		post.setContract(postDto.getContract());
		post.setDescription(postDto.getDescription());
		post.setPrice(postDto.getPrice());
		post.setTenants(postDto.getTenants());
		post.setState(postDto.getState());
		post.setTownship(postDto.getTownship());
		post.setAdditional(postDto.getAdditional());
		
		Apartment apartment = new Apartment();
		ApartmentDto apartmentDto = postDto.getApartment();
		apartment.setFloor(apartmentDto.getFloor());
		apartment.setLength(apartmentDto.getLength());
		apartment.setWidth(apartmentDto.getWidth());
		apartment.setApartmentType(apartmentDto.getApartmentType());	
		post.setApartment(apartmentRepository.save(apartment));
		List<Picture> pictures = new ArrayList<>();
		postDto.getPictures().stream().forEach(pictureDto -> {
			Picture picture = new Picture();
			picture.setId(pictureDto.getId());
			picture.setUrl(pictureDto.getUrl());
			pictures.add(pictureRepository.save(picture));
		});
		post.setPictures(pictures);
		post.setUser(user);
		return postMapper.mapToDto(postRepository.save(post));
	}

}
