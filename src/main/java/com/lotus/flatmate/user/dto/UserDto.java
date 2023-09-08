package com.lotus.flatmate.user.dto;

import java.time.Instant;
import java.util.List;

import com.lotus.flatmate.role.dto.RoleDto;
import com.lotus.flatmate.user.entity.LoginProvider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	private Long id;
	private String username;
	private String email;
	private String mobileNumber;
	private String password;
	private Instant createdAt;
	private Instant updatedAt;
	private LoginProvider loginProvider;
	private List<RoleDto> roles;
}
