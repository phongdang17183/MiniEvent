package com.example.MiniEvent.usecase;

import com.example.MiniEvent.adapter.repository.UserRepository;
import com.example.MiniEvent.adapter.web.exception.BaseApiException;
import com.example.MiniEvent.adapter.web.exception.UnauthorizedException;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.model.entity.DecodedTokenInfo;
import com.example.MiniEvent.service.inteface.AuthService;
import com.example.MiniEvent.usecase.impl.GetUserInfoUseCaseImpl;
import com.google.cloud.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetUserInfoUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private GetUserInfoUseCaseImpl getUserInfoUseCase;

    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser(
                "abc",
                "username",
                "abc@gmail.com",
                Timestamp.now(),
                0,
                0,
                "0901234567",
                "profile.jpg"
        );
    }

    @Test
    void getInfo_success_userExist() {
        String idToken = "idToken";

        // Set up
        DecodedTokenInfo decodedTokenInfo = new DecodedTokenInfo("abc", "abc@gmail.com", "google");
        when(authService.verifyToken(idToken)).thenReturn(decodedTokenInfo);
        when(userRepository.findByUid(decodedTokenInfo.getUid())).thenReturn(Optional.of(appUser));

        // Act
        Optional<AppUser> user =  getUserInfoUseCase.getInfo(idToken);

        // Assert
        verify(authService).verifyToken(idToken);
        verify(userRepository).findByUid(decodedTokenInfo.getUid());
        assertTrue(user.isPresent());
        assertEquals(appUser, user.get());
    }

    @Test
    void getInfo_success_userNotFound() {
        String idToken = "validToken";
        DecodedTokenInfo decodedToken = new DecodedTokenInfo("uid123", "user@example.com", "google");

        when(authService.verifyToken(idToken)).thenReturn(decodedToken);
        when(userRepository.findByUid(decodedToken.getUid())).thenReturn(Optional.empty());

        Optional<AppUser> result = getUserInfoUseCase.getInfo(idToken);

        assertTrue(result.isEmpty());

        verify(authService).verifyToken(idToken);
        verify(userRepository).findByUid("uid123");
    }


    @Test
    void getInfo_fail_wrongToken() {
        String idToken = "wrongToken";

        // Set up
        when(authService.verifyToken(idToken))
                .thenThrow(new UnauthorizedException("Invalid or expired Firebase token", HttpStatus.UNAUTHORIZED));

        // Act
        assertThrows(BaseApiException.class, () -> getUserInfoUseCase.getInfo(idToken));

        // Assert
        verify(authService).verifyToken(idToken);
        verifyNoMoreInteractions(userRepository);
    }


}
