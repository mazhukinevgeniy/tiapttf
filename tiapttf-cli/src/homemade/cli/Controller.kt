package homemade.cli

import homemade.game.fieldstructure.FieldStructure
import homemade.game.loop.*
import homemade.game.model.GameSettings
import homemade.game.pipeline.GameUpdatePipeline
import homemade.game.state.GameState

class Controller(private val fieldStructure: FieldStructure, gameSettings: GameSettings) {
    private val loop = GameLoop()
    private var snapshot: GameState? = null

    init {
        GameUpdatePipeline.create(fieldStructure, gameSettings, loop)

        loop.ui.subscribe<SnapshotReady>(object : GameEventHandler<UIEvent> {
            override fun handle(event: UIEvent) {
                check(event is SnapshotReady) { "bad event $event" }
                snapshot = event.snapshot
            }
        })

        waitUntilModelQueueEmpties()
    }

    private fun waitUntilModelQueueEmpties() {
        while (!loop.model.isEmpty) {
            try {
                Thread.sleep(5)
            } catch (e: InterruptedException) {
                // okay
            }
        }
    }

    fun getSnapshot(): GameState {
        waitUntilModelQueueEmpties()
        loop.model.post(CreateSnapshot)

        snapshot = null
        while (snapshot == null) {
            loop.ui.tryPropagateEvents()
        }
        return snapshot!!
    }

    fun action(x: Int, y: Int): GameState {
        loop.model.post(UserClick(fieldStructure.getCellCode(x, y)))
        return getSnapshot()
    }
}
