package com.example.demo;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

/**
 * @author Marcin Grzejszczak
 * @since
 */
public class BaseClass {

	private static final String OUTPUT = "target/generated-snippets";

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(OUTPUT);

	@Rule public TestName testName = new TestName();

	@Before
	public void setup() {
		RestAssuredMockMvc.mockMvc(
				MockMvcBuilders.standaloneSetup(new FooController())
				.apply(documentationConfiguration(this.restDocumentation))
				.alwaysDo(document(getClass().getSimpleName() + "_" + this.testName.getMethodName()))
				.build());
	}
}
