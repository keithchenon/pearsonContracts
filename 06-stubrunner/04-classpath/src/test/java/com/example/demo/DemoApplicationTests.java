package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.assertj.core.api.BDDAssertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.RunningStubs;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.verifier.messaging.noop.NoOpStubMessages;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
		ids = "sagan:sagan-site:+:stubs:9876",
		stubsMode = StubRunnerProperties.StubsMode.CLASSPATH
)
public class DemoApplicationTests {

	@Test
	public void should_start_stubs_from_classpath() {
		Project project = new RestTemplate()
				.getForObject("http://localhost:9876/project_metadata/{projectName}", Project.class, "spring-framework");

		BDDAssertions.then(project).isNotNull();
		BDDAssertions.then(project.name).containsIgnoringCase("spring");
	}

}

class Project {
	public String id = "";
	public String name = "";
	public String repoUrl = "";
	public String siteUrl = "";
	public String category = "";
	public String stackOverflowTags;
	public List<String> stackOverflowTagList = new ArrayList<>();
	public Boolean aggregator;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRepoUrl() {
		return this.repoUrl;
	}

	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	public String getSiteUrl() {
		return this.siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStackOverflowTags() {
		return this.stackOverflowTags;
	}

	public void setStackOverflowTags(String stackOverflowTags) {
		this.stackOverflowTags = stackOverflowTags;
	}

	public List<String> getStackOverflowTagList() {
		return this.stackOverflowTagList;
	}

	public void setStackOverflowTagList(List<String> stackOverflowTagList) {
		this.stackOverflowTagList = stackOverflowTagList;
	}

	public Boolean getAggregator() {
		return this.aggregator;
	}

	public void setAggregator(Boolean aggregator) {
		this.aggregator = aggregator;
	}
}
