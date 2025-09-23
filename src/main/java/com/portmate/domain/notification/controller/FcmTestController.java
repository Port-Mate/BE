package com.portmate.domain.notification.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FcmTestController {
	@GetMapping("/fcm-test")
	public String fcmTest() {
		return "fcm-test"; // resources/templates/fcm-test.html
	}
}


