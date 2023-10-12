package com.lotus.flatmate.user.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lotus.flatmate.emailVerification.entity.EmailVerification;
import com.lotus.flatmate.post.entity.Post;
import com.lotus.flatmate.post.entity.SavedPost;
import com.lotus.flatmate.refreshToken.entity.RefreshToken;
import com.lotus.flatmate.role.entity.Role;
import com.lotus.flatmate.socialContact.entity.SocialContact;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users", indexes = { @Index(name = "idx_email", columnList = "email"),
		@Index(name = "idx_username", columnList = "username") })
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "username", length = 60)
	private String username;

	@Column(name = "email", unique = true, length = 60)
	private String email;
	
	@Column(name = "profile_url", columnDefinition = "TEXT")
	private String profileUrl;

	@Column(name = "password")
	@JsonIgnore
	private String password;

	@Column(name = "mobile_number", length = 60)
	private String mobileNumber;
	
	@Column(updatable = false)
	@CreationTimestamp
	private Instant createdAt;

	@UpdateTimestamp
	private Instant updatedAt;
	
	@Version
	@Column(name = "version", columnDefinition = "bigint DEFAULT 0", nullable = false)
	private Long version;

	@Enumerated(EnumType.STRING)
	@Column(name = "login_provider", length = 60)
	private LoginProvider loginProvider;
	
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private List<Role> roles;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private EmailVerification emailVerification;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<RefreshToken> refreshTokens;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<SocialContact> socialContacts;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Post> posts;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<SavedPost> savedPosts;
	
}
