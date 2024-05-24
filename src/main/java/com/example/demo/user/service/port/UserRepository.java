package com.example.demo.user.service.port;

import java.util.Optional;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

public interface UserRepository {

	Optional<User> findById(long id);

	Optional<User> findByEmailAndStatus(String email, UserStatus userStatus);

	Optional<User> findByIdAndStatus(long id, UserStatus userStatus);

	User save(User user);
}
