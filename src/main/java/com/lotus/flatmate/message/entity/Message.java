package com.lotus.flatmate.message.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.lotus.flatmate.conversation.entity.Conversation;
import com.lotus.flatmate.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "messages", indexes = {
		@Index(name = "idx_sender_id", columnList = "sender_id"),
		@Index(name = "idx_created_at", columnList = "created_at")
})
@NoArgsConstructor
@AllArgsConstructor
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	private Long id;
	
	@Column(name = "message", columnDefinition = "TEXT")
	private String message;
	
	@ManyToOne
	@JoinColumn(name = "conversation_id", referencedColumnName = "conversation_id")
	private Conversation conversation;
	
	@ManyToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "user_id")
	private User sender;
	
	@CreationTimestamp
	@Column(name = "created_at")
	private Instant createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private Instant updatedAt;
}
