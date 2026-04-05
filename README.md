# YAML

YAML configuration mapping library with file watching and hot-reload support.
Maps YAML files to Java objects using SnakeYAML with custom type converters for
arrays, configs, durations, enums, and more. Provides a `ConfigSection` hierarchy
and a `ConfigMapper` abstraction for structured configuration access.

> [!IMPORTANT]
> This library is under active development. APIs may change between releases
> until a stable `1.0.0` release is published.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
  - [Defining a Configuration](#defining-a-configuration)
  - [Loading a Configuration](#loading-a-configuration)
  - [Config Sections](#config-sections)
  - [Custom Converters](#custom-converters)
- [Built-in Converters](#built-in-converters)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## Features

- **YAML-to-object mapping** - Automatic deserialization of YAML files into typed Java objects
- **Hot-reload** - File watching with automatic re-mapping when configuration files change
- **ConfigSection hierarchy** - Nested section support for structured configuration access
- **Custom type converters** - Extensible converter system for arrays, durations, enums, and more
- **Annotation-driven** - Use `@Flag` to mark and configure fields for mapping
- **Built on SnakeYAML** - Leverages the mature SnakeYAML parser under the hood
- **JitPack distribution** - Add as a Gradle dependency with no manual installation

## Getting Started

### Prerequisites

| Requirement | Version | Notes |
|-------------|---------|-------|
| [Java](https://adoptium.net/) | **21+** | Required (LTS recommended) |
| [Gradle](https://gradle.org/) | **9.4+** | Or use the included `./gradlew` wrapper |
| [Git](https://git-scm.com/) | **2.x+** | For cloning the repository |

### Installation

Add the JitPack repository and dependency to your `build.gradle.kts`:

```kotlin
repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.simplified-dev:yaml:master-SNAPSHOT")
}
```

<details>
<summary>Gradle (Groovy)</summary>

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.simplified-dev:yaml:master-SNAPSHOT'
}
```

</details>

<details>
<summary>Maven</summary>

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.simplified-dev</groupId>
    <artifactId>yaml</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

</details>

> [!NOTE]
> This library depends on other Simplified-Dev modules (`collections`, `utils`,
> `reflection`) and on SnakeYAML, which are resolved from JitPack and Maven
> Central automatically.

## Usage

### Defining a Configuration

Extend `ConfigMapper` and annotate fields with `@Flag`:

```java
import dev.simplified.yaml.ConfigMapper;
import dev.simplified.yaml.annotation.Flag;

public class AppConfig extends ConfigMapper {

    @Flag
    private String serverName;

    @Flag
    private int maxPlayers;

    @Flag
    private Duration sessionTimeout;
}
```

### Loading a Configuration

```java
AppConfig config = new AppConfig();
config.load(Path.of("config.yml"));

String name = config.getServerName();
```

### Config Sections

Use `ConfigSection` for nested YAML structures:

```yaml
# config.yml
database:
  host: localhost
  port: 5432
  name: myapp
```

```java
ConfigSection section = config.getSection("database");
String host = section.getString("host");
int port = section.getInt("port");
```

### Custom Converters

Implement a converter for unsupported types:

```java
import dev.simplified.yaml.converter.ConfigConverter;

public class MyTypeConverter implements ConfigConverter<MyType> {

    @Override
    public MyType convert(Object value) {
        return MyType.parse(value.toString());
    }
}
```

## Built-in Converters

| Converter | Target Type | Description |
|-----------|-------------|-------------|
| `ArrayConverter` | Arrays | Converts YAML lists to typed Java arrays |
| `ConfigConverter` | Config objects | Nested configuration object mapping |
| `DurationConverter` | `java.time.Duration` | Parses duration strings (e.g., `30s`, `5m`, `1h`) |
| `EnumConverter` | Enum types | Case-insensitive enum constant lookup |

## Project Structure

```
src/main/java/dev/simplified/yaml/
├── ConfigMapper.java
├── ConfigSection.java
├── YamlMap.java
├── annotation/
│   └── Flag.java
└── converter/
    ├── ArrayConverter.java
    ├── ConfigConverter.java
    ├── DurationConverter.java
    └── EnumConverter.java
```

| Package | Description |
|---------|-------------|
| `dev.simplified.yaml` | Core classes - ConfigMapper, ConfigSection, YamlMap |
| `dev.simplified.yaml.annotation` | Configuration annotations (`@Flag`) |
| `dev.simplified.yaml.converter` | Type converters for YAML value deserialization |

> [!TIP]
> The `ConfigMapper` base class handles file watching and hot-reload
> automatically. Simply call `load()` with a path and the configuration
> will stay in sync with the file on disk.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for development setup, code style
guidelines, and how to submit a pull request.

## License

This project is licensed under the **Apache License 2.0** - see
[LICENSE.md](LICENSE.md) for the full text.
