package com.swiggy.wallet.controllers;

import com.swiggy.wallet.execptions.UserAlreadyExistsException;
import com.swiggy.wallet.models.requestModels.UserRequestModel;
import com.swiggy.wallet.models.User;
import com.swiggy.wallet.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("")
    public ResponseEntity<User> registerUser(@RequestBody UserRequestModel user) throws UserAlreadyExistsException {
        User returnedUser = userService.register(user);
        return new ResponseEntity<>(returnedUser, HttpStatus.CREATED);
    }
}
