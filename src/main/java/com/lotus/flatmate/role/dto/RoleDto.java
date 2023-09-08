package com.lotus.flatmate.role.dto;

import com.lotus.flatmate.role.entity.RoleName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
	private Long id;
	private RoleName roleName;
}
