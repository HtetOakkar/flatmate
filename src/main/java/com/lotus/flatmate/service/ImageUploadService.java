package com.lotus.flatmate.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

	String uploadImage(MultipartFile image, String folderName) throws IOException;

	void deleteImage(String fileUrl, String folderName);
}
