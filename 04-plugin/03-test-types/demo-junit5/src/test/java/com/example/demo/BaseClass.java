package com.example.demo;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeAll;

/**
 * @author Marcin Grzejszczak
 * @since
 */
public class BaseClass {

	@BeforeAll
	public static void setup() {
		RestAssuredMockMvc.standaloneSetup(new ProducerController(personToCheck -> personToCheck.age >= 20));
	}
}
