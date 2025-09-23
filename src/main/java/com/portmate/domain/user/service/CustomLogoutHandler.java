package com.portmate.domain.user.service;

import com.portmate.domain.token.service.TokenBlackListService;
import com.portmate.global.jwt.CookieUtil;
import com.portmate.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.portmate.global.jwt.CookieUtil.expireCookie;


@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenBlackListService tokenBlackListService;
    private final JwtUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 1) 쿠키에서 Access/Refresh 추출
        Optional<String> refresh = CookieUtil.getRefreshTokenFromRequest(request);

        // 2) Refresh 폐기 (서버 저장소에서 삭제)
        if (refresh.isPresent()) {
            tokenBlackListService.addRefreshTokenBlackList(refresh.get(), jwtUtil.getRefreshTokenExpireTime(refresh.get()));
            expireCookie(response, "refresh", "/", true, true, "None");
            expireCookie(response, "access", "/", true, true, "None");
        }

        // 3) 보안 컨텍스트 정리
        SecurityContextHolder.clearContext();
    }
}
