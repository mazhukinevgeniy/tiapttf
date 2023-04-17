package homemade.game.model;

import homemade.game.Combo;
import homemade.game.model.combo.ComboPack;

import java.util.Iterator;

class GameScore {
    private int score;

    private GameModelLinker linker;

    GameScore(GameModelLinker linker) {
        this.linker = linker;
        linker.updateScore(score = 0);
    }

    void handleCombos(ComboPack pack, int globalMultiplier) {
        int packScore = 0;

        for (Iterator<Combo> iterator = pack.comboIterator(); iterator.hasNext(); ) {
            Combo next = iterator.next();

            packScore += getScore(next.getTier());
        }

        if (packScore != 0)
            synchronized (this) {
                packScore *= pack.packTier() * globalMultiplier;

                score += packScore;

                linker.updateScore(score);
                linker.modifyGlobalMultiplier(pack.multiplierIncrease());
            }
    }

    private int getScore(int tier) {
        int baseScore = 5;

        return baseScore * tier * tier;
    }
}
