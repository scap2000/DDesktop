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

import java.io.InputStream;
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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.pentaho.jfreereport.castormodel.reportspec.Field;
import org.pentaho.jfreereport.castormodel.reportspec.FieldMapping;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.WizardManager;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.IDirtyListener;
import org.pentaho.jfreereport.wizard.ui.WizardPanel;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTLine;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;
import org.pentaho.jfreereport.wizard.utility.connection.ConnectionUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportSpecUtility;

/**
 * DOCUMENT ME!
 */
public class GroupAndDetailPanel extends WizardPanel implements IDirtyListener {
  /**
   * 
   */
  ReportWizard reportDesigner = null;

  /**
   * 
   */
  String[] allColumns = null;

  /**
   * 
   */
  List allFieldsList = null;

  /**
   * 
   */
  List detailsList = null;

  /**
   * 
   */
  List groupsList = null;

  /*
   * 
   */
  Object dragSource = null;

  PentahoSWTButton removeButton = null;

  PentahoSWTButton addToGroupButton = null;

  PentahoSWTButton addToDetailButton = null;

  PentahoSWTButton detailUpButton = null;

  PentahoSWTButton detailDownButton = null;

  PentahoSWTButton groupUpButton = null;

  PentahoSWTButton groupDownButton = null;

  Text numGroupsText = null;

  /**
   * DOCUMENT ME!
   * 
   * @param parent
   * @param style
   * @param manager
   * @param reportDesigner
   */
  public GroupAndDetailPanel(Composite parent, int style, WizardManager manager, ReportWizard reportDesigner) {
    super(parent, style, manager);
    this.reportDesigner = reportDesigner;
    initialize();
    SWTUtility.setBackground(this, ReportWizard.background);
  }

  /**
   * 
   */
  public void initialize() {
    addDirtyListener(this);
    Composite c = getMainPanel();
    c.setBackground(ReportWizard.background);
    c.setLayout(new GridLayout(2, false));
    Label stepLabel = new Label(c, SWT.NONE);
    stepLabel.setText(Messages.getString("GroupAndDetailPanel.8")); //$NON-NLS-1$
    GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    stepLabel.setLayoutData(gridData);
    stepLabel.setFont(labelFont);
    Text stepText = new Text(c, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
    stepText.setText(Messages.getString("GroupAndDetailPanel.9")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    stepText.setLayoutData(gridData);
    stepText.setBackground(ReportWizard.background);
    stepText.setFont(textFont);
    SWTLine line = new SWTLine(c, SWT.NONE);
    line.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.BOTTOM, false, false);
    gridData.heightHint = 5;
    gridData.horizontalSpan = 2;
    line.setLayoutData(gridData);
    // createMoveComposite(contentGroup);

    Composite allFieldsComposite = new Composite(c, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    allFieldsComposite.setLayoutData(gridData);
    allFieldsComposite.setLayout(new GridLayout(2, false));
    Label allFieldsLabel = new Label(allFieldsComposite, SWT.NONE);
    allFieldsLabel.setBackground(ReportWizard.background);
    gridData = new GridData(SWT.LEFT, SWT.FILL, false, false);
    allFieldsLabel.setLayoutData(gridData);
    allFieldsLabel.setText(Messages.getString("GroupAndDetailPanel.0")); //$NON-NLS-1$
    allFieldsLabel.setFont(labelFont);
    Label emptyLabel = new Label(allFieldsComposite, SWT.NONE);
    emptyLabel.setBackground(ReportWizard.background);
    gridData = new GridData(SWT.LEFT, SWT.FILL, false, false);
    emptyLabel.setLayoutData(gridData);
    emptyLabel.setFont(labelFont);
    createAllFieldsComposite(allFieldsComposite);
    createMoveComposite(allFieldsComposite);

    Composite groupAndDetailsComposite = new Composite(c, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    groupAndDetailsComposite.setLayoutData(gridData);
    groupAndDetailsComposite.setLayout(new GridLayout(2, false));
    emptyLabel = new Label(groupAndDetailsComposite, SWT.NONE);
    emptyLabel.setBackground(ReportWizard.background);
    gridData = new GridData(SWT.LEFT, SWT.FILL, false, false);
    emptyLabel.setLayoutData(gridData);
    emptyLabel.setFont(labelFont);
    Label groupsLabel = new Label(groupAndDetailsComposite, SWT.NONE);
    groupsLabel.setBackground(ReportWizard.background);
    gridData = new GridData(SWT.LEFT, SWT.FILL, false, false);
    groupsLabel.setLayoutData(gridData);
    groupsLabel.setText(Messages.getString("GroupAndDetailPanel.1")); //$NON-NLS-1$
    groupsLabel.setFont(labelFont);

    InputStream imageStream = getClass().getResourceAsStream("/images/lrg_icon_group.png"); //$NON-NLS-1$
    Image groupImage = new Image(getDisplay(), imageStream); //$NON-NLS-1$
    emptyLabel = new Label(groupAndDetailsComposite, SWT.NONE);
    gridData = new GridData(SWT.RIGHT, SWT.TOP, false, false);
    emptyLabel.setLayoutData(gridData);
    emptyLabel.setImage(groupImage);
    createGroupsComposite(groupAndDetailsComposite);
    emptyLabel = new Label(groupAndDetailsComposite, SWT.NONE);
    emptyLabel.setBackground(ReportWizard.background);
    gridData = new GridData(SWT.LEFT, SWT.FILL, false, false);
    emptyLabel.setLayoutData(gridData);
    Label detailLabel = new Label(groupAndDetailsComposite, SWT.NONE);
    detailLabel.setBackground(ReportWizard.background);
    gridData = new GridData(SWT.LEFT, SWT.FILL, false, false);
    detailLabel.setLayoutData(gridData);
    detailLabel.setText(Messages.getString("GroupAndDetailPanel.3")); //$NON-NLS-1$
    detailLabel.setFont(labelFont);
    imageStream = getClass().getResourceAsStream("/images/lrg_icon_detail.png"); //$NON-NLS-1$
    Image detailImage = new Image(getDisplay(), imageStream); //$NON-NLS-1$
    emptyLabel = new Label(groupAndDetailsComposite, SWT.NONE);
    gridData = new GridData(SWT.RIGHT, SWT.TOP, false, false);
    emptyLabel.setLayoutData(gridData);
    emptyLabel.setImage(detailImage);
    createDetailsComposite(groupAndDetailsComposite);
    addToDetailButton.setEnabled(false);
    addToGroupButton.setEnabled(false);
    removeButton.setEnabled(false);
    setupDND();
    updateState();
  }

  public void setupDND() {
    Transfer[] types = new Transfer[] { TextTransfer.getInstance(), FileTransfer.getInstance() };
    int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
    final DragSource availableSource = new DragSource(allFieldsList, operations);
    final DragSource groupSource = new DragSource(groupsList, operations);
    final DropTarget groupTarget = new DropTarget(groupsList, operations);
    final DragSource detailSource = new DragSource(detailsList, operations);
    final DropTarget detailTarget = new DropTarget(detailsList, operations);
    availableSource.setTransfer(types);
    availableSource.addDragListener(new DragSourceAdapter() {
      public void dragStart(DragSourceEvent event) {
        event.doit = (allFieldsList.getSelection() != null && allFieldsList.getSelection().length > 0);
        dragSource = allFieldsList;
      }

      public void dragSetData(DragSourceEvent event) {
        event.data = allFieldsList.getItem(allFieldsList.getSelectionIndex());
      }
    });
    groupSource.setTransfer(types);
    groupSource.addDragListener(new DragSourceAdapter() {
      public void dragStart(DragSourceEvent event) {
        event.doit = (groupsList.getSelection() != null && groupsList.getSelection().length > 0);
        dragSource = groupsList;
      }

      public void dragSetData(DragSourceEvent event) {
        event.data = groupsList.getItem(groupsList.getSelectionIndex());
      }
    });
    detailTarget.setTransfer(types);
    detailTarget.addDropListener(new DropTargetAdapter() {
      public void drop(DropTargetEvent event) {
        if (event.data == null) {
          event.detail = DND.DROP_NONE;
          return;
        }
        if (dragSource == detailsList) {
          // drop to ourself
          Point point = new Point(event.x, event.y);
          point = getDisplay().map(null, detailsList, point);
          int index = SWTUtility.getListIndexForPoint(detailsList, point);
          SWTUtility.moveSelectedItems(detailsList, index);
          detailsList.setSelection(index);
          detailsList.select(index);
          // use the detailFieldList to re-order the report spec fields
          Field fields[] = reportDesigner.getReportSpec().getField();
          Field details[] = new Field[ReportSpecUtility.getDetails(fields).length];
          for (int i = 0; i < details.length; i++) {
            details[i] = ReportSpecUtility.getField(fields, detailsList.getItem(i), true);
            reportDesigner.getReportSpec().removeField(details[i]);
            reportDesigner.getReportSpec().addField(details[i]);
          }
          updateState();
          return;
        }
        if (dragSource == allFieldsList) {
          if (event.detail == DND.DROP_COPY || event.detail == DND.DROP_MOVE) {
            try {
              groupsList.deselectAll();
              Field newField = null;
              for (int i = 0; i < allFieldsList.getSelectionIndices().length; i++) {
                newField = new Field();
                newField.setName(allFieldsList.getItem(allFieldsList.getSelectionIndices()[i]));
                newField.setDisplayName(newField.getName());
                newField.setIsDetail(true);
                reportDesigner.getReportSpec().addField(newField);
                String item = (String) event.data;
                detailsList.add(item);
                detailsList.setSelection(detailsList.indexOf(item));
                updateState();
              }
              allFieldsList.deselectAll();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        } else {
          if (event.detail == DND.DROP_MOVE) {
            try {
              Field sourceField = null;
              for (int i = 0; i < groupsList.getSelectionIndices().length; i++) {
                sourceField = ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[groupsList.getSelectionIndices()[i]];
                reportDesigner.getReportSpec().removeField(sourceField);
                sourceField.setIsDetail(true);
                reportDesigner.getReportSpec().addField(sourceField);
              }
              groupsList.remove(groupsList.getSelectionIndices());
              updateState();
            } catch (Exception e) {
              e.printStackTrace();
            }
          } else if (event.detail == DND.DROP_COPY) {
            try {
              Field newField = null;
              for (int i = 0; i < groupsList.getSelectionIndices().length; i++) {
                Field sourceField = ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[groupsList.getSelectionIndices()[i]];
                newField = new Field();
                newField.setName(sourceField.getName());
                newField.setDisplayName(sourceField.getName());
                newField.setIsDetail(true);
                reportDesigner.getReportSpec().addField(newField);
                updateState();
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          String item = (String) event.data;
          detailsList.add(item);
          detailsList.setSelection(detailsList.indexOf(item));
          updateState();
        }
      }
    });
    detailSource.setTransfer(types);
    detailSource.addDragListener(new DragSourceAdapter() {
      public void dragStart(DragSourceEvent event) {
        event.doit = (detailsList.getSelection() != null && detailsList.getSelection().length > 0);
        dragSource = detailsList;
      }

      public void dragSetData(DragSourceEvent event) {
        event.data = detailsList.getItem(detailsList.getSelectionIndex());
      }
    });
    groupTarget.setTransfer(types);
    groupTarget.addDropListener(new DropTargetAdapter() {
      public void drop(DropTargetEvent event) {
        if (event.data == null) {
          event.detail = DND.DROP_NONE;
          return;
        }
        if (dragSource == groupsList) {
          // drop to ourself
          Point point = new Point(event.x, event.y);
          point = getDisplay().map(null, groupsList, point);
          int index = SWTUtility.getListIndexForPoint(groupsList, point);
          SWTUtility.moveSelectedItems(groupsList, index);
          groupsList.setSelection(index);
          groupsList.select(index);
          // use the groupFieldList to re-order the report spec fields
          Field fields[] = reportDesigner.getReportSpec().getField();
          Field groups[] = new Field[ReportSpecUtility.getGroups(fields).length];
          for (int i = 0; i < groups.length; i++) {
            groups[i] = ReportSpecUtility.getField(fields, groupsList.getItem(i), false);
            reportDesigner.getReportSpec().removeField(groups[i]);
            reportDesigner.getReportSpec().addField(groups[i]);
          }
          updateState();
          return;
        }
        if (dragSource == allFieldsList) {
          if (event.detail == DND.DROP_COPY || event.detail == DND.DROP_MOVE) {
            try {
              detailsList.deselectAll();
              Field newField = null;
              for (int i = 0; i < allFieldsList.getSelectionIndices().length; i++) {
                newField = new Field();
                newField.setName(allFieldsList.getItem(allFieldsList.getSelectionIndices()[i]));
                newField.setDisplayName(newField.getName());
                newField.setIsDetail(false);
                newField.setDisplayName(newField.getName() + ": $(" + newField.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
                newField.setGroupTotalsLabel("$(" + newField.getName() + ") Total"); //$NON-NLS-1$ //$NON-NLS-2$
                reportDesigner.getReportSpec().addField(newField);
                String item = (String) event.data;
                groupsList.add(item);
                groupsList.setSelection(groupsList.indexOf(item));
                updateState();
              }
              allFieldsList.deselectAll();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        } else {
          if (event.detail == DND.DROP_MOVE) {
            Field sourceField = null;
            for (int i = 0; i < detailsList.getSelectionIndices().length; i++) {
              sourceField = ReportSpecUtility.getDetails(reportDesigner.getReportSpec().getField())[detailsList.getSelectionIndices()[i]];
              reportDesigner.getReportSpec().removeField(sourceField);
              sourceField.setIsDetail(false);
              sourceField.setDisplayName(sourceField.getName() + ": $(" + sourceField.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
              sourceField.setGroupTotalsLabel("$(" + sourceField.getName() + ") Total"); //$NON-NLS-1$ //$NON-NLS-2$
              reportDesigner.getReportSpec().addField(sourceField);
            }
            detailsList.remove(detailsList.getSelectionIndices());
            updateState();
          } else if (event.detail == DND.DROP_COPY) {
            Field newField = null;
            for (int i = 0; i < detailsList.getSelectionIndices().length; i++) {
              Field sourceField = ReportSpecUtility.getDetails(reportDesigner.getReportSpec().getField())[detailsList.getSelectionIndices()[i]];
              newField = new Field();
              newField.setName(sourceField.getName());
              newField.setDisplayName(sourceField.getDisplayName());
              newField.setIsDetail(false);
              newField.setDisplayName(newField.getName() + ": $(" + newField.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
              newField.setGroupTotalsLabel("$(" + newField.getName() + ") Total"); //$NON-NLS-1$ //$NON-NLS-2$
              reportDesigner.getReportSpec().addField(newField);
            }
            updateState();
          }
          String item = (String) event.data;
          groupsList.add(item);
          groupsList.setSelection(groupsList.indexOf(item));
          updateState();
        }
      }
    });
  }

  /**
   * 
   * 
   * @param contentGroup
   */
  public void createAllFieldsComposite(Composite contentGroup) {
    allFieldsList = new List(contentGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    allFieldsList.setLayoutData(gridData);
    allFieldsList.addSelectionListener(this);
  }

  /**
   * 
   * 
   * @param contentGroup
   */
  public void createGroupsComposite(Composite contentGroup) {
    Composite groupsComposite = new Composite(contentGroup, SWT.NONE);
    GridLayout layout = new GridLayout(2, false);
    groupsComposite.setLayout(layout);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    // gridData.heightHint = 80;
    groupsComposite.setLayoutData(gridData);
    groupsList = new List(groupsComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    groupsList.setData("isDetail", Boolean.FALSE); //$NON-NLS-1$
    groupsList.deselectAll();
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    // gridData.widthHint = 240;
    gridData.verticalSpan = 4;
    groupsList.setLayoutData(gridData);
    groupsList.addSelectionListener(this);
    Label emptyLabel = new Label(groupsComposite, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, true);
    emptyLabel.setLayoutData(gridData);
    gridData = new GridData(SWT.LEFT, SWT.BOTTOM, false, false);
    groupUpButton = new PentahoSWTButton(groupsComposite, SWT.NONE, gridData, PentahoSWTButton.UP, ""); //$NON-NLS-1$
    groupUpButton.setLayoutData(gridData);
    groupUpButton.setEnabled(false);
    groupUpButton.addSelectionListener(this);
    gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
    groupDownButton = new PentahoSWTButton(groupsComposite, SWT.NONE, gridData, PentahoSWTButton.DOWN, ""); //$NON-NLS-1$
    groupDownButton.setLayoutData(gridData);
    groupDownButton.setEnabled(false);
    groupDownButton.addSelectionListener(this);
    emptyLabel = new Label(groupsComposite, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, true);
    emptyLabel.setLayoutData(gridData);
  }

  /**
   * 
   * 
   * @param contentGroup
   */
  public void createMoveComposite(Composite contentGroup) {
    Composite moveComposite = new Composite(contentGroup, SWT.NONE);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    moveComposite.setLayoutData(gridData);
    moveComposite.setLayout(new GridLayout(1, false));
    GridData gridData1 = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    addToGroupButton = new PentahoSWTButton(moveComposite, SWT.NONE, gridData1, PentahoSWTButton.NORMAL, Messages.getString("GroupAndDetailPanel.5")); //$NON-NLS-1$
    addToGroupButton.setLayoutData(gridData1);
    addToGroupButton.addSelectionListener(this);
    GridData gridData2 = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    addToDetailButton = new PentahoSWTButton(moveComposite, SWT.NONE, gridData2, PentahoSWTButton.NORMAL, Messages.getString("GroupAndDetailPanel.6")); //$NON-NLS-1$
    addToDetailButton.setLayoutData(gridData2);
    addToDetailButton.addSelectionListener(this);
    Label emptyLabel = new Label(moveComposite, SWT.NONE);
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
    emptyLabel.setLayoutData(gridData);
    GridData gridData3 = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    removeButton = new PentahoSWTButton(moveComposite, SWT.NONE, gridData3, PentahoSWTButton.NORMAL, Messages.getString("GroupAndDetailPanel.7")); //$NON-NLS-1$
    removeButton.setLayoutData(gridData3);
    removeButton.addSelectionListener(this);
    // int max = Math.max(gridData1.widthHint, Math.max(gridData2.widthHint, gridData3.widthHint));
    // gridData1.widthHint = max;
    // gridData2.widthHint = max;
    // gridData3.widthHint = max;
    numGroupsText = new Text(moveComposite, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
    numGroupsText.setBackground(ReportWizard.background);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
    gridData.heightHint = 200;
    numGroupsText.setLayoutData(gridData);
  }

  /**
   * 
   * 
   * @param contentGroup
   */
  public void createDetailsComposite(Composite contentGroup) {
    Composite detailsComposite = new Composite(contentGroup, SWT.NONE);
    detailsComposite.setLayout(new GridLayout(2, false));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    detailsComposite.setLayoutData(gridData);
    detailsList = new List(detailsComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    detailsList.setData("isDetail", Boolean.TRUE); //$NON-NLS-1$
    detailsList.deselectAll();
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    // gridData.widthHint = 240;
    gridData.verticalSpan = 4;
    detailsList.setLayoutData(gridData);
    detailsList.addSelectionListener(this);
    Label emptyLabel = new Label(detailsComposite, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, true);
    emptyLabel.setLayoutData(gridData);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    detailUpButton = new PentahoSWTButton(detailsComposite, SWT.NONE, gridData, PentahoSWTButton.UP, ""); //$NON-NLS-1$
    detailUpButton.setLayoutData(gridData);
    detailUpButton.setEnabled(false);
    detailUpButton.addSelectionListener(this);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    detailDownButton = new PentahoSWTButton(detailsComposite, SWT.NONE, gridData, PentahoSWTButton.DOWN, ""); //$NON-NLS-1$
    detailDownButton.setLayoutData(gridData);
    detailDownButton.setEnabled(false);
    detailDownButton.addSelectionListener(this);
    emptyLabel = new Label(detailsComposite, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, true);
    emptyLabel.setLayoutData(gridData);
  }

  /**
   * 
   * 
   * @param source
   */
  public boolean nextFired(WizardPanel source) {
    super.nextFired(source);
    if (this == source && stateChanged) {
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

  public boolean isContinueAllowed() {
    if (reportDesigner.getReportSpec().getTemplateSrc() != null) {
      int numGroups = ReportSpecUtility.getNumberOfGroupsInTemplate(reportDesigner.getReportSpec().getTemplateSrc());
      if (numGroups == 0) {
        // new rule based on Kurtis Cruzada: if template has no groups, then it is "flexible" can contain any number of groups
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
   */
  public void updateDesignerState() {
    // already handled
  }

  /**
   * 
   * 
   * @param dirty
   */
  public void dirtyFired(boolean dirty) {
    // updateState();
  }

  /**
   * 
   * 
   * @param e
   */
  public void widgetSelected(SelectionEvent e) {
    Object source = e.getSource();
    if (source == detailUpButton) {
      // use the detailFieldList to re-order the report spec fields
      Field fields[] = reportDesigner.getReportSpec().getField();
      Field details[] = new Field[ReportSpecUtility.getDetails(fields).length];
      SWTUtility.moveSelectedItems(detailsList, true);
      for (int i = 0; i < details.length; i++) {
        details[i] = ReportSpecUtility.getField(fields, detailsList.getItem(i), true);
        reportDesigner.getReportSpec().removeField(details[i]);
        reportDesigner.getReportSpec().addField(details[i]);
      }
    } else if (source == detailDownButton) {
      // use the detailFieldList to re-order the report spec fields
      Field fields[] = reportDesigner.getReportSpec().getField();
      Field details[] = new Field[ReportSpecUtility.getDetails(fields).length];
      SWTUtility.moveSelectedItems(detailsList, false);
      for (int i = 0; i < details.length; i++) {
        details[i] = ReportSpecUtility.getField(fields, detailsList.getItem(i), true);
        reportDesigner.getReportSpec().removeField(details[i]);
        reportDesigner.getReportSpec().addField(details[i]);
      }
    } else if (source == groupUpButton) {
      // use the detailFieldList to re-order the report spec fields
      Field fields[] = reportDesigner.getReportSpec().getField();
      Field groups[] = new Field[ReportSpecUtility.getGroups(fields).length];
      SWTUtility.moveSelectedItems(groupsList, true);
      for (int i = 0; i < groups.length; i++) {
        groups[i] = ReportSpecUtility.getField(fields, groupsList.getItem(i), false);
        reportDesigner.getReportSpec().removeField(groups[i]);
        reportDesigner.getReportSpec().addField(groups[i]);
      }
    } else if (source == groupDownButton) {
      // use the detailFieldList to re-order the report spec fields
      Field fields[] = reportDesigner.getReportSpec().getField();
      Field groups[] = new Field[ReportSpecUtility.getGroups(fields).length];
      SWTUtility.moveSelectedItems(groupsList, false);
      for (int i = 0; i < groups.length; i++) {
        groups[i] = ReportSpecUtility.getField(fields, groupsList.getItem(i), false);
        reportDesigner.getReportSpec().removeField(groups[i]);
        reportDesigner.getReportSpec().addField(groups[i]);
      }
    } else if (source == removeButton) {
      // remove all items from details/groups that are selected
      int selectedIndices[] = detailsList.getSelectionIndices();
      Arrays.sort(selectedIndices);
      for (int i = selectedIndices.length - 1; i >= 0; i--) {
        try {
          int index = selectedIndices[i];
          // using an array due to swt-swing compatibility issues
          detailsList.remove(new int[] { index });
          Field f = ReportSpecUtility.getDetails(reportDesigner.getReportSpec().getField())[index];
          reportDesigner.getReportSpec().removeField(f);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
      selectedIndices = groupsList.getSelectionIndices();
      Arrays.sort(selectedIndices);
      for (int i = selectedIndices.length - 1; i >= 0; i--) {
        try {
          int index = selectedIndices[i];
          // using an array due to swt-swing compatibility issues
          groupsList.remove(new int[] { index });
          Field f = ReportSpecUtility.getGroups(reportDesigner.getReportSpec().getField())[index];
          reportDesigner.getReportSpec().removeField(f);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
      removeButton.setEnabled(false);
      addToDetailButton.setEnabled(false);
      addToGroupButton.setEnabled(false);
    } else if (source == addToDetailButton) {
      try {
        String[] selectedItems = allFieldsList.getSelection();
        if (selectedItems == null || selectedItems.length == 0) {
          selectedItems = groupsList.getSelection();
        }
        for (int i = 0; (selectedItems != null) && (i < selectedItems.length); i++) {
          if (detailsList.getSelectionIndex() >= 0 && detailsList.getSelectionIndex() < detailsList.getItemCount() - 1) {
            detailsList.add(selectedItems[i], detailsList.getSelectionIndex() + 1);
          } else {
            detailsList.add(selectedItems[i]);
          }
          Field f = new Field();
          f.setName(selectedItems[i]);
          f.setDisplayName(selectedItems[i]);
          f.setIsDetail(true);
          f.setWidthLocked(false);
          f.setIsWidthPercent(true);
          if (reportDesigner.getTypeMap() != null && reportDesigner.getTypeMap().get(selectedItems[i]) != null) {
            f.setType(((Integer) reportDesigner.getTypeMap().get(selectedItems[i])).intValue());
          }
          if (f.getType() == Types.NUMERIC) {
            f.setExpression(reportDesigner.defaultNumericFunction); //$NON-NLS-1$
            f.setHorizontalAlignment("right"); //$NON-NLS-1$
          } else if (f.getType() == Types.VARCHAR) {
            f.setHorizontalAlignment("left"); //$NON-NLS-1$
          }
          reportDesigner.getReportSpec().addField(f);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else if (source == addToGroupButton) {
      try {
        String[] selectedItems = allFieldsList.getSelection();
        if (selectedItems == null || selectedItems.length == 0) {
          selectedItems = detailsList.getSelection();
        }
        for (int i = 0; (selectedItems != null) && (i < selectedItems.length); i++) {
          if (groupsList.getSelectionIndex() >= 0 && groupsList.getSelectionIndex() < groupsList.getItemCount() - 1) {
            groupsList.add(selectedItems[i], groupsList.getSelectionIndex() + 1);
          } else {
            groupsList.add(selectedItems[i]);
          }
          Field f = new Field();
          f.setName(selectedItems[i]);
          f.setDisplayName(selectedItems[i]);
          f.setIsDetail(false);
          f.setWidthLocked(false);
          f.setIsWidthPercent(true);
          if (reportDesigner.getTypeMap() != null && reportDesigner.getTypeMap().get(selectedItems[i]) != null) {
            f.setType(((Integer) reportDesigner.getTypeMap().get(selectedItems[i])).intValue());
          }
          if (f.getType() == Types.NUMERIC) {
            f.setExpression(reportDesigner.defaultNumericFunction); //$NON-NLS-1$
            f.setHorizontalAlignment("right"); //$NON-NLS-1$
          } else if (f.getType() == Types.VARCHAR) {
            f.setHorizontalAlignment("left"); //$NON-NLS-1$
          }
          f.setDisplayName(f.getName() + ": $(" + f.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
          f.setGroupTotalsLabel("$(" + f.getName() + ") Total"); //$NON-NLS-1$ //$NON-NLS-2$
          reportDesigner.getReportSpec().addField(f);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else if (source == groupsList) {
      detailsList.deselect(detailsList.getSelectionIndices());
      allFieldsList.deselect(allFieldsList.getSelectionIndices());
      addToDetailButton.setEnabled(true);
      addToGroupButton.setEnabled(false);
      removeButton.setEnabled(true);
      groupUpButton.setEnabled(true);
      groupDownButton.setEnabled(true);
      detailUpButton.setEnabled(false);
      detailDownButton.setEnabled(false);
    } else if (source == detailsList) {
      groupsList.deselect(groupsList.getSelectionIndices());
      allFieldsList.deselect(allFieldsList.getSelectionIndices());
      addToDetailButton.setEnabled(false);
      addToGroupButton.setEnabled(true);
      removeButton.setEnabled(true);
      groupUpButton.setEnabled(false);
      groupDownButton.setEnabled(false);
      detailUpButton.setEnabled(true);
      detailDownButton.setEnabled(true);
    } else if (source == allFieldsList) {
      groupsList.deselect(groupsList.getSelectionIndices());
      detailsList.deselect(detailsList.getSelectionIndices());
      addToDetailButton.setEnabled(true);
      addToGroupButton.setEnabled(true);
      removeButton.setEnabled(false);
      groupUpButton.setEnabled(false);
      groupDownButton.setEnabled(false);
      detailUpButton.setEnabled(false);
      detailDownButton.setEnabled(false);
    }
    // update addToGroupButton state
    int numGroups = 0;
    try {
      numGroups = ReportSpecUtility.getNumberOfGroupsInTemplate(reportDesigner.getReportSpec().getTemplateSrc());
    } catch (Exception ex) {
    }
    if (numGroups > 0 && numGroups == groupsList.getItemCount()) {
      addToGroupButton.setEnabled(false);
    } else {
      addToGroupButton.setEnabled(true);
    }
    super.widgetSelected(e);
  }

  /**
   * 
   * 
   * @param e
   */
  public void keyReleased(KeyEvent e) {
    super.keyPressed(e);
    if ((e.keyCode == SWT.DEL) && e.getSource() instanceof List) {
      List l = (List) e.getSource();
      l.remove(l.getSelectionIndices());
    }
  }

  /**
   * 
   * 
   * @param reportSpec
   */
  public void initWizardPanel(ReportSpec reportSpec) {
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

    int numGroups = 0;
    try {
      numGroups = ReportSpecUtility.getNumberOfGroupsInTemplate(reportDesigner.getReportSpec().getTemplateSrc());
    } catch (Exception e) {
    }

    Field fields[] = reportSpec.getField();
    // if template and number of template groups != number of actual groups
    if (reportSpec.getTemplateSrc() != null && numGroups > 0) {
      if (ReportSpecUtility.getGroupFieldNames(reportSpec).length == 0) {
        // attempt to setup initial groups
        FieldMapping fm[] = reportSpec.getFieldMapping();
        if (fm != null) {
          for (int i = 0; i < fm.length; i++) {
            if (fm[i].getType().equals("group")) { //$NON-NLS-1$
              for (int j = 0; j < fields.length; j++) {
                if (fields[j].getName().equalsIgnoreCase(fm[i].getValue())) {
                  fields[j].setIsDetail(false);
                  break;
                }
              }
            }
          }
        }
        // roll through the groups and determine which ones use group header based on template
        // update if we use the group
        for (int i = 0; i < fields.length; i++) {
          boolean headerExists = ReportSpecUtility.doesHeaderExistForGroup(reportDesigner.getReportSpec().getTemplateSrc(), i);
          fields[i].setCreateGroupHeader(!headerExists);
        }
      }
    }

    detailsList.setItems(ReportSpecUtility.getDetailFieldNames(reportDesigner.getReportSpec()));
    groupsList.setItems(ReportSpecUtility.getGroupFieldNames(reportDesigner.getReportSpec()));
    allFieldsList.setItems(allColumns);
    addToDetailButton.setEnabled(false);
    addToGroupButton.setEnabled(false);
    removeButton.setEnabled(false);
    detailUpButton.setEnabled(false);
    detailDownButton.setEnabled(false);
    groupUpButton.setEnabled(false);
    groupDownButton.setEnabled(false);
    if (numGroups != 0) {
      if (reportDesigner.getReportSpec().getTemplateSrc() != null) {
        numGroupsText
            .setText(Messages.getString("GroupAndDetailPanel.28") + numGroups + (numGroups > 1 ? Messages.getString("GroupAndDetailPanel.29") : Messages.getString("GroupAndDetailPanel.32")) + Messages.getString("GroupAndDetailPanel.31")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      } else {
        numGroupsText.setText(""); //$NON-NLS-1$
      }
    } else {
      numGroupsText.setText(""); //$NON-NLS-1$
    }
  }
}
