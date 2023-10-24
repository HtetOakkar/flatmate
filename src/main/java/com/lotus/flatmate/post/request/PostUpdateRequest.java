package com.lotus.flatmate.post.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lotus.flatmate.apartment.request.ApartmentRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequest {
	private String contract;
	private String description;
	private BigDecimal price;
	private int tenants;
	private String state;
	private String township;
	private String additional;
	private ApartmentRequest apartment;
	@JsonProperty("removed_image_ids")
	private Long[] removedImageIds;
}
