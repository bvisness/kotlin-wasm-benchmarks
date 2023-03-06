import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.CustomChart
import jetbrains.buildServer.configs.kotlin.CustomChart.*
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.buildSteps.sshExec
import jetbrains.buildServer.configs.kotlin.buildTypeChartsOrder
import jetbrains.buildServer.configs.kotlin.buildTypeCustomChart

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2022.04"

data class ChartData(val name: String, val min: Double, val max: Double)

project {

    buildType(Kotlin_Benchmarks_Wasm_Main)

    features {
        val benchmarks = listOf(
            ChartData("microBenchmarks.AbstractMethodBenchmark.sortStrings", 0.005481, 0.09789),
            ChartData("microBenchmarks.AbstractMethodBenchmark.sortStringsWithComparator", 0.005419, 0.059602),
            ChartData("microBenchmarks.AllocationBenchmark.allocateObjects", 0.003096, 0.10427),
            ChartData("microBenchmarks.CallsBenchmark.classOpenMethodCall_BimorphicCallsite", 0.546158, 4.312828),
            ChartData("microBenchmarks.CallsBenchmark.classOpenMethodCall_MonomorphicCallsite", 0.229054, 1.477325),
            ChartData("microBenchmarks.CallsBenchmark.classOpenMethodCall_TrimorphicCallsite", 0.855081, 4.984422),
            ChartData("microBenchmarks.CallsBenchmark.finalMethodCall", 0.222047, 0.769698),
            ChartData("microBenchmarks.CallsBenchmark.interfaceMethodCall_BimorphicCallsite", 0.553198, 4.444791),
            ChartData("microBenchmarks.CallsBenchmark.interfaceMethodCall_HexamorphicCallsite", 2.424683, 7.725765),
            ChartData("microBenchmarks.CallsBenchmark.interfaceMethodCall_MonomorphicCallsite", 0.225347, 1.511616),
            ChartData("microBenchmarks.CallsBenchmark.interfaceMethodCall_TrimorphicCallsite", 0.793079, 5.162998),
            ChartData("microBenchmarks.CallsBenchmark.parameterBoxUnboxFolding", 0.220777, 11.306394),
            ChartData("microBenchmarks.CallsBenchmark.returnBoxUnboxFolding", 0.22587, 11.724544),
            ChartData("microBenchmarks.CastsBenchmark.classCast", 1.459269, 8.368862),
            ChartData("microBenchmarks.CastsBenchmark.interfaceCast", 1.357183, 11.343391),
            ChartData("microBenchmarks.ChainableBenchmark.testChainable", 3.776815, 8.434782),
            ChartData("microBenchmarks.ClassArrayBenchmark.copy", 0.022104, 0.135944),
            ChartData("microBenchmarks.ClassArrayBenchmark.copyManual", 0.055267, 0.287273),
            ChartData("microBenchmarks.ClassArrayBenchmark.countFiltered", 0.128702, 1.544961),
            ChartData("microBenchmarks.ClassArrayBenchmark.countFilteredLocal", 0.127503, 1.102941),
            ChartData("microBenchmarks.ClassArrayBenchmark.countFilteredManual", 0.126191, 1.696171),
            ChartData("microBenchmarks.ClassArrayBenchmark.filter", 0.12693, 1.477419),
            ChartData("microBenchmarks.ClassArrayBenchmark.filterAndCount", 0.127668, 1.613427),
            ChartData("microBenchmarks.ClassArrayBenchmark.filterAndMap", 0.162845, 1.82693),
            ChartData("microBenchmarks.ClassArrayBenchmark.filterAndMapManual", 0.157212, 1.857342),
            ChartData("microBenchmarks.ClassArrayBenchmark.filterManual", 0.128096, 1.525534),
            ChartData("microBenchmarks.ClassBaselineBenchmark.allocateArray", 0.005358, 0.126864),
            ChartData("microBenchmarks.ClassBaselineBenchmark.allocateArrayAndFill", 1.394553, 6.066703),
            ChartData("microBenchmarks.ClassBaselineBenchmark.allocateList", 8.0E-6, 0.017966),
            ChartData("microBenchmarks.ClassBaselineBenchmark.allocateListAndFill", 1.503269, 14.79339),
            ChartData("microBenchmarks.ClassBaselineBenchmark.allocateListAndWrite", 0.04967, 0.271948),
            ChartData("microBenchmarks.ClassBaselineBenchmark.consume", 0.718929, 7.089065),
            ChartData("microBenchmarks.ClassBaselineBenchmark.consumeField", 0.032107, 0.066512),
            ChartData("microBenchmarks.ClassListBenchmark.copy", 0.001651, 0.067566),
            ChartData("microBenchmarks.ClassListBenchmark.copyManual", 0.068824, 0.362624),
            ChartData("microBenchmarks.ClassListBenchmark.countFiltered", 0.206171, 1.19139),
            ChartData("microBenchmarks.ClassListBenchmark.countFilteredManual", 0.201332, 2.163339),
            ChartData("microBenchmarks.ClassListBenchmark.countWithLambda", 0.023561, 0.309786),
            ChartData("microBenchmarks.ClassListBenchmark.filter", 0.204549, 2.121485),
            ChartData("microBenchmarks.ClassListBenchmark.filterAndCount", 0.20434, 1.216108),
            ChartData("microBenchmarks.ClassListBenchmark.filterAndCountWithLambda", 0.055996, 0.342172),
            ChartData("microBenchmarks.ClassListBenchmark.filterAndMap", 0.236994, 1.358356),
            ChartData("microBenchmarks.ClassListBenchmark.filterAndMapManual", 0.235485, 2.423598),
            ChartData("microBenchmarks.ClassListBenchmark.filterAndMapWithLambda", 0.215129, 3.735264),
            ChartData("microBenchmarks.ClassListBenchmark.filterAndMapWithLambdaAsSequence", 0.171293, 3.448357),
            ChartData("microBenchmarks.ClassListBenchmark.filterManual", 0.202085, 1.725512),
            ChartData("microBenchmarks.ClassListBenchmark.filterWithLambda", 0.055987, 0.341032),
            ChartData("microBenchmarks.ClassListBenchmark.mapWithLambda", 0.199812, 5.540552),
            ChartData("microBenchmarks.ClassListBenchmark.reduce", 0.198424, 1.819754),
            ChartData("microBenchmarks.ClassStreamBenchmark.copy", 0.105917, 0.527385),
            ChartData("microBenchmarks.ClassStreamBenchmark.copyManual", 0.079457, 0.544727),
            ChartData("microBenchmarks.ClassStreamBenchmark.countFiltered", 0.206697, 1.19431),
            ChartData("microBenchmarks.ClassStreamBenchmark.countFilteredManual", 0.202336, 3.025851),
            ChartData("microBenchmarks.ClassStreamBenchmark.filter", 0.221067, 1.307983),
            ChartData("microBenchmarks.ClassStreamBenchmark.filterAndCount", 0.219342, 1.316433),
            ChartData("microBenchmarks.ClassStreamBenchmark.filterAndMap", 0.252068, 1.638464),
            ChartData("microBenchmarks.ClassStreamBenchmark.filterAndMapManual", 0.231929, 1.243569),
            ChartData("microBenchmarks.ClassStreamBenchmark.filterManual", 0.19976, 1.706332),
            ChartData("microBenchmarks.ClassStreamBenchmark.reduce", 0.197797, 1.170258),
            ChartData("microBenchmarks.CompanionObjectBenchmark.invokeRegularFunction", 5.0E-6, 1.7E-5),
            ChartData("microBenchmarks.DefaultArgumentBenchmark.testEightOfEight", 4.0E-6, 1.1E-5),
            ChartData("microBenchmarks.DefaultArgumentBenchmark.testFourOfFour", 4.0E-6, 1.2E-5),
            ChartData("microBenchmarks.DefaultArgumentBenchmark.testOneOfEight", 5.0E-6, 1.4E-5),
            ChartData("microBenchmarks.DefaultArgumentBenchmark.testOneOfFour", 5.0E-6, 1.4E-5),
            ChartData("microBenchmarks.DefaultArgumentBenchmark.testOneOfTwo", 4.0E-6, 1.3E-5),
            ChartData("microBenchmarks.DefaultArgumentBenchmark.testTwoOfTwo", 4.0E-6, 1.3E-5),
            ChartData("microBenchmarks.ElvisBenchmark.testCompositeElvis", 0.074098, 0.432417),
            ChartData("microBenchmarks.ElvisBenchmark.testElvis", 0.018459, 0.069948),
            ChartData("microBenchmarks.EulerBenchmark.problem1", 0.007258, 0.015926),
            ChartData("microBenchmarks.EulerBenchmark.problem14", 0.630932, 1.853664),
            ChartData("microBenchmarks.EulerBenchmark.problem14full", 1.925536, 4.972912),
            ChartData("microBenchmarks.EulerBenchmark.problem1bySequence", 0.017116, 0.232822),
            ChartData("microBenchmarks.EulerBenchmark.problem2", 1.82E-4, 0.001947),
            ChartData("microBenchmarks.EulerBenchmark.problem8", 0.033005, 1.860915),
            ChartData("microBenchmarks.EulerBenchmark.problem9", 0.542539, 286.768589),
            ChartData("microBenchmarks.FibonacciBenchmark.calc", 0.002212, 0.143814),
            ChartData("microBenchmarks.FibonacciBenchmark.calcClassic", 0.002268, 0.142059),
            ChartData("microBenchmarks.FibonacciBenchmark.calcWithProgression", 0.002261, 0.142304),
            ChartData("microBenchmarks.ForLoopsBenchmark.arrayIndicesLoop", 0.008859, 0.282455),
            ChartData("microBenchmarks.ForLoopsBenchmark.arrayLoop", 0.005399, 0.270291),
            ChartData("microBenchmarks.ForLoopsBenchmark.charArrayIndicesLoop", 0.00616, 0.271233),
            ChartData("microBenchmarks.ForLoopsBenchmark.charArrayLoop", 0.003491, 0.26594),
            ChartData("microBenchmarks.ForLoopsBenchmark.floatArrayIndicesLoop", 0.004222, 0.017807),
            ChartData("microBenchmarks.ForLoopsBenchmark.floatArrayLoop", 0.004055, 0.015825),
            ChartData("microBenchmarks.ForLoopsBenchmark.intArrayIndicesLoop", 0.006166, 0.282684),
            ChartData("microBenchmarks.ForLoopsBenchmark.intArrayLoop", 0.00371, 0.268586),
            ChartData("microBenchmarks.ForLoopsBenchmark.stringIndicesLoop", 0.021142, 1.183833),
            ChartData("microBenchmarks.ForLoopsBenchmark.stringLoop", 0.015113, 1.152766),
            ChartData("microBenchmarks.ForLoopsBenchmark.uIntArrayIndicesLoop", 0.006735, 0.477855),
            ChartData("microBenchmarks.ForLoopsBenchmark.uIntArrayLoop", 0.033109, 0.515602),
            ChartData("microBenchmarks.ForLoopsBenchmark.uLongArrayIndicesLoop", 0.006575, 0.233181),
            ChartData("microBenchmarks.ForLoopsBenchmark.uLongArrayLoop", 0.03125, 0.267999),
            ChartData("microBenchmarks.ForLoopsBenchmark.uShortArrayIndicesLoop", 0.006646, 0.490236),
            ChartData("microBenchmarks.ForLoopsBenchmark.uShortArrayLoop", 0.038436, 0.536562),
            ChartData("microBenchmarks.InheritanceBenchmark.baseCalls", 0.234073, 8.179977),
            ChartData("microBenchmarks.InlineBenchmark.calculate", 0.002233, 0.004027),
            ChartData("microBenchmarks.InlineBenchmark.calculateGeneric", 0.003071, 0.082291),
            ChartData("microBenchmarks.InlineBenchmark.calculateGenericInline", 0.019447, 0.189992),
            ChartData("microBenchmarks.InlineBenchmark.calculateInline", 0.002234, 0.00408),
            ChartData("microBenchmarks.IntArrayBenchmark.copy", 0.055667, 0.470298),
            ChartData("microBenchmarks.IntArrayBenchmark.copyManual", 0.054941, 0.472885),
            ChartData("microBenchmarks.IntArrayBenchmark.countFiltered", 0.126915, 1.960141),
            ChartData("microBenchmarks.IntArrayBenchmark.countFilteredLocal", 0.122024, 1.986076),
            ChartData("microBenchmarks.IntArrayBenchmark.countFilteredManual", 0.124203, 1.992371),
            ChartData("microBenchmarks.IntArrayBenchmark.countFilteredPrime", 0.078547, 0.15113),
            ChartData("microBenchmarks.IntArrayBenchmark.countFilteredPrimeManual", 0.078598, 0.150663),
            ChartData("microBenchmarks.IntArrayBenchmark.countFilteredSome", 0.009967, 0.022509),
            ChartData("microBenchmarks.IntArrayBenchmark.countFilteredSomeLocal", 0.010133, 0.022507),
            ChartData("microBenchmarks.IntArrayBenchmark.countFilteredSomeManual", 0.010134, 0.022394),
            ChartData("microBenchmarks.IntArrayBenchmark.filter", 0.123148, 2.053023),
            ChartData("microBenchmarks.IntArrayBenchmark.filterAndCount", 0.122569, 1.949856),
            ChartData("microBenchmarks.IntArrayBenchmark.filterAndMap", 0.127182, 1.943237),
            ChartData("microBenchmarks.IntArrayBenchmark.filterAndMapManual", 0.12199, 3.558802),
            ChartData("microBenchmarks.IntArrayBenchmark.filterManual", 0.121456, 2.001791),
            ChartData("microBenchmarks.IntArrayBenchmark.filterPrime", 0.091205, 0.17268),
            ChartData("microBenchmarks.IntArrayBenchmark.filterSome", 0.026561, 0.139631),
            ChartData("microBenchmarks.IntArrayBenchmark.filterSomeAndCount", 0.026706, 0.144576),
            ChartData("microBenchmarks.IntArrayBenchmark.filterSomeManual", 0.026665, 0.177289),
            ChartData("microBenchmarks.IntArrayBenchmark.reduce", 0.123054, 2.009122),
            ChartData("microBenchmarks.IntBaselineBenchmark.allocateArray", 0.001195, 0.012016),
            ChartData("microBenchmarks.IntBaselineBenchmark.allocateArrayAndFill", 0.005201, 0.018817),
            ChartData("microBenchmarks.IntBaselineBenchmark.allocateList", 7.0E-6, 0.018105),
            ChartData("microBenchmarks.IntBaselineBenchmark.consume", 0.008049, 0.028643),
            ChartData("microBenchmarks.IntListBenchmark.copy", 0.00145, 0.070524),
            ChartData("microBenchmarks.IntListBenchmark.copyManual", 0.097136, 0.484119),
            ChartData("microBenchmarks.IntListBenchmark.countFiltered", 0.17054, 2.035556),
            ChartData("microBenchmarks.IntListBenchmark.countFilteredLocal", 0.166655, 2.106564),
            ChartData("microBenchmarks.IntListBenchmark.countFilteredManual", 0.16672, 2.039235),
            ChartData("microBenchmarks.IntListBenchmark.filter", 0.164359, 2.031161),
            ChartData("microBenchmarks.IntListBenchmark.filterAndCount", 0.168979, 2.404262),
            ChartData("microBenchmarks.IntListBenchmark.filterAndMap", 0.175829, 2.306783),
            ChartData("microBenchmarks.IntListBenchmark.filterAndMapManual", 0.175166, 2.040847),
            ChartData("microBenchmarks.IntListBenchmark.filterManual", 0.173577, 2.040424),
            ChartData("microBenchmarks.IntListBenchmark.reduce", 0.166863, 2.370757),
            ChartData("microBenchmarks.IntStreamBenchmark.copyManual", 0.059831, 0.808142),
            ChartData("microBenchmarks.IntStreamBenchmark.countFiltered", 0.121476, 2.159468),
            ChartData("microBenchmarks.IntStreamBenchmark.countFilteredLocal", 0.122164, 2.803637),
            ChartData("microBenchmarks.IntStreamBenchmark.countFilteredManual", 0.123462, 2.205338),
            ChartData("microBenchmarks.IntStreamBenchmark.filter", 0.171356, 2.302764),
            ChartData("microBenchmarks.IntStreamBenchmark.filterAndCount", 0.163864, 2.301182),
            ChartData("microBenchmarks.IntStreamBenchmark.filterAndMap", 0.1649, 2.987022),
            ChartData("microBenchmarks.IntStreamBenchmark.filterAndMapManual", 0.12288, 2.191785),
            ChartData("microBenchmarks.IntStreamBenchmark.filterManual", 0.122005, 2.138953),
            ChartData("microBenchmarks.IntStreamBenchmark.reduce", 0.121627, 2.19233),
            ChartData("microBenchmarks.LambdaBenchmark.capturingLambda", 0.002245, 0.004088),
            ChartData("microBenchmarks.LambdaBenchmark.capturingLambdaNoInline", 0.002616, 0.290231),
            ChartData("microBenchmarks.LambdaBenchmark.methodReference", 0.002269, 0.006452),
            ChartData("microBenchmarks.LambdaBenchmark.methodReferenceNoInline", 0.005933, 0.261744),
            ChartData("microBenchmarks.LambdaBenchmark.mutatingLambda", 0.00399, 0.009289),
            ChartData("microBenchmarks.LambdaBenchmark.mutatingLambdaNoInline", 0.009872, 0.367196),
            ChartData("microBenchmarks.LambdaBenchmark.noncapturingLambda", 0.002287, 0.006044),
            ChartData("microBenchmarks.LambdaBenchmark.noncapturingLambdaNoInline", 0.003123, 0.2863),
            ChartData("microBenchmarks.LocalObjectsBenchmark.localArray", 8.1E-5, 0.00146),
            ChartData("microBenchmarks.LoopBenchmark.arrayForeachLoop", 0.010071, 0.057743),
            ChartData("microBenchmarks.LoopBenchmark.arrayIndexLoop", 0.039202, 0.058413),
            ChartData("microBenchmarks.LoopBenchmark.arrayListForeachLoop", 0.056488, 0.349336),
            ChartData("microBenchmarks.LoopBenchmark.arrayListLoop", 0.055186, 0.33852),
            ChartData("microBenchmarks.LoopBenchmark.arrayLoop", 0.034042, 0.067251),
            ChartData("microBenchmarks.LoopBenchmark.arrayWhileLoop", 0.038181, 0.060727),
            ChartData("microBenchmarks.LoopBenchmark.rangeLoop", 0.008652, 0.02946),
            ChartData("microBenchmarks.MatrixMapBenchmark.add", 0.822286, 2.508575),
            ChartData("microBenchmarks.ParameterNotNullAssertionBenchmark.invokeEightArgsWithNullCheck", 6.0E-6, 3.4E-5),
            ChartData("microBenchmarks.ParameterNotNullAssertionBenchmark.invokeEightArgsWithoutNullCheck", 7.0E-6, 3.3E-5),
            ChartData("microBenchmarks.ParameterNotNullAssertionBenchmark.invokeOneArgWithNullCheck", 4.0E-6, 1.5E-5),
            ChartData("microBenchmarks.ParameterNotNullAssertionBenchmark.invokeOneArgWithoutNullCheck", 4.0E-6, 1.3E-5),
            ChartData("microBenchmarks.ParameterNotNullAssertionBenchmark.invokeTwoArgsWithNullCheck", 5.0E-6, 1.8E-5),
            ChartData("microBenchmarks.ParameterNotNullAssertionBenchmark.invokeTwoArgsWithoutNullCheck", 5.0E-6, 1.7E-5),
            ChartData("microBenchmarks.PrimeListBenchmark.calcDirect", 0.203864, 1.161403),
            ChartData("microBenchmarks.SingletonBenchmark.access", 0.01089, 0.035927),
            ChartData("microBenchmarks.StringBenchmark.stringBuilderConcat", 0.057188, 0.173431),
            ChartData("microBenchmarks.StringBenchmark.stringBuilderConcatNullable", 0.058393, 0.174658),
            ChartData("microBenchmarks.SwitchBenchmark.testConstSwitch", 0.014693, 0.074527),
            ChartData("microBenchmarks.SwitchBenchmark.testDenseEnumsSwitch", 0.017642, 0.521803),
            ChartData("microBenchmarks.SwitchBenchmark.testDenseIntSwitch", 0.014631, 0.075223),
            ChartData("microBenchmarks.SwitchBenchmark.testEnumsSwitch", 0.020316, 0.808218),
            ChartData("microBenchmarks.SwitchBenchmark.testObjConstSwitch", 0.063053, 0.280266),
            ChartData("microBenchmarks.SwitchBenchmark.testSealedWhenSwitch", 0.030262, 0.144783),
            ChartData("microBenchmarks.SwitchBenchmark.testSparseIntSwitch", 0.015263, 0.072568),
            ChartData("microBenchmarks.SwitchBenchmark.testStringsDifficultSwitch", 0.866504, 1.866335),
            ChartData("microBenchmarks.SwitchBenchmark.testStringsSwitch", 0.139299, 0.360802),
            ChartData("microBenchmarks.SwitchBenchmark.testVarSwitch", 0.052969, 0.446365),
            ChartData("microBenchmarks.WithIndiciesBenchmark.withIndicies", 0.18563, 1.465346),
            ChartData("microBenchmarks.WithIndiciesBenchmark.withIndiciesManual", 0.191304, 1.186174),
            ChartData("microBenchmarks.JsInteropBenchmark.externInteropIn", 0.025832, 0.030845),
            ChartData("microBenchmarks.JsInteropBenchmark.externInteropOut", 0.308168, 1.140727),
            ChartData("microBenchmarks.JsInteropBenchmark.intInteropIn", 0.022539, 0.030408),
            ChartData("microBenchmarks.JsInteropBenchmark.intInteropOut", 0.03616, 0.050464),
            ChartData("microBenchmarks.JsInteropBenchmark.simpleInterop", 0.020481, 0.02943),
            ChartData("microBenchmarks.JsInteropBenchmark.stringInteropIn", 1.211468, 4.791487),
            ChartData("microBenchmarks.JsInteropBenchmark.stringInteropOut", 3.866138, 7.043182),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.externInteropInNotNull", 0.024334, 0.030991),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.externInteropInNotNull2Params", 0.029217, 0.031813),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.externInteropInNull", 0.024177, 0.030122),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.externInteropOutNotNull", 0.310033, 1.25705),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.externInteropOutNull", 0.093969, 0.147258),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.intInteropInNotNull", 0.107692, 0.288679),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.intInteropInNotNull2Params", 0.223515, 0.563044),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.intInteropInNull", 0.036881, 0.037816),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.intInteropOutNotNull", 0.118016, 0.53644),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.intInteropOutNull", 0.043827, 0.07076),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.stringInteropInNotNull", 1.317295, 4.891457),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.stringInteropInNotNull2Params", 2.755156, 9.770266),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.stringInteropInNull", 0.029472, 0.038248),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.stringInteropOutNotNull", 3.921246, 7.16919),
            ChartData("microBenchmarks.JsInteropNullableBenchmark.stringInteropOutNull", 0.040254, 0.08201),
            ChartData("microBenchmarks.ArrayCopyBenchmark.copyInSameArray", 1782.224, 14500.079616),
            ChartData("microBenchmarks.BunnymarkBenchmark.testBunnymark", 252.22, 2838.535936),
            ChartData("microBenchmarks.CoordinatesSolverBenchmark.solve", 376.121, 1967.742976),
            ChartData("microBenchmarks.EulerBenchmark.problem4", 233.483, 2223.421952),
            ChartData("microBenchmarks.FibonacciBenchmark.calcSquare", 20.085, 10246.154752),
            ChartData("microBenchmarks.LinkedListWithAtomicsBenchmark.ensureNext", 70.458, 760.568064),
            ChartData("microBenchmarks.PrimeListBenchmark.calcEratosthenes", 278.295, 1369.665024),
            ChartData("microBenchmarks.StringBenchmark.stringConcat", 0.020992, 0.151808),
            ChartData("microBenchmarks.StringBenchmark.stringConcatNullable", 0.087, 0.40704),
            ChartData("microBenchmarks.StringBenchmark.summarizeSplittedCsv", 0.857, 23.47392),
            ChartData("microBenchmarks.superslow.GraphSolverBenchmark.solve", 46.686, 243.547136),
            ChartData("macroBenchmarks.MacroBenchmarksSlow.cd", 155.804, 439.718912),
            ChartData("macroBenchmarks.MacroBenchmarksSlow.havlak", 789.364, 3748.744192),
            ChartData("macroBenchmarks.MacroBenchmarksSlow.json", 0.504, 3.94624),
            ChartData("macroBenchmarks.MacroBenchmarksSlow.mandelbrot", 60.343, 69.850112),
            ChartData("macroBenchmarks.MacroBenchmarksSlow.nBody", 10.226, 21.23136),
            ChartData("macroBenchmarks.MacroBenchmarksFast.bounce", 0.009559, 0.040045),
            ChartData("macroBenchmarks.MacroBenchmarksFast.list", 0.009217, 0.060104),
            ChartData("macroBenchmarks.MacroBenchmarksFast.permute", 0.020376, 0.058235),
            ChartData("macroBenchmarks.MacroBenchmarksFast.queens", 0.02422, 0.05347),
            ChartData("macroBenchmarks.MacroBenchmarksFast.sieve", 0.010572, 0.137879),
            ChartData("macroBenchmarks.MacroBenchmarksFast.storage", 0.076074, 1.194509),
            ChartData("macroBenchmarks.MacroBenchmarksFast.towers", 0.043293, 0.124575),
        )

        fun String.toId() = "id_benchmark_$this"

        fun addBenchmarkChart(name: String, min: Double, max: Double) {
            val margins = (max - min)
            buildTypeCustomChart {
                id = name.toId()
                title = name
                seriesTitle = "Serie"
                format = CustomChart.Format.TEXT
                series = listOf(
                    Serie(title = "js_v8", key = SeriesKey("js_$name")),
                    Serie(title = "js_sm", key = SeriesKey("jsShell_js_$name")),
                    Serie(title = "wasm_v8", key = SeriesKey("wasm_$name")),
                    Serie(title = "wasm_sm", key = SeriesKey("jsShell_wasm_$name")),
                    Serie(title = "wasmOpt_v8", key = SeriesKey("wasmOpt_$name")),
                )
                param("properties.axis.y.min", (min - margins).coerceAtLeast(-margins / 10.0).toString())
                param("properties.axis.y.type", "default")
                param("properties.axis.y.max", (max + margins).toString())
            }
        }

        for (benchmark in benchmarks) {
            addBenchmarkChart(benchmark.name, benchmark.min, benchmark.max)
        }

        addBenchmarkChart("bundleSize", 0.0, 21403263.0)

        buildTypeChartsOrder {
            id = "PROJECT_EXT_2453"
            order = benchmarks.map { it.name.toId() }
        }
    }
}

object Kotlin_Benchmarks_Wasm_Main : BuildType({
    id("Main")
    name = "Main"

    artifactRules = "build/reports/benchmarks => reports"
    publishArtifacts = PublishMode.SUCCESSFUL

    params {
        param("kotlin-version", "%dep.Kotlin_KotlinDev_CompilerDistAndMavenArtifacts.build.number%")
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            scriptContent = "find ./kotlin-compiler -type f"
        }

        gradle {
            name = "clean"
            tasks = ":clean"
            gradleParams = "-Pkotlin_version=%kotlin-version%"
        }
        gradle {
            name = "wasmBenchmark_v8"
            tasks = ":wasmFastMicroBenchmark :wasmSlowMicroBenchmark :wasmFastMacroBenchmark :wasmSlowMacroBenchmark"
            gradleParams = "--rerun-tasks -Pkotlin_version=%kotlin-version%"
        }
        gradle {
            name = "wasmBenchmark_sm"
            tasks = ":jsShell_wasmFastMicroBenchmark :jsShell_wasmSlowMicroBenchmark :jsShell_wasmFastMacroBenchmark :jsShell_wasmSlowMacroBenchmark"
            gradleParams = "--rerun-tasks -Pkotlin_version=%kotlin-version%"
        }
//        gradle {
//            name = "wasmOptBenchmark_v8"
//            tasks = ":wasmOptFastMicroBenchmark :wasmOptSlowMicroBenchmark :wasmOptFastMacroBenchmark :wasmOptSlowMacroBenchmark"
//            gradleParams = "--rerun-tasks -Pkotlin_version=%kotlin-version%"
//        }
        gradle {
            name = "jsBenchmark_v8"
            tasks = ":jsFastMicroBenchmark :jsSlowMicroBenchmark :jsFastMacroBenchmark :jsSlowMacroBenchmark"
            gradleParams = "--rerun-tasks -Pkotlin_version=%kotlin-version%"
        }
        gradle {
            name = "jsBenchmark_sm"
            tasks = ":jsShell_jsFastMicroBenchmark :jsShell_jsSlowMicroBenchmark :jsShell_jsFastMacroBenchmark :jsShell_jsSlowMacroBenchmark"
            gradleParams = "--rerun-tasks -Pkotlin_version=%kotlin-version%"
        }
        gradle {
            name = "reportAllTargetsToTC"
            tasks = ":reportAllTargetsToTC"
            gradleParams = "-Pkotlin_version=%kotlin-version%"
        }
    }

    dependencies {
        artifacts(AbsoluteId("Kotlin_KotlinDev_CompilerDistAndMavenArtifacts")) {
            buildRule = lastSuccessful()
            artifactRules = "maven.zip!**=>kotlin-compiler"
        }
    }

    requirements {
        equals("teamcity.agent.name", "kotlin-linux-x64-wasm-js-perf-munit788")
    }

    cleanup {
        keepRule {
            id = "Keep Artifacts for the 5 years"
            dataToKeep = artifacts()
            keepAtLeast = days(5 * 365)
            applyToBuilds {
                inBranches {
                    branchFilter = patterns(
                        "+:<default>"
                    )
                }
            }
        }
    }
})
