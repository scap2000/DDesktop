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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.pentaho.jfreereport.castormodel.reportspec.Chart;
import org.pentaho.jfreereport.castormodel.reportspec.Field;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.castormodel.reportspec.types.ChartType;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.WizardManager;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.IDirtyListener;
import org.pentaho.jfreereport.wizard.ui.WizardPanel;
import org.pentaho.jfreereport.wizard.ui.dialog.ChartSettingsDialog;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTLine;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportSpecUtility;

public class ReportSetupPanel extends WizardPanel implements IDirtyListener, MouseListener, MouseTrackListener, ModifyListener {
  /**
   * 
   */
  ReportWizard reportDesigner = null;
  Button bandingCheckbox = null;
  SWTButton rowBandingButton = null;
  Button useUnderliningCheckbox = null;
  Button useDoubleUnderliningCheckbox = null;
  Button useColumnHeaderBackgroundCheckbox = null;
  SWTButton columnHeaderBackgroundButton = null;
  Button useHorizontalGridlinesCheckbox = null;
  SWTButton horizontalGridlinesColorButton = null;
  Button useVerticalGridlinesCheckbox = null;
  SWTButton verticalGridlinesColorButton = null;
  Spinner columnHeaderHeightSpinner = null;
  Button calculateGrandTotalsCheckbox = null;
  Label groupHeaderSampleLabel = null;
  PentahoSWTButton groupHeaderFontButton = null;
  Label groupFooterSampleLabel = null;
  PentahoSWTButton groupFooterFontButton = null;
  Label columnHeaderFontSampleLabel = null;
  PentahoSWTButton columnHeaderFontButton = null;
  PentahoSWTButton itemsFontButton = null;
  Label itemsFontSampleLabel = null;
  Text grandTotalText = null;
  Text groupTotalText = null;
  Combo grandTotalHorizontalAlignmentCombo = null;
  Button rowBandingInitialStateCheckbox = null;
  Spinner columnHeaderGapSpinner = null;
  Spinner columnHeaderTopGapSpinner = null;
  Spinner horizontalOffsetSpinner = null;
  Text includeText = null;
  PentahoSWTButton includeBrowseButton = null;
  Button useChartCheckBox = null;
  PentahoSWTButton configureChartButton = null;
  Button generateReportLevelColumnHeadersButton = null;
  Button useGroupFooterBackgroundColorCheckbox = null;
  SWTButton groupFooterBackgroundColorButton = null;
  Composite fontPanel = null;

  
  /**
   * Creates a new PageSetupPanel object.
   * 
   * @param parent
   * @param style
   * @param manager
   * @param reportDesigner
   */
  public ReportSetupPanel(Composite parent, int style, WizardManager manager, ReportWizard reportDesigner) {
    super(parent, style, manager);
    this.reportDesigner = reportDesigner;
    initialize();
  }

  /**
   * 
   */
  public void initialize() {
    addDirtyListener(this);
    Composite contentGroup = getMainPanel();
    contentGroup.setLayout(new GridLayout());
    Label stepLabel = new Label(contentGroup, SWT.NONE);
    stepLabel.setText(Messages.getString("ReportSetupPanel.10")); //$NON-NLS-1$
    GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 3;
    stepLabel.setLayoutData(gridData);
    stepLabel.setFont(labelFont);
    Text stepText = new Text(contentGroup, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
    stepText.setText(Messages.getString("ReportSetupPanel.11")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 3;
    stepText.setLayoutData(gridData);
    stepText.setBackground(ReportWizard.background);
    stepText.setFont(textFont);
    SWTLine line = new SWTLine(contentGroup, SWT.NONE);
    line.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.BOTTOM, false, false);
    gridData.heightHint = 5;
    gridData.horizontalSpan = 3;
    line.setLayoutData(gridData);
    TabFolder tabFolder = new TabFolder(contentGroup, SWT.V_SCROLL);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 3;
    tabFolder.setLayoutData(gridData);
    TabItem item = new TabItem(tabFolder, SWT.NONE);
    item.setText(Messages.getString("ReportSetupPanel.1")); //$NON-NLS-1$
    Composite composite = new Composite(tabFolder, SWT.NONE);
    composite.setBackground(ReportWizard.background);
    GridLayout layout = new GridLayout(2, false);
    layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
    layout.verticalSpacing = 10;
    composite.setLayout(layout);
    item.setControl(composite);
    createOptionsPanel(composite);
    SWTUtility.setBackground(composite, ReportWizard.background);
    item = new TabItem(tabFolder, SWT.NONE);
    item.setText(Messages.getString("ReportSetupPanel.0")); //$NON-NLS-1$
    composite = new Composite(tabFolder, SWT.NONE);
    layout = new GridLayout();
    layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
    layout.verticalSpacing = 10;
    composite.setLayout(layout);
    item.setControl(composite);
    createExpressionsPanel(composite);
    SWTUtility.setBackground(composite, ReportWizard.background);
    item = new TabItem(tabFolder, SWT.NONE);
    item.setText(Messages.getString("ReportSetupPanel.32")); //$NON-NLS-1$
    composite = new Composite(tabFolder, SWT.NONE);
    composite.setBackground(ReportWizard.background);
    layout = new GridLayout();
    layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
    layout.verticalSpacing = 10;
    composite.setLayout(layout);
    item.setControl(composite);
    createColumnHeaderPanel(composite);
    SWTUtility.setBackground(composite, ReportWizard.background);
    item = new TabItem(tabFolder, SWT.NONE);
    item.setText(Messages.getString("ReportSetupPanel.33")); //$NON-NLS-1$
    composite = new Composite(tabFolder, SWT.NONE);
    composite.setBackground(ReportWizard.background);
    layout = new GridLayout();
    layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
    layout.verticalSpacing = 10;
    composite.setLayout(layout);
    item.setControl(composite);
    createFontPanel(composite);
    SWTUtility.setBackground(composite, ReportWizard.background);
    updateState();
    SWTUtility.setBackground(contentGroup, ReportWizard.background);
    SWTUtility.setBackground(tabFolder, ReportWizard.background);
  }

  public void createFontPanel(Composite parent) {
    fontPanel = new Composite(parent, SWT.NONE);
    fontPanel.setLayout(new GridLayout(3, false));
    Label groupHeaderFontLabel = new Label(fontPanel, SWT.NONE);
    GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
    groupHeaderFontLabel.setLayoutData(gridData);
    groupHeaderFontLabel.setText(Messages.getString("ReportSetupPanel.30")); //$NON-NLS-1$

    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    groupHeaderFontButton = new PentahoSWTButton(fontPanel, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("ReportSetupPanel.26")); //$NON-NLS-1$
    groupHeaderFontButton.addSelectionListener(this);
    groupHeaderFontButton.setLayoutData(gridData);

    groupHeaderSampleLabel = new Label(fontPanel, SWT.CENTER);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
    groupHeaderSampleLabel.setLayoutData(gridData);
    groupHeaderSampleLabel.setText(Messages.getString("ReportSetupPanel.27")); //$NON-NLS-1$
    Label groupFooterFontLabel = new Label(fontPanel, SWT.NONE);
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
    groupFooterFontLabel.setLayoutData(gridData);
    groupFooterFontLabel.setText(Messages.getString("ReportSetupPanel.28")); //$NON-NLS-1$

    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    groupFooterFontButton = new PentahoSWTButton(fontPanel, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("ReportSetupPanel.26")); //$NON-NLS-1$
    groupFooterFontButton.addSelectionListener(this);
    groupFooterFontButton.setLayoutData(gridData);
    groupFooterSampleLabel = new Label(fontPanel, SWT.CENTER);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
    groupFooterSampleLabel.setLayoutData(gridData);
    groupFooterSampleLabel.setText(Messages.getString("ReportSetupPanel.27")); //$NON-NLS-1$
    Label columnHeaderFontLabel = new Label(fontPanel, SWT.NONE);
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
    columnHeaderFontLabel.setLayoutData(gridData);
    columnHeaderFontLabel.setText(Messages.getString("ReportSetupPanel.25")); //$NON-NLS-1$

    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    columnHeaderFontButton = new PentahoSWTButton(fontPanel, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("ReportSetupPanel.26")); //$NON-NLS-1$
    columnHeaderFontButton.setLayoutData(gridData);
    columnHeaderFontButton.addSelectionListener(this);
    columnHeaderFontSampleLabel = new Label(fontPanel, SWT.CENTER);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
    columnHeaderFontSampleLabel.setLayoutData(gridData);
    columnHeaderFontSampleLabel.setText(Messages.getString("ReportSetupPanel.27")); //$NON-NLS-1$
    Label itemsFontLabel = new Label(fontPanel, SWT.NONE);
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
    itemsFontLabel.setLayoutData(gridData);
    itemsFontLabel.setText(Messages.getString("ReportSetupPanel.31")); //$NON-NLS-1$

    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    itemsFontButton = new PentahoSWTButton(fontPanel, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("ReportSetupPanel.26")); //$NON-NLS-1$
    itemsFontButton.addSelectionListener(this);
    itemsFontButton.setLayoutData(gridData);
    itemsFontSampleLabel = new Label(fontPanel, SWT.CENTER);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
    itemsFontSampleLabel.setLayoutData(gridData);
    itemsFontSampleLabel.setText(Messages.getString("ReportSetupPanel.27")); //$NON-NLS-1$
    
    fontPanel.pack(true);
  }

  public void createExpressionsPanel(Composite parent) {
    calculateGrandTotalsCheckbox = new Button(parent, SWT.CHECK);
    GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    calculateGrandTotalsCheckbox.setLayoutData(gridData);
    calculateGrandTotalsCheckbox.setText(Messages.getString("ReportSetupPanel.13")); //$NON-NLS-1$
    calculateGrandTotalsCheckbox.addSelectionListener(this);
    Label grandTotalLabel = new Label(parent, SWT.NONE);
    grandTotalLabel.setText(Messages.getString("ReportSetupPanel.5")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    grandTotalLabel.setLayoutData(gridData);
    grandTotalText = new Text(parent, SWT.SINGLE | SWT.BORDER);
    grandTotalText.setText(reportDesigner.getReportSpec().getGrandTotalsLabel());
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.widthHint = 225;
    grandTotalText.setLayoutData(gridData);
    // grandTotalText.setBackground(ReportWizard.background);
    grandTotalText.setFont(textFont);
    grandTotalText.addModifyListener(this);
    Label totalHorizontalAlignmentLabel = new Label(parent, SWT.NONE);
    totalHorizontalAlignmentLabel.setText(Messages.getString("ReportSetupPanel.6")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    totalHorizontalAlignmentLabel.setLayoutData(gridData);
    grandTotalHorizontalAlignmentCombo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.widthHint = 120;
    grandTotalHorizontalAlignmentCombo.setItems(ReportSpecUtility.getHorizontalAlignmentChoices());
    grandTotalHorizontalAlignmentCombo.select(grandTotalHorizontalAlignmentCombo.indexOf(reportDesigner.getReportSpec().getGrandTotalsHorizontalAlignment())); //$NON-NLS-1$
    grandTotalHorizontalAlignmentCombo.setLayoutData(gridData);
    grandTotalHorizontalAlignmentCombo.addSelectionListener(this);
    useUnderliningCheckbox = new Button(parent, SWT.CHECK);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    useUnderliningCheckbox.setLayoutData(gridData);
    useUnderliningCheckbox.setText(Messages.getString("PageSetupPanel.15")); //$NON-NLS-1$
    useUnderliningCheckbox.addSelectionListener(this);
    useDoubleUnderliningCheckbox = new Button(parent, SWT.CHECK);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    useDoubleUnderliningCheckbox.setLayoutData(gridData);
    useDoubleUnderliningCheckbox.setText(Messages.getString("PageSetupPanel.16")); //$NON-NLS-1$
    useDoubleUnderliningCheckbox.addSelectionListener(this);
    Composite grandTotalBackgroundColorComposite = new Composite(parent, SWT.NONE);
    grandTotalBackgroundColorComposite.setBackground(ReportWizard.background);
    grandTotalBackgroundColorComposite.setLayout(new GridLayout(2, false));
    ((GridLayout) grandTotalBackgroundColorComposite.getLayout()).marginHeight = 0;
    ((GridLayout) grandTotalBackgroundColorComposite.getLayout()).marginWidth = 0;
    gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    grandTotalBackgroundColorComposite.setLayoutData(gridData);
    useGroupFooterBackgroundColorCheckbox = new Button(grandTotalBackgroundColorComposite, SWT.CHECK);
    useGroupFooterBackgroundColorCheckbox.setBackground(ReportWizard.background);
    useGroupFooterBackgroundColorCheckbox.setText(Messages.getString("ReportSetupPanel.34")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    useGroupFooterBackgroundColorCheckbox.setLayoutData(gridData);
    useGroupFooterBackgroundColorCheckbox.addSelectionListener(this);
    useGroupFooterBackgroundColorCheckbox.setSelection(reportDesigner.getReportSpec().getUseDummyGroupFooterBackgroundColor());
    groupFooterBackgroundColorButton = new SWTButton(grandTotalBackgroundColorComposite, SWT.NONE);
    groupFooterBackgroundColorButton.setBackground(new Color(getDisplay(), SWTUtility.getRGB(reportDesigner.getReportSpec().getDummyGroupFooterBackgroundColor())));
    groupFooterBackgroundColorButton.setEtchedColors(new Color(getDisplay(), new RGB(0, 0, 0)), new Color(getDisplay(), new RGB(192, 192, 192)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    gridData.heightHint = 15;
    gridData.widthHint = 15;
    groupFooterBackgroundColorButton.setLayoutData(gridData);
    groupFooterBackgroundColorButton.addMouseListener(this);
    groupFooterBackgroundColorButton.addMouseTrackListener(this);
  }

  public void createColumnHeaderPanel(Composite parent) {
    Composite headerHeightComposite = new Composite(parent, SWT.NONE);
    GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    headerHeightComposite.setLayoutData(gridData);
    headerHeightComposite.setLayout(new GridLayout(2, false));
    Label headerHeightLabel = new Label(headerHeightComposite, SWT.RIGHT);
    headerHeightLabel.setText(Messages.getString("PageSetupPanel.18")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    headerHeightLabel.setLayoutData(gridData);
    columnHeaderHeightSpinner = new Spinner(headerHeightComposite, SWT.BORDER);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    columnHeaderHeightSpinner.setLayoutData(gridData);
    columnHeaderHeightSpinner.addSelectionListener(this);
    columnHeaderHeightSpinner.setSelection(reportDesigner.getReportSpec().getColumnHeaderHeight());

    // top gap
    Composite headerTopGapComposite = new Composite(parent, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    headerTopGapComposite.setLayoutData(gridData);
    headerTopGapComposite.setLayout(new GridLayout(2, false));
    Label headerTopGapLabel = new Label(headerTopGapComposite, SWT.RIGHT);
    headerTopGapLabel.setText(Messages.getString("ReportSetupPanel.41")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    headerTopGapLabel.setLayoutData(gridData);
    columnHeaderTopGapSpinner = new Spinner(headerTopGapComposite, SWT.BORDER);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    columnHeaderTopGapSpinner.setLayoutData(gridData);
    columnHeaderTopGapSpinner.addSelectionListener(this);
    columnHeaderTopGapSpinner.setSelection(reportDesigner.getReportSpec().getColumnHeaderTopGap());
    // bottom gap
    Composite headerGapComposite = new Composite(parent, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    headerGapComposite.setLayoutData(gridData);
    headerGapComposite.setLayout(new GridLayout(2, false));
    Label headerGapLabel = new Label(headerGapComposite, SWT.RIGHT);
    headerGapLabel.setText(Messages.getString("ReportSetupPanel.42")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    headerGapLabel.setLayoutData(gridData);
    columnHeaderGapSpinner = new Spinner(headerGapComposite, SWT.BORDER);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    columnHeaderGapSpinner.setLayoutData(gridData);
    columnHeaderGapSpinner.addSelectionListener(this);
    columnHeaderGapSpinner.setSelection(reportDesigner.getReportSpec().getColumnHeaderGap());

    // column header background color
    RGB color = SWTUtility.getRGB(reportDesigner.getReportSpec().getColumnHeaderBackgroundColor());
    Composite backgroundComposite = new Composite(parent, SWT.SHADOW_ETCHED_OUT);
    backgroundComposite.setBackground(ReportWizard.background);
    backgroundComposite.setLayout(new GridLayout(2, false));
    ((GridLayout) backgroundComposite.getLayout()).marginHeight = 0;
    ((GridLayout) backgroundComposite.getLayout()).marginWidth = 0;
    gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    backgroundComposite.setLayoutData(gridData);
    useColumnHeaderBackgroundCheckbox = new Button(backgroundComposite, SWT.CHECK);
    useColumnHeaderBackgroundCheckbox.setBackground(ReportWizard.background);
    useColumnHeaderBackgroundCheckbox.setText(Messages.getString("ReportSetupPanel.2")); //$NON-NLS-1$
    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    useColumnHeaderBackgroundCheckbox.setLayoutData(gridData);
    useColumnHeaderBackgroundCheckbox.addSelectionListener(this);
    useColumnHeaderBackgroundCheckbox.setSelection(reportDesigner.getReportSpec().getUseRowBanding());
    columnHeaderBackgroundButton = new SWTButton(backgroundComposite, SWT.NONE);
    columnHeaderBackgroundButton.setBackground(new Color(getDisplay(), color));
    columnHeaderBackgroundButton.setEtchedColors(new Color(getDisplay(), new RGB(0, 0, 0)), new Color(getDisplay(), new RGB(192, 192, 192)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    gridData.heightHint = 15;
    gridData.widthHint = 15;
    columnHeaderBackgroundButton.setLayoutData(gridData);
    columnHeaderBackgroundButton.addMouseListener(this);
    columnHeaderBackgroundButton.addMouseTrackListener(this);
    generateReportLevelColumnHeadersButton = new Button(parent, SWT.CHECK);
    generateReportLevelColumnHeadersButton.setText(Messages.getString("ReportSetupPanel.35")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    generateReportLevelColumnHeadersButton.setLayoutData(gridData);
    generateReportLevelColumnHeadersButton.addSelectionListener(this);
  }

  public void createOptionsPanel(Composite parent) {
    // row banding color
    RGB color = SWTUtility.getRGB(reportDesigner.getReportSpec().getRowBandingColor());
    Composite backgroundComposite = new Composite(parent, SWT.NONE);
    backgroundComposite.setBackground(ReportWizard.background);
    backgroundComposite.setLayout(new GridLayout(2, false));
    ((GridLayout) backgroundComposite.getLayout()).marginHeight = 0;
    ((GridLayout) backgroundComposite.getLayout()).marginWidth = 0;
    GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    gridData.horizontalSpan = 2;
    backgroundComposite.setLayoutData(gridData);
    bandingCheckbox = new Button(backgroundComposite, SWT.CHECK);
    bandingCheckbox.setBackground(ReportWizard.background);
    bandingCheckbox.setText(Messages.getString("ReportSetupPanel.21")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    bandingCheckbox.setLayoutData(gridData);
    bandingCheckbox.addSelectionListener(this);
    bandingCheckbox.setSelection(reportDesigner.getReportSpec().getUseRowBanding());
    rowBandingButton = new SWTButton(backgroundComposite, SWT.NONE);
    rowBandingButton.setBackground(new Color(getDisplay(), color));
    rowBandingButton.setEtchedColors(new Color(getDisplay(), new RGB(0, 0, 0)), new Color(getDisplay(), new RGB(192, 192, 192)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    gridData.heightHint = 15;
    gridData.widthHint = 15;
    rowBandingButton.setLayoutData(gridData);
    rowBandingButton.addMouseListener(this);
    rowBandingButton.addMouseTrackListener(this);
    rowBandingInitialStateCheckbox = new Button(parent, SWT.CHECK);
    rowBandingInitialStateCheckbox.setBackground(ReportWizard.background);
    rowBandingInitialStateCheckbox.setText(Messages.getString("ReportSetupPanel.7")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    rowBandingInitialStateCheckbox.setLayoutData(gridData);
    rowBandingInitialStateCheckbox.addSelectionListener(this);
    rowBandingInitialStateCheckbox.setSelection(reportDesigner.getReportSpec().getRowBandingInitialState());
    rowBandingInitialStateCheckbox.setEnabled(bandingCheckbox.getSelection());
    // horizontal gridlines background color
    RGB gridColor = SWTUtility.getRGB(reportDesigner.getReportSpec().getHorizontalGridlinesColor());
    Composite horizontalGridlinesComposite = new Composite(parent, SWT.NONE);
    horizontalGridlinesComposite.setBackground(ReportWizard.background);
    horizontalGridlinesComposite.setLayout(new GridLayout(2, false));
    ((GridLayout) horizontalGridlinesComposite.getLayout()).marginHeight = 0;
    ((GridLayout) horizontalGridlinesComposite.getLayout()).marginWidth = 0;
    gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    gridData.horizontalSpan = 2;
    horizontalGridlinesComposite.setLayoutData(gridData);
    useHorizontalGridlinesCheckbox = new Button(horizontalGridlinesComposite, SWT.CHECK);
    useHorizontalGridlinesCheckbox.setBackground(ReportWizard.background);
    useHorizontalGridlinesCheckbox.setText(Messages.getString("ReportSetupPanel.3")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    useHorizontalGridlinesCheckbox.setLayoutData(gridData);
    useHorizontalGridlinesCheckbox.addSelectionListener(this);
    useHorizontalGridlinesCheckbox.setSelection(reportDesigner.getReportSpec().getUseHorizontalGridlines());
    horizontalGridlinesColorButton = new SWTButton(horizontalGridlinesComposite, SWT.NONE);
    horizontalGridlinesColorButton.setBackground(new Color(getDisplay(), gridColor));
    horizontalGridlinesColorButton.setEtchedColors(new Color(getDisplay(), new RGB(0, 0, 0)), new Color(getDisplay(), new RGB(192, 192, 192)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    gridData.heightHint = 15;
    gridData.widthHint = 15;
    horizontalGridlinesColorButton.setLayoutData(gridData);
    horizontalGridlinesColorButton.addMouseListener(this);
    horizontalGridlinesColorButton.addMouseTrackListener(this);
    // vertical gridlines background color
    gridColor = SWTUtility.getRGB(reportDesigner.getReportSpec().getVerticalGridlinesColor());
    Composite verticalGridlinesComposite = new Composite(parent, SWT.NONE);
    verticalGridlinesComposite.setBackground(ReportWizard.background);
    verticalGridlinesComposite.setLayout(new GridLayout(2, false));
    ((GridLayout) verticalGridlinesComposite.getLayout()).marginHeight = 0;
    ((GridLayout) verticalGridlinesComposite.getLayout()).marginWidth = 0;
    gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    gridData.horizontalSpan = 2;
    verticalGridlinesComposite.setLayoutData(gridData);
    useVerticalGridlinesCheckbox = new Button(verticalGridlinesComposite, SWT.CHECK);
    useVerticalGridlinesCheckbox.setBackground(ReportWizard.background);
    useVerticalGridlinesCheckbox.setText(Messages.getString("ReportSetupPanel.4")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    useVerticalGridlinesCheckbox.setLayoutData(gridData);
    useVerticalGridlinesCheckbox.addSelectionListener(this);
    useVerticalGridlinesCheckbox.setSelection(reportDesigner.getReportSpec().getUseVerticalGridlines());
    verticalGridlinesColorButton = new SWTButton(verticalGridlinesComposite, SWT.NONE);
    verticalGridlinesColorButton.setBackground(new Color(getDisplay(), gridColor));
    verticalGridlinesColorButton.setEtchedColors(new Color(getDisplay(), new RGB(0, 0, 0)), new Color(getDisplay(), new RGB(192, 192, 192)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    gridData.heightHint = 15;
    gridData.widthHint = 15;
    verticalGridlinesColorButton.setLayoutData(gridData);
    verticalGridlinesColorButton.addMouseListener(this);
    verticalGridlinesColorButton.addMouseTrackListener(this);
    // create spinner for 'x' offset
    Composite horizontalOffsetComposite = new Composite(parent, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    horizontalOffsetComposite.setLayoutData(gridData);
    horizontalOffsetComposite.setLayout(new GridLayout(2, false));
    Label horizontalOffsetLabel = new Label(horizontalOffsetComposite, SWT.RIGHT);
    horizontalOffsetLabel.setText(Messages.getString("ReportSetupPanel.24")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    horizontalOffsetLabel.setLayoutData(gridData);
    horizontalOffsetSpinner = new Spinner(horizontalOffsetComposite, SWT.BORDER);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    horizontalOffsetSpinner.setLayoutData(gridData);
    horizontalOffsetSpinner.addSelectionListener(this);
    horizontalOffsetSpinner.setSelection(reportDesigner.getReportSpec().getHorizontalOffset());
//    Composite chartComposite = new Composite(parent, SWT.NONE);
//    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
//    gridData.horizontalSpan = 2;
//    chartComposite.setLayoutData(gridData);
//    chartComposite.setLayout(new GridLayout(2, false));
//    useChartCheckBox = new Button(chartComposite, SWT.CHECK);
//    useChartCheckBox.setSelection(reportDesigner.getReportSpec().getUseChart());
//    useChartCheckBox.setText(Messages.getString("ReportSetupPanel.36")); //$NON-NLS-1$
//    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
//    useChartCheckBox.setLayoutData(gridData);
//    useChartCheckBox.addSelectionListener(this);
//
//    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
//    configureChartButton = new PentahoSWTButton(parent, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("ReportSetupPanel.37")); //$NON-NLS-1$
//    configureChartButton.setLayoutData(gridData);
//    configureChartButton.addSelectionListener(this);

    Label includeLabel = new Label(parent, SWT.NONE);
    includeLabel.setText(Messages.getString("ReportSetupPanel.22")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    includeLabel.setLayoutData(gridData);
    includeLabel.setFont(labelFont);
    SWTLine includeLine = new SWTLine(parent, SWT.NONE);
    includeLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.heightHint = 5;
    gridData.horizontalSpan = 2;
    includeLine.setLayoutData(gridData);

    includeText = new Text(parent, SWT.BORDER);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.widthHint = 600;
    includeText.setLayoutData(gridData);
    includeText.addModifyListener(this);

    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    includeBrowseButton = new PentahoSWTButton(parent, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("ReportSetupPanel.23")); //$NON-NLS-1$
    includeBrowseButton.setLayoutData(gridData);
    includeBrowseButton.addSelectionListener(this);
  }

  public void mouseUp(MouseEvent e) {
    if (e.widget == rowBandingButton) {
      ColorDialog colorDialog = new ColorDialog(getShell());
      colorDialog.setRGB(SWTUtility.getRGB(reportDesigner.getReportSpec().getRowBandingColor()));
      RGB pickedColor = colorDialog.open();
      if (pickedColor != null) {
        reportDesigner.getReportSpec().setRowBandingColor(SWTUtility.getJFreeColorString(pickedColor));
        rowBandingButton.setBackground(new Color(getDisplay(), pickedColor));
        bandingCheckbox.setSelection(true);
        reportDesigner.getReportSpec().setUseRowBanding(true);
      }
      rowBandingButton.redraw();
      rowBandingButton.update();
    } else if (e.widget == columnHeaderBackgroundButton) {
      ColorDialog colorDialog = new ColorDialog(getShell());
      colorDialog.setRGB(SWTUtility.getRGB(reportDesigner.getReportSpec().getColumnHeaderBackgroundColor()));
      RGB pickedColor = colorDialog.open();
      if (pickedColor != null) {
        reportDesigner.getReportSpec().setColumnHeaderBackgroundColor(SWTUtility.getJFreeColorString(pickedColor));
        columnHeaderBackgroundButton.setBackground(new Color(getDisplay(), pickedColor));
        useColumnHeaderBackgroundCheckbox.setSelection(true);
        reportDesigner.getReportSpec().setUseColumnHeaderBackgroundColor(true);
      }
      columnHeaderBackgroundButton.redraw();
      columnHeaderBackgroundButton.update();
    } else if (e.widget == horizontalGridlinesColorButton) {
      ColorDialog colorDialog = new ColorDialog(getShell());
      colorDialog.setRGB(SWTUtility.getRGB(reportDesigner.getReportSpec().getHorizontalGridlinesColor()));
      RGB pickedColor = colorDialog.open();
      if (pickedColor != null) {
        reportDesigner.getReportSpec().setHorizontalGridlinesColor(SWTUtility.getJFreeColorString(pickedColor));
        horizontalGridlinesColorButton.setBackground(new Color(getDisplay(), pickedColor));
        useHorizontalGridlinesCheckbox.setSelection(true);
        reportDesigner.getReportSpec().setUseHorizontalGridlines(true);
      }
      horizontalGridlinesColorButton.redraw();
      horizontalGridlinesColorButton.update();
    } else if (e.widget == verticalGridlinesColorButton) {
      ColorDialog colorDialog = new ColorDialog(getShell());
      colorDialog.setRGB(SWTUtility.getRGB(reportDesigner.getReportSpec().getVerticalGridlinesColor()));
      RGB pickedColor = colorDialog.open();
      if (pickedColor != null) {
        reportDesigner.getReportSpec().setVerticalGridlinesColor(SWTUtility.getJFreeColorString(pickedColor));
        verticalGridlinesColorButton.setBackground(new Color(getDisplay(), pickedColor));
        useVerticalGridlinesCheckbox.setSelection(true);
        reportDesigner.getReportSpec().setUseVerticalGridlines(true);
      }
      verticalGridlinesColorButton.redraw();
      verticalGridlinesColorButton.update();
    } else if (e.widget == groupFooterBackgroundColorButton) {
      ColorDialog colorDialog = new ColorDialog(getShell());
      colorDialog.setRGB(SWTUtility.getRGB(reportDesigner.getReportSpec().getDummyGroupFooterBackgroundColor()));
      RGB pickedColor = colorDialog.open();
      if (pickedColor != null) {
        reportDesigner.getReportSpec().setDummyGroupFooterBackgroundColor(SWTUtility.getJFreeColorString(pickedColor));
        groupFooterBackgroundColorButton.setBackground(new Color(getDisplay(), pickedColor));
        useGroupFooterBackgroundColorCheckbox.setSelection(true);
        reportDesigner.getReportSpec().setUseDummyGroupFooterBackgroundColor(true);
      }
      groupFooterBackgroundColorButton.redraw();
      groupFooterBackgroundColorButton.update();
    }
  }

  public void keyReleased(KeyEvent e) {
    if (e.getSource() == grandTotalText) {
      reportDesigner.getReportSpec().setGrandTotalsLabel(grandTotalText.getText());
    } else if (e.getSource() == includeText) {
      reportDesigner.getReportSpec().setIncludeSrc(includeText.getText());
    }
  }

  /**
   * 
   * 
   * @param e
   */
  public void widgetSelected(SelectionEvent e) {
    if (e.getSource() == bandingCheckbox) {
      reportDesigner.getReportSpec().setUseRowBanding(bandingCheckbox.getSelection());
      rowBandingButton.setEnabled(bandingCheckbox.getSelection());
      rowBandingButton.redraw();
      rowBandingButton.update();
      rowBandingInitialStateCheckbox.setEnabled(bandingCheckbox.getSelection());
      if (bandingCheckbox.getSelection()) {
        ColorDialog colorDialog = new ColorDialog(getShell());
        colorDialog.setRGB(SWTUtility.getRGB(reportDesigner.getReportSpec().getRowBandingColor()));
        RGB pickedColor = colorDialog.open();
        if (pickedColor != null) {
          reportDesigner.getReportSpec().setRowBandingColor(SWTUtility.getJFreeColorString(pickedColor));
          rowBandingButton.setBackground(new Color(getDisplay(), pickedColor));
        }
      }
    } else if (e.getSource() == grandTotalHorizontalAlignmentCombo) {
      reportDesigner.getReportSpec().setGrandTotalsHorizontalAlignment(grandTotalHorizontalAlignmentCombo.getItem(grandTotalHorizontalAlignmentCombo.getSelectionIndex()));
    } else if (e.getSource() == useColumnHeaderBackgroundCheckbox) {
      reportDesigner.getReportSpec().setUseColumnHeaderBackgroundColor(useColumnHeaderBackgroundCheckbox.getSelection());
      columnHeaderBackgroundButton.setEnabled(useColumnHeaderBackgroundCheckbox.getSelection());
      columnHeaderBackgroundButton.redraw();
      columnHeaderBackgroundButton.update();
      if (useColumnHeaderBackgroundCheckbox.getSelection()) {
        ColorDialog colorDialog = new ColorDialog(getShell());
        colorDialog.setRGB(SWTUtility.getRGB(reportDesigner.getReportSpec().getColumnHeaderBackgroundColor()));
        RGB pickedColor = colorDialog.open();
        if (pickedColor != null) {
          reportDesigner.getReportSpec().setColumnHeaderBackgroundColor(SWTUtility.getJFreeColorString(pickedColor));
          columnHeaderBackgroundButton.setBackground(new Color(getDisplay(), pickedColor));
        }
      }
    } else if (e.getSource() == useHorizontalGridlinesCheckbox) {
      reportDesigner.getReportSpec().setUseHorizontalGridlines(useHorizontalGridlinesCheckbox.getSelection());
      horizontalGridlinesColorButton.setEnabled(useHorizontalGridlinesCheckbox.getSelection());
      horizontalGridlinesColorButton.redraw();
      useHorizontalGridlinesCheckbox.update();
      if (useHorizontalGridlinesCheckbox.getSelection()) {
        ColorDialog colorDialog = new ColorDialog(getShell());
        colorDialog.setRGB(SWTUtility.getRGB(reportDesigner.getReportSpec().getHorizontalGridlinesColor()));
        RGB pickedColor = colorDialog.open();
        if (pickedColor != null) {
          reportDesigner.getReportSpec().setHorizontalGridlinesColor(SWTUtility.getJFreeColorString(pickedColor));
          horizontalGridlinesColorButton.setBackground(new Color(getDisplay(), pickedColor));
        }
      }
    } else if (e.getSource() == useVerticalGridlinesCheckbox) {
      reportDesigner.getReportSpec().setUseVerticalGridlines(useVerticalGridlinesCheckbox.getSelection());
      verticalGridlinesColorButton.setEnabled(useVerticalGridlinesCheckbox.getSelection());
      verticalGridlinesColorButton.redraw();
      verticalGridlinesColorButton.update();
      if (useVerticalGridlinesCheckbox.getSelection()) {
        ColorDialog colorDialog = new ColorDialog(getShell());
        colorDialog.setRGB(SWTUtility.getRGB(reportDesigner.getReportSpec().getVerticalGridlinesColor()));
        RGB pickedColor = colorDialog.open();
        if (pickedColor != null) {
          reportDesigner.getReportSpec().setVerticalGridlinesColor(SWTUtility.getJFreeColorString(pickedColor));
          verticalGridlinesColorButton.setBackground(new Color(getDisplay(), pickedColor));
        }
      }
    } else if (e.getSource() == useGroupFooterBackgroundColorCheckbox) {
      reportDesigner.getReportSpec().setUseDummyGroupFooterBackgroundColor(useGroupFooterBackgroundColorCheckbox.getSelection());
      groupFooterBackgroundColorButton.setEnabled(useGroupFooterBackgroundColorCheckbox.getSelection());
      groupFooterBackgroundColorButton.redraw();
      groupFooterBackgroundColorButton.update();
      if (useGroupFooterBackgroundColorCheckbox.getSelection()) {
        ColorDialog colorDialog = new ColorDialog(getShell());
        colorDialog.setRGB(SWTUtility.getRGB(reportDesigner.getReportSpec().getDummyGroupFooterBackgroundColor()));
        RGB pickedColor = colorDialog.open();
        if (pickedColor != null) {
          reportDesigner.getReportSpec().setDummyGroupFooterBackgroundColor(SWTUtility.getJFreeColorString(pickedColor));
          groupFooterBackgroundColorButton.setBackground(new Color(getDisplay(), pickedColor));
        }
      }
    } else if (e.getSource() == calculateGrandTotalsCheckbox) {
      reportDesigner.getReportSpec().setCalculateGrandTotals(calculateGrandTotalsCheckbox.getSelection());
    } else if (e.getSource() == useUnderliningCheckbox) {
      reportDesigner.getReportSpec().setUseExpressionUnderlining(useUnderliningCheckbox.getSelection());
    } else if (e.getSource() == useDoubleUnderliningCheckbox) {
      reportDesigner.getReportSpec().setDoubleUnderlineExpression(useDoubleUnderliningCheckbox.getSelection());
    } else if (e.getSource() == columnHeaderHeightSpinner) {
      reportDesigner.getReportSpec().setColumnHeaderHeight(columnHeaderHeightSpinner.getSelection());
    } else if (e.getSource() == columnHeaderGapSpinner) {
      reportDesigner.getReportSpec().setColumnHeaderGap(columnHeaderGapSpinner.getSelection());
    } else if (e.getSource() == columnHeaderTopGapSpinner) {
      reportDesigner.getReportSpec().setColumnHeaderTopGap(columnHeaderTopGapSpinner.getSelection());
    } else if (e.getSource() == horizontalOffsetSpinner) {
      reportDesigner.getReportSpec().setHorizontalOffset(horizontalOffsetSpinner.getSelection());
    } else if (e.getSource() == includeBrowseButton) {
      FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
      String fileName = fileDialog.open();
      if (fileName != null) {
        File f = new File(fileName);
        includeText.setText(f.getAbsolutePath());
        reportDesigner.getReportSpec().setIncludeSrc(f.getAbsolutePath());
      } else {
        includeText.setText(""); //$NON-NLS-1$
        reportDesigner.getReportSpec().setIncludeSrc(""); //$NON-NLS-1$
      }
    } else if (e.getSource() == groupHeaderFontButton) {
      FontDialog fd = new FontDialog(getShell());
      fd.setFontList(groupHeaderSampleLabel.getFont().getFontData());
      fd.setRGB(groupHeaderSampleLabel.getForeground().getRGB());
      FontData fontData = fd.open();
      if (fontData != null) {
        Font font = new Font(getDisplay(), fontData);
        if (fd.getRGB() != null) {
          Color color = new Color(getDisplay(), fd.getRGB());
          groupHeaderSampleLabel.setForeground(color);
          reportDesigner.getReportSpec().setGroupHeaderFontColor(SWTUtility.getJFreeColorString(fd.getRGB()));
        }
        groupHeaderSampleLabel.setFont(font);
        if (fontData.getName() != null && !fontData.getName().equals("")) { //$NON-NLS-1$
          reportDesigner.getReportSpec().setGroupHeaderFontName(fontData.getName());
        }
        reportDesigner.getReportSpec().setGroupHeaderFontSize(fontData.getHeight());
        reportDesigner.getReportSpec().setGroupHeaderFontStyle(fontData.getStyle());
      }
      fontPanel.pack(true);
    } else if (e.getSource() == groupFooterFontButton) {
      FontDialog fd = new FontDialog(getShell());
      fd.setFontList(groupFooterSampleLabel.getFont().getFontData());
      fd.setRGB(groupFooterSampleLabel.getForeground().getRGB());
      FontData fontData = fd.open();
      if (fontData != null) {
        Font font = new Font(getDisplay(), fontData);
        if (fd.getRGB() != null) {
          Color color = new Color(getDisplay(), fd.getRGB());
          groupFooterSampleLabel.setForeground(color);
          reportDesigner.getReportSpec().setGroupFooterFontColor(SWTUtility.getJFreeColorString(fd.getRGB()));
        }
        groupFooterSampleLabel.setFont(font);
        if (fontData.getName() != null && !fontData.getName().equals("")) { //$NON-NLS-1$
          reportDesigner.getReportSpec().setGroupFooterFontName(fontData.getName());
        }
        reportDesigner.getReportSpec().setGroupFooterFontSize(fontData.getHeight());
        reportDesigner.getReportSpec().setGroupFooterFontStyle(fontData.getStyle());
      }
    } else if (e.getSource() == columnHeaderFontButton) {
      FontDialog fd = new FontDialog(getShell());
      fd.setFontList(columnHeaderFontSampleLabel.getFont().getFontData());
      fd.setRGB(columnHeaderFontSampleLabel.getForeground().getRGB());
      FontData fontData = fd.open();
      if (fontData != null) {
        Font font = new Font(getDisplay(), fontData);
        if (fd.getRGB() != null) {
          Color color = new Color(getDisplay(), fd.getRGB());
          columnHeaderFontSampleLabel.setForeground(color);
          reportDesigner.getReportSpec().setColumnHeaderFontColor(SWTUtility.getJFreeColorString(fd.getRGB()));
        }
        columnHeaderFontSampleLabel.setFont(font);
        if (fontData.getName() != null && !fontData.getName().equals("")) { //$NON-NLS-1$
          reportDesigner.getReportSpec().setColumnHeaderFontName(fontData.getName());
        }
        reportDesigner.getReportSpec().setColumnHeaderFontSize(fontData.getHeight());
        reportDesigner.getReportSpec().setColumnHeaderFontStyle(fontData.getStyle());
      }
    } else if (e.getSource() == itemsFontButton) {
      FontDialog fd = new FontDialog(getShell());
      fd.setFontList(itemsFontSampleLabel.getFont().getFontData());
      fd.setRGB(itemsFontSampleLabel.getForeground().getRGB());
      FontData fontData = fd.open();
      if (fontData != null) {
        Font font = new Font(getDisplay(), fontData);
        if (fd.getRGB() != null) {
          Color color = new Color(getDisplay(), fd.getRGB());
          itemsFontSampleLabel.setForeground(color);
          reportDesigner.getReportSpec().setItemsFontColor(SWTUtility.getJFreeColorString(fd.getRGB()));
        }
        itemsFontSampleLabel.setFont(font);
        if (fontData.getName() != null && !fontData.getName().equals("")) { //$NON-NLS-1$
          reportDesigner.getReportSpec().setItemsFontName(fontData.getName());
        }
        reportDesigner.getReportSpec().setItemsFontSize(fontData.getHeight());
        reportDesigner.getReportSpec().setItemsFontStyle(fontData.getStyle());
      }
    } else if (e.getSource() == rowBandingInitialStateCheckbox) {
      reportDesigner.getReportSpec().setRowBandingInitialState(rowBandingInitialStateCheckbox.getSelection());
    } else if (e.widget == configureChartButton) {
      ensureChart();
      ChartSettingsDialog dialog = new ChartSettingsDialog(
          Messages.getString("ReportSetupPanel.40"), reportDesigner.getReportSpec().getChart(), ReportSpecUtility.getFieldNames(reportDesigner.getReportSpec()), ReportSpecUtility.getGroupFieldNames(reportDesigner //$NON-NLS-1$
              .getReportSpec()));
      dialog.open();
    } else if (e.widget == useChartCheckBox) {
      reportDesigner.getReportSpec().setUseChart(useChartCheckBox.getSelection());
    } else if (e.widget == generateReportLevelColumnHeadersButton) {
      reportDesigner.getReportSpec().setGenerateReportLevelColumnHeaders(generateReportLevelColumnHeadersButton.getSelection());
      // if we turn this one, a nice default is to turn off all group headers
      if (generateReportLevelColumnHeadersButton.getSelection()) {
        Field groups[] = ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField());
        for (int i = 0; i < groups.length; i++) {
          groups[i].setCreateGroupHeader(false);
        }
      }
    }
    super.widgetSelected(e);
    stateChanged = true;
  }

  public void ensureChart() {
    Chart chart = reportDesigner.getReportSpec().getChart();
    if (chart == null) {
      chart = new Chart();
      chart.setType(ChartType.BAR);
      reportDesigner.getReportSpec().setChart(chart);
    }
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean isContinueAllowed() {
    boolean continueAllowed = true;
    return continueAllowed;
  }

  /**
   * 
   * 
   * @param source
   */
  public boolean nextFired(WizardPanel source) {
    super.nextFired(source);
    if (isContinueAllowed() && (this == source) && stateChanged) {
      updateDesignerState();
    }
    return true;
  }

  /**
   * 
   * 
   * @param source
   */
  public void finishFired(WizardPanel source) {
    super.finishFired(source);
    if ((this == source) && stateChanged) {
      updateDesignerState();
    }
  }

  /**
   * 
   */
  public void updateDesignerState() {
    // event driven
  }

  public void initWizardPanel() {
    initWizardPanel(reportDesigner.getReportSpec());
  }

  /**
   * 
   * 
   * @param reportSpec
   */
  public void initWizardPanel(ReportSpec reportSpec) {
    if (reportSpec.getRowBandingColor() != null) {
      RGB bandingColor = SWTUtility.getRGB(reportSpec.getRowBandingColor());
      rowBandingButton.setBackground(new Color(getDisplay(), bandingColor));
    }
    bandingCheckbox.setSelection(reportSpec.getUseRowBanding());
    rowBandingButton.setEnabled(bandingCheckbox.getSelection());
    useUnderliningCheckbox.setSelection(reportSpec.getUseExpressionUnderlining());
    useDoubleUnderliningCheckbox.setSelection(reportSpec.getDoubleUnderlineExpression());
    useColumnHeaderBackgroundCheckbox.setSelection(reportSpec.getUseColumnHeaderBackgroundColor());
    columnHeaderBackgroundButton.setEnabled(useColumnHeaderBackgroundCheckbox.getSelection());
    if (reportSpec.getColumnHeaderBackgroundColor() != null) {
      RGB columnHeaderColor = SWTUtility.getRGB(reportSpec.getColumnHeaderBackgroundColor());
      columnHeaderBackgroundButton.setBackground(new Color(getDisplay(), columnHeaderColor));
    }
    useHorizontalGridlinesCheckbox.setSelection(reportSpec.getUseHorizontalGridlines());
    horizontalGridlinesColorButton.setEnabled(useHorizontalGridlinesCheckbox.getSelection());
    if (reportSpec.getHorizontalGridlinesColor() != null) {
      RGB gridlinesColor = SWTUtility.getRGB(reportSpec.getHorizontalGridlinesColor());
      horizontalGridlinesColorButton.setBackground(new Color(getDisplay(), gridlinesColor));
    }
    useVerticalGridlinesCheckbox.setSelection(reportSpec.getUseVerticalGridlines());
    verticalGridlinesColorButton.setEnabled(useVerticalGridlinesCheckbox.getSelection());
    if (reportSpec.getVerticalGridlinesColor() != null) {
      RGB gridlinesColor = SWTUtility.getRGB(reportSpec.getVerticalGridlinesColor());
      verticalGridlinesColorButton.setBackground(new Color(getDisplay(), gridlinesColor));
    }
    useGroupFooterBackgroundColorCheckbox.setSelection(reportSpec.getUseDummyGroupFooterBackgroundColor());
    groupFooterBackgroundColorButton.setEnabled(useGroupFooterBackgroundColorCheckbox.getSelection());
    if (reportSpec.getDummyGroupFooterBackgroundColor() != null) {
      RGB gridlinesColor = SWTUtility.getRGB(reportSpec.getDummyGroupFooterBackgroundColor());
      groupFooterBackgroundColorButton.setBackground(new Color(getDisplay(), gridlinesColor));
    }
    grandTotalHorizontalAlignmentCombo.select(grandTotalHorizontalAlignmentCombo.indexOf(reportSpec.getGrandTotalsHorizontalAlignment())); //$NON-NLS-1$
    columnHeaderHeightSpinner.setSelection(reportSpec.getColumnHeaderHeight());
    columnHeaderGapSpinner.setSelection(reportSpec.getColumnHeaderGap());
    columnHeaderTopGapSpinner.setSelection(reportSpec.getColumnHeaderTopGap());
    horizontalOffsetSpinner.setSelection(reportSpec.getHorizontalOffset());
    calculateGrandTotalsCheckbox.setSelection(reportSpec.getCalculateGrandTotals());
    String groupHeaderFontName = reportSpec.getGroupHeaderFontName();
    int groupHeaderFontSize = reportSpec.getGroupHeaderFontSize();
    int groupHeaderFontStyle = reportSpec.getGroupHeaderFontStyle();
    RGB groupHeaderFontColor = SWTUtility.getRGB(reportSpec.getGroupHeaderFontColor());
    FontData groupHeaderFontData = new FontData(groupHeaderFontName, groupHeaderFontSize, groupHeaderFontStyle);
    groupHeaderSampleLabel.setFont(new Font(getDisplay(), groupHeaderFontData));
    groupHeaderSampleLabel.setForeground(new Color(getDisplay(), groupHeaderFontColor));
    String groupFooterFontName = reportSpec.getGroupFooterFontName();
    int groupFooterFontSize = reportSpec.getGroupFooterFontSize();
    int groupFooterFontStyle = reportSpec.getGroupFooterFontStyle();
    RGB groupFooterFontColor = SWTUtility.getRGB(reportSpec.getGroupFooterFontColor());
    FontData groupFooterFontData = new FontData(groupFooterFontName, groupFooterFontSize, groupFooterFontStyle);
    groupFooterSampleLabel.setFont(new Font(getDisplay(), groupFooterFontData));
    groupFooterSampleLabel.setForeground(new Color(getDisplay(), groupFooterFontColor));
    String columnHeaderFontName = reportSpec.getColumnHeaderFontName();
    int columnHeaderFontSize = reportSpec.getColumnHeaderFontSize();
    int columnHeaderFontStyle = reportSpec.getColumnHeaderFontStyle();
    RGB columnHeaderFontColor = SWTUtility.getRGB(reportSpec.getColumnHeaderFontColor());
    FontData columnHeaderFontData = new FontData(columnHeaderFontName, columnHeaderFontSize, columnHeaderFontStyle);
    columnHeaderFontSampleLabel.setFont(new Font(getDisplay(), columnHeaderFontData));
    columnHeaderFontSampleLabel.setForeground(new Color(getDisplay(), columnHeaderFontColor));
    String itemsFontName = reportSpec.getItemsFontName();
    int itemsFontSize = reportSpec.getItemsFontSize();
    int itemsFontStyle = reportSpec.getItemsFontStyle();
    RGB itemsFontColor = SWTUtility.getRGB(reportSpec.getItemsFontColor());
    FontData itemsFontData = new FontData(itemsFontName, itemsFontSize, itemsFontStyle);
    itemsFontSampleLabel.setFont(new Font(getDisplay(), itemsFontData));
    itemsFontSampleLabel.setForeground(new Color(getDisplay(), itemsFontColor));
    rowBandingInitialStateCheckbox.setSelection(reportSpec.getRowBandingInitialState());
    rowBandingInitialStateCheckbox.setEnabled(bandingCheckbox.getSelection() && wizardManager.isContinueAllowed());
    if (reportSpec.getIncludeSrc() != null) {
      includeText.setText(reportSpec.getIncludeSrc());
    }
//    useChartCheckBox.setSelection(reportDesigner.getReportSpec().getUseChart());
    generateReportLevelColumnHeadersButton.setSelection(reportDesigner.getReportSpec().getGenerateReportLevelColumnHeaders());
    layout();
  }

  /**
   * 
   * 
   * @param dirty
   */
  public void dirtyFired(boolean dirty) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDoubleClick(MouseEvent e) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDown(MouseEvent e) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseEnter(MouseEvent e) {
    Cursor cursor = new Cursor(getDisplay(), SWT.CURSOR_HAND);
    ((Control) e.widget).setCursor(cursor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseExit(MouseEvent e) {
    Cursor cursor = new Cursor(getDisplay(), SWT.CURSOR_ARROW);
    ((Control) e.widget).setCursor(cursor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseHover(MouseEvent e) {
  }

  public void modifyText(ModifyEvent e) {
    stateChanged = true;
    dirty = true;
    fireDirtyEvent();
    reportDesigner.getReportSpec().setTemplateSrc(includeText.getText());
  }
}
