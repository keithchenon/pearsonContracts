package com.example.demo;

import java.io.File;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.assertj.core.api.BDDAssertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DemoApplicationTests {

	@ClassRule
	public static DockerComposeContainer environment =
			new DockerComposeContainer(new File("src/test/resources/docker/docker-compose.yml"))
					.withExposedService("zookeeper_1", 2181, Wait
							.forListeningPort());

	@Test
	public void should_work_with_stub_runner_boot() {
		//when
		ConfigurableApplicationContext context = new SpringApplicationBuilder(
				DemoApplication.class).run();
		int port = context.getBean(Environment.class).getProperty("local.server.port",
				Integer.class);
		DiscoveryClient discoveryClient = context.getBean(DiscoveryClient.class);

		//then
		System.out.println(new RestTemplate()
				.getForObject("http://localhost:" + port + "/stubs/", String.class));

		// when
		List<ServiceInstance> instances = discoveryClient.getInstances("producer");

		// then
		BDDAssertions.then(instances).isNotEmpty();
		int stubPort = instances.get(0).getPort();
		// and
		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity("http://localhost:" + stubPort + "/foo", String.class);
		// cleanup
		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
		CuratorFramework curatorFramework = context.getBean(CuratorFramework.class);
		curatorFramework.close();
	}

}
