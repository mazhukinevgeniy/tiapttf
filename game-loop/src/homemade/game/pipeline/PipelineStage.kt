package homemade.game.pipeline

import homemade.game.state.GameState

abstract class PipelineStage {
    abstract fun process(state: GameState)
}
