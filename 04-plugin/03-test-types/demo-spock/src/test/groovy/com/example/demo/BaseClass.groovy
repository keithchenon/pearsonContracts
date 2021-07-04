package com.example.demo

import io.restassured.module.mockmvc.RestAssuredMockMvc
import spock.lang.Specification;

/**
 * @author Marcin Grzejszczak
 * @since
 */
class BaseClass extends Specification {

	def setup() {
		RestAssuredMockMvc.standaloneSetup(new ProducerController(
				{ personToCheck -> personToCheck.age >= 20 }
		))
	}
}