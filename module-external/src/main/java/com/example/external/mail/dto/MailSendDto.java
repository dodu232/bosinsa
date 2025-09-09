package com.example.external.mail.dto;

import lombok.Getter;

@Getter
public class MailSendDto {

	private String from;
	private String to;
	private String subject;
	private String content;

	public static MailSendDto of(String from, String to, String subject, String content) {
		MailSendDto dto = new MailSendDto();
		dto.from = from;
		dto.to = to;
		dto.subject = subject;
		dto.content = content;
		return dto;
	}
}
