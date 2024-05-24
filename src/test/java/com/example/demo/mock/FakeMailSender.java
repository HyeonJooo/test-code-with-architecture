package com.example.demo.mock;

import org.springframework.stereotype.Component;

import com.example.demo.user.service.port.MailSender;

@Component
public class FakeMailSender implements MailSender {

	public String email;
	public String title;
	public String content;

	@Override
	public void send(String email, String title, String content) {
		this.email = email;
		this.title = title;
		this.content = content;
	}
}
