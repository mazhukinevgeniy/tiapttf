package homemade.game.loop

import homemade.game.model.cellstates.BlockState

sealed class GameEvent

// user-generated
class UserClick(val x: Int, val y: Int) : GameEvent()
object SaveAndQuit : GameEvent()
object PauseToggle : GameEvent()

// rules-generated
class ItemSpawn(val x: Int, val y: Int, val block: BlockState) : GameEvent()
class DelayedEvent(var delayMs: Int, val event: GameEvent) : GameEvent()
class GameOver(val countdown: Int = 5) : GameEvent()

// reality-generated
class TimeElapsed(val diffMs: Int) : GameEvent()

object CreateSnapshot : GameEvent()
