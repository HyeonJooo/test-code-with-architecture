package com.example.demo.user.controller.response;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

public class MyProfileResponseTest {

	@Test
	void User으로_응답을_생성할_수_있다() {
		//given
		User user = User.builder()
				.id(1L)
				.email("testId1")
				.nickname("testNickname1")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.lastLoginAt(100L)
				.certificationCode("aaaa-aaa-aaaaaaa")
				.build();

		//when
		MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

		//then
		assertThat(myProfileResponse.getId()).isEqualTo(1L);
		assertThat(myProfileResponse.getEmail()).isEqualTo("testId1");
		assertThat(myProfileResponse.getNickname()).isEqualTo("testNickname1");
		assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
		assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
		assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}
}
