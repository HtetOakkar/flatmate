package com.lotus.flatmate.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.message.entity.Message;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
