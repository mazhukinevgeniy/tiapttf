package homemade.shell;

import homemade.shell.model.Parameter;

import javax.swing.*;

/**
 * Created by Marid on 27.03.2016.
 */
public class TestShell
{
    public static void main(String [] args)
    {
        JFrame frame = new JFrame("there's a pattern there to follow");
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parameterTest();

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private static void parameterTest()
    {
        Parameter<Integer> parameter = new Parameter<Integer>();
        parameter.setName("test");
        System.out.print(parameter.getName());
    }
}
