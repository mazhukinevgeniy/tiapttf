package homemade.game.pipeline.stages

import homemade.game.loop.EventPoster
import homemade.game.loop.GameEvent
import homemade.game.loop.GameOver
import homemade.game.loop.RequestBlockSpawning
import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.pipeline.operations.CellMarker
import homemade.game.state.MutableGameState
import homemade.game.state.getNumberOfBlocks
import homemade.game.state.getNumberOfMovableBlocks

class BoardStateCheckStage(private val gameEventPoster: EventPoster<GameEvent>) : PipelineStage() {

    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        val field = state.fieldState
        val structure = field.structure

        if (field.getNumberOfBlocks() == structure.fieldSize) {
            val multiplier = state.configState.globalMultiplier
            if (multiplier > 1) {
                state.changeConfig().globalMultiplier = 1
                CellMarker(state, processingInfo).removeRandomBlocks()
            } else {
                gameEventPoster.post(GameOver(1))
            }
        } else if (field.getNumberOfMovableBlocks() == 0) {
            //TODO improve 'movable' criterion here, and maybe help by clearing field if game is stuck
            state.changeConfig().globalMultiplier += BONUS_MULTIPLIER_FOR_BOARD_CLEAR
            gameEventPoster.post(RequestBlockSpawning(INITIAL_SPAWNS))
        }
    }

    companion object {
        private const val BONUS_MULTIPLIER_FOR_BOARD_CLEAR = 50
        private const val INITIAL_SPAWNS = 2
    }
}
