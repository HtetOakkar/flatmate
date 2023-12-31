package com.lotus.flatmate.post.response;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPaginationResponse {
	private List<PostDetailsResponse> posts;
	private Instant cursor;
	@JsonProperty("has_next")
	private boolean hasNext;
}
