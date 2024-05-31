package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;

public class UserServiceTest {

	private UserServiceImpl userService;

	@BeforeEach
	void init() {
		FakeMailSender fakeMailSender = new FakeMailSender();
		FakeUserRepository fakeUserRepository = new FakeUserRepository();
		this.userService = UserServiceImpl.builder()
				.userRepository(fakeUserRepository)
				.clockHolder(new TestClockHolder(123123123L))
				.uuidHolder(new TestUuidHolder("aaaaa-aaa-aaaaaaa"))
				.certificationService(new CertificationService(fakeMailSender))
				.build();

		fakeUserRepository.save(User.builder()
				.id(1L)
				.email("testId1")
				.nickname("testNickname1")
				.address("Seoul")
				.certificationCode("aaaaaa-aaaa-aaa-aaaaaaaaaaaa")
				.status(UserStatus.ACTIVE)
				.lastLoginAt(0L)
				.build());
		fakeUserRepository.save(User.builder()
				.id(2L)
				.email("testId2")
				.nickname("testNickname2")
				.address("Seoul")
				.certificationCode("aaaaaa-aaaa-aaa-aaaaaaaaaaab")
				.status(UserStatus.PENDING)
				.lastLoginAt(0L)
				.build());
	}

	@Test
	void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
		//given
		String email = "testId1";

		//when
		User result = userService.getByEmail(email);

		//then
		assertThat(result.getNickname()).isEqualTo("testNickname1");
	}

	@Test
	void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
		//given
		String email = "testId2";

		//when
		//then
		assertThatThrownBy(() -> {
			userService.getByEmail(email);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
		//given
		//when
		User result = userService.getById(1);

		//then
		assertThat(result.getNickname()).isEqualTo("testNickname1");
	}

	@Test
	void getById는_PENDING_상태인_유저를_찾아올_수_없다() {
		//given
		//when
		//then
		assertThatThrownBy(() -> {
			userService.getById(2);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void userCreate를_이용하여_유저를_생성할_수_있다() {
		//given
		UserCreate userCreate = UserCreate.builder()
			.email("test1@naver.com")
			.address("Seoul")
			.nickname("test-nickname")
			.build();

		//when
		User result = userService.create(userCreate);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
		assertThat(result.getCertificationCode()).isEqualTo("aaaaa-aaa-aaaaaaa");
	}

	@Test
	void userUpdate를_이용하여_유저를_수정할_수_있다() {
		//given
		UserUpdate userUpdate = UserUpdate.builder()
			.address("Incheon2")
			.nickname("test-nickname-n2")
			.build();

		//when
		userService.update(1, userUpdate);

		//then
		User user = userService.getById(1);
		assertThat(user.getId()).isNotNull();
		assertThat(user.getAddress()).isEqualTo("Incheon2");
		assertThat(user.getNickname()).isEqualTo("test-nickname-n2");
	}

	@Test
	void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
		//given
		//when
		userService.login(1);

		//then
		User user = userService.getById(1);
		assertThat(user.getLastLoginAt()).isEqualTo(123123123L);
	}

	@Test
	void PENDING_상태의_사용자는_인증코드로_ACTIVE_시킬_수_있다() {
		//given
		//when
		userService.verifyEmail(2, "aaaaaa-aaaa-aaa-aaaaaaaaaaab");

		//then
		User user = userService.getById(2);
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	void PENDING_상태의_사용자는_잘못된_인증코드를_받으면_에러를_던진다() {
		//given
		//when
		//then
		assertThatThrownBy(() -> {
			userService.verifyEmail(2, "aaaaaa-aaaa-aaa-aaaaaaaaaaac");
		}).isInstanceOf(CertificationCodeNotMatchedException.class);
	}
}
