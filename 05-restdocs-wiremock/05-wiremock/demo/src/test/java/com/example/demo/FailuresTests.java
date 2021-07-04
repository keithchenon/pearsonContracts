package com.example.demo;

import java.io.IOException;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWireMock(port = 8082)
public class FailuresTests {

	RestTemplate restTemplate;

	@Before
	public void setup() {
		SimpleClientHttpRequestFactory simple = new SimpleClientHttpRequestFactory();
		simple.setConnectTimeout(1000);
		simple.setReadTimeout(1000);
		this.restTemplate = new RestTemplate(simple);
	}

	@Test
	public void should_fail_with_timeout() {
		WireMock.stubFor(WireMock.get("/fail")
				.willReturn(WireMock.aResponse().withFixedDelay(5000)));

		BDDAssertions.thenThrownBy(() ->
				this.restTemplate
						.getForObject("http://localhost:8082/fail", String.class))
			.hasRootCauseInstanceOf(IOException.class);
	}

	@Test
	public void should_fail_with_connection_reset_by_peer() {
		WireMock.stubFor(WireMock.get("/fail2")
				.willReturn(WireMock.aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

		BDDAssertions.thenThrownBy(() ->
				this.restTemplate
						.getForObject("http://localhost:8082/fail2", String.class))
			.hasRootCauseInstanceOf(IOException.class);
	}

	@Test
	public void should_fail_with_empty_response() {
		WireMock.stubFor(WireMock.get("/fail3")
				.willReturn(WireMock.aResponse().withFault(Fault.EMPTY_RESPONSE)));

		BDDAssertions.thenThrownBy(() ->
				this.restTemplate
						.getForObject("http://localhost:8082/fail3", String.class))
				.hasRootCauseInstanceOf(IOException.class);
	}

	@Test
	public void should_fail_with_malformed() {
		WireMock.stubFor(WireMock.get("/fail4")
				.willReturn(WireMock.aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

		BDDAssertions.thenThrownBy(() ->
				this.restTemplate
						.getForObject("http://localhost:8082/fail4", String.class))
				.hasRootCauseInstanceOf(IOException.class);
	}

	@Test
	public void should_fail_with_random() {
		WireMock.stubFor(WireMock.get("/fail5")
				.willReturn(WireMock.aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)));

		BDDAssertions.thenThrownBy(() ->
				this.restTemplate
						.getForObject("http://localhost:8082/fail5", String.class))
				.hasRootCauseInstanceOf(IOException.class);
	}
}
