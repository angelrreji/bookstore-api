package com.myproject.book_store.service.impl;

import com.myproject.book_store.mapper.UserInfoUserDetailsMapper;
import com.myproject.book_store.repository.UserInfoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoUserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    public UserInfoUserDetailsServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userInfoRepository.findById(username)
                .map(UserInfoUserDetailsMapper::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }
}
