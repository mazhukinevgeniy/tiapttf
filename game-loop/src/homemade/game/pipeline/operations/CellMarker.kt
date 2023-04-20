package homemade.game.pipeline.operations

import homemade.game.fieldstructure.CellCode
import homemade.game.model.Cell
import homemade.game.model.CellState
import homemade.game.model.cellstates.BlockState
import homemade.game.model.cellstates.SimpleState
import homemade.game.model.combo.ComboEffect
import homemade.game.pipeline.ProcessingInfo
import homemade.game.state.FieldState
import homemade.game.state.MutableGameState
import homemade.game.state.impl.BlockValuePool
import java.util.*
import kotlin.math.min

internal class CellMarkerImpl(private val blockValuePool: BlockValuePool, private val fieldState: FieldState) {
    private val random: Random = Random() //TODO bad random

    /**
     * Can mark more or less than given percentage of cells
     *
     * @param percentage 0..100
     */
    fun markAnyCell(iterator: Iterator<CellCode>, type: Cell, percentage: Int): Map<CellCode, CellState> {
        val changes: MutableMap<CellCode, CellState> = HashMap()
        val state = SimpleState.get(type)
        while (iterator.hasNext()) {
            val cellCode = iterator.next()
            if (random.nextInt(100) < percentage) {
                changes[cellCode] = state
            }
        }
        return changes
    }

    fun markBlocks(iterator: Iterator<CellCode>, effects: ArrayList<ComboEffect>): Map<CellCode, CellState> {
        val changes: MutableMap<CellCode, CellState> = HashMap()
        val availableBlocks = iterator.asSequence().filter {
            val cellState = fieldState.getCellState(it)
            cellState.isAliveBlock && cellState.effect() == ComboEffect.UNDEFINED_COMBO_EFFECT
        }.toMutableList()
        while (availableBlocks.isNotEmpty() && effects.isNotEmpty()) {
            val position = random.nextInt(availableBlocks.size)
            val cellCode = availableBlocks.removeAt(position)
            val oldState: CellState = fieldState.getCellState(cellCode)
            val newCellState: CellState = BlockState(oldState.value(), oldState.isMovableBlock, effects.removeFirst())
            changes[cellCode] = newCellState
        }
        return changes
    }

    fun markForSpawn(iterator: Iterator<CellCode>, targetAmount: Int): Map<CellCode, CellState> {
        val freeCells = iterator.asSequence().filter { fieldState.getCellState(it).isFreeForSpawn }.toMutableList()
        val cellsToMark = min(freeCells.size, min(targetAmount, blockValuePool.blocksAvailable()))
        val marked = SimpleState.get(Cell.MARKED_FOR_SPAWN)

        val changes: MutableMap<CellCode, CellState> = HashMap()
        for (i in 0 until cellsToMark) {
            val position = random.nextInt(freeCells.size)
            changes[freeCells.removeAt(position)] = marked
        }
        return changes
    }

    fun spawnBlocks(iterator: Iterator<CellCode>, blocksToImmobilize: Int): Map<CellCode, CellState> {
        val changes: MutableMap<CellCode, CellState> = HashMap()
        val cells = iterator.asSequence().filter { fieldState.getCellState(it).type() === Cell.MARKED_FOR_SPAWN }.toMutableList()

        for (i in 0 until min(blocksToImmobilize, cells.size)) {
            val pos: Int = random.nextInt(cells.size)
            changes[cells.removeAt(pos)] = BlockState(blockValuePool.takeBlockValue(), false, ComboEffect.UNDEFINED_COMBO_EFFECT)
        }

        for (item in cells) {
            changes[item] = BlockState(blockValuePool.takeBlockValue(), true, ComboEffect.UNDEFINED_COMBO_EFFECT)
        }
        return changes
    }
}

class CellMarker(private val state: MutableGameState, private val processingInfo: ProcessingInfo) {
    private val impl = CellMarkerImpl(state.changeField().blockValuePool, state.fieldState)

    private val simultaneousSpawn = state.configState.settings.spawn
    private fun getCellIterator(): Iterator<CellCode> {
        return state.fieldState.structure.cellCodeIterator
    }

    fun spawnBlocks() {
        val blocksToImmobilize = 1 //let's say it's either 0 or 1
        //TODO: add mechanic to determine whether we immobilize blocks or not
        execute(impl.spawnBlocks(getCellIterator(), blocksToImmobilize))
    }

    fun markCellsForSpawn() {
        execute(impl.markForSpawn(getCellIterator(), simultaneousSpawn))
    }

    fun execute(changes: Map<CellCode, CellState>) {
        if (changes.isEmpty()) {
            return
        }
        processingInfo.comboStarts.addAll(changes.keys)
        state.changeField().applyCascadeChanges(changes)
    }

    fun markBlocksWithEffects(effects: ArrayList<ComboEffect>) {
        state.changeField().applyCascadeChanges(impl.markBlocks(getCellIterator(), effects))
    }

    fun spawnDeadBlocks() {
        state.changeField().applyCascadeChanges(impl.markAnyCell(getCellIterator(), Cell.DEAD_BLOCK, 5))
    }

    fun removeRandomBlocks() {
        state.changeField().applyCascadeChanges(impl.markAnyCell(getCellIterator(), Cell.EMPTY, 25))
    }
}
