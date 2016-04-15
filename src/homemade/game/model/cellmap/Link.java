package homemade.game.model.cellmap;

import homemade.game.fieldstructure.LinkCode;

/**
 * Created by user3 on 27.03.2016.
 */
class Link
{
    boolean value = false;
    LinkCode code;

    Link(LinkCode code)
    {
        this.code = code;
    }
}
