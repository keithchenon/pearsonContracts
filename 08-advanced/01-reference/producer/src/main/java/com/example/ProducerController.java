package com.example;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

	private final PersonCheckingService personCheckingService;

	public ProducerController(PersonCheckingService personCheckingService) {
		this.personCheckingService = personCheckingService;
	}

	@RequestMapping(value = "/check",
			method=RequestMethod.POST,
			consumes="application/json",
			produces="application/json")
	public Response check(@RequestBody PersonToCheck personToCheck) {
		if(this.personCheckingService.shouldGetBeer(personToCheck)) {
			return new Response(BeerCheckStatus.OK, "check", personToCheck.name);
		}
		return new Response(BeerCheckStatus.NOT_OK, "check", personToCheck.name);
	}
	
}

interface PersonCheckingService {
	Boolean shouldGetBeer(PersonToCheck personToCheck);
}

class PersonToCheck {
	public String name;
	public int age;

	public PersonToCheck(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public PersonToCheck() {
	}
}

class Response {
	public BeerCheckStatus status;
	public String path;
	public String name;

	public Response(BeerCheckStatus status, String path, String name) {
		this.status = status;
		this.path = path;
		this.name = name;
	}
}

enum BeerCheckStatus {
	OK, NOT_OK
}