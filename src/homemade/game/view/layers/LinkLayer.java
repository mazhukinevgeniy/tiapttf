package homemade.game.view.layers;

import homemade.game.GameSettings;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.resources.links.LinkAssets;
import homemade.utils.PiecewiseConstantFunction;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;

class LinkLayer extends RenderingLayer.Links {
    private EnumMap<Direction, Offset> offsets;
    private PiecewiseConstantFunction<Double, Integer> chainLengthToSpriteTier;

    private int counter = 0;
    private int countCap;
    private final int rendersPerLinkFrame = 10; //TODO: better link it to the fps and/or assets

    private LinkAssets linkAssets;

    LinkLayer(GameSettings settings) {
        super();

        linkAssets = assets.getLinkAssets();

        offsets = new EnumMap<Direction, Offset>(Direction.class);

        for (Direction direction : Direction.values()) {
            Image img = linkAssets.getArrow(direction, 0, 0);

            int width = direction.isHorizontal() ? 2 : 1;
            int height = direction.isHorizontal() ? 1 : 2;

            offsets.put(direction, new Offset(img, width, height));
        }

        countCap = rendersPerLinkFrame * linkAssets.getNumberOfArrowFrames();

        double minCombo = (double) settings.getMinCombo();
        int minSpriteTier = linkAssets.getNumberOfArrowTiers();

        ArrayList<Double> separators = new ArrayList<Double>(minSpriteTier - 1);
        ArrayList<Integer> spriteTier = new ArrayList<Integer>(minSpriteTier);

        for (int i = 0; i < minSpriteTier - 1; i++) {
            separators.add(minCombo - minSpriteTier + 0.5 + i);
        }

        for (int i = 0; i < minSpriteTier; i++) {
            spriteTier.add(minSpriteTier - (i + 1));
        }

        chainLengthToSpriteTier = new PiecewiseConstantFunction<>(separators, spriteTier);
    }

    @Override
    protected void renderForLink(LinkCode linkCode) {
        Direction linkDirection = state.getLinkBetweenCells(linkCode);

        if (linkDirection != null) {
            Double comboLength = (double) state.getChainLength(linkCode);
            int spriteTier = chainLengthToSpriteTier.getValueAt(comboLength);

            Image sprite = linkAssets.getArrow(linkDirection, spriteTier, counter / rendersPerLinkFrame);
            Offset offset = offsets.get(linkDirection);

            graphics.drawImage(sprite, canvasX + offset.x, canvasY + offset.y, null);
        }
    }

    @Override
    protected void iterate(FieldStructure structure) {
        super.iterate(structure);

        counter = (counter + 1) % countCap;
    }
}
