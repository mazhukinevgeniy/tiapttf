package homemade.game.model;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.combo.ComboDetector;
import homemade.utils.QuickMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameModelLinker
{
    private static final boolean AUTOCOMPLETION = true;

    private FieldStructure structure;

    private CellMap cellMap;
    private ComboDetector comboDetector;
    private NumberPool numberPool;
    private ArrayBasedGameState state;

    GameModelLinker(FieldStructure structure, CellMap cellMap, ComboDetector comboDetector, NumberPool numberPool, ArrayBasedGameState state)
    {
        this.structure = structure;
        this.cellMap = cellMap;
        this.comboDetector = comboDetector;
        this.numberPool = numberPool;
        this.state = state;
    }

    public FieldStructure getStructure() { return structure; }


    synchronized public void requestSpawn(Map<CellCode, Integer> changes)
    {
        Set<CellCode> appliedChanges = cellMap.applyCascadeChanges(changes);
        actOnChangedCells(appliedChanges);
    }

    synchronized public void requestBlockMove(CellCode cellCodeFrom, CellCode cellCodeTo)
    {
        boolean riskOfSpawnDenial = false;

        if (cellMap.getCellValue(cellCodeTo) == Game.CELL_MARKED_FOR_SPAWN)
            riskOfSpawnDenial = true;

        Set<CellCode> changes = cellMap.tryCascadeChanges(cellCodeFrom, cellCodeTo);
        if (changes != null)
        {
            actOnChangedCells(changes);

            if (riskOfSpawnDenial)
                state.incrementDenyCounter();
        }
    }

    public GameState copyGameState()
    {
        return state.getImmutableCopy();
    }

    private void actOnChangedCells(Set<CellCode> changedCells)
    {
        if (changedCells.size() > 0)
        {
            if (AUTOCOMPLETION)
            {
                Map<CellCode, Integer> removedCells = QuickMap.getCleanCellCodeIntMap();

                Set<CellCode> cellsToRemove = comboDetector.findCellsToRemove(changedCells);

                for (CellCode cellCode : cellsToRemove)
                {
                    numberPool.freeNumber(cellMap.getCellValue(cellCode));

                    removedCells.put(cellCode, Game.CELL_EMPTY);
                }

                cellMap.applyCascadeChanges(removedCells);

                changedCells.addAll(removedCells.keySet());
            }

            Map<CellCode, Integer> updatedCells = QuickMap.getCleanCellCodeIntMap();
            Map<LinkCode, Direction> updatedLinks = QuickMap.getCleanLinkCodeDirectionMap();
            Map<LinkCode, Integer> updatedChains = new HashMap<>();

            for (CellCode cellCode : changedCells)
            {
                updatedCells.put(cellCode, cellMap.getCellValue(cellCode));

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
