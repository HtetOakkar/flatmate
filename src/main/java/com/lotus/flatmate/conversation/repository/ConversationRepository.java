package com.lotus.flatmate.conversation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.conversation.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long>{

}
