package com.swiggy.wallet.execptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidMoneyException extends RuntimeException {
    public InvalidMoneyException(String message) {
        super(message);
    }
}