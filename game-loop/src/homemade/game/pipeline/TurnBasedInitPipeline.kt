package homemade.game.pipeline

import homemade.game.loop.BackgroundLoop
import homemade.game.loop.RequestBlockSpawning

class TurnBasedInitPipeline(backgroundLoop: BackgroundLoop) {
    init {
        backgroundLoop.post(RequestBlockSpawning)
        backgroundLoop.post(RequestBlockSpawning)
        backgroundLoop.post(RequestBlockSpawning)
        //TODO do i absolutely not care about updater.takeChanges(spawner.markCellsForSpawn()) ?
    }
}
