package homemade.game.pipeline.starters

import homemade.game.loop.BackgroundLoop
import homemade.game.loop.RequestBlockSpawning

class TurnBasedInitPipeline(backgroundLoop: BackgroundLoop) {
    init {
        backgroundLoop.post(RequestBlockSpawning(3))
    }
}
