package com.chatapp.gateway.model.user;

import com.chatapp.gateway.types.AuthProvider;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "user")
@Builder
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String providerId;
    private AuthProvider provider;
}