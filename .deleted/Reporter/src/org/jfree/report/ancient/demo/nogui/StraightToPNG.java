/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * StraightToPNG.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.nogui;

import com.keypoint.PngEncoder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import java.net.URL;

import java.text.MessageFormat;

import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.PageDefinition;
import org.jfree.report.TableDataFactory;
import org.jfree.report.ancient.demo.opensource.OpenSourceProjects;
import org.jfree.report.modules.output.pageable.graphics.PageDrawable;
import org.jfree.report.modules.output.pageable.graphics.PrintReportProcessor;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.WaitingImageObserver;
import org.jfree.xmlns.parser.ParseException;

/**
 * A demonstration that shows how to generate a report and save it to PDF without
 * displaying the print preview or the PDF save-as dialog.
 *
 * @author David Gilbert
 */
public class StraightToPNG
{

  /**
   * Creates a new demo application.
   *
   * @param filename the output filename.
   * @throws ParseException if the report could not be parsed.
   */
  public StraightToPNG (final String filename)
          throws ParseException
  {
    final URL in = ObjectUtilities.getResource
            ("org/jfree/report/demo/opensource/opensource.xml", StraightToPNG.class);
    final JFreeReport report = parseReport(in);
    final TableModel data = new OpenSourceProjects();
    report.setDataFactory(new TableDataFactory
        ("default", data));
    final long startTime = System.currentTimeMillis();
    savePNG(report, filename);
    Log.debug("Time: " + (System.currentTimeMillis() - startTime));
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL the template location.
   * @return a report.
   *
   * @throws ParseException if the report could not be parsed.
   */
  private JFreeReport parseReport (final URL templateURL)
          throws ParseException
  {
    final ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      final JFreeReport report = generator.parseReport(templateURL);
      // this demo adds the image at runtime just to show how this could be
      // done. Usually such images get referenced from the XML itself without
      // using manual coding.
      final URL imageURL = ObjectUtilities.getResource
              ("org/jfree/report/demo/opensource/gorilla.jpg", StraightToPNG.class);
      final Image image = Toolkit.getDefaultToolkit().createImage(imageURL);
      final WaitingImageObserver obs = new WaitingImageObserver(image);
      obs.waitImageLoaded();
      report.setProperty("logo", image);
      return report;
    }
    catch (Exception e)
    {
      throw new ParseException("Failed to parse the report", e);
    }
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report   the report.
   * @param fileName target file name.
   * @return true or false.
   */
  public boolean savePNG (final JFreeReport report, final String fileName)
  {
    try
    {
      final PrintReportProcessor prc = new PrintReportProcessor(report);
      final int numberOfPages = prc.getNumberOfPages();
      for (int i = 0; i < numberOfPages; i++)
      {
        final String fileNameFormated =
                MessageFormat.format(fileName, new Object[]{ new Integer(i)});
        final BufferedImage image = createImage(report.getPageDefinition());

        final Rectangle rect = new Rectangle(0,0, image.getWidth(), image.getHeight());
        // prepare the image by filling it ...
        final Graphics2D g2 = image.createGraphics();
        g2.setPaint(Color.white);
        g2.fill(rect);

        final PageDrawable pageDrawable = prc.getPageDrawable(i);
        pageDrawable.draw(g2, rect);
        g2.dispose();

        // convert to PNG ...
        final PngEncoder encoder = new PngEncoder(image, true, 0, 9);
        final byte[] data = encoder.pngEncode();

        final BufferedOutputStream out = new BufferedOutputStream
                (new FileOutputStream(fileNameFormated));
        out.write(data);
        out.close();
      }
      return true;
    }
    catch (Exception e)
    {
      System.err.println("Writing PDF failed.");
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Create the empty image for the given page size.
   *
   * @param pd the page definition that defines the image bounds.
   * @return the generated image.
   */
  private BufferedImage createImage(final PageDefinition pd)
  {
    // in this simple case we know, that all pages have the same size..
    final PageFormat pf = pd.getPageFormat(0);

    final double width = pf.getWidth();
    final double height = pf.getHeight();
    //write the report to the temp file
    return new BufferedImage
      ((int) width, (int) height, BufferedImage.TYPE_BYTE_INDEXED);
  }

  /**
   * Demo starting point.
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();
    try
    {
      //final StraightToPDF demo =
      new StraightToPNG(System.getProperty("user.home") + "/OpenSource-Demo-{0}.png");
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.error("Failed to run demo", e);
      System.exit(1);
    }
  }

}
