package contracts.beer.rest


import org.springframework.cloud.contract.spec.Contract

Contract.make {
	request {
		method POST()
		url '/check'
		body(
				name: "marcin",
				age: 22
		)
		headers {
			contentType(applicationJson())
		}
	}
	response {
		status 200
		body([
				status: "OK",
				name: $(fromRequest().body('$.name')),
				path: value(fromRequest().path(0))
		])
		headers {
			contentType(applicationJson())
		}
	}
}
