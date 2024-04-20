package com.ju.api.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("usuario")
public class UserModel {
    @Id
    private Long id;
    @Column("nome")
    private String nome;
    @Column("senha")
    private String senha;
    @Column("cpf")
    private String cpf;
    @Column("email")
    private String email;
}