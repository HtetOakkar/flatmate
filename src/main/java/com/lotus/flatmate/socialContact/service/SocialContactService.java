package com.lotus.flatmate.socialContact.service;

import com.lotus.flatmate.socialContact.dto.SocialContactDto;
import com.lotus.flatmate.socialContact.request.SocialContactRequest;

public interface SocialContactService {

	SocialContactDto addSocialContacts(SocialContactRequest request, Long userId);

	void deleteSocialContact(Long socialContactid, Long userId);

}
