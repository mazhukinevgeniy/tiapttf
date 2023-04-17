package homemade.game.scenarios

import homemade.game.ExtendedGameState
import homemade.game.loop.*
import homemade.game.model.GameModelLinker

class SnapshotRequestScenario(private val gameLoop: GameLoop, private val gameModelLinker: GameModelLinker) : GameEventHandler<GameEvent> {

    init {
        System.out.println("subscribing to create snapshots")
        gameLoop.model.subscribe<CreateSnapshot>(this)
    }

    override fun handle(event: GameEvent) {
        if (event is CreateSnapshot) {
            gameModelLinker.lastGameState = ExtendedGameState(
                    gameModelLinker.state.createImmutableCopy(), gameModelLinker.selection.copySelectionState()
            )
            gameLoop.ui.post(SnapshotReady)
        }
    }
}
