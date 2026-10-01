package com.myproject.book_store.controller;

import com.myproject.book_store.dto.UserInfoDto;
import com.myproject.book_store.service.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-info")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUserInfo(@RequestBody UserInfoDto userInfoDto) {
        UserInfoDto created = userInfoService.createUser(userInfoDto);
        return new ResponseEntity<>("User " + created.userName() + " is created", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserInfoDto userInfoDto) {
        return new ResponseEntity<>(userInfoService.getUserInfo(userInfoDto), HttpStatus.OK);
    }
}
