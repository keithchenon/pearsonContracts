package com.example


import groovy.transform.CompileStatic

import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.ContractConverter
import org.springframework.cloud.contract.spec.internal.Header

/**
 * Basic properties converter
 *
 * @author Marcin Grzejszczak
 */
// Use CompileScope as much as possible
@CompileStatic
class PropertiesContractConverter implements ContractConverter<List<PropertiesRepresentation>> {
	@Override boolean isAccepted(File file) {
		return file.getName().endsWith(".properties")
	}

	@Override Collection<Contract> convertFrom(File file) {
		PropertiesRepresentation representation = new PropertiesRepresentation()
		try {
			representation.load(new FileInputStream(file))
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex)
		}
		return Collections.singletonList(representation.toContract())
	}

	@Override List<PropertiesRepresentation> convertTo(Collection<Contract> collection) {
		return collection.collect {
			new PropertiesRepresentation(it)
		}
	}

	@Override
	Map<String, byte[]> store(List<PropertiesRepresentation> contracts) {
		return contracts.collectEntries {
			String key = it.requestMethod() + "_" + it.hashCode() + ".properties"
			ByteArrayOutputStream baos = new ByteArrayOutputStream()
			it.store(baos, "")
			return [(key) : baos.toByteArray()]
		}
	}
}

@CompileStatic
class PropertiesRepresentation extends Properties {

	PropertiesRepresentation() {}

	PropertiesRepresentation(Contract contract) {
		put("request.method", contract.request.method.serverValue)
		put("request.url", contract.request.url.serverValue)
		put("request.body", contract.request.body.serverValue)
		put("request.headers.content-type", contract.request.headers.entries
				.find { it.name.toLowerCase() == "Content-Type"}?.serverValue)
		put("response.body", contract.response.body.clientValue)
		put("response.status", contract.response.status.clientValue)
		put("response.headers.content-type", contract.response.headers.entries
				.find { Header header -> header.name.toLowerCase() == "Content-Type"}?.clientValue)
	}

	String requestMethod() {
		return get("request.method") as String
	}

	String requestHeader(String headerName) {
		return get("request.headers." + headerName) as String
	}

	String responseHeader(String headerName) {
		return get("response.headers." + headerName) as String
	}

	Contract toContract() {
		return Contract.make {
			request {
				method(requestMethod())
				url(get("request.url") as String)
				body(get("request.body") as String)
				headers {
					header(contentType(), requestHeader("content-type"))
				}
			}
			response {
				body(get("response.body") as String)
				status(get("response.status") as Integer)
				headers {
					header(contentType(), responseHeader("content-type"))
				}
			}
		}
	}
}
