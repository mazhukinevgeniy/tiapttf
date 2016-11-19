package homemade.game.controller;

import homemade.game.fieldstructure.CellCode;

public interface BlockEventHandler {
    void blockRemoved(CellCode atCell);

    void blockExploded(CellCode atCell);
}