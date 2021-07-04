package com.example.demo;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =
				SpringApplication.run(DemoApplication.class, args);
		ProducerTemplate producerTemplate = ctx.getBean(ProducerTemplate.class);
		producerTemplate.sendBody("direct:start", new Person(17));
		producerTemplate.sendBody("direct:start", new Person(30));
		ctx.close();
	}

}

@Configuration
class RouteConfiguration {

	@Bean RoutesBuilder myRouter() {
		return new SpringRouteBuilder() {
			@Override
			public void configure() {
				from("direct:start")
						.process(exchange -> {
							Person person = (Person) exchange.getMessage().getBody();
							exchange.getOut().setBody(new Verification(person.age >= 20));
						})
						.process(exchange -> System.out.println(exchange.getIn().getBody()));
			}

		};
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