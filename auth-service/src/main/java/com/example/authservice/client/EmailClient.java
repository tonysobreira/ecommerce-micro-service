package com.example.authservice.client;

import com.example.authservice.dto.email.ActivationEmailRequest;
import com.example.authservice.dto.email.PasswordResetEmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${email-service.name:email-service}")
public interface EmailClient {

	@PostMapping("/emails/activation")
	void sendActivation(@RequestBody ActivationEmailRequest request);

	@PostMapping("/emails/password-reset")
	void sendPasswordReset(@RequestBody PasswordResetEmailRequest request);

}
