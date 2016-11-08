package homemade.game.model.combo;

import homemade.game.Cell;

import java.util.LinkedList;

public class ComboEffectVendor
{
    private static final int EXPLOSION_COST = 4;
    private static final int BONUS_TIER_COST = 2;

    //TODO: make some kind of two arrays with costs and effects

    public void addComboEffectsForTier(LinkedList<Cell.ComboEffect> effects, int tier)
    {
        int explosions = tier / EXPLOSION_COST;
        tier = tier - explosions * EXPLOSION_COST;

        int bonusTiers = tier / BONUS_TIER_COST;
        tier = tier - bonusTiers * BONUS_TIER_COST;

        int bonusMultipliers = tier;

        for (int i = 0; i < explosions; i++)
            effects.add(Cell.ComboEffect.EXPLOSION);

        for (int i = 0; i < bonusTiers; i++)
            effects.add(Cell.ComboEffect.JUST_EXTRA_TIER);

        for (int i = 0; i < bonusMultipliers; i++)
            effects.add(Cell.ComboEffect.EXTRA_MULTIPLIER);

        //TODO: sort effects so that most powerful and expensive are first
    }
}
