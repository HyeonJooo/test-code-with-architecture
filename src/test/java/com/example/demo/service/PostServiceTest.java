package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.repository.PostEntity;

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
		PostEntity result = postService.getById(1);

		//then
		assertThat(result.getContent()).isEqualTo("helloworld");
		assertThat(result.getWriter().getEmail()).isEqualTo("testId1");
	}

	@Test
	void postCreateDto_를_사용하여_게시물을_생성할_수_있다() {
		//given
		PostCreateDto postCreateDto = PostCreateDto.builder()
			.writerId(1)
			.content("foobar")
			.build();

		//when
		PostEntity result = postService.create(postCreateDto);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getContent()).isEqualTo("foobar");
		assertThat(result.getCreatedAt()).isGreaterThan(0);
	}

	@Test
	void postUpdateDto를_사용하여_게시물을_수정할_수_있다() {
		//given
		PostUpdateDto postUpdateDto = PostUpdateDto.builder()
			.content("foo bar update")
			.build();

		//when
		postService.update(1, postUpdateDto);

		//then
		PostEntity result = postService.getById(1);
		assertThat(result.getContent()).isEqualTo("foo bar update");
		assertThat(result.getModifiedAt()).isGreaterThan(0);
	}

}
