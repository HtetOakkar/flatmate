package com.lotus.flatmate.user.mapper.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lotus.flatmate.auth.request.RegistrationRequest;
import com.lotus.flatmate.post.mapper.PostMapper;
import com.lotus.flatmate.post.response.PostResponse;
import com.lotus.flatmate.socialContact.mapper.SocialContactMapper;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.mapper.UserMapper;
import com.lotus.flatmate.user.response.UserDetailsResponse;
import com.lotus.flatmate.user.response.UserProfileResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

	private final PasswordEncoder passwordEncoder;
	
	private final SocialContactMapper socialContactMapper;
	
	private final PostMapper postMapper;

	@Override
	public UserDto mapToUserDto(@Valid RegistrationRequest request) {
		UserDto userDto = new UserDto();
		userDto.setUsername(request.getUsername());
		userDto.setEmail(request.getEmail());
		userDto.setPassword(passwordEncoder.encode(request.getPassword()));
		userDto.setMobileNumber(request.getMobileNumber());
		return userDto;
	}

	@Override
	public UserDto mapToUserDto(User user) {
		if (user == null) {
			return null;
		}
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setUsername(user.getUsername());
		userDto.setEmail(user.getEmail());
		userDto.setMobileNumber(user.getMobileNumber());
		userDto.setPassword(user.getPassword());
		userDto.setLoginProvider(user.getLoginProvider());
		userDto.setCreatedAt(user.getCreatedAt());
		userDto.setUpdatedAt(user.getUpdatedAt());
		userDto.setProfileUrl(user.getProfileUrl());
		if (user.getSocialContacts().size() > 0) {
			userDto.setSocialContactDtos(user.getSocialContacts().stream().map(socialContactMapper::mapToSocialContactDto).toList());
		}
		if (user.getPosts().size() > 0) {
			userDto.setPostDtos(user.getPosts().stream().map(postMapper::mapToDto).toList());
		}
		if (user.getSavedPosts().size() > 0) {
			userDto.setSavedPostDtos(user.getSavedPosts().stream().map(postMapper::mapToSavedPostDto).toList());
		}
		return userDto;
	}

	@Override
	public UserProfileResponse mapToProfileResponse(UserDto userDto) {
		UserProfileResponse response = new UserProfileResponse();
		response.setId(userDto.getId());
		response.setUsername(userDto.getUsername());
		response.setEmail(userDto.getEmail());
		response.setMobileNumber(userDto.getMobileNumber());
		response.setProfileUrl(userDto.getProfileUrl());
		if (userDto.getSocialContactDtos() != null && !userDto.getSocialContactDtos().isEmpty()) {
			response.setSocialContacts(userDto.getSocialContactDtos().stream().map(socialContactMapper::mapToSocialContactResponse).toList());
		}
		return response;
	}

	@Override
	public UserDetailsResponse mapToUserDetailsResponse(UserDto userDto,  UserDto currentUserDto) {
		UserDetailsResponse response = new UserDetailsResponse();
		response.setId(userDto.getId());
		response.setUsername(userDto.getUsername());
		response.setMobileNumber(userDto.getMobileNumber());
		response.setProfileUrl(userDto.getProfileUrl());
		if (userDto.getSocialContactDtos() != null && !userDto.getSocialContactDtos().isEmpty()) {
			response.setSocialContacts(userDto.getSocialContactDtos().stream().map(socialContactMapper::mapToSocialContactResponse).toList());
		}
		
		if (userDto.getPostDtos() != null && !userDto.getPostDtos().isEmpty()) {
			List<PostResponse> postResponses = userDto.getPostDtos().stream().map(p -> {
				PostResponse postResponse = postMapper.mapToPostResponse(p);
				if (currentUserDto.getSavedPostDtos() != null && !currentUserDto.getSavedPostDtos().isEmpty()) {
					currentUserDto.getSavedPostDtos().stream().forEach(s -> {
						if (s.getPostDto().getId() == p.getId()) {
							postResponse.setSaved(true);
						} 	
					});
				}
				
				return postResponse;
			}).collect(Collectors.toList());
			postResponses.sort(Comparator.comparing(PostResponse::getUpdatedAt).reversed());
			response.setPosts(postResponses);
		}
		return response;
	}
}
