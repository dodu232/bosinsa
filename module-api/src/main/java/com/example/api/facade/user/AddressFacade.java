package com.example.api.facade.user;

import com.example.api.dto.user.AddressRequest;
import com.example.api.usecase.user.CreateAddressUseCase;
import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.domain.entity.Address;
import com.example.domain.entity.User;
import com.example.domain.repository.AddressRepository;
import com.example.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressFacade implements CreateAddressUseCase {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createAddress(Long userId, AddressRequest.Create dto) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException("존재하지 않는 회원 번호", ErrorType.INVALID_PARAMETER,
				HttpStatus.BAD_REQUEST));

		Address address = Address.of(user, dto.getRecipientName(), dto.getPhone(), dto.getAddress(),
			dto.getAddressDetail());

		addressRepository.save(address);
	}
}
