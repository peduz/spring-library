package org.lessons.spring_library.security;

import java.util.Optional;

import org.lessons.spring_library.model.User;
import org.lessons.spring_library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if(userOpt.isPresent()) {
            return new DatabaseUserDetails(userOpt.get());
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

}
