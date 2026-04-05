# yaml

YAML configuration mapping library with file watching and hot-reload support.

## Package Structure
- `dev.simplified.yaml` - core (ConfigMapper, ConfigSection, YamlMap)
- `dev.simplified.yaml.annotation` - config annotations (Flag)
- `dev.simplified.yaml.converter` - type converters

## Key Classes
- `ConfigMapper` - abstract base for YAML-to-object mapping
- `ConfigSection` - section hierarchy for structured config access
- `YamlMap` - YAML map wrapper
- `Flag` - annotation for config fields
- `ArrayConverter`, `ConfigConverter`, `DurationConverter`, `EnumConverter` - type converters

## Dependencies
- Internal: `collections`, `utils`, `reflection` (Simplified-Dev)
- External: SnakeYAML, Log4j2, Lombok, JetBrains annotations
- Test: none

## Build
```bash
./gradlew build
```

## Info
- Java 21
- Group: `dev.simplified`, artifact: `yaml`, version: `1.0.0`
- 18 source classes, no tests
