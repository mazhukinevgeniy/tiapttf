package homemade.game.model.spawn;

import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.GameModelLinker;
import homemade.game.model.GameSettings;
import homemade.game.model.cellmap.CellMapReader;
import homemade.game.pipeline.operations.CellMarker;
import homemade.game.state.impl.BlockValuePool;

public class SpawnManager {
    private BlockSpawner spawner;
    private CellMarker cellMarker;

    private int simultaneousSpawn;

    private FieldStructure structure;

    public SpawnManager(GameModelLinker linker, BlockValuePool blockValuePool) {
        structure = linker.getStructure();

        CellMapReader cellMap = linker.getMapReader();
        spawner = new BlockSpawner(cellMap, blockValuePool);
        cellMarker = new CellMarker(cellMap, blockValuePool);

        GameSettings settings = linker.getSettings();
        simultaneousSpawn = settings.getSpawn();
    }

}
