package homemade.game.loop

import homemade.game.fieldstructure.CellCode

sealed class GameEvent

// user-generated
class UserClick(val cellCode: CellCode) : GameEvent()
object PauseToggle : GameEvent()

// rules-generated
class DelayedEvent(var delayMs: Int, val event: GameEvent) : GameEvent()
class RequestBlockSpawning(val weight: Int) : GameEvent() {
    init {
        require(weight in 1..10) { "invalid block spawning weight $weight" }
    }
}

class GameOver(val countdown: Int = 5) : GameEvent() {
    init {
        require(countdown <= 5) { "invalid gameOver countdown $countdown" }
    }
}

// reality-generated
class TimeElapsed(val diffMs: Int) : GameEvent()

object CreateSnapshot : GameEvent()
