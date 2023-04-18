package homemade.game.state

import homemade.game.model.GameSettings

class MutableConfigState(
        settings: GameSettings,
        override var spawnsDenied: Int,
        override var gameScore: Int,
        multiplier: Int
) : ConfigState(settings) {
    override var globalMultiplier: Int = multiplier
        set(value) {
            field = maxOf(value, 1)
        }

    fun copyConfigState(): ConfigState {
        return MutableConfigState(settings, spawnsDenied, gameScore, globalMultiplier)
    }
}
