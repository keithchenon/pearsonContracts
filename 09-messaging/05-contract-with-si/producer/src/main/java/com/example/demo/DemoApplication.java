package com.example.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =
				SpringApplication.run(DemoApplication.class, args);
		PersonConverter converter = ctx.getBean(PersonConverter.class);
		converter.verifyPerson(new Person(17));
		converter.verifyPerson(new Person(30));
		ctx.close();
	}
}

@Configuration
@EnableIntegration
class AppConfiguration {

	static final String OUTPUT_DESTINATION = "verifications";

	@Bean
	public IntegrationFlow personFlow() {
		return f ->
				f.channel("person")
				.transform(Person.class, source -> new Verification(source.age >= 20))
				.transform(Transformers.toJson())
				.channel(OUTPUT_DESTINATION);
	}
}

@Configuration
class SenderConfiguration {

	@Bean
	RabbitSender sender(AmqpTemplate amqpTemplate) {
		return new RabbitSender(amqpTemplate);
	}

	@Bean
	Queue myQueue() {
		return new Queue(AppConfiguration.OUTPUT_DESTINATION);
	}

}

@MessagingGateway
interface PersonConverter {

	@Gateway(requestChannel = "person")
	void verifyPerson(Person person);

}

@Component
class RabbitSender {

	private final AmqpTemplate amqpTemplate;

	RabbitSender(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}

	@ServiceActivator(inputChannel = AppConfiguration.OUTPUT_DESTINATION)
	public void send(String verification) {
		this.amqpTemplate.send(AppConfiguration.OUTPUT_DESTINATION,
				MessageBuilder.withBody(verification.getBytes()).build());
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
}