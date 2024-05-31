package com.example.demo.user.controller.port;

public interface CertificationService {
	void send(String email, long id, String certificationCode);
	String generateCertificationUrl(long id, String certificationCode);
}
