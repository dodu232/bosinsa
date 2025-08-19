package com.example.api.config;

import com.example.api.facade.user.UserFacade;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserFacade userFacade;

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		final String auth = request.getHeader("Authorization");

		if (auth == null || !auth.regionMatches(true, 0, "Bearer ", 0, 7)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = auth.substring(7);
		if (token.isEmpty()) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			Authentication authentication = jwtUtil.getAuthentication(token);

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (Exception e) {
			// 인증 실패는 401로 내려가게 하고 로그만 남김
			// 필요 시 response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			// return;  // 보통은 ExceptionTranslationFilter가 처리하게 흘려보냅니다.
		}

		filterChain.doFilter(request, response);
	}

}
