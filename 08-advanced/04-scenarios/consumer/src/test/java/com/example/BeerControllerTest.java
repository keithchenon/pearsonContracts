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
		ids = "com.example:08-04-producer")
public class BeerControllerTest {

	@StubRunnerPort("08-04-producer") int producerPort;

	@Test public void should_be_drunk() {
		Response response = new RestTemplate()
				.postForObject("http://localhost:" + this.producerPort + "/beer",
						new Person("marcin"), Response.class);

		BDDAssertions.then(response.previousStatus).isEqualTo("SOBER");
		BDDAssertions.then(response.currentStatus).isEqualTo("TIPSY");

		response = new RestTemplate()
				.postForObject("http://localhost:" + this.producerPort + "/beer",
						new Person("marcin"), Response.class);

		BDDAssertions.then(response.previousStatus).isEqualTo("TIPSY");
		BDDAssertions.then(response.currentStatus).isEqualTo("DRUNK");

		response = new RestTemplate()
				.postForObject("http://localhost:" + this.producerPort + "/beer",
						new Person("marcin"), Response.class);

		BDDAssertions.then(response.previousStatus).isEqualTo("DRUNK");
		BDDAssertions.then(response.currentStatus).isEqualTo("WASTED");

		response = new RestTemplate()
				.postForObject("http://localhost:" + this.producerPort + "/beer",
						new Person("marcin"), Response.class);

		BDDAssertions.then(response.previousStatus).isEqualTo("DRUNK");
		BDDAssertions.then(response.currentStatus).isEqualTo("WASTED");
	}
}


class Person {
	public String name;

	public Person(String name) {
		this.name = name;
	}

	public Person() {
	}
}

class Response {
	public String previousStatus;
	public String currentStatus;
}
