package com.example.demo.user.controller.response;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

public class UserResponseTest {

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
		UserResponse userResponse = UserResponse.from(user);

		//then
		assertThat(userResponse.getId()).isEqualTo(1L);
		assertThat(userResponse.getEmail()).isEqualTo("testId1");
		assertThat(userResponse.getNickname()).isEqualTo("testNickname1");
		assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
		assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}
}
