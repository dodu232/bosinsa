package com.example.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	private final long exp;
	private final SecretKey key;

	public JwtUtil(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.expired}") long expired
	) {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.exp = expired;
	}

	public String generateToken(String userId) {
		return Jwts.builder()
			.subject(userId)
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + exp))
			.signWith(key)
			.compact();
	}

}
