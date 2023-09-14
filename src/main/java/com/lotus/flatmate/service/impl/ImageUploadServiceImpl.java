package com.lotus.flatmate.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import com.lotus.flatmate.service.ImageUploadService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageUploadServiceImpl implements ImageUploadService {

	private final StorageClient storageClient;

	@Value("${firebase.storage.bucket}")
	private String BUCKET_NAME;

	public ImageUploadServiceImpl(StorageClient storageClient) {
		this.storageClient = storageClient;
	}

	@Override
	public String uploadImage(MultipartFile image, String folderName) throws IOException {
		UUID uuid = UUID.randomUUID();
		String fileName = image.getOriginalFilename().replace(" ", "_");
		String filePath = folderName + "/" + fileName;
		Map<String, String> map = new HashMap<>();
		map.put("firebaseStorageDownloadTokens", uuid.toString());
		BlobId blobId = BlobId.of(BUCKET_NAME, filePath);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map).setContentType("media").build();
		Storage storage = storageClient.bucket(BUCKET_NAME).getStorage();

		File file = convertToFile(image, fileName);
		Path path = file.toPath();
		log.info("Uploading image....");
		storage.create(blobInfo, Files.readAllBytes(path));
		log.info("Uploaded.");
		file.delete();
		return "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/" + folderName + "%2F" + fileName
				+ "?alt=media&token=" + uuid.toString();
	}

	private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
		File tempFile = new File(Objects.requireNonNull(fileName));
		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(multipartFile.getBytes());
		fos.close();
		return tempFile;
	}

	@Override
	public void deleteImage(String fileUrl, String folderName) {
		URI uri;
		try {
			uri = new URI(fileUrl);
			String path = uri.getPath();
			
			String fileName = retiveFileNameFromUrl(path);
			if (uri.getHost().equals("firebasestorage.googleapis.com")) {
				log.info("Deleting image...");
				BlobId blobId = BlobId.of(BUCKET_NAME, folderName + "/" +fileName);
				Storage storage = storageClient.bucket(BUCKET_NAME).getStorage();
				boolean success = storage.delete(blobId);
				if (success) {
					log.info("Image delete success...");
				} else {
					log.error("image delete failed...");
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	private String retiveFileNameFromUrl(String path) {
		String[] segments = path.split("/");
		if (segments.length > 0) {
			return segments[segments.length - 1];
		} 
		return null;

	}
}
