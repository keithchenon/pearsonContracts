package com.example.demo;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.restdocs.SpringCloudContractRestDocs;
import org.springframework.cloud.contract.wiremock.restdocs.WireMockRestDocs;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class PersonControllerTests {

	@Autowired MockMvc mockMvc;

	@Test
	public void should_return_a_person() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/person/1").content("hello"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("{\"name\":\"marcin\"}"))
				.andDo(MockMvcRestDocumentation.document("index",
						// let's generate contracts too!
						SpringCloudContractRestDocs.dslContract()))
				.andDo(WireMockRestDocs
						.verify()
						.wiremock(
								WireMock.post(WireMock.urlMatching("/person/[0-9]"))
								.withRequestBody(WireMock.matching("hello.*")))
						.stub("person"));
	}

}
