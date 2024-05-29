package com.example.demo.post.domain;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Post {
	private final Long id;
	private final String content;
	private final Long createdAt;
	private final Long modifiedAt;
	private final User writer;

	public static Post from(User writer, PostCreate postCreate, ClockHolder clockHolder) {
		return Post.builder()
			.content(postCreate.getContent())
			.createdAt(clockHolder.millis())
			.writer(writer)
			.build();
	}

	public Post update(PostUpdate postUpdate, ClockHolder clockHolder) {
		return Post.builder()
			.id(id)
			.content(postUpdate.getContent())
			.createdAt(createdAt)
			.modifiedAt(clockHolder.millis())
			.writer(writer)
			.build();
	}
}
