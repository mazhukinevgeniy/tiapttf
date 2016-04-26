package homemade.game.view.layers;

import homemade.game.GameSettings;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.view.GameView;
import homemade.utils.PiecewiseConstantFunction;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;

class LinkLayer extends RenderingLayer.Links
{
    private EnumMap<Direction, Offset> offsets;
    private PiecewiseConstantFunction<Double, Integer> chainLengthToSpriteTier;

    LinkLayer(GameSettings settings)
    {
        super();

        offsets = new EnumMap<Direction, Offset>(Direction.class);

        for (Direction direction : Direction.values())
        {
            Image img = assets.getArrow(direction, 0);

            int width = direction.isHorizontal() ? 2 : 1;
            int height = direction.isHorizontal() ? 1 : 2;

            offsets.put(direction, new Offset(img, width, height));
        }

        double minCombo = (double) settings.minCombo();

        ArrayList<Double> separators = new ArrayList<Double>(2);
        separators.add(minCombo - 2.5);
        separators.add(minCombo - 1.5);

        ArrayList<Integer> spriteTier = new ArrayList<Integer>(3);
        spriteTier.add(2);
        spriteTier.add(1);
        spriteTier.add(0);

        if (assets.getNumberOfArrowTiers() != 3)
            throw new RuntimeException("wrong assumptions in linklayer");

        chainLengthToSpriteTier = new PiecewiseConstantFunction<>(separators, spriteTier);
    }

    @Override
    protected void renderForLink(LinkCode linkCode)
    {
        Direction linkDirection = state.getLinkBetweenCells(linkCode);

        if (linkDirection != null)
        {
            Double comboLength = (double) state.getChainLength(linkCode);
            int spriteTier = chainLengthToSpriteTier.getValueAt(comboLength);

            Image sprite = assets.getArrow(linkDirection, spriteTier);
            Offset offset = offsets.get(linkDirection);

            graphics.drawImage(sprite, canvasX + offset.x, canvasY + offset.y, null);
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
