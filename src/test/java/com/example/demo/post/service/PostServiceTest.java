package com.example.demo.post.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

public class PostServiceTest {

	private PostServiceImpl postService;

	@BeforeEach
	void init() {
		FakePostRepository fakePostRepository = new FakePostRepository();
		FakeUserRepository fakeUserRepository = new FakeUserRepository();
		this.postService = PostServiceImpl.builder()
			.postRepository(fakePostRepository)
			.userRepository(fakeUserRepository)
			.clockHolder(new TestClockHolder(1678530673958L))
			.build();

		User user1 = User.builder()
			.id(1L)
			.email("testId1")
			.nickname("testNickname1")
			.address("Seoul")
			.certificationCode("aaaaaa-aaaa-aaa-aaaaaaaaaaaa")
			.status(UserStatus.ACTIVE)
			.lastLoginAt(0L)
			.build();
		User user2 = User.builder()
			.id(2L)
			.email("testId2")
			.nickname("testNickname2")
			.address("Seoul")
			.certificationCode("aaaaaa-aaaa-aaa-aaaaaaaaaaab")
			.status(UserStatus.PENDING)
			.lastLoginAt(0L)
			.build();
		fakeUserRepository.save(user1);
		fakeUserRepository.save(user2);
		fakePostRepository.save(Post.builder()
			.id(1L)
			.content("helloworld")
			.createdAt(1678530673958L)
			.modifiedAt(0L)
			.writer(user1)
			.build());
	}

	@Test
	void getById는_존재하는_게시물을_내려준다() {
		//given
		//when
		Post result = postService.getById(1);

		//then
		assertThat(result.getContent()).isEqualTo("helloworld");
		assertThat(result.getWriter().getEmail()).isEqualTo("testId1");
	}

	@Test
	void postCreate_를_사용하여_게시물을_생성할_수_있다() {
		//given
		PostCreate postCreate = PostCreate.builder()
			.writerId(1)
			.content("foobar")
			.build();

		//when
		Post result = postService.create(postCreate);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getContent()).isEqualTo("foobar");
		assertThat(result.getCreatedAt()).isEqualTo(1678530673958L);
	}

	@Test
	void postUpdate를_사용하여_게시물을_수정할_수_있다() {
		//given
		PostUpdate postUpdate = PostUpdate.builder()
			.content("foo bar update")
			.build();

		//when
		postService.update(1, postUpdate);

		//then
		Post result = postService.getById(1);
		assertThat(result.getContent()).isEqualTo("foo bar update");
		assertThat(result.getModifiedAt()).isEqualTo(1678530673958L);
	}

}
