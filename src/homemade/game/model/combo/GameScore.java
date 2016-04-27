package homemade.game.model.combo;

import homemade.game.Combo;
import homemade.game.GameSettings;
import homemade.game.controller.ScoreHandler;
import homemade.game.fieldstructure.FieldStructure;

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
        int baseScore = 100;

        scores = new int[lengthsPossible];

        for (int i = 0; i < lengthsPossible; i++)
        {
            scores[i] = baseScore * (1 + i);
        }
    }

    //TODO: find combos on linker level, then send them there
    void handleCombos(ComboPack pack)
    {
        int packScore = 0;

        for (Iterator<Combo> iterator = pack.comboIterator(); iterator.hasNext(); )
        {
            Combo next = iterator.next();

            packScore += scores[next.getLength() - minCombo];
        }

        int packMultiplier = pack.numberOfCombos();
        packScore *= packMultiplier;

        if (packScore != 0)
            synchronized(this)
            {
                score += packScore;

                scoreHandler.scoreUpdated(score);
            }
    }
}
