package com.lotus.flatmate.post.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lotus.flatmate.picture.dto.PictureDto;
import com.lotus.flatmate.post.dto.PostDto;
import com.lotus.flatmate.post.mapper.PostMapper;
import com.lotus.flatmate.post.request.PostRequest;
import com.lotus.flatmate.post.response.PostDetailsResponse;
import com.lotus.flatmate.post.service.PostService;
import com.lotus.flatmate.security.CurrentUser;
import com.lotus.flatmate.security.UserPrincipal;
import com.lotus.flatmate.service.ImageUploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
	
	private final ImageUploadService imageUploadService;
	
	private final PostMapper postMapper;
	
	private final PostService postService;
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> createPost(@CurrentUser UserPrincipal userPrincipal,@RequestPart(value = "images") MultipartFile[] images, @RequestPart(value = "data") PostRequest request) throws IOException {
		List<PictureDto> pictureDtos = new ArrayList<>();
		if (images.length > 0) {
			
			for (MultipartFile multipartFile : images) {
				if(multipartFile != null) {
					String url = imageUploadService.uploadImage(multipartFile, "post_photos");
					PictureDto pictureDto = new PictureDto();
					pictureDto.setUrl(url);
					pictureDtos.add(pictureDto);
				}
			}
		}
		
		PostDto postDto = postMapper.mapToDto(request);
		postDto.setPictures(pictureDtos);
		PostDto savedPostDto = postService.createPost(postDto, userPrincipal.getId());
		PostDetailsResponse response = postMapper.mapToResponse(savedPostDto);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/me")
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<PostDetailsResponse> getUserPosts(@CurrentUser UserPrincipal userPrincipal) {
		List<PostDto> postDtos = postService.getUserPosts(userPrincipal.getId());
		return postDtos.stream().map(postMapper::mapToResponse).toList();
	}
	
	@GetMapping("/{id}") 
	@PreAuthorize("hasRole('ROLE_USER')")
	public PostDetailsResponse getPostDetails(@PathVariable Long id) {
		PostDto postDto = postService.getPostDetails(id);
		return postMapper.mapToResponse(postDto);
	}
	
}
