package com.lotus.flatmate.socialContact.dto;

import com.lotus.flatmate.socialContact.entity.SocialContactType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialContactDto {
	private Long id;
	private String username;
	private String url;
	private SocialContactType contactType;
}
