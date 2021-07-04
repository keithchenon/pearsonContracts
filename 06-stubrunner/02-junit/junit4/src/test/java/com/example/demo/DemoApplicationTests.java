package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.BDDAssertions;
import org.junit.ClassRule;
import org.junit.Test;

import org.springframework.cloud.contract.stubrunner.RunningStubs;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.web.client.RestTemplate;

public class DemoApplicationTests {

	@ClassRule
	public static StubRunnerRule rule = new StubRunnerRule()
			//.repoRoot(repoRoot())
			.stubsMode(StubRunnerProperties.StubsMode.LOCAL)
			.downloadStub("com.example", "05-04-restdocs-from-dsl", "+", "stubs")
				.withPort(9876)
			.downloadStub("com.example:05-03-dsl")
			.downloadLatestStub("com.example", "05-02-provider", "stubs")
			// messaging
			.downloadStub("com.example:producer")
			.minPort(8000)
			.maxPort(8050)
			// pass your own options
			// .options()
			.withMappingsOutputFolder("target/output")
			// deprecated
			// .withSnapshotCheckSkip(true)
			// pass your own message verifier
			.withConsumerName("foo")
			.withStubPerConsumer(false)
			.withProperties(new HashMap<String, String>() {
				{
					this.put("property", "value");
				}
			})
			.withDeleteStubsAfterTest(true)
			.messageVerifier(new MyMessageVerifier())
			// labels
			// .labels()
			// trigger messaging
			// .trigger()
			;

	@Test
	public void should_start_stubs_from_junit_rule() {
		RunningStubs runningStubs = rule.findAllRunningStubs();

		BDDAssertions.then(runningStubs.getAllServicesNames()).contains("05-04-restdocs-from-dsl");
		BDDAssertions.then(runningStubs.getAllServices()).hasSize(4);
		BDDAssertions.then(runningStubs.getPort("05-04-restdocs-from-dsl")).isEqualTo(9876);
		BDDAssertions.then(runningStubs.getPort("05-03-dsl")).isBetween(8000, 8050);
		BDDAssertions.then(runningStubs.isPresent("05-02-provider")).isTrue();
		BDDAssertions.then(runningStubs.isPresent("I'm not here")).isFalse();

		new RestTemplate().getForObject("http://localhost:" + 9876 + "/foo", String.class);
	}

	@Test
	public void should_work_with_messaging() {
		rule.trigger("accepted_verification");

		BDDAssertions.then(MyMessageVerifier.payload).isNotNull();
	}

}


class MyMessageVerifier implements MessageVerifier {

	static Object payload;

	@Override
	public void send(Object message, String destination) {
		System.out.println("SEND MESSAGE");
		MyMessageVerifier.payload = message;
	}

	@Override
	public void send(Object payload, Map headers, String destination) {
		System.out.println("SEND PAYLOAD");
		MyMessageVerifier.payload = payload;
	}

	@Override
	public Object receive(String destination, long timeout, TimeUnit timeUnit) {
		return receive(destination);
	}

	@Override
	public Object receive(String destination) {
		System.out.println("GET PAYLOAD");
		return payload;
	}
}