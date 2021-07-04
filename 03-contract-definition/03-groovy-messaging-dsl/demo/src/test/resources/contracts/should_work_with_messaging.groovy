import org.springframework.cloud.contract.spec.Contract

Contract.make {
	// 1 description (single line, multiline), name, ignored, label, input, output
	description("""
	Hello
	Some meaningful comment
""")
	name("should_use_a_different_name")
	label ("some_label")
//	ignored()
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
		// we'll talk about those later
		bodyMatchers {

		}
	}
	outputMessage {
		sentTo('output')
		body([
				bookName: 'foo'
		])
		headers {
			header('BOOK-NAME', 'foo')
		}
		// we'll talk about those later
		bodyMatchers {

		}
		assertThat("equals(this)")
	}
}