package homemade.game.model

import homemade.game.fieldstructure.FieldStructure
import homemade.game.loop.*
import homemade.game.model.combo.ComboDetector
import homemade.game.model.spawn.SpawnManager
import homemade.game.pipeline.GameUpdatePipeline
import homemade.game.scenarios.GameOverScenario
import homemade.game.state.*
import homemade.game.state.impl.BlockValuePool
import java.util.*

class GameModelLinker(val structure: FieldStructure, val settings: GameSettings, private val gameLoop: GameLoop) {
    private val spawner: SpawnManager
    private val storedEffects: LinkedList<ComboEffect>
    private val updater: Updater

    init {
        GameUpdatePipeline(
                gameLoop,
                MutableGameState(
                        MutableFieldState(structure, BlockValuePool(settings.maxBlockValue, structure.fieldSize)),
                        MutableSelectionState(null, HashSet()),
                        MutableConfigState(settings, 0, 0, 1)
                )
        )
        storedEffects = LinkedList()
        val comboDetector = ComboDetector(this, gameLoop.ui)
        val gameScore = GameScore(this)
        spawner = SpawnManager(this, blockValuePool)
        GameOverScenario(gameLoop, this)
    }

    @Synchronized
    fun killRandomBlocks() {
        updater.takeChanges(spawner.spawnDeadBlocks())
        updateStates()
    }

    @Synchronized
    fun modifyGlobalMultiplier(change: Int) {
        val oldMultiplier = trueState.configState.globalMultiplier
        val rawMultiplier = oldMultiplier + change
        val newMultiplier = Math.max(1, rawMultiplier)
        if (oldMultiplier != newMultiplier) {
            state.updateMultiplier(newMultiplier)
            gameLoop.ui.post(MultiplierChanged(change))
        }
    }

    private fun updateStates() {
        //cleanup processing stage
    }

}
