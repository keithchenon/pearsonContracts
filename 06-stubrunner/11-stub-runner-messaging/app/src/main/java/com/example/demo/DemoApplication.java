package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.contract.stubrunner.server.EnableStubRunnerServer;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableStubRunnerServer
// for Spring Cloud Stream autoconfiguration to jump in
@EnableBinding
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@AutoConfigureStubRunner
	static class Config {}
}
