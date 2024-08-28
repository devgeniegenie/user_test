package com.example.user_test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class UserEntity extends BaseEntity{
    @Id
    private String id;

    @NonNull
    private String userName;

    @NonNull
    private String password;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;
}
