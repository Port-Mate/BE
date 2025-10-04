package com.portmate.domain.user.dto;

import com.portmate.domain.user.entity.User;

public record UserStatusRequest(User.UserStatus userStatus) {
}
