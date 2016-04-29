package homemade.game.model;

import homemade.game.Combo;
import homemade.game.GameSettings;
import homemade.game.controller.ScoreHandler;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.combo.ComboPack;

import java.util.Iterator;

class GameScore
{
    private int score;
    private int scores[];

    private int minCombo;

    private ScoreHandler scoreHandler;

    GameScore(FieldStructure structure, ScoreHandler scoreHandler, GameSettings settings)
    {
        this.scoreHandler = scoreHandler;
        scoreHandler.scoreUpdated(score = 0);

        minCombo = settings.minCombo();

        int lengthsPossible = structure.getMaxDimension() - minCombo + 1;
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
                int globalMultiplier = 1; //TODO: add spawn stunning and persistent global multiplier
                int packMultiplier = pack.numberOfCombos();
                packScore *= packMultiplier * (globalMultiplier + packMultiplier / 2);

                score += packScore;

                scoreHandler.scoreUpdated(score);
            }
    }
}
