package contracts.beer.rest

org.springframework.cloud.contract.spec.Contract.make {
	request {
		description("""
Represents a successful scenario of getting a beer

given:
	client is old enough
when:
	he applies for a beer
then:
	we'll grant him the beer
""")
		method POST()
		url '/check'
		body(
				age: $(regex('[2-9][0-9]'))
		)
		headers {
			contentType(applicationJson())
		}
	}
	response {
		status OK()
		body( """
			{
				"status": "OK"
			}
			""")
		headers {
			contentType(applicationJson())
		}
	}
}
