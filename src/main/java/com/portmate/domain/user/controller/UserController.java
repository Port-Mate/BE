package com.portmate.domain.user.controller;

import com.portmate.docs.UserControllerDocs;
import com.portmate.domain.user.dto.LoginRequest;
import com.portmate.domain.user.dto.RegisterRequest;
import com.portmate.domain.user.dto.UserInfoResponse;
import com.portmate.domain.user.dto.UserStatusRequest;
import com.portmate.domain.user.dto.UserStatusResponse;
import com.portmate.domain.user.entity.User;
import com.portmate.domain.user.repository.UserRepository;
import com.portmate.domain.user.service.UserService;
import com.portmate.global.auth.CustomUserDetails;
import com.portmate.global.jwt.JwtUtil;
import com.portmate.global.response.ApiResponse;
import com.portmate.global.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController implements UserControllerDocs {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(ApiResponse.onSuccess(userService.fetchUserInfo(customUserDetails)));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<PageResponse<List<UserStatusResponse>>>> getPendingUsers(
            @RequestParam(defaultValue = "1") @Min(value = 1) int page,
            @RequestParam(defaultValue = "10") @Min(value = 1) int size
    ){
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.ok(ApiResponse.onSuccess(userService.queryByStatus(User.UserStatus.PENDING, pageable)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> postRegister(@Validated @RequestBody RegisterRequest registerRequest){
        userService.insertNewUser(registerRequest);
        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<Void>> putUserStatus(@PathVariable("userId") String userId,
                                                           @RequestBody UserStatusRequest request){
        userService.updateUserStatus(userId, request);
        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> createDevToken(@RequestParam String email) {
        String accessToken = jwtUtil.createAccess(email);
        String refreshToken = jwtUtil.createRefresh(email);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", accessToken);
        tokens.put("refresh", refreshToken);

        return ResponseEntity.ok(ApiResponse.onSuccess(tokens));
    }



}
