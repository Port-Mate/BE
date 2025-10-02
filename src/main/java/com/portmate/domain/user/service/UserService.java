package com.portmate.domain.user.service;

import com.portmate.domain.user.dto.RegisterRequest;
import com.portmate.domain.user.dto.UserInfoResponse;
import com.portmate.domain.user.entity.User;
import com.portmate.domain.user.repository.UserRepository;
import com.portmate.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserInfoResponse fetchUserInfo(CustomUserDetails customUserDetails){
        return new UserInfoResponse(customUserDetails.getUsername(), customUserDetails.getEmail());
    }

    @Transactional
    public void insertNewUser(RegisterRequest registerRequest){
        User newUser = User.create(registerRequest, encoder.encode(registerRequest.password()));

        userRepository.save(newUser);
    }
}
