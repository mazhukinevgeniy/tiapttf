package homemade.game.model;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.combo.ComboDetector;
import homemade.game.model.combo.ComboPack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Used by GameModelLinker for tasks such as updating cellmap and gamestate
 *
 * Must be synchronized externally
 */
class Updater
{
    private FieldStructure structure;

    private ComboDetector comboDetector;

    private CellMap cellMap;
    private GameScore gameScore;
    private BlockPool blockPool;
    private ArrayBasedGameState state;

    private Set<CellCode> storedChanges;
    private ComboPack storedCombos;

    Updater(GameModelLinker linker, ComboDetector comboDetector, CellMap cellMap, GameScore gameScore, BlockPool blockPool, ArrayBasedGameState state)
    {
        structure = linker.getStructure();

        this.comboDetector = comboDetector;

        this.cellMap = cellMap;
        this.gameScore = gameScore;
        this.blockPool = blockPool;
        this.state = state;

        storedChanges = new HashSet<>();
        storedCombos = new ComboPack();
    }

    void takeChanges(Map<CellCode, CellState> changedCells)
    {
        Set<CellCode> cellMapChanges = cellMap.applyCascadeChanges(changedCells);
        storedChanges.addAll(cellMapChanges);
    }

    void takeComboChanges(Map<CellCode, CellState> changedCells)
    {
        Set<CellCode> cellMapChanges = cellMap.applyCascadeChanges(changedCells);
        storedChanges.addAll(cellMapChanges);

        ComboPack newCombos = comboDetector.findCombos(cellMapChanges);
        storedCombos.append(newCombos);

        Set<CellCode> comboCells = cellMap.applyCascadeChanges(removeCombos(newCombos));
        storedChanges.addAll(comboCells);
    }

    void flush()
    {
        gameScore.handleCombos(storedCombos);
        storedCombos = new ComboPack();

        updateState(storedChanges);
        storedChanges.clear();
    }

    boolean hasCellChanges()
    {
        return !storedChanges.isEmpty();
    }

    boolean hasCombos()
    {
        return storedCombos.numberOfCombos() > 0;
    }

    int comboPackTier()
    {
        return storedCombos.packTier();
    }

    private Map<CellCode, CellState> removeCombos(ComboPack combos)
    {
        Map<CellCode, CellState> cellsToRemove = new HashMap<>();

        CellState empty = CellState.simpleState(Cell.EMPTY);

        Set<CellCode> comboCells = combos.cellSet();

        for (CellCode cellCode : comboCells)
        {
            blockPool.freeBlock(cellMap.getCell(cellCode));

            cellsToRemove.put(cellCode, empty);
        }

        Direction[] directions = Direction.values();

        for (CellCode cellCode : comboCells)
        {
            for (Direction direction : directions)
            {
                CellCode neighbour = cellCode.neighbour(direction);

                if (    neighbour != null &&
                        !cellsToRemove.containsKey(neighbour) &&
                        cellMap.getCell(neighbour).type() == Cell.DEAD_BLOCK)
                    cellsToRemove.put(neighbour, empty);
            }
        }

        return cellsToRemove;
    }

    private void updateState(Set<CellCode> changedCells)
    {
        if (changedCells.size() > 0)
        {
            Map<CellCode, CellState> updatedCells = new HashMap<>();
            Map<LinkCode, Direction> updatedLinks = new HashMap<>();
            Map<LinkCode, Integer> updatedChains = new HashMap<>();

            for (CellCode cellCode : changedCells)
            {
                updatedCells.put(cellCode, cellMap.getCell(cellCode));

                for (Direction direction : Direction.values())
                {
                    CellCode neighbour = cellCode.neighbour(direction);

                    if (neighbour != null)
                    {
                        LinkCode link = structure.getLinkCode(cellCode, neighbour);
                        updatedLinks.put(link, cellMap.getLinkDirection(link));
                    }

                    CellCode previousCell = cellCode;
                    while (neighbour != null)
                    {
                        LinkCode linkCode = structure.getLinkCode(previousCell, neighbour);

                        updatedChains.put(linkCode, cellMap.getChainLength(linkCode));

                        previousCell = neighbour;
                        neighbour = previousCell.neighbour(direction);
                    }
                }
            }

            state.updateFieldSnapshot(updatedCells, updatedLinks, updatedChains);
        }
    }
}