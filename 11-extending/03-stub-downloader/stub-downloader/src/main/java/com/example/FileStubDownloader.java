package com.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.contract.stubrunner.StubConfiguration;
import org.springframework.cloud.contract.stubrunner.StubDownloader;
import org.springframework.cloud.contract.stubrunner.StubDownloaderBuilder;
import org.springframework.cloud.contract.stubrunner.StubRunnerOptions;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

/**
 * Allows to read stubs and contracts from a given location. Contrary
 * to {@link org.springframework.cloud.contract.stubrunner.AetherStubDownloaderBuilder},
 * doesn't require the location to be a maven repository.
 *
 * @author Marcin Grzejszczak
 */
public class FileStubDownloader implements StubDownloaderBuilder {

	private static final List<String> ACCEPTABLE_PROTOCOLS = Collections
		.singletonList("stubs");

	/**
	 * Does any of the accepted protocols matches the URL of the repository
	 * @param url - of the repository
	 */
	public static boolean isProtocolAccepted(String url) {
		return ACCEPTABLE_PROTOCOLS.stream().anyMatch(url::startsWith);
	}

	@Override
	public StubDownloader build(StubRunnerOptions stubRunnerOptions) {
		// should  work only in remote and local option
		if (stubRunnerOptions.getStubsMode() == StubRunnerProperties.StubsMode.CLASSPATH
				|| stubRunnerOptions.getStubRepositoryRoot() == null) {
			return null;
		}
		Resource resource = stubRunnerOptions.getStubRepositoryRoot();
		// we verify whether the protocol starts with `stubs://`
		if (!(resource instanceof StubsResource)) {
			return null;
		}
		return new StubsStubDownloader(stubRunnerOptions);
	}

	@Override
	public Resource resolve(String location, ResourceLoader resourceLoader) {
		if (StringUtils.isEmpty(location) || !isProtocolAccepted(location)) {
			return null;
		}
		return new StubsResource(location);
	}

}

/**
 * Primitive version of a Stubs {@link Resource}. Automatically makes Spring convert
 * the URL to a Resource
 */
class StubsResource extends AbstractResource {

	private final String rawLocation;

	StubsResource(String location) {
		this.rawLocation = location;
	}

	@Override
	public String getDescription() {
		return this.rawLocation;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public URI getURI() throws IOException {
		return URI.create(this.rawLocation);
	}
}

/**
 * Concrete logic of picking stubs
 */
class StubsStubDownloader implements StubDownloader {
	private static final Log log = LogFactory.getLog(StubsStubDownloader.class);

	private final StubRunnerOptions stubRunnerOptions;

	StubsStubDownloader(StubRunnerOptions stubRunnerOptions) {
		this.stubRunnerOptions = stubRunnerOptions;
	}

	// StubConfiguration is the concrete stub to be fetched
	@Override public Map.Entry<StubConfiguration, File> downloadAndUnpackStubJar(
			StubConfiguration stubConfiguration) {
		try {
			String schemeSpecific = schemeSpecificPart(this.stubRunnerOptions.getStubRepositoryRoot().getURI());
			log.info("Stubs are present under [" + schemeSpecific + "]");
			return new AbstractMap.SimpleEntry<>(stubConfiguration, new File(schemeSpecific));
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	// stubs://foo -> foo
	private String schemeSpecificPart(URI uri) {
		String part = uri.getSchemeSpecificPart();
		if (StringUtils.isEmpty(part)) {
			return part;
		}
		return part.startsWith("//") ? part.substring(2) : part;
	}
}