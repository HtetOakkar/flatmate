package com.lotus.flatmate.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {

			String jwt = getJwt(request);

			if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
				String username = tokenProvider.getUserNameFromJwtToken(jwt);
				Claims claims = tokenProvider.getClaims(jwt);
				String roles = claims.get("roles", String.class);
				Long userId = Long.parseLong(claims.getId());
				List<String> authorityArray = Arrays.asList(roles.split(","));
				List<GrantedAuthority> authorities = authorityArray.stream()
						.map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
				UserPrincipal userPrincipal = new UserPrincipal(userId, username, null, authorities);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userPrincipal, null, authorities);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		} catch (Exception ex) {
			logger.info(ex.getMessage());
		}

		filterChain.doFilter(request, response);
	}

	private String getJwt(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.replace("Bearer ", "");
		}
		return null;
	}
}
