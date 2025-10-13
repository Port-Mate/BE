package com.portmate.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

	@Value("${firebase.json}")
	private String firebaseJson;

	@Bean
	public FirebaseMessaging firebaseMessaging() throws IOException {
		if (firebaseJson == null || firebaseJson.isBlank()) {
			throw new IllegalStateException("Firebase JSON 환경변수(FIREBASE_JSON)가 설정되지 않았습니다.");
		}

		ByteArrayInputStream serviceAccountStream =
			new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8));

		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
			.build();

		FirebaseApp app;
		if (FirebaseApp.getApps().isEmpty()) {
			app = FirebaseApp.initializeApp(options);
		} else {
			app = FirebaseApp.getInstance();
		}

		return FirebaseMessaging.getInstance(app);
	}
}
