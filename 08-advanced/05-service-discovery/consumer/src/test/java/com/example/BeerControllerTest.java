package com.example;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		ids = "com.example:08-05-producer")
public class BeerControllerTest {

	@Autowired RestTemplate restTemplate;

	@Test public void should_find_the_registered_service_via_artifactid() {
		Response response = this.restTemplate
				.postForObject("http://08-05-producer/check",
						new Person("marcin", 22), Response.class);

		BDDAssertions.then(response.status).isEqualTo("OK");
	}

	@Test public void should_give_me_a_beer_when_im_old_enough() {
		Response response = this.restTemplate
				.postForObject("http://my-loadbalanced-producer/check",
						new Person("marcin", 22), Response.class);

		BDDAssertions.then(response.status).isEqualTo("OK");
	}

	@Test public void should_reject_a_beer_when_im_too_young() {
		Response response = this.restTemplate
				.postForObject("http://my-loadbalanced-producer/check",
						new Person("marcin", 17), Response.class);

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

class Response {
	public String status;
}
