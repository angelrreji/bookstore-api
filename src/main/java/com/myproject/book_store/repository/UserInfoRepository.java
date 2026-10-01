package com.myproject.book_store.repository;

import com.myproject.book_store.entity.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserInfoRepository extends MongoRepository<UserInfo, String> {

}
