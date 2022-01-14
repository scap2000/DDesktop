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

import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.pentaho.jfreereport.castormodel.reportspec.Field;
import org.pentaho.jfreereport.castormodel.reportspec.FieldMapping;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.WizardManager;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.IDirtyListener;
import org.pentaho.jfreereport.wizard.ui.WizardPanel;
import org.pentaho.jfreereport.wizard.ui.swt.SWTLine;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;
import org.pentaho.jfreereport.wizard.utility.connection.ConnectionUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportSpecUtility;

public class MappingPanel extends WizardPanel implements IDirtyListener, ModifyListener {
  protected static String[] titles = { Messages.getString("MappingPanel.0"), Messages.getString("MappingPanel.3"), Messages.getString("MappingPanel.8"), Messages.getString("MappingPanel.23") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

  ReportWizard reportDesigner = null;

  Table mapTable = null;

  Composite propertyComposite = null;

  Text customValueText = null;

  TableItem selectedItem = null;

  List columnList = null;

  Button customValueButton = null;

  Button columnValueButton = null;

  Button defaultValueButton = null;

  HashMap typeMap = new HashMap();

  HashMap defaultMap = new HashMap();

  /**
   * @param parent
   * @param style
   * @param manager
   */
  public MappingPanel(Composite parent, int style, WizardManager manager, ReportWizard reportDesigner) {
    super(parent, style, manager);
    this.reportDesigner = reportDesigner;
    initialize();
    SWTUtility.setBackground(getMainPanel(), ReportWizard.background);
  }

  public void initialize() {
    addDirtyListener(this);
    Composite c = getMainPanel();
    c.setLayout(new GridLayout(1, true));
    final Composite contentGroup = new Composite(c, SWT.NONE);
    contentGroup.setLayout(new GridLayout(2, false));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    contentGroup.setLayoutData(gridData);
    Label stepLabel = new Label(contentGroup, SWT.NONE);
    stepLabel.setText(Messages.getString("MappingPanel.4")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    stepLabel.setLayoutData(gridData);
    stepLabel.setFont(labelFont);
    Text stepText = new Text(contentGroup, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
    stepText.setText(Messages.getString("MappingPanel.5")); //$NON-NLS-1$
    stepText.setBackground(ReportWizard.background);
    stepText.setFont(textFont);
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    stepText.setLayoutData(gridData);
    SWTLine line = new SWTLine(contentGroup, SWT.NONE);
    line.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.BOTTOM, false, false);
    gridData.heightHint = 5;
    gridData.horizontalSpan = 2;
    line.setLayoutData(gridData);
    Label tableLabel = new Label(contentGroup, SWT.NONE);
    tableLabel.setText(Messages.getString("MappingPanel.6")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    tableLabel.setLayoutData(gridData);
    tableLabel.setFont(labelFont);
    Label mapToLabel = new Label(contentGroup, SWT.NONE);
    mapToLabel.setText(Messages.getString("MappingPanel.7")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    mapToLabel.setLayoutData(gridData);
    mapToLabel.setFont(labelFont);
    SWTLine tableLabelLine = new SWTLine(contentGroup, SWT.NONE);
    tableLabelLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.BOTTOM, false, false);
    gridData.heightHint = 5;
    tableLabelLine.setLayoutData(gridData);
    SWTLine mapToLabelLine = new SWTLine(contentGroup, SWT.NONE);
    mapToLabelLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.BOTTOM, false, false);
    gridData.heightHint = 5;
    mapToLabelLine.setLayoutData(gridData);
    mapTable = new Table(contentGroup, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
    gridData = new GridData(SWT.LEFT, SWT.FILL, false, true);
    gridData.widthHint = 400;
    mapTable.setLayoutData(gridData);
    mapTable.setLinesVisible(true);
    mapTable.setHeaderVisible(true);
    mapTable.addSelectionListener(this);
    mapTable.addMouseListener(new MouseListener() {

      public void mouseDoubleClick(MouseEvent e) {
      }

      public void mouseDown(MouseEvent e) {
      }

      public void mouseUp(MouseEvent e) {
        selectedItem = mapTable.getSelection()[0];
        selectItem(selectedItem);
        updateState();
      }

    });
    // make initial selection (this is for SwingSWT)
    if (mapTable.getSelection() != null && mapTable.getSelection().length > 0) {
      selectedItem = mapTable.getSelection()[0];
      selectItem(selectedItem);
      updateState();
    }
    for (int i = 0; i < titles.length; i++) {
      TableColumn column = new TableColumn(mapTable, SWT.NONE);
      column.setText(titles[i]);
      // mapTable.setSortColumn(column);
      column.addSelectionListener(this);
    }
    propertyComposite = new Composite(contentGroup, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    propertyComposite.setLayoutData(gridData);
    propertyComposite.setLayout(new GridLayout(1, false));
  }

  public void widgetSelected(SelectionEvent e) {
    if (e.widget == mapTable) {
      selectedItem = mapTable.getSelection()[0];
      selectItem(selectedItem);
    } else if (e.widget == columnList) {
      selectedItem.setText(1, columnList.getSelection()[0]);
      selectedItem.setText(2, Messages.getString("MappingPanel.9")); //$NON-NLS-1$
      updateReportSpecMappings();
    } else if (e.widget == defaultValueButton) {
      selectedItem.setText(1, (String) defaultMap.get(selectedItem.getText(0)));
      selectedItem.setText(2, Messages.getString("MappingPanel.10")); //$NON-NLS-1$
      updateReportSpecMappings();
      return;
    } else if (e.widget == customValueButton) {
      columnList.setEnabled(false);
      customValueText.setEnabled(true);
      selectedItem.setText(1, customValueText.getText());
      selectedItem.setText(2, Messages.getString("MappingPanel.11")); //$NON-NLS-1$
      updateReportSpecMappings();
      return;
    }
    updateState();
    super.widgetSelected(e);
  }

  public void updateReportSpecMappings() {
    String key = selectedItem.getText(0);
    String value = selectedItem.getText(1);
    // put mapping in reportspec
    FieldMapping[] fieldMappings = reportDesigner.getReportSpec().getFieldMapping();
    boolean mapExists = false;
    for (int i = 0; fieldMappings != null && i < fieldMappings.length; i++) {
      FieldMapping fm = fieldMappings[i];
      if (fm.getKey().equals(key)) {
        // we already have a fieldMapping, just update the value
        fm.setValue(value);
        mapExists = true;
        break;
      }
    }
    if (!mapExists) {
      // create one, add it
      FieldMapping fm = new FieldMapping();
      fm.setKey(key);
      fm.setValue(value);
      fm.setType(selectedItem.getText(1));
      reportDesigner.getReportSpec().addFieldMapping(fm);
    }
  }

  public void keyReleased(KeyEvent e) {
    if (e.widget == customValueText) {
      if (selectedItem != null && customValueText != null && customValueText.getText() != null) {
        selectedItem.setText(1, customValueText.getText());
        selectedItem.setText(2, Messages.getString("MappingPanel.12")); //$NON-NLS-1$
        updateReportSpecMappings();
        customValueText.setFocus();
        return;
      }
    }
    super.keyReleased(e);
  }

  public void selectItem(TableItem item) {
    SWTUtility.removeChildren(propertyComposite);
    defaultValueButton = new Button(propertyComposite, SWT.RADIO);
    defaultValueButton.setBackground(ReportWizard.background);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    defaultValueButton.setLayoutData(gridData);
    defaultValueButton.setText(Messages.getString("MappingPanel.13")); //$NON-NLS-1$
    defaultValueButton.addSelectionListener(this);
    defaultValueButton.setSelection(true);
    // handle label mapping, just text
    Text defaultValueText = new Text(propertyComposite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
    defaultValueText.setText(item.getText(1));
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    gridData.widthHint = 400;
    defaultValueText.setLayoutData(gridData);
    defaultValueText.setText((String) defaultMap.get(item.getText(0)));
    if (((String) typeMap.get(item.getText(0))).equalsIgnoreCase("label")) { //$NON-NLS-1$
      customValueButton = new Button(propertyComposite, SWT.RADIO);
      gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
      customValueButton.setText(Messages.getString("MappingPanel.14")); //$NON-NLS-1$
      customValueButton.addSelectionListener(this);
      // handle label mapping, just text
      customValueText = new Text(propertyComposite, SWT.SINGLE | SWT.BORDER);
      customValueText.setText(item.getText(1));
      gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
      gridData.widthHint = 400;
      customValueButton.setSelection(true);
      defaultValueButton.setSelection(false);
      customValueText.setLayoutData(gridData);
//      customValueText.addModifyListener(this);
      customValueText.addKeyListener(this);
      customValueText.setEnabled(true);
    }
    columnValueButton = new Button(propertyComposite, SWT.RADIO);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    columnValueButton.setText(Messages.getString("MappingPanel.15")); //$NON-NLS-1$
    columnValueButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent arg0) {
        super.widgetSelected(arg0);
        columnList.setEnabled(true);
        if (customValueText != null && !customValueText.isDisposed())
          customValueText.setEnabled(false);
      }
    });
    columnList = new List(propertyComposite, SWT.SINGLE | SWT.BORDER);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    columnList.setLayoutData(gridData);
    String xQueryURL = reportDesigner.getReportSpec().getReportSpecChoice() == null ? null : reportDesigner.getReportSpec().getReportSpecChoice().getXqueryUrl();
    Object[] cols = null;
    try {
      cols = ConnectionUtility.getColumns(reportDesigner.getPentahoConnection(), reportDesigner.getReportSpec().getQuery(), xQueryURL);
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
    columnList.setItems(allColumns);
    int index = columnList.indexOf(item.getText(1));
    if (index >= 0) {
      columnList.select(index);
      columnValueButton.setSelection(true);
      defaultValueButton.setSelection(false);
      if (customValueButton != null && !customValueButton.isDisposed()) {
        customValueButton.setSelection(false);
      }
    }
    columnList.addSelectionListener(this);
    columnList.setEnabled(false);
    SWTUtility.setBackground(getMainPanel(), ReportWizard.background);
    propertyComposite.layout();
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean isContinueAllowed() {
    boolean continueAllowed = true;
    // pull groups from table
    // check if group mapped in table ALL match actual available columns
    // if yes - continue allowed

    if (reportDesigner.getReportSpec().getTemplateSrc() != null) {
      Object[] cols = null;
      try {
        cols = ConnectionUtility.getColumns(reportDesigner.getPentahoConnection(), reportDesigner.getReportSpec().getQuery(), reportDesigner.getReportSpec().getReportSpecChoice() == null ? null : reportDesigner.getReportSpec()
            .getReportSpecChoice().getXqueryUrl());
      } catch (Exception ex) {
        continueAllowed = false;
      }
      for (int i = 0; i < mapTable.getItemCount() && cols != null; i++) {
        TableItem item = mapTable.getItem(i);
        String type = item.getText(3);
        // String key = item.getText(0);
        String value = item.getText(1);
        if (type.equalsIgnoreCase("group")) { //$NON-NLS-1$
          // check against available columns
          boolean found = false;
          for (int j = 0; j < cols.length; j++) {
            if (((String) cols[j]).equalsIgnoreCase(value)) {
              found = true;
              break;
            }
          }
          if (!found) {
            continueAllowed = false;
            break;
          }
        }
      }
    }
    return continueAllowed;
  }

  /**
   * 
   * 
   * @param source
   */
  public boolean nextFired(WizardPanel source) {
    super.nextFired(source);
    if (stateChanged && source == this) {
      stateChanged = false;
      if (reportDesigner.getReportSpec().getTemplateSrc() != null) {
        int numGroups = ReportSpecUtility.getNumberOfGroupsInTemplate(reportDesigner.getReportSpec().getTemplateSrc());
        // new rule based on Kurtis Cruzada: if template has no groups,
        // then it is "flexible" can contain any number of groups
        if (numGroups == 0) {
          return true;
        }
        setupTemplateGroups();
      }
    }
    return true;
  }

  public void setupTemplateGroups() {

    boolean hasGroupConfig = false;
    for (int i = 0; i < mapTable.getItemCount(); i++) {
      TableItem item = mapTable.getItem(i);
      String type = item.getText(3);
      if (type.equalsIgnoreCase("group")) { //$NON-NLS-1$
        hasGroupConfig = true;
        break;
      }
    }

    if (hasGroupConfig) {
      // at this point we should unflag all group fields
      Field fields[] = reportDesigner.getReportSpec().getField();
      for (int j = 0; j < fields.length; j++) {
        fields[j].setIsDetail(true);
      }
      // now mark all intended columns as groups
      for (int i = 0; i < mapTable.getItemCount(); i++) {
        TableItem item = mapTable.getItem(i);
        String type = item.getText(3);
        // String key = item.getText(0);
        String value = item.getText(1);
        if (type.equalsIgnoreCase("group")) { //$NON-NLS-1$
          for (int j = 0; j < fields.length; j++) {
            if (fields[j].getName().equalsIgnoreCase(value)) {
              fields[j].setIsDetail(false);
              break;
            }
          }
        }
      }
      // roll through the groups and determine which ones use group header
      // based on template
      fields = ReportSpecUtility.getGroups(fields);
      if (reportDesigner.getReportSpec().getTemplateSrc() != null) {
        // update if we use the group
        for (int i = 0; i < fields.length; i++) {
          boolean headerExists = ReportSpecUtility.doesHeaderExistForGroup(reportDesigner.getReportSpec().getTemplateSrc(), i);
          fields[i].setCreateGroupHeader(!headerExists);
        }
      }
    }
  }

  /**
   * 
   * 
   * @param source
   */
  public void finishFired(WizardPanel source) {
    super.finishFired(source);
  }

  public void initWizardPanel() {
    super.initWizardPanel();
    if (reportDesigner.getReportSpec().getTemplateSrc() != null) {
      SWTUtility.setEnabled(mapTable, true);
      SWTUtility.setEnabled(propertyComposite, true);
      if (columnList != null && !columnValueButton.isDisposed() && columnValueButton.getSelection() && !columnList.isDisposed()) {
        columnList.setEnabled(true);
      } else if (columnList != null && (columnValueButton == null || !columnValueButton.isDisposed())) {
        columnList.setEnabled(false);
      }
      if (columnValueButton != null && !customValueButton.isDisposed() && customValueButton.getSelection()) {
        customValueText.setEnabled(true);
      } else if (columnValueButton != null && !customValueButton.isDisposed()) {
        customValueText.setEnabled(false);
      }
    } else {
      SWTUtility.setEnabled(mapTable, false);
      SWTUtility.setEnabled(propertyComposite, false);
    }
    if (selectedItem != null && !selectedItem.isDisposed() && defaultMap.get(selectedItem.getText(0)).equals(selectedItem.getText(1))) {
      defaultValueButton.setSelection(true);
      if (customValueText != null && !customValueText.isDisposed()) {
        customValueText.setEnabled(false);
      }
      if (customValueButton != null && !customValueButton.isDisposed()) {
        customValueButton.setSelection(false);
      }
      columnList.setEnabled(false);
    }
  }

  /**
   * 
   * 
   * @param reportSpec
   */
  public void initWizardPanel(ReportSpec reportSpec) {
    SWTUtility.removeChildren(mapTable);
    mapTable.removeAll();
    mapTable.clearAll();
    typeMap.clear();
    defaultMap.clear();
    FieldMapping fm[] = reportDesigner.getReportSpec().getFieldMapping();
    if (fm != null) {
      java.util.List labelList = new LinkedList();
      java.util.List itemList = new LinkedList();
      java.util.List groupList = new LinkedList();
      for (int i = 0; i < fm.length; i++) {
        if (fm[i].getType() == null || fm[i].getType().equals("label")) { //$NON-NLS-1$
          labelList.add(fm[i]);
          fm[i].setType("label"); //$NON-NLS-1$
        } else if (fm[i].getType().equals("item")) { //$NON-NLS-1$
          itemList.add(fm[i]);
        } else if (fm[i].getType().equals("group")) { //$NON-NLS-1$
          groupList.add(fm[i]);
        }
      }
      for (int i = 0; i < labelList.size(); i++) {
        String key = ((FieldMapping) labelList.get(i)).getKey();
        String value = ((FieldMapping) labelList.get(i)).getValue();
        TableItem item = new TableItem(mapTable, SWT.NONE);
        item.setText(0, key);
        typeMap.put(key, "label"); //$NON-NLS-1$
        item.setText(1, value);
        item.setText(2, Messages.getString("MappingPanel.17")); //$NON-NLS-1$
        item.setText(3, "label"); //$NON-NLS-1$
        defaultMap.put(key, value);
        // System.out.println("key = " + key + " = " + value);
      }
      for (int i = 0; i < itemList.size(); i++) {
        String key = ((FieldMapping) itemList.get(i)).getKey();
        String value = ((FieldMapping) itemList.get(i)).getValue();
        TableItem item = new TableItem(mapTable, SWT.NONE);
        item.setText(0, key);
        typeMap.put(key, Messages.getString("MappingPanel.22")); //$NON-NLS-1$
        item.setText(1, value);
        item.setText(2, Messages.getString("MappingPanel.19")); //$NON-NLS-1$
        item.setText(3, "item"); //$NON-NLS-1$
        defaultMap.put(key, value);
        // System.out.println("key = " + key + " = " + value);
      }
      for (int i = 0; i < groupList.size(); i++) {
        String key = ((FieldMapping) groupList.get(i)).getKey();
        String value = ((FieldMapping) groupList.get(i)).getValue();
        TableItem item = new TableItem(mapTable, SWT.NONE);
        item.setText(0, key);
        typeMap.put(key, "group"); //$NON-NLS-1$
        item.setText(1, value);
        item.setText(2, Messages.getString("MappingPanel.21")); //$NON-NLS-1$
        item.setText(3, "group"); //$NON-NLS-1$
        defaultMap.put(key, value);
        // System.out.println("key = " + key + " = " + value);
      }
      int width = 500;
      int widthSum = 0;
      for (int i = 0; i < titles.length; i++) {
        if (i == titles.length - 1) {
          mapTable.getColumn(i).setWidth(width - widthSum + 17);
        } else {
          mapTable.getColumn(i).pack();
          widthSum += mapTable.getColumn(i).getWidth();
        }
      }
      SWTUtility.setEnabled(mapTable, true);
      if (mapTable.getItemCount() > 0) {
        mapTable.select(0);
        selectItem(mapTable.getItem(0));
      }
      ((GridData) mapTable.getLayoutData()).widthHint = width;
      mapTable.getParent().layout();
    } else {
      SWTUtility.setEnabled(mapTable, false);
    }
    if (mapTable != null && mapTable.getItems() != null && mapTable.getItems().length > 0) {
      selectedItem = mapTable.getItems()[0];
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.jfreereport.wizard.IDirtyListener#dirtyFired(boolean)
   */
  public void dirtyFired(boolean dirty) {
  }

  public void modifyText(ModifyEvent e) {
    stateChanged = true;
    dirty = true;
    fireDirtyEvent();
    wizardManager.update();
    updateReportSpecMappings();
  }
}
