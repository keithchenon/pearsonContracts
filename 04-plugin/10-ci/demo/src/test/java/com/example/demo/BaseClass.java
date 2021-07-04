package com.example.demo;

import io.restassured.RestAssured;
import org.junit.Before;

/**
 * @author Marcin Grzejszczak
 * @since
 */
public class BaseClass {

	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost:8080";
	}
}
