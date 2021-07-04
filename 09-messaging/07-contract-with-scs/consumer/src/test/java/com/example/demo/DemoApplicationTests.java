package com.example.demo;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
		ids = "com.example:09-07-producer",
		stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class DemoApplicationTests {

	@Autowired VerificationHandler handler;
	@Autowired StubTrigger stubTrigger;
	@Autowired PersonSender personSender;

	@Before
	public void setup() {
		this.handler.verification = null;
	}

	@Test
	public void should_trigger_a_positive_verification() {
		this.stubTrigger.trigger("positive");

		BDDAssertions.then(this.handler.verification).isNotNull();
		BDDAssertions.then(this.handler.verification.eligible).isTrue();
	}

	@Test
	public void should_trigger_a_negative_verification() {
		this.stubTrigger.trigger("negative");

		BDDAssertions.then(this.handler.verification).isNotNull();
		BDDAssertions.then(this.handler.verification.eligible).isFalse();
	}

	@Test
	public void should_trigger_a_positive_verification_from_a_route() {
		this.personSender.send(25);

		BDDAssertions.then(this.handler.verification).isNotNull();
		BDDAssertions.then(this.handler.verification.eligible).isTrue();
	}

	@Test
	public void should_trigger_a_negative_verification_from_a_route() {
		this.personSender.send(17);

		BDDAssertions.then(this.handler.verification).isNotNull();
		BDDAssertions.then(this.handler.verification.eligible).isFalse();
	}

	@Test
	public void should_fail_trigger_a_verification_from_a_route() {
		this.personSender.send(13);

		BDDAssertions.then(this.handler.verification).isNull();
	}

}
