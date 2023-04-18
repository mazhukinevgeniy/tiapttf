package homemade.game.pipeline

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.LinkCode
import homemade.game.loop.GameEvent
import homemade.game.model.CellState
import homemade.game.state.ConfigState
import homemade.game.state.MutableGameState
import java.util.*

enum class ChangedData {
    BLOCK,
    LINK,
    SELECTION
}

data class ProcessingInfo(
        var sourceEvent: GameEvent,
        val initialConfigState: ConfigState,
        var updatedCells: HashMap<CellCode, CellState> = HashMap(),
        var updatedLinks: HashMap<LinkCode, Direction> = HashMap(),
        var updatedChains: HashMap<LinkCode, Int> = HashMap(),
        var changedData: EnumSet<ChangedData> = EnumSet.noneOf(ChangedData::class.java)
)

abstract class PipelineStage {
    abstract fun process(state: MutableGameState, processingInfo: ProcessingInfo)
}
