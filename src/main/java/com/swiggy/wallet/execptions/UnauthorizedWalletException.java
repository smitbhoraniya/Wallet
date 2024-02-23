package com.swiggy.wallet.execptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UnauthorizedWalletException extends RuntimeException {
    public UnauthorizedWalletException(String message) {
        super(message);
    }
}
