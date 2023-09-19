package com.lotus.flatmate.post.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.lotus.flatmate.aparment.response.ApartmentResponse;
import com.lotus.flatmate.picture.response.PictureResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDetailsResponse {
	private Long id;
	private String contract;
	private String description;
	private BigDecimal price;
	private int tenants;
	private String state;
	private String township;
	private String additional;
	private Instant createdAt;
	private Instant updatedAt;
	private ApartmentResponse apartment;
	private List<PictureResponse> pictures;
}
