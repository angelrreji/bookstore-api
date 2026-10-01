package com.myproject.book_store.service;

import com.myproject.book_store.dto.UserInfoDto;

public interface UserInfoService {

    public UserInfoDto createUser(UserInfoDto userInfoDto);

    public String getUserInfo(UserInfoDto userInfoDto);


}
