package com.portmate.domain.user.dto;

import com.portmate.domain.user.entity.User;

public record UserStatusResponse(
    String userId,
    String userName,
    String userCompany,
    User.UserStatus userStatus,
    User.UserRole userRole
){
    public static UserStatusResponse from(User u) {
        return new UserStatusResponse(u.getId(), u.getName(), u.getCompany(), u.getUserStatus(), u.getUserRole());
    }
}
