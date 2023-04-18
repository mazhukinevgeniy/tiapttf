package homemade.game.pipeline

import homemade.game.loop.GameEvent
import homemade.game.state.ConfigState
import homemade.game.state.MutableGameState


data class ProcessingInfo(
        var sourceEvent: GameEvent,
        val initialConfigState: ConfigState
)

abstract class PipelineStage {
    abstract fun process(state: MutableGameState, processingInfo: ProcessingInfo)
}
