package com.swiggy.wallet.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swiggy.wallet.enums.Country;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    @Column(unique = true)
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    private Country country;

    private LocalDateTime createdAt = LocalDateTime.now();

    public User(String userName, String password, Country country) {
        this.userName = userName;
        this.password = password;
        this.country = country;
    }
}
