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
 * StraightToEverything.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.nogui;

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
import org.jfree.report.modules.output.pageable.plaintext.PageableTextOutputProcessor;
import org.jfree.report.modules.output.pageable.plaintext.driver.TextFilePrinterDriver;
import org.jfree.report.modules.output.table.base.StreamReportProcessor;
import org.jfree.report.modules.output.table.csv.StreamCSVOutputProcessor;
import org.jfree.report.modules.output.table.html.HtmlReportUtil;
import org.jfree.report.modules.output.table.rtf.RTFReportUtil;
import org.jfree.report.modules.output.table.xls.ExcelReportUtil;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.parser.ParseException;

/**
 * A demonstration that shows how to generate a report and save it to PDF without
 * displaying the print preview or the PDF save-as dialog. The methods to save the report
 * to the various file formats are also implemented in
 *
 * @author Thomas Morgner
 */
public class StraightToEverything
{

  /**
   * Creates a new demo application.
   *
   * @param filename the output filename.
   * @throws ParseException if the report could not be parsed.
   */
  public StraightToEverything (final String filename)
          throws ParseException
  {
    final URL in = ObjectUtilities.getResource
            ("org/jfree/report/demo/opensource/opensource.xml", StraightToEverything.class);
    final JFreeReport report = parseReport(in);
    final TableModel data = new OpenSourceProjects();
    report.setDataFactory(new TableDataFactory
        ("default", data));
    try
    {
      createPDF(report, filename + ".pdf");
      createCSV(report, filename + ".csv");
      createDirectoryHTML(report, filename + ".html");
      createPlainText(report, filename + ".txt");
      createRTF(report, filename + ".rtf");
      createStreamHTML(report, filename + "-single-file.html");
      createXLS(report, filename + ".xls");
      createZIPHTML(report, filename + ".zip");
    }
    catch (Exception e)
    {
      Log.error("Failed to write report", e);
    }
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
      return generator.parseReport(templateURL);
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
  public static boolean createPDF (final JFreeReport report, final String fileName)
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
      final PdfOutputProcessor outputProcessor = new PdfOutputProcessor(report.getConfiguration(), out);
      final PageableReportProcessor proc = new PageableReportProcessor(report, outputProcessor);
      proc.processReport();
      proc.close();
      out.close();
      return true;
    }
    catch (Exception e)
    {
      Log.error("Writing PDF failed.", e);
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
        Log.error("Saving PDF failed.", e);
      }
    }
  }

  /**
   * Saves a report to plain text format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createPlainText (final JFreeReport report, final String filename)
          throws Exception
  {
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    // cpi = 15, lpi = 10
    final TextFilePrinterDriver pc = new TextFilePrinterDriver(fout, 15, 10);

    final PageableTextOutputProcessor outputProcessor = new PageableTextOutputProcessor(pc, report.getConfiguration());
    final PageableReportProcessor proc = new PageableReportProcessor(report, outputProcessor);
    proc.processReport();
    proc.close();
    fout.close();
  }

  /**
   * Saves a report to rich-text format (RTF).
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createRTF (final JFreeReport report, final String filename)
          throws Exception
  {
    RTFReportUtil.createRTF(report, filename);
  }

  /**
   * Saves a report to CSV format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createCSV (final JFreeReport report, final String filename)
          throws Exception
  {

    final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filename));
    final StreamCSVOutputProcessor target = new StreamCSVOutputProcessor(report.getConfiguration(), outputStream);

    final StreamReportProcessor reportProcessor = new StreamReportProcessor(report, target);
    reportProcessor.processReport();
    outputStream.close();
  }

  /**
   * Saves a report to Excel format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createXLS (final JFreeReport report, final String filename)
          throws Exception
  {
    ExcelReportUtil.createXLS(report, filename);
  }

  /**
   * Saves a report into a single HTML format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createStreamHTML (final JFreeReport report, final String filename)
          throws Exception
  {
    HtmlReportUtil.createStreamHTML(report, filename);
  }

  /**
   * Saves a report to HTML. The HTML file is stored in a directory.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createDirectoryHTML (final JFreeReport report,
                                          final String filename)
          throws Exception
  {
    HtmlReportUtil.createDirectoryHTML(report, filename);
  }

  /**
   * Saves a report in a ZIP file. The zip file contains a HTML document.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createZIPHTML (final JFreeReport report, final String filename)
          throws Exception
  {
    HtmlReportUtil.createZIPHTML(report, filename);
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
      final String folder;
      if (args.length == 0)
      {
        folder = System.getProperty("user.home");
      }
      else
      {
        folder = args[0];
      }
      //final StraightToEverything demo =
      new StraightToEverything(folder + "/OpenSource-Demo");
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.error("Failed to run demo", e);
      System.exit(1);
    }
  }

}
