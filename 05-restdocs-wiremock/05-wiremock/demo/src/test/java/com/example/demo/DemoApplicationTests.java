package com.example.demo;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.assertj.core.api.BDDAssertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.contract.wiremock.WireMockRestServiceServer;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWireMock(port = 8081)
public class DemoApplicationTests {

	// ConnectionRefused
	@Test(expected = ResourceAccessException.class)
	public void should_return_ok() {
		String object = new RestTemplate()
				.getForObject("http://localhost:9876/ok", String.class);

		BDDAssertions.then(object).isEqualTo("ok");
	}

	// 404
	@Test(expected = HttpClientErrorException.class)
	public void should_return_ok_from_wiremock() {
		String object = new RestTemplate()
				.getForObject("http://localhost:8081/ok", String.class);

		BDDAssertions.then(object).isEqualTo("ok");
	}

	/*

	2018-11-01 20:40:16.002 ERROR 87047 --- [qtp623330465-24] WireMock                                 :
                                               Request was not matched
                                               =======================

-----------------------------------------------------------------------------------------------------------------------
| Closest stub                                             | Request                                                  |
-----------------------------------------------------------------------------------------------------------------------
                                                           |
GET                                                        | GET
/foo                                                       | /ok                                                 <<<<< URL does not match
                                                           |
                                                           |
-----------------------------------------------------------------------------------------------------------------------

	 */

	@Test
	public void should_return_ok_from_wiremock_with_stub() {
		WireMock.stubFor(WireMock.get("/foo")
				.willReturn(WireMock.aResponse().withBody("bar")));
		String object = new RestTemplate()
				.getForObject("http://localhost:8081/foo", String.class);

		BDDAssertions.then(object).isEqualTo("bar");
	}

	@Value("${wiremock.server.port}") int port;

	@Test
	public void should_return_server_port() {
		BDDAssertions.then(this.port).isEqualTo(8081);
	}

	@Test
	public void should_return_hello_world_from_mappings_from_wiremock_with_stub() {
		String object = new RestTemplate()
				.getForObject("http://localhost:8081/resource", String.class);

		BDDAssertions.then(object).isEqualTo("Hello World");
	}

	@ClassRule
	public static WireMockClassRule wiremock = new WireMockClassRule(
			WireMockSpring.options().port(5435));

	@Test
	public void should_return_hello_world_from_mappings_from_wiremock_with_stub_from_class_rule() {
		String object = new RestTemplate()
				.getForObject("http://localhost:5435/resource", String.class);

		BDDAssertions.then(object).isEqualTo("Hello World");
	}

	@Test
	public void should_work_with_mock_rest_service() {
		RestTemplate restTemplate = new RestTemplate();
		MockRestServiceServer server = WireMockRestServiceServer
				.with(restTemplate)
				.baseUrl("http://example.org")
				.stubs("classpath:/mappings/resource.json")
				.build();
		BDDAssertions.then(
				restTemplate.getForObject("http://example.org/resource",
				String.class)).isEqualTo("Hello World");
		server.verify();
	}
}
