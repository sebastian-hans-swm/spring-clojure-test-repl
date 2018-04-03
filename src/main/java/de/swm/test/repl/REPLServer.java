/*
 * Copyright 2018 SWM Services GmbH
 */

package de.swm.test.repl;

import clojure.java.api.Clojure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class REPLServer implements ApplicationContextAware, InitializingBean, DisposableBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(REPLServer.class);
	private static ApplicationContext applicationContext = null;

	public static ApplicationContext getContext() {
		return applicationContext;
	}

	private final long port;
	private final String bindAddress;
	private Object serverInstance = null;

	public REPLServer(final String bindAddress, final long port) {
		this.port = port;
		this.bindAddress = bindAddress;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		REPLServer.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() {
		if (serverInstance == null) {
			Clojure.var("clojure.core", "require").invoke(Clojure.read("clojure.tools.nrepl.server"));
			serverInstance = Clojure.var("clojure.tools.nrepl.server", "start-server")
					.invoke(Clojure.read(":port"), port,
							Clojure.read(":bind"), bindAddress);
			LOGGER.info("Started nREPL on {}:{}", bindAddress, port);
		}
	}

	@Override
	public void destroy() {
		if (serverInstance != null) {
			LOGGER.info("Shutting down nREPL on {}:{}", bindAddress, port);
			Clojure.var("clojure.tools.nrepl.server", "stop-server").invoke(serverInstance);
			serverInstance = null;
		}
	}
}
