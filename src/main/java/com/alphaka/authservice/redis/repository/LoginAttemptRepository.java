package com.alphaka.authservice.redis.repository;

import com.alphaka.authservice.redis.entity.LoginAttempt;
import org.springframework.data.repository.CrudRepository;

public interface LoginAttemptRepository extends CrudRepository<LoginAttempt, String> {
}
