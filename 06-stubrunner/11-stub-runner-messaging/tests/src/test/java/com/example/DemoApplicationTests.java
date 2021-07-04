package com.example;

import java.io.File;

import org.assertj.core.api.BDDAssertions;
import org.awaitility.Awaitility;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

public class DemoApplicationTests {

	@BeforeClass
	public static void setup() {
		new ProcessExecutor(
				new File("src/test/resources/docker/").getAbsolutePath())
				.runCommand("./start.sh");
	}

	@AfterClass
	public static void tearDown() {
		new ProcessExecutor(
				new File("src/test/resources/docker/").getAbsolutePath())
				.runCommand("./stop.sh");
	}

	@Test
	public void should_work_with_stub_runner_boot() {
		//when
		ConfigurableApplicationContext context = new SpringApplicationBuilder(
				ClientApplication.class).run();
		int port = 7654;
		VerificationListener listener = context.getBean(VerificationListener.class);

		//then
		System.out.println(new RestTemplate()
				.getForObject("http://localhost:" + port + "/stubs/", String.class));

		// when
		new RestTemplate()
				.postForObject("http://localhost:" + port + "/triggers/accepted_verification", "", String.class);

		// then
		Awaitility.await().untilAsserted(() ->
				BDDAssertions.then(listener.eligibleCounter.get()).isGreaterThan(0));

	}

}