package com.example.demo;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
		ids = "com.example:09-05-producer",
		stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class DemoApplicationTests {

	@Autowired VerificationHandler handler;
	@Autowired StubTrigger stubTrigger;

	@Before
	public void setup() {
		this.handler.verification = null;
	}

	@Test
	public void should_trigger_a_positive_verifcation() {
		this.stubTrigger.trigger("positive");

		BDDAssertions.then(this.handler.verification).isNotNull();
		BDDAssertions.then(this.handler.verification.eligible).isTrue();
	}

	@Test
	public void should_trigger_a_negative_verifcation() {
		this.stubTrigger.trigger("negative");

		BDDAssertions.then(this.handler.verification).isNotNull();
		BDDAssertions.then(this.handler.verification.eligible).isFalse();
	}


	@Configuration
	@EnableAutoConfiguration
	static class Config extends AppConfiguration {

	}
}
