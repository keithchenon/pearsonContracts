package com.example;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		ids = "com.example:08-03-producer")
public class BeerControllerTest {

	@StubRunnerPort("08-03-producer") int producerPort;

	@Test public void should_give_me_a_beer_when_im_old_enough() {
		Response response = new RestTemplate()
				.postForObject("http://localhost:" + this.producerPort + "/check",
						new Person("marcin", 22), Response.class);

		BDDAssertions.then(response.status).isEqualTo("OK");
	}

	@Test public void should_reject_a_beer_for_nameless() {
		Response response = new RestTemplate()
				.postForObject("http://localhost:" + this.producerPort + "/check",
						new NamelessPerson(22), Response.class);

		BDDAssertions.then(response.status).isEqualTo("NOT_OK");
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


class NamelessPerson {
	public int age;

	public NamelessPerson(int age) {
		this.age = age;
	}

	public NamelessPerson() {
	}
}

class Response {
	public String status;
}
