package com.example;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//remove::start[]
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
//remove::end[]
import org.springframework.test.context.junit4.SpringRunner;

//remove::start[]
import javax.inject.Inject;
//remove::end[]
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
public abstract class BeerMessagingBase {
	@Inject MessageVerifier messaging;
	@Autowired PersonCheckingService personCheckingService;

	@Before
	public void setup() {
		this.messaging.receive("output", 100, TimeUnit.MILLISECONDS);
	}

	public void clientIsOldEnough() {
		this.personCheckingService.shouldGetBeer(new PersonToCheck(25));
	}

	public void clientIsTooYoung() {
		this.personCheckingService.shouldGetBeer(new PersonToCheck(5));
	}
}
