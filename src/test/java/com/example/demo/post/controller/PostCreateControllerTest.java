package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

public class PostCreateControllerTest {

	@Test
	void 사용자는_게시물을_작성할_수_있다() {
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
		PostCreate postCreate = PostCreate.builder()
			.writerId(1)
			.content("hello")
			.build();

		//when
		ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getContent()).isEqualTo("hello");
		assertThat(result.getBody().getCreatedAt()).isEqualTo(1678530673958L);
		assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
		assertThat(result.getBody().getWriter().getEmail()).isEqualTo("testId1");
		assertThat(result.getBody().getWriter().getNickname()).isEqualTo("testNickname1");

	}

}
