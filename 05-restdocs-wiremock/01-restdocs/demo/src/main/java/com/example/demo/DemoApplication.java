package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
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
class FooController {
	@GetMapping("/foo")
	String foo() {
		return "bar";
	}

	@PostMapping(value = "/person")
	Person person(@RequestBody Request request) {
		System.out.println(request.getName());
		return new Person(request.name, "g");
	}
}

class Person {
	public String name, surname;

	public Person(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public Person() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}

class Request {
	public String name;

	public Request(String name) {
		this.name = name;
	}

	public Request() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}



