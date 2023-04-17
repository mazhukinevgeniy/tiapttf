package homemade.game.state

import homemade.game.model.GameSettings

abstract class ConfigState(val settings: GameSettings, denies: Int = 0, score: Int = 0, multiplier: Int = 1) {
    var spawnsDenied: Int = denies
        protected set

    var gameScore: Int = score
        protected set

    var globalMultiplier: Int = multiplier
        protected set
}
