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
package org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.connection.SQLUtil;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.ClasspathSearcher;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCClassLoader;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 31.07.2006
 * Time: 16:22:30
 */
public class JNDIChooser
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(JNDIChooser.class.getName());

    @NotNull
    private static final String[] drivers = new String[]{"org.hsqldb.jdbcDriver",
                                                         "oracle.jdbc.driver.OracleDriver",
                                                         "com.ibm.db2.jcc.DB2Driver",
                                                         "com.mysql.jdbc.Driver",
                                                         "com.ncr.teradata.TeraDriver"};

    @NotNull
    private static final String[] connectStrings = new String[]{"jdbc:hsqldb:hsql://localhost/sampledata",
                                                                "jdbc:oracle:thin:@localhost:1521:orcl",
                                                                "jdbc:db2:DATABASE_NAME",
                                                                "jdbc:mysql://localhost:PORT/DATABASE_NAME",
                                                                "jdbc:teradata://teradata.server.ip.swap"};


    @Nullable
    public static JNDISource showChooser(@NotNull CenterPanelDialog centerPanelDialog, @NotNull String title, @NotNull JNDISource jndiSource)
    {
        JNDIChooser jndiChooser = new JNDIChooser(centerPanelDialog, title, jndiSource);
        if (jndiChooser.ok)
        {
            return jndiChooser.result;
        }
        return null;
    }


    private boolean ok;
    @Nullable
    private JNDISource result;


    public JNDIChooser(@NotNull CenterPanelDialog parent, @NotNull String title, @NotNull JNDISource jndiSource)
    {
        final CenterPanelDialog centerPanelDialog = new CenterPanelDialog(parent, title, true);
        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, default, 4dlu, fill:10dlu:grow, 0dlu",
                                               "0dlu, " +
                                               "pref, 4dlu, " +
                                               "pref, 4dlu, " +
                                               "pref, 4dlu, " +
                                               "pref, 4dlu, " +
                                               "pref, 4dlu, " +
                                               "pref, 4dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();

        JPanel centerPanel = new JPanel(formLayout);

        final JTextField jndiNameTextField = new JTextField(jndiSource.getJndiName());
        JLabel jndiNameLabel = ComponentFactory.createLabel("R", "JNDIChooser.JNDINameLabel", jndiNameTextField);

        LinkedHashSet<String> driverClasses = new LinkedHashSet<String>(Arrays.asList(drivers));
        driverClasses.addAll(Arrays.asList(ClasspathSearcher.getInstance().getDrivers()));
        final JComboBox driverComboBox = new JComboBox(driverClasses.toArray(new String[driverClasses.size()]));
        driverComboBox.setEditable(true);
        driverComboBox.setSelectedItem(jndiSource.getDriverClass());
        JLabel driverLabel = ComponentFactory.createLabel("R", "JNDIChooser.DriverLabel", driverComboBox);

        final JComboBox connectionStringComboBox = new JComboBox(connectStrings);
        connectionStringComboBox.setEditable(true);
        connectionStringComboBox.setSelectedItem(jndiSource.getConnectionString());
        JLabel connectionStringLabel = ComponentFactory.createLabel("R", "JNDIChooser.ConnectStringLabel", connectionStringComboBox);

        final JTextField usernameTextField = new JTextField(jndiSource.getUsername());
        JLabel usernameLabel = ComponentFactory.createLabel("R", "JNDIChooser.UsernameLabel", usernameTextField);

        final JPasswordField passwordField = new JPasswordField(jndiSource.getPassword());
        JLabel passwordLabel = ComponentFactory.createLabel("R", "JNDIChooser.PasswordLabel", passwordField);

        JButton testButton = ComponentFactory.createButton("R", "JNDIChooser.TestButton");

        centerPanel.add(jndiNameLabel, cc.xy(2, 2));
        centerPanel.add(jndiNameTextField, cc.xy(4, 2));

        centerPanel.add(driverLabel, cc.xy(2, 4));
        centerPanel.add(driverComboBox, cc.xy(4, 4));

        centerPanel.add(connectionStringLabel, cc.xy(2, 6));
        centerPanel.add(connectionStringComboBox, cc.xy(4, 6));

        centerPanel.add(usernameLabel, cc.xy(2, 8));
        centerPanel.add(usernameTextField, cc.xy(4, 8));

        centerPanel.add(passwordLabel, cc.xy(2, 10));
        centerPanel.add(passwordField, cc.xy(4, 10));

        centerPanel.add(testButton, cc.xyw(2, 12, 3, "center, center"));


        testButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Connection connection = null;
                try
                {
                    connection = JDBCClassLoader.getConnection(driverComboBox.getSelectedItem() != null ? driverComboBox.getSelectedItem().toString() : "",
                                                               connectionStringComboBox.getSelectedItem() != null ? connectionStringComboBox.getSelectedItem().toString() : "",
                                                               usernameTextField.getText(),
                                                               new String(passwordField.getPassword()));

                    JOptionPane.showMessageDialog(centerPanelDialog, TranslationManager.getInstance().getTranslation("R", "JNDIChooser.ConnectionSuccessful"), TranslationManager.getInstance().getTranslation("R", "JNDIChooser.Success"), JOptionPane.INFORMATION_MESSAGE);
                }
                catch (Throwable t)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JNDIChooser.actionPerformed ", t);
                    JOptionPane.showMessageDialog(centerPanelDialog, TranslationManager.getInstance().getTranslation("R", "JNDIChooser.Error.Message", t.getMessage()), TranslationManager.getInstance().getTranslation("R", "Error.Title"), JOptionPane.ERROR_MESSAGE);
                }
                finally
                {
                    SQLUtil.closeConnection(connection);
                }
            }
        });


        centerPanelDialog.setCenterPanel(centerPanel);

        JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));
        centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                ok = true;
                String jndiName = jndiNameTextField.getText();
                String driverClass = driverComboBox.getSelectedItem() != null ? driverComboBox.getSelectedItem().toString() : "";
                String connectionString = connectionStringComboBox.getSelectedItem() != null ? connectionStringComboBox.getSelectedItem().toString() : "";
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());
                result = new JNDISource(jndiName, driverClass, connectionString, username, password);
                centerPanelDialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                ok = false;
                result = null;
                centerPanelDialog.dispose();
            }
        });

        centerPanelDialog.pack();
        GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 400, 200);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);
    }
}
