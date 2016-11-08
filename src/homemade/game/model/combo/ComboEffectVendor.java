package homemade.game.model.combo;

import homemade.game.Cell.ComboEffect;

import java.util.Arrays;
import java.util.LinkedList;

public class ComboEffectVendor
{
    private int[] costs = new int[]{
            4,
            2,
            1};
    private ComboEffect[] pricedEffects = new ComboEffect[]{
            ComboEffect.EXPLOSION,
            ComboEffect.JUST_EXTRA_TIER,
            ComboEffect.EXTRA_MULTIPLIER};

    public void addComboEffectsForTier(LinkedList<ComboEffect> effects, int tier)
    {
        for (int i = 0; i < costs.length; i++)
        {
            int given = tier / costs[i];
            tier -= given * costs[i];

            for (int j = 0; j < given; j++)
            {
                effects.add(pricedEffects[i]);
            }
        }

        effects.sort(ComboEffect.getComparator());
    }
}