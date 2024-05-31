package com.lotus.flatmate.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.message.entity.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>{

}
