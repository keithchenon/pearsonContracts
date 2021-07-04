package com.example;

import java.io.File;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.REMOTE,
		ids = "com.example:05-beer-api-producer-external:0.0.1-SNAPSHOT",
		repositoryRoot = "git://${ROOT}/target/gitrepo/")
public class BeerControllerTest {

	static {
		if (System.getProperty("ROOT") == null) {
			System.setProperty("ROOT", new File("../git-repo").getAbsolutePath());
		}
	}

	@StubRunnerPort("04-beer-api-producer-external") int producerPort;

	@Test public void should_give_me_a_beer_when_im_old_enough() {
		Response response = new RestTemplate()
				.postForObject("http://localhost:" + this.producerPort + "/check",
						new Person("marcin", 22), Response.class);

		BDDAssertions.then(response.status).isEqualTo("OK");
	}

	@Test public void should_reject_a_beer_when_im_too_young() {
		Response response = new RestTemplate()
				.postForObject("http://localhost:" + this.producerPort + "/check",
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