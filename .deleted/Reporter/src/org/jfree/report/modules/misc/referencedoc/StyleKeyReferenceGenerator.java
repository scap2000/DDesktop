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
 * StyleKeyReferenceGenerator.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.misc.referencedoc;

import java.net.URL;

import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.modules.output.pageable.pdf.PdfReportUtil;
import org.jfree.report.modules.output.table.html.HtmlReportUtil;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.modules.parser.ext.factory.stylekey.DefaultStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.util.ObjectUtilities;

/**
 * An application that generates a report that provides style key reference information.
 *
 * @author Thomas Morgner.
 */
public final class StyleKeyReferenceGenerator
{
  /**
   * The report definition file.
   */
  private static final String REFERENCE_REPORT = "StyleKeyReferenceReport.xml"; //$NON-NLS-1$

  /**
   * DefaultConstructor.
   */
  private StyleKeyReferenceGenerator ()
  {
  }

  /**
   * Creates the default tablemodel for the stylekey reference generator.
   *
   * @return the tablemodel for the stylekey reference generator.
   */
  public static TableModel createData ()
  {
    final StyleKeyFactoryCollector cc = new StyleKeyFactoryCollector();
    cc.addFactory(new DefaultStyleKeyFactory());
    cc.addFactory(new PageableLayoutStyleKeyFactory());

    return new StyleKeyReferenceTableModel(cc);
  }

  /**
   * The starting point for the application.
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();
    final ReportGenerator gen = ReportGenerator.getInstance();
    final URL reportURL = ObjectUtilities.getResourceRelative
            (REFERENCE_REPORT, StyleKeyReferenceGenerator.class);
    if (reportURL == null)
    {
      System.err.println("The report was not found in the classpath"); //$NON-NLS-1$
      System.err.println("File: " + REFERENCE_REPORT); //$NON-NLS-1$
      System.exit(1);
      return;
    }

    final JFreeReport report;
    try
    {
      report = gen.parseReport(reportURL);
    }
    catch (Exception e)
    {
      System.err.println("The report could not be parsed."); //$NON-NLS-1$
      System.err.println("File: " + REFERENCE_REPORT); //$NON-NLS-1$
      e.printStackTrace(System.err);
      System.exit(1);
      return;
    }
    report.setDataFactory(new TableDataFactory
        ("default", createData())); //$NON-NLS-1$
    try
    {
      HtmlReportUtil.createStreamHTML(report, System.getProperty("user.home") //$NON-NLS-1$
              + "/stylekey-reference.html"); //$NON-NLS-1$
      PdfReportUtil.createPDF(report, System.getProperty("user.home") //$NON-NLS-1$
              + "/stylekey-reference.pdf"); //$NON-NLS-1$
    }
    catch (Exception e)
    {
      System.err.println("The report processing failed."); //$NON-NLS-1$
      System.err.println("File: " + REFERENCE_REPORT); //$NON-NLS-1$
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

}
