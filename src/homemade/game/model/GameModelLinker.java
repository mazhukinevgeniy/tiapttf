package homemade.game.model;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.cellmap.Cell;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.cellmap.Link;
import homemade.game.model.combo.ComboDetector;
import homemade.utils.QuickMap;

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

        if (cellMap.getCell(cellCodeTo).getValue() == Game.CELL_MARKED_FOR_SPAWN)
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
