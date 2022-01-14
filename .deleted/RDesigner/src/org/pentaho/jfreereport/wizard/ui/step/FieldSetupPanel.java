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

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
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
import org.pentaho.jfreereport.wizard.ui.dialog.ListInputDialog;
import org.pentaho.jfreereport.wizard.ui.dialog.TrafficLightDialog;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTLine;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;
import org.pentaho.jfreereport.wizard.utility.connection.ConnectionUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportSpecUtility;

public class FieldSetupPanel extends WizardPanel implements IDirtyListener, MouseListener, MouseTrackListener, ModifyListener {
  /**
   * 
   */
  ReportWizard reportDesigner = null;
  List detailFieldList = null;
  List groupFieldList = null;
  Composite fieldGroup = null;
  Text displayNameText = null;
  Button calculateGroupTotalsCheckBox = null;
  Button breakBeforeHeaderCheckBox = null;
  Button breakAfterHeaderCheckBox = null;
  Button breakBeforeFooterCheckBox = null;
  Button breakAfterFooterCheckBox = null;
  Field selectedField = null;
  Combo horizontalAlignmentCombo = null;
  Combo verticalAlignmentCombo = null;
  Combo expressionCombo = null;
  Combo totalHorizontalAlignmentCombo = null;
  Text formatText = null;
  Button widthLockButton = null;
  Button widthAutoButton = null;
  Spinner widthSpinner = null;
  Button createGroupHeaderButton = null;
  Button useBackgroundButton = null;
  Composite backgroundButton = null;
  Button useGroupFooterBackgroundButton = null;
  Composite groupFooterBackgroundButton = null;
  Group widthGroup = null;
  Button repeatGroupHeadersCheckBox = null;
  Object dragSource = null;
  Text groupTotalText = null;
  Button useTrafficLightingButton = null;
  PentahoSWTButton trafficLightingSettings = null;
  Combo typeCombo = null;
  Button useChartCheckBox = null;
  PentahoSWTButton configureChartButton = null;
  Button showChartAboveGroupHeaderCheckBox = null;
  Text nullStringText = null;
  Button useItemHideButton = null;
  PentahoSWTButton setCalculatedColumnsButton;

  /**
   * Creates a new PageSetupPanel object.
   * 
   * @param parent
   * @param style
   * @param manager
   * @param reportDesigner
   */
  public FieldSetupPanel(Composite parent, int style, WizardManager manager, ReportWizard reportDesigner) {
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
    contentGroup.setLayout(new GridLayout(2, false));
    Label stepLabel = new Label(contentGroup, SWT.NONE);
    stepLabel.setText(Messages.getString("FieldSetupPanel.28")); //$NON-NLS-1$
    GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    stepLabel.setLayoutData(gridData);
    stepLabel.setFont(labelFont);
    Text stepText = new Text(contentGroup, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
    stepText.setText(Messages.getString("FieldSetupPanel.29")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    stepText.setLayoutData(gridData);
    stepText.setBackground(ReportWizard.background);
    stepText.setFont(textFont);
    SWTLine line = new SWTLine(contentGroup, SWT.NONE);
    line.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.BOTTOM, false, false);
    gridData.heightHint = 5;
    gridData.horizontalSpan = 2;
    line.setLayoutData(gridData);
    Composite detailsAndFields = new Composite(contentGroup, SWT.NONE);
    detailsAndFields.setLayout(new GridLayout(1, false));
    gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
    detailsAndFields.setLayoutData(gridData);
    Label detailLabel = new Label(detailsAndFields, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    detailLabel.setLayoutData(gridData);
    detailLabel.setText(Messages.getString("FieldSetupPanel.1")); //$NON-NLS-1$
    detailLabel.setFont(labelFont);
    detailFieldList = new List(detailsAndFields, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    detailFieldList.setItems(ReportSpecUtility.getDetailFieldNames(reportDesigner.getReportSpec()));
    gridData = new GridData(SWT.CENTER, SWT.FILL, false, true);
    gridData.widthHint = 120;
    detailFieldList.setLayoutData(gridData);
    detailFieldList.addSelectionListener(this);
    detailFieldList.addMouseListener(this);
    Label groupLabel = new Label(detailsAndFields, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    groupLabel.setLayoutData(gridData);
    groupLabel.setText(Messages.getString("FieldSetupPanel.0")); //$NON-NLS-1$
    groupLabel.setFont(labelFont);
    groupFieldList = new List(detailsAndFields, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    groupFieldList.setItems(ReportSpecUtility.getGroupFieldNames(reportDesigner.getReportSpec()));
    gridData = new GridData(SWT.LEFT, SWT.FILL, false, true);
    gridData.widthHint = 120;
    groupFieldList.setLayoutData(gridData);
    groupFieldList.addSelectionListener(this);
    groupFieldList.addMouseListener(this);
    fieldGroup = new Composite(contentGroup, SWT.NONE);
    fieldGroup.setLayout(new GridLayout(3, false));
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    fieldGroup.setLayoutData(gridData);
    Transfer[] types = new Transfer[] { TextTransfer.getInstance(), FileTransfer.getInstance() };
    int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
    final DragSource groupSource = new DragSource(groupFieldList, operations);
    final DropTarget groupTarget = new DropTarget(groupFieldList, operations);
    final DragSource detailSource = new DragSource(detailFieldList, operations);
    final DropTarget detailTarget = new DropTarget(detailFieldList, operations);
    groupSource.setTransfer(types);
    groupSource.addDragListener(new DragSourceAdapter() {
      public void dragStart(DragSourceEvent event) {
        event.doit = (groupFieldList.getSelection() != null && groupFieldList.getSelection().length > 0);
        dragSource = groupFieldList;
      }

      public void dragSetData(DragSourceEvent event) {
        event.data = groupFieldList.getItem(groupFieldList.getSelectionIndex());
      }
    });
    detailTarget.setTransfer(types);
    detailTarget.addDropListener(new DropTargetAdapter() {
      public void drop(DropTargetEvent event) {
        if (event.data == null) {
          event.detail = DND.DROP_NONE;
          return;
        }
        if (dragSource == detailFieldList) {
          // drop to ourself
          Point point = new Point(event.x, event.y);
          point = getDisplay().map(null, detailFieldList, point);
          int index = SWTUtility.getListIndexForPoint(detailFieldList, point);
          SWTUtility.moveSelectedItems(detailFieldList, index);
          detailFieldList.setSelection(index);
          detailFieldList.select(index);
          // use the detailFieldList to re-order the report spec fields
          Field fields[] = reportDesigner.getReportSpec().getField();
          Field details[] = new Field[ReportSpecUtility.getDetails(fields).length];
          for (int i = 0; i < details.length; i++) {
            details[i] = ReportSpecUtility.getField(fields, detailFieldList.getItem(i), true);
            reportDesigner.getReportSpec().removeField(details[i]);
            reportDesigner.getReportSpec().addField(details[i]);
          }
          try {
            populateUI(ReportSpecUtility.getField(fields, detailFieldList.getItem(index), true));
          } catch (Exception e) {
          }
          return;
        }
        if (event.detail == DND.DROP_MOVE) {
          Field sourceField = null;
          for (int i = 0; i < groupFieldList.getSelectionIndices().length; i++) {
            sourceField = ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[groupFieldList.getSelectionIndices()[i]];
            reportDesigner.getReportSpec().removeField(sourceField);
            sourceField.setIsDetail(true);
            sourceField.setDisplayName(sourceField.getName());
            reportDesigner.getReportSpec().addField(sourceField);
          }
          populateUI(sourceField);
          groupFieldList.remove(groupFieldList.getSelectionIndices());
        } else if (event.detail == DND.DROP_COPY) {
          Field newField = null;
          for (int i = 0; i < groupFieldList.getSelectionIndices().length; i++) {
            Field sourceField = ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[groupFieldList.getSelectionIndices()[i]];
            newField = new Field();
            newField.setName(sourceField.getName());
            newField.setDisplayName(newField.getName());
            newField.setIsDetail(true);
            reportDesigner.getReportSpec().addField(newField);
          }
          populateUI(newField);
        }
        String item = (String) event.data;
        detailFieldList.add(item);
        detailFieldList.select(detailFieldList.indexOf(item));
        updateState();
      }
    });
    detailSource.setTransfer(types);
    detailSource.addDragListener(new DragSourceAdapter() {
      public void dragStart(DragSourceEvent event) {
        event.doit = (detailFieldList.getSelection() != null && detailFieldList.getSelection().length > 0);
        dragSource = detailFieldList;
      }

      public void dragSetData(DragSourceEvent event) {
        event.data = detailFieldList.getItem(detailFieldList.getSelectionIndex());
      }
    });
    groupTarget.setTransfer(types);
    groupTarget.addDropListener(new DropTargetAdapter() {
      public void drop(DropTargetEvent event) {
        if (event.data == null) {
          event.detail = DND.DROP_NONE;
          return;
        }
        if (dragSource == groupFieldList) {
          // drop to ourself
          Point point = new Point(event.x, event.y);
          point = getDisplay().map(null, groupFieldList, point);
          int index = SWTUtility.getListIndexForPoint(groupFieldList, point);
          SWTUtility.moveSelectedItems(groupFieldList, index);
          groupFieldList.setSelection(index);
          groupFieldList.select(index);
          // use the groupFieldList to re-order the report spec fields
          Field fields[] = reportDesigner.getReportSpec().getField();
          Field groups[] = new Field[ReportSpecUtility.getGroups(fields).length];
          for (int i = 0; i < groups.length; i++) {
            groups[i] = ReportSpecUtility.getField(fields, groupFieldList.getItem(i), false);
            reportDesigner.getReportSpec().removeField(groups[i]);
            reportDesigner.getReportSpec().addField(groups[i]);
          }
          try {
            populateUI(ReportSpecUtility.getField(fields, groupFieldList.getItem(index), false));
          } catch (Exception e) {
          }
          return;
        }
        if (event.detail == DND.DROP_MOVE) {
          Field sourceField = null;
          for (int i = 0; i < detailFieldList.getSelectionIndices().length; i++) {
            sourceField = ReportSpecUtility.getDetails(reportDesigner.getReportSpec().getField())[detailFieldList.getSelectionIndices()[i]];
            reportDesigner.getReportSpec().removeField(sourceField);
            sourceField.setIsDetail(false);
            sourceField.setDisplayName(sourceField.getName() + ": $(" + sourceField.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
            sourceField.setGroupTotalsLabel("$(" + sourceField.getName() + ") Total"); //$NON-NLS-1$ //$NON-NLS-2$
            reportDesigner.getReportSpec().addField(sourceField);
          }
          detailFieldList.remove(detailFieldList.getSelectionIndices());
          populateUI(sourceField);
        } else if (event.detail == DND.DROP_COPY) {
          Field newField = null;
          for (int i = 0; i < detailFieldList.getSelectionIndices().length; i++) {
            Field sourceField = ReportSpecUtility.getDetails(reportDesigner.getReportSpec().getField())[detailFieldList.getSelectionIndices()[i]];
            newField = new Field();
            newField.setName(sourceField.getName());
            newField.setIsDetail(false);
            newField.setDisplayName(sourceField.getName() + ": $(" + sourceField.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
            newField.setGroupTotalsLabel("$(" + newField.getName() + ") Total"); //$NON-NLS-1$ //$NON-NLS-2$
            reportDesigner.getReportSpec().addField(newField);
          }
          populateUI(newField);
        }
        String item = (String) event.data;
        groupFieldList.add(item);
        groupFieldList.select(groupFieldList.indexOf(item));
        updateState();
      }
    });
    SWTUtility.setBackground(getMainPanel(), ReportWizard.background);
    updateState();
  }

  public void widgetSelected(SelectionEvent e) {
    if (e.widget != detailFieldList && e.widget != groupFieldList) {
      super.widgetSelected(e);
    }
    if (e.widget == detailFieldList || e.widget == groupFieldList) {
      if (e.widget == detailFieldList) {
        groupFieldList.deselectAll();
      } else {
        detailFieldList.deselectAll();
      }
      // create UI given selected item, field or group
      int index = ((List) e.widget).getSelectionIndex();
      if (index >= 0) {
        try {
          Field f = e.widget == detailFieldList ? ReportSpecUtility.getDetails(reportDesigner.getReportSpec().getField())[index] : ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[index];
          populateUI(f);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    } else if (e.widget == setCalculatedColumnsButton) {
      Object[] cols = null;
      try {
        cols = ConnectionUtility.getColumns(reportDesigner.getPentahoConnection(), reportDesigner.getReportSpec().getQuery(), reportDesigner.getReportSpec().getReportSpecChoice().getXqueryUrl());
      } catch (Exception ex) {
        MessageBox mb = new MessageBox(getShell(), SWT.OK);
        mb.setMessage(ex.getMessage());
        mb.setText("Error");
        mb.open();
      }
      String allColumns[] = null;
      if (cols != null) {
        allColumns = new String[cols.length];
        for (int i = 0; i < cols.length; i++) {
          allColumns[i] = (String) cols[i];
        }
      } else {
        allColumns = new String[0];
      }
      ListInputDialog listInputDialog = new ListInputDialog(Messages.getString("GroupAndDetailPanel.14"), allColumns, 320, 200); //$NON-NLS-1$
      String[] selectedItems = listInputDialog.open();
      selectedField.removeAllCalculatedColumns();
      selectedField.setCalculatedColumns(selectedItems);
      populateUI(selectedField);
    } else if (e.widget == calculateGroupTotalsCheckBox) {
      selectedField.setCalculateGroupTotals(calculateGroupTotalsCheckBox.getSelection());
      groupTotalText.setEnabled(calculateGroupTotalsCheckBox.getSelection());
    } else if (e.widget == createGroupHeaderButton) {
      selectedField.setCreateGroupHeader(createGroupHeaderButton.getSelection());
    } else if (e.widget == breakBeforeHeaderCheckBox) {
      selectedField.setPageBreakBeforeHeader(breakBeforeHeaderCheckBox.getSelection());
    } else if (e.widget == breakAfterHeaderCheckBox) {
      selectedField.setPageBreakAfterHeader(breakAfterHeaderCheckBox.getSelection());
    } else if (e.widget == breakBeforeFooterCheckBox) {
      selectedField.setPageBreakBeforeFooter(breakBeforeFooterCheckBox.getSelection());
    } else if (e.widget == breakAfterFooterCheckBox) {
      selectedField.setPageBreakAfterFooter(breakAfterFooterCheckBox.getSelection());
    } else if (e.widget == horizontalAlignmentCombo) {
      selectedField.setHorizontalAlignment(horizontalAlignmentCombo.getItem(horizontalAlignmentCombo.getSelectionIndex()));
    } else if (e.widget == verticalAlignmentCombo) {
      selectedField.setVerticalAlignment(verticalAlignmentCombo.getItem(verticalAlignmentCombo.getSelectionIndex()));
    } else if (e.widget == expressionCombo) {
      selectedField.setExpression(expressionCombo.getItem(expressionCombo.getSelectionIndex()));
    } else if (e.widget == widthSpinner) {
      selectedField.setWidth(new BigDecimal(widthSpinner.getSelection()));
    } else if (e.widget == widthAutoButton) {
      selectedField.setWidthLocked(!widthAutoButton.getSelection());
      widthSpinner.setEnabled(false);
    } else if (e.widget == widthLockButton) {
      selectedField.setWidthLocked(widthLockButton.getSelection());
      widthSpinner.setEnabled(true);
    } else if (e.widget == repeatGroupHeadersCheckBox) {
      selectedField.setRepeatGroupHeader(repeatGroupHeadersCheckBox.getSelection());
    } else if (e.widget == useBackgroundButton) {
      backgroundButton.setEnabled(useBackgroundButton.getSelection());
      backgroundButton.redraw();
      backgroundButton.update();
      if (useBackgroundButton.getSelection()) {
        ColorDialog colorDialog = new ColorDialog(getShell());
        colorDialog.setRGB(SWTUtility.getRGB(selectedField.getBackgroundColor()));
        RGB pickedColor = colorDialog.open();
        // get all selected fields, apply to all
        int[] details = detailFieldList.getSelectionIndices();
        if (details != null) {
          for (int i = 0; i < details.length; i++) {
            int index = details[i];
            updateBackgroundColor(ReportSpecUtility.getDetails(reportDesigner.getReportSpec().getField())[index], pickedColor);
          }
        }
        int[] groups = groupFieldList.getSelectionIndices();
        if (groups != null) {
          for (int i = 0; i < groups.length; i++) {
            int index = groups[i];
            updateBackgroundColor(ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[index], pickedColor);
          }
        }
      } else {
        // get all selected fields, apply to all
        int[] details = detailFieldList.getSelectionIndices();
        if (details != null) {
          for (int i = 0; i < details.length; i++) {
            int index = details[i];
            ReportSpecUtility.getDetails(reportDesigner.getReportSpec().getField())[index].setUseBackgroundColor(false);
          }
        }
        int[] groups = groupFieldList.getSelectionIndices();
        if (groups != null) {
          for (int i = 0; i < groups.length; i++) {
            int index = groups[i];
            ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[index].setUseBackgroundColor(false);
          }
        }
      }
    } else if (e.widget == useGroupFooterBackgroundButton) {
      groupFooterBackgroundButton.setEnabled(useGroupFooterBackgroundButton.getSelection());
      groupFooterBackgroundButton.redraw();
      groupFooterBackgroundButton.update();
      if (useGroupFooterBackgroundButton.getSelection()) {
        groupFooterBackgroundButton.setEnabled(useGroupFooterBackgroundButton.getSelection());
        ColorDialog colorDialog = new ColorDialog(getShell());
        colorDialog.setRGB(SWTUtility.getRGB(selectedField.getGroupFooterBackgroundColor()));
        RGB pickedColor = colorDialog.open();
        if (pickedColor != null) {
          groupFooterBackgroundButton.setBackground(new Color(getDisplay(), pickedColor));
        }
        // get all selected fields, apply to all
        int[] groups = groupFieldList.getSelectionIndices();
        if (groups != null) {
          for (int i = 0; i < groups.length; i++) {
            int index = groups[i];
            Field f = ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[index];
            f.setGroupFooterBackgroundColor(SWTUtility.getJFreeColorString(pickedColor));
            f.setUseGroupFooterBackgroundColor(true);
          }
        }
      } else {
        // get all selected fields, apply to all
        int[] groups = groupFieldList.getSelectionIndices();
        if (groups != null) {
          for (int i = 0; i < groups.length; i++) {
            int index = groups[i];
            Field f = ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[index];
            f.setUseGroupFooterBackgroundColor(false);
          }
        }
      }
    } else if (e.widget == totalHorizontalAlignmentCombo) {
      selectedField.setGroupTotalsHorizontalAlignment(totalHorizontalAlignmentCombo.getItem(totalHorizontalAlignmentCombo.getSelectionIndex()));
    } else if (e.widget == useTrafficLightingButton) {
      selectedField.setUseTrafficLighting(useTrafficLightingButton.getSelection());
      trafficLightingSettings.setEnabled(useTrafficLightingButton.getSelection());
    } else if (e.widget == trafficLightingSettings) {
      // handle TL settings
      TrafficLightDialog tld = new TrafficLightDialog(600, 400, Messages.getString("FieldSetupPanel.30"), selectedField); //$NON-NLS-1$
      tld.open();
    } else if (e.widget == typeCombo) {
      if (typeCombo.getItem(typeCombo.getSelectionIndex()).equals(Messages.getString("FieldSetupPanel.54"))) { //$NON-NLS-1$
        selectedField.setType(Types.NUMERIC);
      } else if (typeCombo.getItem(typeCombo.getSelectionIndex()).equals(Messages.getString("FieldSetupPanel.55"))) { //$NON-NLS-1$
        selectedField.setType(Types.DATE);
      } else {
        selectedField.setType(Types.VARCHAR);
      }
      populateUI(selectedField);
    } else if (e.widget == useChartCheckBox) {
      selectedField.setUseChart(useChartCheckBox.getSelection());
      ensureChart();
      selectedField.getChart().setGroup(selectedField.getName());
    } else if (e.widget == showChartAboveGroupHeaderCheckBox) {
      ensureChart();
      selectedField.setShowChartAboveGroupHeader(showChartAboveGroupHeaderCheckBox.getSelection());
    } else if (e.widget == configureChartButton) {
      ensureChart();
      ChartSettingsDialog dialog = new ChartSettingsDialog(
          Messages.getString("FieldSetupPanel.64"), selectedField.getChart(), ReportSpecUtility.getFieldNames(reportDesigner.getReportSpec()), ReportSpecUtility.getGroupFieldNames(reportDesigner.getReportSpec())); //$NON-NLS-1$
      dialog.open();
    } else if (e.widget == useItemHideButton) {
      selectedField.setUseItemHide(useItemHideButton.getSelection());
    }
  }

  public void ensureChart() {
    Chart chart = selectedField.getChart();
    if (chart == null) {
      chart = new Chart();
      chart.setType(ChartType.BAR);
      chart.setGroup(selectedField.getName());
      selectedField.setChart(chart);
    }
  }

  public void updateBackgroundColor(Field f, RGB pickedColor) {
    f.setUseBackgroundColor(useBackgroundButton.getSelection());
    backgroundButton.setEnabled(useBackgroundButton.getSelection());
    backgroundButton.redraw();
    backgroundButton.update();
    if (useBackgroundButton.getSelection()) {
      if (pickedColor != null) {
        f.setBackgroundColor(SWTUtility.getJFreeColorString(pickedColor));
        backgroundButton.setBackground(new Color(getDisplay(), pickedColor));
      }
    }
  }

  public void populateUI(Field f) {
    selectedField = f;
    SWTUtility.removeChildren(fieldGroup);
    if (f == null) {
      return;
    }
    Composite formatComposite = new Composite(fieldGroup, SWT.NONE);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    formatComposite.setLayout(new GridLayout(1, false));
    formatComposite.setLayoutData(gridData);
    SWTLine vLine = new SWTLine(fieldGroup, SWT.NONE);
    vLine.setHorizontal(false);
    vLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
    gridData.widthHint = 5;
    gridData.verticalSpan = 2;
    vLine.setLayoutData(gridData);
    Composite generalComposite = new Composite(fieldGroup, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.verticalSpan = 2;
    generalComposite.setLayout(new GridLayout(1, false));
    generalComposite.setLayoutData(gridData);
    Label generalLabel = new Label(generalComposite, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.BOTTOM, false, false);
    generalLabel.setLayoutData(gridData);
    generalLabel.setText(Messages.getString("FieldSetupPanel.2")); //$NON-NLS-1$
    generalLabel.setFont(labelFont);
    SWTLine generalLine = new SWTLine(generalComposite, SWT.NONE);
    generalLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.heightHint = 5;
    generalLine.setLayoutData(gridData);
    Label displayNameLabel = new Label(generalComposite, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    displayNameLabel.setLayoutData(gridData);
    displayNameLabel.setText(Messages.getString("FieldSetupPanel.3")); //$NON-NLS-1$
    displayNameText = new Text(generalComposite, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.widthHint = 225;
    displayNameText.setLayoutData(gridData);
    // displayNameText.setBackground(ReportWizard.background);
    displayNameText.setFont(textFont);
    displayNameText.setText(f.getDisplayName());
    displayNameText.addModifyListener(this);
    // create nullstring field
    Label nullStringLabel = new Label(generalComposite, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    nullStringLabel.setLayoutData(gridData);
    nullStringLabel.setText(Messages.getString("FieldSetupPanel.65")); //$NON-NLS-1$
    nullStringText = new Text(generalComposite, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.widthHint = 225;
    nullStringText.setLayoutData(gridData);
    nullStringText.setFont(textFont);
    nullStringText.setText(f.getNullString());
    nullStringText.addModifyListener(this);

    if (f.getIsCalculatedColumn()) {
      // add button to set columns used for this calculated column
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      setCalculatedColumnsButton = new PentahoSWTButton(generalComposite, SWT.NONE, gridData, PentahoSWTButton.NORMAL, "Set Calculated Columns...");
      setCalculatedColumnsButton.setLayoutData(gridData);
      setCalculatedColumnsButton.addSelectionListener(this);
    }

    if (!f.getIsDetail()) {
      createGroupHeaderButton = new Button(generalComposite, SWT.CHECK);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      createGroupHeaderButton.setLayoutData(gridData);
      createGroupHeaderButton.setSelection(f.getCreateGroupHeader());
      createGroupHeaderButton.setText(Messages.getString("FieldSetupPanel.31")); //$NON-NLS-1$
      createGroupHeaderButton.addSelectionListener(this);
      // group page breaking before header
      breakBeforeHeaderCheckBox = new Button(generalComposite, SWT.CHECK);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      breakBeforeHeaderCheckBox.setLayoutData(gridData);
      breakBeforeHeaderCheckBox.setSelection(f.getPageBreakBeforeHeader());
      breakBeforeHeaderCheckBox.setText(Messages.getString("FieldSetupPanel.32")); //$NON-NLS-1$
      breakBeforeHeaderCheckBox.addSelectionListener(this);
      // group page breaking after header
      breakAfterHeaderCheckBox = new Button(generalComposite, SWT.CHECK);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      breakAfterHeaderCheckBox.setLayoutData(gridData);
      breakAfterHeaderCheckBox.setSelection(f.getPageBreakAfterHeader());
      breakAfterHeaderCheckBox.setText(Messages.getString("FieldSetupPanel.33")); //$NON-NLS-1$
      breakAfterHeaderCheckBox.addSelectionListener(this);
      // group page breaking before footer
      breakBeforeFooterCheckBox = new Button(generalComposite, SWT.CHECK);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      breakBeforeFooterCheckBox.setLayoutData(gridData);
      breakBeforeFooterCheckBox.setSelection(f.getPageBreakBeforeFooter());
      breakBeforeFooterCheckBox.setText(Messages.getString("FieldSetupPanel.34")); //$NON-NLS-1$
      breakBeforeFooterCheckBox.addSelectionListener(this);
      // group page breaking after footer
      breakAfterFooterCheckBox = new Button(generalComposite, SWT.CHECK);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      breakAfterFooterCheckBox.setLayoutData(gridData);
      breakAfterFooterCheckBox.setSelection(f.getPageBreakAfterFooter());
      breakAfterFooterCheckBox.setText(Messages.getString("FieldSetupPanel.35")); //$NON-NLS-1$
      breakAfterFooterCheckBox.addSelectionListener(this);
      // group repeating on page break
      repeatGroupHeadersCheckBox = new Button(generalComposite, SWT.CHECK);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      repeatGroupHeadersCheckBox.setLayoutData(gridData);
      repeatGroupHeadersCheckBox.setSelection(f.getRepeatGroupHeader());
      repeatGroupHeadersCheckBox.setText(Messages.getString("FieldSetupPanel.6")); //$NON-NLS-1$
      repeatGroupHeadersCheckBox.addSelectionListener(this);
      calculateGroupTotalsCheckBox = new Button(generalComposite, SWT.CHECK);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      calculateGroupTotalsCheckBox.setLayoutData(gridData);
      calculateGroupTotalsCheckBox.setSelection(f.getCalculateGroupTotals());
      calculateGroupTotalsCheckBox.setText(Messages.getString("FieldSetupPanel.36")); //$NON-NLS-1$
      calculateGroupTotalsCheckBox.addSelectionListener(this);
      Label groupTotalLabel = new Label(generalComposite, SWT.NONE);
      groupTotalLabel.setText(Messages.getString("FieldSetupPanel.27")); //$NON-NLS-1$
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      groupTotalLabel.setLayoutData(gridData);
      groupTotalText = new Text(generalComposite, SWT.SINGLE | SWT.BORDER);
      groupTotalText.setText(f.getGroupTotalsLabel());
      groupTotalText.setEnabled(f.getCalculateGroupTotals());
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.widthHint = 225;
      groupTotalText.setLayoutData(gridData);
      // groupTotalText.setBackground(ReportWizard.background);
      groupTotalText.setFont(textFont);
      groupTotalText.addModifyListener(this);
      Label totalHorizontalAlignmentLabel = new Label(generalComposite, SWT.NONE);
      totalHorizontalAlignmentLabel.setText(Messages.getString("FieldSetupPanel.37")); //$NON-NLS-1$
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      totalHorizontalAlignmentLabel.setLayoutData(gridData);
      totalHorizontalAlignmentCombo = new Combo(generalComposite, SWT.BORDER | SWT.READ_ONLY);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.widthHint = 120;
      totalHorizontalAlignmentCombo.setItems(ReportSpecUtility.getHorizontalAlignmentChoices());
      totalHorizontalAlignmentCombo.select(totalHorizontalAlignmentCombo.indexOf(f.getGroupTotalsHorizontalAlignment())); //$NON-NLS-1$
      totalHorizontalAlignmentCombo.setLayoutData(gridData);
      totalHorizontalAlignmentCombo.addSelectionListener(this);
      RGB color = SWTUtility.getRGB(f.getGroupFooterBackgroundColor());
      Composite backgroundComposite = new Composite(generalComposite, SWT.SHADOW_ETCHED_OUT);
      backgroundComposite.setBackground(ReportWizard.background);
      backgroundComposite.setLayout(new GridLayout(2, false));
      ((GridLayout) backgroundComposite.getLayout()).marginHeight = 0;
      ((GridLayout) backgroundComposite.getLayout()).marginWidth = 0;
      gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
      backgroundComposite.setLayoutData(gridData);
      useGroupFooterBackgroundButton = new Button(backgroundComposite, SWT.CHECK);
      useGroupFooterBackgroundButton.setBackground(ReportWizard.background);
      useGroupFooterBackgroundButton.setText(Messages.getString("FieldSetupPanel.66")); //$NON-NLS-1$
      gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
      useGroupFooterBackgroundButton.setLayoutData(gridData);
      useGroupFooterBackgroundButton.addSelectionListener(this);
      useGroupFooterBackgroundButton.setSelection(selectedField.getUseGroupFooterBackgroundColor());
      groupFooterBackgroundButton = new SWTButton(backgroundComposite, SWT.NONE);
      groupFooterBackgroundButton.setBackground(new Color(getDisplay(), color));
      ((SWTButton) groupFooterBackgroundButton).setEtchedColors(new Color(getDisplay(), new RGB(0, 0, 0)), new Color(getDisplay(), new RGB(192, 192, 192)), new Color(getDisplay(), new RGB(255, 255, 255)));
      groupFooterBackgroundButton.setEnabled(useGroupFooterBackgroundButton.getSelection());
      gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
      gridData.heightHint = 15;
      gridData.widthHint = 15;
      groupFooterBackgroundButton.setLayoutData(gridData);
      groupFooterBackgroundButton.addMouseListener(this);
      groupFooterBackgroundButton.addMouseTrackListener(this);
    }
    // background color
    RGB color = SWTUtility.getRGB(f.getBackgroundColor());
    Composite backgroundComposite = new Composite(generalComposite, SWT.SHADOW_ETCHED_OUT);
    backgroundComposite.setBackground(ReportWizard.background);
    backgroundComposite.setLayout(new GridLayout(2, false));
    ((GridLayout) backgroundComposite.getLayout()).marginHeight = 0;
    ((GridLayout) backgroundComposite.getLayout()).marginWidth = 0;
    gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    backgroundComposite.setLayoutData(gridData);
    useBackgroundButton = new Button(backgroundComposite, SWT.CHECK);
    useBackgroundButton.setBackground(ReportWizard.background);
    useBackgroundButton.setText(Messages.getString("FieldSetupPanel.7")); //$NON-NLS-1$
    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    useBackgroundButton.setLayoutData(gridData);
    useBackgroundButton.addSelectionListener(this);
    useBackgroundButton.setSelection(selectedField.getUseBackgroundColor());
    backgroundButton = new SWTButton(backgroundComposite, SWT.NONE);
    backgroundButton.setBackground(new Color(getDisplay(), color));
    ((SWTButton) backgroundButton).setEtchedColors(new Color(getDisplay(), new RGB(0, 0, 0)), new Color(getDisplay(), new RGB(192, 192, 192)), new Color(getDisplay(), new RGB(255, 255, 255)));
    backgroundButton.setEnabled(useBackgroundButton.getSelection());
    gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    gridData.heightHint = 15;
    gridData.widthHint = 15;
    backgroundButton.setLayoutData(gridData);
    backgroundButton.addMouseListener(this);
    backgroundButton.addMouseTrackListener(this);
    Label formatLabel = new Label(formatComposite, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.BOTTOM, false, false);
    formatLabel.setLayoutData(gridData);
    formatLabel.setText(Messages.getString("FieldSetupPanel.8")); //$NON-NLS-1$
    formatLabel.setFont(labelFont);
    SWTLine formatLine = new SWTLine(formatComposite, SWT.NONE);
    formatLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.heightHint = 5;
    formatLine.setLayoutData(gridData);
    if (f.getIsDetail()) {
      // width
      widthGroup = new Group(formatComposite, SWT.NONE);
      widthGroup.setText(Messages.getString("FieldSetupPanel.9")); //$NON-NLS-1$
      widthGroup.setLayout(new GridLayout(2, false));
      gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
      widthGroup.setLayoutData(gridData);
      widthAutoButton = new Button(widthGroup, SWT.RADIO);
      widthAutoButton.setSelection(!f.getWidthLocked());
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.horizontalSpan = 2;
      widthAutoButton.setLayoutData(gridData);
      widthAutoButton.setText(Messages.getString("FieldSetupPanel.10")); //$NON-NLS-1$
      widthAutoButton.addSelectionListener(this);
      widthLockButton = new Button(widthGroup, SWT.RADIO);
      widthLockButton.setSelection(f.getWidthLocked());
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      widthLockButton.setLayoutData(gridData);
      widthLockButton.setText(Messages.getString("FieldSetupPanel.11")); //$NON-NLS-1$
      widthLockButton.addSelectionListener(this);
      BigDecimal width = f.getWidth();
      widthSpinner = new Spinner(widthGroup, SWT.BORDER);
      widthSpinner.setSelection(width.intValue());
      widthSpinner.setData("column", f.getName()); //$NON-NLS-1$
      widthSpinner.setData("isWidthSpinner", Boolean.TRUE); //$NON-NLS-1$
      gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
      widthSpinner.setLayoutData(gridData);
      widthSpinner.addSelectionListener(this);
      widthSpinner.addModifyListener(this);
      if (!f.getWidthLocked()) {
        widthSpinner.setEnabled(false);
      }
      if (!f.getIsDetail()) {
        SWTUtility.setEnabled(widthGroup, false);
      }
    }
    if (f.getIsDetail()) {
      // use item hide function (used to simulate multiple master-detail reports)
      useItemHideButton = new Button(formatComposite, SWT.CHECK);
      useItemHideButton.setText(Messages.getString("FieldSetupPanel.67")); //$NON-NLS-1$
      useItemHideButton.setSelection(f.getUseItemHide());
      gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
      useItemHideButton.setLayoutData(gridData);
      useItemHideButton.addSelectionListener(this);
      // expression
      Label typeLabel = new Label(formatComposite, SWT.NONE);
      gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
      typeLabel.setLayoutData(gridData);
      typeLabel.setText(Messages.getString("FieldSetupPanel.42")); //$NON-NLS-1$
      typeCombo = new Combo(formatComposite, SWT.NONE | SWT.READ_ONLY);
      typeCombo.setItems(new String[] { Messages.getString("FieldSetupPanel.43"), Messages.getString("FieldSetupPanel.44"), Messages.getString("FieldSetupPanel.45") }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      if (f.getType() == Types.NUMERIC) {
        typeCombo.select(typeCombo.indexOf(Messages.getString("FieldSetupPanel.46"))); //$NON-NLS-1$
      } else if (f.getType() == Types.DATE) {
        typeCombo.select(typeCombo.indexOf(Messages.getString("FieldSetupPanel.47"))); //$NON-NLS-1$
      } else if (f.getType() == Types.VARCHAR) {
        typeCombo.select(typeCombo.indexOf(Messages.getString("FieldSetupPanel.48"))); //$NON-NLS-1$
      }
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      typeCombo.setLayoutData(gridData);
      typeCombo.addSelectionListener(this);
      // expression
      Label expressionLabel = new Label(formatComposite, SWT.NONE);
      gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
      expressionLabel.setLayoutData(gridData);
      expressionLabel.setText(Messages.getString("FieldSetupPanel.14")); //$NON-NLS-1$
      expressionCombo = new Combo(formatComposite, SWT.NONE | SWT.READ_ONLY);
      expressionCombo.setItems(ReportSpecUtility.getExpressionChoices(f.getType()));
      int selIndex = expressionCombo.indexOf(f.getExpression() == null ? Messages.getString("FieldSetupPanel.49") : f.getExpression()); //$NON-NLS-1$
      if (selIndex >= 0) {
        expressionCombo.select(selIndex); //$NON-NLS-1$
      } else {
        expressionCombo.select(0);
      }
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      expressionCombo.setLayoutData(gridData);
      expressionCombo.addSelectionListener(this);
//      if (f.getType() == Types.NUMERIC) {
//        Composite trafficLightingComposite = new Composite(formatComposite, SWT.NONE);
//        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
//        trafficLightingComposite.setLayoutData(gridData);
//        trafficLightingComposite.setLayout(new GridLayout(2, false));
//        useTrafficLightingButton = new Button(trafficLightingComposite, SWT.CHECK);
//        useTrafficLightingButton.setText(Messages.getString("FieldSetupPanel.38")); //$NON-NLS-1$
//        useTrafficLightingButton.setSelection(f.getUseTrafficLighting());
//        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
//        useTrafficLightingButton.setLayoutData(gridData);
//        useTrafficLightingButton.addSelectionListener(this);
//        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
//        trafficLightingSettings = new PentahoSWTButton(trafficLightingComposite, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("FieldSetupPanel.39")); //$NON-NLS-1$
//        trafficLightingSettings.setLayoutData(gridData);
//        trafficLightingSettings.addSelectionListener(this);
//        trafficLightingSettings.setEnabled(useTrafficLightingButton.getSelection());
//      }
    }
    // format
    Label formattingLabel = new Label(formatComposite, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    formattingLabel.setLayoutData(gridData);
    formattingLabel.setText(Messages.getString("FieldSetupPanel.16")); //$NON-NLS-1$
    formatText = new Text(formatComposite, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.widthHint = 225;
    formatText.setLayoutData(gridData);
    // formatText.setBackground(ReportWizard.background);
    formatText.setFont(textFont);
    formatText.setText(f.getFormat());
    formatText.addModifyListener(this);
    // group alignment
    Group alignmentGroup = new Group(formatComposite, SWT.NONE);
    alignmentGroup.setLayout(new GridLayout(1, false));
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.widthHint = 200;
    alignmentGroup.setLayoutData(gridData);
    alignmentGroup.setText(Messages.getString("FieldSetupPanel.17")); //$NON-NLS-1$
    // horizontal alignment
    Label horizontalAlignmentLabel = new Label(alignmentGroup, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    horizontalAlignmentLabel.setLayoutData(gridData);
    horizontalAlignmentLabel.setText(Messages.getString("FieldSetupPanel.18")); //$NON-NLS-1$
    horizontalAlignmentCombo = new Combo(alignmentGroup, SWT.NONE | SWT.READ_ONLY);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    horizontalAlignmentCombo.setLayoutData(gridData);
    horizontalAlignmentCombo.setItems(ReportSpecUtility.getHorizontalAlignmentChoices());
    horizontalAlignmentCombo.select(horizontalAlignmentCombo.indexOf(f.getHorizontalAlignment() == null ? "center" : f.getHorizontalAlignment())); //$NON-NLS-1$
    horizontalAlignmentCombo.addSelectionListener(this);
    // vertical alignment
    Label verticalAlignmentLabel = new Label(alignmentGroup, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    verticalAlignmentLabel.setLayoutData(gridData);
    verticalAlignmentLabel.setText(Messages.getString("FieldSetupPanel.20")); //$NON-NLS-1$
    verticalAlignmentCombo = new Combo(alignmentGroup, SWT.NONE | SWT.READ_ONLY);
    verticalAlignmentCombo.setItems(ReportSpecUtility.getVerticalAlignmentChoices());
    verticalAlignmentCombo.select(verticalAlignmentCombo.indexOf(f.getVerticalAlignment() == null ? "middle" : f.getVerticalAlignment())); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    verticalAlignmentCombo.setLayoutData(gridData);
    verticalAlignmentCombo.addSelectionListener(this);
//    if (!f.getIsDetail()) {
//      Composite chartComposite = new Composite(fieldGroup, SWT.NONE);
//      gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//      chartComposite.setLayout(new GridLayout(1, false));
//      chartComposite.setLayoutData(gridData);
//      Label chartLabel = new Label(chartComposite, SWT.NONE);
//      gridData = new GridData(SWT.LEFT, SWT.BOTTOM, false, false);
//      chartLabel.setLayoutData(gridData);
//      chartLabel.setText(Messages.getString("FieldSetupPanel.68")); //$NON-NLS-1$
//      chartLabel.setFont(labelFont);
//      SWTLine chartLine = new SWTLine(chartComposite, SWT.NONE);
//      chartLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
//      gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
//      gridData.heightHint = 5;
//      chartLine.setLayoutData(gridData);
//      useChartCheckBox = new Button(chartComposite, SWT.CHECK);
//      useChartCheckBox.setSelection(f.getUseChart());
//      useChartCheckBox.setText(Messages.getString("FieldSetupPanel.69")); //$NON-NLS-1$
//      gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
//      useChartCheckBox.setLayoutData(gridData);
//      useChartCheckBox.addSelectionListener(this);
//      showChartAboveGroupHeaderCheckBox = new Button(chartComposite, SWT.CHECK);
//      showChartAboveGroupHeaderCheckBox.setSelection(f.getShowChartAboveGroupHeader());
//      showChartAboveGroupHeaderCheckBox.setText(Messages.getString("FieldSetupPanel.70")); //$NON-NLS-1$
//      gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
//      showChartAboveGroupHeaderCheckBox.setLayoutData(gridData);
//      showChartAboveGroupHeaderCheckBox.addSelectionListener(this);
//      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
//      configureChartButton = new PentahoSWTButton(chartComposite, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("FieldSetupPanel.71")); //$NON-NLS-1$
//      configureChartButton.setLayoutData(gridData);
//      configureChartButton.addSelectionListener(this);
//    }
    SWTUtility.setBackground(getMainPanel(), ReportWizard.background);
    fieldGroup.layout();
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean isContinueAllowed() {
    if (reportDesigner.getReportSpec().getTemplateSrc() != null) {
      int numGroups = ReportSpecUtility.getNumberOfGroupsInTemplate(reportDesigner.getReportSpec().getTemplateSrc());
      // new rule based on Kurtis Cruzada: if template has no groups, then it is "flexible" can contain any number of groups
      if (numGroups == 0) {
        return true;
      }
      if (numGroups == ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField()).length) {
        return true;
      }
      return false;
    }
    return true;
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
    } else if (source instanceof QueryPanel) {
      initWizardPanel(reportDesigner.getReportSpec());
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
  }

  /**
   * 
   * 
   * @param reportSpec
   */
  public void initWizardPanel(ReportSpec reportSpec) {
    detailFieldList.setItems(ReportSpecUtility.getDetailFieldNames(reportSpec));
    groupFieldList.setItems(ReportSpecUtility.getGroupFieldNames(reportSpec));
    if (detailFieldList.getItemCount() > 0) {
      detailFieldList.select(0);
      populateUI(ReportSpecUtility.getDetails(reportSpec.getField())[0]);
    } else {
      populateUI(null);
    }
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
   * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseUp(MouseEvent e) {
    if (e.widget == backgroundButton) {
      ColorDialog colorDialog = new ColorDialog(getShell());
      colorDialog.setRGB(SWTUtility.getRGB(selectedField.getBackgroundColor()));
      RGB pickedColor = colorDialog.open();
      if (pickedColor != null) {
        selectedField.setBackgroundColor(SWTUtility.getJFreeColorString(pickedColor));
        backgroundButton.setBackground(new Color(getDisplay(), pickedColor));
        useBackgroundButton.setSelection(true);
        selectedField.setUseBackgroundColor(true);
      }
    } else if (e.widget == groupFooterBackgroundButton) {
      ColorDialog colorDialog = new ColorDialog(getShell());
      colorDialog.setRGB(SWTUtility.getRGB(selectedField.getBackgroundColor()));
      RGB pickedColor = colorDialog.open();
      if (pickedColor != null) {
        selectedField.setGroupFooterBackgroundColor(SWTUtility.getJFreeColorString(pickedColor));
        groupFooterBackgroundButton.setBackground(new Color(getDisplay(), pickedColor));
        useGroupFooterBackgroundButton.setSelection(true);
        selectedField.setUseGroupFooterBackgroundColor(true);
      }
    } else if (e.widget == detailFieldList) {
      if ((e.button) == 3) {
        // add field
        Menu menu = new Menu(getShell(), SWT.POP_UP);
        MenuItem addMenuItem = new MenuItem(menu, SWT.PUSH);
        addMenuItem.setText(Messages.getString("FieldSetupPanel.21")); //$NON-NLS-1$
        addMenuItem.addSelectionListener(new SelectionListener() {
          public void widgetSelected(SelectionEvent e) {
            Object[] cols = null;
            try {
              cols = ConnectionUtility.getColumns(reportDesigner.getPentahoConnection(), reportDesigner.getReportSpec().getQuery(), reportDesigner.getReportSpec().getReportSpecChoice().getXqueryUrl());
            } catch (Exception ex) {
              MessageBox mb = new MessageBox(getShell(), SWT.OK);
              mb.setMessage(ex.getMessage());
              mb.setText("Error");
              mb.open();
            }
            String allColumns[] = null;
            if (cols != null) {
              allColumns = new String[cols.length];
              for (int i = 0; i < cols.length; i++) {
                allColumns[i] = (String) cols[i];
              }
            } else {
              allColumns = new String[0];
            }
            ListInputDialog listInputDialog = new ListInputDialog(Messages.getString("GroupAndDetailPanel.14"), allColumns, 320, 200); //$NON-NLS-1$
            String[] selectedItems = listInputDialog.open();
            for (int i = 0; (selectedItems != null) && (i < selectedItems.length); i++) {
              detailFieldList.add(selectedItems[i]);
              Field f = new Field();
              f.setName(selectedItems[i]);
              f.setDisplayName(selectedItems[i]);
              f.setIsDetail(true);
              f.setWidthLocked(false);
              f.setIsWidthPercent(true);
              f.setType(((Integer) reportDesigner.getTypeMap().get(selectedItems[i])).intValue());
              if (f.getType() == Types.NUMERIC) {
                f.setUseItemHide(false);
                f.setExpression(reportDesigner.defaultNumericFunction); //$NON-NLS-1$
                f.setHorizontalAlignment("right"); //$NON-NLS-1$
              } else if (f.getType() == Types.VARCHAR) {
                f.setHorizontalAlignment("left"); //$NON-NLS-1$
              }
              reportDesigner.getReportSpec().addField(f);
              detailFieldList.setSelection(detailFieldList.getItemCount() - 1);
            }
          }

          public void widgetDefaultSelected(SelectionEvent e) {
          }
        });

        MenuItem addCalculatedColumnItem = new MenuItem(menu, SWT.PUSH);
        addCalculatedColumnItem.setText("Add Calculated Column.."); //$NON-NLS-1$
        addCalculatedColumnItem.addSelectionListener(new SelectionListener() {
          public void widgetSelected(SelectionEvent e) {
            detailFieldList.add("CALC");
            Field f = new Field();
            f.setIsCalculatedColumn(true);
            f.setName("CALC");
            f.setDisplayName("Total");
            f.setIsDetail(true);
            f.setWidthLocked(false);
            f.setIsWidthPercent(true);
            f.setType(Types.NUMERIC);
            f.setUseItemHide(false);
            f.setExpression(reportDesigner.defaultNumericFunction); //$NON-NLS-1$
            f.setHorizontalAlignment("right"); //$NON-NLS-1$
            reportDesigner.getReportSpec().addField(f);
            detailFieldList.setSelection(detailFieldList.getItemCount() - 1);
          }

          public void widgetDefaultSelected(SelectionEvent arg0) {
          }
        });

        MenuItem addSpacerMenuItem = new MenuItem(menu, SWT.PUSH);
        addSpacerMenuItem.setText(Messages.getString("FieldSetupPanel.50")); //$NON-NLS-1$
        addSpacerMenuItem.addSelectionListener(new SelectionListener() {
          public void widgetSelected(SelectionEvent e) {
            Field spacer = new Field();
            spacer.setWidthLocked(true);
            spacer.setWidth(new BigDecimal(1));
            spacer.setIsWidthPercent(true);
            spacer.setIsDetail(true);
            int spacerIndex = getNextSpacerIndex();
            spacer.setName("_SPACE_" + spacerIndex); //$NON-NLS-1$
            spacer.setDisplayName("_SPACE_" + spacerIndex); //$NON-NLS-1$
            spacer.setType(Types.VARCHAR);
            spacer.setUseBackgroundColor(false);
            spacer.setBackgroundColor("#C0C0D0"); //$NON-NLS-1$
            reportDesigner.getReportSpec().addField(spacer);
            detailFieldList.add(spacer.getName());
          }

          public void widgetDefaultSelected(SelectionEvent e) {
          }
        });
        MenuItem removeMenuItem = new MenuItem(menu, SWT.PUSH);
        removeMenuItem.setText(Messages.getString("FieldSetupPanel.22")); //$NON-NLS-1$
        removeMenuItem.addSelectionListener(new SelectionListener() {
          public void widgetSelected(SelectionEvent e) {
            int selectedIndices[] = detailFieldList.getSelectionIndices();
            Arrays.sort(selectedIndices);
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
              int index = selectedIndices[i];
              detailFieldList.remove(index);
              Field f = ReportSpecUtility.getDetails(reportDesigner.getReportSpec().getField())[index];
              reportDesigner.getReportSpec().removeField(f);
            }
          }

          public void widgetDefaultSelected(SelectionEvent e) {
          }
        });
        menu.setVisible(true);
      }
    } else if (e.widget == groupFieldList) {
      if ((e.button) == 3) {
        // add field
        Menu menu = new Menu(getShell(), SWT.POP_UP);
        MenuItem addMenuItem = new MenuItem(menu, SWT.PUSH);
        addMenuItem.setText(Messages.getString("FieldSetupPanel.23")); //$NON-NLS-1$
        addMenuItem.addSelectionListener(new SelectionListener() {
          public void widgetSelected(SelectionEvent e) {
            String xqueryurl = null;
            if (reportDesigner.getReportSpec().getReportSpecChoice() != null) {
              xqueryurl = reportDesigner.getReportSpec().getReportSpecChoice().getXqueryUrl();
            }
            Object[] cols = null;
            try {
              cols = ConnectionUtility.getColumns(reportDesigner.getPentahoConnection(), reportDesigner.getReportSpec().getQuery(), xqueryurl);
            } catch (Exception ex) {
              MessageBox mb = new MessageBox(getShell(), SWT.OK);
              mb.setMessage(ex.getMessage());
              mb.setText("Error");
              mb.open();
            }
            String allColumns[] = null;
            if (cols != null) {
              allColumns = new String[cols.length];
              for (int i = 0; i < cols.length; i++) {
                allColumns[i] = (String) cols[i];
              }
            } else {
              allColumns = new String[0];
            }
            ListInputDialog listInputDialog = new ListInputDialog(Messages.getString("GroupAndDetailPanel.14"), allColumns, 320, 200); //$NON-NLS-1$
            String[] selectedItems = listInputDialog.open();
            for (int i = 0; (selectedItems != null) && (i < selectedItems.length); i++) {
              groupFieldList.add(selectedItems[i]);
              Field f = new Field();
              f.setName(selectedItems[i]);
              f.setDisplayName(selectedItems[i]);
              f.setIsDetail(false);
              f.setWidthLocked(false);
              f.setIsWidthPercent(true);
              f.setType(((Integer) reportDesigner.getTypeMap().get(selectedItems[i])).intValue());
              if (f.getType() == Types.NUMERIC) {
                f.setUseItemHide(false);
                f.setExpression(reportDesigner.defaultNumericFunction); //$NON-NLS-1$
                f.setHorizontalAlignment("right"); //$NON-NLS-1$
              } else if (f.getType() == Types.VARCHAR) {
                f.setHorizontalAlignment("left"); //$NON-NLS-1$
              }
              f.setDisplayName(f.getName() + ": $(" + f.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
              f.setGroupTotalsLabel("$(" + f.getName() + ") Total"); //$NON-NLS-1$ //$NON-NLS-2$
              reportDesigner.getReportSpec().addField(f);
              groupFieldList.setSelection(groupFieldList.getItemCount() - 1);
            }
          }

          public void widgetDefaultSelected(SelectionEvent e) {
          }
        });
        MenuItem removeMenuItem = new MenuItem(menu, SWT.PUSH);
        removeMenuItem.setText(Messages.getString("FieldSetupPanel.24")); //$NON-NLS-1$
        removeMenuItem.addSelectionListener(new SelectionListener() {
          public void widgetSelected(SelectionEvent e) {
            int selectedIndices[] = groupFieldList.getSelectionIndices();
            Arrays.sort(selectedIndices);
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
              int index = selectedIndices[i];
              groupFieldList.remove(index);
              Field f = ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[index];
              reportDesigner.getReportSpec().removeField(f);
            }
          }

          public void widgetDefaultSelected(SelectionEvent e) {
          }
        });
        menu.setVisible(true);
      }
    }
    updateState();
  }

  public int getNextSpacerIndex() {
    Field fields[] = ReportSpecUtility.getDetails(reportDesigner.getReportSpec().getField());
    int maxFoundIndex = 0;
    for (int i = 0; i < fields.length; i++) {
      Field f = fields[i];
      String name = f.getName();
      if (name.startsWith("_SPACE_")) { //$NON-NLS-1$
        int spacerIndex = Integer.parseInt(name.substring(7));
        if (spacerIndex > maxFoundIndex) {
          maxFoundIndex = spacerIndex;
        }
      }
    }
    return (maxFoundIndex + 1);
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
    // TODO Auto-generated method stub
  }

  public void modifyText(ModifyEvent e) {
    if (e.widget == displayNameText) {
      selectedField.setDisplayName(displayNameText.getText());
    } else if (e.widget == formatText) {
      selectedField.setFormat(formatText.getText());
    } else if (e.widget == groupTotalText) {
      selectedField.setGroupTotalsLabel(groupTotalText.getText());
    } else if (e.widget == nullStringText) {
      selectedField.setNullString(nullStringText.getText());
    }
    stateChanged = true;
    dirty = true;
    fireDirtyEvent();
    wizardManager.update();
  }
}
