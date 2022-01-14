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
 * ReportConverterGUI.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.gui.converter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.gui.commonswing.EncodingComboBoxModel;
import org.jfree.report.modules.gui.converter.components.OperationResultTableModel;
import org.jfree.report.modules.gui.converter.parser.ConverterParser;
import org.jfree.report.modules.parser.ext.factory.base.ArrayClassFactory;
import org.jfree.report.modules.parser.ext.factory.base.URLClassFactory;
import org.jfree.report.modules.parser.ext.factory.datasource.DefaultDataSourceFactory;
import org.jfree.report.modules.parser.ext.factory.elements.DefaultElementFactory;
import org.jfree.report.modules.parser.ext.factory.objects.BandLayoutClassFactory;
import org.jfree.report.modules.parser.ext.factory.objects.DefaultClassFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.DefaultStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.templates.DefaultTemplateCollection;
import org.jfree.report.modules.parser.extwriter.ReportWriter;
import org.jfree.report.util.i18n.Messages;
import org.jfree.ui.FilesystemFilter;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.DefaultConfiguration;
import org.jfree.util.Log;
import org.jfree.util.StringUtils;
import org.jfree.xmlns.parser.AbstractXmlResourceFactory;

/**
 * A utility application for converting XML report files from the old format to the new
 * format.
 *
 * @author Thomas Morgner.
 */
public class ReportConverterGUI extends JFrame
{
  private static class ExitWindowAdapter extends WindowAdapter
  {
    protected ExitWindowAdapter()
    {
    }

    public void windowClosing (final WindowEvent e)
    {
      System.exit(0);
    }
  }

  /**
   * An action for selecting the target.
   */
  private class SelectTargetAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    protected SelectTargetAction ()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.selectTarget.name")); //$NON-NLS-1$
      putValue(Action.SHORT_DESCRIPTION,
              getResources().getString("convertdialog.action.selectTarget.description")); //$NON-NLS-1$
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the action event.
     */
    public void actionPerformed (final ActionEvent e)
    {
      setTargetFile(performSelectFile(getTargetFile(), true));
    }
  }

  /**
   * An action for selecting the source file.
   */
  private class SelectSourceAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    protected SelectSourceAction ()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.selectSource.name")); //$NON-NLS-1$
      putValue(Action.SHORT_DESCRIPTION,
              getResources().getString("convertdialog.action.selectSource.description")); //$NON-NLS-1$
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the action event.
     */
    public void actionPerformed (final ActionEvent e)
    {
      setSourceFile(performSelectFile(getSourceFile(), false));
    }
  }

  /**
   * An action for converting an XML report definition from the old format to the new.
   */
  private class ConvertAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    protected ConvertAction ()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.convert.name")); //$NON-NLS-1$
      putValue(Action.SHORT_DESCRIPTION,
              getResources().getString("convertdialog.action.convert.description")); //$NON-NLS-1$
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the action event.
     */
    public void actionPerformed (final ActionEvent e)
    {
      convert();
    }
  }

  /**
   * The source field.
   */
  private final JTextField sourceField;

  /**
   * The target field.
   */
  private final JTextField targetField;

  /**
   * A file chooser.
   */
  private final JFileChooser fileChooser;

  /**
   * Localised resources.
   */
  private ResourceBundle resources;

  /**
   * The encoding combo box model used to select the target file encoding.
   */
  private final EncodingComboBoxModel encodingModel;

  /**
     * The destTable model that displays all messages from the conversion.
     */
  private final OperationResultTableModel resultTableModel;

  /**
   * A primitive status bar.
   */
  private JLabel statusHolder;
  
  private final Messages messages;

  /**
   * Default constructor.
   */
  public ReportConverterGUI ()
  {
    messages = new Messages(getLocale(), ConverterGUIModule.BUNDLE_NAME);
    encodingModel = EncodingComboBoxModel.createDefaultModel(Locale.getDefault());
    encodingModel.ensureEncodingAvailable("UTF-16"); //$NON-NLS-1$
    encodingModel.setSelectedIndex(encodingModel.indexOf("UTF-16")); //$NON-NLS-1$
    encodingModel.sort();

    resultTableModel = new OperationResultTableModel();

    sourceField = new JTextField();
    targetField = new JTextField();

    final JTable table = new JTable(resultTableModel);
    table.setMinimumSize(new Dimension(100, 100));
    final JSplitPane componentPane = new JSplitPane
            (JSplitPane.VERTICAL_SPLIT, createMainPane(), new JScrollPane(table));
    componentPane.setOneTouchExpandable(true);
    componentPane.resetToPreferredSizes();

    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(componentPane, BorderLayout.CENTER);
    contentPane.add(createStatusBar(), BorderLayout.SOUTH);
    setContentPane(contentPane);

    fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new FilesystemFilter(new String[]{".xml"}, //$NON-NLS-1$
            messages.getString("convertdialog.file-description.xml"), true)); //$NON-NLS-1$
    fileChooser.addChoosableFileFilter(new FilesystemFilter(new String[]{".report"}, //$NON-NLS-1$
            messages.getString("convertdialog.file-description.report"), true)); //$NON-NLS-1$
    fileChooser.setMultiSelectionEnabled(false);

    setTitle(getResources().getString("convertdialog.title")); //$NON-NLS-1$
  }

  /**
   * Creates the statusbar for this frame. Use setStatus() to display text on the status
   * bar.
   *
   * @return the status bar.
   */
  protected JPanel createStatusBar ()
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
   * Sets the text of the status line.
   *
   * @param text the new text that should be displayed in the status bar.
   */
  public void setStatusText (final String text)
  {
    statusHolder.setText(text);
  }

  /**
   * Returns the text from the status bar.
   *
   * @return the status bar text.
   */
  public String getStatusText ()
  {
    return statusHolder.getText();
  }

  /**
   * Creates the main panel and all child components.
   *
   * @return the created panel.
   */
  private JPanel createMainPane ()
  {
    final JButton selectSourceButton = new ActionButton(new SelectSourceAction());
    final JButton selectTargetButton = new ActionButton(new SelectTargetAction());
    final JButton convertFilesButton = new ActionButton(new ConvertAction());

    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(new JLabel(getResources().getString("convertdialog.sourceFile")), gbc); //$NON-NLS-1$

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    contentPane.add(sourceField, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(selectSourceButton, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(new JLabel(getResources().getString("convertdialog.targetFile")), gbc); //$NON-NLS-1$

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    contentPane.add(targetField, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(selectTargetButton, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(new JLabel(getResources().getString("convertdialog.encoding")), gbc); //$NON-NLS-1$

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    contentPane.add(new JComboBox(encodingModel), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(convertFilesButton, gbc);

    return contentPane;
  }

  /**
   * Starting point for the utility application.
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final ReportConverterGUI gui = new ReportConverterGUI();
    gui.addWindowListener(new ExitWindowAdapter());
    gui.pack();
    gui.setVisible(true);
  }

  /**
   * Validates the contents of the dialogs input fields. If the selected file exists, it
   * is also checked for validity.
   *
   * @param filename the file name.
   * @return true, if the input is valid, false otherwise
   */
  public boolean performTargetValidate (final String filename)
  {
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
              getResources().getString("convertdialog.targetIsEmpty"), //$NON-NLS-1$
              getResources().getString("convertdialog.errorTitle"), //$NON-NLS-1$
              JOptionPane.ERROR_MESSAGE);
      return false;
    }
    final File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        JOptionPane.showMessageDialog(this,
                getResources().getString("convertdialog.targetIsNoFile"), //$NON-NLS-1$
                getResources().getString("convertdialog.errorTitle"), //$NON-NLS-1$
                JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (f.canWrite() == false)
      {
        JOptionPane.showMessageDialog(this,
                getResources().getString("convertdialog.targetIsNotWritable"), //$NON-NLS-1$
                getResources().getString("convertdialog.errorTitle"), //$NON-NLS-1$
                JOptionPane.ERROR_MESSAGE);
        return false;
      }
      final String key1 = "convertdialog.targetOverwriteConfirmation"; //$NON-NLS-1$
      final String key2 = "convertdialog.targetOverwriteTitle"; //$NON-NLS-1$
      if (JOptionPane.showConfirmDialog(this,
              MessageFormat.format(getResources().getString(key1),
                      new Object[]{filename}),
              getResources().getString(key2),
              JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
              == JOptionPane.NO_OPTION)
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Validates the contents of the dialogs input fields. If the selected file exists, it
   * is also checked for validity.
   *
   * @param filename the file name.
   * @return true, if the input is valid, false otherwise
   */
  public boolean performSourceValidate (final String filename)
  {
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
              getResources().getString("convertdialog.sourceIsEmpty"), //$NON-NLS-1$
              getResources().getString("convertdialog.errorTitle"), //$NON-NLS-1$
              JOptionPane.ERROR_MESSAGE);
      return false;
    }
    final File f = new File(filename);
    if (f.exists() == false)
    {
      return false;
    }

    if (f.isFile() == false)
    {
      JOptionPane.showMessageDialog(this,
              getResources().getString("convertdialog.sourceIsNoFile"), //$NON-NLS-1$
              getResources().getString("convertdialog.errorTitle"), //$NON-NLS-1$
              JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (f.canRead() == false)
    {
      JOptionPane.showMessageDialog(this,
              getResources().getString("convertdialog.sourceIsNotReadable"), //$NON-NLS-1$
              getResources().getString("convertdialog.errorTitle"), //$NON-NLS-1$
              JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  /**
   * Retrieves the resources for the frame. If the resources are not initialized, they get
   * loaded on the first call to this method.
   *
   * @return The resources.
   */
  protected ResourceBundle getResources ()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(ConverterGUIModule.BUNDLE_NAME);
    }
    return resources;
  }

  /**
   * Sets the source file.
   *
   * @param file the file name.
   */
  protected void setSourceFile (final String file)
  {
    sourceField.setText(file);
  }

  /**
   * Sets the target file.
   *
   * @param file the file name.
   */
  protected void setTargetFile (final String file)
  {
    targetField.setText(file);
  }

  /**
   * Returns the source file name.
   *
   * @return The name.
   */
  protected String getSourceFile ()
  {
    return sourceField.getText();
  }

  /**
   * Returns the name of the target file.
   *
   * @return The name.
   */
  protected String getTargetFile ()
  {
    return targetField.getText();
  }

  /**
   * Performs the conversion, returning <code>true</code> if the conversion is successful,
   * and <code>false</code> otherwise.
   *
   * @return A boolean.
   */
  public boolean convert ()
  {
    
    if (performSourceValidate(getSourceFile()) == false)
    {
      setStatusText(messages.getString("ReportConverterGUI.USER_VALIDATE_SOURCE_FAILED")); //$NON-NLS-1$
      return false;
    }
    if (performTargetValidate(getTargetFile()) == false)
    {
      setStatusText(messages.getString("ReportConverterGUI.USER_VALIDATE_TARGET_FAILED")); //$NON-NLS-1$
      return false;
    }

    final ConverterParser frontend = new ConverterParser();
    try
    {
      final File sourceFile = new File(getSourceFile());
      final File targetFile = new File(getTargetFile());
      final String encoding = encodingModel.getSelectedEncoding();
      final JFreeReport report =
          frontend.parse(sourceFile.toURL(), sourceFile.toURL());
      if (report == null)
      {
        setStatusText(messages.getString("ReportConverterGUI.USER_PARSING_SOURCE_FAILED")); //$NON-NLS-1$
        return false;
      }


      final DefaultConfiguration config = new DefaultConfiguration();
      config.setProperty(AbstractXmlResourceFactory.CONTENTBASE_KEY,
          targetFile.toURL().toExternalForm());

      // adding all factories will make sure that all stylekeys are found,
      // even if the report was parsed from a simple report definition
      final ReportWriter writer = new ReportWriter(report, encoding, config);
      writer.addClassFactoryFactory(new URLClassFactory());
      writer.addClassFactoryFactory(new DefaultClassFactory());
      writer.addClassFactoryFactory(new BandLayoutClassFactory());
      writer.addClassFactoryFactory(new ArrayClassFactory());

      writer.addStyleKeyFactory(new DefaultStyleKeyFactory());
      writer.addStyleKeyFactory(new PageableLayoutStyleKeyFactory());
      writer.addTemplateCollection(new DefaultTemplateCollection());
      writer.addElementFactory(new DefaultElementFactory());
      writer.addDataSourceFactory(new DefaultDataSourceFactory());

      OutputStream base = new FileOutputStream(targetFile);
      try
      {
        final Writer w = new BufferedWriter(new OutputStreamWriter(base, encoding));
        writer.write(w);
        w.close();
        base = null;
      }
      finally
      {
        if (base != null)
        {
          try
          {
            base.close();
          }
          catch(IOException ioe)
          {
            // Ignored. If writing failed for some reason, we dont want the close()-exception to hide the
            // real exception.
          }
        }
      }
      setStatusText(messages.getString("ReportConverterGUI.USER_CONVERSION_COMPLETE")); //$NON-NLS-1$
      return true;
    }
    catch (Exception e)
    {
      final String error = messages.getString("ReportConverterGUI.ERROR_0001_CONVERSION_FAILED", e.getMessage()); //$NON-NLS-1$
      Log.warn(error, e);
      setStatusText(error);
      return false;
    }
    finally
    {
      resultTableModel.setData(frontend.getErrors());
    }
  }

  /**
   * Selects a file to use.
   *
   * @param filename  the current selection.
   * @param appendExt append an extension?
   * @return The file name.
   */
  protected String performSelectFile (final String filename, final boolean appendExt)
  {
    final File file = new File(filename);
    fileChooser.setCurrentDirectory(file);
    fileChooser.setSelectedFile(file);
    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      if (appendExt)
      {
        if ((StringUtils.endsWithIgnoreCase(selFileName, ".xml") == false) &&
            (StringUtils.endsWithIgnoreCase(selFileName, ".report") == false)) //$NON-NLS-1$
        {
          selFileName = selFileName + ".report"; //$NON-NLS-1$
        }
      }
      return selFileName;
    }
    return filename;
  }

}
