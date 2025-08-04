package com.example.demo.dto;

import com.example.demo.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String email;
    private Set<String> role;
    private String sdt;
    private String name;
}
