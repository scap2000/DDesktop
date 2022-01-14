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
 * StraightToPDF.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.nogui;

import java.awt.Image;
import java.awt.Toolkit;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.net.URL;

import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.ancient.demo.opensource.OpenSourceProjects;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.pdf.PdfOutputProcessor;
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
public class StraightToPDF
{

  /**
   * Creates a new demo application.
   *
   * @param filename the output filename.
   * @throws ParseException if the report could not be parsed.
   */
  public StraightToPDF (final String filename)
          throws ParseException
  {
    final URL in = ObjectUtilities.getResource
            ("org/jfree/report/demo/opensource/opensource.xml", StraightToPDF.class);
    final JFreeReport report = parseReport(in);
    final TableModel data = new OpenSourceProjects();
    report.setDataFactory(new TableDataFactory
        ("default", data));
    final long startTime = System.currentTimeMillis();
    savePDF(report, filename);
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
      final URL imageURL = ObjectUtilities.getResource
              ("org/jfree/report/demo/opensource/gorilla.jpg", StraightToPDF.class);
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
  public boolean savePDF (final JFreeReport report, final String fileName)
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
      final PdfOutputProcessor outputProcessor = new PdfOutputProcessor(report.getConfiguration(), out);
      final PageableReportProcessor proc = new PageableReportProcessor(report, outputProcessor);
      proc.processReport();

      out.close();
      return true;
    }
    catch (Exception e)
    {
      System.err.println("Writing PDF failed.");
      e.printStackTrace();
      return false;
    }
    finally
    {
      try
      {
        if (out != null)
        {
          out.close();
        }
      }
      catch (Exception e)
      {
        System.err.println("Saving PDF failed.");
        e.printStackTrace();
      }
    }
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
      new StraightToPDF(System.getProperty("user.home") + "/OpenSource-Demo.pdf");
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.error("Failed to run demo", e);
      System.exit(1);
    }
  }

}
