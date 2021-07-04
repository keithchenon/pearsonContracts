import org.springframework.cloud.contract.spec.Contract

Contract.make {
	// 1 description (single line, multiline), priority, name, ignored, request, response
	description("""
	Hello
	Some meaningful comment
""")
	// the lower the value the higher the priority
	priority(100)
	name("should_use_a_different_name")
//	ignored()
	request {
		// 2 method "GET"
		method(GET())
		// 3 first URL
		url("/info")
		// 4 url path as syntactic sugar
//		urlPath("info") {
//			queryParameters {
//				parameter("first", "value")
//				parameter([
//				        second: "value", third: "value"
//				])
//			}
//		}
		// 4 headers, show the `@Delegate`
		headers {
			// show header delegates for keys and value
//			header(contentType(), applicationPdf())
			// show content type and accept shortcuts
			contentType(applicationJson())
			header([
			        second: "value", third: "value"
			])
		}
		// 5 body (no sense for GET; show text, list, map, full json text, file)
//		body("value")
//		body(["list, of", "value"])
//		body("""{ "some": "value" }""")
//		body(file("some_file.json"))
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
		// 7 multipart, map of parameters, file
		multipart(
				[
					// key (parameter name), value (parameter value) pair
					key: "value",
					// a named parameter (e.g. with `file` name) that represents file with
					// `name` and `content`. You can also call `named("fileName", "fileContent")`
					file: named(
							// name of the file
							name: $("filename.csv"),
							// content of the file
							content:  $('file content'),
							// content type for the part
							contentType:  $('application/json'))
				]
		)
		bodyMatchers {
			jsonPath('$.foo', byRegex("[0-9][0-1]"))
		}
	}
	// 8 - response
	response {
		// 9 - status
		status(OK())
		// 10 - delay in stubs
		fixedDelayMilliseconds(1000)
		// 11 - async for futures etc.
		async()
		// 12 - headers
		headers {
			// show header delegates for keys and value
//			header(contentType(), applicationPdf())
			// show content type and accept shortcuts
			contentType(applicationJson())
			header([
					second: "value", third: "value"
			])
		}
		// 13 - cookies
		cookies {
			cookie("first", "value")
			cookie([
					second: "value", third: "value"
			])
		}
		// 14 body (no sense for GET; show text, list, map, full json text, file)
//		body("value")
//		body(["list, of", "value"])
//		body("""{ "some": "value" }""")
//		body(file("some_file.json"))
		body([
				some: "value"
		])
		bodyMatchers {
			jsonPath('$.foo', byRegex("[0-9][0-1]"))
		}
	}
}