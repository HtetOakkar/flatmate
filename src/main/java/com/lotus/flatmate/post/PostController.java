package com.lotus.flatmate.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lotus.flatmate.service.ImageUploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
	
	private final ImageUploadService imageUploadService;
	
	@PostMapping()
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> createPost(@RequestPart(value = "images") MultipartFile[] images) throws IOException {
		List<String> imageUrls = new ArrayList<>();
		if (images.length > 0) {
			for (MultipartFile multipartFile : images) {
				if(multipartFile != null) {
					String url = imageUploadService.uploadImage(multipartFile, "post_photos");
					imageUrls.add(url);
				}
			}
		}
		return ResponseEntity.ok(imageUrls);
	}
	
}
