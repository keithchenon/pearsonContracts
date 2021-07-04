package com.example.demo;

import java.net.URL;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class DemoApplicationWithRuleTests {

	@ClassRule public static StubRunnerRule rule =
			new StubRunnerRule()
					.downloadStub("com.example:06-08-app")
					.stubsMode(StubRunnerProperties.StubsMode.LOCAL)
					.withConsumerName("bar")
					.withStubPerConsumer(true);

	int port;

	@Before
	public void setup() {
		this.port = rule.findStubUrl("06-08-app").getPort();
	}

	@Test
	public void should_work_with_stubs_per_consumer() {
		URL url = rule.findStubUrl("06-08-app");

		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity(url.toString() + "/bar", String.class);

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
	}

	@Test(expected = HttpClientErrorException.class)
	public void should_fail_with_stubs_per_consumer() {
		new RestTemplate()
				.getForEntity("http://localhost:" + this.port + "/foo", String.class);
	}

}

