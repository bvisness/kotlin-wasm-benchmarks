# Kotlin Wasm Benchmarks

# SPIDERMONKEY INTERLUDE

The following four tasks will run the full benchmark suite:

- V8: `./gradlew wasmBenchmark`
- V8 (wasm-opt): `./gradlew wasmOptBenchmark`
- SpiderMonkey: `./gradlew jsShell_wasmBenchmark`
- SpiderMonkey (wasm-opt): `./gradlew jsShell_wasmOptBenchmark`

Results can be aggregated using the provided [Nushell](https://www.nushell.sh/) script `compare.sh`. Example:

```
./compare.sh \
  build/reports/benchmarks/main/2023-06-14T17.18.09.579649/wasm.json \
  build/reports/benchmarks/main/2023-06-14T14.56.11.053839/jsShell_wasm.json \
  build/reports/benchmarks/main/2023-06-14T15.51.16.615318/wasmOpt.json \
  build/reports/benchmarks/main/2023-06-14T16.32.08.205242/jsShell_wasmOpt.json
```

This will convert multiple JSON build reports to a single CSV in `benchmarks.csv`.

## Running with a local JS shell

By default, the gradle scripts will download its own copy of the JS shell to run tests against. To use a local build of the JS shell, replace `executable` in `createJsShellExec` in `build.gradle.kts`:

```kotlin
// executable = File(unzipJsShell.get().destinationDir, "js").absolutePath
executable = "js" // or a full path, if PATH lookups aren't working for you
```

## Java versions

The Kotlin compiler doesn't yet work with Java 20 - if you try, you will likely see "Unknown Kotlin JVM target: 20". To resolve this, you can install Java 17 from this page: https://www.oracle.com/java/technologies/downloads/

If you have multiple versions of Java on your system, you'll have to figure out the JAVA_HOME location for Java 17. On Mac you can do it like so:

```
/usr/libexec/java_home -v 17
```

You can then tell Gradle which Java to use like so:

```
./gradlew -Dorg.gradle.java.home=$(/usr/libexec/java_home -v 17)
```

# END SPIDERMONKEY INTERLUDE

This is kotlin multiplatform benchmarks to compare Kotlin Wasm vs Kotlin JS performance.

# Description
These benchmarks are based on JetBrains micro-benchmarks benchmarks and macro-benchmarks based on [are-we-fast-yet](https://github.com/smarr/are-we-fast-yet) benchmarks collection.
To perform benchmarks it uses [kotlinx-benchmarks](https://github.com/Kotlin/kotlinx-benchmark) library.

# Build and Run
Specify Kotlin version in `gradle.properties` file or use additional gradle argument `-Pkotlin_version=1.8.0`.

### To run benchmarks with Wasm:

`./gradlew wasmBenchmark`

### To run benchmarks with [Binaryen](https://github.com/WebAssembly/binaryen) optimised Wasm:

`./gradlew wasmOptBenchmark`

### To run benchmarks with JS:

`./gradlew jsBenchmark`

### Result output is located in `build/reports` directory.

# License
See LICENSE.md file for details.