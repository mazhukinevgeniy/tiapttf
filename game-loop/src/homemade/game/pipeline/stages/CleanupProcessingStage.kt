package homemade.game.pipeline.stages

import homemade.game.loop.GameOver
import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.pipeline.operations.GameScore
import homemade.game.state.MutableGameState

class CleanupProcessingStage : PipelineStage() {

    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        //TODO("Not yet implemented")

        GameScore().handleCombos(processingInfo.storedCombos, state.changeConfig())
        // declaring game over, filling empty field, this stuff goes here


        if (updater.hasCellChanges()) {
            val comboPackTier = updater.comboPackTier()
            ComboEffectVendor().addComboEffectsForTier(storedEffects, comboPackTier)
            updater.takeChanges(spawner.markBlocksWithEffects(storedEffects))
            trueState.selectionState.updateSelectionState()
            updater.flush(trueState.configState.globalMultiplier)
        }
        if (state.getNumberOfBlocks() == structure.fieldSize) {
            val multiplier = trueState.configState.globalMultiplier
            if (multiplier > 1) {
                modifyGlobalMultiplier(-multiplier)
                updater.takeChanges(spawner.removeRandomBlocks())
                updateStates()
                println("multiplier consumed")
            } else {
                gameLoop.model.post(GameOver(1))
                println("can't trade multiplier for blocks")
            }
        } else if (state.getNumberOfMovableBlocks() == 0) {
            modifyGlobalMultiplier(BONUS_MULTIPLIER_FOR_BOARD_CLEAR + INITIAL_SPAWNS)
            for (i in 0 until INITIAL_SPAWNS) {
                ///requestSpawn()
            }
        }
    }

    companion object {
        private const val BONUS_MULTIPLIER_FOR_BOARD_CLEAR = 50
        private const val INITIAL_SPAWNS = 2
    }
}
