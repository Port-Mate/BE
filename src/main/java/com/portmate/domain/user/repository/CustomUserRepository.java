package com.portmate.domain.user.repository;

import com.portmate.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomUserRepository {
    Page<User> findByStatus(User.UserStatus status, Pageable pageable);
}
