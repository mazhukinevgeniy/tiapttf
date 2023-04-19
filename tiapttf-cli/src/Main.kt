import homemade.cli.Controller
import homemade.game.fieldstructure.FieldStructure
import homemade.game.model.GameSettings
import kotlin.reflect.KMutableProperty
import kotlin.system.exitProcess

object Main {
    internal var width: Int = 9
    internal var height: Int = 9
    internal var minCombo: Int = 5
    internal var spawnSize: Int = 5
    internal var spawnPeriod: Int = 200
    internal var isTurnBased: Int = 1

    private val argsMapping = mapOf<String, KMutableProperty<*>>(
            "-w" to ::width,
            "-h" to ::height,
            "-s" to ::spawnSize,
            "-p" to ::spawnPeriod,
            "-t" to ::isTurnBased,
            "-c" to ::minCombo
    )

    private fun parseArgs(args: Array<String>) {
        check(args.size % 2 == 0)
        for (i in args.indices step 2) {
            check(args[i] in argsMapping) { "unknown arg ${args[i]}" }
            val property = argsMapping[args[i]]!!
            property.setter.call(args[i + 1].toInt())
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        parseArgs(args)

        val structure = FieldStructure(width, height)
        val settings = GameSettings(
                if (isTurnBased > 0) GameSettings.GameMode.TURN_BASED else GameSettings.GameMode.REAL_TIME,
                minCombo, spawnSize, spawnPeriod, 9
        )
        val controller = Controller(structure, settings)

        println("Hello world!")

        if (args.size == 42) {
            controller.action(1, 2)
        }

        exitProcess(0)
    }
}
