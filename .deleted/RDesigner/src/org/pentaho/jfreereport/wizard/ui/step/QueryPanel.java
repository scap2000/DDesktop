/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created Feb 01, 2006
 * @author Michael D'Amour
 */
package org.pentaho.jfreereport.wizard.ui.step;

import java.io.File;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.commons.mql.ui.mqldesigner.CWMStartup;
import org.pentaho.commons.mql.ui.mqldesigner.MQLQueryBuilderDialog;
import org.pentaho.core.admin.datasources.DataSourceInfo;
import org.pentaho.core.connection.IPentahoConnection;
import org.pentaho.jfreereport.castormodel.reportspec.Field;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpecChoice;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.WizardManager;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.IDirtyListener;
import org.pentaho.jfreereport.wizard.ui.WizardPanel;
import org.pentaho.jfreereport.wizard.ui.dialog.JDBCPanel;
import org.pentaho.jfreereport.wizard.ui.dialog.PentahoSWTMessageBox;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTLine;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;
import org.pentaho.jfreereport.wizard.utility.connection.ConnectionUtility;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.sqleonardo.swt.IQueryCallback;
import org.pentaho.sqleonardo.swt.SQLeonardo;

public class QueryPanel extends WizardPanel implements IDirtyListener, ModifyListener, IQueryCallback {
  ReportWizard reportDesigner = null;

  Combo connectionTypeCombo = null;

  List jndiList = null;

  Text queryText = null;

  Text driverName = null;

  PentahoSWTButton kettleBrowseButton = null;

  Text xqueryName = null;

  Text kettleName = null;

  PentahoSWTButton xqueryBrowseButton = null;

  Composite kettleGroup = null;

  Composite xqueryGroup = null;

  Composite jndiGroup = null;

  PentahoSWTButton xmiBrowseButton = null;

  Text xmiFileNameText = null;

  Composite mqlGroup = null;

  String xQueryFilePath = null;

  String kettleFilePath = null;

  Composite connectionContentGroup = null;

  PentahoSWTButton addButton = null;

  PentahoSWTButton editButton = null;

  PentahoSWTButton removeButton = null;

  Button mondrianConnectionButton = null;

  PentahoSWTButton mondrianBrowseButton = null;

  Text mondrianDefinitionPath = null;

  PentahoSWTButton designButton = null;

  Combo kettleStepCombo = null;

  PentahoSWTButton kettlePreviewButton = null;

  Display display = Display.getCurrent();

  /**
   * DOCUMENT ME!
   * 
   * @param parent
   * @param style
   * @param manager
   * @param reportDesigner
   */
  public QueryPanel(Composite parent, int style, WizardManager manager, ReportWizard reportDesigner) {
    super(parent, style, manager);
    this.reportDesigner = reportDesigner;
    initialize();
  }

  /**
   * 
   */
  public void initialize() {
    addDirtyListener(this);
    Composite c = getMainPanel();
    c.setLayout(new GridLayout(1, true));
    Composite connectionGroup = new Composite(c, SWT.NONE);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 1;
    connectionGroup.setLayoutData(gridData);
    connectionGroup.setLayout(new GridLayout(3, false));
    Label connectionLabel = new Label(connectionGroup, SWT.NONE);
    connectionLabel.setText(Messages.getString("QueryPanel.45")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 3;
    connectionLabel.setLayoutData(gridData);
    connectionLabel.setFont(labelFont);
    Label stepText = new Label(connectionGroup, SWT.WRAP);
    stepText.setText(Messages.getString("QueryPanel.46")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 3;
    stepText.setLayoutData(gridData);
    stepText.setBackground(ReportWizard.background);
    // stepText.setFont(textFont);
    SWTLine connectionLine = new SWTLine(connectionGroup, SWT.NONE);
    connectionLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.heightHint = 30;
    gridData.horizontalSpan = 3;
    connectionLine.setLayoutData(gridData);
    Label connectionInfoLabel = new Label(connectionGroup, SWT.NONE);
    connectionInfoLabel.setText(Messages.getString("QueryPanel.50")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    gridData.widthHint = 400;
    connectionInfoLabel.setLayoutData(gridData);
    connectionInfoLabel.setFont(labelFont);
    SWTLine vLine = new SWTLine(connectionGroup, SWT.NONE);
    vLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    vLine.setHorizontal(false);
    gridData = new GridData(SWT.CENTER, SWT.FILL, false, true);
    gridData.widthHint = 15;
    gridData.verticalSpan = 5;
    vLine.setLayoutData(gridData);
    Label queryDetailsLabel = new Label(connectionGroup, SWT.NONE);
    queryDetailsLabel.setText(Messages.getString("QueryPanel.51")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    queryDetailsLabel.setLayoutData(gridData);
    queryDetailsLabel.setFont(labelFont);
    SWTLine connectionInfoLine = new SWTLine(connectionGroup, SWT.NONE);
    connectionInfoLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, false, false);
    gridData.heightHint = 10;
    connectionInfoLine.setLayoutData(gridData);
    SWTLine queryDetailsLine = new SWTLine(connectionGroup, SWT.NONE);
    queryDetailsLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.heightHint = 10;
    queryDetailsLine.setLayoutData(gridData);
    Label connectionTypeLabel = new Label(connectionGroup, SWT.NONE);
    connectionTypeLabel.setText(Messages.getString("QueryPanel.0")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, false, false);
    connectionTypeLabel.setLayoutData(gridData);
    Label specifyQueryLabel = new Label(connectionGroup, SWT.NONE);
    specifyQueryLabel.setText(Messages.getString("QueryPanel.52")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    specifyQueryLabel.setLayoutData(gridData);
    Composite connectionTypeGroup = new Composite(connectionGroup, SWT.NONE);
    GridLayout gridLayout = new GridLayout(3, false);
    gridLayout.marginHeight = 0;
    connectionTypeGroup.setLayout(gridLayout);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    connectionTypeGroup.setLayoutData(gridData);
    connectionTypeCombo = new Combo(connectionTypeGroup, SWT.READ_ONLY);
    connectionTypeCombo.setItems(new String[] { "JNDI", "XQuery", /* "Kettle Transformation (KTR)", */
    "Metadata Query (MQL)" });
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    connectionTypeCombo.setLayoutData(gridData);
    connectionTypeCombo.select(0);
    connectionTypeCombo.addSelectionListener(this);
    Composite queryComposite = new Composite(connectionGroup, SWT.NONE);
    queryComposite.setLayout(new GridLayout(1, false));
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.verticalSpan = 2;
    queryComposite.setLayoutData(gridData);
    queryText = new Text(queryComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    queryText.setLayoutData(gridData);
    queryText.addModifyListener(this);
    connectionContentGroup = new Composite(connectionGroup, SWT.NONE);
    gridLayout = new GridLayout(1, false);
    gridLayout.marginHeight = 0;
    connectionContentGroup.setLayout(gridLayout);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
    connectionContentGroup.setLayoutData(gridData);
    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    designButton = new PentahoSWTButton(queryComposite, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("QueryPanel.55")); //$NON-NLS-1$
    designButton.setLayoutData(gridData);
    designButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        final String originalQuery = QueryPanel.this.getQuery();
        String query = "";
        String item = connectionTypeCombo.getItem(connectionTypeCombo.getSelectionIndex());
        if (item.equalsIgnoreCase("JNDI")) {
          DataSourceInfo dsi = ReportWizard.dataSourceAdmin.getDataSourceInfo(getJNDIName());

          Connection conn = ConnectionUtility.initializeDataConnection(dsi.getDriver(), dsi.getUrl(), dsi.getUserId(), dsi.getPassword());
          Shell sqlBuilderShell = SQLeonardo.createQueryBuilderShell(QueryPanel.this, conn, originalQuery);
          sqlBuilderShell.setVisible(true);

        } else if (item.equalsIgnoreCase("Metadata Query (MQL)")) {
          try {
            CWMStartup.loadMetadata(reportDesigner.getReportSpec().getXmiPath(), "samples");
            Shell shell = new Shell();
            MQLQueryBuilderDialog dialog = new MQLQueryBuilderDialog(shell);
            if (queryText.getText() != null && !queryText.getText().equals("")) {
              MQLQuery mqlQuery = new MQLQuery(queryText.getText(), "en_US", new CwmSchemaFactory()); //$NON-NLS-1$
              dialog = new MQLQueryBuilderDialog(shell, mqlQuery);
            }
            if (dialog.open() == Window.OK) {
              // dialog = new MQLQueryBuilderDialog(shell, dialog.getMqlQuery());
              MQLQuery mqlQuery = dialog.getMqlQuery();
              query = mqlQuery.getXML();
              QueryPanel.this.setQuery(query);
              QueryPanel.this.updateState();
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    });
    SWTUtility.setBackground(c, ReportWizard.background);
    updateState();
  }

  public void selectXQuery() {
    SWTUtility.removeChildren(connectionContentGroup);
    xqueryGroup = new Composite(connectionContentGroup, SWT.NONE);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    xqueryGroup.setLayoutData(gridData);
    xqueryGroup.setLayout(new GridLayout(2, false));
    xqueryName = new Text(xqueryGroup, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    xqueryName.setLayoutData(gridData);
    xqueryName.addModifyListener(this);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    xqueryBrowseButton = new PentahoSWTButton(xqueryGroup, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("QueryPanel.35")); //$NON-NLS-1$
    xqueryBrowseButton.setLayoutData(gridData);
    xqueryBrowseButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        openXQuery();
        wizardManager.update();
      }
    });
    SWTUtility.setBackground(connectionContentGroup, ReportWizard.background);
    connectionContentGroup.redraw();
    connectionContentGroup.layout();
    connectionContentGroup.update();
    xqueryGroup.redraw();
    xqueryGroup.layout();
    xqueryGroup.update();
  }

  public void selectKettle() {
    SWTUtility.removeChildren(connectionContentGroup);
    // kettleGroup = new Composite(connectionContentGroup, SWT.NONE);
    // GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    // kettleGroup.setLayoutData(gridData);
    // kettleGroup.setLayout(new GridLayout(2, false));
    // kettleName = new Text(kettleGroup, SWT.BORDER | SWT.SINGLE);
    // gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    // kettleName.setLayoutData(gridData);
    // kettleName.addModifyListener(this);
    // gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    // kettleBrowseButton = new PentahoSWTButton(kettleGroup, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages
    // .getString("QueryPanel.35")); //$NON-NLS-1$
    // kettleBrowseButton.setLayoutData(gridData);
    // kettleBrowseButton.addSelectionListener(new SelectionAdapter() {
    // public void widgetSelected(SelectionEvent e) {
    // openKettle();
    // wizardManager.update();
    // }
    // });
    // Composite kettleStepGroup = new Composite(connectionContentGroup, SWT.NONE);
    // gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    // kettleStepGroup.setLayoutData(gridData);
    // kettleStepGroup.setLayout(new GridLayout(2, false));
    // Label kettleStepLabel = new Label(kettleStepGroup, SWT.NONE);
    // kettleStepLabel.setText("Kettle Step");
    // gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    // kettleStepLabel.setLayoutData(gridData);
    // kettleStepCombo = new Combo(kettleStepGroup, SWT.READ_ONLY);
    // gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    // kettleStepCombo.setLayoutData(gridData);
    // kettleStepCombo.addSelectionListener(this);
    // gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    // gridData.horizontalSpan = 2;
    // kettlePreviewButton = new PentahoSWTButton(kettleStepGroup, SWT.NONE, gridData, PentahoSWTButton.NORMAL,
    // "Kettle Transformation Preview");
    // kettlePreviewButton.setLayoutData(gridData);
    // kettlePreviewButton.addSelectionListener(this);
    SWTUtility.setBackground(connectionContentGroup, ReportWizard.background);
    // String[] steps = getKettleSteps(kettleName.getText());
    // if (steps != null) {
    // kettleStepCombo.setItems(steps);
    // }
    // kettleGroup.redraw();
    // kettleGroup.layout();
    // kettleGroup.update();
    connectionContentGroup.redraw();
    connectionContentGroup.layout();
    connectionContentGroup.update();
  }

  public static Image getKettleStepImage(String kettleFileStr, Display display) {
    // try {
    // File kettleFile = new File(kettleFileStr);
    // FileInputStream fis = new FileInputStream(kettleFile);
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // byte[] buffer = new byte[16536];
    // int numRead = 0;
    // while (numRead < kettleFile.length()) {
    // numRead = fis.read(buffer);
    // baos.write(buffer, 0, numRead);
    // }
    // String jobXmlStr = baos.toString();
    // jobXmlStr = jobXmlStr.replaceAll(
    // "\\$\\{pentaho.solutionpath\\}", PentahoSystem.getApplicationContext().getFileOutputPath("")); //$NON-NLS-1$
    // //$NON-NLS-2$
    // jobXmlStr = jobXmlStr.replaceAll(
    // "\\%\\%pentaho.solutionpath\\%\\%", PentahoSystem.getApplicationContext().getFileOutputPath("")); //$NON-NLS-1$
    // //$NON-NLS-2$
    // // org.w3c.dom.Document doc = XmlW3CHelper.getDomFromString(jobXmlStr);
    // // create a tranformation from the document
    // // if (!Props.isInitialized()) {
    // // Props.init(display, Props.TYPE_PROPERTIES_SPOON); // things to remember... }
    // // }
    // // TransMeta transMeta = new TransMeta(doc.getFirstChild());
    // // Point area = transMeta.getMaximum();
    // // TransPainter transPainter = new TransPainter(transMeta, area, null, null, null, null, null);
    // // Image image = transPainter.getTransformationImage(display); return image;
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    return null;
  }

  public static String[] getKettleSteps(String kettleFileStr) {
    // try {
    // File kettleFile = new File(kettleFileStr);
    // FileInputStream fis = new FileInputStream(kettleFile);
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // byte[] buffer = new byte[16536];
    // int numRead = 0;
    // while (numRead < kettleFile.length()) {
    // numRead = fis.read(buffer);
    // baos.write(buffer, 0, numRead);
    // }
    // String jobXmlStr = baos.toString();
    // jobXmlStr = jobXmlStr.replaceAll("\\$\\{pentaho.solutionpath\\}", PentahoSystem.getApplicationContext()
    // .getFileOutputPath(""));
    // //$NON-NLS-1$ //$NON-NLS-2$
    // jobXmlStr = jobXmlStr.replaceAll("\\%\\%pentaho.solutionpath\\%\\%", PentahoSystem.getApplicationContext()
    // .getFileOutputPath(""));
    // //$NON-NLS-1$ //$NON-NLS-2$
    // org.w3c.dom.Document doc = XmlW3CHelper.getDomFromString(jobXmlStr);
    // // create a tranformation from the document
    // TransMeta transMeta = new TransMeta(doc.getFirstChild());
    // StepMeta[] steps = transMeta.getStepsArray();
    // String[] stepStrings = new String[steps.length];
    // for (int i = 0; i < steps.length; i++) {
    // stepStrings[i] = steps[i].getName();
    // }
    // return stepStrings;
    // } catch (Exception e) {
    // }
    return null;
  }

  public void selectJNDI() {
    if (connectionContentGroup == null) {
      return;
    }
    SWTUtility.removeChildren(connectionContentGroup);
    jndiGroup = new Composite(connectionContentGroup, SWT.NONE);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    jndiGroup.setLayoutData(gridData);
    jndiGroup.setLayout(new GridLayout(4, false));
    jndiList = new List(jndiGroup, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
    gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
    gridData.heightHint = 100;
    gridData.horizontalSpan = 4;
    jndiList.setLayoutData(gridData);
    jndiList.addSelectionListener(this);
    jndiList.setItems(getJNDINames());
    jndiList.select(0);
    Label connectionInfoLabel = new Label(jndiGroup, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    connectionInfoLabel.setLayoutData(gridData);
    gridData = new GridData(SWT.RIGHT, SWT.NONE, false, false);
    addButton = new PentahoSWTButton(jndiGroup, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("QueryPanel.37")); //$NON-NLS-1$
    addButton.setLayoutData(gridData);
    addButton.addSelectionListener(this);
    gridData = new GridData(SWT.RIGHT, SWT.NONE, false, false);
    editButton = new PentahoSWTButton(jndiGroup, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("QueryPanel.56")); //$NON-NLS-1$
    editButton.setLayoutData(gridData);
    editButton.addSelectionListener(this);
    gridData = new GridData(SWT.RIGHT, SWT.NONE, false, false);
    removeButton = new PentahoSWTButton(jndiGroup, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("QueryPanel.53")); //$NON-NLS-1$
    removeButton.setLayoutData(gridData);
    removeButton.addSelectionListener(this);
    Composite mondrianComposite = new Composite(jndiGroup, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 4;
    mondrianComposite.setLayoutData(gridData);
    mondrianComposite.setLayout(new GridLayout(2, false));
    // mondrian specific:
    mondrianConnectionButton = new Button(mondrianComposite, SWT.CHECK);
    mondrianConnectionButton.setText(Messages.getString("QueryPanel.54")); //$NON-NLS-1$
    mondrianConnectionButton.addSelectionListener(this);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    gridData.horizontalSpan = 2;
    mondrianConnectionButton.setLayoutData(gridData);
    mondrianConnectionButton.setSelection(reportDesigner.getReportSpec().getIsMDX());
    // cube definition path
    mondrianDefinitionPath = new Text(mondrianComposite, SWT.SINGLE | SWT.BORDER);
    mondrianDefinitionPath.addModifyListener(this);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    mondrianDefinitionPath.setLayoutData(gridData);
    if (reportDesigner.getReportSpec().getMondrianCubeDefinitionPath() != null) {
      mondrianDefinitionPath.setText(reportDesigner.getReportSpec().getMondrianCubeDefinitionPath());
    }
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    mondrianBrowseButton = new PentahoSWTButton(mondrianComposite, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("QueryPanel.49")); //$NON-NLS-1$
    mondrianBrowseButton.setLayoutData(gridData);
    mondrianBrowseButton.addSelectionListener(this);
    mondrianDefinitionPath.setEnabled(reportDesigner.getReportSpec().getIsMDX());
    mondrianBrowseButton.setEnabled(reportDesigner.getReportSpec().getIsMDX());
    // end mondrian specific
    SWTUtility.setBackground(connectionContentGroup, ReportWizard.background);
    connectionContentGroup.redraw();
    connectionContentGroup.layout();
    connectionContentGroup.update();
    jndiGroup.redraw();
    jndiGroup.layout();
    jndiGroup.update();
  }

  public void selectMQL() {
    if (connectionContentGroup == null) {
      return;
    }
    SWTUtility.removeChildren(connectionContentGroup);
    mqlGroup = new Composite(connectionContentGroup, SWT.NONE);
    mqlGroup.setLayout(new GridLayout(2, false));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    mqlGroup.setLayoutData(gridData);
    Label selectXMILabel = new Label(mqlGroup, SWT.NONE);
    selectXMILabel.setText("Select XMI Document:");
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    gridData.horizontalSpan = 2;
    selectXMILabel.setLayoutData(gridData);

    xmiFileNameText = new Text(mqlGroup, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    gridData.widthHint = 120;
    xmiFileNameText.setLayoutData(gridData);
    xmiFileNameText.addModifyListener(this);

    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    xmiBrowseButton = new PentahoSWTButton(mqlGroup, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("QueryPanel.35")); //$NON-NLS-1$
    xmiBrowseButton.setLayoutData(gridData);
    xmiBrowseButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
        File f = new File(fileDialog.open());
        xmiFileNameText.setText(f.getAbsolutePath());
        reportDesigner.getReportSpec().setXmiPath(f.getAbsolutePath());
        wizardManager.update();
      }
    });
    SWTUtility.setBackground(connectionContentGroup, ReportWizard.background);
    connectionContentGroup.redraw();
    connectionContentGroup.layout();
    connectionContentGroup.update();
    mqlGroup.redraw();
    mqlGroup.layout();
    mqlGroup.update();
  }

  public void widgetSelected(SelectionEvent e) {
    super.widgetSelected(e);
    if (e.widget == connectionTypeCombo) {
      String item = connectionTypeCombo.getItem(connectionTypeCombo.getSelectionIndex());
      if (item.equalsIgnoreCase("XQuery")) {
        selectXQuery();
        designButton.setEnabled(false);
        queryText.setEnabled(true);
      } else if (item.equalsIgnoreCase("JNDI")) {
        selectJNDI();
        designButton.setEnabled(!mondrianConnectionButton.getSelection());
        queryText.setEnabled(true);
      } else if (item.equalsIgnoreCase("Kettle Transformation (KTR)")) {
        selectKettle();
        designButton.setEnabled(false);
        queryText.setEnabled(false);
      } else if (item.equalsIgnoreCase("Metadata Query (MQL)")) {
        designButton.setEnabled(true);
        queryText.setEnabled(true);
        selectMQL();
      }
    }
    if (e.widget == kettlePreviewButton) {
      // Shell shell = new Shell(getDisplay());
      // Image image = getKettleStepImage(kettleName.getText(),
      // getDisplay());
      // ShowImageDialog sid = new ShowImageDialog(shell, image,
      // image.getImageData().height, image.getImageData().width);
      // sid.open();
    } else if (e.widget == kettleStepCombo) {
      reportDesigner.getReportSpec().setKettleStep(kettleStepCombo.getText());
      ConnectionUtility.queryColumnMap.clear();
      ConnectionUtility.queryResultSetMap.clear();
    } else if (e.widget == mondrianConnectionButton) {
      reportDesigner.getReportSpec().setIsMDX(mondrianConnectionButton.getSelection());
      mondrianDefinitionPath.setEnabled(reportDesigner.getReportSpec().getIsMDX());
      mondrianBrowseButton.setEnabled(reportDesigner.getReportSpec().getIsMDX());
      designButton.setEnabled(!mondrianConnectionButton.getSelection());
    } else if (e.widget == mondrianBrowseButton) {
      FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
      File f = new File(fileDialog.open());
      mondrianDefinitionPath.setText(f.getAbsolutePath());
      reportDesigner.getReportSpec().setMondrianCubeDefinitionPath(f.getAbsolutePath());
    } else if (e.widget == removeButton) {
      
      if (jndiList.getSelection() == null || jndiList.getSelection().length == 0) {
        PentahoSWTMessageBox mb = new PentahoSWTMessageBox(Messages.getString("QueryPanel.43"), Messages.getString("QueryPanel.57"), 320, 50); //$NON-NLS-1$ //$NON-NLS-2$
        mb.open();
        return;
      }
      
      
      int firstIndex = 0;
      if (jndiList.getSelectionCount() > 0) {
        firstIndex = jndiList.getSelectionIndices()[0];
      }
      for (int i = 0; i < jndiList.getSelectionCount(); i++) {
        String selection = jndiList.getSelection()[i];
        ReportWizard.dataSourceAdmin.deleteDataSource(selection);
      }
      jndiList.setItems(getJNDINames());
      try {
        jndiList.select(firstIndex);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else if (e.widget == addButton) {
      try {
        JDBCPanel jdbcPanel = new JDBCPanel(320, 360, Messages.getString("QueryPanel.38"), reportDesigner.rdwRoot); //$NON-NLS-1$
        if (jdbcPanel.open()) {
          DataSourceInfo dsi = jdbcPanel.getDataSourceInfo();
          if (dsi.getName() != null && !dsi.getName().equals("")) { //$NON-NLS-1$
            ReportWizard.dataSourceAdmin.saveDataSource(dsi, true);
            jndiList.setItems(getJNDINames());
          } else {
            // give error message
            PentahoSWTMessageBox mb = new PentahoSWTMessageBox(Messages.getString("QueryPanel.43"), Messages.getString("QueryPanel.44"), 320, 50); //$NON-NLS-1$ //$NON-NLS-2$
            mb.open();
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else if (e.widget == editButton) {
      try {
        if (jndiList.getSelection() == null || jndiList.getSelection().length == 0) {
          PentahoSWTMessageBox mb = new PentahoSWTMessageBox(Messages.getString("QueryPanel.43"), Messages.getString("QueryPanel.57"), 320, 50); //$NON-NLS-1$ //$NON-NLS-2$
          mb.open();
          return;
        }
        String selection = jndiList.getSelection()[0];
        JDBCPanel jdbcPanel = new JDBCPanel(320, 360, Messages.getString("QueryPanel.38"), reportDesigner.rdwRoot, ReportWizard.dataSourceAdmin.getDataSourceInfo(selection)); //$NON-NLS-1$
        if (jdbcPanel.open()) {
          DataSourceInfo dsi = jdbcPanel.getDataSourceInfo();
          if (dsi.getName() != null && !dsi.getName().equals("")) { //$NON-NLS-1$
            ReportWizard.dataSourceAdmin.deleteDataSource(selection);
            ReportWizard.dataSourceAdmin.saveDataSource(dsi, true);
            jndiList.setItems(getJNDINames());
          } else {
            // give error message
            PentahoSWTMessageBox mb = new PentahoSWTMessageBox(Messages.getString("QueryPanel.43"), Messages.getString("QueryPanel.44"), 320, 50); //$NON-NLS-1$ //$NON-NLS-2$
            mb.open();
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    SWTUtility.setBackground(connectionContentGroup, ReportWizard.background);
    connectionContentGroup.layout();
    wizardManager.update();
  }

  public String[] getJNDINames() {
    Map dsMap = ReportWizard.dataSourceAdmin.listDataSources();
    Iterator keySetIterator = dsMap.keySet().iterator();
    String[] jndiNames = new String[dsMap.size()];
    int i = 0;
    while (keySetIterator.hasNext()) {
      String key = (String) keySetIterator.next();
      DataSourceInfo dsi = (DataSourceInfo) dsMap.get(key);
      jndiNames[i] = dsi.getName();
      i++;
    }
    return jndiNames;
  }

  public boolean testConnection() {
    boolean testResult = true;
    String item = connectionTypeCombo.getItem(connectionTypeCombo.getSelectionIndex());
    if (item.equalsIgnoreCase("JNDI") && mondrianConnectionButton.getSelection()) {
      testResult = ConnectionUtility.testMDXConnection(getJNDIName(), reportDesigner.getReportSpec().getMondrianCubeDefinitionPath());
    } else if (item.equalsIgnoreCase("JNDI")) {
      testResult = ConnectionUtility.testConnection(getJNDIName(), false);
    } else if (item.equalsIgnoreCase("XQuery")) {
      testResult = ConnectionUtility.testConnection(xqueryName.getText(), true);
    } else if (item.equalsIgnoreCase("Kettle Transformation (KTR)")) {
      // need to test kettle somehow
    }
    return testResult;
  }

  public void openXQuery() {
    FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
    fd.setFilterExtensions(SWTUtility.XML_FILTER_STRINGS);
    xQueryFilePath = fd.open();
    if (xQueryFilePath != null && !xQueryFilePath.equals("")) { //$NON-NLS-1$
      xqueryName.setText(xQueryFilePath);
    }
    ConnectionUtility.queryColumnMap.clear();
    ConnectionUtility.queryResultSetMap.clear();
    stateChanged = true;
    updateState();
  }

  public void openKettle() {
    FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
    fd.setFilterExtensions(SWTUtility.KETTLE_TRANSFORMATION_FILE);
    kettleFilePath = fd.open();
    if (kettleFilePath != null && !kettleFilePath.equals("")) { //$NON-NLS-1$
      kettleName.setText(kettleFilePath);
      kettleStepCombo.setItems(getKettleSteps(kettleName.getText()));
    }
    ConnectionUtility.queryColumnMap.clear();
    ConnectionUtility.queryResultSetMap.clear();
    stateChanged = true;
    updateState();
  }

  /**
   * @return $objectType$
   */
  public boolean isContinueAllowed() {
    boolean continueAllowed = false;
    String item = connectionTypeCombo.getItem(connectionTypeCombo.getSelectionIndex());
    if (item.equalsIgnoreCase("JNDI")) {
      if ((getJNDIName() != null) && !"".equals(getJNDIName()) && (queryText.getText() != null) && !"".equals(queryText.getText())) { //$NON-NLS-1$ //$NON-NLS-2$
        if (mondrianConnectionButton.getSelection() && !"".equals(mondrianDefinitionPath.getText())) { //$NON-NLS-1$
          continueAllowed = true;
        } else if (!mondrianConnectionButton.getSelection()) {
          continueAllowed = true;
        }
      }
    } else if (item.equalsIgnoreCase("XQuery")) {
      if (xqueryName != null && !xqueryName.isDisposed() && xqueryName.getText() != null && !xqueryName.getText().equals("") && (queryText.getText() != null) && !"".equals(queryText.getText())) { //$NON-NLS-1$ //$NON-NLS-2$
        continueAllowed = true;
      }
    } else if (item.equalsIgnoreCase("Kettle Transformation (KTR)") && kettleName != null && kettleName.getText() != null && !kettleName.getText().equals("")) {
      continueAllowed = true;
    } else if (item.equalsIgnoreCase("Metadata Query (MQL)")) {
      if (xmiFileNameText != null && !xmiFileNameText.isDisposed() && !"".equalsIgnoreCase(xmiFileNameText.getText())) {
        continueAllowed = true;
      }
    }
    return continueAllowed;
  }

  /**
   * @param source
   */
  public boolean previewFired(WizardPanel source) {
    if (source == this) {
      return nextFired(source);
    }
    return true;
  }

  /**
   * @param source
   */
  public boolean nextFired(WizardPanel source) {
    // we should filter these to our 'next'
    super.nextFired(source);
    boolean status = true;
    if ((this == source) && stateChanged) {
      ReportSpec reportSpec = reportDesigner.getReportSpec();
      String item = connectionTypeCombo.getItem(connectionTypeCombo.getSelectionIndex());
      if (!testConnection()) {
        if (item.equalsIgnoreCase("JNDI")) {
          DataSourceInfo info = ReportWizard.dataSourceAdmin.getDataSourceInfo(getJNDIName());
          PentahoSWTMessageBox mb = new PentahoSWTMessageBox(Messages.getString("QueryPanel.31"), ReportWizard.dataSourceAdmin.testDataSource(info), 360, 200); //$NON-NLS-1$
          mb.open();
        }
      } else {
        // connection passed, what about query?
        if (item.equalsIgnoreCase("JNDI")) {
          if (mondrianConnectionButton.getSelection()) {
            reportDesigner.setPentahoConnection(ConnectionUtility.getConnection(getJNDIName(), reportSpec.getMondrianCubeDefinitionPath(), IPentahoConnection.MDX_DATASOURCE));
          } else if (item.equalsIgnoreCase("JNDI")) {
            reportDesigner.setPentahoConnection(ConnectionUtility.getConnection(getJNDIName()));
          }
          reportSpec.setQuery(queryText.getText());
          ReportSpecChoice rsc = new ReportSpecChoice();
          rsc.setJndiSource(getJNDIName());
          reportSpec.setReportSpecChoice(rsc);
          IPentahoConnection connection = reportDesigner.getPentahoConnection();
          Object[] cols = null;
          try {
            cols = ConnectionUtility.getColumns(connection, queryText.getText(), null);
          } catch (Exception ex) {
            MessageBox mb = new MessageBox(getShell(), SWT.OK);
            mb.setMessage(ex.getMessage());
            mb.setText("Error");
            mb.open();
            return false;
          }
          if (cols == null) {
            PentahoSWTMessageBox mb = new PentahoSWTMessageBox(Messages.getString("QueryPanel.31"), Messages.getString("QueryPanel.40"), 360, 140); //$NON-NLS-1$ //$NON-NLS-2$
            mb.open();
            return false;
          }
        } else if (item.equalsIgnoreCase("XQuery")) {
          reportDesigner.setPentahoConnection(ConnectionUtility.getConnection(xQueryFilePath, null, IPentahoConnection.XML_DATASOURCE));
          ReportSpecChoice rsc = new ReportSpecChoice();
          rsc.setXqueryUrl(xQueryFilePath);
          reportSpec.setReportSpecChoice(rsc);
          reportSpec.setQuery(queryText.getText());
          IPentahoConnection connection = reportDesigner.getPentahoConnection();
          Object[] cols = null;
          try {
            cols = ConnectionUtility.getColumns(connection, queryText.getText(), reportSpec.getReportSpecChoice().getXqueryUrl());
          } catch (Exception ex) {
            MessageBox mb = new MessageBox(getShell(), SWT.OK);
            mb.setMessage(ex.getMessage());
            mb.setText("Error");
            mb.open();
            return false;
          }
          if (cols == null) {
            PentahoSWTMessageBox mb = new PentahoSWTMessageBox(Messages.getString("QueryPanel.31"), Messages.getString("QueryPanel.40"), 360, 140); //$NON-NLS-1$ //$NON-NLS-2$
            mb.open();
            return false;
          }
        } else if (item.equalsIgnoreCase("Kettle Transformation (KTR)")) {
          reportDesigner.setPentahoConnection(null);
          ReportSpecChoice rsc = new ReportSpecChoice();
          rsc.setKettleUrl(kettleFilePath);
          reportSpec.setReportSpecChoice(rsc);
          reportSpec.setQuery(kettleFilePath);
        } else if (item.equalsIgnoreCase("Metadata Query (MQL)")) {
          reportSpec.setIsMQL(true);
          reportSpec.setMqlQuery(queryText.getText());
          reportDesigner.setPentahoConnection(ConnectionUtility.getMQLConnection(reportSpec));
        }
      }
      status = updateDesignerState();
      if (!status) {
        PentahoSWTMessageBox mb = new PentahoSWTMessageBox(Messages.getString("QueryPanel.31"), Messages.getString("QueryPanel.40"), 360, 140); //$NON-NLS-1$ //$NON-NLS-2$
        mb.open();
      } else {
        stateChanged = false;
      }
    }
    return status;
  }

  /**
   * @param source
   */
  public void finishFired(WizardPanel source) {
    // we should filter these to our 'next'
    super.finishFired(source);
    if ((this == source) && stateChanged) {
      stateChanged = false;
      updateDesignerState();
    }
  }

  /**
   * 
   */
  public boolean updateDesignerState() {
    ReportSpec reportSpec = reportDesigner.getReportSpec();
    Object[] cols = null;
    String item = connectionTypeCombo.getItem(connectionTypeCombo.getSelectionIndex());
    if (item.equalsIgnoreCase("JNDI")) {
      if (mondrianConnectionButton.getSelection()) {
        reportDesigner.setPentahoConnection(ConnectionUtility.getConnection(getJNDIName(), reportSpec.getMondrianCubeDefinitionPath(), IPentahoConnection.MDX_DATASOURCE));
        designButton.setEnabled(false);
      } else {
        reportDesigner.setPentahoConnection(ConnectionUtility.getConnection(getJNDIName()));
        designButton.setEnabled(true);
      }
      reportSpec.setQuery(queryText.getText());
      ReportSpecChoice rsc = new ReportSpecChoice();
      rsc.setJndiSource(getJNDIName());
      reportSpec.setReportSpecChoice(rsc);
      IPentahoConnection connection = reportDesigner.getPentahoConnection();
      try {
        cols = ConnectionUtility.getColumns(connection, reportSpec.getQuery(), reportSpec.getReportSpecChoice().getXqueryUrl());
      } catch (Exception ex) {
        MessageBox mb = new MessageBox(getShell(), SWT.OK);
        mb.setMessage(ex.getMessage());
        mb.setText("Error");
        mb.open();
      }
      if (cols == null) {
        return false;
      }
    } else if (item.equalsIgnoreCase("XQuery")) {
      reportDesigner.setPentahoConnection(ConnectionUtility.getConnection(xQueryFilePath, null, IPentahoConnection.XML_DATASOURCE));
      ReportSpecChoice rsc = new ReportSpecChoice();
      rsc.setXqueryUrl(xQueryFilePath);
      reportSpec.setReportSpecChoice(rsc);
      reportSpec.setQuery(queryText.getText());
      designButton.setEnabled(false);
      IPentahoConnection connection = reportDesigner.getPentahoConnection();
      try {
        cols = ConnectionUtility.getColumns(connection, reportSpec.getQuery(), reportSpec.getReportSpecChoice().getXqueryUrl());
      } catch (Exception ex) {
        MessageBox mb = new MessageBox(getShell(), SWT.OK);
        mb.setMessage(ex.getMessage());
        mb.setText("Error");
        mb.open();
      }
      if (cols == null) {
        return false;
      }
    } else if (item.equalsIgnoreCase("Metadata Query (MQL)")) {
    } else if (item.equalsIgnoreCase("Kettle Transformation (KTR)")) {
      ReportSpecChoice rsc = new ReportSpecChoice();
      rsc.setKettleUrl(kettleFilePath);
      reportSpec.setReportSpecChoice(rsc);
      reportSpec.setQuery(kettleFilePath);
      designButton.setEnabled(false);
      cols = ConnectionUtility.getKettleColumns(reportSpec, kettleFilePath);
      if (cols == null) {
        return false;
      }
    }
    // if columns from the reportSpec perfectly match the query, prompt to
    // ask if to overwrite
    boolean perfectMatch = reportSpec.getFieldCount() > 0;
    for (int i = 0; i < reportSpec.getFieldCount(); i++) {
      Field f = reportSpec.getField(i);
      boolean found = false;
      for (int j = 0; j < cols.length; j++) {
        if (f.getName().equalsIgnoreCase(cols[j].toString())) {
          found = true;
        }
      }
      if (!found) {
        perfectMatch = false;
        break;
      }
    }
    if (!perfectMatch) {
      // this should attempt to reconcile
      reportDesigner.reconcileReportSpec(reportDesigner.getReportSpec());
    }
    return true;
  }

  /**
   * @param dirty
   */
  public void dirtyFired(boolean dirty) {
    // if we care
    // updateState();
  }

  /**
   * @return $objectType$
   */
  public boolean isJNDI() {
    return connectionTypeCombo.getItem(connectionTypeCombo.getSelectionIndex()).equalsIgnoreCase("JNDI");
  }

  /**
   * @return $objectType$
   */
  public String getJNDIName() {
    if (jndiList == null || jndiList.isDisposed() || jndiList.getSelection() == null || jndiList.getSelection().length == 0) {
      return null;
    }
    return jndiList.getSelection()[0];
  }

  /**
   * @return $objectType$
   */
  public String getQuery() {
    return queryText.getText();
  }

  public void initWizardPanel(ReportSpec reportSpec) {
    if (reportSpec.getIsMQL()) {
      queryText.setText(reportSpec.getMqlQuery());
    } else if (reportSpec.getQuery() != null) {
      queryText.setText(reportSpec.getQuery());
    } else {
      // queryText.setText(""); //$NON-NLS-1$
    }
    if (reportSpec.getReportSpecChoice() != null) {
      String jndiSource = reportSpec.getReportSpecChoice().getJndiSource();
      xQueryFilePath = reportSpec.getReportSpecChoice().getXqueryUrl();
      kettleFilePath = reportSpec.getReportSpecChoice().getKettleUrl();
      if (jndiSource != null && !jndiSource.equals("")) { //$NON-NLS-1$
        selectJNDI();
        // do jndi
        connectionTypeCombo.select(0);
        // select: jndiSource
        jndiList.setSelection(jndiList.indexOf(jndiSource));
      } else if (kettleFilePath != null && !kettleFilePath.equals("")) {
        selectKettle();
        kettleName.setText(kettleFilePath);
        // connectionTypeCombo.select(2);
      } else {
        selectXQuery();
        xqueryName.setText(xQueryFilePath);
        connectionTypeCombo.select(1);
      }
    } else if (reportSpec.getIsMQL()) {
      selectMQL();
      xmiFileNameText.setText(reportSpec.getXmiPath());
      connectionTypeCombo.select(2);
    } else {
      // do jndi by default
      selectJNDI();
      connectionTypeCombo.select(0);
    }
    stateChanged = false;
    updateState();
  }

  public void setQuery(final String query) {
    Runnable r = new Runnable() {
      public void run() {
        queryText.setText(query);
        stateChanged = true;
        updateState();
        reportDesigner.getReportSpec().setQuery(query);
      }
    };
    display.asyncExec(r);
  }

  public void modifyText(ModifyEvent arg0) {
    stateChanged = true;
    dirty = true;
    fireDirtyEvent();
    wizardManager.update();
  }

  public void queryGenerated(String query) {
    if (reportDesigner.removeCrlfFromQuery) {
      query = query.trim().replace('\r', ' ').replace('\n', ' ');
    }
    setQuery(query);
  }
}
