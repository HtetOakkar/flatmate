package com.lotus.flatmate.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.ImageReadException;
import org.springframework.web.multipart.MultipartFile;

public class ImageResizeUtil {

	public static BufferedImage resizeImage(MultipartFile multipartFile, String fileName) throws ImageReadException, IOException {
		//resize large images to 1440p resolution
		int maxWidth = 1920;
		int maxHeight = 1440;
		InputStream inputStream = multipartFile.getInputStream();
		BufferedImage originalImage = ImageIO.read(inputStream);
		
		if (originalImage == null) {
			throw new IOException("Image Input Error.");
		}
		int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
		
        if (originalHeight > originalWidth) {
			maxWidth = 1440;
			maxHeight = 1920;
		}
		
        int newWidth = originalWidth;
        int newHeight = originalHeight;

		if (originalHeight > maxHeight) {
			newHeight = maxHeight;
			newWidth = (newHeight * originalWidth) / originalHeight;
		} else if (originalWidth > maxWidth) {
			newWidth = maxWidth;
			newHeight = (newWidth * originalHeight) / originalWidth;
		}
	
		Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		BufferedImage bufferedResizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
		Graphics2D g2d = bufferedResizedImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, null);
        g2d.dispose();
		return bufferedResizedImage;
	}
}
