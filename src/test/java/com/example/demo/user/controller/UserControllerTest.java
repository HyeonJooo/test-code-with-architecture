package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;

public class UserControllerTest {

	@Test
	void 사용자는_특정_유저의_정보를_개인정보는_소거된_채_전달_받을_수_있다() {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();
		testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("testId1")
			.nickname("testNickname1")
			.address("Seoul")
			.certificationCode("aaaaaa-aaaa-aaa-aaaaaaaaaaaa")
			.status(UserStatus.ACTIVE)
			.lastLoginAt(100L)
			.build()
		);

		//when
		ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getNickname()).isEqualTo("testNickname1");
		assertThat(result.getBody().getEmail()).isEqualTo("testId1");
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
	}

	@Test
	void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();

		//when
		//then
		assertThatThrownBy(() -> {
			testContainer.userController.getUserById(1);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void 사용자는_인증코드로_계정을_활성화_시킬_수_있다() {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();
		testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("testId1")
			.nickname("testNickname1")
			.address("Seoul")
			.certificationCode("aaaaaa-aaaa-aaa-aaaaaaaaaaaa")
			.status(UserStatus.PENDING)
			.lastLoginAt(100L)
			.build()
		);

		//when
		ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaaaaa-aaaa-aaa-aaaaaaaaaaaa");

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
		assertThat(testContainer.userRepository.getById(1).getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	void 사용자는_인증코드가_일치하지_않을_경우_권한없음_에러를_내려준다() {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();
		testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("testId1")
			.nickname("testNickname1")
			.address("Seoul")
			.certificationCode("aaaaaa-aaaa-aaa-aaaaaaaaaaaa")
			.status(UserStatus.PENDING)
			.lastLoginAt(100L)
			.build()
		);

		//when
		//then
		assertThatThrownBy(() -> {
			testContainer.userController.verifyEmail(1, "aaaaaa-aaaa-aaa-aaaaaaaaaaab");
		}).isInstanceOf(CertificationCodeNotMatchedException.class);
	}

	@Test
	void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() {
		//given
		TestContainer testContainer = TestContainer.builder()
			.clockHolder(() -> 1678530673958L)
			.build();
		testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("testId1")
			.nickname("testNickname1")
			.address("Seoul")
			.certificationCode("aaaaaa-aaaa-aaa-aaaaaaaaaaaa")
			.status(UserStatus.ACTIVE)
			.lastLoginAt(100L)
			.build()
		);

		//when
		ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("testId1");

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getNickname()).isEqualTo("testNickname1");
		assertThat(result.getBody().getEmail()).isEqualTo("testId1");
		assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(result.getBody().getLastLoginAt()).isEqualTo(1678530673958L);
	}

	@Test
	void 사용자는_내_정보를_수정할_수_있다() {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();
		testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("testId1")
			.nickname("testNickname1")
			.address("Seoul")
			.certificationCode("aaaaaa-aaaa-aaa-aaaaaaaaaaaa")
			.status(UserStatus.ACTIVE)
			.lastLoginAt(100L)
			.build()
		);

		//when
		ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("testId1", UserUpdate.builder()
			.address("Pangyo")
			.nickname("testNickname1-n")
			.build());

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getNickname()).isEqualTo("testNickname1-n");
		assertThat(result.getBody().getEmail()).isEqualTo("testId1");
		assertThat(result.getBody().getAddress()).isEqualTo("Pangyo");
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
	}
}
