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

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.core.admin.datasources.DataSourceInfo;
import org.pentaho.core.admin.datasources.StandaloneSimpleJNDIDatasourceAdmin;
import org.pentaho.core.session.StandaloneSession;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement.ConnectionType;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.ExceptionUtils;
import org.pentaho.reportdesigner.lib.client.util.FontUtils;
import org.pentaho.util.logging.ILogger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin Date: 01.02.2006 Time: 11:48:14
 */
public class MultiDataSetReportElementConfigurator {
  @NonNls
  @NotNull
  private static final Logger LOG = Logger.getLogger(MultiDataSetReportElementConfigurator.class.getName());

  @NotNull
  public static final String JNDI_CARD = "JNDI";
  @NotNull
  public static final String XQUERY_CARD = "XQuery";

  public static final String MQL_CARD = "MQL";

  public static final String[] connectionTypes = new String[] { JNDI_CARD, XQUERY_CARD, MQL_CARD };

  public static boolean showStaticFactoryDataSetReportElementConfigurator(@NotNull
  ReportDialog parent, @NotNull
  MultiDataSetReportElement multiDataSetReportElement) {
    MultiDataSetReportElementConfigurator configurator = new MultiDataSetReportElementConfigurator(parent, multiDataSetReportElement);
    return configurator.ok;
  }

  private boolean ok;
  @NotNull
  private CenterPanelDialog centerPanelDialog;

  @NotNull
  private JPanel jndiPanel;
  @NotNull
  private JPanel xqueryPanel;

  @NotNull
  private JTextField xqueryTextField;
  @NotNull
  private JButton xqueryBrowseButton;

  @NotNull
  private JPanel mqlPanel;

  @NotNull
  private JTextField mqlTextField;
  @NotNull
  private JButton mqlBrowseButton;

  JComboBox connectionTypeCombo;

  private QueryPanel queryPanel;
  @NotNull
  private JButton mondrianBrowseButton;
  @NotNull
  private JCheckBox mondrianJCheckBox;

  @NotNull
  private JList jndiSourceList;
  @NotNull
  private MutableListModel jndiSourceListModel;
  @NotNull
  private JButton jndiAddButton;
  @NotNull
  private JButton jndiEditButton;
  @NotNull
  private JButton jndiRemoveButton;

  @NotNull
  private JTextField mondrianTextField;

  private MultiDataSetReportElementConfigurator(@NotNull
  final ReportDialog parent, @NotNull
  final MultiDataSetReportElement multiDataSetReportElement) {
    ok = false;

    final JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
    okButton.setEnabled(true);

    JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));

    centerPanelDialog = CenterPanelDialog.createDialog(parent, TranslationManager.getInstance().getTranslation("R", "StaticFactoryDataSetReportElementConfigurator.Title"), false);

    final CardLayout cardLayout = new CardLayout();
    final JPanel cardPanel = new JPanel(cardLayout);

    @NonNls
    CellConstraints cc = new CellConstraints();
    @NonNls
    FormLayout jndiPanelLayout = new FormLayout("4dlu, fill:default:grow, 4dlu, default, 4dlu, default, 4dlu, default, 4dlu", "0dlu, fill:100dlu, " + "4dlu, pref, " + "4dlu, pref, " + "4dlu, pref, " + "4dlu:grow");
    jndiPanelLayout.setColumnGroups(new int[][] { { 4, 6, 8 } });
    jndiPanel = new JPanel(jndiPanelLayout);

    jndiSourceListModel = new MutableListModel();
    jndiSourceList = new JList(jndiSourceListModel);
    jndiPanel.add(new JScrollPane(jndiSourceList), cc.xyw(2, 2, 7, "fill, fill"));
    jndiAddButton = ComponentFactory.createButton("R", "MultiDataSetReportElementConfigurator.AddButton");

    jndiPanel.add(jndiAddButton, cc.xy(4, 4));
    jndiEditButton = ComponentFactory.createButton("R", "MultiDataSetReportElementConfigurator.EditButton");
    jndiEditButton.setEnabled(false);
    jndiPanel.add(jndiEditButton, cc.xy(6, 4));
    jndiRemoveButton = ComponentFactory.createButton("R", "MultiDataSetReportElementConfigurator.RemoveButton");
    jndiRemoveButton.setEnabled(false);
    jndiPanel.add(jndiRemoveButton, cc.xy(8, 4));
    mondrianJCheckBox = ComponentFactory.createCheckBox("R", "MultiDataSetReportElementConfigurator.MondrianCheckBox");
    jndiPanel.add(mondrianJCheckBox, cc.xyw(2, 6, 5));
    mondrianTextField = new JTextField();
    mondrianTextField.setEnabled(false);
    jndiPanel.add(mondrianTextField, cc.xyw(2, 8, 5));
    mondrianBrowseButton = ComponentFactory.createButton("R", "MultiDataSetReportElementConfigurator.BrowseButton");
    mondrianBrowseButton.setEnabled(false);

    jndiPanel.add(mondrianBrowseButton, cc.xy(8, 8));

    cardPanel.add(JNDI_CARD, jndiPanel);

    @NonNls
    FormLayout xqueryPanelLayout = new FormLayout("4dlu, fill:default:grow, 4dlu, pref, 4dlu", "0dlu, pref, 0dlu");
    xqueryPanel = new JPanel(xqueryPanelLayout);
    xqueryTextField = new JTextField();
    xqueryPanel.add(xqueryTextField, cc.xy(2, 2));
    xqueryBrowseButton = ComponentFactory.createButton("R", "MultiDataSetReportElementConfigurator.BrowseButton");
    xqueryPanel.add(xqueryBrowseButton, cc.xy(4, 2));

    cardPanel.add(XQUERY_CARD, xqueryPanel);

    @NonNls
    FormLayout mqlPanelLayout = new FormLayout("4dlu, fill:default:grow, 4dlu, pref, 4dlu", "0dlu, pref, 0dlu");
    mqlPanel = new JPanel(mqlPanelLayout);
    mqlTextField = new JTextField();
    mqlPanel.add(mqlTextField, cc.xy(2, 2));
    mqlBrowseButton = ComponentFactory.createButton("R", "MQLDataSetReportElementConfigurator.BrowseButton");
    mqlPanel.add(mqlBrowseButton, cc.xy(4, 2));

    cardPanel.add(MQL_CARD, mqlPanel);

    cardLayout.show(cardPanel, JNDI_CARD);

    @NonNls
    FormLayout centerPanelLayout = new FormLayout("4dlu, 250dlu, 4dlu, pref, 4dlu, fill:default:grow, 4dlu", "4dlu, pref, " + "4dlu, pref, " + "10dlu, pref, " + "10dlu, pref, " + "4dlu, pref, " + "4dlu, pref, " + "4dlu, pref, "
        + "4dlu, default:grow, " + "4dlu, pref, " + "4dlu, pref, " + "4dlu");

    final JPanel centerPanel = new JPanel(centerPanelLayout);

    JLabel mainTitleLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.MainTitleLabel"));
    mainTitleLabel.setFont(FontUtils.getDerivedFont(mainTitleLabel.getFont(), Font.BOLD, mainTitleLabel.getFont().getSize() + 2));

    JLabel descriptionLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.DescriptionLabel"));

    JLabel connectionInformationLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.ConnectionInformationLabel"));
    connectionInformationLabel.setFont(FontUtils.getDerivedFont(connectionInformationLabel.getFont(), Font.BOLD, connectionInformationLabel.getFont().getSize() + 2));

    JLabel queryDetailsLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.QueryDetailsLabel"));
    queryDetailsLabel.setFont(FontUtils.getDerivedFont(queryDetailsLabel.getFont(), Font.BOLD, queryDetailsLabel.getFont().getSize() + 2));

    JLabel connectionTypeLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.ConnectionTypeLabel"));
    JLabel queryStringLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.QueryStringLabel"));

    JPanel connectionTypeHelperPanel = new JPanel();
    connectionTypeHelperPanel.setPreferredSize(new Dimension(300, 300));

    queryPanel = new QueryPanel(centerPanelDialog, jndiSourceList, multiDataSetReportElement.getQueries());

    connectionTypeCombo = new JComboBox(connectionTypes);

    centerPanel.add(mainTitleLabel, cc.xyw(2, 2, 5));
    centerPanel.add(descriptionLabel, cc.xyw(2, 4, 5));
    centerPanel.add(new JSeparator(), cc.xyw(2, 6, 5));
    centerPanel.add(connectionInformationLabel, cc.xy(2, 8));
    centerPanel.add(new JSeparator(), cc.xy(2, 10));
    centerPanel.add(connectionTypeLabel, cc.xy(2, 12));
    JPanel comboButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
    comboButtonPanel.add(connectionTypeCombo);
    centerPanel.add(comboButtonPanel, cc.xy(2, 14));
    centerPanel.add(cardPanel, cc.xy(2, 16, "fill, fill"));

    centerPanel.add(queryDetailsLabel, cc.xy(6, 8));
    centerPanel.add(new JSeparator(), cc.xy(6, 10));
    centerPanel.add(queryStringLabel, cc.xy(6, 12));
    centerPanel.add(queryPanel, cc.xywh(6, 14, 1, 3, "fill, fill"));

    centerPanel.add(new JSeparator(JSeparator.VERTICAL), cc.xywh(4, 8, 1, 13));

    connectionTypeCombo.addActionListener(new ActionListener() {
      public void actionPerformed(@NotNull
      ActionEvent e) {
        if (connectionTypeCombo.getSelectedItem().equals(JNDI_CARD)) {
          cardLayout.show(cardPanel, JNDI_CARD);
          queryPanel.setConnectionType(JNDI_CARD);
          queryPanel.setMultiQueryCapable(!mondrianJCheckBox.isSelected());
          updateComponents();
        } else if (connectionTypeCombo.getSelectedItem().equals(XQUERY_CARD)) {
          cardLayout.show(cardPanel, XQUERY_CARD);
          queryPanel.setConnectionType(XQUERY_CARD);
          queryPanel.setMultiQueryCapable(false);
          updateComponents();
        } else if (connectionTypeCombo.getSelectedItem().equals(MQL_CARD)) {
          cardLayout.show(cardPanel, MQL_CARD);
          queryPanel.setConnectionType(MQL_CARD);
          queryPanel.setMultiQueryCapable(true);
          queryPanel.setXmiPath(mqlTextField.getText());
          updateComponents();
        }
      }
    });

    // restore data from dataset
    if (multiDataSetReportElement.getSelectedJNDIDataSource() != null) {
      // mondrian or sql
      cardLayout.show(cardPanel, JNDI_CARD);
      queryPanel.setConnectionType(JNDI_CARD);
      boolean isMDX = !StringUtils.isEmpty(multiDataSetReportElement.getMondrianCubeDefinitionFile());
      mondrianJCheckBox.setSelected(isMDX);
      mondrianTextField.setEnabled(isMDX);
      queryPanel.getDesignerButton().setEnabled(!isMDX);
      if (isMDX) {
        mondrianTextField.setText(multiDataSetReportElement.getMondrianCubeDefinitionFile());
      }
      connectionTypeCombo.setSelectedItem(JNDI_CARD);
    } else if (!StringUtils.isEmpty(multiDataSetReportElement.getXQueryDataFile())) {
      // xquery
      cardLayout.show(cardPanel, XQUERY_CARD);
      queryPanel.setConnectionType(XQUERY_CARD);
      queryPanel.getDesignerButton().setEnabled(false);
      xqueryTextField.setText(multiDataSetReportElement.getXQueryDataFile());
      connectionTypeCombo.setSelectedItem(XQUERY_CARD);
    } else if (!StringUtils.isEmpty(multiDataSetReportElement.getXmiDefinitionFile())) {
      // MQL
      System.out.println("mql");
      cardLayout.show(cardPanel, MQL_CARD);
      queryPanel.setConnectionType(MQL_CARD);
      queryPanel.getDesignerButton().setEnabled(true);
      mqlTextField.setText(multiDataSetReportElement.getXmiDefinitionFile());
      connectionTypeCombo.setSelectedItem(MQL_CARD);
    }

    ArrayList<JNDISource> jndiSources = multiDataSetReportElement.getJndiSources();
    for (JNDISource jndiSource : jndiSources) {
      jndiSourceListModel.addElement(jndiSource);
    }

    JNDISource jndiDataSource = multiDataSetReportElement.getSelectedJNDIDataSource();
    if (jndiDataSource != null) {
      jndiSourceList.setSelectedValue(jndiDataSource, true);
    }


    jndiSourceList.setCellRenderer(new DefaultListCellRenderer() {
      @NotNull
      public Component getListCellRendererComponent(@NotNull
      JList list, @Nullable
      Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
          String jndiName = ((JNDISource) value).getJndiName();
          if (!"".equals(jndiName)) {
            listCellRendererComponent.setText(jndiName);
          } else {
            listCellRendererComponent.setText(" ");
          }
        }
        return listCellRendererComponent;
      }
    });

    jndiSourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jndiSourceList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(@NotNull
      ListSelectionEvent e) {
        updateComponents();
      }
    });

    jndiAddButton.addActionListener(new ActionListener() {
      public void actionPerformed(@NotNull
      ActionEvent e) {
        JNDISource jndiSource = JNDIChooser.showChooser(centerPanelDialog, TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.AddJNDIConnectionDialogTitle"), new JNDISource());
        if (jndiSource != null) {
          jndiSourceListModel.addElement(jndiSource);
          jndiSourceList.setSelectedIndex(jndiSourceListModel.getSize() - 1);
          // update simple-jndi file
          StandaloneSession session = new StandaloneSession("Datasource-Session"); //$NON-NLS-1$
          session.setLoggingLevel(ILogger.ERROR);
          StandaloneSimpleJNDIDatasourceAdmin dataSourceAdmin = new StandaloneSimpleJNDIDatasourceAdmin(ReportWizard.simpleJNDIPath, session);
          DataSourceInfo dsi = new DataSourceInfo(jndiSource.getJndiName(), jndiSource.getJndiName(), "javax.sql.DataSource");
          dsi.setDriver(jndiSource.getDriverClass());
          dsi.setUrl(jndiSource.getConnectionString());
          dsi.setUserId(jndiSource.getUsername());
          dsi.setPassword(jndiSource.getPassword());
          dataSourceAdmin.saveDataSource(dsi, true);
        }
      }
    });

    jndiEditButton.addActionListener(new ActionListener() {
      public void actionPerformed(@NotNull
      ActionEvent e) {
        JNDISource existingJNDISource = (JNDISource) jndiSourceList.getSelectedValue();
        JNDISource jndiSource = JNDIChooser.showChooser(centerPanelDialog, TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.EditJNDIConnectionDialogTitle"), existingJNDISource);
        if (jndiSource != null) {
          existingJNDISource.setJndiName(jndiSource.getJndiName());
          existingJNDISource.setDriverClass(jndiSource.getDriverClass());
          existingJNDISource.setConnectionString(jndiSource.getConnectionString());
          existingJNDISource.setUsername(jndiSource.getUsername());
          existingJNDISource.setPassword(jndiSource.getPassword());

          jndiSourceListModel.fireContentsChanged(jndiSourceList.getSelectedIndex());

          // update simple-jndi file
          StandaloneSession session = new StandaloneSession("Datasource-Session"); //$NON-NLS-1$
          session.setLoggingLevel(ILogger.ERROR);
          StandaloneSimpleJNDIDatasourceAdmin dataSourceAdmin = new StandaloneSimpleJNDIDatasourceAdmin(ReportWizard.simpleJNDIPath, session);
          DataSourceInfo dsi = (DataSourceInfo) dataSourceAdmin.getDataSourceInfo(jndiSource.getJndiName());
          dsi.setDriver(jndiSource.getDriverClass());
          dsi.setUrl(jndiSource.getConnectionString());
          dsi.setUserId(jndiSource.getUsername());
          dsi.setPassword(jndiSource.getPassword());
          dataSourceAdmin.saveDataSource(dsi, true);
        }
      }
    });

    jndiRemoveButton.addActionListener(new ActionListener() {
      public void actionPerformed(@NotNull
      ActionEvent e) {
        String jndiName = ((JNDISource) jndiSourceList.getSelectedValue()).getJndiName();
        System.out.println("jndiName=" + jndiName);
        StandaloneSession session = new StandaloneSession("Datasource-Session"); //$NON-NLS-1$
        session.setLoggingLevel(ILogger.ERROR);
        StandaloneSimpleJNDIDatasourceAdmin dataSourceAdmin = new StandaloneSimpleJNDIDatasourceAdmin(ReportWizard.simpleJNDIPath, session);
        dataSourceAdmin.deleteDataSource(jndiName);
        System.out.println("trying to remove jndiName: " + jndiName);

        int selectedIndex = jndiSourceList.getSelectedIndex();
        if (selectedIndex != -1) {
          jndiSourceListModel.remove(selectedIndex);
        }
      }
    });

    mondrianJCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(@NotNull
      ActionEvent e) {
        queryPanel.setMultiQueryCapable(!mondrianJCheckBox.isSelected());
        updateComponents();
      }
    });

    mondrianBrowseButton.addActionListener(new ActionListener() {
      public void actionPerformed(@NotNull
      ActionEvent e) {
        File initiallySelectedFile;
        if (!"".equals(mondrianTextField.getText().trim())) {
          initiallySelectedFile = new File(mondrianTextField.getText());
        } else {
          initiallySelectedFile = WorkspaceSettings.getInstance().getFile("MultiDataSetReportElementConfigurator.MondrianFile");
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(initiallySelectedFile);
        int success = fileChooser.showOpenDialog(centerPanelDialog);
        if (success == JFileChooser.APPROVE_OPTION) {
          mondrianTextField.setText(fileChooser.getSelectedFile().toString());
          WorkspaceSettings.getInstance().put("MultiDataSetReportElementConfigurator.MondrianFile", fileChooser.getSelectedFile().toString());
        }
      }
    });

    xqueryBrowseButton.addActionListener(new ActionListener() {
      public void actionPerformed(@NotNull
      ActionEvent e) {
        File initiallySelectedFile;
        if (!"".equals(xqueryTextField.getText().trim())) {
          initiallySelectedFile = new File(xqueryTextField.getText());
        } else {
          initiallySelectedFile = WorkspaceSettings.getInstance().getFile("MultiDataSetReportElementConfigurator.XQueryFile");
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(initiallySelectedFile);
        int success = fileChooser.showOpenDialog(centerPanelDialog);
        if (success == JFileChooser.APPROVE_OPTION) {
          xqueryTextField.setText(fileChooser.getSelectedFile().toString());
          WorkspaceSettings.getInstance().put("MultiDataSetReportElementConfigurator.XQueryFile", fileChooser.getSelectedFile().toString());
        }
      }
    });

    mqlBrowseButton.addActionListener(new ActionListener() {
      public void actionPerformed(@NotNull
      ActionEvent e) {
        File initiallySelectedFile;
        if (!"".equals(mqlTextField.getText().trim())) {
          initiallySelectedFile = new File(mqlTextField.getText());
        } else {
          initiallySelectedFile = WorkspaceSettings.getInstance().getFile("MQLDataSetReportElementConfigurator.XMIFile");
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(initiallySelectedFile);
        int success = fileChooser.showOpenDialog(centerPanelDialog);
        if (success == JFileChooser.APPROVE_OPTION) {
          mqlTextField.setText(fileChooser.getSelectedFile().toString());
          queryPanel.setXmiPath(mqlTextField.getText());
          WorkspaceSettings.getInstance().put("MQLDataSetReportElementConfigurator.XMIFile", fileChooser.getSelectedFile().toString());
        }
      }
    });

    updateComponents();

    centerPanelDialog.setCenterPanel(centerPanel);
    centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(@NotNull
      ActionEvent e) {
        ok = false;
        centerPanelDialog.dispose();
      }
    });

    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(@NotNull
      ActionEvent e) {
        
        try {
          String queryName = queryPanel.getQueries().get(0).getQueryName();
          DocumentHelper.parseText("<" + queryName + "/>");
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(centerPanelDialog, TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.InvalidQueryName.Title"), TranslationManager.getInstance().getTranslation("R", "Error.Title"), JOptionPane.OK_OPTION);
          return;
        }           
        
        final ArrayList[] columnInfos = new ArrayList[1];

        final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(centerPanelDialog, TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.ProgressDialog.Title"), "");

        Thread t = new Thread() {
          public void run() {
            ConnectionType connectionType = ConnectionType.JNDI;
            try {
              if (connectionTypeCombo.getSelectedItem().equals(JNDI_CARD)) {
                connectionType = ConnectionType.JNDI;
              } else if (connectionTypeCombo.getSelectedItem().equals(XQUERY_CARD)) {
                connectionType = ConnectionType.XQuery;
              } else if (connectionTypeCombo.getSelectedItem().equals(MQL_CARD)) {
                connectionType = ConnectionType.MQL;
              }

              columnInfos[0] = multiDataSetReportElement.fetchColumnInfos(connectionType, (JNDISource) jndiSourceList.getSelectedValue(), xqueryTextField.getText(), queryPanel.getXmiPath(), mondrianJCheckBox.isSelected(), mondrianTextField
                  .getText(), queryPanel.getQueries().get(0).getQuery());

              final ConnectionType ct = connectionType;

              EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                  progressDialog.dispose();

                  parent.getUndo().startTransaction(UndoConstants.CONNECTION_SETTINGS);
                  // noinspection unchecked
                  multiDataSetReportElement.setColumnInfos(new ArrayList<ColumnInfo>(columnInfos[0]));
                  multiDataSetReportElement.setConnectionType(ct);
                  multiDataSetReportElement.setXmiDefinitionFile(mqlTextField.getText());
                  multiDataSetReportElement.setXQueryDataFile(xqueryTextField.getText());

                  ArrayList<JNDISource> jndiSources = new ArrayList<JNDISource>();
                  for (int i = 0; i < jndiSourceListModel.getSize(); i++) {
                    jndiSources.add((JNDISource) jndiSourceListModel.getElementAt(i));
                  }
                  multiDataSetReportElement.setJndiSources(jndiSources);
                  multiDataSetReportElement.setSelectedJNDIDataSource((JNDISource) jndiSourceList.getSelectedValue());
                  multiDataSetReportElement.setUseMondrianCubeDefinition(mondrianJCheckBox.isSelected());
                  multiDataSetReportElement.setMondrianCubeDefinitionFile(mondrianTextField.getText());
                  multiDataSetReportElement.setQueries(queryPanel.getQueries());

                  parent.getUndo().endTransaction();

                  parent.getWorkspaceSettings().storeDialogBounds(centerPanelDialog, "StaticFactoryDataSetReportElementConfigurator");
                  ok = true;
                  centerPanelDialog.dispose();

                    Runnable callback = multiDataSetReportElement.getCallback();
                    if (callback!=null)
                    {
                        callback.run();
                    }
                }
              });
            } catch (final Exception e1) {
              if (LOG.isLoggable(Level.FINE))
                LOG.log(Level.FINE, "MultiDataSetReportElementConfigurator.run ", e1);
              ExceptionUtils.disposeDialogInEDT(progressDialog);
              final ConnectionType ct = connectionType;
              EventQueue.invokeLater(new Runnable() {
                public void run() {
                  int option = JOptionPane.showConfirmDialog(centerPanelDialog, TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementConfigurator.CouldNotFetchData", e1.getMessage()), TranslationManager
                      .getInstance().getTranslation("R", "Error.Title"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                  if (option == JOptionPane.YES_OPTION) {
                    parent.getUndo().startTransaction(UndoConstants.CONNECTION_SETTINGS);
                    // noinspection unchecked
                    // don't update columninfos use the old ones
                    multiDataSetReportElement.setConnectionType(ct);
                    multiDataSetReportElement.setXmiDefinitionFile(mqlTextField.getText());
                    multiDataSetReportElement.setXQueryDataFile(xqueryTextField.getText());

                    ArrayList<JNDISource> jndiSources = new ArrayList<JNDISource>();
                    for (int i = 0; i < jndiSourceListModel.getSize(); i++) {
                      jndiSources.add((JNDISource) jndiSourceListModel.getElementAt(i));
                    }
                    multiDataSetReportElement.setJndiSources(jndiSources);
                    multiDataSetReportElement.setSelectedJNDIDataSource((JNDISource) jndiSourceList.getSelectedValue());
                    multiDataSetReportElement.setUseMondrianCubeDefinition(mondrianJCheckBox.isSelected());
                    multiDataSetReportElement.setMondrianCubeDefinitionFile(mondrianTextField.getText());
                    multiDataSetReportElement.setQueries(queryPanel.getQueries());

                    parent.getUndo().endTransaction();

                    parent.getWorkspaceSettings().storeDialogBounds(centerPanelDialog, "StaticFactoryDataSetReportElementConfigurator");
                    ok = true;
                    centerPanelDialog.dispose();
                  }
                    Runnable callback = multiDataSetReportElement.getCallback();
                    if (callback!=null)
                    {
                        callback.run();
                    }
                }
              });
            }
          }
        };
        t.setDaemon(true);
        t.setPriority(Thread.NORM_PRIORITY - 1);
        t.start();

        if (t.isAlive()) {
          progressDialog.setVisible(true);
        } else {
          progressDialog.dispose();
        }
      }
    });

    if (!parent.getWorkspaceSettings().restoreDialogBounds(centerPanelDialog, "StaticFactoryDataSetReportElementConfigurator")) {
      centerPanelDialog.pack();
      centerPanelDialog.setSize(800, 700);
    }
    WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);

    centerPanelDialog.setVisible(true);
  }

  private void updateComponents() {
    jndiEditButton.setEnabled(jndiSourceList.getSelectedIndex() != -1);
    jndiRemoveButton.setEnabled(jndiSourceList.getSelectedIndex() != -1);

    mondrianTextField.setEnabled(mondrianJCheckBox.isSelected());
    mondrianBrowseButton.setEnabled(mondrianJCheckBox.isSelected());

    queryPanel.getDesignerButton()
        .setEnabled(connectionTypeCombo.getSelectedItem().equals(MQL_CARD) || (connectionTypeCombo.getSelectedItem().equals(JNDI_CARD) && jndiSourceList.getSelectedIndex() != -1 && !mondrianJCheckBox.isSelected()));
  }

  private static class MutableListModel extends DefaultListModel {
    private MutableListModel() {
    }

    public void fireContentsChanged(int index) {
      super.fireContentsChanged(this, index, index);
    }
  }

}
