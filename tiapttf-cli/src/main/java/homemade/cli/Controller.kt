package homemade.cli

import homemade.game.fieldstructure.FieldStructure
import homemade.game.loop.*
import homemade.game.model.GameSettings
import homemade.game.pipeline.GameUpdatePipeline
import homemade.game.state.GameState
import java.util.concurrent.atomic.AtomicBoolean


class GameOverException : Exception("game over")
class Controller(private val fieldStructure: FieldStructure, gameSettings: GameSettings) {
    private val loop = GameLoop()
    private var snapshot: GameState? = null

    private val gameOver = AtomicBoolean(false)

    init {
        GameUpdatePipeline.create(fieldStructure, gameSettings, loop)

        loop.ui.subscribe<SnapshotReady>(object : GameEventHandler<UIEvent> {
            override fun handle(event: UIEvent) {
                require(event is SnapshotReady) { "bad event $event" }
                snapshot = event.snapshot
            }
        })
        loop.model.subscribe<GameOver>(object : GameEventHandler<GameEvent> {
            override fun handle(event: GameEvent) {
                require(event is GameOver) { "bad event $event" }
                gameOver.set(true)
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
        if (gameOver.get()) {
            throw GameOverException()
        }
        return snapshot!!
    }

    fun action(x: Int, y: Int): GameState {
        loop.model.post(UserClick(fieldStructure.getCellCode(x, y)))
        getSnapshot()
        // double-click to surely get the end result of the action
        loop.model.post(UserClick(fieldStructure.getCellCode(x, y)))
        return getSnapshot()
    }
}
