package com.example.demo;

import java.net.URL;

import org.assertj.core.api.BDDAssertions;
import org.junit.ClassRule;
import org.junit.Test;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;

public class DemoApplicationWithRuleTests {

	@ClassRule public static StubRunnerRule rule =
			new StubRunnerRule()
					.downloadStub("com.example:05-04-restdocs-from-dsl")
					.stubsMode(StubRunnerProperties.StubsMode.LOCAL)
					.withDeleteStubsAfterTest(false)
					.withMappingsOutputFolder("target/output_rule");

	@Test
	public void should_inject_port_via_stub_finder() {
		URL url = rule.findStubUrl("05-04-restdocs-from-dsl");

		ResponseEntity<String> entity = new TestRestTemplate()
				.getForEntity(url.toString() + "/baz", String.class);

		System.out.println("REPORT");
		System.out.println(entity.getBody());

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(404);
	}

}

