package homemade.game.pipeline.stages

import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.state.MutableGameState
import java.util.*

class SpawnProcessingStage : PipelineStage() {
    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        state.configState.globalMultiplier--
        modifyGlobalMultiplier(-1)
        updater.takeComboChanges(spawner.spawnBlocks())
        updater.takeChanges(spawner.markCellsForSpawn())
        updateStates()

        //note: else if (state.getNumberOfMovableBlocks() == 0) might happen if a) field is empty or b) field is stuck
        //TODO where does this note belong huh?
    }
}
