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
 * InternalFrameDemoFrame.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.layouts.internalframe;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.beans.PropertyVetoException;

import java.net.URL;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.TableDataFactory;
import org.jfree.report.ancient.demo.functions.PaintComponentTableModel;
import org.jfree.report.ancient.demo.layouts.ComponentDrawingDemoHandler;
import org.jfree.report.demo.util.AbstractDemoFrame;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionDowngrade;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 11.12.2005, 12:49:55
 *
 * @author Thomas Morgner
 */
public class InternalFrameDemoFrame extends AbstractDemoFrame
{
  private class NewFrameAction extends AbstractActionDowngrade
  {
    protected NewFrameAction()
    {
      putValue(ActionDowngrade.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
      putValue(ActionDowngrade.NAME, "New");
      putValue(ActionDowngrade.ACCELERATOR_KEY,
              KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
    }

    /** Invoked when an action occurs. */
    public void actionPerformed(final ActionEvent e)
    {
      final JInternalFrame frame = new DocumentInternalFrame();
      frame.setSize(400, 300);
      frame.setVisible(true); //necessary as of 1.3
      desktop.add(frame);
      try
      {
        frame.setSelected(true);
      }
      catch (PropertyVetoException ex)
      {
        // ignore exception ..
      }
    }
  }

  private JDesktopPane desktop;

  public InternalFrameDemoFrame()
  {
    setTitle("InternalFrameDemo");

    setJMenuBar(createMenuBar());

    desktop = new JDesktopPane();
    setContentPane(desktop);
  }

  public void updateFrameSize (final int inset)
  {
    final Rectangle screenSize = RefineryUtilities.getMaximumWindowBounds();
    setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
  }

  protected JMenuBar createMenuBar()
  {
    final JMenuBar menuBar = new JMenuBar();

//Set up the lone menu.
    final JMenu menu = new JMenu("Document");
    menu.setMnemonic(KeyEvent.VK_D);
    menuBar.add(menu);

    menu.add(new ActionMenuItem(new NewFrameAction()));
    menu.add(new ActionMenuItem(getPreviewAction()));
    menu.addSeparator();
    menu.add(new ActionMenuItem(getCloseAction()));

    final JMenu helpmenu = new JMenu("Help");
    helpmenu.setMnemonic(KeyEvent.VK_H);
    helpmenu.add(new ActionMenuItem(getAboutAction()));
    return menuBar;
  }


  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptPreview()
  {
    final JInternalFrame frame = findSelectedFrame();
    if (frame == null)
    {
      return;
    }
    final Rectangle bounds = frame.getBounds();
    final Container parent = frame.getParent();
    final boolean visible = frame.isVisible();
    final int layer = frame.getLayer();

    // now print ..
    previewReport(frame);

    if (parent != null)
    {
      if (frame.getParent() != parent)
      {
        frame.getParent().remove(frame);
        parent.add(frame);
      }
    }
    frame.setBounds(bounds);
    frame.setVisible(visible);
    frame.setLayer(new Integer(layer));
  }


  protected void previewReport(final JInternalFrame frame)
  {
    final ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      final URL in = ObjectUtilities.getResourceRelative
              ("component-drawing.xml", ComponentDrawingDemoHandler.class);
      if (in == null)
      {
        return;
      }
      final JFreeReport report = generator.parseReport(in);
      report.getReportConfiguration().setConfigProperty
              ("org.jfree.report.AllowOwnPeerForComponentDrawable", "true");
      final PaintComponentTableModel tableModel = new PaintComponentTableModel();
      tableModel.addComponent(frame);
      report.setDataFactory(new TableDataFactory ("default", tableModel));

      // Important: The dialog must be modal, so that we know, when the report
      // processing is finished.
      final PreviewDialog previewDialog = new PreviewDialog(report, this, true);
      previewDialog.setToolbarFloatable(true);
      previewDialog.pack();
      RefineryUtilities.positionFrameRandomly(previewDialog);
      previewDialog.setVisible(true);
    }
    catch (Exception e)
    {
      Log.error("Failed to parse the report definition", e);
    }
  }


  private JInternalFrame findSelectedFrame()
  {
    final JInternalFrame[] frames = desktop.getAllFrames();
    for (int i = 0; i < frames.length; i++)
    {
      final JInternalFrame frame = frames[i];
      if (frame.isSelected())
      {
        return frame;
      }
    }
    return null;
  }
}
