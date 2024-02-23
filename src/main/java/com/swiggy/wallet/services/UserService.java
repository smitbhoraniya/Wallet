package com.swiggy.wallet.services;

import com.swiggy.wallet.execptions.UserAlreadyExistsException;
import com.swiggy.wallet.execptions.UserNotFoundException;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.requestModels.UserRequestModel;
import com.swiggy.wallet.models.responseModels.UserResponseModel;
import com.swiggy.wallet.models.responseModels.WalletResponseModel;
import com.swiggy.wallet.repositories.UserRepository;
import com.swiggy.wallet.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseModel register(UserRequestModel user) {
        if (userRepository.findByUserName(user.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Username taken. Please try with another username.");
        User userToSave = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getCountry());
        User createdUser = userRepository.save(userToSave);

        WalletResponseModel wallet = walletService.create(createdUser.getUserName());

        return new UserResponseModel(createdUser.getUserName(), wallet);
    }

    @Override
    public void delete() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userToDelete = userRepository.findByUserName(username);
        if (userToDelete.isEmpty()) {
            throw new UserNotFoundException("User could not be found.");
        }

        userRepository.delete(userToDelete.get());
    }
}
