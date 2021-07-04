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
@AutoConfigureStubRunner(
		stubsMode = StubRunnerProperties.StubsMode.REMOTE,
		ids = "com.example:11-03-producer",
		repositoryRoot = "stubs://${PRODUCER_ROOT}/target/stubs/")
public class BeerControllerTest {

	@StubRunnerPort("11-03-producer") int producerPort;

	@Test public void should_give_me_a_beer_when_im_old_enough() {
		Response response = new RestTemplate()
				.postForObject("http://localhost:" + this.producerPort + "/check",
						new Person("marcin", 22), Response.class);

		BDDAssertions.then(response.status).isEqualTo("OK");
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
