package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}


@RestController
class PersonController {

	@PostMapping("/person/{id}")
	Response person(@PathVariable int id, @RequestBody String body) {
		System.out.println("ID [" + id + "], body [" + body + "]");
		return new Response("marcin");
	}
}

class Response {
	String name;

	public Response(String name) {
		this.name = name;
	}

	public Response() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
