package homemade.game.controller

import homemade.game.GameSettings
import homemade.game.fieldstructure.FieldStructure
import homemade.game.loop.*
import homemade.game.model.GameModelLinker
import homemade.game.view.GameView
import homemade.game.view.ShownEffect
import homemade.menu.controller.MenuManager
import homemade.menu.model.records.Records
import java.awt.Container
import java.awt.Frame
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.time.LocalDateTime
import javax.swing.Timer

class GameController(private val menuManager: MenuManager, private val frame: Frame, container: Container?, private val settings: GameSettings, private val records: Records) : MouseInputHandler, GameEventHandler<UIEvent> {
    private val structure: FieldStructure = FieldStructure()
    private val gameLoop: GameLoop = GameLoop() //TODO convert to kotlin to subscribe normally
    private val keyboard: GameKeyboard = GameKeyboard(gameLoop.model)
    private val model: GameModelLinker
    private val view: GameView
    private val mainTimer: Timer

    init {
        gameLoop.ui.subscribe<ShutDown>(this)
        gameLoop.ui.subscribe<SnapshotReady>(this)
        gameLoop.ui.subscribe<MultiplierChanged>(this)

        val viewListener = ViewListener(this, this, keyboard)
        view = GameView(structure, settings, viewListener, container)
        model = GameModelLinker(structure, settings, gameLoop)
        //model must be initialized after view because there could be combos in initialization
        //TODO: if everything is done with eventLoop channels, this observation is invalid
        mainTimer = Timer(5, object : ActionListener {
            var previousTime: Long = 0
            var sum: Long = 0
            override fun actionPerformed(e: ActionEvent) {
                val newTime = System.currentTimeMillis()
                if (previousTime > 0 && newTime > previousTime) {
                    val diff = newTime - previousTime
                    gameLoop.model.post(TimeElapsed(diff.toInt()))
                    sum += diff
                    if (sum >= 1000 / TARGET_FPS) {
                        gameLoop.model.post(CreateSnapshot)
                        sum %= (1000 / TARGET_FPS)
                    }
                }
                previousTime = newTime
                gameLoop.ui.tryPropagateEvents()
            }
        })
        mainTimer.start()
    }

    override fun handle(event: UIEvent) {
        when (event) {
            is ShutDown -> {
                view.dispose()
                mainTimer.stop()
                val score = model.lastGameState.gameState.gameScore
                records.add(score, settings.toString(), LocalDateTime.now())

                menuManager.switchToMenu(MenuManager.MenuCode.MAIN_MENU)
            }

            is SnapshotReady -> {
                val state = model.lastGameState
                frame.title = "score: " + state.gameState.gameScore + ", multiplier: " + state.gameState.globalMultiplier
                view.renderNextFrame(state.gameState, state.selectionState)
            }

            is MultiplierChanged -> {
                view.effectManager.blink(event.diff > 0)
            }

            is BlockRemoved -> {
                view.effectManager.addEffect(event.cell, ShownEffect.FADEAWAY)
            }

            is BlockExploded -> {
                view.effectManager.addEffect(event.cell, ShownEffect.EXPLOSION)
            }

            else -> {
                throw RuntimeException("unexpected event $event")
            }
        }
    }

    override fun handleMouseRelease(canvasX: Int, canvasY: Int) {
        val gridX = canvasX - GameView.GRID_OFFSET_X
        val gridY = canvasY - GameView.GRID_OFFSET_Y
        val fullCell = GameView.CELL_WIDTH + GameView.CELL_OFFSET
        val maxX = structure.width * fullCell
        val maxY = structure.height * fullCell
        if (gridX in 0 until maxX && gridY in 0 until maxY) {
            val cellX = gridX / (GameView.CELL_WIDTH + GameView.CELL_OFFSET)
            val cellY = gridY / (GameView.CELL_WIDTH + GameView.CELL_OFFSET)
            val eventCell = structure.getCellCode(cellX, cellY)
            gameLoop.model.post(UserClick(eventCell))
        }
    }

    fun requestQuit() {
        gameLoop.model.post(GameOver())
    }

    companion object {
        private const val TARGET_FPS = 60
    }
}
