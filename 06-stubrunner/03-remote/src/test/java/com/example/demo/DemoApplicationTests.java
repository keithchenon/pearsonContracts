package com.example.demo;

import java.io.File;

import org.assertj.core.api.BDDAssertions;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureStubRunner(
//		ids = "com.example:05-04-restdocs-from-dsl:+:stubs:7654",
//		repositoryRoot = "http://localhost:8081/artifactory/libs-release-local/",
//		stubsMode = StubRunnerProperties.StubsMode.REMOTE
//)
public class DemoApplicationTests {

	@ClassRule
	public static DockerComposeContainer environment =
			new DockerComposeContainer(new File("src/test/resources/docker/docker-compose.yml"))
					.withExposedService("artifactory_1", 8081, Wait
							.forHttp("/artifactory/libs-release-local/"));

	@BeforeClass
	public static void uploadJar() {
		// uploading jars to artifactory before running the tests
		new ProcessExecutor(
				new File("src/test/resources/jars/").getAbsolutePath())
			.runCommand("./upload.sh");
	}

	@Rule
	public StubRunnerRule rule = new StubRunnerRule()
			.repoRoot("http://localhost:8081/artifactory/libs-release-local/")
			.stubsMode(StubRunnerProperties.StubsMode.REMOTE)
			.downloadStub("com.example", "05-04-restdocs-from-dsl", "+", "stubs")
			.withPort(7654);
			// What happened in Finchley vs Greenwich
			// .withSnapshotCheckSkip(true);

	// Show logs for Aether, explain what happened in Finchley vs Greenwich
	@Test
	public void should_work_with_artifactory() {
		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity("http://localhost:7654/foo", String.class);

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
	}

}
