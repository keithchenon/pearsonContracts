package com.example;

import java.util.concurrent.Callable;

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
	public Callable<Response> check(@RequestBody PersonToCheck personToCheck) {
		return () -> {
			if(this.personCheckingService.shouldGetBeer(personToCheck)) {
				return new Response(BeerCheckStatus.OK);
			}
			return new Response(BeerCheckStatus.NOT_OK);
		};
	}
	
}

interface PersonCheckingService {
	Boolean shouldGetBeer(PersonToCheck personToCheck);
}

class PersonToCheck {
	public int age;

	public PersonToCheck(int age) {
		this.age = age;
	}

	public PersonToCheck() {
	}
}

class Response {
	public BeerCheckStatus status;
	
	Response(BeerCheckStatus status) {
		this.status = status;
	}
}

enum BeerCheckStatus {
	OK, NOT_OK
}