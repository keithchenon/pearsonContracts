package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcin Grzejszczak
 */
@RestController
public class FooController {
	@GetMapping("/foo")
	ResponseEntity foo() {
		return ResponseEntity.status(200).build();
	}
}
