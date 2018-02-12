/*
 * Copyright 2018 SWM Services GmbH
 */

package de.swm.test.repl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class REPLConfiguration {
	@Value("${test.repl.port:7888}")
	private long port;

	@Bean
	@Profile("test-repl")
	REPLServer testREPLServer() {
		return new REPLServer(port);
	}
}
