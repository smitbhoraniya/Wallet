package com.swiggy.wallet.clients;

import com.swiggy.wallet.enums.Currency;
import com.swiggy.wallet.models.Money;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import proto.*;

public class CurrencyConvertClient {
    public static double convert(Money money, Currency currency) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        CurrencyConvertServiceGrpc.CurrencyConvertServiceBlockingStub stub = CurrencyConvertServiceGrpc.newBlockingStub(channel);
        CurrencyConvertRequest request = CurrencyConvertRequest
                .newBuilder()
                .setAmount((float) money.getAmount())
                .setFromCurrency(String.valueOf(money.getCurrency()))
                .setToCurrency(String.valueOf(currency))
                .build();
        CurrencyConvertResponse response = stub.convert(request);
        channel.shutdown();
        return response.getAmount();
    }
}
