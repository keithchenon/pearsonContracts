package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableBinding(Processor.class)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@Component
class MyProcessor {
	private final Processor processor;

	MyProcessor(Processor processor) {
		this.processor = processor;
	}

	@StreamListener(Processor.INPUT)
	void send(Person person) {
		System.out.println("Got person [" + person + "]");
		this.processor.output()
				.send(MessageBuilder.withPayload(new Verification(person.age >= 20)).build());
	}
}

class Person {
	public Integer age;

	public Person(Integer age) {
		this.age = age;
	}

	public Person() {
	}

	@Override
	public String toString() {
		return "Person{" +
				"age=" + this.age +
				'}';
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
