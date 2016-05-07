package homemade.menu.model.records;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Created by Marid on 29.04.2016.
 */
public class Record
{
    private static final String SEPARATOR = "&";

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

    @Override
    public String toString()
    {
        String string = String.valueOf(score) + SEPARATOR;
        string += playerName + SEPARATOR;
        string += dataTime;
        return string;
    }

    public static Record valueOf(String string)
    {
        Pattern pattern = Pattern.compile(SEPARATOR);
        String[] fields = pattern.split(string);

        Record record = new Record();
        try
        {
            record.score = Integer.parseInt(fields[0]);
            record.playerName = fields[1];

            LocalDateTime.parse(fields[2]);
            record.dataTime = fields[2];
        }
        catch (Exception e)
        {
            e.printStackTrace();
            record = null;
        }
        return record;
    }

}
