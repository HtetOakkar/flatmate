package com.lotus.flatmate.socialContact.service.impl;

import org.springframework.stereotype.Service;

import com.lotus.flatmate.socialContact.dto.SocialContactDto;
import com.lotus.flatmate.socialContact.entity.SocialContact;
import com.lotus.flatmate.socialContact.mapper.SocialContactMapper;
import com.lotus.flatmate.socialContact.repository.SocialContactRepository;
import com.lotus.flatmate.socialContact.request.SocialContactRequest;
import com.lotus.flatmate.socialContact.service.SocialContactService;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialContactServiceImpl implements SocialContactService{
	
	private final SocialContactRepository socialContactRepository;
	
	private final SocialContactMapper socialContactMapper;
	
	private final UserRepository userRepository;
	
	@Override
	public SocialContactDto addSocialContacts(SocialContactRequest request, Long userId) {
		User user = userRepository.findById(userId).get();
		
		SocialContact socialContact = new SocialContact();
		if (user.getSocialContacts() != null && user.getSocialContacts().size() > 0) {
			log.info("contacts exitst");
			
			for (SocialContact contact : user.getSocialContacts()) {
				if (contact.getContactType().equals(request.getContactType())) {
					log.info("contact type exist");
					contact.setUsername(request.getUsername());
					contact.setUrl(request.getUrl());
					socialContact = socialContactRepository.save(contact);
				} else {
					log.info("contact type does not exist");
					socialContact.setUsername(request.getUsername());
					socialContact.setUrl(request.getUrl());
					socialContact.setContactType(request.getContactType());
					socialContact.setUser(user);
					
				}
			}
		} else {
			log.info("contacts do not exist");
			
			socialContact.setUsername(request.getUsername());
			socialContact.setUrl(request.getUrl());
			socialContact.setContactType(request.getContactType());
			socialContact.setUser(user);
		}
		
		
		return socialContactMapper.mapToSocialContactDto(socialContactRepository.save(socialContact));
		
	}

}
