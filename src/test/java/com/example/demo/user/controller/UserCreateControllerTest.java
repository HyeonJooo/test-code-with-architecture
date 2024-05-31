package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;

public class UserCreateControllerTest {

	@Test
	void 사용자는_회원가입을_할_수_있고_회원가입_된_사용자는_PENDING_상태이다() {
		//given
		TestContainer testContainer = TestContainer.builder()
			.uuidHolder(() -> "aaaaaa-aaaa-aaa-aaaaaaaaaaaa")
			.build();

		UserCreate userCreate = UserCreate.builder()
			.email("testId1")
			.nickname("testNickname1")
			.address("Pangyo")
			.build();

		//when
		ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(userCreate);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getNickname()).isEqualTo("testNickname1");
		assertThat(result.getBody().getEmail()).isEqualTo("testId1");
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
		assertThat(result.getBody().getLastLoginAt()).isNull();
		assertThat(testContainer.userRepository.getById(1).getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaa-aaaaaaaaaaaa");
	}

}
