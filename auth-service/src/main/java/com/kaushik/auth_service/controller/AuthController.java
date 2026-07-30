package com.kaushik.auth_service.controller;

import com.kaushik.auth_service.dto.AuthResponse;
import com.kaushik.auth_service.dto.LoginRequest;
import com.kaushik.auth_service.dto.RegisterRequest;
import com.kaushik.auth_service.model.Users;
import com.kaushik.auth_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(
			@RequestBody RegisterRequest request) {

		return ResponseEntity.ok(
				authService.register(request)
		);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(
			@RequestBody LoginRequest request,
			HttpServletRequest httpRequest) {

		Authentication authentication =
				authService.login(request);

		SecurityContext context =
				SecurityContextHolder.createEmptyContext();

		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);

		HttpSession session =
				httpRequest.getSession(true);

		session.setAttribute(
				HttpSessionSecurityContextRepository
						.SPRING_SECURITY_CONTEXT_KEY,
				context
		);

		return ResponseEntity.ok(
				"Login successful"
		);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(
			HttpServletRequest request) {

		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate();
		}

		SecurityContextHolder.clearContext();

		return ResponseEntity.ok(
				"Logout successful"
		);
	}

	@GetMapping("/me")
	public ResponseEntity<?> currentUser(
			Authentication authentication) {

		Users user = authService.findByEmail(authentication.getName());

		return ResponseEntity.ok(
				new AuthResponse(
						"User found",
						user.getEmail(),
						user.getRole()
				)
		);
	}

}
