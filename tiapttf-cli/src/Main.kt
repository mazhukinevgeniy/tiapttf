import homemade.cli.Controller
import homemade.game.fieldstructure.FieldStructure
import homemade.game.model.GameSettings

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val structure = FieldStructure()
        val settings = GameSettings(
                GameSettings.GameMode.TURN_BASED,
                5,
                6,
                100,
                9
        )
        val controller = Controller(structure, settings)

        println("Hello world!")

        if (args.size == 42) {
            controller.action(1, 2)
        }
    }
}
