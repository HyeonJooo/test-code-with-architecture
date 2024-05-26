package com.example.demo.post.controller.response;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

public class PostResponseTest {

	@Test
	void Post으로_응답을_생성할_수_있다() {
		//given
		Post post = Post.builder()
				.content("helloworld")
				.writer(User.builder()
						.email("testId1")
						.nickname("testNickname1")
						.address("Seoul")
						.status(UserStatus.ACTIVE)
						.certificationCode("aaaa-aaa-aaaaaaa")
						.build())
				.build();

		//when
		PostResponse postResponse = PostResponse.from(post);

		//then
		assertThat(post.getContent()).isEqualTo("helloworld");
		assertThat(post.getWriter().getEmail()).isEqualTo("testId1");
		assertThat(post.getWriter().getNickname()).isEqualTo("testNickname1");
		assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
		assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaa-aaa-aaaaaaa");
	}
}
