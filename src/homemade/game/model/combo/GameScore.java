package homemade.game.model.combo;

import homemade.game.Game;
import homemade.game.controller.ScoreHandler;

/**
 * Created by user3 on 02.04.2016.
 */
class GameScore
{
    private int score = 0;
    private int scores[];

    private ScoreHandler scoreHandler;

    GameScore(ScoreHandler scoreHandler)
    {
        this.scoreHandler = scoreHandler;

        int lengthsPossible = Math.max(Game.FIELD_WIDTH, Game.FIELD_HEIGHT) - Game.MIN_COMBO + 1;
        int baseScore = 100;

        scores = new int[lengthsPossible];

        for (int i = 0; i < lengthsPossible; i++)
        {
            scores[i] = baseScore * (1 + i);
        }
    }

    synchronized void handleCombo(int length)
    {
        score += scores[length - Game.MIN_COMBO];

        scoreHandler.scoreUpdated(score);
    }
}
