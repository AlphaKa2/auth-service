package com.alphaka.authservice.redis.repository;

import com.alphaka.authservice.redis.entity.SmsAuthenticationCode;
import org.springframework.data.repository.CrudRepository;

public interface SmsAuthenticationCodeRepostiory extends CrudRepository<SmsAuthenticationCode, String> {
}
