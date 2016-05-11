package homemade.menu.model.records;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * This class has a natural ordering that is inconsistent with equals:
 * player name is considered in equals but not in compareTo
 */
public class Record implements Comparable<Record>
{
    private static final String SEPARATOR = "&";

    private int score;
    private String playerName;
    private LocalDateTime date;

    private Record() {}

    Record(int score, String playerName, LocalDateTime date)
    {
        this.score = score;
        this.playerName = playerName;
        this.date = date;
    }

    public int getScore()
    {
        return score;
    }

    public String getPlayerName()
    {
        return  playerName;
    }

    public LocalDateTime getDate()
    {
        return date;
    }

    @Override
    public String toString()
    {
        String string = String.valueOf(score) + SEPARATOR;
        string += playerName + SEPARATOR;
        string += date.toString();
        return string;
    }

    public static Record valueOf(String string)
    {
        Pattern pattern = Pattern.compile(SEPARATOR);
        String[] fields = pattern.split(string);

        Record record = null;

        if (fields.length == 3)
        {
            record = new Record();
            record.playerName = fields[1];

            try
            {
                record.score = Integer.parseInt(fields[0]);
                record.date = LocalDateTime.parse(fields[2]);
            }
            catch (NumberFormatException | DateTimeParseException e)
            {
                record = null;
            }
        }

        if (record == null)
            System.out.println("can't recover record: " + string);

        return record;
    }

    @Override
    public int compareTo(Record otherRecord)
    {
        int otherScore = otherRecord.score;
        boolean comparableByScore = score != otherScore;

        return comparableByScore ? (score > otherScore ? -1 : 1) : date.compareTo(otherRecord.date);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj != null && obj.getClass() == getClass())
        {
            Record record = (Record) obj;

            return score == record.score && playerName.equals(record.playerName) && date.equals(record.date);
        }
        else
            return false;
    }

    @Override
    public int hashCode()
    {
        return score + playerName.length() + date.hashCode();
    }
}
