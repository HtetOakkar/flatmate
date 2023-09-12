package com.lotus.flatmate.socialContact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.socialContact.entity.SocialContact;

@Repository
public interface SocialContactRepository extends JpaRepository<SocialContact, Long>{

}
