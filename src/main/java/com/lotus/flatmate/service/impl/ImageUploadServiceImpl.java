package com.lotus.flatmate.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.ImageReadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import com.lotus.flatmate.service.ImageUploadService;
import com.lotus.flatmate.util.ImageResizeUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageUploadServiceImpl implements ImageUploadService {

	private final StorageClient storageClient;

	private final ImageResizeUtil imageResizeUtil;

	@Value("${firebase.storage.bucket}")
	private String BUCKET_NAME;

	public ImageUploadServiceImpl(StorageClient storageClient, ImageResizeUtil imageResizeUtil) {
		this.storageClient = storageClient;
		this.imageResizeUtil = imageResizeUtil;
	}

	@Override
	public String uploadImage(MultipartFile image, String folderName) throws IOException, ImageReadException {
		UUID uuid = UUID.randomUUID();
		String fileName = "image-" + uuid.toString().replace("-", "")
				+ Instant.now().toString().replace("-", "").replace(".", "").replace(":", "");
		String filePath = folderName + "/" + fileName;
		
		Map<String, String> map = new HashMap<>();
		map.put("firebaseStorageDownloadTokens", uuid.toString());
		
		BlobId blobId = BlobId.of(BUCKET_NAME, filePath);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map).setContentType("media").build();
		
		Storage storage = storageClient.bucket(BUCKET_NAME).getStorage();
		log.info("Uploading image....");
		
		File file = convertToFile(image, fileName);
		Path path = file.toPath();

		
		storage.create(blobInfo, Files.readAllBytes(path));
		Thread deleteThread = new Thread(() -> {
			file.delete();
		});
		
		deleteThread.start();

		log.info("Uploaded.");

		return "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/" + folderName + "%2F" + fileName
				+ "?alt=media&token=" + uuid.toString();
	}

	private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException, ImageReadException {
		File tempFile = new File(Objects.requireNonNull(fileName));

		BufferedImage resizedImage = imageResizeUtil.resizeImage(multipartFile, fileName);
		FileOutputStream fos = new FileOutputStream(tempFile);
		ImageIO.write(resizedImage, "jpg", tempFile);
		fos.close();
		return tempFile;
	}

	@Async("TaskExecutor")
	@Override
	public void deleteImage(String fileUrl, String folderName) {
		URI uri;
		try {
			uri = new URI(fileUrl);
			String path = uri.getPath();

			String fileName = retriveFileNameFromUrl(path);
			if (uri.getHost().equals("firebasestorage.googleapis.com")) {
				log.info("Deleting image...");
				BlobId blobId = BlobId.of(BUCKET_NAME, folderName + "/" + fileName);
				Storage storage = storageClient.bucket(BUCKET_NAME).getStorage();
				boolean success = storage.delete(blobId);
				if (success) {
					log.info("Image delete success...");
				} else {
					log.error("Image delete failed...");
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	private String retriveFileNameFromUrl(String path) {
		String[] segments = path.split("/");
		if (segments.length > 0) {
			return segments[segments.length - 1];
		}
		return null;
	}
}
