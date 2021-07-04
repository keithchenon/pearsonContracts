package com.example.extension;

import java.util.Arrays;
import java.util.List;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Extension;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import org.springframework.cloud.contract.verifier.dsl.wiremock.DefaultResponseTransformer;
import org.springframework.cloud.contract.verifier.dsl.wiremock.WireMockExtensions;

public class TestWireMockExtensions implements WireMockExtensions {
	@Override
	public List<Extension> extensions() {
		return Arrays.asList(
				new DefaultResponseTransformer(),
				new CustomResponseExtension()
		);
	}
}

class CustomResponseExtension extends ResponseDefinitionTransformer {

	@Override
	public ResponseDefinition transform(Request request,
			ResponseDefinition responseDefinition, FileSource files,
			Parameters parameters) {
		ResponseDefinitionBuilder newResponseDefBuilder = ResponseDefinitionBuilder.like(responseDefinition);
		newResponseDefBuilder.withHeader("HACKED", "TRANSFORMER");
		return newResponseDefBuilder.build();
	}

	@Override
	public String getName() {
		return "foo-response-transformer";
	}
}