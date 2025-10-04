package org.lessons.spring_library.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.lessons.spring_library.model.Role;
import org.lessons.spring_library.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DatabaseUserDetails implements UserDetails {

    private String username;

    private String password;

    private Set<GrantedAuthority> authorities;

    public DatabaseUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = new HashSet<>();
        for(Role role : user.getRoles()) {
            SimpleGrantedAuthority sGA = new SimpleGrantedAuthority(role.getName());
            this.authorities.add(sGA);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
