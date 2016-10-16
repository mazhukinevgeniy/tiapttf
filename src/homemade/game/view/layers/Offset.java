package homemade.game.view.layers;

import homemade.game.view.GameView;

import java.awt.*;

class Offset
{
    int x, y;

    Offset(Image image, int horizontalCells, int verticalCells)
    {
        x = (   GameView.CELL_WIDTH * horizontalCells +
                GameView.CELL_OFFSET * (horizontalCells - 1) -
                image.getWidth(null)) / 2;
        y = (   GameView.CELL_WIDTH * verticalCells +
                GameView.CELL_OFFSET * (verticalCells - 1) -
                image.getHeight(null)) / 2;
    }
}
//TODO: check if this class is useful elsewhere