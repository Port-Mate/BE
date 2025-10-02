package com.portmate.domain.user.entity;


import com.portmate.domain.user.dto.RegisterRequest;
import com.portmate.global.entity.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    private String company;

    private String phoneNumber;

    private UserStatus userStatus;

    private UserRole userRole;

    public enum UserStatus {
        PENDING, ACTIVE, EXIT
    }

    public enum UserRole {
        ADMIN,          // 관리자
        AGENT,          // 선박 대리점
        SHIPPER         // 화주사
    }

    public static User create(RegisterRequest request, String encodedPassword) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .company(request.company())
                .phoneNumber(request.phone())
                .userStatus(UserStatus.PENDING)
                .userRole(request.userRole())
                .build();
    }
}



