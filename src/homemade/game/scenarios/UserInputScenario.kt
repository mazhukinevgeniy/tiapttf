package homemade.game.scenarios

import homemade.game.loop.GameLoop
import homemade.game.loop.UserClick
import homemade.game.state.impl.BlockSelection

class UserInputScenario(gameLoop: GameLoop, selection: BlockSelection) {

    init {
        gameLoop.model.subscribe<UserClick>(selection)
    }
}
