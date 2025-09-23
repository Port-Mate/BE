package com.portmate.domain.notification.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "user_fcm_tokens")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserFcmToken {

	@Id
	private String id;

	private String userId;

	private String token;

	private String platform;

	private boolean valid = true;

	@Builder
	public UserFcmToken(String userId, String token, String platform) {
		this.userId = userId;
		this.token = token;
		this.platform = platform;
		this.valid = true;
	}

	public void invalidate() {
		this.valid = false;
	}
}


