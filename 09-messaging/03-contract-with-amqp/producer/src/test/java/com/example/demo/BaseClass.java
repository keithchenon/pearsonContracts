package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
// IMPORTANT!
@AutoConfigureMessageVerifier
public class BaseClass {

	@Autowired RabbitTemplate rabbitTemplate;
	ObjectMapper objectMapper = new ObjectMapper();

	public void positive() throws Exception {
		this.rabbitTemplate.send("verifications",
				"verifications", message(true));
	}

	private Message message(boolean eligible) throws JsonProcessingException {
		MessageProperties properties = new MessageProperties();
		properties.setContentType("application/json");
		return new Message(this.objectMapper.writeValueAsBytes(new Verification(eligible)),
				properties);
	}

	public void negative() throws Exception {
		this.rabbitTemplate.send("verifications",
				"verifications", message(false));
	}
}

