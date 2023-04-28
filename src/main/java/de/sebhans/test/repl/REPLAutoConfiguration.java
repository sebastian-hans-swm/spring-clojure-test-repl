package de.sebhans.test.repl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@AutoConfiguration
public class REPLAutoConfiguration {
	@Value("${test.repl.port:7888}")
	private long port;

	@Value("${test.repl.bind-address:localhost}")
	private String bindAddress;

	@Bean
	@Profile("test-repl")
	REPLServer testREPLServer() {
		return new REPLServer(bindAddress, port);
	}
}
