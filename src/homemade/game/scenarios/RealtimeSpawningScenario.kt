package homemade.game.scenarios

import homemade.game.loop.GameLoop
import homemade.game.loop.GameOver
import homemade.game.loop.PauseToggle
import homemade.game.loop.TimeElapsed
import homemade.game.model.spawn.SpawnManager

class RealtimeSpawningScenario(spawner: SpawnManager, loop: GameLoop) {
    init {
        loop.model.subscribe<TimeElapsed>(spawner)
        loop.model.subscribe<GameOver>(spawner)
        loop.model.subscribe<PauseToggle>(spawner)
    }
}
