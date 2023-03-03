import kotlinx.benchmark.gradle.*
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.de.undercouch.gradle.tasks.download.Download
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsBinaryMode
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmSubTargetContainerDsl
import org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget

//import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
//import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin

buildscript {
    repositories {
        gradlePluginPortal()
        maven(uri("./kotlin-compiler"))
    }

    dependencies {
        classpath(files("./kotlinx-benchmarks/kotlinx-benchmark-plugin-0.4.7.jar"))
        classpath("com.squareup:kotlinpoet:1.3.0")
    }
}

plugins {
    kotlin("multiplatform")
}

apply {
    plugin<BenchmarksPlugin>()
}

//with(NodeJsRootPlugin.apply(rootProject)) {
//    nodeVersion = "19.0.0-nightly202206017ad5b420ae"
//    nodeDownloadBaseUrl = "https://nodejs.org/download/nightly/"
//}
//
//with(YarnPlugin.apply(rootProject)) {
//    command = "echo"
//    download = false
//}

//with(D8RootPlugin.apply(rootProject)) {
//    version = "10.7.22"
//}

repositories {
    mavenCentral()
    maven(uri("./kotlin-compiler"))
}

kotlin {
    js(IR) {
        this as KotlinJsIrTarget
        d8()
        //nodejs()
    }
    wasm {
        d8()
        //nodejs()
    }

    wasm("wasmOpt") {
        d8()
        //nodejs()
        applyBinaryen()
    }

    sourceSets.all {
        languageSettings {
            progressiveMode = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(files("./kotlinx-benchmarks/kotlinx-benchmark-runtime-0.4.7.jar"))
            }
        }

        val wasmMain by getting {
            dependencies {
                implementation(files("./kotlinx-benchmarks/kotlinx-benchmark-runtime-wasm-0.4.7.klib"))
            }
        }

        val wasmOptMain by getting {
            dependencies {
                implementation(files("./kotlinx-benchmarks/kotlinx-benchmark-runtime-wasm-0.4.7.klib"))
                kotlin.srcDirs("$rootDir/src")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(files("./kotlinx-benchmarks/kotlinx-benchmark-runtime-jsir-0.4.7.klib"))
            }
        }
    }
}

fun reportToTC(csvFile: File, targetName: String) {
    csvFile.readLines().forEachIndexed { i, line ->
        if (i == 0) return@forEachIndexed
        val dataLine = line.split(',')
        val benchmarkName = dataLine[0].replace("\"", "")
        val score = dataLine[4]
        val valueTypeKey = "${targetName}_$benchmarkName"
        println("##teamcity[buildStatisticValue key='$valueTypeKey' value='$score']")
    }
}


val reportAllTargetsToTC = tasks.register("reportAllTargetsToTC")
fun createReportTargetToTC(reportDir: File, targetName: String) {
    val teamcityReport by tasks.register("${targetName}ReportResultsForTC") {
        doLast {
            val fileName = "${targetName}.csv"
            fileTree(reportDir).visit {
                if (file.isFile && file.name == fileName) {
                    reportToTC(file, targetName)
                }
            }
        }
    }
    reportAllTargetsToTC.configure {
        dependsOn(teamcityReport)
    }
}

fun registerReportBundleSizes(bundleSizeList: List<String>) {
    val teamcityReport = tasks.register("reportBundleSizes") {
        doLast {
            for (target in bundleSizeList) {
                val compileSyncDir = project.buildDir.resolve("compileSync").resolve(target)
                val score = compileSyncDir.walk().sumOf {
                    val isBundleFile = it.extension.let { ext -> ext == "js" || ext == "wasm" || ext == "mjs" }
                    if (isBundleFile) it.length() else 0
                }
                val valueTypeKey = "${target}_bundleSize"
                println("##teamcity[buildStatisticValue key='$valueTypeKey' value='$score']")
            }
        }
    }
    reportAllTargetsToTC.configure {
        dependsOn(teamcityReport)
    }
}

benchmark {
    configurations {
        with(create("fastMacro")) {
            iterations = 5
            warmups = 5
            iterationTime = 50
            iterationTimeUnit = "millis"
            outputTimeUnit = "millis"
            reportFormat = "csv"
            mode = "avgt"
            advanced("jsUseBridge", true)
            includes.add("macroBenchmarks.MacroBenchmarksFast")
        }
        with(create("slowMacro")) {
            iterations = 1
            warmups = 5
            iterationTime = 0
            iterationTimeUnit = "millis"
            outputTimeUnit = "millis"
            reportFormat = "csv"
            mode = "avgt"
            advanced("jsUseBridge", true)
            includes.add("macroBenchmarks.MacroBenchmarksSlow")
        }
        val slowMicroBenchmarks = listOf(
            "microBenchmarks.StringBenchmark.summarizeSplittedCsv",
            "microBenchmarks.PrimeListBenchmark.calcEratosthenes",
            "microBenchmarks.FibonacciBenchmark.calcSquare",
            "microBenchmarks.superslow.GraphSolverBenchmark.solve",
            "microBenchmarks.LinkedListWithAtomicsBenchmark.ensureNext",
            "microBenchmarks.StringBenchmark.stringConcat",
            "microBenchmarks.StringBenchmark.stringConcatNullable",
            "microBenchmarks.EulerBenchmark.problem4",
            "microBenchmarks.ArrayCopyBenchmark.copyInSameArray",
            "microBenchmarks.BunnymarkBenchmark.testBunnymark",
            "microBenchmarks.CoordinatesSolverBenchmark.solve",
        )
        with(create("fastMicro")) {
            iterations = 5
            warmups = 5
            iterationTime = 50
            iterationTimeUnit = "millis"
            outputTimeUnit = "millis"
            reportFormat = "csv"
            mode = "avgt"
            advanced("jsUseBridge", true)
            includes.add("microBenchmarks")
            excludes.addAll(slowMicroBenchmarks)
        }
        with(create("slowMicro")) {
            iterations = 1
            warmups = 5
            iterationTime = 0
            iterationTimeUnit = "millis"
            outputTimeUnit = "millis"
            reportFormat = "csv"
            mode = "avgt"
            advanced("jsUseBridge", true)
            includes.addAll(slowMicroBenchmarks)
        }
    }

    val reportDir = project.buildDir.resolve(reportsDir)
    targets {
        val bundleSizeList = mutableListOf<String>()
        register("wasm") {
            createReportTargetToTC(reportDir, name)
            bundleSizeList.add(name)
            createReportTargetToTC(reportDir, "jsShell_${name}")
            bundleSizeList.add("jsShell_${name}")
        }
        register("wasmOpt") {
            createReportTargetToTC(reportDir, name)
            bundleSizeList.add(name)
        }
        register("js") {
            (this as JsBenchmarkTarget).jsBenchmarksExecutor = JsBenchmarksExecutor.BuiltIn
            createReportTargetToTC(reportDir, name)
            bundleSizeList.add(name)
            createReportTargetToTC(reportDir, "jsShell_${name}")
            bundleSizeList.add("jsShell_${name}")
        }
        registerReportBundleSizes(bundleSizeList)
    }
}


/////////////////////////

enum class OsName { WINDOWS, MAC, LINUX, UNKNOWN }
enum class OsArch { X86_32, X86_64, ARM64, UNKNOWN }
data class OsType(val name: OsName, val arch: OsArch)

val currentOsType = run {
    val gradleOs = OperatingSystem.current()
    val osName = when {
        gradleOs.isMacOsX -> OsName.MAC
        gradleOs.isWindows -> OsName.WINDOWS
        gradleOs.isLinux -> OsName.LINUX
        else -> OsName.UNKNOWN
    }

    val osArch = when (providers.systemProperty("sun.arch.data.model").forUseAtConfigurationTime().get()) {
        "32" -> OsArch.X86_32
        "64" -> when (providers.systemProperty("os.arch").forUseAtConfigurationTime().get().toLowerCase()) {
            "aarch64" -> OsArch.ARM64
            else -> OsArch.X86_64
        }
        else -> OsArch.UNKNOWN
    }

    OsType(osName, osArch)
}

val jsShellDirectory = "https://archive.mozilla.org/pub/firefox/nightly/2023/03/2023-03-02-21-22-31-mozilla-central"
val jsShellSuffix = when (currentOsType) {
    OsType(OsName.LINUX, OsArch.X86_32) -> "linux-i686"
    OsType(OsName.LINUX, OsArch.X86_64) -> "linux-x86_64"
    OsType(OsName.MAC, OsArch.X86_64),
    OsType(OsName.MAC, OsArch.ARM64) -> "mac"
    OsType(OsName.WINDOWS, OsArch.X86_32) -> "win32"
    OsType(OsName.WINDOWS, OsArch.X86_64) -> "win64"
    else -> error("unsupported os type $currentOsType")
}
val jsShellLocation = "$jsShellDirectory/jsshell-$jsShellSuffix.zip"

val downloadedTools = File(buildDir, "tools")

val downloadJsShell = tasks.register("jsShellDownload", Download::class) {
    src(jsShellLocation)
    dest(File(downloadedTools, "jsshell-$jsShellSuffix.zip"))
    overwrite(false)
}

val unzipJsShell = tasks.register("jsShellUnzip", Copy::class) {
    dependsOn(downloadJsShell)
    from(zipTree(downloadJsShell.get().dest))
    val unpackedDir = File(downloadedTools, "jsshell-$jsShellSuffix")
    into(unpackedDir)
}

fun Project.getExecutableFile(compilation: KotlinJsCompilation): Provider<RegularFile> {
    val executableFile = when (val kotlinTarget = compilation.target) {
        is KotlinJsIrTarget -> {
            val binary = kotlinTarget.binaries.executable(compilation)
                .first { it.mode == KotlinJsBinaryMode.PRODUCTION } as JsIrBinary
            val outputFile = binary.linkTask.flatMap { it.outputFileProperty }
            val destinationDir = binary.linkSyncTask.map { it.destinationDir }
            destinationDir.zip(outputFile) { dir, file -> dir.resolve(file.name) }
        }
        else -> compilation.compileKotlinTaskProvider.flatMap { it.outputFileProperty }
    }
    return project.layout.file(executableFile)
}


fun Project.createJsShellExec(
    config: BenchmarkConfiguration,
    target: BenchmarkTarget,
    compilation: KotlinJsIrCompilation,
    taskName: String
): TaskProvider<Exec> = tasks.register(taskName, Exec::class) {
    dependsOn(compilation.runtimeDependencyFiles)
    dependsOn(unzipJsShell)
    dependsOn(compilation.compileKotlinTaskName)

    group = BenchmarksPlugin.BENCHMARKS_TASK_GROUP
    description = "Executes benchmark for '${target.name}' with jsShell"

    val newArgs = mutableListOf<String>()
    executable = File(unzipJsShell.get().destinationDir, "js").absolutePath

    newArgs.add("--wasm-gc")
    newArgs.add("--wasm-function-references")
    newArgs.add("--wasm-compiler=ion")

    val inputFile = getExecutableFile(compilation).get().asFile
    workingDir = inputFile.parentFile
    if (compilation.target.platformType == KotlinPlatformType.wasm) {
        newArgs.add("--module=${inputFile.absolutePath}")
    } else {
        newArgs.add("--file=${inputFile.absolutePath}")
    }
    val reportFile = setupReporting(target, config)
    val jsShellReportFile = File(reportFile.parentFile, "jsShell_" + reportFile.name)
    newArgs.add("--")
    newArgs.add(writeParameters(target.name, jsShellReportFile, traceFormat(), config).absolutePath)
    args = newArgs
    standardOutput = ConsoleAndFilesOutputStream()
}


fun Project.createJsEngineBenchmarkExecTask(
    config: BenchmarkConfiguration,
    target: BenchmarkTarget,
    compilation: KotlinJsCompilation
) {
    val taskName = "jsShell_${target.name}${config.capitalizedName()}${BenchmarksPlugin.BENCHMARK_EXEC_SUFFIX}"
    val compilationTarget = compilation.target
    if (compilationTarget is KotlinWasmSubTargetContainerDsl) {
        check(compilation is KotlinJsIrCompilation) { "Legacy Kotlin/JS is does not supported by JsShell engine" }
        val execTask = createJsShellExec(config, target, compilation, taskName)
        tasks.getByName(config.prefixName(BenchmarksPlugin.RUN_BENCHMARKS_TASKNAME)).dependsOn(execTask)
    }
}

afterEvaluate {
    val extension = extensions.getByName(BenchmarksPlugin.BENCHMARK_EXTENSION_NAME) as BenchmarksExtension
    extension.targets.forEach { target ->
        val compilation = when (target) {
            is WasmBenchmarkTarget -> target.compilation
            is JsBenchmarkTarget -> target.compilation
            else -> null
        }
        if (compilation != null) {
            target.extension.configurations.forEach { config ->
                val benchmarkCompilation = compilation.target.compilations.maybeCreate(BenchmarksPlugin.BENCHMARK_COMPILATION_NAME) as KotlinJsCompilation
                createJsEngineBenchmarkExecTask(config, target, benchmarkCompilation)
            }
        }
    }
}