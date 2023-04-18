package homemade.game.pipeline

import homemade.game.fieldstructure.CellCode
import homemade.game.loop.GameEvent
import homemade.game.model.combo.ComboPack
import homemade.game.state.ConfigState
import homemade.game.state.MutableGameState


data class ProcessingInfo(
        var sourceEvent: GameEvent,
        val initialConfigState: ConfigState,
        val storedCombos: ComboPack = ComboPack(),
        val comboStarts: HashSet<CellCode> = HashSet()
)

abstract class PipelineStage {
    abstract fun process(state: MutableGameState, processingInfo: ProcessingInfo)
}
