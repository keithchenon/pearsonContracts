package com.example.demo;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
				.andExpect(MockMvcResultMatchers.content().string("marcin"))
				//.andDo(MockMvcRestDocumentation.document("{method-name}"));
				// this stub will be generated with index name
				.andDo(MockMvcRestDocumentation.document("index"))
				// stubs!
				.andDo(WireMockRestDocs
						// perform additional verification
						.verify()
						// dynamic matching of request via WireMock API
						.wiremock(
								WireMock.post(WireMock.urlMatching("/person/[0-9]"))
								.withRequestBody(WireMock.matching("hello.*")))
						// name of the stub for dynamic part
						.stub("person"));
	}

}
