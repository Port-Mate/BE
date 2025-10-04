package com.portmate.domain.user.service;

import com.portmate.domain.user.dto.RegisterRequest;
import com.portmate.domain.user.dto.UserInfoResponse;
import com.portmate.domain.user.dto.UserStatusRequest;
import com.portmate.domain.user.dto.UserStatusResponse;
import com.portmate.domain.user.entity.User;
import com.portmate.domain.user.repository.UserRepository;
import com.portmate.global.auth.CustomUserDetails;
import com.portmate.global.response.PageResponse;
import io.github.kamillcream.mpa.MongoEntityLoader;
import io.github.kamillcream.mpa.TransactionalMongo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MongoEntityLoader loader;
    private final BCryptPasswordEncoder encoder;

    public UserInfoResponse fetchUserInfo(CustomUserDetails customUserDetails){
        return new UserInfoResponse(customUserDetails.getUsername(), customUserDetails.getEmail());
    }

    public PageResponse<List<UserStatusResponse>> queryByStatus(User.UserStatus status, Pageable pageable) {
        Page<User> usersMeetQuery = userRepository.findByStatus(status, pageable);
        List<UserStatusResponse> items = usersMeetQuery.stream()
                .map(UserStatusResponse::from)
                .toList();
        return PageResponse.of(items, usersMeetQuery.getNumber(), usersMeetQuery.getSize(),
                usersMeetQuery.getTotalElements(), usersMeetQuery.getTotalPages());
    }

    @Transactional
    public void insertNewUser(RegisterRequest registerRequest){
        User newUser = User.create(registerRequest, encoder.encode(registerRequest.password()));
        userRepository.save(newUser);
    }

    @TransactionalMongo
    public void updateUserStatus(String userId, UserStatusRequest request) {
        User user = loader.findById(userId, User.class);
        user.updateStatus(request);
    }
}
