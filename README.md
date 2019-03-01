# test-repl
Adds a Clojure REPL to Spring applications for test purposes.

## Integration
To use, add the following snippet to your POM and build and run your Spring application with profile `test-repl`.
```xml
<profiles>
	<profile>
		<id>test-repl</id>
		<dependencies>
			<dependency>
				<groupId>de.swm.test</groupId>
				<artifactId>test-repl</artifactId>
				<version>1.2.0-SNAPSHOT</version>
				<scope>runtime</scope>
			</dependency>
		</dependencies>
	</profile>
</profiles>
```

Putting the dependency in a Maven profile ensures that the dependency on Clojure does not pollute any builds (test, production, dev) unless explicitly enabled during the Maven build.

When running the application, activate the spring profile `test-repl` to automatically start a REPL listening on localhost:7888.
The port can be changed by setting the property `test.repl.port` to a different port.
The bind address can be changed by setting the property `test.repl.bind-address` to a different value.

## Changelog
See [CHANGELOG.md](CHANGELOG.md).
