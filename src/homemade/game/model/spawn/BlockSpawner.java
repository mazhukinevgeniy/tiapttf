package homemade.game.model.spawn;

import homemade.game.fieldstructure.CellCode;
import homemade.game.model.BlockValuePool;
import homemade.game.model.Cell;
import homemade.game.model.CellState;
import homemade.game.model.cellmap.CellMapReader;
import homemade.game.model.cellstates.BlockState;

import java.util.*;

class BlockSpawner {
    private CellMapReader cellMap;
    private BlockValuePool blockValuePool;

    private Random random = new Random();

    BlockSpawner(CellMapReader cellMap, BlockValuePool blockValuePool) {
        this.cellMap = cellMap;
        this.blockValuePool = blockValuePool;
    }

    Map<CellCode, CellState> spawnBlocks(Iterator<CellCode> iterator, int blocksToImmobilize) {
        Map<CellCode, CellState> changes = new HashMap<>();

        LinkedList<CellCode> cells = new LinkedList<>();

        while (iterator.hasNext()) {
            CellCode cellCode = iterator.next();

            if (cellMap.getCell(cellCode).type() == Cell.MARKED_FOR_SPAWN)
                cells.add(cellCode);
        }

        for (int cSize = cells.size(); blocksToImmobilize > 0 && cSize > 0; ) {
            int pos = random.nextInt(cSize);

            changes.put(cells.remove(pos),
                    new BlockState(blockValuePool.takeBlockValue(), false, null));

            blocksToImmobilize--;
            cSize--;
        }

        for (int i = 0, size = cells.size(); i < size; i++) {
            changes.put(cells.get(i), new BlockState(blockValuePool.takeBlockValue(), true, null));
        }

        return changes;
    }
}
