package com.example.demo;

import java.io.File;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
		// you have to pass the version explicitly
		ids = "com.example:05-04-restdocs-from-dsl:0.0.1-SNAPSHOT:stubs:9876",
		stubsMode = StubRunnerProperties.StubsMode.REMOTE,
		// git:// at the beginning
		repositoryRoot = "git://${ROOT}/target/gitrepo/"
)
public class DemoApplicationTests {

	static {
		if (System.getProperty("ROOT") == null) {
			System.setProperty("ROOT", new File("../git-repo").getAbsolutePath());
		}
	}

	// Show logs
	@Test
	public void should_start_stubs_from_git() {
		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity("http://localhost:9876/foo", String.class);

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
	}

}

