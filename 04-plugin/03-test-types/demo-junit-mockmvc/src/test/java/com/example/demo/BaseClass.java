package com.example.demo;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.BeforeClass;

/**
 * @author Marcin Grzejszczak
 * @since
 */
public class BaseClass {

	@BeforeClass
	public static void setup() {
		RestAssuredMockMvc.standaloneSetup(new ProducerController(personToCheck -> personToCheck.age >= 20));
	}
}
