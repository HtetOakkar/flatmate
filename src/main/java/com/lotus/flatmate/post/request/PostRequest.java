package com.lotus.flatmate.post.request;

import java.math.BigDecimal;

import com.lotus.flatmate.apartment.request.ApartmentRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
	private String contract;
	private String description;
	private BigDecimal price;
	private int tenants;
	private String state;
	private String township;
	private String additional;
	private ApartmentRequest apartment;
}
