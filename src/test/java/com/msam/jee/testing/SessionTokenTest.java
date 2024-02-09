package com.msam.jee.testing;

import com.msam.jee.ejb.token.SessionTokenService;
import com.msam.jee.ejb.user.UserRepoEjb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SessionTokenTest {

    @InjectMocks
    protected SessionTokenService tokenService;

    @Mock
    protected UserRepoEjb userRepo;

    private final Map<String, String> testCredentials = Map.of(
            "Alice", "passw1",
            "Bob", "passw2",
            "Carl", "passw3"
    );

    @BeforeEach
    private void init() {
        for (var entry : testCredentials.entrySet()) {
            when(userRepo.validateCredentials(entry.getKey(), entry.getValue())).thenReturn(true);
        }
        when(userRepo.validateCredentials("Alice", "invalidpass")).thenReturn(false);
    }

    @Test
    void testLogin() {
        Optional<String> token = tokenService.login("Alice", "invalidpass");
        assertTrue(token.isEmpty());
        for (var entry : testCredentials.entrySet()) {
            token = tokenService.login(entry.getKey(), entry.getValue());
            assertTrue(token.isPresent());
        }
    }

    @Test
    void testValidate() {
        String token = tokenService.login("Alice", "passw1").orElseThrow();
        assertFalse(tokenService.validate("Alice", ""));
        assertTrue(tokenService.validate("Alice", token));
    }

    @Test
    void testLogout() {
        String token = tokenService.login("Alice", "passw1").orElseThrow();
        tokenService.logout("Alice", token);
        assertFalse(tokenService.validate("Alice", token));
    }
}
