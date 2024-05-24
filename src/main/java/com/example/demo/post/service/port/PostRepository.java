package com.example.demo.post.service.port;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.post.domain.Post;

@Repository
public interface PostRepository {

	Optional<Post> findById(long id);

	Post save(Post post);
}
