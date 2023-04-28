# spring-clojure-test-repl
Adds a Clojure REPL to Spring applications to support interactive bug hunting.

## Integration
To use, add the following snippet to your POM and **build** and **run** your Spring application with profile
`test-repl`.
```xml
<profiles>
    <profile>
        <id>test-repl</id>
        <dependencies>
            <dependency>
                <groupId>de.sebhans.test</groupId>
                <artifactId>spring-clojure-test-repl</artifactId>
                <version>1.2.0</version>
                <scope>runtime</scope>
                <classifier>include-clojars</classifier>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

Putting the dependency in a Maven profile ensures that the dependency on Clojure does not pollute any builds (test, production, dev) unless explicitly enabled during the Maven build.

When running the application, activate the spring profile `test-repl` to automatically start a REPL listening on `localhost:7888`.
The port can be changed by setting the property `test.repl.port` to a different port.
The bind address can be changed by setting the property `test.repl.bind-address` to a different value.

Since JDK 16, the following JDK options are required to make the reflection in Clojure work: `--add-exports=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED`

## Usage

### Connecting to the REPL
Use your favorite nREPL client to connect to `localhost:7888`.
The only client which has been tested extensively (because I use it) is
[GNU Emacs](https://www.gnu.org/software/emacs/) with [Monroe](https://github.com/sanel/monroe/).
You might also try the [Clojure-Kit](https://plugins.jetbrains.com/plugin/8636-clojure-kit) IntelliJ plugin
or any client listed on the [nREPL Clients](https://nrepl.org/nrepl/usage/clients.html) web page.

Type Clojure code into the REPL and have fun.

### Available Functions
This project includes the following Clojure libraries.
* [nrepl 1.0.0](https://github.com/nrepl/nrepl) for provisioning of the nREPL
* [org.clojure/data.json 2.4.0](https://github.com/clojure/data.json) for JSON parsing and building
* [http-kit 2.6.0](https://github.com/http-kit/http-kit) for HTTP requests
* [enlive 1.1.6](https://github.com/cgrand/enlive) for HTML parsing

Access to the Spring application context is provided via the static method `REPLServer.getContext`,
which means you can access Spring beans from the REPL like this:
```clojure
; Import REPLServer class so its methods are available in the REPL:
(import '(de.sebhans.test.repl REPLServer))

; Define helper function to access beans:
(defn get-bean
  "Look up bean by name in the Spring application context"
  [name]
  (.getBean (REPLServer/getContext) name))

; Fetch a bean from the application context:
(def my-bean (get-bean "myBeanName"))

; Call a method on the bean
(.someMethod my-bean "these" "are" "the" "arguments")
```

## Changelog
See [CHANGELOG.md](CHANGELOG.md).
