package com.ParkCore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ParkCore.dto.LoginRequestDTO;
import com.ParkCore.dto.RegisterRequestDTO;
import com.ParkCore.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        var response = userService.login(body);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        var response = userService.register(body);
        return ResponseEntity.ok(response);
    }
	
}
