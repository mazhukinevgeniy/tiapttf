package homemade.game.state

import homemade.game.model.GameSettings

class MutableConfigState(
        settings: GameSettings,
        override var spawnsDenied: Int,
        override var gameScore: Int,
        override var globalMultiplier: Int
) : ConfigState(settings, spawnsDenied, gameScore, globalMultiplier) {

    fun copyConfigState(): ConfigState {
        return MutableConfigState(settings, spawnsDenied, gameScore, globalMultiplier)
    }
}
