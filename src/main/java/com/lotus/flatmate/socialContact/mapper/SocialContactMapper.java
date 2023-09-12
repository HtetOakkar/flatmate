package com.lotus.flatmate.socialContact.mapper;

import com.lotus.flatmate.socialContact.dto.SocialContactDto;
import com.lotus.flatmate.socialContact.entity.SocialContact;
import com.lotus.flatmate.socialContact.response.SocialContactResponse;

public interface SocialContactMapper {

	SocialContactDto mapToSocialContactDto(SocialContact socialContact);
	
	SocialContactResponse mapToSocialContactResponse(SocialContactDto contactDto);

}
