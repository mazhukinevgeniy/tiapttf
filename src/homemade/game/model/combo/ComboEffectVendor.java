package homemade.game.model.combo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class ComboEffectVendor {

    public void addComboEffectsForTier(LinkedList<ComboEffect> effects, int tier) {
        ComboEffect[] pricedEffects = ComboEffect.values();
        Comparator<ComboEffect> comparator = ComboEffect.getComparator();

        Arrays.sort(pricedEffects, comparator);

        for (int i = 0; i < pricedEffects.length && tier > 0; i++) {
            ComboEffect effect = pricedEffects[i];
            int price = effect.price;

            int given = tier / price;
            tier -= given * price;

            for (int j = 0; j < given; j++) {
                effects.add(effect);
            }
        }

        effects.sort(comparator);
    }
}
