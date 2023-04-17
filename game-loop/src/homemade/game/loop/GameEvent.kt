package homemade.game.loop

import homemade.game.fieldstructure.CellCode
import homemade.game.model.CellState

enum class ChangeReason {
    UserInput,
    BlockSpawn
}

sealed class GameEvent

// user-generated
class UserClick(val cellCode: CellCode) : GameEvent()
object SaveAndQuit : GameEvent()
object PauseToggle : GameEvent()

// rules-generated
class DelayedEvent(var delayMs: Int, val event: GameEvent) : GameEvent()
class BatchedBlockChange(val map: Map<CellCode, CellState>, val reason: ChangeReason) : GameEvent()
class GameOver(val countdown: Int = 5) : GameEvent()

// reality-generated
class TimeElapsed(val diffMs: Int) : GameEvent()

object CreateSnapshot : GameEvent()
