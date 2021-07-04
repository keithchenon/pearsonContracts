package com.example.demo;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWireMock(port = 0,
		// subfolder must be __files
		files = "file:src/test/resources/custom/",
		stubs = "file:src/test/resources/custom/mappings/")
public class DemoApplication2Tests {

	@Value("${wiremock.server.port}") int port;

	@Test
	public void should_fetch_stubs_from_custom_location() {
		String object = new RestTemplate()
				.getForObject("http://localhost:" + this.port + "/custom/resource", String.class);

		BDDAssertions.then(object).isEqualTo("Hello Custom World");
	}

	@Test
	public void should_fetch_stubs_from_resource_that_reads_body_from_custom_location() {
		String object = new RestTemplate()
				.getForObject("http://localhost:" + this.port + "/hello2", String.class);

		BDDAssertions.then(object).isEqualTo("{\"message\":\"Hello World 2\"}");
	}
}
