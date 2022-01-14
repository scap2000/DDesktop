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
 * PreviewInternalFrame.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.base;

import java.awt.BorderLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.common.IconTheme;
import org.jfree.report.modules.gui.commonswing.JStatusBar;
import org.jfree.report.modules.gui.commonswing.ReportProgressBar;

/**
 * Creation-Date: 11.11.2006, 19:35:09
 *
 * @author Thomas Morgner
 */
public class PreviewInternalFrame extends JInternalFrame
{
  private class PreviewPanePropertyChangeHandler implements PropertyChangeListener
  {

    protected PreviewPanePropertyChangeHandler()
    {
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and
     *            the property that has changed.
     */

    public void propertyChange(final PropertyChangeEvent evt)
    {
      final String propertyName = evt.getPropertyName();
      if (PreviewPane.MENU_PROPERTY.equals(propertyName))
      {
        // Update the menu
        final JMenu[] menus = previewPane.getMenu();
        if (menus != null && menus.length > 0)
        {
          final JMenuBar menuBar = new JMenuBar();
          for (int i = 0; i < menus.length; i++)
          {
            final JMenu menu = menus[i];
            menuBar.add(menu);
          }
          setJMenuBar(menuBar);
        }
        else
        {
          setJMenuBar(null);
        }
        return;
      }

      if (PreviewPane.TITLE_PROPERTY.equals(propertyName))
      {
        setTitle(previewPane.getTitle());
        return;
      }

      if (PreviewPane.STATUS_TEXT_PROPERTY.equals(propertyName)
          || PreviewPane.STATUS_TYPE_PROPERTY.equals(propertyName))
      {
        statusBar.setStatus(previewPane.getStatusType(), previewPane.getStatusText());
        return;
      }

      if (PreviewPane.ICON_THEME_PROPERTY.equals(propertyName))
      {
        statusBar.setIconTheme(previewPane.getIconTheme());
        return;
      }

      if (PreviewPane.PAGINATING_PROPERTY.equals(propertyName))
      {
        if (Boolean.TRUE.equals(evt.getNewValue()))
        {
          progressBar.setOnlyPagination(true);
          progressBar.setVisible(true);
          pageLabel.setVisible(false);
        }
        else
        {
          progressBar.setOnlyPagination(true);
          progressBar.setVisible(false);
          pageLabel.setVisible(true);
        }
        progressBar.revalidate();
        return;
      }

      if (PreviewPane.PAGE_NUMBER_PROPERTY.equals(propertyName)
          || PreviewPane.NUMBER_OF_PAGES_PROPERTY.equals(propertyName))
      {
        pageLabel.setText(previewPane.getPageNumber() + "/" + previewPane.getNumberOfPages()); //$NON-NLS-1$
        return;
      }

      if (PreviewPane.CLOSED_PROPERTY.equals(propertyName))
      {
        if (previewPane.isClosed())
        {
          try
          {
            setClosed(true);
          } catch (PropertyVetoException e)
          {
            // Ignored ..
          }
          dispose();
        }
        else
        {
          setVisible(true);
        }
      }
    }
  }

  private PreviewPane previewPane;

  private JStatusBar statusBar;

  private ReportProgressBar progressBar;

  private JLabel pageLabel;

  /**
   * Creates a non-modal dialog without a title and without a specified
   * <code>Frame</code> owner.  A shared, hidden frame will be set as the owner
   * of the dialog.
   * <p/>
   * This constructor sets the component's locale property to the value returned
   * by <code>JComponent.getDefaultLocale</code>.
   *
   * @throws java.awt.HeadlessException if GraphicsEnvironment.isHeadless()
   *                                    returns true.
   * @see java.awt.GraphicsEnvironment#isHeadless
   * @see javax.swing.JComponent#getDefaultLocale
   */
  public PreviewInternalFrame()
  {
    init();
  }

  /**
   * Constructs a new frame that is initially invisible.
   * <p/>
   * This constructor sets the component's locale property to the value returned by
   * <code>JComponent.getDefaultLocale</code>.
   *
   * @see java.awt.GraphicsEnvironment#isHeadless
   * @see java.awt.Component#setSize
   * @see java.awt.Component#setVisible
   * @see javax.swing.JComponent#getDefaultLocale
   */
  public PreviewInternalFrame(final JFreeReport report)
  {
    init();
    setReportJob(report);
  }

  protected void init()
  {
    previewPane = new PreviewPane();
    statusBar = new JStatusBar(previewPane.getIconTheme());

    progressBar = new ReportProgressBar();
    progressBar.setVisible(false);
    previewPane.addReportProgressListener(progressBar);

    pageLabel = new JLabel();
    previewPane.addPropertyChangeListener(new PreviewInternalFrame.PreviewPanePropertyChangeHandler());

    final JComponent extensionArea = statusBar.getExtensionArea();
    extensionArea.setLayout(new BoxLayout(extensionArea, BoxLayout.X_AXIS));
    extensionArea.add(progressBar);
    extensionArea.add(pageLabel);

    final JComponent contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(previewPane, BorderLayout.CENTER);
    contentPane.add(statusBar, BorderLayout.SOUTH);
    setContentPane(contentPane);
    
    updateMenu(previewPane.getMenu());
    setTitle(previewPane.getTitle());
    statusBar.setIconTheme(previewPane.getIconTheme());
    statusBar.setStatus(previewPane.getStatusType(), previewPane.getStatusText());
  }

  private void updateMenu(final JMenu[] menus)
  {
    if (menus != null && menus.length > 0)
    {
      final JMenuBar menuBar = new JMenuBar();
      for (int i = 0; i < menus.length; i++)
      {
        final JMenu menu = menus[i];
        menuBar.add(menu);
      }
      setJMenuBar(menuBar);
    }
    else
    {
      setJMenuBar(null);
    }
  }

  public ReportController getReportController()
  {
    return previewPane.getReportController();
  }

  public void setReportController(final ReportController reportController)
  {
    previewPane.setReportController(reportController);
  }

  public IconTheme getIconTheme()
  {
    return previewPane.getIconTheme();
  }

  public void setIconTheme(final IconTheme theme)
  {
    previewPane.setIconTheme(theme);
  }

  public JFreeReport getReportJob()
  {
    return previewPane.getReportJob();
  }

  public void setReportJob(final JFreeReport reportJob)
  {
    previewPane.setReportJob(reportJob);
  }

  public void dispose()
  {
    super.dispose();
    previewPane.setClosed(true);
  }

  protected PreviewPane getPreviewPane()
  {
    return previewPane;
  }

  public boolean isToolbarFloatable()
  {
    return previewPane.isToolbarFloatable();
  }

  public void setToolbarFloatable(final boolean toolbarFloatable)
  {
    previewPane.setToolbarFloatable(toolbarFloatable);
  }

  public double getZoom()
  {
    return previewPane.getZoom();
  }

  public void setZoom(final double zoom)
  {
    previewPane.setZoom(zoom);
  }
}
