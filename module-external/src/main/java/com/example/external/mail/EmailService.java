package com.example.external.mail;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

	void sendEmail(MailSendDto dto);
}
