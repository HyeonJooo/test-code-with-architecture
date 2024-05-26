package com.example.demo.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;

public class UserTest {

	@Test
	void UserCreate_객체로_생성할_수_있다() {
		//given
		UserCreate userCreate = UserCreate.builder()
				.email("testId1")
				.nickname("testNickname1")
				.address("Seoul")
				.build();

		//when
		User user = User.from(userCreate, new TestUuidHolder("aaaaaa-aaaa-aaaaaaaaa"));

		//then
		assertThat(user.getId()).isNull();
		assertThat(user.getEmail()).isEqualTo("testId1");
		assertThat(user.getNickname()).isEqualTo("testNickname1");
		assertThat(user.getAddress()).isEqualTo("Seoul");
		assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
		assertThat(user.getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaaaaaaa");
	}
	@Test
	void UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
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

		UserUpdate userUpdate = UserUpdate.builder()
				.nickname("testNickname1-n")
				.address("Pangyo")
				.build();

		//when
		user = user.update(userUpdate);

		//then
		assertThat(user.getId()).isEqualTo(1L);
		assertThat(user.getEmail()).isEqualTo("testId1");
		assertThat(user.getNickname()).isEqualTo("testNickname1-n");
		assertThat(user.getAddress()).isEqualTo("Pangyo");
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(user.getCertificationCode()).isEqualTo("aaaa-aaa-aaaaaaa");
		assertThat(user.getLastLoginAt()).isEqualTo(100L);
	}
	@Test
	void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
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
		user = user.login(new TestClockHolder(123123L));

		//then
		assertThat(user.getId()).isEqualTo(1L);
		assertThat(user.getEmail()).isEqualTo("testId1");
		assertThat(user.getNickname()).isEqualTo("testNickname1");
		assertThat(user.getAddress()).isEqualTo("Seoul");
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(user.getCertificationCode()).isEqualTo("aaaa-aaa-aaaaaaa");
		assertThat(user.getLastLoginAt()).isEqualTo(123123L);
	}
	@Test
	void 유효한_인증코드로_계정을_활성화_할_수_있다() {
		//given
		User user = User.builder()
				.id(1L)
				.email("testId1")
				.nickname("testNickname1")
				.address("Seoul")
				.status(UserStatus.PENDING)
				.lastLoginAt(100L)
				.certificationCode("aaaa-aaa-aaaaaaa")
				.build();

		//when
		user = user.certificate("aaaa-aaa-aaaaaaa");

		//then
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}
	@Test
	void 잘못된_인증코드로_계정을_활성화_하려하면_에러를_던진다() {
		//given
		User user = User.builder()
				.id(1L)
				.email("testId1")
				.nickname("testNickname1")
				.address("Seoul")
				.status(UserStatus.PENDING)
				.lastLoginAt(100L)
				.certificationCode("aaaa-aaa-aaaaaaa")
				.build();

		//when
		//then
		assertThatThrownBy(() -> {
			user.certificate("aaaa-aaa-aaaaaaab");
		}).isInstanceOf(CertificationCodeNotMatchedException.class);
	}
}
