package homemade.game.view.layers;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.view.GameView;
import homemade.resources.Assets;

import java.awt.*;
import java.util.EnumMap;
import java.util.EnumSet;

/**
 * As of 2.04.2016, this layer will not work properly if we don't call renderForCell for each cell
 */
class LinkLayer extends RenderingLayer
{
    private EnumSet<Direction> drawingDirections = EnumSet.of(Direction.BOTTOM, Direction.RIGHT);
    private EnumMap<Direction, Image> assets;

    private EnumMap<Direction, Offset> offsets;

    private FieldStructure structure;

    LinkLayer(FieldStructure structure)
    {
        super();

        this.structure = structure;

        assets = new EnumMap<Direction, Image>(Direction.class);
        assets.put(Direction.RIGHT, Assets.glowHorizontal);
        assets.put(Direction.BOTTOM, Assets.glowVertical);

        offsets = new EnumMap<Direction, Offset>(Direction.class);
        offsets.put(Direction.RIGHT, new Offset(Assets.glowHorizontal, 2, 1));
        offsets.put(Direction.BOTTOM, new Offset(Assets.glowVertical, 1, 2));


    }

    @Override
    void renderForCell(CellCode cellCode)
    {
        for (Direction direction : drawingDirections)
        {
            CellCode neighbour = cellCode.neighbour(direction);

            if (neighbour != null)
            {
                LinkCode link = structure.getLinkCode(cellCode, neighbour);

                if (state.getLinkBetweenCells(link))
                {
                    Image sprite = assets.get(direction);
                    Offset offset = offsets.get(direction);

                    graphics.drawImage(sprite, canvasX + offset.x, canvasY + offset.y, null);
                }
            }
        }
    }

    private static final class Offset
    {
        int x, y;

        private Offset(Image image, int horizontalCells, int verticalCells)
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
}
