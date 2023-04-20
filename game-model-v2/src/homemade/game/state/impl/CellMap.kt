package homemade.game.state.impl

import homemade.game.fieldstructure.*
import homemade.game.model.Cell
import homemade.game.model.CellState
import homemade.game.model.cellstates.SimpleState
import homemade.game.state.FieldState
import java.util.*

abstract class CellMap(final override val structure: FieldStructure, private val blockValuePool: BlockValuePool) : FieldState() {
    private val cells: Array<CellState>
    private val links: Array<Link?>

    init {
        cells = Array(structure.fieldSize) { SimpleState.get(Cell.EMPTY) }
        val maxLinkCode = structure.numberOfLinks
        links = arrayOfNulls(maxLinkCode)
        val iterator = structure.linkCodeIterator
        while (iterator.hasNext()) {
            val link = iterator.next()
            links[link.hashCode()] = Link()
        }
    }

    override fun getCellState(cellCode: CellCode): CellState {
        return cells[cellCode.hashCode()]
    }

    override fun getLinkBetweenCells(linkCode: LinkCode): Direction? {
        return links[linkCode.hashCode()]!!.direction
    }

    override fun getChainLength(linkCode: LinkCode): Int {
        return links[linkCode.hashCode()]!!.chainLength
    }

    fun applyCascadeChanges(changes: Map<CellCode, CellState?>) {
        val keys = changes.keys
        val linksToUpdate: MutableSet<LinkCode> = HashSet(keys.size * 4)
        val removedBlocks: MutableSet<CellState> = HashSet()
        val addedBlockNumbers: MutableSet<Int> = HashSet()
        for (key in keys) {
            val newState = changes[key]
            val oldState = cells[key.hashCode()]
            cells[key.hashCode()] = newState!!
            for (direction in Direction.values()) {
                val neighbour = key.neighbour(direction)
                if (neighbour != null) {
                    linksToUpdate.add(structure.getLinkCode(key, neighbour))
                }
            }
            if (oldState.isAliveBlock) {
                removedBlocks.add(oldState)
            }
            if (newState.isAliveBlock) {
                addedBlockNumbers.add(newState.value())
            }
        }
        for (removedBlock in removedBlocks) {
            val `val` = removedBlock.value()
            if (!addedBlockNumbers.contains(`val`)) {
                // so, this is correct, if we process move as single update,
                // and it basically means, that we shouldn't stack removals/additions from different sources
                blockValuePool.freeBlockValue(`val`)
            }
        }
        for (linkCode in linksToUpdate) {
            updateLinkValue(linkCode)
        }
    }

    private fun updateLinkValue(linkCode: LinkCode) {
        val link = links[linkCode.hashCode()]
        val cellA = cells[linkCode.lower.hashCode()]
        val cellB = cells[linkCode.higher.hashCode()]
        val oldDirection = link!!.direction
        val lowerToHigher = linkCode.lowerToHigherDirection
        val newDirection = linkDirection(cellA.value(), cellB.value(), lowerToHigher)
        link.direction = newDirection
        val alignedLinks: MutableSet<Link?> = HashSet(structure.maxDimension)
        for (direction in EnumSet.of<Direction?>(lowerToHigher, lowerToHigher.opposite)) {
            val pair = CellCodePair(linkCode)
            val linksCollected = collectLinks(alignedLinks, pair, direction, newDirection)
            if (pair.isValid && linksCollected == 1) {
                val brokenChainLinks: MutableSet<Link?> = HashSet(structure.maxDimension)
                collectLinks(brokenChainLinks, pair, direction, oldDirection)
                assignLinkLengths(brokenChainLinks, oldDirection)
            }
        }
        assignLinkLengths(alignedLinks, newDirection)
    }

    private fun linkDirection(lowerValue: Int, higherValue: Int, lowerToHigher: Direction): Direction? {
        val direction: Direction?
        direction = if (lowerValue == CellState.UNDEFINED_VALUE || higherValue == CellState.UNDEFINED_VALUE || lowerValue == higherValue) {
            null
        } else if (lowerValue > higherValue) {
            lowerToHigher
        } else {
            lowerToHigher.opposite
        }
        return direction
    }

    private fun collectLinks(linkSet: MutableSet<Link?>, pair: CellCodePair, moveDirection: Direction?, chainDirection: Direction?): Int {
        var count = 0
        var checkLink = links[structure.getLinkCode(pair).hashCode()]
        while (pair.isValid && checkLink!!.direction === chainDirection) {
            linkSet.add(checkLink)
            count++
            pair.move(moveDirection!!)
            if (pair.isValid) {
                checkLink = links[structure.getLinkCode(pair).hashCode()]
            }
        }
        return count
    }

    private fun assignLinkLengths(links: Set<Link?>, direction: Direction?) {
        val newChainLength = if (direction == null) 0 else links.size + 1
        for (link in links) {
            link!!.chainLength = newChainLength
        }
    }

    private class Link {
        var direction: Direction? = null
        var chainLength = 0
    }
}
