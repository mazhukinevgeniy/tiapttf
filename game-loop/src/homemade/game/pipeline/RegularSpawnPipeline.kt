package homemade.game.pipeline

import homemade.game.loop.*
import homemade.game.state.MutableGameState

class RegularSpawnPipeline(
        val mutableState: MutableGameState,
        val backgroundLoop: BackgroundLoop
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
        if (event is GameOver) {
            stopped = true
        } else if (event is TimeElapsed) {
            timeElapsed(event.diffMs)
        } else if (event is PauseToggle) {
            paused = !paused
        } else {
            throw RuntimeException("unexpected event $event")
        }
    }

    private fun timeElapsed(time: Int) {
        if (paused || stopped) {
            return
        }
        timeElapsed += time
        val timeRequired = mutableState.currentSpawnPeriod()
        if (timeElapsed > timeRequired) {
            timeElapsed = 0
            backgroundLoop.post(RequestBlockSpawning)
        }
    }
}
