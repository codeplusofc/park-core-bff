package com.ParkCore.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ParkCore.dto.LoginRequestDTO;
import com.ParkCore.dto.RegisterRequestDTO;
import com.ParkCore.dto.ResponseDTO;
import com.ParkCore.enums.UserRole;
import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.infra.security.TokenService;
import com.ParkCore.model.User;
import com.ParkCore.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//Create a user with encrypted password
	public User CreateUser(User user) {
		var userCreate = new User();
		
		userCreate.setName(user.getName());
		var encrypt = passwordEncoder.encode(user.getPassword());
		userCreate.setPassword(encrypt);
		userCreate.setRole(user.getRole());
		return userRepository.save(userCreate);
	}
	
	
	public User updateUser(User user, Long id) {
		var userFind = userRepository.findById(id);
		
		if(!userFind.isPresent()) {
			throw new RuntimeException("Can´t find a user with this id");
		}
		
		var userMod = userFind.get();
		userMod.setName(user.getName());
		var encrypt = passwordEncoder.encode(user.getPassword());
		userMod.setPassword(encrypt);
		userMod.setRole(user.getRole());
		return userRepository.save(userMod);
	}
	
	
	public List<User> findAllUsers(){
		return userRepository.findAll();
	}
	
	
	public ResponseEntity<ResponseDTO> login(LoginRequestDTO body) {
		var user = userRepository.findByName(body.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        if(passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            var token = tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }
        	throw new ObjectNotFoundException(user.getId(), User.class.getName());
	}
	
	public ResponseEntity<ResponseDTO> register(RegisterRequestDTO body){
		
		var user = userRepository.findByName(body.getName());

        if(user.isEmpty()) {
        	var newUser = new User();
            newUser.setName(body.getName());
            newUser.setPassword(passwordEncoder.encode(body.getPassword()));
            //In this phase, it's interesting to use Flyway.
            newUser.setRole(UserRole.USER);
            userRepository.save(newUser);

            var token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }
        throw new BadRequestException("Unable to register the user!!");
		
	}
	
	
	
}
