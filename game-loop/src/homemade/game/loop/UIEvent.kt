package homemade.game.loop

import homemade.game.fieldstructure.CellCode

sealed class UIEvent

object ShutDown : UIEvent()

object SnapshotReady : UIEvent()
//TODO to be replaced with stateful snapshot?

class BlockRemoved(val cell: CellCode) : UIEvent()
class BlockExploded(val cell: CellCode) : UIEvent()

class MultiplierChanged(val diff: Int) : UIEvent()
