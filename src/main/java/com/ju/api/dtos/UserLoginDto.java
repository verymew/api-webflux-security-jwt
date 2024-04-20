package com.ju.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserLoginDto {
    public String email;
    public String senha;
}
