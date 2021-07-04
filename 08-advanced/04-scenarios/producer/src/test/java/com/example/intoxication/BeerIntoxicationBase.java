package com.example.intoxication;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

import static com.example.intoxication.DrunkLevel.DRUNK;
import static com.example.intoxication.DrunkLevel.SOBER;
import static com.example.intoxication.DrunkLevel.TIPSY;
import static com.example.intoxication.DrunkLevel.WASTED;

public abstract class BeerIntoxicationBase {

	// we need to maintain state between test executions!
	static ResponseProvider provider = new MockResponseProvider();

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(
				new BeerServingController(provider)
		);
	}

	static class MockResponseProvider implements ResponseProvider {

		private DrunkLevel previous = SOBER;
		private DrunkLevel current = SOBER;

		@Override public Response thereYouGo(Customer personToCheck) {
			if ("marcin".equals(personToCheck.name)) {
				 switch (this.current) {
				 case SOBER:
					 this.current = TIPSY;
					 this.previous = SOBER;
					 break;
				 case TIPSY:
					 this.current = DRUNK;
					 this.previous = TIPSY;
					 break;
				 case DRUNK:
					 this.current = WASTED;
					 this.previous = DRUNK;
					 break;
				 case WASTED:
					 throw new UnsupportedOperationException("You can't handle it");
				 }
			}
			return new Response(this.previous, this.current);
		}
	}
}
