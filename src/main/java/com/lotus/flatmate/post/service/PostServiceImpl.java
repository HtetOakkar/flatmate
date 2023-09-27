package com.lotus.flatmate.post.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lotus.flatmate.aparment.dto.ApartmentDto;
import com.lotus.flatmate.aparment.entity.Apartment;
import com.lotus.flatmate.aparment.repository.ApartmentRepository;
import com.lotus.flatmate.exception.RecordNotFoundException;
import com.lotus.flatmate.exception.UnauthorizedActionException;
import com.lotus.flatmate.picture.entity.Picture;
import com.lotus.flatmate.picture.repository.PictureRepository;
import com.lotus.flatmate.post.dto.AllPostDto;
import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.post.entity.Post;
import com.lotus.flatmate.post.mapper.PostMapper;
import com.lotus.flatmate.post.repository.PostRepository;
import com.lotus.flatmate.service.ImageUploadService;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.mapper.UserMapper;
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
	
	private final UserMapper userMapper;
	
	private final ImageUploadService imageUploadService;

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
		post.setUser(user);
		Post savedPost = postRepository.save(post);
		Apartment apartment = new Apartment();
		ApartmentDto apartmentDto = postDto.getApartment();
		apartment.setFloor(apartmentDto.getFloor());
		apartment.setLength(apartmentDto.getLength());
		apartment.setWidth(apartmentDto.getWidth());
		apartment.setApartmentType(apartmentDto.getApartmentType());	
		apartment.setPost(savedPost);
		savedPost.setApartment(apartmentRepository.save(apartment));
		List<Picture> pictures = new ArrayList<>();
		postDto.getPictures().stream().forEach(pictureDto -> {
			Picture picture = new Picture();
			picture.setId(pictureDto.getId());
			picture.setUrl(pictureDto.getUrl());
			picture.setPost(savedPost);
			pictures.add(pictureRepository.save(picture));
		});
		savedPost.setPictures(pictures);
		return postMapper.mapToDto(savedPost);
	}

	@Override
	public List<PostDto> getUserPosts(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RecordNotFoundException("User not found with id : " + userId));
		List<PostDto> postDtos = user.getPosts().stream().map(postMapper::mapToDto).collect(Collectors.toList());
		postDtos.sort(Comparator.comparingLong(PostDto::getId).reversed());
		return postDtos;
	}

	@Override
	public PostDto getPostDetails(Long id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Post not found with id : " + id));
		return postMapper.mapToDto(post);
	}

	@Override
	public Page<AllPostDto> getAllPosts(Long cursor, int limit, Long userId) {
		if (cursor == null) {
			cursor = postRepository.getLargestId() + 1;
		}
		Pageable pageble = PageRequest.of(0, limit);
		return postRepository.findAllPageDto(cursor, pageble, userId);
	}

	@Override
	public UserDto getUserFromPost(Long id) {
		User user = postRepository.getUserByPostId(id);
		return userMapper.mapToUserDto(user);
	}

	@Override
	public void deletePost(Long id, Long userId) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Post Not found wiht ID : " + id));
		User user = userRepository.findById(userId).get();
		if (!post.getUser().equals(user)) {
			throw new UnauthorizedActionException("You are not authorized to delete this post");
		}
		post.getPictures().stream().forEach(i -> {
			imageUploadService.deleteImage(i.getUrl(), "post_photos");
		});
		postRepository.delete(post);
	}


}
