package homemade.menu.model.records;

/**
 * Created by Marid on 29.04.2016.
 */
public class Record
{
    private int score;
    private String playerName;
    private String dataTime;

    private Record() {}

    public Record(int score, String playerName, String dataTime)
    {
        this.score = score;
        this.playerName = playerName;
        this.dataTime = dataTime;
    }

    public int getScore()
    {
        return score;
    }

    public String getPlayerName()
    {
        return  playerName;
    }

    public String getDataTime()
    {
        return dataTime;
    }
}
