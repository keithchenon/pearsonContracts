package com.example.demo;

import java.io.File;
import java.io.IOException;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureJsonTesters
public class DemoApplicationTests {

	@ClassRule
	public static DockerComposeContainer environment =
			new DockerComposeContainer(new File("src/test/resources/docker/docker-compose.yml"))
					.withExposedService("rabbitmq_1", 5672, Wait.forListeningPort());

	@Autowired AmqpTemplate amqpTemplate;
	JacksonTester<Verification> jacksonTester;

	@Before
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	public void should_send_and_receive() throws IOException {
		Verification verification = new Verification(true);

		this.amqpTemplate.send("verifications",
				MessageBuilder.withBody(
						this.jacksonTester.write(verification).getJson().getBytes()).build());

		Verification received = this.jacksonTester.parseObject(
				this.amqpTemplate.receive("verifications").getBody());
		BDDAssertions.then(received.eligible).isEqualTo(true);
	}

}

