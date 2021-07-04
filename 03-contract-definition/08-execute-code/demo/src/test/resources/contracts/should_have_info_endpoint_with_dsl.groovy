import org.springframework.cloud.contract.spec.Contract

Contract.make {
	request {
		method(GET())
		url($(c(regex("/get/[0-9]")), p(execute("generateUrl()"))))
		body([
				duck: "foo"
		])
		cookies {
			cookie("foo", $(c("foo"), p(execute("generateCookie()"))))
		}
		headers {
			header("foo", $(c("foo"), p(execute("generateHeaderValue()"))))
		}
	}
	response {
		status(OK())
		headers {
			header("bar", $(c("bar"), p(execute('assertThatIsAString($it)'))))
		}
		body([
				duck : $(c(123), p(execute('assertThatIsANumber($it)'))),
				alpha: 'abc',
				beta : 'bcd'
		])
		bodyMatchers {
			jsonPath('$.alpha', byCommand('assertThatIsAString($it)'))
		}
	}
}