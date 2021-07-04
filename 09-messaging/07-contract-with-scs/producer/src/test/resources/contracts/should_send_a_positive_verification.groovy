import org.springframework.cloud.contract.spec.Contract

Contract.make {
	label("positive")
	input {
		messageFrom("person")
		messageBody([
		        age: 25
		])
		messageHeaders {
			messagingContentType(applicationJson())
		}
	}
	outputMessage {
		sentTo("verifications")
		body([
		        eligible: true
		])
		headers {
			messagingContentType(applicationJson())
		}
	}
}