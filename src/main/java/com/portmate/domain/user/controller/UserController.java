package com.portmate.domain.user.controller;

import com.portmate.domain.user.dto.RegisterRequest;
import com.portmate.domain.user.dto.UserInfoResponse;
import com.portmate.domain.user.service.UserService;
import com.portmate.global.auth.CustomUserDetails;
import com.portmate.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(ApiResponse.onSuccess(userService.fetchUserInfo(customUserDetails)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> postRegister(@Validated @RequestBody RegisterRequest registerRequest){
        userService.insertNewUser(registerRequest);
        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }
}
