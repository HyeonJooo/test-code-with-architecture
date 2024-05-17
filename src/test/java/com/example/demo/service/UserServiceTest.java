package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
	@Sql(value = "/sql/user-repository-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD) //테스트 메서드가 종료될 때
})
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Test
	void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
		//given
		String email = "testId1";

		//when
		UserEntity result = userService.getByEmail(email);

		//then
		assertThat(result.getNickname()).isEqualTo("testNickname");
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
		assertThat(result.getNickname()).isEqualTo("testNickname");
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
}
