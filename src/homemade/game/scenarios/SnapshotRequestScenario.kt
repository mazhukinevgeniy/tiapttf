package homemade.game.scenarios

import homemade.game.loop.*

class SnapshotRequestScenario(val gameLoop: GameLoop) : GameEventHandler<GameEvent> {

    init {
        gameLoop.model.subscribe<CreateSnapshot>(this)
    }

    override fun handle(event: GameEvent) {
        if (event is CreateSnapshot) {
            gameLoop.ui.post(SnapshotReady)
            //TODO actually create snapshots there
        }
    }
}
