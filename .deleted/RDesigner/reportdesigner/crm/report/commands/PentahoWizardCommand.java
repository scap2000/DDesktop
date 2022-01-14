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
package org.pentaho.reportdesigner.crm.report.commands;

import java.awt.Cursor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.KeyStroke;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.pentaho.core.admin.datasources.DataSourceInfo;
import org.pentaho.core.admin.datasources.StandaloneSimpleJNDIDatasourceAdmin;
import org.pentaho.core.session.StandaloneSession;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.ui.IWizardListener;
import org.pentaho.jfreereport.wizard.ui.WizardPanel;
import org.pentaho.jfreereport.wizard.utility.report.ReportGenerationUtility;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.JNDISource;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.Query;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.reportimporter.JFreeReportImporter;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.util.logging.ILogger;
import org.xml.sax.InputSource;

/**
 * User: Martin Date: 20.02.2006 Time: 19:30:01
 */
@SuppressWarnings({"ALL"})//TODO temporary
public class PentahoWizardCommand extends AbstractCommand implements IWizardListener {
  @NotNull
  @NonNls
  private static final Logger LOG = Logger.getLogger(PentahoWizardCommand.class.getName());

  @Nullable
  private ReportWizard designer;
  @Nullable
  ReportDialog reportDialog;

  @NotNull
  private static Executor executor;
  
  Shell shell = null;

  public PentahoWizardCommand() {
    super("ReportGenerateWizardCommand");
    getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "ReportGenerateWizardCommand.Text"));
    getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "ReportGenerateWizardCommand.Description"));
    getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "ReportGenerateWizardCommand.Text"));
    getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "ReportGenerateWizardCommand.Text"));

    getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getOpenWizardIcon());

    getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "ReportGenerateWizardCommand.Accelerator")));
  }

  public void update(@NotNull
  CommandEvent event) {
    event.getPresentation().setEnabled(true);
  }

  public void execute(@NotNull
  CommandEvent event) {
    reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

    reportDialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));

    // Display display = new Display(); // display object to manage SWT lifecycle.
    shell = new Shell(Display.getDefault());

    shell.setLayout(new FillLayout());
    Composite shellContent = new Composite(shell, SWT.NONE);
    shellContent.setLayout(new FillLayout());

    try {
      designer = new ReportWizard(null, null, ".", ".", shellContent, ReportWizard.MODE_DIALOG);
      reportDialog.setEnabled(false);
    } catch (Throwable e) {
      UncaughtExcpetionsModel.getInstance().addException(e);
      return;
    }

    reportDialog.setEnabled(false);

    if (designer != null) {
      designer.wizardManager.addWizardListener(PentahoWizardCommand.this);
    }

    try {
      shell.setVisible(true);
      shell.moveAbove(null);
    } catch (Exception e) {
    }

    try {
      while (!shell.isDisposed()) {
        if (!shell.getDisplay().readAndDispatch()) {
          shell.getDisplay().sleep();
        }
      }
    } catch (Exception e) {
      if (LOG.isLoggable(Level.FINE))
        LOG.log(Level.FINE, "PentahoWizardCommand.run ", e);
    }

    // reportDialog.setVisible(true);
    reportDialog.setEnabled(true);
    // reportDialog.toFront();
    reportDialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

  }

  public boolean nextFired(@NotNull
  WizardPanel source) {
    // TODO Auto-generated method stub
    return false;
  }

  public void backFired(@NotNull
  WizardPanel source) {
    // TODO Auto-generated method stub

  }

  public void finishFired(@NotNull
  WizardPanel source) {

    ReportSpec reportSpec = designer.getReportSpec();
    source.getShell().dispose();

    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      String jfreeReportOutputPath = designer.jfreeReportOutputPath;

      if (reportSpec.getTemplateSrc() != null) {

        FileOutputStream outStream = null;
        try {
          // noinspection IOResourceOpenedButNotSafelyClosed
          outStream = new FileOutputStream(jfreeReportOutputPath + reportSpec.getReportName() + "_generated.xml");// NON-NLS
          ReportGenerationUtility.createJFreeReportXML(reportSpec, outStream, 0, 0, false, "", 0, 0); //$NON-NLS-1$
          File templateFile = new File(reportSpec.getTemplateSrc());
          ReportGenerationUtility.mergeTemplate(templateFile, new File(jfreeReportOutputPath + reportSpec.getReportName() + "_generated.xml"), baos); //$NON-NLS-1$
        } finally {
          IOUtil.closeStream(outStream);
        }
      } else {
        ReportGenerationUtility.createJFreeReportXML(reportSpec, baos, 0, 0, false, "", 0, 0); //$NON-NLS-1$
      }

      // ReportDialog newReportDialog = ReportDesignerWindows.getInstance().createNewReportDialog(null);

      final ReportGenerator generator = ReportGenerator.getInstance();
      JFreeReport jFreeReport = generator.parseReport(new InputSource(new ByteArrayInputStream(baos.toByteArray())), new File(".").toURI().toURL()); //$NON-NLS-1$

      JFreeReportImporter jFreeReportImporter = new JFreeReportImporter(jFreeReport);
      reportDialog.setReport(jFreeReportImporter.getReport(), true);

      DataSetsReportElement dataSetsReportElement = reportDialog.getReport().getDataSetsReportElement();
      MultiDataSetReportElement dataSet = new MultiDataSetReportElement();
      ArrayList<Query> queries = new ArrayList<Query>();
      queries.add(new Query(ReportDialogConstants.DEFAULT_DATA_FACTORY, reportSpec.getQuery()));
      dataSet.setQueries(queries);

      JNDISource selectedJNDISource = null;
      boolean found = false;
      for (int i = 0; i < dataSet.getJndiSources().size(); i++) {
        if (dataSet.getJndiSources().get(i).getJndiName().equalsIgnoreCase(reportSpec.getReportSpecChoice().getJndiSource())) {
          selectedJNDISource = dataSet.getJndiSources().get(i);
          found = true;
        }
      }

      if (!found) {
        if (reportSpec.getReportSpecChoice().getJndiSource() != null) {
          StandaloneSession session = new StandaloneSession("Datasource-Session"); //$NON-NLS-1$
          session.setLoggingLevel(ILogger.ERROR);
          StandaloneSimpleJNDIDatasourceAdmin dataSourceAdmin = new StandaloneSimpleJNDIDatasourceAdmin(ReportWizard.simpleJNDIPath, session);
          DataSourceInfo info = dataSourceAdmin.getDataSourceInfo(reportSpec.getReportSpecChoice().getJndiSource());

          JNDISource jndiSource = new JNDISource(info.getName(), info.getDriver(), info.getUrl(), info.getUserId(), info.getPassword());
          ArrayList<JNDISource> jndiSources = dataSet.getJndiSources();
          jndiSources.add(jndiSource);
          dataSet.setJndiSources(jndiSources);
          selectedJNDISource = jndiSource;
        }
      }

      ArrayList[] columnInfos = new ArrayList[1];
      columnInfos[0] = dataSet.fetchColumnInfos(selectedJNDISource != null ? MultiDataSetReportElement.ConnectionType.JNDI : MultiDataSetReportElement.ConnectionType.XQuery, selectedJNDISource, reportSpec.getReportSpecChoice()
          .getXqueryUrl(), reportSpec.getMqlQuery(), reportSpec.getIsMDX(), reportSpec.getMondrianCubeDefinitionPath(), reportSpec.getQuery());

      // noinspection unchecked
      dataSet.setColumnInfos(new ArrayList<ColumnInfo>(columnInfos[0]));

      if (selectedJNDISource == null) {
        dataSet.setXQueryDataFile(reportSpec.getReportSpecChoice().getXqueryUrl());
      } else {
        dataSet.setSelectedJNDIDataSource(selectedJNDISource);
      }
      dataSet.setConnectionType(selectedJNDISource != null ? MultiDataSetReportElement.ConnectionType.JNDI : MultiDataSetReportElement.ConnectionType.XQuery);
      dataSetsReportElement.addChild(dataSet);
      reportDialog.getReportElementModel().setSelection(Arrays.asList(dataSet));

      // Marshaller.marshal(reportSpec, stringWriter);
      // String xml = stringWriter.getBuffer().toString();
      // if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PentahoWizardCommand.run xml = " + xml);
    } catch (Exception e) {
      if (LOG.isLoggable(Level.FINE))
        LOG.log(Level.FINE, "PentahoWizardCommand.run ", e);
    }

  }

  public void publishFired(@NotNull
  WizardPanel source) {
  }

  public boolean cancelFired(@NotNull
  WizardPanel source) {
    source.getShell().dispose();

//    source.getShell().setVisible(false);
//    source.getShell().dispose();
//    shell.close();
//    shell.dispose();
//    shell.setVisible(false);
    return true;
  }

  public boolean previewFired(@NotNull
  WizardPanel source) {
    return true;
  }

}
