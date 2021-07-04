package com.example.demo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean Exchange myExchange() {
		return new TopicExchange("verifications");
	}

	@Bean Queue myQueue() {
		return new Queue("verifications");
	}

	@Bean Binding myBinding() {
		return BindingBuilder.bind(myQueue()).to(myExchange())
				.with("verifications").noargs();
	}
}