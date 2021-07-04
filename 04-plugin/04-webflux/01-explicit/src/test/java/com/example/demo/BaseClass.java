package com.example.demo;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Marcin Grzejszczak
 * @since
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseClass {

	@LocalServerPort int port;

	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost:" + this.port;
	}
}
