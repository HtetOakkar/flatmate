package com.lotus.flatmate.post.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPaginationResponse {
	private List<PostDetailsResponse> posts;
	@JsonProperty("total_pages")
	private int totalPages;
	@JsonProperty("total_items")
	private long totalItems;
	@JsonProperty("current_page")
	private int currentPage;
	@JsonProperty("next_page")
	private int nextPage;
}
