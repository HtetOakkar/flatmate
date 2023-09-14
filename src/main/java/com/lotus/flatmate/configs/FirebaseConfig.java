package com.lotus.flatmate.configs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FirebaseConfig {

	@Value("${firebase.service-account}")
	private String SERVICE_ACCOUNT;

	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		return FirebaseMessaging.getInstance(firebaseApp());
	}
	
	@Bean
	FirebaseAuth firebaseAuth() throws IOException {
		return FirebaseAuth.getInstance(firebaseApp());
	}
	
	@Bean
	StorageClient storageClient() throws IOException {
		return StorageClient.getInstance(firebaseApp());
	}
	
	@Bean
	StorageOptions storageOptions() throws FileNotFoundException, IOException {
		return StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT))).build();
	}

	@Bean
	FirebaseApp firebaseApp() throws IOException {
		FileInputStream serviceAccount = new FileInputStream(SERVICE_ACCOUNT);
		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();
		return FirebaseApp.initializeApp(options);
	}
}
