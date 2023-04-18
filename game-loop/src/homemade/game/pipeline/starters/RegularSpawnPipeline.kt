package homemade.game.pipeline.starters

import homemade.game.loop.*
import homemade.game.state.GameState

class RegularSpawnPipeline(
        private val gameState: GameState,
        private val backgroundLoop: BackgroundLoop
) : GameEventHandler<GameEvent> {

    private var paused = false
    private var stopped = false

    private var timeElapsed = 0

    init {
        backgroundLoop.subscribe<GameOver>(this)
        backgroundLoop.subscribe<TimeElapsed>(this)
        backgroundLoop.subscribe<PauseToggle>(this)
    }

    override fun handle(event: GameEvent) {
        when (event) {
            is GameOver -> stopped = true
            is TimeElapsed -> timeElapsed(event.diffMs)
            is PauseToggle -> paused = !paused
            else -> throw RuntimeException("unexpected event $event")
        }
    }

    private fun timeElapsed(time: Int) {
        if (paused || stopped) {
            return
        }
        timeElapsed += time
        val timeRequired = gameState.currentSpawnPeriod()
        if (timeElapsed > timeRequired) {
            timeElapsed = 0
            backgroundLoop.post(RequestBlockSpawning(1))
        }
    }
}
