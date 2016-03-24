package homemade.game.controller;

/**
 * Created by user3 on 24.03.2016.
 */
public class Selection
{
    boolean active = false;

    int x, y;
    //TODO: add range support

    Selection()
    {

    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public boolean isActive() { return this.active; }
}
