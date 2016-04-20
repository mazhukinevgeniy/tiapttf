package homemade.game.model.combo;

import homemade.game.GameSettings;
import homemade.game.controller.ScoreHandler;
import homemade.game.fieldstructure.FieldStructure;

/**
 * Created by user3 on 02.04.2016.
 */
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

    synchronized void handleCombo(int length)
    {
        score += scores[length - minCombo];

        scoreHandler.scoreUpdated(score);
    }
}
