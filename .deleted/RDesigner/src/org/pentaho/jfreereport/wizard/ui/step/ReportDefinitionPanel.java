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
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.pentaho.core.connection.IPentahoConnection;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.WizardManager;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.IDirtyListener;
import org.pentaho.jfreereport.wizard.ui.WizardPanel;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTLine;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;

public class ReportDefinitionPanel extends WizardPanel implements IDirtyListener, ModifyListener {
  ReportWizard reportDesigner = null;
  Button templateButton = null;
  Button reportSpecButton = null;
  Text existingText = null;
  PentahoSWTButton existingFileBrowseButton = null;
  Composite reportSpecComposite = null;
  List templateList = null;
  Image thumbnailImage = null;
  Composite templateThumbnail = null;
  Text reportNameText = null;
  Text reportDescText = null;
  Composite lookAndFeelStackComposite = null;
  StackLayout stackLayout = null;
  boolean ignoreDirtyEvents = false;

  /**
   * @param parent
   * @param style
   * @param manager
   */
  public ReportDefinitionPanel(Composite parent, int style, WizardManager manager, ReportWizard reportDesigner) {
    super(parent, style, manager);
    this.reportDesigner = reportDesigner;
    initialize();
    SWTUtility.setBackground(getMainPanel(), ReportWizard.background);
  }

  public void initialize() {
    addDirtyListener(this);
    Composite c = getMainPanel();
    c.setLayout(new GridLayout(1, true));
    Composite contentGroup = new Composite(c, SWT.NONE);
    contentGroup.setLayout(new GridLayout(1, false));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    contentGroup.setLayoutData(gridData);
    Label stepLabel = new Label(contentGroup, SWT.NONE);
    stepLabel.setText(Messages.getString("ReportDefinitionPanel.4")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    stepLabel.setLayoutData(gridData);
    stepLabel.setFont(labelFont);
    Text stepText = new Text(contentGroup, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
    // Label stepText = new Label(contentGroup, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
    stepText.setText(Messages.getString("ReportDefinitionPanel.3")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    stepText.setLayoutData(gridData);
    stepText.setBackground(ReportWizard.background);
    stepText.setFont(textFont);
    SWTLine line1 = new SWTLine(contentGroup, SWT.NONE);
    line1.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
    gridData.heightHint = 30;
    line1.setLayoutData(gridData);
    Label nameTitleLabel = new Label(contentGroup, SWT.NONE);
    nameTitleLabel.setText(Messages.getString("ReportDefinitionPanel.2")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    stepLabel.setLayoutData(gridData);
    nameTitleLabel.setFont(labelFont);
    SWTLine line2 = new SWTLine(contentGroup, SWT.NONE);
    line2.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
    gridData.heightHint = 10;
    line2.setLayoutData(gridData);
    Label reportNameLabel = new Label(contentGroup, SWT.NONE);
    reportNameLabel.setText(Messages.getString("ReportDefinitionPanel.1")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    reportNameLabel.setLayoutData(gridData);
    reportNameLabel.setFont(labelFont);
    reportNameText = new Text(contentGroup, SWT.SINGLE | SWT.BORDER | SWT.LEFT);
    reportNameText.setText(reportDesigner.getReportSpec().getReportName());
    reportNameText.addModifyListener(this);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    gridData.widthHint = 250;
    reportNameText.setLayoutData(gridData);
    Label reportDescLabel = new Label(contentGroup, SWT.NONE);
    reportDescLabel.setText(Messages.getString("ReportDefinitionPanel.12")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    reportDescLabel.setLayoutData(gridData);
    reportDescLabel.setFont(labelFont);
    reportDescText = new Text(contentGroup, SWT.SINGLE | SWT.BORDER | SWT.LEFT);
    reportDescText.setText(reportDesigner.getReportSpec().getReportDesc());
    reportDescText.addModifyListener(this);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    gridData.widthHint = 500;
    reportDescText.setLayoutData(gridData);
    Label instructionLabel = new Label(contentGroup, SWT.NONE);
    instructionLabel.setText(Messages.getString("ReportDefinitionPanel.7")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    instructionLabel.setLayoutData(gridData);
    instructionLabel.setFont(labelFont);
    SWTLine line3 = new SWTLine(contentGroup, SWT.NONE);
    line3.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
    gridData.heightHint = 10;
    line3.setLayoutData(gridData);
    Composite lookAndFeelComposite = new Composite(contentGroup, SWT.NONE);
    lookAndFeelComposite.setLayout(new GridLayout(3, false));
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    lookAndFeelComposite.setLayoutData(gridData);
    templateButton = new Button(lookAndFeelComposite, SWT.RADIO);
    templateButton.setText(Messages.getString("ReportDefinitionPanel.6")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    templateButton.setLayoutData(gridData);
    templateButton.addSelectionListener(this);
    reportSpecButton = new Button(lookAndFeelComposite, SWT.RADIO);
    reportSpecButton.setText(Messages.getString("ReportDefinitionPanel.8")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    reportSpecButton.setLayoutData(gridData);
    reportSpecButton.addSelectionListener(this);
    templateThumbnail = new Composite(lookAndFeelComposite, SWT.NONE);
    // templateThumbnail.setText(Messages.getString("ReportDefinitionPanel.9")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    templateThumbnail.setLayoutData(gridData);
    templateThumbnail.addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        if (thumbnailImage != null) {
          e.gc.drawImage(thumbnailImage, 10, 20);
        } else {
          // clear needed?
        }
      }
    });
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.verticalSpan = 3;
    templateThumbnail.setLayoutData(gridData);
    lookAndFeelStackComposite = new Composite(lookAndFeelComposite, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
    gridData.widthHint = 300;
    gridData.horizontalSpan = 2;
    lookAndFeelStackComposite.setLayoutData(gridData);
    stackLayout = new StackLayout();
    lookAndFeelStackComposite.setLayout(stackLayout);
    templateList = new List(lookAndFeelStackComposite, SWT.SINGLE | SWT.BORDER);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    templateList.setLayoutData(gridData);
    templateList.addSelectionListener(this);
    populateTemplateList(templateList);
    templateList.setSelection(0);
    reportSpecComposite = new Composite(lookAndFeelStackComposite, SWT.NONE);
    reportSpecComposite.setLayout(new GridLayout(2, false));
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    reportSpecComposite.setLayoutData(gridData);
    existingText = new Text(reportSpecComposite, SWT.SINGLE | SWT.BORDER);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    existingText.setLayoutData(gridData);
    existingText.addModifyListener(this);
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    existingFileBrowseButton = new PentahoSWTButton(reportSpecComposite, SWT.PUSH, gridData, PentahoSWTButton.NORMAL, Messages.getString("ReportDefinitionPanel.5")); //$NON-NLS-1$
    existingFileBrowseButton.addSelectionListener(this);
    existingFileBrowseButton.setLayoutData(gridData);
    stackLayout.topControl = templateList;
    stackLayout.topControl.update();
  }

  public void populateTemplateList(List templateList) {
    // list files from 'templates' folder
    LinkedList templates = new LinkedList();
    try {
      File templatesFolder = new File(reportDesigner.templatePath);
      if (templatesFolder.exists()) {
        if (templateList != null) {
          FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
              for (int i = 0; i < SWTUtility.REPORT_ARCHIVE_FILTERS.length; i++) {
                if (name.endsWith(SWTUtility.REPORT_ARCHIVE_FILTERS[i])) {
                  return true;
                }
              }
              return false;
            }
          };
          String files[] = templatesFolder.list(filter);
          templates.add(Messages.getString("ReportDefinitionPanel.11")); //$NON-NLS-1$
          for (int i = 0; i < files.length; i++) {
            String file = files[i];
            int index = file.indexOf(".xreportarc");
            if (index != -1) {
              file = file.substring(0, index);
            }
            templates.add(file);
          }
          templateList.setItems((String[]) templates.toArray(new String[0]));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void widgetSelected(SelectionEvent e) {
    if (!ignoreDirtyEvents) {
      super.widgetSelected(e);
    }
    if (e.getSource() == reportSpecButton) {
      stateChanged = true;
      if (reportSpecButton.getSelection()) {
        stackLayout.topControl = reportSpecComposite;
      }
      lookAndFeelStackComposite.layout();
    } else if (e.getSource() == templateButton) {
      stateChanged = true;
      if (templateButton.getSelection()) {
        stackLayout.topControl = templateList;
      }
      lookAndFeelStackComposite.layout();
    } else if (e.getSource() == existingFileBrowseButton) {
      FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
      dialog.setFilterNames(new String[] { Messages.getString("ReportWizard.USER_REPORT_SPEC_DOCUMENTS"), Messages.getString("ReportWizard.USER_ALL_XML_FILES") }); //$NON-NLS-1$ //$NON-NLS-2$
      dialog.setFilterExtensions(SWTUtility.REPORT_SPEC_FILTERS);
      String path = dialog.open();
      if (!StringUtils.isEmpty(path)) {
        reportDesigner.reportSpecFilePath = path;
        reportDesigner.doOpen(reportDesigner.reportSpecFilePath, false);
        reportDesigner.templateZipPath = null;
        existingText.setText(reportDesigner.reportSpecFilePath == null ? "" : reportDesigner.reportSpecFilePath); //$NON-NLS-1$
        stateChanged = true;
      }
    } else if (e.getSource() == templateList) {
      setTemplateThumbnail();
    }
    updateState();
    existingFileBrowseButton.redraw();
  }

  public void setTemplateThumbnail() {
    try {
      if (templateList.getSelectionIndex() > 0) {
        // crack archive, pull out thumbnail.png (or jpg)
        ZipFile zipFile = new ZipFile(reportDesigner.templatePath + "/" + templateList.getSelection()[0] + ".xreportarc"); //$NON-NLS-1$
        ZipEntry thumbnailEntry = zipFile.getEntry("thumbnail.png"); //$NON-NLS-1$
        thumbnailImage = new Image(getDisplay(), zipFile.getInputStream(thumbnailEntry));
        zipFile.close();
        templateThumbnail.redraw();
      } else {
        InputStream imageStream = getClass().getResourceAsStream("/images/img_blankReport.png"); //$NON-NLS-1$
        thumbnailImage = new Image(getDisplay(), imageStream); //$NON-NLS-1$
        templateThumbnail.redraw();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * 
   * 
   * @return $objectType$
   */
  public boolean isContinueAllowed() {
    boolean continueAllowed = false;
    if (reportSpecButton.getSelection() && existingText.getText() != null && !"".equals(existingText.getText())) { //$NON-NLS-1$
      continueAllowed = true;
    } else if (templateButton.getSelection()) {
      continueAllowed = true;
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
    if (stateChanged && this == source) {
      if (reportSpecButton.getSelection()) {
        reportDesigner.doOpen(reportDesigner.reportSpecFilePath, false);
      } else if (templateButton.getSelection()) {
        if (templateList.getSelectionIndex() == 0) {
          reportDesigner.doNew(false);
          reportDesigner.getReportSpec().setReportName(reportNameText.getText());
          reportDesigner.getReportSpec().setReportDesc(reportDescText.getText());
        } else {
          selectTemplate();
        }
      }
      stateChanged = false;
    }
    return true;
  }

  public void selectTemplate() {
    if (!StringUtils.isEmpty(reportDesigner.getReportSpec().getTemplateSrc()) || reportDesigner.getReportSpec().getField().length > 0) {
      // warn user they are about to overwrite their template settings
      MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO);
      mb.setText("Warning");
      mb.setMessage("Changing the template on an existing report may reset some report settings.  Continue with selection?"); //$NON-NLS-1$
      if (mb.open() == SWT.YES) {
        ReportSpec original = reportDesigner.getReportSpec();
        IPentahoConnection originalConnection = reportDesigner.getPentahoConnection();
        reportDesigner.doOpenTemplate(reportDesigner.templatePath + "/" + templateList.getSelection()[0] + ".xreportarc", false); //$NON-NLS-1$
        ReportSpec afterTemplate = reportDesigner.getReportSpec();
        afterTemplate.setField(original.getField());
        afterTemplate.setReportSpecChoice(original.getReportSpecChoice());
        afterTemplate.setQuery(original.getQuery());
        afterTemplate.setIsMDX(original.getIsMDX());
        afterTemplate.setIsMQL(original.getIsMQL());
        afterTemplate.setMqlQuery(original.getMqlQuery());
        afterTemplate.setMondrianCubeDefinitionPath(original.getMondrianCubeDefinitionPath());
        reportDesigner.setPentahoConnection(originalConnection);
        int[] selectedIndices = templateList.getSelectionIndices();
        reportDesigner.initWizardPanels();
        templateList.setSelection(selectedIndices);
        Event event = new Event();
        event.widget = templateList;
        SelectionEvent selectionEvent = new SelectionEvent(event);
        widgetSelected(selectionEvent);
      }
    } else {
      reportDesigner.doOpenTemplate(reportDesigner.templatePath + "/" + templateList.getSelection()[0] + ".xreportarc", false); //$NON-NLS-1$
    }
  }

  public boolean previewFired(WizardPanel source) {
    if (stateChanged && this == source) {
      selectTemplate();
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
  }

  public void keyReleased(KeyEvent e) {
    if (e.widget == reportNameText && reportNameText != null) {
      reportDesigner.getReportSpec().setReportName(reportNameText.getText());
    } else if (e.widget == reportDescText && reportDescText != null) {
      reportDesigner.getReportSpec().setReportDesc(reportDescText.getText());
    }
  }

  /**
   * 
   * 
   * @param reportSpec
   */
  public void initWizardPanel(ReportSpec reportSpec) {
    ignoreDirtyEvents = true;
    if (reportSpec != null) {
      if (reportSpec.getTemplateSrc() != null) {
        templateButton.setSelection(true);
        reportSpecButton.setSelection(false);
        Event event = new Event();
        event.widget = templateButton;
        SelectionEvent selectionEvent = new SelectionEvent(event);
        widgetSelected(selectionEvent);
        stateChanged = false;
      } else if (reportDesigner.reportSpecFilePath != null) {
        reportSpecButton.setSelection(true);
        templateButton.setSelection(false);
        existingText.setText(reportDesigner.reportSpecFilePath == null ? "" : reportDesigner.reportSpecFilePath); //$NON-NLS-1$
        Event event = new Event();
        event.widget = reportSpecButton;
        SelectionEvent selectionEvent = new SelectionEvent(event);
        widgetSelected(selectionEvent);
        stateChanged = false;
      } else {
        reportSpecButton.setSelection(false);
        templateButton.setSelection(true);
        Event event = new Event();
        event.widget = templateButton;
        SelectionEvent selectionEvent = new SelectionEvent(event);
        widgetSelected(selectionEvent);
        stateChanged = false;
      }

      reportNameText.setText(reportSpec.getReportName());
      reportDescText.setText(reportSpec.getReportDesc());

    }
    setTemplateThumbnail();
    ignoreDirtyEvents = false;
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
  }
}
