package homemade.game.state

import homemade.game.model.GameSettings

abstract class ConfigState(val settings: GameSettings, denies: Int = 0, score: Int = 0, multiplier: Int = 1) {
    open val spawnsDenied: Int = denies

    open val gameScore: Int = score

    open val globalMultiplier: Int = multiplier
}
