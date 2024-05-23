package com.example.demo.post.service.port;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.post.infrastructure.PostEntity;

@Repository
public interface PostRepository {

	Optional<PostEntity> findById(long id);

	PostEntity save(PostEntity postEntity);
}
