package com.swiggy.wallet.services;

import com.swiggy.wallet.execptions.UserAlreadyExistsException;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.models.requestModels.UserRequestModel;
import com.swiggy.wallet.repositories.UserRepository;
import com.swiggy.wallet.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(UserRequestModel user) throws UserAlreadyExistsException {
        if(userRepository.findByUserName(user.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Username taken. Please try with another username.");
        User userToSave = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()));
        return userRepository.save(userToSave);
    }
}
