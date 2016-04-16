package homemade.game.view.layers;

import homemade.game.Game;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.view.GameView;
import homemade.resources.Assets;
import homemade.utils.PiecewiseConstantFunction;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * As of 2.04.2016, this layer will not work properly if we don't call renderForCell for each cell
 */
class LinkLayer extends RenderingLayer
{
    private EnumSet<Direction> drawingDirections = EnumSet.of(Direction.BOTTOM, Direction.RIGHT);
    private List<Map<Direction, Image>> assets;
    //TODO: check if we can design safe asset storage

    private EnumMap<Direction, Offset> offsets;
    private PiecewiseConstantFunction<Double, Integer> chainLengthToSpriteTier;

    private FieldStructure structure;

    LinkLayer(FieldStructure structure)
    {
        super();

        this.structure = structure;

        assets = Assets.arrows;

        offsets = new EnumMap<Direction, Offset>(Direction.class);

        for (Direction direction : assets.get(0).keySet())
        {
            Image img = assets.get(0).get(direction);

            int width = direction.isHorizontal() ? 2 : 1;
            int height = direction.isHorizontal() ? 1 : 2;

            offsets.put(direction, new Offset(img, width, height));
        }

        ArrayList<Double> separators = new ArrayList<Double>(2);
        separators.add((double)Game.MIN_COMBO - 2.5);
        separators.add((double)Game.MIN_COMBO - 1.5);

        ArrayList<Integer> spriteTier = new ArrayList<Integer>(3);
        spriteTier.add(2);
        spriteTier.add(1);
        spriteTier.add(0);

        chainLengthToSpriteTier = new PiecewiseConstantFunction<Double, Integer>(separators, spriteTier);
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
                Direction linkDirection = state.getLinkBetweenCells(link);

                if (linkDirection != null)
                {
                    Double comboLength = (double) state.getChainLength(link);
                    int spriteTier = chainLengthToSpriteTier.getValueAt(comboLength);

                    Image sprite = assets.get(spriteTier).get(linkDirection);
                    Offset offset = offsets.get(linkDirection);

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
