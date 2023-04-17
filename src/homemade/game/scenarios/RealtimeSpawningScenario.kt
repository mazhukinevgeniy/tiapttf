package homemade.game.scenarios

import homemade.game.GameSettings
import homemade.game.loop.GameLoop
import homemade.game.loop.GameOver
import homemade.game.loop.PauseToggle
import homemade.game.loop.TimeElapsed
import homemade.game.model.GameModelLinker
import homemade.game.model.spawn.DynamicPeriodTimer

class RealtimeSpawningScenario(linker: GameModelLinker, settings: GameSettings, loop: GameLoop) {
    init {
        val spawner = DynamicPeriodTimer(linker, settings)
        loop.model.subscribe<TimeElapsed>(spawner)
        loop.model.subscribe<GameOver>(spawner)
        loop.model.subscribe<PauseToggle>(spawner)
    }
}
