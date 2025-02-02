# Kotlin Wasm Benchmarks

[![JetBrains team project](https://jb.gg/badges/team.svg?style=flat)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![GitHub license](https://img.shields.io/badge/license-BSD%20License%202.0-blue.svg?style=flat)](http://www.opensource.org/licenses/bsd-license.php)
[![GitHub license](https://img.shields.io/badge/license-MIT%20License%202.0-blue.svg?style=flat)](https://opensource.org/license/mit/)

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

Kotlin Multiplatform Collection of Benchmarks focused on Kotlin/Wasm performance.

![kotlin-wasm-macro-benchmarks.png](screenshots/kotlin-wasm-macro-benchmarks.png)

![compose-multiplatform-benchmarks.png](screenshots/compose-multiplatform-benchmarks.png)
_Based on data from [Compose Multiplatform Benchmarks](https://github.com/JetBrains/compose-multiplatform/tree/bench_with_kwasm/benchmarks/ios/jvm-vs-kotlin-native)_

# Description
These benchmarks are based on [are-we-fast-yet](https://github.com/smarr/are-we-fast-yet) benchmarks collection and JetBrains/Kotlin micro-benchmarks (work-in-progress).
To perform benchmarks it uses [kotlinx-benchmarks](https://github.com/Kotlin/kotlinx-benchmark) library.

# Build and Run
Specify Kotlin version in `gradle.properties` file or use additional gradle argument `-Pkotlin_version=1.8.0`.

### To run All benchmarks in V8:
`./gradlew benchmark`

### To run All Kotlin/Wasm benchmarks in V8:

`./gradlew wasmBenchmark`

### To run All Kotlin/Wasm benchmarks with binaries optimized by [Binaryen](https://github.com/WebAssembly/binaryen) in V8:

`./gradlew wasmOptBenchmark`

### To run All Kotlin/JS benchmarks in V8:

`./gradlew jsBenchmark`

### To see all tasks:

`./gradlew tasks`

### Result output is located in `build/reports` directory.

# License
See LICENSE.md file for details.
