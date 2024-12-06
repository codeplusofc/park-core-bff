package com.ParkCore.infra.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import org.springframework.security.core.userdetails.User;
import com.ParkCore.repository.UserRepository;


@Component
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
    private UserRepository repository;
    
	 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        var user = repository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	       
	        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRole().toUpperCase());
	        
	        return new User(
	            user.getName(), 
	            user.getPassword(), 
	            Collections.singleton(authority)
	        );
	    }
}
