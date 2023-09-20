package com.example.ep.service;

import com.example.ep.domain.Role;
import com.example.ep.domain.User;
import com.example.ep.dto.LoginResponseDTO;
import com.example.ep.repository.RoleRepository;
import com.example.ep.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public User registerUser(String username, String password) {
        Set<Role> userRoles = roleRepository.findByAuthority("USER").stream().collect(Collectors.toSet());
        User user = new User(username, passwordEncoder.encode(password), userRoles);
        return userRepository.save(user);
    }

    public LoginResponseDTO loginUser(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = tokenService.generateJwt(auth);
        return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
    }
}
