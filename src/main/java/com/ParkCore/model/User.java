package com.ParkCore.model;

import com.ParkCore.enums.UserRole;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "User_Pk")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@NotNull
	@Column(length = 200, nullable = false)
	private String name;
	
	@NotBlank
	@NotNull
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
	private UserRole role;
	
}
