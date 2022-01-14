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
 * InternalFrameDemoHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.opensource;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.util.AbstractDemoFrame;
import org.jfree.report.demo.util.DemoController;
import org.jfree.report.demo.util.PreviewHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.demo.util.SimpleDemoFrame;
import org.jfree.report.modules.gui.base.PreviewInternalFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.Log;

/**
 * A demo to show the usage of the InteralPreviewFrame. It shows a report on a primitive
 * desktop.
 *
 * @author Thomas Morgner
 */
public class InternalFrameDemoHandler extends OpenSourceXMLDemoHandler
{
  private class InternalFramePreviewHandler implements PreviewHandler
  {
    protected InternalFramePreviewHandler()
    {
    }

    public void attemptPreview()
    {
      try
      {
        final JFreeReport report = createReport();

        final PreviewInternalFrame frame = new PreviewInternalFrame(report);
        frame.setClosable(true);
        frame.setResizable(true);
        frame.setToolbarFloatable(false);
        getDesktop().add(frame);
        frame.pack();
        frame.setVisible(true);
        frame.requestFocus();
      }
      catch (ReportDefinitionException e)
      {
        Log.error("Unable to create the report; report definition contained errors.", e);
        AbstractDemoFrame.showExceptionDialog("report.definitionfailure", e);
      }
    }
  }

  /**
   * The data for the report.
   */
  private final TableModel data;
  /**
   * The desktop pane.
   */
  private JDesktopPane desktop;
  private PreviewHandler previewHandler;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public InternalFrameDemoHandler ()
  {
    this.data = new OpenSourceProjects();
    this.previewHandler = new InternalFramePreviewHandler();
  }

  public String getDemoName()
  {
    return "Internal Frame Demo (External)";
  }

  public synchronized JComponent getPresentationComponent()
  {
    return getDesktop();
  }

  protected JDesktopPane getDesktop() {
    if (desktop == null)
    {
      desktop = init(getController());
    }
    return desktop;
  }

  /**
   * Creates the content for the application frame.
   *
   * @return a panel containing the basic user interface.
   */
  private JDesktopPane init(final DemoController ctrl)
  {
    final JPanel content = new JPanel(new BorderLayout());
    content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    content.add(createDefaultTable(data));
    content.add(new ActionButton(ctrl.getExportAction()), BorderLayout.SOUTH);

    final JInternalFrame frame = new JInternalFrame();
    frame.setClosable(false);
    frame.setVisible(true);
    frame.setContentPane(content);
    frame.pack();

    final JDesktopPane desktop = new JDesktopPane();
    desktop.setDoubleBuffered(false);
    desktop.add(frame);
    return desktop;
  }

  public PreviewHandler getPreviewHandler()
  {
    return previewHandler;
  }

   /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final InternalFrameDemoHandler handler = new InternalFrameDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
