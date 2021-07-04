package com.example.demo;

import java.net.URL;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
		// random-port
		ids = "com.example:05-04-restdocs-from-dsl",
		stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class DemoApplicationTests {

	@Autowired StubFinder stubFinder;

	@Test
	public void should_inject_port_via_stub_finder() {
		URL url = this.stubFinder.findStubUrl("05-04-restdocs-from-dsl");

		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity(url.toString() + "/foo", String.class);

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
	}

	@StubRunnerPort("05-04-restdocs-from-dsl") int port;

	@Test
	public void should_inject_port_via_annotation() {
		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity("http://localhost:" + this.port + "/foo", String.class);

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
	}

}

