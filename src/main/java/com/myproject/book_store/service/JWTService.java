package com.myproject.book_store.service;

public interface JWTService {

    String generateToken(String username);

    /** Returns the username if the token is validly signed and unexpired, otherwise null. */
    String validateAndExtractUsername(String token);
}
