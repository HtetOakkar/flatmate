package com.lotus.flatmate.post.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.lotus.flatmate.apartment.dto.AllApartmentDto;
import com.lotus.flatmate.picture.dto.AllPictureDto;
import com.lotus.flatmate.user.dto.AllUserPostDto;

public interface AllPostDto {
	
	public Long getId();
	
	public String getContract();
	
	public String getDescription();
	
	public BigDecimal getPrice();
	
	public int getTenants();
	
	public String getState();
	
	public String getTownship();
	
	public String getAdditional();
	
	public Instant getCreatedAt();
	
	public Instant getUpdatedAt();
	
	public List<AllPictureDto> getPictures();
	
	public AllApartmentDto getApartment();
	
	public AllUserPostDto getUser();
	
}
