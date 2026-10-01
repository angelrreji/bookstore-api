package com.myproject.book_store.mapper;

import com.myproject.book_store.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UserInfoUserDetailsMapper implements UserDetails {

    private final String userName;
    private final String password;
    private final List<GrantedAuthority> grantedAuthorities;

    public UserInfoUserDetailsMapper(UserInfo userInfo) {
        userName = userInfo.getUserName();
        password = userInfo.getPassword();
        String roles = userInfo.getRoles() == null ? "" : userInfo.getRoles();
        // hasRole('X') checks for the authority "ROLE_X", so make sure the prefix is present.
        grantedAuthorities = Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(r -> !r.isEmpty())
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }
}
