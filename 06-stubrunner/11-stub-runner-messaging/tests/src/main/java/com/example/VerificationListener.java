package com.example;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

/**
 * @author Marcin Grzejszczak
 */
@Component
class VerificationListener {

	private static final Log log = LogFactory.getLog(VerificationListener.class);

	AtomicInteger eligibleCounter = new AtomicInteger();
	AtomicInteger notEligibleCounter = new AtomicInteger();

	@StreamListener(Sink.INPUT)
	public void listen(Verification verification) {
		log.info("Received new verification");
		if (verification.eligible) {
			this.eligibleCounter.incrementAndGet();
		} else {
			this.notEligibleCounter.incrementAndGet();
		}
	}

	public static class Verification {
		public boolean eligible;
	}
}
