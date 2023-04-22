package homemade.game.loop

import homemade.game.fieldstructure.CellCode
import homemade.game.state.GameState

sealed class UIEvent

object ShutDown : UIEvent()

class SnapshotReady(val snapshot: GameState) : UIEvent()

class BlockRemoved(val cell: CellCode) : UIEvent()
class BlockExploded(val cell: CellCode) : UIEvent()

class MultiplierChanged(val diff: Int) : UIEvent()
