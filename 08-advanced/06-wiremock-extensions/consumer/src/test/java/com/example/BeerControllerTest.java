package com.example;

import java.util.Collections;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		ids = "com.example:08-06-producer")
public class BeerControllerTest {

	RestTemplate restTemplate = new RestTemplate();

	@StubRunnerPort("08-06-producer") int port;

	@Test public void should_give_me_a_beer_when_im_old_enough() {
		ResponseEntity<Response> response = this.restTemplate
				.postForEntity("http://localhost:" + this.port + "/check",
						new Person("marcin", 22), Response.class);

		BDDAssertions.then(response.getHeaders()).containsEntry("HACKED", Collections
				.singletonList("TRANSFORMER"));
		BDDAssertions.then(response.getBody().status).isEqualTo("OK");
	}

	@Test public void should_reject_a_beer_when_im_too_young() {
		ResponseEntity<Response> response = this.restTemplate
				.postForEntity("http://localhost:" + this.port + "/check",
						new Person("marcin", 17), Response.class);

		BDDAssertions.then(response.getHeaders()).containsEntry("HACKED", Collections
				.singletonList("TRANSFORMER"));
		BDDAssertions.then(response.getBody().status).isEqualTo("NOT_OK");
	}
}


class Person {
	public String name;
	public int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public Person() {
	}
}

class Response {
	public String status;
}
