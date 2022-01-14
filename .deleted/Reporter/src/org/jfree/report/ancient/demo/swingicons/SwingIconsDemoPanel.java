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
 * SwingIconsDemoPanel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.swingicons;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.io.FileUtilities;
import org.jfree.report.modules.misc.configstore.base.ConfigFactory;
import org.jfree.report.modules.misc.configstore.base.ConfigStorage;
import org.jfree.report.util.ImageUtils;
import org.jfree.ui.action.AbstractFileSelectionAction;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.Configuration;
import org.jfree.util.DefaultConfiguration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * A demonstration application. <P> This demo is written up in the JFreeReport PDF
 * Documentation.  Please notify David Gilbert (david.gilbert@object-refinery.com) if you
 * need to make changes to this file. <P> To run this demo, you need to have the Java Look
 * and Feel Icons jar file on your classpath.
 *
 * @author David Gilbert
 */
public class SwingIconsDemoPanel extends JPanel
{
  private class SelectRepositoryFileAction extends AbstractFileSelectionAction
  {
    private File selectedFile;

    protected SelectRepositoryFileAction ()
    {
      super(SwingIconsDemoPanel.this);
      putValue(Action.NAME, "Select graphics archive ..");
      this.putValue(Action.SMALL_ICON, ImageUtils.createTransparentIcon(16, 16));
      this.putValue("ICON24", ImageUtils.createTransparentIcon(24, 24));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      selectedFile = performSelectFile(selectedFile, JFileChooser.OPEN_DIALOG, true);
      if (selectedFile != null)
      {
        if (selectedFile.exists() && selectedFile.canRead() && selectedFile.isFile())
        {
          try
          {
            loadData(selectedFile.toURL());
          }
          catch (MalformedURLException ex)
          {
            Log.warn ("Unable to form local file URL. Is there no local filesystem?");
          }
        }
      }
    }

    /**
     * Returns a descriptive text describing the file extension.
     *
     * @return the file description.
     */
    protected String getFileDescription ()
    {
      return "Java Look and Feel Graphics Repository";
    }

    /**
     * Returns the file extension that should be used for the operation.
     *
     * @return the file extension.
     */
    protected String getFileExtension ()
    {
      return ".jar";
    }
  }

  /**
   * The data for the report.
   */
  private SwingIconsDemoTableModel data;

  /**
   * Constructs the demo application.
   */
  public SwingIconsDemoPanel ()
  {
    data = new SwingIconsDemoTableModel();

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    final JTable table = new JTable(data);
    table.setDefaultRenderer(Image.class, new ImageCellRenderer());
    table.setRowHeight(26);
    final JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.CENTER);

    final JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
    buttonPanel.add (new ActionButton (new SelectRepositoryFileAction()));
    add (buttonPanel, BorderLayout.SOUTH);

    loadData(findDataFile());
  }

  protected void loadData(final URL sourceURL)
  {
    if (sourceURL != null)
    {
      // on success update the config path, else clear the path.
      if (this.data.readData(sourceURL))
      {
        storeToConfiguration(sourceURL);
        return;
      }
      else
      {
        final String message =
                ("There was a problem while loading 'jlfgr-1_0.jar'.\n"
                + "A URL was given, but the contents seems to be invalid.\n\n"
                + "You may download this jar-file from: \n"
                + "http://java.sun.com/developer/techDocs/hi/repository/");
        Log.warn(message);
      }
    }
    this.data.clear();
    storeToConfiguration(null);
  }

  /**
   * Loads the URL of the Graphics Repository from the local configuration.
   *
   * @return the loaded URL or null, if the configuration did not hold an entry.
   */
  protected URL loadFromConfiguration()
  {
    final String configPath = ConfigFactory.encodePath("SwingIconsDemo-TableModel");
    final ConfigStorage cs = ConfigFactory.getInstance().getUserStorage();
    if (cs.isAvailable(configPath) == false)
    {
      return null;
    }
    try
    {
      final Configuration p = cs.load(configPath, null);
      final String property = p.getConfigProperty("repository-path");
      if (property == null)
      {
        return null;
      }
      return new URL (property);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  protected void storeToConfiguration(final URL url)
  {
    final String configPath = ConfigFactory.encodePath("SwingIconsDemo-TableModel");
    final ConfigStorage cs = ConfigFactory.getInstance().getUserStorage();
    try
    {
      final DefaultConfiguration p = new DefaultConfiguration();
      if (url != null)
      {
        p.setConfigProperty("repository-path", url.toExternalForm());
      }
      cs.store(configPath, p);
    }
    catch (Exception e)
    {
      // ignored ..
      Log.debug ("Unable to store the configuration.", e);
    }
  }

  /**
   * Searches for the 'jlfgr_1_0.jar' file on the classpath, in the
   * classpath directories and the working directory. If that fails,
   * the user is asked to choose the correct file.
   *
   * @return the URL to the graphics repository.
   */
  private URL findDataFile ()
  {
    final URL url = ObjectUtilities.getResource("jlfgr-1_0.jar", SwingIconsDemoPanel.class);
    if (url != null)
    {
      return url;
    }
    final URL urlFromConfig = loadFromConfiguration();
    if (urlFromConfig != null)
    {
      return urlFromConfig;
    }

    final File localFile = new File ("jlfgr-1_0.jar");
    if (localFile.exists() && localFile.canRead() && localFile.isFile())
    {
      try
      {
        return localFile.toURL();
      }
      catch (MalformedURLException e)
      {
        Log.warn ("Unable to form local file URL. Is there no local filesystem?");
      }
    }

    final File classpathFile = FileUtilities.findFileOnClassPath("jlfgr-1_0.jar");
    if (classpathFile != null)
    {
      if (classpathFile.exists() && classpathFile.canRead() && classpathFile.isFile())
      {
        try
        {
          return classpathFile.toURL();
        }
        catch (MalformedURLException e)
        {
          Log.warn ("Unable to form local file URL. Is there no local filesystem?");
        }
      }
    }

    final String title = "Unable to load the icons.";
    final String message = ("Unable to find 'jlfgr-1_0.jar'\n"
            + "Please make sure you have the Java Look and Feel Graphics Repository in "
            + "in your classpath, the same directory as the JFreeReport-jar files or in "
            + "the current working directory.\n\n"
            + "You may download this jar-file from: \n"
            + "http://java.sun.com/developer/techDocs/hi/repository/");
    JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    return null;
  }

  public SwingIconsDemoTableModel getData()
  {
    return data;
  }
}
