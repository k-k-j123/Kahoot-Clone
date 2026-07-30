package com.kaushik.auth_service.service;

import com.kaushik.auth_service.Repository.UserRepository;
import com.kaushik.auth_service.model.Users;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user1 = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));

        User user = new User(
                user1.getEmail(),
                user1.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_"+user1.getRole().name())
                )
        );
        return user;
    }
}
