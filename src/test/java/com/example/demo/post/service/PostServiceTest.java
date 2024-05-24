package com.example.demo.post.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
	@Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PostServiceTest {

	@Autowired
	private PostService postService;

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
		assertThat(result.getCreatedAt()).isGreaterThan(0);
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
		assertThat(result.getModifiedAt()).isGreaterThan(0);
	}

}
