package com.example.demo;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Marcin Grzejszczak
 * @since
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public abstract class BaseClass {

	@Autowired WebApplicationContext context;

	@Before
	public void setup() {
		RestAssuredMockMvc.webAppContextSetup(this.context);
	}
}
