package homemade.game.model

import homemade.game.fieldstructure.FieldStructure
import homemade.game.loop.*
import homemade.game.model.spawn.SpawnManager
import homemade.game.pipeline.GameUpdatePipeline
import homemade.game.pipeline.operations.GameScore
import homemade.game.scenarios.GameOverScenario
import homemade.game.state.*
import homemade.game.state.impl.BlockValuePool
import java.util.*

class GameModelLinker(val structure: FieldStructure, val settings: GameSettings, private val gameLoop: GameLoop) {
    private val spawner: SpawnManager

    init {
        GameUpdatePipeline(
                gameLoop,
                MutableGameState(
                        MutableFieldState(structure, BlockValuePool(settings.maxBlockValue, structure.fieldSize)),
                        MutableSelectionState(null, HashSet()),
                        MutableConfigState(settings, 0, 0, 1)
                )
        )
        val gameScore = GameScore(this)
        spawner = SpawnManager(this, blockValuePool)
        GameOverScenario(gameLoop, this)
    }

}
