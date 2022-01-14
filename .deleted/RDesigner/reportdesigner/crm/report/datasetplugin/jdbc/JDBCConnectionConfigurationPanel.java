/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.BundleAlreadyExistsException;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.ExceptionUtils;
import org.pentaho.reportdesigner.lib.client.util.TextFieldCompletionSupport;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;
import org.pentaho.reportdesigner.lib.common.util.LoggerUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 10.02.2006
 * Time: 07:51:54
 */
public class JDBCConnectionConfigurationPanel extends JPanel
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(JDBCConnectionConfigurationPanel.class.getName());

    @NotNull
    public static final String CONNECTION_TEST = "connectionTest";
    @NotNull
    private static final String JAR_ENDING = ".jar";
    @NotNull
    private static final String ZIP_ENDING = ".zip";
    @NotNull
    private static final String SAMPLE_DB_USERNAME = "sa";


    public static void main(@NotNull String[] args) throws BundleAlreadyExistsException
    {
        LoggerUtil.initLogger();

        Locale.setDefault(Locale.ENGLISH);
        TranslationManager.getInstance().addBundle("R", ResourceBundle.getBundle("res.Translations", Locale.getDefault()));//NON-NLS
        TranslationManager.getInstance().addSupportedLocale(Locale.GERMAN);
        TranslationManager.getInstance().addSupportedLocale(Locale.ENGLISH);


        @NonNls
        JFrame frame = new JFrame("JDBCConnectionConfigurationPanel");
        WorkspaceSettings workspaceSettings = WorkspaceSettings.getInstance();
        workspaceSettings.put("JDBCConnectionConfigurationPanel.LastSelectedFile", "C:\\Daten\\07_Projekte\\Report\\org.pentaho.reportdesigner.crm.report\\lib\\postgresql-8.1dev-403.jdbc3.jar");//NON-NLS
        frame.getContentPane().add(new JDBCConnectionConfigurationPanel(frame, workspaceSettings), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 500, 500);
        frame.setVisible(true);

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                Collection<String> missingKeysList = TranslationManager.getInstance().getMissingKeysList();
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "MISSING KEYS");
                for (String s : missingKeysList)
                {
                    //noinspection UseOfSystemOutOrSystemErr
                    System.out.println(s);
                }
            }
        });
    }


    @NotNull
    private static final String ORG_HSQLDB_JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    @NotNull
    private static final String[] DRIVER_CLASSES = new String[]{ORG_HSQLDB_JDBC_DRIVER,
                                                                "org.postgresql.Driver",
                                                                "com.mysql.jdbc.Driver",
                                                                "COM.ibm.db2.jdbc.app.DB2Driver",
                                                                "oracle.jdbc.driver.OracleDriver",
    };

    @NonNls
    @NotNull
    private static final HashMap<String, String> connectionStringMap = new HashMap<String, String>();


    @NotNull
    private static final String JDBC_HSQLDB_MEM_SAMPLE = "jdbc:hsqldb:mem:sample";


    static
    {
        connectionStringMap.put(DRIVER_CLASSES[0], JDBC_HSQLDB_MEM_SAMPLE);
        connectionStringMap.put(DRIVER_CLASSES[1], "jdbc:postgresql:");
        connectionStringMap.put(DRIVER_CLASSES[2], "jdbc:mysql:");
        connectionStringMap.put(DRIVER_CLASSES[3], "jdbc:db2:");
        connectionStringMap.put(DRIVER_CLASSES[4], "jdbc:oracle:thin:");
    }


    @NotNull
    private Component parent;
    @NotNull
    private WorkspaceSettings workspaceSettings;

    @NotNull
    private JLabel jarLabel;
    @NotNull
    private JList jarList;
    @NotNull
    private JarListModel jarListModel;
    @NotNull
    private JButton jarAddButton;
    @NotNull
    private JButton jarRemoveButton;

    @NotNull
    private JLabel classLabel;
    @NotNull
    private JComboBox driverComboBox;
    @NotNull
    private JLabel connectionStringLabel;
    @NotNull
    private JComboBox connectionStringComboBox;
    @NotNull
    private JLabel usernameLabel;
    @NotNull
    private JTextField usernameTextField;
    @NotNull
    private JLabel passwordLabel;
    @NotNull
    private JPasswordField passwordField;
    @NotNull
    private JButton testConnectionButton;

    @NotNull
    private TreeSet<String> usernameSet;
    @NotNull
    private TreeSet<String> connectionStringSet;
    @NotNull
    private TreeSet<String> driversSet;

    @NotNull
    private PropertyChangeSupport propertyChangeSupport;


    public JDBCConnectionConfigurationPanel(@NotNull Component parent, @NotNull WorkspaceSettings workspaceSettings)
    {
        //noinspection ConstantConditions
        if (parent == null)
        {
            throw new IllegalArgumentException("parent must not be null");
        }
        //noinspection ConstantConditions
        if (workspaceSettings == null)
        {
            throw new IllegalArgumentException("workspaceSettings must not be null");
        }

        this.parent = parent;
        this.workspaceSettings = workspaceSettings;
        propertyChangeSupport = new PropertyChangeSupport(this);

        initGUI();
        initLogic();
    }


    private void initLogic()
    {
        jarList.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(@NotNull ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    jarRemoveButton.setEnabled(jarList.getSelectedIndex() != -1);
                }
            }
        });

        jarRemoveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                String[] oldJars = jarListModel.getJars();
                int[] selectedIndices = jarList.getSelectedIndices();
                for (int i = selectedIndices.length - 1; i >= 0; i--)
                {
                    int selectedIndex = selectedIndices[i];
                    jarListModel.remove(selectedIndex);
                }
                validateDriverComboBox();
                firePropertyChange(PropertyKeys.JARS, oldJars, jarListModel.getJars());
            }
        });

        jarAddButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                String lastSelectedFile = workspaceSettings.getString("JDBCConnectionConfigurationPanel.LastSelectedFile");
                JFileChooser fileChooser;
                if (lastSelectedFile != null)
                {
                    File file = new File(lastSelectedFile);
                    fileChooser = new JFileChooser(file);
                    fileChooser.setSelectedFile(file);
                }
                else
                {
                    fileChooser = new JFileChooser();
                }

                fileChooser.setFileFilter(new FileFilter()
                {
                    public boolean accept(@NotNull File f)
                    {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(JAR_ENDING) || f.getName().toLowerCase().endsWith(ZIP_ENDING);
                    }


                    @NotNull
                    public String getDescription()
                    {
                        return TranslationManager.getInstance().getTranslation("R", "JDBCConnectionConfigurationPanel.FileTypeDescription");
                    }
                });

                int i = fileChooser.showOpenDialog(parent);
                if (i == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        String[] oldJars = jarListModel.getJars();
                        String newJar = fileChooser.getSelectedFile().getCanonicalPath();
                        for (String jar : oldJars)
                        {
                            if (jar.equals(newJar))
                            {
                                return;
                            }
                        }
                        jarListModel.addElement(newJar);
                        String[] newJars = jarListModel.getJars();
                        workspaceSettings.put("JDBCConnectionConfigurationPanel.LastSelectedFile", newJar);
                        workspaceSettings.put("JDBCConnectionConfigurationPanel.LastSelectedFiles", Arrays.asList(newJars));

                        propertyChangeSupport.firePropertyChange(PropertyKeys.JARS, oldJars, newJars);
                    }
                    catch (IOException e1)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCConnectionConfigurationPanel.actionPerformed ", e1);
                    }
                }
            }
        });

        propertyChangeSupport.addPropertyChangeListener(PropertyKeys.JARS, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                validateDriverComboBox();
            }
        });

        Component editorComponent = driverComboBox.getEditor().getEditorComponent();
        if (editorComponent instanceof JTextField)
        {
            JTextField textField = (JTextField) editorComponent;
            textField.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(@NotNull DocumentEvent e)
                {
                    validateDriverComboBox();
                }


                public void removeUpdate(@NotNull DocumentEvent e)
                {
                    validateDriverComboBox();
                }


                public void changedUpdate(@NotNull DocumentEvent e)
                {
                    validateDriverComboBox();
                }
            });
        }

        testConnectionButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Component editorComponent = driverComboBox.getEditor().getEditorComponent();
                if (editorComponent instanceof JTextField)
                {
                    testConnection();
                }
            }
        });

        DocumentListener modificationListener = new DocumentListener()
        {
            public void insertUpdate(@NotNull DocumentEvent e)
            {
                propertyChangeSupport.firePropertyChange(CONNECTION_TEST, Boolean.TRUE, Boolean.FALSE);
            }


            public void removeUpdate(@NotNull DocumentEvent e)
            {
                propertyChangeSupport.firePropertyChange(CONNECTION_TEST, Boolean.TRUE, Boolean.FALSE);
            }


            public void changedUpdate(@NotNull DocumentEvent e)
            {
                propertyChangeSupport.firePropertyChange(CONNECTION_TEST, Boolean.TRUE, Boolean.FALSE);
            }
        };

        if (editorComponent instanceof JTextField)
        {
            JTextField textField = (JTextField) editorComponent;
            textField.getDocument().addDocumentListener(modificationListener);
            UndoHelper.installUndoSupport(textField);
            TextComponentHelper.installDefaultPopupMenu(textField);
        }

        Component csEditorComponent = connectionStringComboBox.getEditor().getEditorComponent();
        if (csEditorComponent instanceof JTextField)
        {
            JTextField textField = (JTextField) csEditorComponent;
            textField.getDocument().addDocumentListener(modificationListener);
            UndoHelper.installUndoSupport(textField);
            TextComponentHelper.installDefaultPopupMenu(textField);
        }

        usernameTextField.getDocument().addDocumentListener(modificationListener);
        UndoHelper.installUndoSupport(usernameTextField);
        TextComponentHelper.installDefaultPopupMenu(usernameTextField);

        passwordField.getDocument().addDocumentListener(modificationListener);
        UndoHelper.installUndoSupport(passwordField);
        TextComponentHelper.installDefaultPopupMenu(passwordField);

        validateDriverComboBox();
    }


    private void testConnection()
    {
        final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(parent,
                                                                                  TranslationManager.getInstance().getTranslation("R", "JDBCConnectionConfigurationPanel.ProgressDialog.Title"),
                                                                                  "");

        final String url = connectionStringComboBox.getSelectedItem().toString();
        final String username = usernameTextField.getText();
        final String password = new String(passwordField.getPassword());

        Thread t = new Thread()
        {
            public void run()
            {
                boolean success = false;
                String errorMessage = "";

                try
                {
                    JDBCClassLoader.getConnection(getJars(), getDriverClass(), url, username, password);
                    success = true;
                }
                catch (Throwable e1)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCConnectionConfigurationPanel.actionPerformed ", e1);
                    //noinspection InstanceofCatchParameter
                    if (e1 instanceof JDBCClassLoader.CompoundException && e1.getCause() instanceof ClassNotFoundException)
                    {
                        errorMessage = TranslationManager.getInstance().getTranslation("R", "JDBCConnectionConfigurationPanel.ClassNotFoundException", e1.getMessage());
                    }
                    else
                    {
                        errorMessage = e1.getMessage();
                    }
                }

                try
                {
                    final boolean ts = success;
                    final String em = errorMessage;
                    EventQueue.invokeAndWait(new Runnable()
                    {
                        public void run()
                        {
                            propertyChangeSupport.firePropertyChange(CONNECTION_TEST, Boolean.valueOf(!ts), Boolean.valueOf(ts));
                            progressDialog.dispose();
                            if (!ts)
                            {
                                JOptionPane.showMessageDialog(parent, TranslationManager.getInstance().getTranslation("R", "ConnectionTest.Error.Message", em), TranslationManager.getInstance().getTranslation("R", "ConnectionTest.Error.Title"), JOptionPane.ERROR_MESSAGE);
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(parent, TranslationManager.getInstance().getTranslation("R", "ConnectionTest.Success.Message"), TranslationManager.getInstance().getTranslation("R", "ConnectionTest.Success.Title"), JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCConnectionConfigurationPanel.run ", e);
                    ExceptionUtils.disposeDialogInEDT(progressDialog);
                    UncaughtExcpetionsModel.getInstance().addException(e);
                }
            }
        };

        t.setDaemon(true);
        t.setPriority(Thread.NORM_PRIORITY - 1);
        t.start();

        if (t.isAlive())
        {
            progressDialog.setVisible(true);
        }
    }


    private void validateDriverComboBox()
    {
        Component editorComponent = driverComboBox.getEditor().getEditorComponent();
        if (editorComponent instanceof JTextField)
        {
            JTextField textField = (JTextField) editorComponent;

            if (JDBCClassLoader.tryToFindClass(getJars(), textField.getText()))
            {
                textField.setForeground(Color.BLACK);
                return;
            }

            textField.setForeground(Color.RED);
        }
    }


    private void initGUI()
    {
        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, default, 4dlu, fill:10dlu:grow, 2dlu, pref, 0dlu",
                                               "0dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "0dlu");

        @NonNls
        CellConstraints cc = new CellConstraints();
        setLayout(formLayout);

        ArrayList<String> lastSelectedFiles = workspaceSettings.getList("JDBCConnectionConfigurationPanel.LastSelectedFiles");
        jarListModel = new JarListModel();
        jarListModel.setJars(lastSelectedFiles.toArray(new String[lastSelectedFiles.size()]));
        jarList = new JList(jarListModel);
        jarAddButton = ComponentFactory.createButton("R", "JDBCConnectionConfigurationPanel.ButtonJar");

        jarLabel = ComponentFactory.createLabel("R", "JDBCConnectionConfigurationPanel.LabelJar", jarList);

        jarRemoveButton = ComponentFactory.createButton("R", "JDBCConnectionConfigurationPanel.ButtonCleanJar");
        jarRemoveButton.setEnabled(false);

        add(jarLabel, cc.xy(2, 2));
        add(new JScrollPane(jarList), cc.xywh(4, 2, 1, 3));
        add(jarAddButton, cc.xy(6, 2));
        add(jarRemoveButton, cc.xy(6, 4));

        driversSet = new TreeSet<String>(workspaceSettings.getList("JDBCConnectionConfigurationPanel.Drivers.Completion"));
        driversSet.addAll(Arrays.asList(DRIVER_CLASSES));
        driverComboBox = new JComboBox(driversSet.toArray());
        driverComboBox.setEditable(true);

        classLabel = ComponentFactory.createLabel("R", "JDBCConnectionConfigurationPanel.LabelDriverClass", driverComboBox);

        boolean defaultDriver = false;
        String lastSelectedDriver = workspaceSettings.getString("JDBCConnectionConfigurationPanel.Drivers.LastSelectedValue");
        if (lastSelectedDriver != null)
        {
            driverComboBox.setSelectedItem(lastSelectedDriver);
        }
        else
        {
            driverComboBox.setSelectedItem(ORG_HSQLDB_JDBC_DRIVER);
        }

        if (ORG_HSQLDB_JDBC_DRIVER.equals(driverComboBox.getSelectedItem()))
        {
            defaultDriver = true;
        }

        add(classLabel, cc.xy(2, 6));
        add(driverComboBox, cc.xy(4, 6));

        connectionStringSet = new TreeSet<String>(workspaceSettings.getList("JDBCConnectionConfigurationPanel.ConnectionString.Completion"));
        connectionStringSet.add(JDBC_HSQLDB_MEM_SAMPLE);
        connectionStringSet.addAll(connectionStringMap.values());
        connectionStringComboBox = new JComboBox(connectionStringSet.toArray());
        connectionStringComboBox.setEditable(true);

        connectionStringLabel = ComponentFactory.createLabel("R", "JDBCConnectionConfigurationPanel.LabelConnectionString", connectionStringComboBox);

        boolean defaultConnectionString = false;
        String lastSelectedConnectionString = workspaceSettings.getString("JDBCConnectionConfigurationPanel.ConnectionString.LastSelectedValue");
        if (lastSelectedConnectionString != null)
        {
            connectionStringComboBox.setSelectedItem(lastSelectedConnectionString);
        }
        else
        {
            connectionStringComboBox.setSelectedItem(JDBC_HSQLDB_MEM_SAMPLE);
        }

        if (JDBC_HSQLDB_MEM_SAMPLE.equals(connectionStringComboBox.getSelectedItem()))
        {
            defaultConnectionString = true;
        }

        add(connectionStringLabel, cc.xy(2, 8));
        add(connectionStringComboBox, cc.xy(4, 8));

        usernameTextField = new JTextField();
        usernameLabel = ComponentFactory.createLabel("R", "JDBCConnectionConfigurationPanel.LabelUsername", usernameTextField);
        usernameSet = new TreeSet<String>(workspaceSettings.getList("JDBCConnectionConfigurationPanel.Username.Completion"));
        TextFieldCompletionSupport.initCompletionSupport(usernameSet, usernameTextField);

        if (defaultDriver && defaultConnectionString)
        {
            usernameTextField.setText(SAMPLE_DB_USERNAME);
        }

        add(usernameLabel, cc.xy(2, 10));
        add(usernameTextField, cc.xy(4, 10));

        passwordField = new JPasswordField();
        passwordLabel = ComponentFactory.createLabel("R", "JDBCConnectionConfigurationPanel.LabelPassword", passwordField);
        if (passwordField.getFont().canDisplay('\u2022'))
        {
            passwordField.setEchoChar('\u2022');
        }

        add(passwordLabel, cc.xy(2, 12));
        add(passwordField, cc.xy(4, 12));

        testConnectionButton = ComponentFactory.createButton("R", "JDBCConnectionConfigurationPanel.ButtonTestConnection");
        add(testConnectionButton, cc.xy(4, 14, "right, center"));
    }


    public void storeSettings()
    {
        workspaceSettings.put("JDBCConnectionConfigurationPanel.Username.Completion", usernameSet);

        driversSet.add(driverComboBox.getSelectedItem().toString());
        workspaceSettings.put("JDBCConnectionConfigurationPanel.Drivers.Completion", driversSet);
        workspaceSettings.put("JDBCConnectionConfigurationPanel.Drivers.LastSelectedValue", driverComboBox.getSelectedItem().toString());

        connectionStringSet.add(connectionStringComboBox.getSelectedItem().toString());
        workspaceSettings.put("JDBCConnectionConfigurationPanel.ConnectionString.Completion", connectionStringSet);
        workspaceSettings.put("JDBCConnectionConfigurationPanel.ConnectionString.LastSelectedValue", connectionStringComboBox.getSelectedItem().toString());
    }


    @NotNull
    public String[] getJars()
    {
        return jarListModel.getJars();
    }


    public void setJars(@NotNull String[] jars)
    {
        jarListModel.setJars(jars);
    }


    @NotNull
    public String getDriverClass()
    {
        return driverComboBox.getSelectedItem().toString();
    }


    public void setDriverClass(@NotNull String driverClass)
    {
        driverComboBox.setSelectedItem(driverClass);
    }


    @NotNull
    public String getConnectionString()
    {
        return connectionStringComboBox.getSelectedItem().toString();
    }


    public void setConnectionString(@NotNull String cs)
    {
        connectionStringComboBox.setSelectedItem(cs);
    }


    @NotNull
    public String getUsername()
    {
        return usernameTextField.getText();
    }


    public void setUsername(@NotNull String username)
    {
        usernameTextField.setText(username);
    }


    @NotNull
    public String getPassword()
    {
        return new String(passwordField.getPassword());
    }


    public void setPassword(@NotNull String password)
    {
        passwordField.setText(password);
    }


    public void addPropertyChangeListener2(@NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }


    public void removePropertyChangeListener2(@NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


    public void addPropertyChangeListener2(@NotNull String propertyName, @NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }


    public void removePropertyChangeListener2(@NotNull String propertyName, @NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }


    private static class JarListModel extends DefaultListModel
    {
        private JarListModel()
        {
        }


        @NotNull
        public String[] getJars()
        {
            String[] jars = new String[getSize()];
            for (int i = 0; i < getSize(); i++)
            {
                jars[i] = (String) getElementAt(i);
            }

            return jars;
        }


        public void setJars(@NotNull String[] jars)
        {
            removeAllElements();
            for (String jar : jars)
            {
                addElement(jar);
            }
        }
    }
}
