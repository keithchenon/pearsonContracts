package com.example.demo;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
		// defaults
		ids = "com.example:06-08-app",
		stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		stubsPerConsumer = true
		//
		// consumerName = will be taken from spring.application.name
)
public class DemoApplicationTests {

	@StubRunnerPort("06-08-app") int port;

	@Test
	public void should_work_with_stubs_per_consumer() {
		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity("http://localhost:" + this.port + "/foo", String.class);

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
	}

	@Test(expected = HttpClientErrorException.class)
	public void should_fail_with_stubs_per_consumer() {
		new RestTemplate()
				.getForEntity("http://localhost:" + this.port + "/bar", String.class);
	}

}

