package com.example.demo;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.Before;

/**
 * @author Marcin Grzejszczak
 * @since
 */
public abstract class BaseClass {
	@Before
	public void setup() {
		RestAssuredWebTestClient.standaloneSetup(new ProducerController(personToCheck -> personToCheck.age >= 20));
	}
}
