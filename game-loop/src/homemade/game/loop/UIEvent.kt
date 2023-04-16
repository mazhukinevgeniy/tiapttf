package homemade.game.loop

sealed class UIEvent

object ShutDown : UIEvent()

object SnapshotReady : UIEvent()
//TODO to be replaced with stateful snapshot?

class BlockRemoved(val x: Int, val y: Int) : UIEvent()
class BlockExploded(val x: Int, val y: Int) : UIEvent()
