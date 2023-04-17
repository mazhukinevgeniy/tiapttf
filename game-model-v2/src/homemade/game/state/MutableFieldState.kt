package homemade.game.state

import homemade.game.fieldstructure.FieldStructure
import homemade.game.state.impl.BlockValuePool
import homemade.game.state.impl.CellMap

class MutableFieldState(
        override val structure: FieldStructure,
        blockValuePool: BlockValuePool
) : CellMap(structure, blockValuePool)

/*
    void updateFieldSnapshot(Map<CellCode, CellState> cellUpdates,
                             Map<LinkCode, Direction> linkUpdates,
                             Map<LinkCode, Integer> chainUpdates) {
        immutableCopy = null;

        for (Map.Entry<CellCode, CellState> entry : cellUpdates.entrySet()) {
            field[entry.getKey().hashCode()] = entry.getValue();
        }

        for (Map.Entry<LinkCode, Direction> entry : linkUpdates.entrySet()) {
            links[entry.getKey().hashCode()] = entry.getValue();
        }

        for (Map.Entry<LinkCode, Integer> entry : chainUpdates.entrySet()) {
            chainLengths[entry.getKey().hashCode()] = entry.getValue();
        }
    }*/
