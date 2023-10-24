package com.lotus.flatmate.post.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.lotus.flatmate.apartment.dto.ApartmentDto;
import com.lotus.flatmate.picture.dto.PictureDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
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
	private ApartmentDto apartment;
	private List<PictureDto> pictures;
}
