package com.lotus.flatmate.conversation.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.lotus.flatmate.message.entity.Message;
import com.lotus.flatmate.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "conversations", indexes = {
		@Index(name="idx_users", columnList = "sender_id, receiver_id"),
		@Index(name = "idx_timestamps", columnList = "created_at, updated_at")
})
public class Conversation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "conversation_id")
	private Long id;
	
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "conversation")
	private List<Message> messages;
	
	@ManyToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "user_id")
	private User sender;
	
	@ManyToOne
	@JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
	private User receiver;
	
	@CreationTimestamp
	@Column(name = "created_at")
	private Instant createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private Instant updatedAt;
}
