package org.pentaho.reportdesigner.crm.report.tests;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 11.01.2007
 * Time: 06:30:01
 */
@SuppressWarnings({"ALL"})
public class JFilerChooserTest
{
    private static JFrame jFrame;


    public static void main(String[] args) throws Exception
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        for (int i = 0; i < 100; i++)
        {
            Thread.sleep(300);
            test();
        }

    }


    private static void test() throws InterruptedException
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                jFrame = new JFrame();
                jFrame.setBounds(0, 0, 100, 100);
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.setVisible(true);
            }
        });

        new JFileChooser();

        Thread.sleep(200);

        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                JFileChooser jFileChooser = new JFileChooser();
                System.out.println("OK");
                jFrame.dispose();
            }
        });
    }
}
