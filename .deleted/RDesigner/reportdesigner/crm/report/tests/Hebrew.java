package org.pentaho.reportdesigner.crm.report.tests;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 25.01.2007
 * Time: 20:25:30
 */
@SuppressWarnings({"ALL"})
public class Hebrew extends JLabel
{

    public static void main(String[] args) throws Exception
    {
        EventQueue.invokeAndWait(new Runnable()
        {
            public void run()
            {
                JFrame frm = new JFrame("Circles");
                frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frm.add(new Hebrew());
                frm.pack();
                frm.setVisible(true);
            }
        });
        Thread.sleep(5000);
    }


    public Hebrew()
    {
        super("\u05e4\u05bc");
        setFont(new Font("Code2000", Font.PLAIN, 136));
    }

}