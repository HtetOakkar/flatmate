package com.lotus.flatmate.security;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.lotus.flatmate.role.dto.RoleDto;
import com.lotus.flatmate.role.entity.Role;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String SECRET;

	@Value("${jwt.expiration}")
	private int JWT_EXPIRATION;

	@Value("${jwt.refreshtoken.expiration}")
	private int REFRESH_TOKEN_EXPIRATION;

	public String generateJwtToken(Authentication auth) {

		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		final String authorities = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		return Jwts.builder().setId(Long.toString(userPrincipal.getId())).claim("roles", authorities)
				.setSubject(userPrincipal.getUsername())
				.setExpiration(new Date(new Date().getTime() + JWT_EXPIRATION * 1000))
				.signWith(getSignKey(), SignatureAlgorithm.HS512).compact();
	}

	public String generateJwtToken(UserDto user) {

		List<RoleDto> roles = user.getRoles();
		for (RoleDto role : roles) {
			return Jwts.builder().setId(Long.toString(user.getId())).setSubject((user.getUsername()))
					.claim("roles", role.getRoleName()).setIssuedAt(new Date(new Date().getTime()))
					.setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION * 1000))
					.signWith(getSignKey(), SignatureAlgorithm.HS512).compact();
		}
		return Jwts.builder().toString();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token -> Message: {}", e);
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token -> Message: {}", e);
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token -> Message: {}", e);
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty -> Message: {}", e);
		}

		return false;
	}

	public String getUserNameFromJwtToken(String token) {
		return getClaims(token).getSubject();
	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateRefreshToken(User user) {
		List<Role> roles = user.getRoles();
		for (Role role : roles) {
			return Jwts.builder().setId(Long.toString(user.getId())).setSubject((user.getUsername()))
					.claim("roles", role.getName()).setIssuedAt(new Date())
					.setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION * 1000))
					.signWith(getSignKey(), SignatureAlgorithm.HS512).compact();
		}
		return Jwts.builder().toString();
	}

}
