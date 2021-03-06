package homemade.game.model;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.GameState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.cellstates.SimpleState;

import java.util.Map;

/**
 * Must be synchronized externally!
 */
class ArrayBasedGameState implements GameState {
    private CellState[] field;
    private Direction[] links;
    private int[] chainLengths;

    private GameState immutableCopy;

    private int numberOfBlocks = 0;
    private int numberOfMovableBlocks = 0;
    private int denies = 0;

    private int score = 0;
    private int multiplier = 1;

    ArrayBasedGameState(FieldStructure structure) {
        int fieldSize = structure.getFieldSize();

        field = new CellState[fieldSize];

        CellState empty = SimpleState.getSimpleState(Cell.EMPTY);
        for (int i = 0; i < fieldSize; i++) {
            field[i] = empty;
        }

        int numberOfLinks = structure.getNumberOfLinks();

        links = new Direction[numberOfLinks];

        for (int i = 0; i < numberOfLinks; i++) {
            links[i] = null;
        }

        chainLengths = new int[numberOfLinks];

        for (int i = 0; i < numberOfLinks; i++) {
            chainLengths[i] = 0;
        }
    }

    private ArrayBasedGameState(ArrayBasedGameState stateToCopy) {
        field = stateToCopy.field.clone();
        links = stateToCopy.links.clone();
        chainLengths = stateToCopy.chainLengths.clone();

        numberOfBlocks = stateToCopy.numberOfBlocks;
        numberOfMovableBlocks = stateToCopy.numberOfMovableBlocks;
        denies = stateToCopy.denies;

        score = stateToCopy.score;
        multiplier = stateToCopy.multiplier;
    }

    void incrementDenyCounter() {
        denies++;
    }

    void updateScore(int newScore) {
        score = newScore;
    }

    void updateMultiplier(int newMultiplier) {
        immutableCopy = null;
        multiplier = newMultiplier;
    }


    void updateFieldSnapshot(Map<CellCode, CellState> cellUpdates,
                             Map<LinkCode, Direction> linkUpdates,
                             Map<LinkCode, Integer> chainUpdates) {
        immutableCopy = null;

        for (Map.Entry<CellCode, CellState> entry : cellUpdates.entrySet()) {
            int pos = entry.getKey().hashCode();
            CellState newState = entry.getValue();
            CellState oldState = field[pos];

            if (!oldState.isAnyBlock() && newState.isAnyBlock()) {
                numberOfBlocks++;
            } else if (oldState.isAnyBlock() && !newState.isAnyBlock()) {
                numberOfBlocks--;
            }

            if (!oldState.isMovableBlock() && newState.isMovableBlock()) {
                numberOfMovableBlocks++;
            } else if (oldState.isMovableBlock() && !newState.isMovableBlock()) {
                numberOfMovableBlocks--;
            }

            field[pos] = newState;
        }

        for (Map.Entry<LinkCode, Direction> entry : linkUpdates.entrySet()) {
            links[entry.getKey().hashCode()] = entry.getValue();
        }

        for (Map.Entry<LinkCode, Integer> entry : chainUpdates.entrySet()) {
            chainLengths[entry.getKey().hashCode()] = entry.getValue();
        }
    }

    @Override
    public int spawnsDenied() {
        return denies;
    }

    @Override
    public int numberOfBlocks() {
        return numberOfBlocks;
    }

    @Override
    public int numberOfMovableBlocks() {
        return numberOfMovableBlocks;
    }

    @Override
    public int gameScore() {
        return score;
    }

    @Override
    public int globalMultiplier() {
        return multiplier;
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

    GameState getImmutableCopy() {
        if (immutableCopy == null)
            immutableCopy = new ArrayBasedGameState(this);

        return immutableCopy;
    }
}
