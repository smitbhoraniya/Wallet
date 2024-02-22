package com.swiggy.wallet.controllers;

import com.swiggy.wallet.models.requestModels.UserRequestModel;
import com.swiggy.wallet.models.responseModels.UserResponseModel;
import com.swiggy.wallet.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("")
    public ResponseEntity<UserResponseModel> registerUser(@RequestBody UserRequestModel user) {
        UserResponseModel returnedUser = userService.register(user);
        return new ResponseEntity<>(returnedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser() {
        userService.delete();
        return ResponseEntity.noContent().build();
    }
}
