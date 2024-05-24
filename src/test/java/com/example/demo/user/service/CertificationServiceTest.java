package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.mock.FakeMailSender;

public class CertificationServiceTest {

	@Test
	void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() {
		//given
		FakeMailSender fakeMailSender = new FakeMailSender();
		CertificationService certificationService = new CertificationService(fakeMailSender);

		//when
		certificationService.send("testId1", 1, "aaaaa");

		//then
		assertThat(fakeMailSender.email).isEqualTo("testId1");
		assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
		assertThat(fakeMailSender.content).isEqualTo(
			"Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaaa"
		);
	}
}
