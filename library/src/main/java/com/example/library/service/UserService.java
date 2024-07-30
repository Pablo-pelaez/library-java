package com.example.library.service;

import com.example.library.entity.User;
import com.example.library.exception.UserAlreadyExistsException;
import com.example.library.exception.InvalidCredentialsException;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public void registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /*
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    */
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return user;
    }
}
