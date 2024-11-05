package com.example.projectbase.service;

import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.entity.VerificationToken;

public interface VerificationTokenService {

    VerificationToken getByToken(String token);

    VerificationToken createVerificationToken(User user);

    void deleteToken(Long id);

    //Delete junk token
    void deleteAllJunkVerificationToken();

}
