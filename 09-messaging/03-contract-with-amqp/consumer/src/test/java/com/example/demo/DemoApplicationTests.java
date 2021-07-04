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
		ids = "com.example:09-03-producer",
		stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class DemoApplicationTests {

	@Autowired MyListener myListener;
	@Autowired StubTrigger stubTrigger;

	@Before
	public void setup() {
		this.myListener.verification = null;
	}

	@Test
	public void should_trigger_positive() {
		this.stubTrigger.trigger("positive");

		BDDAssertions.then(this.myListener.verification).isNotNull();
		BDDAssertions.then(this.myListener.verification.eligible).isEqualTo(true);
	}

	@Test
	public void should_trigger_negative() {
		this.stubTrigger.trigger("negative");

		BDDAssertions.then(this.myListener.verification).isNotNull();
		BDDAssertions.then(this.myListener.verification.eligible).isEqualTo(false);
	}

}

