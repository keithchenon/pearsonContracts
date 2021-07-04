package contracts.beer.rest


import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description("""
Represents a successful scenario of getting a beer

```
given:
	client is old enough
when:
	he applies for a beer
then:
	we'll grant him the beer
```

""")
	request {
		method POST()
		// name in format consumer___producer___text
		name("10-04-pact-consumer___10-05-pact-producer___too young")
		url '/check'
		body(
				age: 60
		)
		headers {
			contentType(applicationJson())
		}
		bodyMatchers {
			jsonPath('$.age', byRegex(regex("[2-9][0-9]")))
		}
	}
	response {
		status 200
		body("""
			{
				"status": "OK"
			}
			""")
		headers {
			contentType(applicationJson())
		}
	}
}
