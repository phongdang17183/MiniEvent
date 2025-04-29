package com.example.MiniEvent.service.inteface;

import com.example.MiniEvent.model.entity.AuthenticatedUser;
import com.example.MiniEvent.model.entity.DecodedTokenInfo;

public interface AuthService {
    AuthenticatedUser createUser(String email, String password);
    DecodedTokenInfo verifyToken(String idToken);
    Object login(String email, String password);
}
