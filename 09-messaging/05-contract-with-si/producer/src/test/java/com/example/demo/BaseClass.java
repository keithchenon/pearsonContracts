package com.example.demo;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseClass.TestingConfiguration.class)
@AutoConfigureMessageVerifier
public class BaseClass {

	@Autowired PersonConverter personConverter;

	public void positive() {
		this.personConverter.verifyPerson(new Person(22));
	}

	public void negative() {
		this.personConverter.verifyPerson(new Person(17));
	}

	@Configuration
	@EnableAutoConfiguration
	static class TestingConfiguration extends AppConfiguration {

		// was:      MessagingGateway -> Person -> Verification -> RabbitMQ
		// will be : MessagingGateway -> Person -> Verification -> Queue
		@Bean(AppConfiguration.OUTPUT_DESTINATION)
		MessageChannel outputChannel() {
			return new QueueChannel();
		}
	}
}