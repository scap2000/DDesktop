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
 * HtmlReportUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.io.IOUtils;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.table.base.FlowReportProcessor;
import org.jfree.report.modules.output.table.base.StreamReportProcessor;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultNameGenerator;
import org.jfree.repository.RepositoryUtilities;
import org.jfree.repository.file.FileRepository;
import org.jfree.repository.stream.StreamRepository;
import org.jfree.repository.zipwriter.ZipRepository;
import org.jfree.util.Log;

/**
 * Utility class to provide an easy to use default implementation of html exports.
 *
 * @author Thomas Morgner
 */
public final class HtmlReportUtil
{
  /**
   * DefaultConstructor.
   */
  private HtmlReportUtil ()
  {
  }

  /**
   * Saves a report into a single HTML format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws java.io.IOException       if there was an IOerror while processing the
   *                                   report.
   */
  public static void createStreamHTML (final JFreeReport report, final String filename)
          throws IOException, ReportProcessingException
  {
    final File file = new File(filename).getAbsoluteFile();
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(file));
    try
    {
      createStreamHTML(report, fout);
    }
    finally
    {
      fout.close();
    }
  }

  public static void createStreamHTML (final JFreeReport report,
                                       final OutputStream outputStream)
          throws ReportProcessingException
  {
    final StreamRepository targetRepository = new StreamRepository(null, outputStream);
    final ContentLocation targetRoot = targetRepository.getRoot();

    final HtmlOutputProcessor outputProcessor = new StreamHtmlOutputProcessor(report.getConfiguration());
    final HtmlPrinter printer = new AllItemsHtmlPrinter(report.getResourceManager());
    printer.setContentWriter(targetRoot, new DefaultNameGenerator(targetRoot, "index", "html"));
    printer.setDataWriter(null, null);
    printer.setUrlRewriter(new FileSystemURLRewriter());
    outputProcessor.setPrinter(printer);

    final StreamReportProcessor sp = new StreamReportProcessor(report, outputProcessor);
    sp.processReport();
    sp.close();
  }

  /**
   * Saves a report to HTML. The HTML file is stored in a directory; all other content goes into the same
   * directory as the specified html file.
   *
   * @param report   the report.
   * @param targetFileName target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the
   *                                   report.
   */
  public static void createDirectoryHTML (final JFreeReport report,
                                          final String targetFileName)
          throws IOException, ReportProcessingException
  {
    try
    {
      final File targetFile = new File(targetFileName);
      if (targetFile.exists())
      {
        // try to delete it ..
        if (targetFile.delete() == false)
        {
          throw new IOException("Unable to remove the already existing target-file.");
        }
      }

      final File targetDirectory = targetFile.getParentFile();

      final FileRepository targetRepository = new FileRepository(targetDirectory);
      final ContentLocation targetRoot = targetRepository.getRoot();

      final String suffix = getSuffix(targetFileName);
      final String filename = IOUtils.getInstance().stripFileExtension(targetFile.getName());

      final FlowHtmlOutputProcessor outputProcessor = new FlowHtmlOutputProcessor(report.getConfiguration());

      final HtmlPrinter printer = new AllItemsHtmlPrinter(report.getResourceManager());
      printer.setContentWriter(targetRoot, new DefaultNameGenerator(targetRoot, filename, suffix));
      printer.setDataWriter(targetRoot, new DefaultNameGenerator(targetRoot, "content"));
      printer.setUrlRewriter(new FileSystemURLRewriter());
      outputProcessor.setPrinter(printer);

      final FlowReportProcessor sp = new FlowReportProcessor(report, outputProcessor);
      sp.processReport();
      sp.close();
    }
    catch(ReportProcessingException re)
    {
      throw re;
    }
    catch (ContentIOException e)
    {
      throw new IOException("Failed to get repository-root.");
    }
  }

  /**
   * Saves a report to HTML. The HTML file is stored in a directory; all other content goes into the same
   * directory as the specified html file.
   *
   * @param report   the report.
   * @param targetFileName target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the
   *                                   report.
   */
  public static void createDirectoryHTML (final JFreeReport report,
                                          final String targetFileName,
                                          final String dataDirectoryName)
          throws IOException, ReportProcessingException
  {
    try
    {
      final File targetFile = new File(targetFileName);
      if (targetFile.exists())
      {
        // try to delete it ..
        if (targetFile.delete() == false)
        {
          throw new IOException("Unable to remove the already existing target-file.");
        }
      }

      final File targetDirectory = targetFile.getParentFile();

      final File tempDataDir = new File (dataDirectoryName);
      File dataDirectory;
      if (tempDataDir.isAbsolute())
      {
        dataDirectory = tempDataDir;
      }
      else
      {
        dataDirectory = new File(targetDirectory, dataDirectoryName);
      }
      if (dataDirectory.exists() && dataDirectory.isDirectory() == false)
      {
        dataDirectory = dataDirectory.getParentFile();
        if (dataDirectory.isDirectory() == false)
        {
          throw new ReportProcessingException("DataDirectory is invalid: " + dataDirectory);
        }
      }
      else if (dataDirectory.exists() == false)
      {
        dataDirectory.mkdirs();
      }

      final FileRepository targetRepository = new FileRepository(targetDirectory);
      final ContentLocation targetRoot = targetRepository.getRoot();

      final FileRepository dataRepository = new FileRepository(dataDirectory);
      final ContentLocation dataRoot = dataRepository.getRoot();

      final String suffix = getSuffix(targetFileName);
      final String filename = IOUtils.getInstance().stripFileExtension(targetFile.getName());

      final FlowHtmlOutputProcessor outputProcessor = new FlowHtmlOutputProcessor(report.getConfiguration());

      final HtmlPrinter printer = new AllItemsHtmlPrinter(report.getResourceManager());
      printer.setContentWriter(targetRoot, new DefaultNameGenerator(targetRoot, filename, suffix));
      printer.setDataWriter(dataRoot, new DefaultNameGenerator(dataRoot, "content"));
      printer.setUrlRewriter(new FileSystemURLRewriter());
      outputProcessor.setPrinter(printer);

      final FlowReportProcessor sp = new FlowReportProcessor(report, outputProcessor);
      sp.processReport();
      sp.close();
    }
    catch(ReportProcessingException re)
    {
      throw re;
    }
    catch (ContentIOException e)
    {
      throw new IOException("Failed to get repository-root.");
    }
  }

  private static String getSuffix (final String filename)
  {
    final String suffix = IOUtils.getInstance().getFileExtension(filename);
    if (suffix.length() == 0)
    {
      return "";
    }
    return suffix.substring(1);
  }

  /**
   * Saves a report in a ZIP file. The zip file contains a HTML document.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the
   *                                   report.
   */
  public static void createZIPHTML (final JFreeReport report, final String filename)
          throws IOException, ReportProcessingException
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(filename));
      createZIPHTML(report, out, "report");
      out.close();
      out = null;
    }
    catch(IOException ioe)
    {
      throw ioe;
    }
    catch(ReportProcessingException re)
    {
      throw re;
    }
    catch (Exception re)
    {
      Log.error("Exporting failed .", re);
      throw new ReportProcessingException("Failed to process the report", re);
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
        Log.error("Unable to close the output stream.", e);
      }
    }
  }


  /**
   * Saves a report in a ZIP file. The zip file contains a HTML document.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the
   *                                   report.
   */
  public static void createZIPHTML (final JFreeReport report, final OutputStream out, final String filename)
          throws IOException, ReportProcessingException
  {
    try
    {
      final ZipRepository zipRepository = new ZipRepository(out);
      final ContentLocation root = zipRepository.getRoot();
      final ContentLocation data = RepositoryUtilities.createLocation
          (zipRepository, RepositoryUtilities.split("data", "/"));

      final FlowHtmlOutputProcessor outputProcessor = new FlowHtmlOutputProcessor(report.getConfiguration());

      final HtmlPrinter printer = new AllItemsHtmlPrinter(report.getResourceManager());
      printer.setContentWriter(root, new DefaultNameGenerator(root, filename));
      printer.setDataWriter(data, new DefaultNameGenerator(data, "content"));
      printer.setUrlRewriter(new SingleRepositoryURLRewriter());
      outputProcessor.setPrinter(printer);

      final FlowReportProcessor sp = new FlowReportProcessor(report, outputProcessor);
      sp.processReport();
      sp.close();
      zipRepository.close();
    }
    catch(IOException ioe)
    {
      throw ioe;
    }
    catch(ReportProcessingException re)
    {
      throw re;
    }
    catch (Exception re)
    {
      Log.error("Exporting failed .", re);
      throw new ReportProcessingException("Failed to process the report", re);
    }
  }

}
