package homemade.game.model;

import homemade.game.fieldstructure.CellCode;
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

}
