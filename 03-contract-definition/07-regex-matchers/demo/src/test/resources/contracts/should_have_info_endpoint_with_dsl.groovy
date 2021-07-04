import org.springframework.cloud.contract.spec.Contract

Contract.make {
	request {
		method(GET())
		url($(c(regex("/get/[0-9]")), p("/get/1")))
		body([
				duck                : 123,
				alpha               : 'abc',
				number              : 123,
				aBoolean            : true,
				date                : '2017-01-01',
				dateTime            : '2017-01-01T01:23:45',
				time                : '01:02:34',
				valueWithoutAMatcher: 'foo',
				valueWithTypeMatch  : 'string'
		])
		bodyMatchers {
			jsonPath('$.duck', byRegex("[0-9]{3}"))
			jsonPath('$.duck', byEquality())
			jsonPath('$.alpha', byRegex(onlyAlphaUnicode()))
			jsonPath('$.alpha', byEquality())
			jsonPath('$.number', byRegex(number()))
			jsonPath('$.aBoolean', byRegex(anyBoolean()))
			jsonPath('$.date', byDate())
			jsonPath('$.dateTime', byTimestamp())
			jsonPath('$.time', byTime())
		}
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
				duck                 : 123,
				alpha                : 'abc',
				number               : 123,
				positiveInteger      : 1234567890,
				negativeInteger      : -1234567890,
				positiveDecimalNumber: 123.4567890,
				negativeDecimalNumber: -123.4567890,
				aBoolean             : true,
				date                 : '2017-01-01',
				dateTime             : '2017-01-01T01:23:45',
				time                 : "01:02:34",
				valueWithoutAMatcher : 'foo',
				valueWithTypeMatch   : 'string',
				valueWithMin         : [
						1, 2, 3
				],
				valueWithMax         : [
						1, 2, 3
				],
				valueWithMinMax      : [
						1, 2, 3
				],
				valueWithMinEmpty    : [],
				valueWithMaxEmpty    : [],
				nullValue            : null
		])
		bodyMatchers {
			// asserts the jsonpath value against manual regex
			jsonPath('$.duck', byRegex("[0-9]{3}"))
			// asserts the jsonpath value against the provided value
			jsonPath('$.duck', byEquality())
			// asserts the jsonpath value against some default regex
			jsonPath('$.alpha', byRegex(onlyAlphaUnicode()))
			jsonPath('$.alpha', byEquality())
			jsonPath('$.number', byRegex(number()))
			jsonPath('$.positiveInteger', byRegex(anInteger()))
			jsonPath('$.negativeInteger', byRegex(anInteger()))
			jsonPath('$.positiveDecimalNumber', byRegex(aDouble()))
			jsonPath('$.negativeDecimalNumber', byRegex(aDouble()))
			jsonPath('$.aBoolean', byRegex(anyBoolean()))
			// asserts vs inbuilt time related regex
			jsonPath('$.date', byDate())
			jsonPath('$.dateTime', byTimestamp())
			jsonPath('$.time', byTime())
			// asserts that the resulting type is the same as in response body
			jsonPath('$.valueWithTypeMatch', byType())
			jsonPath('$.valueWithMin', byType {
				// results in verification of size of array (min 1)
				minOccurrence(1)
			})
			jsonPath('$.valueWithMax', byType {
				// results in verification of size of array (max 3)
				maxOccurrence(3)
			})
			jsonPath('$.valueWithMinMax', byType {
				// results in verification of size of array (min 1 & max 3)
				minOccurrence(1)
				maxOccurrence(3)
			})
			jsonPath('$.valueWithMinEmpty', byType {
				// results in verification of size of array (min 0)
				minOccurrence(0)
			})
			jsonPath('$.valueWithMaxEmpty', byType {
				// results in verification of size of array (max 0)
				maxOccurrence(0)
			})
			// fixed in 2.0.3
			jsonPath('$.nullValue', byNull())
		}
	}
}