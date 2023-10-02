package com.lotus.flatmate.post.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lotus.flatmate.auth.response.ApiResponse;
import com.lotus.flatmate.model.exception.RecordAlreadyExistException;
import com.lotus.flatmate.model.exception.RecordNotFoundException;
import com.lotus.flatmate.picture.dto.PictureDto;
import com.lotus.flatmate.post.dto.AllPostDto;
import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.post.mapper.PostMapper;
import com.lotus.flatmate.post.request.PostRequest;
import com.lotus.flatmate.post.request.PostUpdateRequest;
import com.lotus.flatmate.post.response.PostDetailsResponse;
import com.lotus.flatmate.post.response.PostPaginationResponse;
import com.lotus.flatmate.post.response.PostResponse;
import com.lotus.flatmate.post.service.PostService;
import com.lotus.flatmate.security.CurrentUser;
import com.lotus.flatmate.security.UserPrincipal;
import com.lotus.flatmate.service.ImageUploadService;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

	private final ImageUploadService imageUploadService;

	private final PostMapper postMapper;

	private final PostService postService;

	private final UserService userService;

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasRole('ROLE_USER')")
	public PostDetailsResponse createPost(@CurrentUser UserPrincipal userPrincipal,
			@RequestPart(value = "images") MultipartFile[] images, @RequestPart(value = "data") PostRequest request)
			throws IOException {
		List<PictureDto> pictureDtos = new ArrayList<>();
		if (images.length > 0) {

			for (MultipartFile multipartFile : images) {
				if (multipartFile != null) {
					String url = imageUploadService.uploadImage(multipartFile, "post_photos");
					PictureDto pictureDto = new PictureDto();
					pictureDto.setUrl(url);
					pictureDtos.add(pictureDto);
				}
			}
		}
		UserDto userDto = userService.getById(userPrincipal.getId());
		PostDto postDto = postMapper.mapToDto(request);
		postDto.setPictures(pictureDtos);
		PostDto savedPostDto = postService.createPost(postDto, userPrincipal.getId());
		PostDetailsResponse response = postMapper.mapToResponse(savedPostDto, userDto);
		return response;
	}

	@GetMapping("/me")
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<PostDetailsResponse> getUserPosts(@CurrentUser UserPrincipal userPrincipal) {
		List<PostDto> postDtos = postService.getUserPosts(userPrincipal.getId());
		UserDto userDto = userService.getById(userPrincipal.getId());
		return postDtos.stream().map(p -> postMapper.mapToResponse(p, userDto)).toList();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public PostDetailsResponse getPostDetails(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		PostDto postDto = postService.getPostDetails(id);
		UserDto userDto = postService.getUserFromPost(id);
		PostDetailsResponse response = postMapper.mapToResponse(postDto, userDto);
		boolean isSaved = postService.isPostSaved(id, currentUser.getId());
		response.setSaved(isSaved);
		return response;
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public PostPaginationResponse getAllPost(@RequestParam(name = "cursor", required = false) Long cursor,
			@RequestParam(defaultValue = "10", required = false) int limit, @CurrentUser UserPrincipal userPrincipal) {
		Page<AllPostDto> postPage = postService.getAllPosts(cursor, limit, userPrincipal.getId());
		UserDto userDto = userService.getById(userPrincipal.getId());
		return postMapper.mapToPostPageResponse(postPage, userDto);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ApiResponse deletePost(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		postService.deletePost(id, currentUser.getId());
		return new ApiResponse(true, "Post successfully deleted.");
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasRole('ROLE_USER')")
	public PostResponse updatePost(@RequestPart(value = "images") MultipartFile[] images,
			@RequestPart(value = "data") PostUpdateRequest request, @PathVariable Long id,
			@CurrentUser UserPrincipal currentUser) throws IOException {

		PostDto postDto = postService.updatePost(id, currentUser.getId(), images, request);
		return postMapper.mapToPostResponse(postDto);
	}
	
	@PostMapping("/{id}/save")
	@PreAuthorize("hasRole('ROLE_USER')")
	public PostDetailsResponse saveOrUnsavePost(@PathVariable Long id, @CurrentUser UserPrincipal currentUser, @RequestParam boolean save) {
		UserDto userDto = postService.getUserFromPost(id);
		PostDto postDto;
		if (save) {
			if (postService.isPostSaved(id, currentUser.getId())) {
				throw new RecordAlreadyExistException("You have already saved this post.");
			}
			postDto = postService.savePost(id, currentUser.getId());
		} else {
			if (!postService.isPostSaved(id, currentUser.getId())) {
				throw new RecordNotFoundException("You haven't saved this post.");
			}
			postDto = postService.unsavePost(id, currentUser.getId());
		}
		PostDetailsResponse postResponse =  postMapper.mapToResponse(postDto , userDto);
		postResponse.setSaved(save);
		return postResponse;
	}
	
	@GetMapping("/me/save")
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<PostDetailsResponse> getSavedPosts(@CurrentUser UserPrincipal currentUser) {
		List<PostDto> postDtos = postService.getUserSavedPost(currentUser.getId());
		List<PostDetailsResponse> responses = new ArrayList<>();
		postDtos.stream().forEach(p -> {
			UserDto userDto = postService.getUserFromPost(p.getId());
			PostDetailsResponse postResponse =  postMapper.mapToResponse(p, userDto);
			postResponse.setSaved(true);
			responses.add(postResponse);
		});
		return responses;
	}
}
