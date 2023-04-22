package homemade.game.pipeline.stages

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.FieldStructure
import homemade.game.fieldstructure.LinkCode
import homemade.game.loop.BlockExploded
import homemade.game.loop.BlockRemoved
import homemade.game.loop.EventPoster
import homemade.game.loop.UIEvent
import homemade.game.model.Cell
import homemade.game.model.CellState
import homemade.game.model.cellstates.SimpleState
import homemade.game.model.combo.Combo
import homemade.game.model.combo.ComboEffect
import homemade.game.model.combo.ComboPack
import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.pipeline.operations.CellMarker
import homemade.game.state.FieldState
import homemade.game.state.MutableGameState

class ComboProcessingStage(private val uiLoop: EventPoster<UIEvent>) : PipelineStage() {

    private lateinit var structure: FieldStructure
    private lateinit var field: FieldState
    private var minCombo: Int = 50

    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        structure = state.fieldState.structure
        field = state.fieldState
        minCombo = state.configState.settings.minCombo

        val combos = findCombos(processingInfo.comboStarts)
        processingInfo.storedCombos.append(combos)

        CellMarker(state, processingInfo).execute(removeCombos(combos))
    }

    private fun findCombos(starts: Set<CellCode>): ComboPack {
        val numberOfStarts = starts.size
        val horizontals: MutableSet<Int> = HashSet(numberOfStarts)
        val verticals: MutableSet<Int> = HashSet(numberOfStarts)
        for (cellCode in starts) {
            horizontals.add(cellCode.y)
            verticals.add(cellCode.x)
        }
        val pack = ComboPack()
        for (horizontal in horizontals) {
            iterateThroughTheLine(pack, structure.getCellCode(0, horizontal), Direction.RIGHT)
        }
        for (vertical in verticals) {
            iterateThroughTheLine(pack, structure.getCellCode(vertical, 0), Direction.BOTTOM)
        }
        return pack
    }

    /**
     * @param start         cellCode of the beginning
     * @param mainDirection where to look for the next cell
     */
    private fun iterateThroughTheLine(pack: ComboPack, start: CellCode, mainDirection: Direction) {
        var currentCell: CellCode? = start
        while (currentCell != null) {
            var nextCell: CellCode? = currentCell.neighbour(mainDirection) ?: break
            val nextLink: LinkCode = structure.getLinkCode(currentCell, nextCell!!)
            val comboLength: Int = field.getChainLength(nextLink)

            if (comboLength < minCombo) {
                currentCell = nextCell
                continue
            }

            val comboCells: MutableSet<CellCode> = java.util.HashSet()
            var comboTier: Int = comboLength - minCombo + 1
            var lastCell = currentCell
            for (i in 0 until comboLength) {
                currentCell = lastCell
                nextCell = lastCell!!.neighbour(mainDirection)
                val comboEffect: ComboEffect = currentCell?.let { field.getCellState(it).effect() }
                        ?: throw RuntimeException("unexpected $currentCell")
                if (comboEffect != ComboEffect.UNDEFINED_COMBO_EFFECT) {
                    comboTier += comboEffect.tierBonus()
                    pack.addMultiplier(comboEffect.multiplierBonus())
                    if (comboEffect === ComboEffect.EXPLOSION) {
                        comboCells.addAll(currentCell.vicinity)
                        for (cell in currentCell.vicinity) {
                            if (field.getCellState(cell).isAliveBlock) {
                                uiLoop.post(BlockRemoved(cell))
                            }
                        }
                        uiLoop.post(BlockExploded(currentCell))
                        //TODO are you saying explosions were never actually implemented
                    }
                }
                comboCells.add(currentCell)
                uiLoop.post(BlockRemoved(currentCell))
                lastCell = nextCell
            }
            pack.add(Combo(comboCells, comboTier))

            //currentCell must be the last cell of combo after this block
        }
    }

    private fun removeCombos(combos: ComboPack): Map<CellCode, CellState> {
        val updateList: MutableMap<CellCode, CellState> = HashMap()
        val empty = SimpleState.get(Cell.EMPTY)
        val comboCells = combos.cellSet()

        for (cellCode in comboCells) {
            updateList[cellCode] = empty
        }

        for (cellCode in comboCells) {
            for (direction in Direction.values()) {
                val neighbour = cellCode.neighbour(direction) ?: continue
                if (neighbour !in updateList && field.getCellState(neighbour).type() === Cell.DEAD_BLOCK) {
                    updateList[neighbour] = empty
                }
            }
        }
        return updateList
    }
}
