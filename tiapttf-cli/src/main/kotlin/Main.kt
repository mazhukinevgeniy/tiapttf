import homemade.cli.Controller
import homemade.cli.GameOverException
import homemade.game.fieldstructure.FieldStructure
import homemade.game.model.GameSettings
import homemade.game.model.GameStateEncoder
import homemade.game.model.JsonEncoder
import homemade.game.model.PrettyEncoder
import homemade.game.state.GameState
import kotlin.reflect.KMutableProperty
import kotlin.system.exitProcess

object Main {
    internal var width: Int = 9
    internal var height: Int = 9
    internal var minCombo: Int = 5
    internal var spawnSize: Int = 5
    internal var spawnPeriod: Int = 200
    internal var maxValue: Int = 9
    internal var isTurnBased: Int = 1
    internal var isPrettyPrint: Int = 0

    private val argsMapping = mapOf<String, KMutableProperty<*>>(
            "-w" to ::width,
            "-h" to ::height,
            "-s" to ::spawnSize,
            "-p" to ::spawnPeriod,
            "-m" to ::maxValue,
            "-t" to ::isTurnBased,
            "-c" to ::minCombo,
            "-x" to ::isPrettyPrint
    )

    private lateinit var encoder: GameStateEncoder

    private fun parseArgs(args: Array<String>) {
        require(args.size % 2 == 0)
        for (i in args.indices step 2) {
            require(args[i] in argsMapping) { "unknown arg ${args[i]}" }
            val property = argsMapping[args[i]]!!
            property.setter.call(args[i + 1].toInt())
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        parseArgs(args)
        encoder = if (isPrettyPrint > 0) PrettyEncoder() else JsonEncoder()

        val structure = FieldStructure(width, height)
        val settings = GameSettings(
                if (isTurnBased > 0) GameSettings.GameMode.TURN_BASED else GameSettings.GameMode.REAL_TIME,
                minCombo, spawnSize, spawnPeriod, maxValue
        )
        val controller = Controller(structure, settings)

        var lastState = controller.getSnapshot()
        printState(lastState)

        while (true) {
            try {
                val (x, y) = readln().split(',').map { it.toInt() }
                lastState = controller.action(x, y)
                printState(lastState)
            } catch (_: GameOverException) {
                println("game over. current score: ${lastState.configState.gameScore}")
                break
            } catch (_: ArrayIndexOutOfBoundsException) {
                println("bad input, expecting format \"x,y\", where x in 0 until $width, y in 0 until $height")
            } catch (_: NumberFormatException) {
                println("bad input, expecting format \"x,y\", where x in 0 until $width, y in 0 until $height")
            } catch (e: IllegalArgumentException) {
                println("bad input, $e")
            } catch (e: RuntimeException) {
                println("amazing: $e")
                break
            }
        }

        exitProcess(0)
    }

    private fun printState(state: GameState) {
        println(encoder.encode(state))
    }
}
