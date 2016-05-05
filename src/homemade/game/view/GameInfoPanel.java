package homemade.game.view;

import homemade.game.controller.ViewListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GameInfoPanel
{
    private JPanel container;

    private ViewListener listener;

    GameInfoPanel(ViewListener listener, JLayeredPane layers, int width, int height)
    {
        this.listener = listener;

        container = new JPanel();
        container.setPreferredSize(new Dimension(width, height));
        container.setBounds(0, 0, width, height);
        //TODO: remove if it's not going to be useful

        layers.add(getButton(width, height), GameView.PANEL_LAYER);
    }

    private JButton getButton(int panelWidth, int panelHeight)
    {
        JButton button = new JButton("Quit");

        int width = 80;
        int height = 25;
        int offsetX = 20;

        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Verdana", Font.PLAIN, 13));
        button.setBounds(panelWidth - offsetX - width, (panelHeight - height) / 2, width, height);

        button.addActionListener(new QuitListener());

        return button;
    }

    private class QuitListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            listener.quit();
        }
    }
}
