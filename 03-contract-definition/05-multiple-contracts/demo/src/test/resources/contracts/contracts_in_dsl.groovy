import org.springframework.cloud.contract.spec.Contract

[
		Contract.make {
			description("""
	Hello
	Some meaningful comment
""")
			label("some_label")
			input {
				// triggeredBy(..)
				messageBody([
						bookName: 'foo'
				])
				messageFrom('input')
				messageHeaders {
					header('sample', 'header')
				}
				assertThat("equals(this)")
			}
			outputMessage {
				sentTo('output')
				body([
						bookName: 'foo'
				])
				headers {
					header('BOOK-NAME', 'foo')
				}
				assertThat("equals(this)")
			}
		},
		Contract.make {
			description("""
	Hello
	Some meaningful comment
""")
			request {
				// 2 method "GET"
				method(GET())
				// 3 first URL
				url("/info")
				headers {
					contentType(applicationJson())
					header([
							second: "value", third: "value"
					])
				}
				body([
						some: "value"
				])
				// 6 cookies
				cookies {
					cookie("first", "value")
					cookie([
							second: "value", third: "value"
					])
				}
				// 8 - response
				response {
					status(OK())
					headers {
						contentType(applicationJson())
					}
					body([
							some: "value"
					])
				}
			}
		},
		Contract.make {
			description("""
	Hello
	Some meaningful comment
""")
			request {
				// 2 method "GET"
				method(GET())
				// 3 first URL
				url("/info2")
				name("hacked_groovy")
				headers {
					contentType(applicationJson())
					header([
							second: "value", third: "value"
					])
				}
				body([
						some: "value"
				])
				// 6 cookies
				cookies {
					cookie("first", "value")
					cookie([
							second: "value", third: "value"
					])
				}
				// 8 - response
				response {
					status(OK())
					headers {
						contentType(applicationJson())
					}
					body([
							some: "value"
					])
				}
			}
		}
]