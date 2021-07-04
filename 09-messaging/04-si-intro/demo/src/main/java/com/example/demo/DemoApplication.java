package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;

@SpringBootApplication
@EnableIntegration
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =
				SpringApplication.run(DemoApplication.class, args);
		PersonConverter converter = ctx.getBean(PersonConverter.class);
		System.out.println(converter.verifyPerson(new Person(17)));
		System.out.println(converter.verifyPerson(new Person(30)));
		ctx.close();
	}

	@Bean
	public IntegrationFlow personFlow() {
		return f -> f
				.channel("person")
				.transform(Person.class,
						source -> new Verification(source.age >= 20));
	}

}

@MessagingGateway
interface PersonConverter {

	@Gateway(requestChannel = "person")
	Verification verifyPerson(Person person);

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