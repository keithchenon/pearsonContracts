package com.example.demo;

import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.springframework.util.SocketUtils;

/**
 * @author Marcin Grzejszczak
 * @since
 */
public class BaseClass {

	Javalin app;

	@Before
	public void setup() {
		int port = SocketUtils.findAvailableTcpPort();
		this.app = start(port);
		RestAssured.baseURI = "http://localhost:" + port;
	}

	@After
	public void close() {
		this.app.stop();
	}

	private Javalin start(int port) {
		return new DemoApplication().run(port);
	}
}
