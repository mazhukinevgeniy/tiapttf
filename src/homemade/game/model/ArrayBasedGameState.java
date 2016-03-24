package homemade.game.model;

import homemade.game.Game;
import homemade.game.GameState;

/**
 * Created by user3 on 24.03.2016.
 */
class ArrayBasedGameState implements GameState
{
    private int[] field;

    ArrayBasedGameState(int[] data)
    {
        this.field = data;
    }

    @Override
    public int getCellValue(int cellX, int cellY)
    {
        //System.out.println("returning cell value from gamestate, " + this.field[cellX + Game.FIELD_WIDTH * cellY]);
        return this.field[cellX + Game.FIELD_WIDTH * cellY];
    }
}
