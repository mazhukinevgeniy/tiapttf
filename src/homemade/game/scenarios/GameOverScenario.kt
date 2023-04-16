package homemade.game.scenarios

import homemade.game.loop.*
import homemade.game.model.GameModelLinker

class GameOverScenario(val gameLoop: GameLoop, val model: GameModelLinker) : GameEventHandler<GameEvent> {
    init {
        gameLoop.model.subscribe<GameOver>(this)
    }

    override fun handle(event: GameEvent) {
        when (event) {
            is GameOver -> {
                if (event.countdown == 0) {
                    gameLoop.ui.post(ShutDown)
                } else {
                    model.killRandomBlocks()
                    gameLoop.model.post(DelayedEvent(200, GameOver(event.countdown - 1)))
                }
            }

            else -> {
                throw IllegalArgumentException("can't handle $event")
            }
        }
    }
}
