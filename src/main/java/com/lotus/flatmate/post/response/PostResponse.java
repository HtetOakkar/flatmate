package com.lotus.flatmate.post.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lotus.flatmate.aparment.response.ApartmentResponse;
import com.lotus.flatmate.picture.response.PictureResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {
	private Long id;
	private String contract;
	private String description;
	private BigDecimal price;
	private int tenants;
	private String state;
	private String township;
	private String additional;
	private boolean saved;
	@JsonProperty("created_at")
	private Instant createdAt;
	@JsonProperty("updated_at")
	private Instant updatedAt;
	private ApartmentResponse apartment;
	private List<PictureResponse> pictures;
}
