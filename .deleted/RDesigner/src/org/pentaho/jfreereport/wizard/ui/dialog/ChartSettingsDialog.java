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
 * @created May 22, 2006
 * @author Michael D'Amour
 */
package org.pentaho.jfreereport.wizard.ui.dialog;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.pentaho.jfreereport.castormodel.reportspec.Chart;
import org.pentaho.jfreereport.castormodel.reportspec.Series;
import org.pentaho.jfreereport.castormodel.reportspec.types.ChartType;
import org.pentaho.jfreereport.castormodel.reportspec.types.LegendLocation;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTButton;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportSpecUtility;

public class ChartSettingsDialog implements SelectionListener, KeyListener, MouseListener, MouseTrackListener {
  String title;
  Chart chart;
  Shell dialog;
  Combo chartTypeCombo = null;
  Text chartTitleText = null;
  Spinner heightSpinner = null;
  Spinner widthSpinner = null;
  Button showLegendButton = null;
  Button drawLegendBorderButton = null;
  Button showBorderButton = null;
  Button isHorizontalButton = null;
  Button is3DButton = null;
  Button isStackedButton = null;
  Button isSummaryOnlyButton = null;
  Combo categoryCombo = null;
  Combo resetGroupCombo = null;
  String columns[] = null;
  String groups[] = null;
  Combo legendLocationCombo = null;
  SWTButton backgroundButton = null;
  List seriesList = null;
  List valuesList = null;
  Spinner offsetSpinner = null;
  Spinner labelRotationSpinner = null;
  Button addValuesButton = null;
  Button addSeriesButton = null;
  Button removeValueButton = null;
  Button removeSeriesButton = null;
  Button valuesupButton = null;
  Button valuesdownButton = null;
  Button seriesupButton = null;
  Button seriesdownButton = null;

  public ChartSettingsDialog(String title, Chart chart, String columns[], String groups[]) {
    this.title = title;
    this.chart = chart;
    this.columns = columns;
    this.groups = new String[groups.length+1];
    this.groups[0] = Messages.getString("ChartSettingsDialog.0"); //$NON-NLS-1$
    for (int i=0;i<groups.length;i++) {
      this.groups[i+1] = groups[i];
    }
    init();
  }

  public void open() {
    dialog.open();
    while (!dialog.isDisposed()) {
      if (!dialog.getDisplay().readAndDispatch())
        dialog.getDisplay().sleep();
    }
  }

  public void fillLeftContent(Composite leftContent) {
    // chart title label
    Label chartTitleLabel = new Label(leftContent, SWT.NONE);
    chartTitleLabel.setText(Messages.getString("ChartSettingsDialog.1")); //$NON-NLS-1$
    GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    chartTitleLabel.setLayoutData(gridData);
    chartTitleText = new Text(leftContent, SWT.BORDER);
    if (chart.getTitle() != null) {
      chartTitleText.setText(chart.getTitle());
    }
    chartTitleText.addKeyListener(this);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    chartTitleText.setLayoutData(gridData);
    // chart type label
    Label chartTypeLabel = new Label(leftContent, SWT.NONE);
    chartTypeLabel.setText(Messages.getString("ChartSettingsDialog.2")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    chartTypeLabel.setLayoutData(gridData);
    // chart type combo
    chartTypeCombo = new Combo(leftContent, SWT.READ_ONLY);
    String chartTypes[] = ReportSpecUtility.enumerationToStringArray(ChartType.enumerate());
    Arrays.sort(chartTypes);
    chartTypeCombo.setItems(chartTypes);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    chartTypeCombo.setLayoutData(gridData);
    chartTypeCombo.addSelectionListener(this);
    if (chart.getType() == null) {
      chartTypeCombo.select(0);
    } else {
      chartTypeCombo.select(chartTypeCombo.indexOf(chart.getType().toString()));
    }
    // category
    Label categoryLabel = new Label(leftContent, SWT.NONE);
    categoryLabel.setText(Messages.getString("ChartSettingsDialog.3")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    categoryLabel.setLayoutData(gridData);
    categoryCombo = new Combo(leftContent, SWT.READ_ONLY);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    categoryCombo.setLayoutData(gridData);
    categoryCombo.addSelectionListener(this);
    categoryCombo.setItems(columns);
    if (chart.getCategoryColumn() == null) {
      categoryCombo.select(0);
      chart.setCategoryColumn(categoryCombo.getItem(categoryCombo.getSelectionIndex()));
    } else {
      categoryCombo.select(categoryCombo.indexOf(chart.getCategoryColumn()));
    }
    // reset-group
    Label resetGroupLabel = new Label(leftContent, SWT.NONE);
    resetGroupLabel.setText(Messages.getString("ChartSettingsDialog.4")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    resetGroupLabel.setLayoutData(gridData);
    resetGroupCombo = new Combo(leftContent, SWT.READ_ONLY);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    resetGroupCombo.setLayoutData(gridData);
    resetGroupCombo.addSelectionListener(this);
    resetGroupCombo.setItems(groups);
    if (chart.getResetGroup() != null) {
      resetGroupCombo.select(resetGroupCombo.indexOf(chart.getResetGroup()));
    }
    // height
    Label heightLabel = new Label(leftContent, SWT.NONE);
    heightLabel.setText(Messages.getString("ChartSettingsDialog.5")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    heightLabel.setLayoutData(gridData);
    heightSpinner = new Spinner(leftContent, SWT.BORDER);
    heightSpinner.setMinimum(0);
    heightSpinner.setMaximum(99999);
    heightSpinner.setSelection(chart.getHeight());
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    heightSpinner.setLayoutData(gridData);
    heightSpinner.addSelectionListener(this);
    // width
    Label widthLabel = new Label(leftContent, SWT.NONE);
    widthLabel.setText(Messages.getString("ChartSettingsDialog.6")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    widthLabel.setLayoutData(gridData);
    widthSpinner = new Spinner(leftContent, SWT.BORDER);
    widthSpinner.setMinimum(0);
    widthSpinner.setMaximum(99999);
    widthSpinner.setSelection(chart.getWidth());
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    widthSpinner.setLayoutData(gridData);
    widthSpinner.addSelectionListener(this);
    // horizontal offset
    Label offsetLabel = new Label(leftContent, SWT.NONE);
    offsetLabel.setText(Messages.getString("ChartSettingsDialog.7")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    offsetLabel.setLayoutData(gridData);
    offsetSpinner = new Spinner(leftContent, SWT.BORDER);
    offsetSpinner.setMinimum(0);
    offsetSpinner.setMaximum(99999);
    offsetSpinner.setSelection(chart.getHorizontalOffset());
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    offsetSpinner.setLayoutData(gridData);
    offsetSpinner.addSelectionListener(this);
    // show legend
    showLegendButton = new Button(leftContent, SWT.CHECK);
    showLegendButton.setText(Messages.getString("ChartSettingsDialog.8")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    showLegendButton.setLayoutData(gridData);
    showLegendButton.setSelection(chart.getShowLegend());
    showLegendButton.addSelectionListener(this);
    // draw legend border
    drawLegendBorderButton = new Button(leftContent, SWT.CHECK);
    drawLegendBorderButton.setText(Messages.getString("ChartSettingsDialog.9")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    drawLegendBorderButton.setLayoutData(gridData);
    drawLegendBorderButton.setSelection(chart.getDrawLegendBorder());
    drawLegendBorderButton.addSelectionListener(this);
    // legend location
    Label legendLocationLabel = new Label(leftContent, SWT.NONE);
    legendLocationLabel.setText(Messages.getString("ChartSettingsDialog.10")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    legendLocationLabel.setLayoutData(gridData);
    legendLocationCombo = new Combo(leftContent, SWT.READ_ONLY);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    legendLocationCombo.setLayoutData(gridData);
    legendLocationCombo.addSelectionListener(this);
    String legendLocationTypes[] = ReportSpecUtility.enumerationToStringArray(LegendLocation.enumerate());
    Arrays.sort(legendLocationTypes);
    legendLocationCombo.setItems(legendLocationTypes);
    if (chart.getLegendLocation() == null) {
      legendLocationCombo.select(0);
    } else {
      legendLocationCombo.select(legendLocationCombo.indexOf(chart.getLegendLocation().toString()));
    }
    // label rotation
    Label rotationLabel = new Label(leftContent, SWT.NONE);
    rotationLabel.setText(Messages.getString("ChartSettingsDialog.11")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    rotationLabel.setLayoutData(gridData);
    labelRotationSpinner = new Spinner(leftContent, SWT.BORDER);
    labelRotationSpinner.setMinimum(0);
    labelRotationSpinner.setMaximum(360);
    labelRotationSpinner.setSelection(chart.getLabelRotation());
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    labelRotationSpinner.setLayoutData(gridData);
    labelRotationSpinner.addSelectionListener(this);
    
    
    // show border
    showBorderButton = new Button(leftContent, SWT.CHECK);
    showBorderButton.setText(Messages.getString("ChartSettingsDialog.12")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    showBorderButton.setLayoutData(gridData);
    showBorderButton.setSelection(chart.getShowBorder());
    showBorderButton.addSelectionListener(this);
    // is horizontal
    isHorizontalButton = new Button(leftContent, SWT.CHECK);
    isHorizontalButton.setText(Messages.getString("ChartSettingsDialog.13")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    isHorizontalButton.setLayoutData(gridData);
    isHorizontalButton.setSelection(chart.getIsHorizontal());
    isHorizontalButton.addSelectionListener(this);
    // is 3D
    is3DButton = new Button(leftContent, SWT.CHECK);
    is3DButton.setText(Messages.getString("ChartSettingsDialog.14")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    is3DButton.setLayoutData(gridData);
    is3DButton.setSelection(chart.getIsThreeDimensional());
    is3DButton.addSelectionListener(this);
    // is stacked
    isStackedButton = new Button(leftContent, SWT.CHECK);
    isStackedButton.setText(Messages.getString("ChartSettingsDialog.15")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    isStackedButton.setLayoutData(gridData);
    isStackedButton.setSelection(chart.getIsStacked());
    isStackedButton.addSelectionListener(this);
    // is summary only
    isSummaryOnlyButton = new Button(leftContent, SWT.CHECK);
    isSummaryOnlyButton.setText(Messages.getString("ChartSettingsDialog.16")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    isSummaryOnlyButton.setLayoutData(gridData);
    isSummaryOnlyButton.setSelection(chart.getIsSummaryOnly());
    isSummaryOnlyButton.addSelectionListener(this);
    // background color
    if (chart.getBackgroundColor() == null) {
      chart.setBackgroundColor("#C0C0C0"); //$NON-NLS-1$
    }
    RGB color = SWTUtility.getRGB(chart.getBackgroundColor());
    Label backgroundLabel = new Label(leftContent, SWT.NONE);
    backgroundLabel.setText(Messages.getString("ChartSettingsDialog.18")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    backgroundLabel.setLayoutData(gridData);
    backgroundButton = new SWTButton(leftContent, SWT.NONE);
    backgroundButton.setBackground(new Color(dialog.getDisplay(), color));
    backgroundButton.setEtchedColors(new Color(dialog.getDisplay(), new RGB(0, 0, 0)), new Color(dialog.getDisplay(), new RGB(192, 192, 192)), new Color(dialog.getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.heightHint = 15;
    gridData.widthHint = 15;
    backgroundButton.setLayoutData(gridData);
    backgroundButton.addMouseListener(this);
    backgroundButton.addMouseTrackListener(this);
  }

  public void fillRightContent(Composite rightContent) {
    Label seriesLabel = new Label(rightContent, SWT.NONE);
    GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    gridData.horizontalSpan = 2;
    seriesLabel.setLayoutData(gridData);
    seriesLabel.setText("Series");

    seriesList = new List(rightContent, SWT.BORDER | SWT.MULTI);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    seriesList.setLayoutData(gridData);
    seriesList.setItems(ReportSpecUtility.getSeriesColumns(chart));
    seriesList.addMouseListener(this);    
    
    Composite seriesUpDownComposite = new Composite(rightContent, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    seriesUpDownComposite.setLayoutData(gridData);
    seriesUpDownComposite.setLayout(new GridLayout(1, false));
    addSeriesButton = new Button(seriesUpDownComposite, SWT.PUSH);
    addSeriesButton.setText("Add.."); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    addSeriesButton.setLayoutData(gridData);
    addSeriesButton.addSelectionListener(this);
    removeSeriesButton = new Button(seriesUpDownComposite, SWT.PUSH);
    removeSeriesButton.setText("Remove.."); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    removeSeriesButton.setLayoutData(gridData);
    removeSeriesButton.addSelectionListener(this);
    Label upButtonSpacerLabel = new Label(seriesUpDownComposite, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
    upButtonSpacerLabel.setLayoutData(gridData);
    seriesupButton = new Button(seriesUpDownComposite, SWT.PUSH);
    seriesupButton.setText("^"); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    seriesupButton.setLayoutData(gridData);
    seriesupButton.addSelectionListener(this);
    seriesdownButton = new Button(seriesUpDownComposite, SWT.PUSH);
    seriesdownButton.setText("v"); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    seriesdownButton.setLayoutData(gridData);
    seriesdownButton.addSelectionListener(this);
    Label downButtonSpacerLabel = new Label(seriesUpDownComposite, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
    downButtonSpacerLabel.setLayoutData(gridData);
    
    Label valuesLabel = new Label(rightContent, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    gridData.horizontalSpan = 2;
    valuesLabel.setLayoutData(gridData);
    valuesLabel.setText("Values");
    
    valuesList = new List(rightContent, SWT.BORDER | SWT.MULTI);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    valuesList.setLayoutData(gridData);
    valuesList.setItems(chart.getValuesColumn());
    valuesList.addMouseListener(this);    
    
    Composite valuesUpDownComposite = new Composite(rightContent, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    valuesUpDownComposite.setLayoutData(gridData);
    valuesUpDownComposite.setLayout(new GridLayout(1, false));
    addValuesButton = new Button(valuesUpDownComposite, SWT.PUSH);
    addValuesButton.setText("Add.."); //$NON-NLS-1$
    addValuesButton.addSelectionListener(this);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    addValuesButton.setLayoutData(gridData);
    removeValueButton = new Button(valuesUpDownComposite, SWT.PUSH);
    removeValueButton.setText("Remove.."); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    removeValueButton.setLayoutData(gridData);
    removeValueButton.addSelectionListener(this);
    
    Label valuesupButtonSpacerLabel = new Label(valuesUpDownComposite, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
    valuesupButtonSpacerLabel.setLayoutData(gridData);
    valuesupButton = new Button(valuesUpDownComposite, SWT.PUSH);
    valuesupButton.setText("^"); //$NON-NLS-1$
    valuesupButton.addSelectionListener(this);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    valuesupButton.setLayoutData(gridData);
    valuesdownButton = new Button(valuesUpDownComposite, SWT.PUSH);
    valuesdownButton.setText("v"); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    valuesdownButton.setLayoutData(gridData);
    valuesdownButton.addSelectionListener(this);
    Label valuesdownButtonSpacerLabel = new Label(valuesUpDownComposite, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
    valuesdownButtonSpacerLabel.setLayoutData(gridData);
  }

  public void fillButtonPanel(Composite buttonPanel) {
    // left spacer for ok button
    Label left = new Label(buttonPanel, SWT.NONE);
    GridData gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
    left.setLayoutData(gridData);
    gridData = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
    final PentahoSWTButton ok = new PentahoSWTButton(buttonPanel, SWT.PUSH, gridData, PentahoSWTButton.NORMAL, Messages.getString("ChartSettingsDialog.30")); //$NON-NLS-1$
    ok.setLayoutData(gridData);
    ok.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        // before we get out of this dialog, we should check that the series & values both have the same number of items
        if (seriesList.getItemCount() == valuesList.getItemCount()) {
          dialog.close();
        } else {
          // uh oh, don't exit, warn user
          PentahoSWTMessageBox mb = new PentahoSWTMessageBox("Warning", "Series and Values must contain the same number of items.", 200, 120);
          mb.open();
        }
      }
    });
    SWTUtility.setBackground(dialog, ReportWizard.background);
    // dialog.pack();
  }

  public void init() {
    dialog = SWTUtility.createModalDialogShell(700, 600, title);
    dialog.setLayout(new GridLayout(2, false));
    Composite leftContent = new Composite(dialog, SWT.NONE);
    leftContent.setLayout(new GridLayout(2, false));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    leftContent.setLayoutData(gridData);
    Composite rightContent = new Composite(dialog, SWT.NONE);
    rightContent.setLayout(new GridLayout(2, false));
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    rightContent.setLayoutData(gridData);
    fillLeftContent(leftContent);
    fillRightContent(rightContent);
    Composite buttonPanel = new Composite(dialog, SWT.NONE);
    buttonPanel.setLayout(new GridLayout(3, false));
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
    gridData.horizontalSpan = 2;
    buttonPanel.setLayoutData(gridData);
    fillButtonPanel(buttonPanel);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetSelected(SelectionEvent e) {
    if (e.widget == chartTypeCombo) {
      chart.setType(ChartType.valueOf(chartTypeCombo.getItem(chartTypeCombo.getSelectionIndex())));
    } else if (e.widget == heightSpinner) {
      chart.setHeight(heightSpinner.getSelection());
    } else if (e.widget == widthSpinner) {
      chart.setWidth(widthSpinner.getSelection());
    } else if (e.widget == offsetSpinner) {
      chart.setHorizontalOffset(offsetSpinner.getSelection());
    } else if (e.widget == labelRotationSpinner) {
      chart.setLabelRotation(labelRotationSpinner.getSelection());
    } else if (e.widget == showLegendButton) {
      chart.setShowLegend(showLegendButton.getSelection());
    } else if (e.widget == drawLegendBorderButton) {
      chart.setDrawLegendBorder(drawLegendBorderButton.getSelection());
    } else if (e.widget == showBorderButton) {
      chart.setShowBorder(showBorderButton.getSelection());
    } else if (e.widget == isHorizontalButton) {
      chart.setIsHorizontal(isHorizontalButton.getSelection());
    } else if (e.widget == is3DButton) {
      chart.setIsThreeDimensional(is3DButton.getSelection());
    } else if (e.widget == isStackedButton) {
      chart.setIsStacked(isStackedButton.getSelection());
    } else if (e.widget == isSummaryOnlyButton) {
      chart.setIsSummaryOnly(isSummaryOnlyButton.getSelection());
    } else if (e.widget == categoryCombo) {
      chart.setCategoryColumn(categoryCombo.getItem(categoryCombo.getSelectionIndex()));
    } else if (e.widget == resetGroupCombo) {
      if (!resetGroupCombo.getItem(resetGroupCombo.getSelectionIndex()).equals("<none>")) { //$NON-NLS-1$
        chart.setResetGroup(resetGroupCombo.getItem(resetGroupCombo.getSelectionIndex()));
      }
    } else if (e.widget == legendLocationCombo) {
      chart.setLegendLocation(LegendLocation.valueOf(legendLocationCombo.getItem(legendLocationCombo.getSelectionIndex())));
    } else if (e.widget == valuesupButton) {
      SWTUtility.moveSelectedItems(valuesList, true);
      chart.removeAllValuesColumn();
      for (int i=0;i<valuesList.getItemCount();i++) {
        chart.addValuesColumn(valuesList.getItem(i));
      }
    } else if (e.widget == valuesdownButton) {
      SWTUtility.moveSelectedItems(valuesList, false);
      chart.removeAllValuesColumn();
      for (int i=0;i<valuesList.getItemCount();i++) {
        chart.addValuesColumn(valuesList.getItem(i));
      }
    } else if (e.widget == seriesupButton) {
      SWTUtility.moveSelectedItems(seriesList, true);
      chart.removeAllSeries();
      for (int i=0;i<seriesList.getItemCount();i++) {
        Series s = new Series();
        s.setSeriesName(seriesList.getItem(i));
        chart.addSeries(s);
      }
    } else if (e.widget == seriesdownButton) {
      SWTUtility.moveSelectedItems(seriesList, false);
      chart.removeAllSeries();
      for (int i=0;i<seriesList.getItemCount();i++) {
        Series s = new Series();
        s.setSeriesName(seriesList.getItem(i));
        chart.addSeries(s);
      }
    } else if (e.widget == addValuesButton) {
      // popup available columns dialog
      ListInputDialog listInputDialog = new ListInputDialog(Messages.getString("GroupAndDetailPanel.14"), columns, 320, 400); //$NON-NLS-1$
      String[] selectedItems = listInputDialog.open();
      for (int i = 0; (selectedItems != null) && (i < selectedItems.length); i++) {
        valuesList.add(selectedItems[i]);
        chart.addValuesColumn(selectedItems[i]);
      }
    } else if (e.widget == addSeriesButton) {
      // popup available columns dialog
      ListInputDialog listInputDialog = new ListInputDialog(Messages.getString("GroupAndDetailPanel.14"), columns, 320, 400); //$NON-NLS-1$
      String[] selectedItems = listInputDialog.open();
      for (int i = 0; (selectedItems != null) && (i < selectedItems.length); i++) {
        seriesList.add(selectedItems[i]);
        Series s = new Series();
        s.setSeriesName(selectedItems[i]);
        chart.addSeries(s);
      }
    } else if (e.widget == removeSeriesButton) {
      String[] values = seriesList.getSelection();
      for (int i = 0; (values != null) && (i < values.length); i++) {
        chart.removeSeriesAt(i);
      }
      seriesList.remove(seriesList.getSelectionIndices());
    } else if (e.widget == removeValueButton) {
      String[] values = valuesList.getSelection();
      for (int i = 0; (values != null) && (i < values.length); i++) {
        chart.removeValuesColumn(values[i]);
      }
      valuesList.remove(valuesList.getSelectionIndices());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetDefaultSelected(SelectionEvent arg0) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
   */
  public void keyPressed(KeyEvent arg0) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
   */
  public void keyReleased(KeyEvent e) {
    if (e.widget == chartTitleText) {
      chart.setTitle(chartTitleText.getText());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDoubleClick(MouseEvent e) {
    if (e.widget == seriesList) {
      ColorDialog colorDialog = new ColorDialog(dialog);
      String selection = seriesList.getSelection()[0];
      String colorStr = "#0000FF"; //$NON-NLS-1$
      Series series = null;
      for (int i = 0; i < chart.getSeriesCount(); i++) {
        series = chart.getSeries()[i];
        if (selection.equals(series.getSeriesName())) {
          if (series.getSeriesColor() != null && !series.getSeriesColor().equals("")) { //$NON-NLS-1$
            colorStr = series.getSeriesColor();
          }
          break;
        }
      }
      if (series != null) {
        colorDialog.setRGB(SWTUtility.getRGB(colorStr));
        RGB pickedColor = colorDialog.open();
        if (pickedColor != null) {
          series.setSeriesColor(SWTUtility.getJFreeColorString(pickedColor));
        }
      }
    }
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
   * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseUp(MouseEvent e) {
    if (e.widget == backgroundButton) {
      ColorDialog colorDialog = new ColorDialog(dialog);
      colorDialog.setRGB(SWTUtility.getRGB(chart.getBackgroundColor()));
      RGB pickedColor = colorDialog.open();
      if (pickedColor != null) {
        chart.setBackgroundColor(SWTUtility.getJFreeColorString(pickedColor));
        backgroundButton.setBackground(new Color(dialog.getDisplay(), pickedColor));
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseEnter(MouseEvent e) {
    Cursor cursor = new Cursor(dialog.getDisplay(), SWT.CURSOR_HAND);
    ((Control) e.widget).setCursor(cursor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseExit(MouseEvent e) {
    Cursor cursor = new Cursor(dialog.getDisplay(), SWT.CURSOR_ARROW);
    ((Control) e.widget).setCursor(cursor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseHover(MouseEvent e) {
    // TODO Auto-generated method stub
  }
}
