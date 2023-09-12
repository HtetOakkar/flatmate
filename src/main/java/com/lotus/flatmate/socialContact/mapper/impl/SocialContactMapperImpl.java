package com.lotus.flatmate.socialContact.mapper.impl;

import org.springframework.stereotype.Component;

import com.lotus.flatmate.socialContact.dto.SocialContactDto;
import com.lotus.flatmate.socialContact.entity.SocialContact;
import com.lotus.flatmate.socialContact.mapper.SocialContactMapper;
import com.lotus.flatmate.socialContact.response.SocialContactResponse;

@Component
public class SocialContactMapperImpl implements SocialContactMapper{

	@Override
	public SocialContactDto mapToSocialContactDto(SocialContact socialContact) {
		if (socialContact == null) {
			return null;
		}
		SocialContactDto contactDto = new SocialContactDto();
		contactDto.setId(socialContact.getId());
		contactDto.setUsername(socialContact.getUsername());
		contactDto.setUrl(socialContact.getUrl());
		contactDto.setContactType(socialContact.getContactType());
		return contactDto;
	}

	@Override
	public SocialContactResponse mapToSocialContactResponse(SocialContactDto contactDto) {
		SocialContactResponse response = new SocialContactResponse();
		response.setId(contactDto.getId());
		response.setUsername(contactDto.getUsername());
		response.setUrl(contactDto.getUrl());
		response.setContactType(contactDto.getContactType());
		return response;
	}

}
