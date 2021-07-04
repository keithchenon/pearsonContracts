package com.example.demo;

import java.io.File;

import org.assertj.core.api.BDDAssertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Show Stub Runner Boot code
 * run it via JAR
 * run it via CLI
 * run it via Docker
 */
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
		System.out.println(new RestTemplate()
				.getForObject("http://localhost:8083/stubs/", String.class));

		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity("http://localhost:9876/foo", String.class);

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
	}

}
