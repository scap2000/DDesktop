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
 * RTFExportDialog.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.rtf;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import java.io.File;

import java.text.MessageFormat;

import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.commonswing.AbstractExportDialog;
import org.jfree.report.modules.gui.commonswing.JStatusBar;
import org.jfree.report.modules.gui.commonswing.StatusType;
import org.jfree.ui.FilesystemFilter;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.Configuration;
import org.jfree.util.DefaultConfiguration;
import org.jfree.util.StringUtils;

/**
 * A dialog that is used to prepare the printing of a report into an RTF file.
 * <p/>
 * The main method to call the dialog is {@link RTFExportDialog#performQueryForExport(JFreeReport)}.
 * Given a report, the dialog is shown and if the user approved the dialog, the
 * RTF file is saved using the settings made in the dialog.
 *
 * @author Heiko Evermann
 */
public class RTFExportDialog extends AbstractExportDialog
{
  /**
   * Internal action class to select a target file.
   */
  private class ActionSelectFile extends AbstractAction
  {
    /**
     * Default constructor.
     */
    protected ActionSelectFile(final ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("rtf-exportdialog.selectFile")); //$NON-NLS-1$
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      performSelectFile();
    }
  }

  /**
   * Select file action.
   */
  private Action actionSelectFile;

  /**
   * Filename text field.
   */
  private JTextField txFilename;

  /**
   * The strict layout check-box.
   */
  private JCheckBox cbStrictLayout;

  /**
   * A file chooser.
   */
  private JFileChooser fileChooser;
  private static final String RTF_FILE_EXTENSION = ".rtf"; //$NON-NLS-1$
  private JStatusBar statusBar;

  /**
   * Creates a new Excel save dialog.
   *
   * @param owner the dialog owner.
   */
  public RTFExportDialog(final Frame owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new Excel dialog.
   *
   * @param owner the dialog owner.
   */
  public RTFExportDialog(final Dialog owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new Excel save dialog.  The created dialog is modal.
   */
  public RTFExportDialog()
  {
    initConstructor();
  }

  protected String getConfigurationSuffix()
  {
    return "_rtfexport"; //$NON-NLS-1$
  }

  /**
   * Initialisation.
   */
  private void initConstructor()
  {
    actionSelectFile = new ActionSelectFile(getResources());
    statusBar = new JStatusBar();
    setTitle(getResources().getString("rtf-exportdialog.dialogtitle")); //$NON-NLS-1$
    initialize();
    clear();
  }

  /**
   * Returns a single instance of the file selection action.
   *
   * @return the action.
   */
  private Action getActionSelectFile()
  {
    return actionSelectFile;
  }

  public JStatusBar getStatusBar()
  {
    return statusBar;
  }

  /**
   * Initializes the Swing components of this dialog.
   */
  private void initialize()
  {
    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    final JLabel lblFileName = new JLabel(getResources().getString(
        "rtf-exportdialog.filename")); //$NON-NLS-1$
    final JButton btnSelect = new ActionButton(getActionSelectFile());

    txFilename = new JTextField();
    cbStrictLayout = new JCheckBox(getResources().getString(
        "rtf-exportdialog.strict-layout")); //$NON-NLS-1$

    getFormValidator().registerButton(cbStrictLayout);
    getFormValidator().registerTextField(txFilename);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 5);
    contentPane.add(lblFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.ipadx = 120;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(txFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.ipadx = 120;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbStrictLayout, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    contentPane.add(btnSelect, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.REMAINDER;
    gbc.insets = new Insets(10, 1, 1, 1);
    contentPane.add(new JPanel(), gbc);

    setContentPane(createContentPane(contentPane));
  }

  /**
   * Returns the filename of the RTF file.
   *
   * @return the name of the file where to save the RTF file.
   */
  public String getFilename()
  {
    return txFilename.getText();
  }

  /**
   * Defines the filename of the RTF file.
   *
   * @param filename the filename of the RTF file
   */
  public void setFilename(final String filename)
  {
    this.txFilename.setText(filename);
  }

  /**
   * Returns the setting of the 'strict layout' check-box.
   *
   * @return A boolean.
   */
  public boolean isStrictLayout()
  {
    return cbStrictLayout.isSelected();
  }

  /**
   * Sets the 'strict-layout' check-box.
   *
   * @param strictLayout the new setting.
   */
  public void setStrictLayout(final boolean strictLayout)
  {
    cbStrictLayout.setSelected(strictLayout);
  }

  /**
   * Clears all selections and input fields.
   */
  public void clear()
  {
    txFilename.setText(""); //$NON-NLS-1$
    cbStrictLayout.setSelected(false);
  }

  /**
   * Returns a new (and not connected to the default config from the job)
   * configuration containing all properties from the dialog.
   *
   * @param full
   * @return
   */
  protected Configuration grabDialogContents(final boolean full)
  {
    final DefaultConfiguration p = new DefaultConfiguration();
    if (full)
    {
      p.setProperty("org.jfree.report.modules.gui.rtf.FileName", getFilename()); //$NON-NLS-1$
    }
    p.setProperty("org.jfree.report.modules.output.table.rtf.StrictLayout", String.valueOf(isStrictLayout())); //$NON-NLS-1$
    return p;
  }

  /**
   * Initialises the Excel export dialog from the settings in the report
   * configuration.
   *
   * @param config the report configuration.
   */
  protected void setDialogContents(final Configuration config)
  {
    final String strict = config.getConfigProperty
        ("org.jfree.report.modules.output.table.rtf.StrictLayout"); //$NON-NLS-1$
    setStrictLayout("true".equals(strict)); //$NON-NLS-1$

    final String defaultFileName = config.getConfigProperty
        ("org.jfree.report.modules.gui.rtf.FileName"); //$NON-NLS-1$
    if (defaultFileName != null)
    {
      setFilename(resolvePath(defaultFileName).getAbsolutePath());
    }
  }

  /**
   * Selects a file to use as target for the report processing.
   */
  protected void performSelectFile()
  {
    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      final FilesystemFilter filter = new FilesystemFilter(RTF_FILE_EXTENSION,
          getResources().getString("rtf-exportdialog.rtf-file-description")); //$NON-NLS-1$
      fileChooser.addChoosableFileFilter(filter);
      fileChooser.setMultiSelectionEnabled(false);
    }

    final File file = new File(getFilename());
    fileChooser.setCurrentDirectory(file);
    fileChooser.setSelectedFile(file);
    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on xls
      if (StringUtils.endsWithIgnoreCase(selFileName, RTF_FILE_EXTENSION) == false)
      {
        selFileName = selFileName + RTF_FILE_EXTENSION;
      }
      setFilename(selFileName);
    }
  }

  /**
   * Validates the contents of the dialog's input fields. If the selected file
   * exists, it is also checked for validity.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performValidate()
  {
    getStatusBar().clear();

    final String filename = getFilename();
    if (filename.trim().length() == 0)
    {
      getStatusBar().setStatus(StatusType.ERROR,
          getResources().getString("rtf-exportdialog.targetIsEmpty")); //$NON-NLS-1$
      return false;
    }
    final File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        getStatusBar().setStatus(StatusType.ERROR,
            getResources().getString("rtf-exportdialog.targetIsNoFile")); //$NON-NLS-1$
        return false;
      }
      if (f.canWrite() == false)
      {
        getStatusBar().setStatus(StatusType.ERROR,
            getResources().getString(
                "rtf-exportdialog.targetIsNotWritable")); //$NON-NLS-1$
        return false;
      }
      final String message = MessageFormat.format(getResources().getString
          ("rtf-exportdialog.targetExistsWarning"), //$NON-NLS-1$
          new Object[]{filename});
      getStatusBar().setStatus(StatusType.WARNING, message);
    }
    return true;
  }

  protected boolean performConfirm()
  {
    final String filename = getFilename();
    final File f = new File(filename);
    if (f.exists())
    {
      final String key1 = "rtf-exportdialog.targetOverwriteConfirmation"; //$NON-NLS-1$
      final String key2 = "rtf-exportdialog.targetOverwriteTitle"; //$NON-NLS-1$
      if (JOptionPane.showConfirmDialog(this,
          MessageFormat.format(getResources().getString(key1),
              new Object[]{getFilename()}), getResources().getString(key2),
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
          == JOptionPane.NO_OPTION)
      {
        return false;
      }
    }
    return true;
  }


  protected String getResourceBaseName()
  {
    return RTFExportPlugin.BASE_RESOURCE_CLASS;
  }

  protected String getConfigurationPrefix()
  {
    return "org.jfree.report.modules.gui.rtf."; //$NON-NLS-1$
  }
}
