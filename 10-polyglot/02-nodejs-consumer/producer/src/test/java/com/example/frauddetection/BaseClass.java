package com.example.frauddetection;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by mgrzejszczak.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FraudDetectionApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BaseClass {

	// For messaging
	@Autowired FraudController controller;

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.controller);
	}

}
