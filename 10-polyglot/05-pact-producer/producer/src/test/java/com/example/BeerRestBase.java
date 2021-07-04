package com.example;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

public abstract class BeerRestBase {
	ProducerController producerController = new ProducerController(oldEnough());

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.producerController);
	}

	private PersonCheckingService oldEnough() {
		return argument -> argument.age >= 20;
	}

}