package com.example.api.usecase.user;

import com.example.api.dto.user.AddressRequest;
import org.springframework.stereotype.Service;

@Service
public interface CreateAddressUseCase {

	void createAddress(Long userId, AddressRequest.Create dto);

}
