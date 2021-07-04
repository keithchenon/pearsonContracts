package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableBinding({Sink.class, Source.class })
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@Component
class VerificationHandler {

	Verification verification;

	@StreamListener(Sink.INPUT)
	void receive(Verification verification) {
		System.out.println("Got verification [" + verification + "]");
		this.verification = verification;
	}
}

@Component
class PersonSender {

	private final Source source;

	PersonSender(Source source) {
		this.source = source;
	}

	void send(int age) {
		this.source.output()
				.send(MessageBuilder.withPayload(new Person(age)).build());
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
