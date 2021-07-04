package com.example.demo;

import java.net.URL;

import org.assertj.core.api.BDDAssertions;
import org.junit.ClassRule;
import org.junit.Test;

import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DemoApplicationWithRuleTests {

	@ClassRule public static StubRunnerRule rule =
			new StubRunnerRule()
					.downloadStub("com.example:05-04-restdocs-from-dsl")
					.stubsMode(StubRunnerProperties.StubsMode.LOCAL);

	@Test
	public void should_inject_port_via_stub_finder() {
		URL url = rule.findStubUrl("05-04-restdocs-from-dsl");

		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity(url.toString() + "/foo", String.class);

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
	}

}

