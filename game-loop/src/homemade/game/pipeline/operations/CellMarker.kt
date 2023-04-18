package homemade.game.pipeline.operations

import homemade.game.fieldstructure.CellCode
import homemade.game.model.Cell
import homemade.game.model.CellState
import homemade.game.model.ComboEffect
import homemade.game.model.cellstates.BlockState
import homemade.game.model.cellstates.SimpleState
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
    fun markAnyCell(iterator: Iterator<CellCode>, type: Cell?, percentage: Int): Map<CellCode, CellState> {
        val changes: MutableMap<CellCode, CellState> = HashMap()
        val state = SimpleState.getSimpleState(type) ?: throw RuntimeException("not a simple type")
        while (iterator.hasNext()) {
            val cellCode = iterator.next()
            if (random.nextInt(100) < percentage) {
                changes[cellCode] = state
            }
        }
        return changes
    }

    fun markBlocks(iterator: Iterator<CellCode>, effects: LinkedList<ComboEffect>): Map<CellCode, CellState> {
        val changes: MutableMap<CellCode, CellState> = HashMap()
        val availableBlocks = iterator.asSequence().filter {
            val cellState = fieldState.getCellState(it)
            cellState.isAliveBlock && cellState.effect() == null
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
        val marked = SimpleState.getSimpleState(Cell.MARKED_FOR_SPAWN)

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
            changes[cells.removeAt(pos)] = BlockState(blockValuePool.takeBlockValue(), false, null)
        }

        for (item in cells) {
            changes[item] = BlockState(blockValuePool.takeBlockValue(), true, null)
        }
        return changes
    }
}

class CellMarker(private val state: MutableGameState) {
    private val impl = CellMarkerImpl(state.changeField().blockValuePool, state.fieldState)

    private val simultaneousSpawn = state.configState.settings.spawn
    private fun getCellIterator(): Iterator<CellCode> {
        return state.fieldState.structure.cellCodeIterator
    }

    fun spawnBlocks(): Map<CellCode, CellState> {
        val blocksToImmobilize = 1 //let's say it's either 0 or 1
        //TODO: add mechanic to determine whether we immobilize blocks or not
        return impl.spawnBlocks(getCellIterator(), blocksToImmobilize)
    }

    fun markCellsForSpawn(): Map<CellCode, CellState> {
        return impl.markForSpawn(getCellIterator(), simultaneousSpawn)
    }

    fun markBlocksWithEffects(effects: LinkedList<ComboEffect>): Map<CellCode, CellState> {
        return impl.markBlocks(getCellIterator(), effects)
    }

    fun spawnDeadBlocks(): Map<CellCode, CellState> {
        return impl.markAnyCell(getCellIterator(), Cell.DEAD_BLOCK, 5)
    }

    fun removeRandomBlocks(): Map<CellCode, CellState> {
        return impl.markAnyCell(getCellIterator(), Cell.EMPTY, 25)
    }
}