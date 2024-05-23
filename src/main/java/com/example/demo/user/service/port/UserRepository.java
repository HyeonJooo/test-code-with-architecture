package com.example.demo.user.service.port;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;

@Repository
public interface UserRepository {

	Optional<UserEntity> findById(long id);

	Optional<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus);

	Optional<UserEntity> findByIdAndStatus(long id, UserStatus userStatus);

	UserEntity save(UserEntity userEntity);
}
