package homemade.game.pipeline.stages

import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.pipeline.operations.CellMarker
import homemade.game.pipeline.operations.ComboEffectVendor
import homemade.game.state.MutableGameState

class EffectSpawningStage : PipelineStage() {

    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        if (processingInfo.comboStarts.isEmpty()) {
            return
        }

        val comboPackTier = processingInfo.storedCombos.packTier()
        ComboEffectVendor().addComboEffectsForTier(state.storedEffects, comboPackTier)
        CellMarker(state, processingInfo).markBlocksWithEffects(state.storedEffects)
    }
}
