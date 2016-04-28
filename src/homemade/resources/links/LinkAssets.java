package homemade.resources.links;

import homemade.game.fieldstructure.Direction;
import homemade.utils.AssetLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class LinkAssets extends AssetLoader
{
    public enum Variation
    {
        ANIMATED, COLORED
    }

    private List<List<Map<Direction, Image>>> assets;

    public LinkAssets(Variation variation)
    {
        if (variation == Variation.ANIMATED)
            loadAnimatedLinks();
        else if (variation == Variation.COLORED)
            loadColoredLinks();
        else
            throw new RuntimeException("can't load link assets");
    }

    private void loadAnimatedLinks()
    {
        assets = new ArrayList<>(6);

        for (int frame = 1; frame < 7; frame++)
        {
            List<Map<Direction, Image>> listOfMaps = new ArrayList<>(1);

            Map<Direction, Double> angles = getAngles(-Math.PI / 2);

            Image baseImage = getImage(frame + ".png");
            listOfMaps.add(getRotatedLinks(baseImage, angles));

            assets.add(listOfMaps);
        }
    }

    private void loadColoredLinks()
    {
        List<String> images = new ArrayList<String>(3);
        images.add("arrow_red.png");
        images.add("arrow_orange.png");
        images.add("arrow_green.png");

        List<Map<Direction, Image>> listOfMaps = new ArrayList<>(3);

        Map<Direction, Double> angles = getAngles(0.0);

        for (String imageName : images)
        {
            Image baseImage = getImage(imageName);
            listOfMaps.add(getRotatedLinks(baseImage, angles));
        }

        assets = new ArrayList<>(1);
        assets.add(listOfMaps);
    }

    private Map<Direction, Double> getAngles(double rotateToBottom)
    {
        Map<Direction, Double> angles = new EnumMap<>(Direction.class);
        angles.put(Direction.BOTTOM, rotateToBottom + 0.0);
        angles.put(Direction.LEFT, rotateToBottom + Math.PI / 2);
        angles.put(Direction.TOP, rotateToBottom + Math.PI);
        angles.put(Direction.RIGHT, rotateToBottom + 3 * Math.PI / 2);

        return angles;
    }

    private Map<Direction, Image> getRotatedLinks(Image baseImage, Map<Direction, Double> angles)
    {
        Map<Direction, Image> arrows = new EnumMap<>(Direction.class);

        for (Map.Entry<Direction, Double> entry : angles.entrySet())
        {
            arrows.put(entry.getKey(), createRotatedCopy(baseImage, entry.getValue()));
        }

        return arrows;
    }


    public int getNumberOfArrowTiers()
    {
        return assets.get(0).size();
    }

    public int getNumberOfArrowFrames()
    {
        return assets.size();
    }

    public Image getArrow(Direction direction, int tier, int frame)
    {
        return assets.get(frame).get(tier).get(direction);
    }
}
