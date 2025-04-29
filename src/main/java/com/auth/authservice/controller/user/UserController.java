package com.auth.authservice.controller.user;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@SecurityRequirement(name = "Auth")
public class UserController {

	@GetMapping("test")
	public String getMethodName(@RequestParam String param) {
		return "Hello " + param;
	}

}
