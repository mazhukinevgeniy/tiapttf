package homemade.game.model;

import homemade.game.CellCode;
import homemade.game.Direction;
import homemade.game.Game;
import homemade.game.model.cellmap.Cell;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.cellmap.Link;
import homemade.game.model.combo.ComboDetector;
import homemade.utils.QuickMap;

import java.util.Map;
import java.util.Set;

public class GameModelLinker
{
    private CellMap cellMap;
    private ComboDetector comboDetector;
    private NumberPool numberPool;
    private ArrayBasedGameState state;

    GameModelLinker(CellMap cellMap, ComboDetector comboDetector, NumberPool numberPool, ArrayBasedGameState state)
    {
        this.cellMap = cellMap;
        this.comboDetector = comboDetector;
        this.numberPool = numberPool;
        this.state = state;
    }


    public void requestSpawn(Map<CellCode, Integer> changes)
    {
        Set<CellCode> appliedChanges = cellMap.applyCascadeChanges(changes);
        actOnChangedCells(appliedChanges);
    }

    public void requestBlockMove(CellCode cellCodeFrom, CellCode cellCodeTo)
    {
        Set<CellCode> changes = cellMap.tryCascadeChanges(cellCodeFrom, cellCodeTo);
        if (changes != null)
            actOnChangedCells(changes);
    }

    private void actOnChangedCells(Set<CellCode> changedCells)
    {
        if (changedCells.size() > 0)
        {
            if (Game.AUTOCOMPLETION)
            {
                Map<CellCode, Integer> removedCells = QuickMap.getCleanCellCodeIntMap();

                Set<CellCode> cellsToRemove = comboDetector.findCellsToRemove(changedCells);

                for (CellCode cellCode : cellsToRemove)
                {
                    numberPool.freeNumber(cellMap.getCell(cellCode).getValue());

                    removedCells.put(cellCode, Game.CELL_EMPTY);
                }

                cellMap.applyCascadeChanges(removedCells);

                changedCells.addAll(removedCells.keySet());
            }

            Map<CellCode, Integer> updatedCells = QuickMap.getCleanCellCodeIntMap();
            Map<Integer, Boolean> updatedLinks = QuickMap.getCleanIntBoolMap();

            for (CellCode cellCode : changedCells)
            {
                Cell cell = cellMap.getCell(cellCode);

                updatedCells.put(cellCode, cell.getValue());

                for (Direction direction : Direction.values())
                {
                    Link link = cell.link(direction);

                    if (link != null)
                    {
                        updatedLinks.put(link.getNumber(), link.getValue());
                    }
                }
            }

            state.updateFieldSnapshot(updatedCells, updatedLinks);
        }
    }
}
