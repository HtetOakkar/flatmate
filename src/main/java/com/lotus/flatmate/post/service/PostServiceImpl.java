package com.lotus.flatmate.post.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.imaging.ImageReadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lotus.flatmate.aparment.dto.ApartmentDto;
import com.lotus.flatmate.aparment.entity.Apartment;
import com.lotus.flatmate.aparment.repository.ApartmentRepository;
import com.lotus.flatmate.model.exception.BadRequestException;
import com.lotus.flatmate.model.exception.RecordNotFoundException;
import com.lotus.flatmate.model.exception.UnauthorizedActionException;
import com.lotus.flatmate.picture.entity.Picture;
import com.lotus.flatmate.picture.repository.PictureRepository;
import com.lotus.flatmate.post.dto.AllPostDto;
import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.post.entity.Post;
import com.lotus.flatmate.post.entity.SavedPost;
import com.lotus.flatmate.post.mapper.PostMapper;
import com.lotus.flatmate.post.repository.PostRepository;
import com.lotus.flatmate.post.repository.SavedPostRepository;
import com.lotus.flatmate.post.request.PostUpdateRequest;
import com.lotus.flatmate.service.ImageUploadService;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.mapper.UserMapper;
import com.lotus.flatmate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;

	private final PostMapper postMapper;

	private final UserRepository userRepository;

	private final ApartmentRepository apartmentRepository;

	private final PictureRepository pictureRepository;

	private final UserMapper userMapper;

	private final ImageUploadService imageUploadService;

	private final SavedPostRepository savedPostRepository;

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
			Optional<Long> latestPostId = postRepository.getLargestId();
			if (latestPostId.isPresent()) {
				cursor = latestPostId.get() + 1;
			} else {
				throw new RecordNotFoundException("There is no post for this moment.");
			}
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

	@Override
	public PostDto updatePost(Long id, Long userId, MultipartFile[] images, PostUpdateRequest request)
			throws IOException, ImageReadException {

		User user = userRepository.findById(userId).get();
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Post not found wiht Id : " + id));
		if (!post.getUser().equals(user)) {
			throw new UnauthorizedActionException("You are not authorized to update this post.");
		}
		if (request.getRemovedImageIds().length > 0) {
			for (Long pictureId : request.getRemovedImageIds()) {
				Picture picture = pictureRepository.findById(pictureId).orElseThrow(
						() -> new RecordNotFoundException("Picture with id '" + pictureId + "' not found"));
				imageUploadService.deleteImage(picture.getUrl(), "post_photos");
				post.getPictures().remove(picture);
				pictureRepository.delete(picture);
			}
		}

		if (images.length != 0) {
			for (MultipartFile multipartFile : images) {
				if (multipartFile != null) {
					String url = imageUploadService.uploadImage(multipartFile, "post_photos");
					Picture picture = new Picture();
					picture.setUrl(url);
					picture.setPost(post);
					post.getPictures().add(pictureRepository.save(picture));
				}
			}
		}

		post.setContract(request.getContract());
		post.setDescription(request.getDescription());
		post.setPrice(request.getPrice());
		post.setTenants(request.getTenants());
		post.setState(request.getState());
		post.setTownship(request.getTownship());
		post.setAdditional(request.getAdditional());
		post.getApartment().setApartmentType(request.getApartment().getApartmentType());
		post.getApartment().setFloor(request.getApartment().getFloor());
		post.getApartment().setWidth(request.getApartment().getWidth());
		post.getApartment().setLength(request.getApartment().getLength());

		return postMapper.mapToDto(postRepository.save(post));
	}

	@Override
	public PostDto savePost(Long id, Long userId) {
		User user = userRepository.findById(userId).get();
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Post not found with id : " + id));
		if (user.getPosts().contains(post)) {
			throw new BadRequestException("You can't save your own post.");
		}
		SavedPost savedPost = new SavedPost();
		savedPost.setUser(user);
		savedPost.setPost(post);
		SavedPost storedSavedPost = savedPostRepository.save(savedPost);

		return postMapper.mapToDto(storedSavedPost.getPost());
	}

	@Override
	public boolean isPostSaved(Long id, Long userId) {
		Long count = savedPostRepository.existsByPostIdAndUserId(id, userId);
		return count == 1L;
	}

	@Override
	public List<PostDto> getUserSavedPost(Long userId) {
		List<Post> posts = savedPostRepository.findbyUserId(userId);

		return posts.stream().map(postMapper::mapToDto).toList();
	}

	@Override
	public PostDto unsavePost(Long id, Long userId) {
		Post post = postRepository.findById(id).get();
		SavedPost savedPost = savedPostRepository.findByPostAndUser(id, userId);
		savedPostRepository.delete(savedPost);
		return postMapper.mapToDto(post);
	}

	@Override
	public PostDto updateTenant(int tenant, Long id, Long userId) {
		User user = userRepository.findById(userId).get();
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Post Not Found with id : " + id));
		if (!post.getUser().equals(user)) {
			throw new UnauthorizedActionException("You are not authorized to update this post.");
		}
		
		post.setTenants(tenant);
		
		return postMapper.mapToDto(postRepository.save(post));
	}

}
