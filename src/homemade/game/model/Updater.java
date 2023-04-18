package homemade.game.model;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.combo.ComboPack;
import homemade.game.pipeline.operations.GameScore;
import homemade.game.state.impl.CellMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Used by GameModelLinker for tasks such as updating cellmap and gamestate
 */
class Updater {
    private GameScore gameScore;

    private Set<CellCode> storedChanges;
    private ComboPack storedCombos;

    Updater(GameModelLinker linker, CellMap cellMap, GameScore gameScore) {
        this.gameScore = gameScore;

        storedChanges = new HashSet<>();
        storedCombos = new ComboPack();
    }

    void takeChanges(Map<CellCode, CellState> changedCells) {
        Set<CellCode> cellMapChanges = cellMap.applyCascadeChanges(changedCells);
        storedChanges.addAll(cellMapChanges);
    }

    void takeComboChanges(Map<CellCode, CellState> changedCells) {
        Set<CellCode> cellMapChanges = cellMap.applyCascadeChanges(changedCells);
        storedChanges.addAll(cellMapChanges);

        ComboPack newCombos = comboDetector.findCombos(cellMapChanges);
        storedCombos.append(newCombos);

        Set<CellCode> comboCells = cellMap.applyCascadeChanges(removeCombos(newCombos));
        storedChanges.addAll(comboCells);
    }

    void flush(int multiplier) {
        gameScore.handleCombos(storedCombos, multiplier);
        storedCombos = new ComboPack();

        updateState(storedChanges);
        storedChanges.clear();
    }

    boolean hasCellChanges() {
        return !storedChanges.isEmpty();
    }

    boolean hasCombos() {
        return storedCombos.numberOfCombos() > 0;
    }

    int comboPackTier() {
        return storedCombos.packTier();
    }

    private void updateState(Set<CellCode> changedCells) {
        if (changedCells.size() > 0) {
            //maps in ProcessingInfo

            for (CellCode cellCode : changedCells) {
                updatedCells.put(cellCode, cellMap.getCell(cellCode));

                for (Direction direction : Direction.values()) {
                    CellCode neighbour = cellCode.neighbour(direction);

                    if (neighbour != null) {
                        LinkCode link = structure.getLinkCode(cellCode, neighbour);
                        updatedLinks.put(link, cellMap.getLinkDirection(link));
                    }

                    CellCode previousCell = cellCode;
                    while (neighbour != null) {
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
