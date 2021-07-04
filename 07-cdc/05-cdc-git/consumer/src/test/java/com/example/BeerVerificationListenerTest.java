package com.example;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.REMOTE,
		ids = "com.example:05-beer-api-producer-external:0.0.1-SNAPSHOT",
		repositoryRoot = "git://${ROOT}/target/gitrepo/")
public class BeerVerificationListenerTest {

	static {
		if (System.getProperty("ROOT") == null) {
			System.setProperty("ROOT", new File("../git-repo").getAbsolutePath());
		}
	}

	@Autowired StubTrigger stubTrigger;
	@Autowired BeerVerificationListener listener;

	@Test public void should_increase_the_eligible_counter_when_verification_was_accepted() throws Exception {
		int initialCounter = this.listener.eligibleCounter.get();

		this.stubTrigger.trigger("accepted_verification");

		then(this.listener.eligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@Test public void should_increase_the_noteligible_counter_when_verification_was_rejected() throws Exception {
		int initialCounter = this.listener.notEligibleCounter.get();

		this.stubTrigger.trigger("rejected_verification");

		then(this.listener.notEligibleCounter.get()).isGreaterThan(initialCounter);
	}
}
