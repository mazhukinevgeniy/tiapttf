package homemade.game.state

import homemade.game.model.GameSettings

abstract class ConfigState(val settings: GameSettings) {
    abstract val spawnsDenied: Int

    abstract val gameScore: Int

    abstract val globalMultiplier: Int
}
