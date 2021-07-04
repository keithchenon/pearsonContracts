import org.springframework.cloud.contract.spec.Contract

Contract.make {
	request {
		method(GET())
		urlPath(value(regex("/info/[0-9]"))) {
			queryParameters {
				parameter 'limit': $(consumer(equalTo('20')), producer(equalTo('10')))
				parameter 'offset': $(c(containing("20")), p(equalTo('20')))
				parameter 'filter': 'email'
				parameter 'sort': equalTo("name")
				parameter 'age': $(stub(notMatching("^\\w*\$")), test('99'))
				parameter 'name': $(client(matching('John.*')), server('John.Doe'))
				parameter 'email': 'bob@email.com'
				parameter 'hello': $(consumer(matching('John.*')), producer(absent()))
				parameter 'hello2': absent()
			}
		}
		headers {
			contentType(applicationJson())
			header([
					second: "value", third: $(anyAlphaNumeric())
			])
		}
		body([
				foo1: $(c(regex("[0-9]")), p(1)),
				foo2: $(c(regex("[0-9]"))),
				foo3: $(anyAlphaNumeric()),
				foo4: value(regex(aDouble())),
				foo5: value(anyDouble())
		])
	}
	response {
		status(OK())
		headers {
			contentType(applicationJson())
			header([
					second: "value", third: $(anyAlphaNumeric())
			])
		}
		body([
				foo1: $(p(regex("[0-9]")), c(1)),
				foo2: $(p(regex("[0-9]"))),
				foo3: $(anyAlphaNumeric()),
				foo4: value(regex(aDouble())),
				foo5: value(anyDouble())
		])
	}
}