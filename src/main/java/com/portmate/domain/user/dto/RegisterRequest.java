package com.portmate.domain.user.dto;

import com.portmate.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotNull String name,
        @NotNull @Email String email,
        @NotNull String password,
        @NotNull String company,
        @NotNull String phone,
        @NotNull User.UserRole userRole) {
}
