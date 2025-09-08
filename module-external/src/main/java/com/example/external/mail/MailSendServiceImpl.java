package com.example.external.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailSendServiceImpl implements EmailService {

	private final JavaMailSender javaMailSender;

	@Override
	public void sendEmail(MailSendDto dto) {

		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setFrom(dto.getFrom());
		smm.setTo(dto.getTo());
		smm.setSubject(dto.getSubject());
		smm.setText(dto.getContent());

		try {
			javaMailSender.send(smm);
		} catch (MailException e) {
			log.error("결제 완료 이메일 전송 실패");
			throw e;
		}
	}
}
