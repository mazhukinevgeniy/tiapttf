package homemade.game.state.impl;

import homemade.game.fieldstructure.*;
import homemade.game.model.Cell;
import homemade.game.model.CellState;
import homemade.game.model.cellstates.SimpleState;
import homemade.game.state.FieldState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

abstract public class CellMap extends FieldState {
    private CellState cells[];
    private Link links[];

    private FieldStructure structure;

    private BlockValuePool blockValuePool;

    public CellMap(FieldStructure structure, BlockValuePool blockValuePool) {
        this.structure = structure;
        this.blockValuePool = blockValuePool;

        cells = new CellState[structure.fieldSize];

        CellState emptyState = SimpleState.getSimpleState(Cell.EMPTY);
        for (int i = 0; i < structure.fieldSize; ++i) {
            cells[i] = emptyState;
        }

        int maxLinkCode = structure.numberOfLinks;
        links = new Link[maxLinkCode];

        for (Iterator<LinkCode> iterator = structure.getLinkCodeIterator(); iterator.hasNext(); ) {
            LinkCode link = iterator.next();
            links[link.hashCode()] = new Link();
        }
    }

    @NotNull
    @Override
    public FieldStructure getStructure() {
        return structure;
    }

    @Override
    public CellState getCellState(@NotNull CellCode cellCode) {
        return cells[cellCode.hashCode()];
    }

    @Nullable
    @Override
    public Direction getLinkBetweenCells(@NotNull LinkCode linkCode) {
        return links[linkCode.hashCode()].direction;
    }

    @Override
    public int getChainLength(@NotNull LinkCode linkCode) {
        return links[linkCode.hashCode()].chainLength;
    }

    public void applyCascadeChanges(Map<CellCode, CellState> changes) {
        Set<CellCode> keys = changes.keySet();

        Set<LinkCode> linksToUpdate = new HashSet<>(keys.size() * 4);

        Set<CellState> removedBlocks = new HashSet<>();
        Set<Integer> addedBlockNumbers = new HashSet<>();

        for (CellCode key : keys) {
            CellState newState = changes.get(key);
            CellState oldState = cells[key.hashCode()];

            cells[key.hashCode()] = newState;

            for (Direction direction : Direction.values()) {
                CellCode neighbour = key.neighbour(direction);

                if (neighbour != null) {
                    linksToUpdate.add(structure.getLinkCode(key, neighbour));
                }
            }

            if (oldState.isAliveBlock()) {
                removedBlocks.add(oldState);
            }
            if (newState.isAliveBlock()) {
                addedBlockNumbers.add(newState.value());
            }
        }

        for (CellState removedBlock : removedBlocks) {
            Integer val = removedBlock.value();

            if (!addedBlockNumbers.contains(val)) {
                // so, this is correct, if we process move as single update,
                // and it basically means, that we shouldn't stack removals/additions from different sources
                blockValuePool.freeBlockValue(val);
            }
        }

        for (LinkCode linkCode : linksToUpdate) {
            updateLinkValue(linkCode);
        }
    }

    private void updateLinkValue(LinkCode linkCode) {
        Link link = links[linkCode.hashCode()];

        CellState cellA = cells[linkCode.lower.hashCode()];
        CellState cellB = cells[linkCode.higher.hashCode()];

        Direction oldDirection = link.direction;
        Direction lowerToHigher = linkCode.getLowerToHigherDirection();
        Direction newDirection = linkDirection(cellA.value(), cellB.value(), lowerToHigher);

        link.direction = newDirection;

        Set<Link> alignedLinks = new HashSet<>(structure.getMaxDimension());

        for (Direction direction : EnumSet.of(lowerToHigher, lowerToHigher.getOpposite())) {
            CellCodePair pair = new CellCodePair(linkCode);

            int linksCollected = collectLinks(alignedLinks, pair, direction, newDirection);

            if (pair.isValid() && linksCollected == 1) {
                Set<Link> brokenChainLinks = new HashSet<>(structure.getMaxDimension());
                collectLinks(brokenChainLinks, pair, direction, oldDirection);
                assignLinkLengths(brokenChainLinks, oldDirection);
            }
        }

        assignLinkLengths(alignedLinks, newDirection);
    }

    private Direction linkDirection(int lowerValue, int higherValue, Direction lowerToHigher) {
        Direction direction;

        if (lowerValue == CellState.UNDEFINED_VALUE || higherValue == CellState.UNDEFINED_VALUE ||
                lowerValue == higherValue) {
            direction = null;
        } else if (lowerValue > higherValue) {
            direction = lowerToHigher;
        } else {
            direction = lowerToHigher.getOpposite();
        }

        return direction;
    }

    private int collectLinks(Set<Link> linkSet, CellCodePair pair, Direction moveDirection, Direction chainDirection) {
        int count = 0;
        Link checkLink = links[structure.getLinkCode(pair).hashCode()];

        while (pair.isValid() && checkLink.direction == chainDirection) {
            linkSet.add(checkLink);
            count++;

            pair.move(moveDirection);

            if (pair.isValid()) {
                checkLink = links[structure.getLinkCode(pair).hashCode()];
            }
        }
        return count;
    }

    private void assignLinkLengths(Set<Link> links, Direction direction) {
        int newChainLength = direction == null ? 0 : links.size() + 1;
        for (Link link : links) {
            link.chainLength = newChainLength;
        }
    }

    private static class Link {
        private Direction direction;
        private int chainLength;

        private Link() {
            direction = null;
            chainLength = 0;
        }
    }
}
