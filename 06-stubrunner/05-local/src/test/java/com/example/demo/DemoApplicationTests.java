package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.assertj.core.api.BDDAssertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.RunningStubs;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.verifier.messaging.noop.NoOpStubMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
		ids = "com.example:05-04-restdocs-from-dsl:+:stubs:9876",
		stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class DemoApplicationTests {

	// Show logs for Aether
	@Test
	public void should_start_stubs_from_local_storage() {
		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity("http://localhost:9876/foo", String.class);

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
	}

}

