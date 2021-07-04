package com.example;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

public abstract class BeerRestBase {

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(
				new ProducerController(argument -> argument.age >= 20)
		);
	}

}
