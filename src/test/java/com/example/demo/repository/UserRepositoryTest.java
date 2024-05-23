package com.example.demo.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;

@DataJpaTest
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
		//given
		//when
		Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

		//then
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	void findByIdAndStatus_는_데이터가_없으면_optional_empty_를_내려준다() {
		//given
		//when
		Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

		//then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
		//given
		//when
		Optional<UserEntity> result = userRepository.findByEmailAndStatus("testId1", UserStatus.ACTIVE);

		//then
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	void findByEmailAndStatus_는_데이터가_없으면_optional_empty_를_내려준다() {
		//given
		//when
		Optional<UserEntity> result = userRepository.findByEmailAndStatus("testId1", UserStatus.PENDING);

		//then
		assertThat(result.isEmpty()).isTrue();
	}
}
