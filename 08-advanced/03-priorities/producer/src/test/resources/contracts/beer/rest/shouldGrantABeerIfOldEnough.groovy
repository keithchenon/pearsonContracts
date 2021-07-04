package contracts.beer.rest


import org.springframework.cloud.contract.spec.Contract

Contract.make {
	// the higher the value of priority, the lower priority
	priority(10)
	request {
		method POST()
		url '/check'
		body(
				age: 22,
				name: "marcin"
		)
		headers {
			contentType(applicationJson())
		}
	}
	response {
		status 200
		body([
				status: "OK"
		])
		headers {
			contentType(applicationJson())
		}
	}
}
