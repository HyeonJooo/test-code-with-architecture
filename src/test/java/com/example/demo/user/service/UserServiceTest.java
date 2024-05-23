package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.service.UserService;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
	@Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD) //테스트 메서드가 종료될 때
})
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@MockBean //test를 실행할 때 MockBean이 주입돼서 실행된다.
	private JavaMailSender javaMailSender;

	@Test
	void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
		//given
		String email = "testId1";

		//when
		UserEntity result = userService.getByEmail(email);

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
		UserEntity result = userService.getById(1);

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

		//SimpleMailMessage를 사용하는 send가 호출돼도 아무 것도 하지말라는 뜻
		doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

		//when
		UserEntity result = userService.create(userCreate);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
		//assertThat(result.getCertificationCode()).isEqualTo("T.T"); //FIXME
	}

	@Test
	void userUpdate를_이용하여_유저를_수정할_수_있다() {
		//given
		UserUpdate userUpdate = UserUpdate.builder()
			.address("Incheon")
			.nickname("test-nickname-n")
			.build();

		//when
		userService.update(1, userUpdate);

		//then
		UserEntity userEntity = userService.getById(1);
		assertThat(userEntity.getId()).isNotNull();
		assertThat(userEntity.getAddress()).isEqualTo("Incheon");
		assertThat(userEntity.getNickname()).isEqualTo("test-nickname-n");
	}

	@Test
	void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
		//given
		//when
		userService.login(1);

		//then
		UserEntity userEntity = userService.getById(1);
		assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
		// assertThat(userEntity.getLastLoginAt()).isEqualTo(""); //FIXME
	}

	@Test
	void PENDING_상태의_사용자는_인증코드로_ACTIVE_시킬_수_있다() {
		//given
		//when
		userService.verifyEmail(2, "aaaaaa-aaaa-aaa-aaaaaaaaaaab");

		//then
		UserEntity userEntity = userService.getById(2);
		assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
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
