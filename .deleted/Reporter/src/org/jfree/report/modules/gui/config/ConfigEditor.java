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
 * ConfigEditor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.gui.config.editor.ConfigEditorPanel;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ConfigTreeModel;
import org.jfree.report.modules.gui.config.model.ConfigTreeModelException;
import org.jfree.report.modules.gui.config.model.ConfigTreeModuleNode;
import org.jfree.report.util.i18n.Messages;
import org.jfree.ui.FilesystemFilter;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.LineBreakIterator;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.ResourceBundleSupport;
import org.jfree.util.StringUtils;

/**
 * The ConfigEditor can be used to edit the global jfreereport.properties files. These files provide global settings for
 * all reports and contain the system level configuration of JFreeReport.
 *
 * @author Thomas Morgner
 */
public class ConfigEditor extends JFrame
{
  /**
   * An Action to handle close requests.
   */
  private class CloseAction extends AbstractActionDowngrade
  {
    /**
     * DefaultConstructor.
     */
    protected CloseAction()
    {
      putValue(Action.NAME, getResources().getString("action.exit.name")); //$NON-NLS-1$
    }

    /**
     * Invoked when an action occurs. The action invokes System.exit(0).
     *
     * @param e the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      attempClose();
    }
  }

  /**
   * An action to handle save requests.
   */
  private class SaveAction extends AbstractActionDowngrade
  {
    /**
     * DefaultConstructor.
     */
    protected SaveAction()
    {
      putValue(Action.NAME, getResources().getString("action.save.name")); //$NON-NLS-1$
      putValue(Action.SMALL_ICON, getResources().getIcon("action.save.small-icon")); //$NON-NLS-1$
    }

    /**
     * Saves the configuration.
     *
     * @param e the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      save();
    }
  }

  /**
   * An action to handle load requests.
   */
  private class LoadAction extends AbstractActionDowngrade
  {
    /**
     * DefaultConstructor.
     */
    protected LoadAction()
    {
      putValue(Action.NAME, getResources().getString("action.load.name")); //$NON-NLS-1$
      putValue(Action.SMALL_ICON, getResources().getIcon("action.load.small-icon")); //$NON-NLS-1$
    }

    /**
     * Loads the configuration.
     *
     * @param e the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      load();
    }
  }

  /**
   * An action to handle new requests, which reset the report configuration.
   */
  private class NewAction extends AbstractActionDowngrade
  {
    /**
     * DefaultConstructor.
     */
    protected NewAction()
    {
      putValue(Action.NAME, getResources().getString("action.new.name")); //$NON-NLS-1$
      putValue(Action.SMALL_ICON, getResources().getIcon("action.new.small-icon")); //$NON-NLS-1$
    }

    /**
     * Reset the configuration.
     *
     * @param e the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      reset();
    }
  }

  /**
   * This class handles the tree selection events and activates the detail editors.
   */
  private class ModuleTreeSelectionHandler implements TreeSelectionListener
  {
    /**
     * DefaultConstructor.
     */
    protected ModuleTreeSelectionHandler()
    {
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged(final TreeSelectionEvent e)
    {
      final TreePath path = e.getPath();
      final Object lastPathElement = path.getLastPathComponent();
      if (lastPathElement instanceof ConfigTreeModuleNode)
      {
        final ConfigTreeModuleNode node = (ConfigTreeModuleNode) lastPathElement;
        final ConfigEditorPanel detailEditorPane = getDetailEditorPane();
        detailEditorPane.store();
        detailEditorPane.editModule
            (node.getModule(), node.getConfiguration(), node.getAssignedKeys());
      }
    }
  }

  private class CloseHandler extends WindowAdapter
  {
    /**
       * Invoked when a window is in the process of being closed. The close operation can be overridden at this point.
     */
    public void windowClosing(final WindowEvent e)
    {
      attempClose();
    }
  }

  /**
   * The name of the condigu description filename
   */
  public static final String CONFIG_DESCRIPTION_FILENAME = "config-description.xml"; //$NON-NLS-1$

  /**
   * A constant defining that text should be escaped in a way which is suitable for property keys.
   */
  private static final int ESCAPE_KEY = 0;
  /**
   * A constant defining that text should be escaped in a way which is suitable for property values.
   */
  private static final int ESCAPE_VALUE = 1;
  /**
   * A constant defining that text should be escaped in a way which is suitable for property comments.
   */
  private static final int ESCAPE_COMMENT = 2;

  /**
   * A label that serves as status bar.
   */
  private JLabel statusHolder;
  /**
   * The resource bundle instance of this dialog.
   */
  private final ResourceBundleSupport resources;

  /**
   * The detail editor for the currently selected tree node.
   */
  private final ConfigEditorPanel detailEditorPane;
  /**
   * The tree model used to display the structure of the report configuration.
   */
  private ConfigTreeModel treeModel;

  /**
   * The currently used report configuration.
   */
  private final HierarchicalConfiguration currentReportConfiguration;
  /**
   * The file chooser used to load and save the report configuration.
   */
  private JFileChooser fileChooser;

  /**
   * Externalized string access
   */
  private Messages messages;
  private static final String PROPERTIES_FILE_EXTENSION = ".properties";


  /**
   * Constructs a new ConfigEditor.
   *
   * @throws ConfigTreeModelException if the tree model could not be built.
   */
  public ConfigEditor()
      throws ConfigTreeModelException
  {
    resources = new ResourceBundleSupport(getLocale(), ConfigGUIModule.BUNDLE_NAME);
    messages = new Messages(getLocale(), ConfigGUIModule.BUNDLE_NAME);
    currentReportConfiguration = new HierarchicalConfiguration
        (JFreeReportBoot.getInstance().getGlobalConfig());
    detailEditorPane = new ConfigEditorPanel();

    setTitle(resources.getString("config-editor.title")); //$NON-NLS-1$


    final JSplitPane splitPane = new JSplitPane
        (JSplitPane.HORIZONTAL_SPLIT, createEntryTree(),
            detailEditorPane);

    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(splitPane, BorderLayout.CENTER);
    contentPane.add(createButtonPane(), BorderLayout.SOUTH);


    final JPanel cPaneStatus = new JPanel();
    cPaneStatus.setLayout(new BorderLayout());
    cPaneStatus.add(contentPane, BorderLayout.CENTER);
    cPaneStatus.add(createStatusBar(), BorderLayout.SOUTH);

    setContentPane(cPaneStatus);

    addWindowListener(new CloseHandler());

  }

  /**
   * Returns the resource bundle of this editor for translating strings.
   *
   * @return the resource bundle.
   */
  protected ResourceBundleSupport getResources()
  {
    return resources;
  }

  /**
   * Creates the JTree for the report configuration.
   *
   * @return the tree component.
   * @throws ConfigTreeModelException if the model could not be built.
   */
  private JComponent createEntryTree()
      throws ConfigTreeModelException
  {
    final InputStream in = ObjectUtilities.getResourceRelativeAsStream
        (CONFIG_DESCRIPTION_FILENAME, ConfigEditor.class);
    if (in == null)
    {
      throw new IllegalStateException(messages.getErrorString("ConfigEditor.ERROR_0002_MISSING_RESOURCE", CONFIG_DESCRIPTION_FILENAME)); //$NON-NLS-1$
    }
    try
    {
      treeModel = new ConfigTreeModel(in);
      treeModel.init(currentReportConfiguration);

      final TreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
      selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

      final JTree tree = new JTree(treeModel);
      tree.setSelectionModel(selectionModel);
      tree.setCellRenderer(new ConfigTreeRenderer());
      tree.setRootVisible(false);
      tree.setShowsRootHandles(true);
      tree.addTreeSelectionListener(new ModuleTreeSelectionHandler());

      return new JScrollPane
          (tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
              JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    finally
    {
      try
      {
        in.close();
      }
      catch (IOException e)
      {
        // can be ignored ..
      }
    }
  }

  /**
   * Creates the button pane to hold all control buttons.
   *
   * @return the created panel with all control buttons.
   */
  private JPanel createButtonPane()
  {
    final Action closeAction = new CloseAction();
    final Action saveAction = new SaveAction();
    final Action loadAction = new LoadAction();
    final Action newAction = new NewAction();

    final JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    panel.setBorder(new EmptyBorder(5, 5, 5, 5));

    final JPanel buttonHolder = new JPanel();
    buttonHolder.setLayout(new GridLayout(1, 4));
    buttonHolder.add(new ActionButton(newAction));
    buttonHolder.add(new ActionButton(loadAction));
    buttonHolder.add(new ActionButton(saveAction));
    buttonHolder.add(new ActionButton(closeAction));

    panel.add(buttonHolder);
    return panel;
  }

  /**
   * Creates the statusbar for this frame. Use setStatus() to display text on the status bar.
   *
   * @return the status bar.
   */
  protected JPanel createStatusBar()
  {
    final JPanel statusPane = new JPanel();
    statusPane.setLayout(new BorderLayout());
    statusPane.setBorder(BorderFactory.createLineBorder(UIManager.getDefaults().getColor("controlShadow"))); //$NON-NLS-1$
    statusHolder = new JLabel(" "); //$NON-NLS-1$
    statusPane.setMinimumSize(statusHolder.getPreferredSize());
    statusPane.add(statusHolder, BorderLayout.WEST);

    return statusPane;
  }

  /**
   * Defines the text to be displayed on the status bar. Setting text will replace any other previously defined text.
   *
   * @param text the new statul bar text.
   */
  private void setStatusText(final String text)
  {
    statusHolder.setText(text);
  }

//  private String getStatusText ()
//  {
//    return statusHolder.getText();
//  }

  /**
   * Loads the report configuration from a user selectable report properties file.
   */
  protected void load()
  {
    setStatusText(messages.getString("ConfigEditor.USER_LOADING_FILE")); //$NON-NLS-1$
    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      final FilesystemFilter filter = new FilesystemFilter
          (PROPERTIES_FILE_EXTENSION, messages.getString("config-editor.file-description.properties")); //$NON-NLS-1$ 
      fileChooser.addChoosableFileFilter(filter);
      fileChooser.setMultiSelectionEnabled(false);
    }

    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on .properties
      if (StringUtils.endsWithIgnoreCase(selFileName, PROPERTIES_FILE_EXTENSION) == false)
      {
        selFileName = selFileName + PROPERTIES_FILE_EXTENSION;
      }
      final Properties prop = new Properties();
      try
      {
        final InputStream in = new BufferedInputStream(new FileInputStream(selFileName));
        try
        {
          prop.load(in);
        }
        finally
        {
          in.close();
        }
      }
      catch (IOException ioe)
      {
        Log.debug(messages.getErrorString("ConfigEditor.ERROR_0003_FAILED_TO_LOAD_PROPERTIES", ioe.toString()), ioe); //$NON-NLS-1$
        setStatusText(messages.getString("ConfigEditor.ERROR_0003_FAILED_TO_LOAD_PROPERTIES", ioe.getMessage())); //$NON-NLS-1$
        return;
      }

      reset();

      final Enumeration keys = prop.keys();
      while (keys.hasMoreElements())
      {
        final String key = (String) keys.nextElement();
        final String value = prop.getProperty(key);
        currentReportConfiguration.setConfigProperty(key, value);
      }
      try
      {
        treeModel.init(currentReportConfiguration);
        setStatusText(messages.getString("ConfigEditor.USER_LOAD_PROPS_COMPLETE")); //$NON-NLS-1$
      }
      catch (ConfigTreeModelException e)
      {
        final String error = messages.getString("ConfigEditor.USER_FAILED_TO_UPDATE_MODEL"); //$NON-NLS-1$
        Log.debug(error, e);
        setStatusText(error);
      }
    }
  }

  /**
   * Resets all values.
   */
  protected void reset()
  {
    // clear all previously set configuration settings ...
    final Enumeration defaults = currentReportConfiguration.getConfigProperties();
    while (defaults.hasMoreElements())
    {
      final String key = (String) defaults.nextElement();
      currentReportConfiguration.setConfigProperty(key, null);
    }
  }

  /**
   * Saves the report configuration to a user selectable report properties file.
   */
  protected void save()
  {
    setStatusText(messages.getString("ConfigEditor.USER_SAVING")); //$NON-NLS-1$
    detailEditorPane.store();

    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      final FilesystemFilter filter = new FilesystemFilter
          (PROPERTIES_FILE_EXTENSION, messages.getString("config-editor.file-description.properties")); //$NON-NLS-1$
      fileChooser.addChoosableFileFilter(filter);
      fileChooser.setMultiSelectionEnabled(false);
    }

    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on xls
      if (StringUtils.endsWithIgnoreCase(selFileName, PROPERTIES_FILE_EXTENSION) == false)
      {
        selFileName = selFileName + PROPERTIES_FILE_EXTENSION;
      }
      write(selFileName);
    }
  }

  /**
   * Writes the configuration into the file specified by the given file name.
   *
   * @param filename the target file name
   */
  private void write(final String filename)
  {
    final Properties prop = new Properties();
    final ArrayList names = new ArrayList();
    // clear all previously set configuration settings ...
    final Enumeration defaults = currentReportConfiguration.getConfigProperties();
    while (defaults.hasMoreElements())
    {
      final String key = (String) defaults.nextElement();
      names.add(key);
      prop.setProperty(key, currentReportConfiguration.getConfigProperty(key));
    }

    Collections.sort(names);

    PrintWriter out = null;
    try
    {
       out =new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(filename))));

      for (int i = 0; i < names.size(); i++)
      {
        final String key = (String) names.get(i);
        final String value = prop.getProperty(key);

        final ConfigDescriptionEntry entry = treeModel.getEntryForKey(key);
        if (entry != null)
        {
          final String description = entry.getDescription();
          writeDescription(description, out);
        }
        saveConvert(key, ESCAPE_KEY, out);
        out.print("="); //$NON-NLS-1$
        saveConvert(value, ESCAPE_VALUE, out);
        out.println();
      }
      out.close();
      setStatusText(messages.getString("ConfigEditor.USER_SAVING_COMPLETE")); //$NON-NLS-1$
    }
    catch (IOException ioe)
    {
      Log.debug(messages.getErrorString("ConfigEditor.ERROR_0004_FAILED_PROPERTIES_SAVE", ioe.toString()), ioe); //$NON-NLS-1$
      setStatusText(messages.getString("ConfigEditor.ERROR_0004_FAILED_PROPERTIES_SAVE", ioe.getMessage())); //$NON-NLS-1$
    }
    finally
    {
      if (out != null)
      {
        out.close();
      }
    }
  }

  /**
   * Writes a descriptive comment into the given print writer.
   *
   * @param text   the text to be written. If it contains more than one line, every line will be prepended by the
   *               comment character.
   * @param writer the writer that should receive the content.
   */
  private void writeDescription(final String text, final PrintWriter writer)
  {
    // check if empty content ... this case is easy ...
    if (text.length() == 0)
    {
      return;
    }

    writer.println("# "); //$NON-NLS-1$
    final LineBreakIterator iterator = new LineBreakIterator(text);
    while (iterator.hasNext())
    {
      writer.print("# "); //$NON-NLS-1$
      saveConvert((String) iterator.next(), ESCAPE_COMMENT, writer);
      writer.println();
    }
  }

  /**
   * Performs the necessary conversion of an java string into a property escaped string.
   *
   * @param text       the text to be escaped
   * @param escapeMode the mode that should be applied.
   * @param writer     the writer that should receive the content.
   */
  private void saveConvert(final String text, final int escapeMode,
                           final PrintWriter writer)
  {
    final char[] string = text.toCharArray();
    final char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    for (int x = 0; x < string.length; x++)
    {
      final char aChar = string[x];
      switch (aChar)
      {
        case' ':
        {
          if ((escapeMode != ESCAPE_COMMENT) &&
              (x == 0 || escapeMode == ESCAPE_KEY))
          {
            writer.print('\\');
          }
          writer.print(' ');
          break;
        }
        case'\\':
        {
          writer.print('\\');
          writer.print('\\');
          break;
        }
        case'\t':
        {
          if (escapeMode == ESCAPE_COMMENT)
          {
            writer.print(aChar);
          }
          else
          {
            writer.print('\\');
            writer.print('t');
          }
          break;
        }
        case'\n':
        {
          writer.print('\\');
          writer.print('n');
          break;
        }
        case'\r':
        {
          writer.print('\\');
          writer.print('r');
          break;
        }
        case'\f':
        {
          if (escapeMode == ESCAPE_COMMENT)
          {
            writer.print(aChar);
          }
          else
          {
            writer.print('\\');
            writer.print('f');
          }
          break;
        }
        case'#':
        case'"':
        case'!':
        case'=':
        case':':
        {
          if (escapeMode == ESCAPE_COMMENT)
          {
            writer.print(aChar);
          }
          else
          {
            writer.print('\\');
            writer.print(aChar);
          }
          break;
        }
        default:
          if ((aChar < 0x0020) || (aChar > 0x007e))
          {
            writer.print('\\');
            writer.print('u');
            writer.print(hexChars[(aChar >> 12) & 0xF]);
            writer.print(hexChars[(aChar >> 8) & 0xF]);
            writer.print(hexChars[(aChar >> 4) & 0xF]);
            writer.print(hexChars[aChar & 0xF]);
          }
          else
          {
            writer.print(aChar);
          }
      }
    }
  }

  /**
   * Closes this frame and exits the JavaVM.
   */
  protected void attempClose()
  {
    System.exit(0);
  }

  /**
   * Returns the detail editor pane.
   *
   * @return the detail editor.
   */
  protected ConfigEditorPanel getDetailEditorPane()
  {
    return detailEditorPane;
  }

  /**
   * main Method to start the editor.
   *
   * @param args not used.
   */
  public static void main(final String[] args)
  {
    try
    {
      JFreeReportBoot.getInstance().start();
      final ConfigEditor ed = new ConfigEditor();
      ed.pack();
      ed.setVisible(true);
    }
    catch (Exception e)
    {
      final String message = new Messages(ConfigGUIModule.BUNDLE_NAME).getString("ConfigEditor.ERROR_0001_FAILED_TO_INITIALIZE"); //$NON-NLS-1$
      Log.debug(message, e);
      JOptionPane.showMessageDialog(null, message);
    }
  }
}
