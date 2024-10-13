package com.alphaka.authservice.redis.repository;

import com.alphaka.authservice.redis.entity.AccessTokenBlacklist;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenBlackListRepository extends CrudRepository<AccessTokenBlacklist, String> {
}
