package com.myproject.book_store.service.impl;

import com.myproject.book_store.dto.UserInfoDto;
import com.myproject.book_store.entity.UserInfo;
import com.myproject.book_store.exception.UserAlreadyExistsException;
import com.myproject.book_store.mapper.UserInfoMapper;
import com.myproject.book_store.repository.UserInfoRepository;
import com.myproject.book_store.service.JWTService;
import com.myproject.book_store.service.UserInfoService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder,
                               AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public UserInfoDto createUser(UserInfoDto userInfoDto) {
        if (userInfoDto.userName() == null || userInfoDto.userName().isBlank()
                || userInfoDto.password() == null || userInfoDto.password().isBlank()) {
            throw new IllegalArgumentException("userName and password are required");
        }
        if (userInfoRepository.existsById(userInfoDto.userName())) {
            throw new UserAlreadyExistsException("User " + userInfoDto.userName() + " already exists");
        }
        UserInfo userInfo = UserInfoMapper.toEntity(userInfoDto);
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        // Never trust client-supplied roles on self-registration (privilege escalation).
        userInfo.setRoles(DEFAULT_ROLE);
        return UserInfoMapper.toDto(userInfoRepository.save(userInfo));
    }

    @Override
    public String getUserInfo(UserInfoDto userInfoDto) {
        // Throws AuthenticationException (-> 401) on bad credentials.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userInfoDto.userName(), userInfoDto.password()));
        return jwtService.generateToken(userInfoDto.userName());
    }
}
