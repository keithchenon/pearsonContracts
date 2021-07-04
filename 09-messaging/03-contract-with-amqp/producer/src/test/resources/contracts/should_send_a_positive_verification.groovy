import org.springframework.cloud.contract.spec.Contract

Contract.make {
	label("positive")
	input {
		triggeredBy("positive()")
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