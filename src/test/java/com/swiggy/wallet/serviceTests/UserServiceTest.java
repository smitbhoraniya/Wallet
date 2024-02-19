package com.swiggy.wallet.serviceTests;

import com.swiggy.wallet.execptions.UserAlreadyExistsException;
import com.swiggy.wallet.execptions.UserNotFoundException;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.requestModels.UserRequestModel;
import com.swiggy.wallet.models.responseModels.UserResponseModel;
import com.swiggy.wallet.repositories.UserRepository;
import com.swiggy.wallet.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private Authentication authentication;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void expectUserCreated() throws UserAlreadyExistsException {
        when(userRepository.findByUserName("user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(new User("user", "encodedPassword"));
        UserRequestModel userRequestModel = new UserRequestModel("user", "password");

        UserResponseModel savedUser = userService.register(userRequestModel);

        assertEquals("user", savedUser.getUsername());
        assertNotNull(savedUser.getWallet());
        verify(userRepository, times(1)).findByUserName("user");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void expectUserAlreadyExistsException() {
        when(userRepository.findByUserName("existingUser")).thenReturn(Optional.of(new User()));
        UserRequestModel userRequestModel = new UserRequestModel("existingUser", "password");

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.register(userRequestModel);
        });
        verify(userRepository, times(1)).findByUserName("existingUser");
        verify(userRepository, never()).save(any());
    }

    @Test
    void expectUserDeleted() {
        String username = "user";
        User user = new User(username, "password");
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserName("user")).thenReturn(Optional.of(user));

        userService.delete();

        verify(userRepository, times(1)).findByUserName("user");
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void expectUserNotFoundExceptionInUserDelete() {
        when(userRepository.findByUserName("NoUser")).thenReturn(Optional.empty());
        when(authentication.getName()).thenReturn("NoUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(UserNotFoundException.class, () -> userService.delete());
        verify(userRepository, times(1)).findByUserName("NoUser");
        verify(userRepository, never()).delete(any());
    }
}
