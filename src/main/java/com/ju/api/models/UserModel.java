package com.ju.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Table("usuario")
public class UserModel {
    @Id
    private Long id;
    @Column("username")
    private String username;
    @Column("password")
    private String password;
    @Column("cpf")
    private String cpf;
    @Column("email")
    private String email;
    private List<String> roles = new ArrayList<>();
    private boolean active = true;
}