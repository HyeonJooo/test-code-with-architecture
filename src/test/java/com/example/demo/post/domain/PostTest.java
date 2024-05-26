package com.example.demo.post.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

public class PostTest {

	@Test
	void PostCreate으로_게시물을_만들_수_있다() {
		//given
		PostCreate postCreate = PostCreate.builder()
				.writerId(1)
				.content("helloworld")
				.build();

		User writer = User.builder()
				.email("testId1")
				.nickname("testNickname1")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.certificationCode("aaaa-aaa-aaaaaaa")
				.build();

		//when
		Post post = Post.from(writer, postCreate);

		//then
		assertThat(post.getContent()).isEqualTo("helloworld");
		assertThat(post.getWriter().getEmail()).isEqualTo("testId1");
		assertThat(post.getWriter().getNickname()).isEqualTo("testNickname1");
		assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
		assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaa-aaa-aaaaaaa");

	}
}
