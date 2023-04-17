package homemade.game.model;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.cellstates.SimpleState;
import homemade.game.state.GameState;
import homemade.game.state.GameStateEncoder;

import java.util.Map;

public class ArrayBasedGameState extends GameState {
    private CellState[] field;
    private Direction[] links;
    private int[] chainLengths;

    private GameState immutableCopy;

    ArrayBasedGameState(FieldStructure structure) {
        super(structure, 0, 0, 1);
        int fieldSize = structure.fieldSize;

        field = new CellState[fieldSize];

        CellState empty = SimpleState.getSimpleState(Cell.EMPTY);
        for (int i = 0; i < fieldSize; i++) {
            field[i] = empty;
        }

        int numberOfLinks = structure.numberOfLinks;

        links = new Direction[numberOfLinks];

        for (int i = 0; i < numberOfLinks; i++) {
            links[i] = null;
        }

        chainLengths = new int[numberOfLinks];

        for (int i = 0; i < numberOfLinks; i++) {
            chainLengths[i] = 0;
        }
    }

    void incrementDenyCounter() {
        setSpawnsDenied(getSpawnsDenied() + 1);
    }

    void updateScore(int newScore) {
        setGameScore(newScore);
    }

    void updateMultiplier(int newMultiplier) {
        immutableCopy = null;
        setGlobalMultiplier(newMultiplier);
    }


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
    }

    @Override
    public CellState getCellState(CellCode cellCode) {
        return this.field[cellCode.hashCode()];
    }

    @Override
    public Direction getLinkBetweenCells(LinkCode linkCode) {
        return links[linkCode.hashCode()];
    }

    @Override
    public int getChainLength(LinkCode linkCode) {
        return chainLengths[linkCode.hashCode()];
    }

    public GameState createImmutableCopy() {
        if (immutableCopy == null) {
            immutableCopy = new GameStateEncoder().encode(this);
        }

        return immutableCopy;
    }
}
