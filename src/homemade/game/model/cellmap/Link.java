package homemade.game.model.cellmap;

import homemade.game.fieldstructure.LinkCode;

/**
 * Created by user3 on 27.03.2016.
 */
public class Link
{
    boolean value = false;

    private LinkCode code;

    Link(LinkCode code)
    {
        this.code = code;
    }

    //TODO: check if we even need this class

    public LinkCode getCode()
    {
        return code;
    }

    public boolean getValue()
    {
        return value;
    }
}
