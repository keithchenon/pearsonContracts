import org.springframework.cloud.contract.spec.Contract

Contract.make {
	request {
		method(GET())
		url("/")
	}
	response {
		status(OK())
		body("Hello World")
	}
}