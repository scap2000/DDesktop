package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.ImageElement;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.elementfactory.StaticImageURLElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

/**
 * User: Martin
 * Date: 06.10.2005
 * Time: 15:54:54
 */
@SuppressWarnings({"ALL"})
public class ImageScalingBugTest
{
    public static void main(@NotNull String[] args) throws IOException
    {
        JFreeReport report = new JFreeReport();
        //StaticImageElementFactory sief = new StaticImageElementFactory();
        //sief.setImage(new ImageIcon("C:\\temp\\sackmesser.jpg").getImage());
        //sief.setMinimumSize(new Dimension(100, 100));
        //sief.setScale(true);
        //sief.setKeepAspectRatio(true);
        //sief.setAbsolutePosition(new Point2D.Double(0, 0));

        ImageElement ie = StaticImageURLElementFactory.createImageElement("test", new Rectangle2D.Double(0, 0, 100, 100), new File("C:\\temp\\sackmesser.jpg").toURI().toURL(), true, true);

        report.getReportHeader().addElement(ie);

        JFreeReportBoot.getInstance().start();
        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }

}
