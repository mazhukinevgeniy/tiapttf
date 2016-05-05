package homemade.game.view;

import javax.swing.*;
import java.awt.*;

class GameInfoPanel
{
    private JPanel container;

    GameInfoPanel(JLayeredPane layers, int width, int height)
    {
        container = new JPanel();
        container.setPreferredSize(new Dimension(width, height));
        container.setBounds(0, 0, width, height);

        layers.add(container, GameView.PANEL_LAYER);
    }
}
