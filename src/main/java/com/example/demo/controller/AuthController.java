package com.example.demo.controller;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.JwtBlacklistService;
import com.example.demo.service.JwtService;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtBlacklistService jwtBlacklistService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword()));
        String jwt = jwtService.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO dto){
        if(userRepository.findByUsername(dto.getUsername()).isPresent()){
            throw new InvalidRequestException("Người dùng đã tồn tại");
        }
        Set<Role> roles = new HashSet<>();
        for(String role : dto.getRole()){
            String toupperRole = role.toUpperCase();
            if(!toupperRole.equals("MANAGER")&&!toupperRole.equals("STAFF")){
                throw new RuntimeException("Vai trò không hợp lệ");
            }
            Role r = roleRepository.findByName(toupperRole)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(toupperRole);
                        roleRepository.save(newRole);
                        return newRole;
                    });
            roles.add(r);
        }
        User user = new User();
        BeanUtils.copyProperties(dto, user,"password", "role");
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok("Đăng ký thành công");
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        jwtBlacklistService.blacklistToken(token);
        return ResponseEntity.ok("Đăng xuất thành công");
    }
} 