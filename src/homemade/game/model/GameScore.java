package homemade.game.model;

import homemade.game.Combo;
import homemade.game.model.combo.ComboPack;

import java.util.Iterator;

class GameScore
{
    private int score;
    private int scores[];

    private int minCombo;

    private GameModelLinker linker;

    GameScore(GameModelLinker linker)
    {
        this.linker = linker;
        linker.updateScore(score = 0);

        minCombo = linker.getSettings().minCombo();

        int lengthsPossible = linker.getStructure().getMaxDimension() - minCombo + 1;
        int baseScore = 5;

        scores = new int[lengthsPossible];

        for (int i = 0; i < lengthsPossible; i++)
        {
            scores[i] = baseScore * (1 + i);
        }
    }

    void handleCombos(ComboPack pack)
    {
        int packScore = 0;

        for (Iterator<Combo> iterator = pack.comboIterator(); iterator.hasNext(); )
        {
            Combo next = iterator.next();

            packScore += scores[next.getLength() - minCombo];
        }

        if (packScore != 0)
            synchronized(this)
            {
                int globalMultiplier = linker.lastGameState().globalMultiplier();
                int packMultiplier = pack.numberOfCombos();
                packScore *= packMultiplier * (globalMultiplier + packMultiplier / 2);

                score += packScore;

                linker.updateScore(score);
                //TODO: increase global multiplier
                //TODO: add spawn stunning
            }
    }
}
