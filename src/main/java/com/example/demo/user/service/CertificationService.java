package com.example.demo.user.service;

import org.springframework.stereotype.Service;

import com.example.demo.user.service.port.MailSender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificationService {

	private final MailSender mailSender;

	public void send(String email, long id, String certificationCode) {
		String certificationUrl = generateCertificationUrl(id, certificationCode);
		String title = "Please certify your email address";
		String content = "Please click the following link to certify your email address: " + certificationUrl;

		mailSender.send(email, title, content);
	}

	private String generateCertificationUrl(long id, String certificationCode) {
		return "http://localhost:8080/api/users/" + id + "/verify?certificationCode=" + certificationCode;
	}

}
