package com.example.api.config;

import com.example.api.infra.auth.CustomUserDetails;
import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	private final long exp;
	private final SecretKey key;

	public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expired}") long expired) {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.exp = expired;
	}

	public String generateToken(long userId, String email, String nickname) {
		return Jwts.builder().subject(String.valueOf(userId)).claim("email", email)
			.claim("nickname", nickname).claim("roles", List.of("ROLE_USER")).issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + exp)).signWith(key).compact();
	}

	public Authentication getAuthentication(String token) {
		validateToken(token);

		Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

		List<String> roles = claims.get("roles", List.class);

		if (roles == null) {
			throw new ApiException("권한 정보가 없는 토큰입니다.", ErrorType.INVALID_TOKEN,
				HttpStatus.UNAUTHORIZED);
		}

		List<? extends GrantedAuthority> authorities = roles.stream()
			.map(SimpleGrantedAuthority::new).toList();

		Long userId = Long.parseLong(claims.getSubject());
		String email = claims.get("email", String.class);
		String nickname = claims.get("nickname", String.class);

		CustomUserDetails principal = CustomUserDetails.of(userId, email, nickname, authorities);

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(key).build().parseClaimsJws(token);

			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			throw new ApiException("토큰이 잘못되었습니다.", ErrorType.INVALID_TOKEN,
				HttpStatus.UNAUTHORIZED);
		} catch (ExpiredJwtException e) {
			throw new ApiException("토큰이 만료되었습니다.", ErrorType.INVALID_TOKEN,
				HttpStatus.UNAUTHORIZED);
		} catch (UnsupportedJwtException | IllegalArgumentException e) {
			throw new ApiException("지원하지 않는 토큰이거나 잘못된 형식의 토큰입니다.", ErrorType.INVALID_TOKEN,
				HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			throw new ApiException("알 수 없는 토큰 문제", ErrorType.INVALID_TOKEN,
				HttpStatus.UNAUTHORIZED);
		}
	}

}
