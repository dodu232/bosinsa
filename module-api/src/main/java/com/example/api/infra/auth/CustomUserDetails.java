package com.example.api.infra.auth;

import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {

	private final Long id;
	private final String nickname;
	private final String email;
	private final Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities != null ? authorities : Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public static CustomUserDetails of(
		Long id, String email, String nickname, Collection<? extends GrantedAuthority> authorities
	) {
		return CustomUserDetails.builder()
			.id(id)
			.email(email)
			.nickname(nickname)
			.authorities(authorities)
			.build();
	}
}
