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
    };

    private List<Map<Direction, Image>> assets;

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
        throw new RuntimeException("isn't implemented yet");//TODO: implement
    }

    private void loadColoredLinks()
    {
        List<String> images = new ArrayList<String>(3);
        images.add("arrow_red.png");
        images.add("arrow_orange.png");
        images.add("arrow_green.png");

        List<Map<Direction, Image>> listOfMaps = new ArrayList<>(3);

        Map<Direction, Double> angles = new EnumMap<>(Direction.class);
        angles.put(Direction.BOTTOM, 0.0);
        angles.put(Direction.LEFT, Math.PI / 2);
        angles.put(Direction.TOP, Math.PI);
        angles.put(Direction.RIGHT, 3 * Math.PI / 2);

        for (String imageName : images)
        {
            Map<Direction, Image> arrows = new EnumMap<>(Direction.class);
            listOfMaps.add(arrows);

            Image baseImage = getImage(imageName);

            for (Map.Entry<Direction, Double> entry : angles.entrySet())
            {
                arrows.put(entry.getKey(), createRotatedCopy(baseImage, entry.getValue()));
            }
        }

        assets = listOfMaps;
    }


    public int getNumberOfArrowTiers()
    {
        return assets.size();
    }

    public int getNumberOfArrowFrames()
    {
        return 1; //TODO: return actual value
    }

    public Image getArrow(Direction direction, int tier, int frame)
    {
        return assets.get(tier).get(direction);//TODO: use frame
    }
}
