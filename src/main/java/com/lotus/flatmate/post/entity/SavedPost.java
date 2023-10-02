package com.lotus.flatmate.post.entity;

import com.lotus.flatmate.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "saved_posts")
@Getter
@Setter
public class SavedPost {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "post_id", referencedColumnName = "post_id")
	private Post post;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "user_id", referencedColumnName = "user_id")
	private User user;
}
