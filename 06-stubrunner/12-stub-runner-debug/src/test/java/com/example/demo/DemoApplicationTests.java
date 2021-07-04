package com.example.demo;

import java.net.URL;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
		// defaults
		ids = "com.example:05-04-restdocs-from-dsl",
		stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		mappingsOutputFolder = "target/output_annotation/",
		deleteStubsAfterTest = false
)
public class DemoApplicationTests {

	@Autowired StubFinder stubFinder;
	@StubRunnerPort("05-04-restdocs-from-dsl") int port;

	@Test
	public void should_inject_port_via_stub_finder() {
		URL url = this.stubFinder.findStubUrl("05-04-restdocs-from-dsl");

		ResponseEntity<String> entity = new TestRestTemplate()
				.getForEntity(url.toString() + "/baz", String.class);

		System.out.println("REPORT");
		System.out.println(entity.getBody());

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(404);
	}

}

