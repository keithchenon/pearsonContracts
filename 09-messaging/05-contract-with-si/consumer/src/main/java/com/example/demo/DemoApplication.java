package com.example.demo;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@Configuration
@EnableIntegration
class AppConfiguration {

	static final String INPUT_DESTINATION = "verifications";

	@Bean
	public IntegrationFlow personFlow() {
		return f ->
				f.channel(INPUT_DESTINATION)
				.transform(Transformers.fromJson(Verification.class))
				.handle(verificationHandler());
	}

	@Bean
	VerificationHandler verificationHandler() {
		return new VerificationHandler();
	}
}

class VerificationHandler {

	public Verification verification;

	@ServiceActivator
	public void handle(Verification verification) {
		this.verification = verification;
		System.out.println("Got verification! [" + verification + "]");
	}
}

@Configuration
class ListenerConfiguration {

	@Bean
	public IntegrationFlow listenerFlow(ConnectionFactory connectionFactory) {
		return IntegrationFlows
				.from(Amqp.inboundAdapter(connectionFactory, AppConfiguration.INPUT_DESTINATION))
				.channel(AppConfiguration.INPUT_DESTINATION)
				.get();
	}
}

class Person {
	public Integer age;

	public Person(Integer age) {
		this.age = age;
	}

	public Person() {
	}
}

class Verification {
	public boolean eligible;

	public Verification(boolean eligible) {
		this.eligible = eligible;
	}

	public Verification() {
	}

	@Override
	public String toString() {
		return "Verification{" +
				"eligible=" + this.eligible +
				'}';
	}
}