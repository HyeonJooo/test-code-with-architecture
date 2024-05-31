package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

public class PostControllerTest {

	@Test
	void 사용자는_게시물을_단건조회_할_수_있다() {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();
		User user = User.builder()
			.id(1L)
			.email("testId1")
			.nickname("testNickname1")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaa-aaa-aaaaaaa")
			.build();
		testContainer.userRepository.save(user);
		testContainer.postRepository.save(Post.builder()
			.id(1L)
			.content("helloworld")
			.writer(user)
			.createdAt(100L)
			.build());

		//when
		ResponseEntity<PostResponse> result = testContainer.postController.getById(1L);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getContent()).isEqualTo("helloworld");
		assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
		assertThat(result.getBody().getWriter().getEmail()).isEqualTo("testId1");
		assertThat(result.getBody().getWriter().getNickname()).isEqualTo("testNickname1");
	}

	@Test
	void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다() {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();

		//when
		//then
		assertThatThrownBy(() -> {
			testContainer.postController.getById(1L);
		}).isInstanceOf(ResourceNotFoundException.class);

	}

	@Test
	void 사용자는_게시물을_수정할_수_있다() {
		//given
		TestContainer testContainer = TestContainer.builder()
			.clockHolder(() -> 1678530673958L)
			.build();
		User user = User.builder()
			.id(1L)
			.email("testId1")
			.nickname("testNickname1")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaa-aaa-aaaaaaa")
			.build();
		testContainer.userRepository.save(user);
		testContainer.postRepository.save(Post.builder()
			.id(1L)
			.content("helloworld")
			.writer(user)
			.createdAt(100L)
			.build());
		PostUpdate postUpdate = PostUpdate.builder()
			.content("helloworld2")
			.build();

		//when
		ResponseEntity<PostResponse> result = testContainer.postController.update(1L, postUpdate);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
		assertThat(result.getBody().getModifiedAt()).isEqualTo(1678530673958L);
		assertThat(result.getBody().getContent()).isEqualTo("helloworld2");
		assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
		assertThat(result.getBody().getWriter().getEmail()).isEqualTo("testId1");
		assertThat(result.getBody().getWriter().getNickname()).isEqualTo("testNickname1");
	}
}
