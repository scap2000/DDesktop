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
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.pentaho.jfreereport.castormodel.jfree.types.PageFormats;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.castormodel.reportspec.Watermark;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.WizardManager;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.IDirtyListener;
import org.pentaho.jfreereport.wizard.ui.WizardPanel;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTLine;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportSpecUtility;

public class PageSetupPanel extends WizardPanel implements IDirtyListener, ModifyListener {
  /**
   * 
   */
  ReportWizard reportDesigner = null;
  /**
   * 
   */
  Combo pageFormatCombo = null;
  /**
   * 
   */
  Spinner leftMarginSpinner = null;
  /**
   * 
   */
  Spinner rightMarginSpinner = null;
  /**
   * 
   */
  Spinner topMarginSpinner = null;
  /**
   * 
   */
  Spinner bottomMarginSpinner = null;
  /**
   * 
   */
  Text imageURLText = null;
  /**
   * 
   */
  PentahoSWTButton browseButton = null;
  /**
   * 
   */
  Spinner xSpinner = null;
  /**
   * 
   */
  Spinner ySpinner = null;
  /**
   * 
   */
  Spinner widthSpinner = null;
  /**
   * 
   */
  Spinner heightSpinner = null;
  /**
   * 
   */
  Button scaleButton = null;
  /**
   * 
   */
  Button keepAspectRatioButton = null;
  Composite imageGroup = null;
  Button pageOrientation = null;
  Button landscapeOrientation = null;
  Button customPageButton = null;
  Button standardPageButton = null;
  Composite customPageComposite = null;
  Spinner customHeightSpinner = null;
  Spinner customWidthSpinner = null;
  Button useWatermarkButton = null;

  /**
   * Creates a new PageSetupPanel object.
   * 
   * @param parent
   * @param style
   * @param manager
   * @param reportDesigner
   */
  public PageSetupPanel(Composite parent, int style, WizardManager manager, ReportWizard reportDesigner) {
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
    Composite contentGroup = new Composite(c, SWT.NONE);
    contentGroup.setLayout(new GridLayout(3, false));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    contentGroup.setLayoutData(gridData);
    contentGroup.setBackground(ReportWizard.background);
    Label stepLabel = new Label(contentGroup, SWT.NONE);
    stepLabel.setText(Messages.getString("PageSetupPanel.22")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 3;
    stepLabel.setLayoutData(gridData);
    stepLabel.setFont(labelFont);
    Text stepText = new Text(contentGroup, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
    stepText.setText(Messages.getString("PageSetupPanel.21")); //$NON-NLS-1$
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
    // create orientation/pageformat
    createPaperPanel(contentGroup);
    SWTLine vLine = new SWTLine(contentGroup, SWT.NONE);
    vLine.setHorizontal(false);
    gridData = new GridData(SWT.TOP, SWT.FILL, false, true);
    vLine.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData.widthHint = 5;
    gridData.verticalSpan = 2;
    vLine.setLayoutData(gridData);
    createWatermarkPanel(contentGroup);
    createMarginPanel(contentGroup);
    updateState();
    SWTUtility.setBackground(c, ReportWizard.background);
  }

  public void createMarginPanel(Composite parent) {
    GridData gridData = new GridData(SWT.FILL, SWT.TOP, false, false);
    Group container = new Group(parent, SWT.NONE);
    container.setText(Messages.getString("PageSetupPanel.20")); //$NON-NLS-1$
    container.setLayout(new GridLayout(4, false));
    container.setLayoutData(gridData);
    Label leftMarginLabel = new Label(container, SWT.RIGHT);
    leftMarginLabel.setText(Messages.getString("PageSetupPanel.24")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    leftMarginLabel.setLayoutData(gridData);
    leftMarginSpinner = new Spinner(container, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    leftMarginSpinner.setLayoutData(gridData);
    leftMarginSpinner.setSelection(reportDesigner.getReportSpec().getLeftMargin());
    leftMarginSpinner.addSelectionListener(this);
    Label topMarginLabel = new Label(container, SWT.RIGHT);
    topMarginLabel.setText(Messages.getString("PageSetupPanel.26")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    topMarginSpinner = new Spinner(container, SWT.BORDER | SWT.SINGLE);
    topMarginLabel.setLayoutData(gridData);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    topMarginSpinner.setLayoutData(gridData);
    topMarginSpinner.setSelection(reportDesigner.getReportSpec().getTopMargin());
    topMarginSpinner.addSelectionListener(this);
    Label rightMarginLabel = new Label(container, SWT.RIGHT);
    rightMarginLabel.setText(Messages.getString("PageSetupPanel.25")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    rightMarginLabel.setLayoutData(gridData);
    rightMarginSpinner = new Spinner(container, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    rightMarginSpinner.setLayoutData(gridData);
    rightMarginSpinner.setSelection(reportDesigner.getReportSpec().getRightMargin());
    rightMarginSpinner.addSelectionListener(this);
    Label bottomMarginLabel = new Label(container, SWT.RIGHT);
    bottomMarginLabel.setText(Messages.getString("PageSetupPanel.27")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    bottomMarginLabel.setLayoutData(gridData);
    bottomMarginSpinner = new Spinner(container, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    bottomMarginSpinner.setLayoutData(gridData);
    bottomMarginSpinner.setSelection(reportDesigner.getReportSpec().getBottomMargin());
    bottomMarginSpinner.addSelectionListener(this);
  }

  /**
   * 
   * 
   * @param parent
   */
  public void createWatermarkPreviewPanel(Composite parent) {
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.verticalSpan = 2;
    Composite container = new Composite(parent, SWT.NONE);
    container.setLayout(new GridLayout(2, false));
    container.setLayoutData(gridData);
    // container.setText(Messages.getString("PageSetupPanel.37")); //$NON-NLS-1$
    imageGroup = new Composite(container, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    imageGroup.setLayoutData(gridData);
    imageGroup.addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        if (reportDesigner.getReportSpec().getWatermark() != null && reportDesigner.getReportSpec().getWatermark().getSrc() != null) {
          Image watermarkImage = new Image(getDisplay(), reportDesigner.getReportSpec().getWatermark().getSrc()); //$NON-NLS-1$
          GC gc = e.gc;
          int width = (int) (imageGroup.getBounds().width * (widthSpinner.getSelection() * 0.01));
          int height = (int) (imageGroup.getBounds().height * (heightSpinner.getSelection() * 0.01));
          int x = (int) (imageGroup.getBounds().width * xSpinner.getSelection() * 0.01);
          int y = (int) (imageGroup.getBounds().height * ySpinner.getSelection() * 0.01);
          // if it scaled and ascpect ratio is not locked, draw stretched to fit
          if (scaleButton.getSelection()) {
            gc.drawImage(watermarkImage, 0, 0, watermarkImage.getImageData().width, watermarkImage.getImageData().height, x, y, width, height);
            // if not scale, draw image exactly as it is
          } else if (!scaleButton.getSelection()) {
            gc.drawImage(watermarkImage, x, y);
          }
        }
      }
    });
  }

  /**
   * 
   * 
   * @param parent
   */
  public void createWatermarkPanel(Composite parent) {
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    gridData.verticalSpan = 2;
    Composite container = new Composite(parent, SWT.NONE);
    container.setLayout(new GridLayout(2, false));
    container.setLayoutData(gridData);
    Label label = new Label(container, SWT.NONE);
    label.setText(Messages.getString("PageSetupPanel.28")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    label.setLayoutData(gridData);
    label.setFont(labelFont);
    SWTLine line = new SWTLine(container, SWT.NONE);
    line.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.heightHint = 5;
    gridData.horizontalSpan = 2;
    line.setLayoutData(gridData);
    Composite urlComposite = new Composite(container, SWT.NONE);
    urlComposite.setLayout(new GridLayout(3, false));
    gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    gridData.horizontalSpan = 2;
    urlComposite.setLayoutData(gridData);
    useWatermarkButton = new Button(urlComposite, SWT.CHECK);
    useWatermarkButton.setText(Messages.getString("PageSetupPanel.19")); //$NON-NLS-1$
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    useWatermarkButton.setLayoutData(gridData);
    useWatermarkButton.addSelectionListener(this);
    imageURLText = new Text(urlComposite, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    imageURLText.setLayoutData(gridData);
    // imageURLText.setBackground(ReportWizard.background);
    imageURLText.setFont(textFont);
    imageURLText.addModifyListener(this);
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
    browseButton = new PentahoSWTButton(urlComposite, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("PageSetupPanel.29")); //$NON-NLS-1$
    browseButton.addSelectionListener(this);
    browseButton.setLayoutData(gridData);
    Composite optionsGroup = new Composite(container, SWT.NONE);
    optionsGroup.setLayout(new GridLayout(2, false));
    gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    gridData.horizontalSpan = 2;
    optionsGroup.setLayoutData(gridData);
    Group sizeGroup = new Group(optionsGroup, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    sizeGroup.setLayoutData(gridData);
    sizeGroup.setText(Messages.getString("PageSetupPanel.18")); //$NON-NLS-1$
    sizeGroup.setLayout(new GridLayout(4, false));
    Label widthLabel = new Label(sizeGroup, SWT.NONE);
    widthLabel.setText(Messages.getString("PageSetupPanel.31")); //$NON-NLS-1$
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
    widthLabel.setLayoutData(gridData);
    widthSpinner = new Spinner(sizeGroup, SWT.BORDER);
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    widthSpinner.setLayoutData(gridData);
    widthSpinner.addSelectionListener(this);
    Label heightLabel = new Label(sizeGroup, SWT.NONE);
    heightLabel.setText(Messages.getString("PageSetupPanel.32")); //$NON-NLS-1$
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
    heightLabel.setLayoutData(gridData);
    heightSpinner = new Spinner(sizeGroup, SWT.BORDER);
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    heightSpinner.setLayoutData(gridData);
    heightSpinner.addSelectionListener(this);
    scaleButton = new Button(sizeGroup, SWT.CHECK);
    scaleButton.setText(Messages.getString("PageSetupPanel.35")); //$NON-NLS-1$
    scaleButton.addSelectionListener(this);
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    gridData.horizontalSpan = 4;
    scaleButton.setLayoutData(gridData);
    keepAspectRatioButton = new Button(sizeGroup, SWT.CHECK);
    keepAspectRatioButton.setText(Messages.getString("PageSetupPanel.36")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    gridData.horizontalSpan = 4;
    keepAspectRatioButton.setLayoutData(gridData);
    keepAspectRatioButton.addSelectionListener(this);
    Group positionGroup = new Group(optionsGroup, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    positionGroup.setLayoutData(gridData);
    positionGroup.setText(Messages.getString("PageSetupPanel.17")); //$NON-NLS-1$
    positionGroup.setLayout(new GridLayout(4, false));
    Label xLabel = new Label(positionGroup, SWT.NONE);
    xLabel.setText(Messages.getString("PageSetupPanel.33")); //$NON-NLS-1$
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
    xLabel.setLayoutData(gridData);
    xSpinner = new Spinner(positionGroup, SWT.BORDER);
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    xSpinner.setLayoutData(gridData);
    xSpinner.addSelectionListener(this);
    Label yLabel = new Label(positionGroup, SWT.NONE);
    yLabel.setText(Messages.getString("PageSetupPanel.34")); //$NON-NLS-1$
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
    yLabel.setLayoutData(gridData);
    ySpinner = new Spinner(positionGroup, SWT.BORDER);
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    ySpinner.setLayoutData(gridData);
    ySpinner.addSelectionListener(this);
    createWatermarkPreviewPanel(container);
  }

  /**
   * 
   * 
   * @param parent
   */
  public void createPaperPanel(Composite parent) {
    Composite container = new Composite(parent, SWT.NONE);
    container.setBackground(ReportWizard.background);
    GridLayout gridLayout = new GridLayout(1, true);
    gridLayout.marginWidth = 0;
    container.setLayout(gridLayout);
    GridData gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    container.setLayoutData(gridData);
    Label label = new Label(container, SWT.NONE);
    label.setText(Messages.getString("PageSetupPanel.38")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.TOP, false, false);
    label.setLayoutData(gridData);
    label.setFont(labelFont);
    SWTLine line = new SWTLine(container, SWT.NONE);
    line.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, false, false);
    gridData.heightHint = 5;
    line.setLayoutData(gridData);
    Group orientationGroup = new Group(container, SWT.NONE);
    SWTUtility.setBackground(orientationGroup, ReportWizard.background);
    orientationGroup.setBackground(ReportWizard.background);
    orientationGroup.setLayout(new GridLayout(4, false));
    orientationGroup.setText(Messages.getString("PageSetupPanel.40")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    orientationGroup.setLayoutData(gridData);
    Image landscapeImage = null;
    Image portraitImage = null;
    try {
      InputStream imageStream = getClass().getResourceAsStream("/images/img_landscape.png"); //$NON-NLS-1$
      landscapeImage = new Image(getDisplay(), imageStream); //$NON-NLS-1$
      imageStream = getClass().getResourceAsStream("/images/img_portrait.png"); //$NON-NLS-1$
      portraitImage = new Image(getDisplay(), imageStream); //$NON-NLS-1$
    } catch (Exception e) {
    }
    try {
      pageOrientation = new Button(orientationGroup, SWT.RADIO);
      pageOrientation.setBackground(ReportWizard.background);
      gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
      pageOrientation.setLayoutData(gridData);
      pageOrientation.setImage(portraitImage);
      pageOrientation.addSelectionListener(this);
      Label portraitLabel = new Label(orientationGroup, SWT.NONE);
      portraitLabel.setText(Messages.getString("PageSetupPanel.14")); //$NON-NLS-1$
      gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
      portraitLabel.setLayoutData(gridData);
      landscapeOrientation = new Button(orientationGroup, SWT.RADIO);
      gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
      landscapeOrientation.setLayoutData(gridData);
      landscapeOrientation.setImage(landscapeImage);
      landscapeOrientation.addSelectionListener(this);
      Label landscapeLabel = new Label(orientationGroup, SWT.NONE);
      landscapeLabel.setText(Messages.getString("PageSetupPanel.13")); //$NON-NLS-1$
      gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
      landscapeLabel.setLayoutData(gridData);
      if (reportDesigner.getReportSpec().getOrientation().equals("portrait")) { //$NON-NLS-1$
        pageOrientation.setSelection(true);
        landscapeOrientation.setSelection(false);
      } else {
        pageOrientation.setSelection(false);
        landscapeOrientation.setSelection(true);
      }
    } catch (Exception e) {
    }
    Group pageFormatGroup = new Group(container, SWT.NONE);
    pageFormatGroup.setBackground(ReportWizard.background);
    pageFormatGroup.setLayout(new GridLayout(4, false));
    pageFormatGroup.setText(Messages.getString("PageSetupPanel.11")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    pageFormatGroup.setLayoutData(gridData);
    try {
      standardPageButton = new Button(pageFormatGroup, SWT.RADIO);
      standardPageButton.setText(Messages.getString("PageSetupPanel.10")); //$NON-NLS-1$
      gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
      gridData.horizontalSpan = 2;
      standardPageButton.setLayoutData(gridData);
      standardPageButton.addSelectionListener(this);
      pageFormatCombo = new Combo(pageFormatGroup, SWT.READ_ONLY);
      gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
      gridData.horizontalSpan = 2;
      pageFormatCombo.setLayoutData(gridData);
      String pageFormats[] = ReportSpecUtility.enumerationToStringArray(PageFormats.enumerate());
      Arrays.sort(pageFormats);
      pageFormatCombo.setItems(pageFormats);
      pageFormatCombo.select(pageFormatCombo.indexOf(reportDesigner.getReportSpec().getPageFormat()));
      pageFormatCombo.addSelectionListener(this);
      customPageButton = new Button(pageFormatGroup, SWT.RADIO);
      customPageButton.setText(Messages.getString("PageSetupPanel.9")); //$NON-NLS-1$
      gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
      gridData.horizontalSpan = 4;
      customPageButton.setLayoutData(gridData);
      customPageButton.addSelectionListener(this);
    } catch (Exception e) {
    }
    customPageComposite = new Composite(pageFormatGroup, SWT.NONE);
    customPageComposite.setBackground(ReportWizard.background);
    customPageComposite.setLayout(new GridLayout(4, false));
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    gridData.horizontalSpan = 4;
    customPageComposite.setLayoutData(gridData);
    Label widthLabel = new Label(customPageComposite, SWT.NONE);
    widthLabel.setText(Messages.getString("PageSetupPanel.8")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    widthLabel.setLayoutData(gridData);
    customWidthSpinner = new Spinner(customPageComposite, SWT.BORDER);
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    customWidthSpinner.setLayoutData(gridData);
    customWidthSpinner.addSelectionListener(this);
    customWidthSpinner.setMaximum(Integer.MAX_VALUE);
    Label heightLabel = new Label(customPageComposite, SWT.NONE);
    heightLabel.setText(Messages.getString("PageSetupPanel.7")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    heightLabel.setLayoutData(gridData);
    customHeightSpinner = new Spinner(customPageComposite, SWT.BORDER);
    customHeightSpinner.setMaximum(Integer.MAX_VALUE);
    gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
    customHeightSpinner.setLayoutData(gridData);
    customHeightSpinner.addSelectionListener(this);
    SWTUtility.setBackground(container, ReportWizard.background);

  }

  /**
   * 
   * 
   * @param e
   */
  public void widgetSelected(SelectionEvent e) {
    stateChanged = true;
    if (e.getSource() == browseButton) {
      FileDialog openDialog = new FileDialog(getShell(), SWT.OPEN);
      openDialog.setFilterExtensions(SWTUtility.IMAGE_FILTER_STRINGS);
      // openDialog.setFilterNames(new String[] { Messages.getString("PageSetupPanel.42") }); //$NON-NLS-1$
      String text = openDialog.open();
      if (text != null) {
        imageURLText.setText(text);
      }
      if (reportDesigner.getReportSpec().getWatermark() != null) {
        reportDesigner.getReportSpec().getWatermark().setSrc(text);
      } else {
        Watermark wm = new Watermark();
        wm.setSrc(text);
        reportDesigner.getReportSpec().setWatermark(wm);
      }
      imageGroup.redraw();
    } else if (e.getSource() == pageFormatCombo) {
      reportDesigner.getReportSpec().setPageFormat(pageFormatCombo.getItem(pageFormatCombo.getSelectionIndex()));
    } else if (e.getSource() == pageOrientation) {
      reportDesigner.getReportSpec().setOrientation("portrait"); //$NON-NLS-1$
    } else if (e.getSource() == landscapeOrientation) {
      reportDesigner.getReportSpec().setOrientation("landscape"); //$NON-NLS-1$
    } else if (e.getSource() == widthSpinner) {
      if (reportDesigner.getReportSpec().getWatermark() != null) {
        reportDesigner.getReportSpec().getWatermark().setWidth(widthSpinner.getSelection());
      } else {
        Watermark wm = new Watermark();
        wm.setWidth(widthSpinner.getSelection());
        reportDesigner.getReportSpec().setWatermark(wm);
      }
      imageGroup.redraw();
    } else if (e.getSource() == heightSpinner) {
      if (reportDesigner.getReportSpec().getWatermark() != null) {
        reportDesigner.getReportSpec().getWatermark().setHeight(heightSpinner.getSelection());
      } else {
        Watermark wm = new Watermark();
        wm.setHeight(heightSpinner.getSelection());
        reportDesigner.getReportSpec().setWatermark(wm);
      }
      imageGroup.redraw();
    } else if (e.getSource() == xSpinner) {
      if (reportDesigner.getReportSpec().getWatermark() != null) {
        reportDesigner.getReportSpec().getWatermark().setX(xSpinner.getSelection());
      } else {
        Watermark wm = new Watermark();
        wm.setX(xSpinner.getSelection());
        reportDesigner.getReportSpec().setWatermark(wm);
      }
      imageGroup.redraw();
    } else if (e.getSource() == ySpinner) {
      if (reportDesigner.getReportSpec().getWatermark() != null) {
        reportDesigner.getReportSpec().getWatermark().setY(ySpinner.getSelection());
      } else {
        Watermark wm = new Watermark();
        wm.setY(ySpinner.getSelection());
        reportDesigner.getReportSpec().setWatermark(wm);
      }
      imageGroup.redraw();
    } else if (e.getSource() == scaleButton) {
      if (reportDesigner.getReportSpec().getWatermark() != null) {
        reportDesigner.getReportSpec().getWatermark().setScale(scaleButton.getSelection());
      } else {
        Watermark wm = new Watermark();
        wm.setScale(scaleButton.getSelection());
        reportDesigner.getReportSpec().setWatermark(wm);
      }
      imageGroup.redraw();
    } else if (e.getSource() == keepAspectRatioButton) {
      if (reportDesigner.getReportSpec().getWatermark() != null) {
        reportDesigner.getReportSpec().getWatermark().setKeepAspectRatio(keepAspectRatioButton.getSelection());
      } else {
        Watermark wm = new Watermark();
        wm.setKeepAspectRatio(keepAspectRatioButton.getSelection());
        reportDesigner.getReportSpec().setWatermark(wm);
      }
      imageGroup.redraw();
    } else if (e.getSource() == leftMarginSpinner) {
      reportDesigner.getReportSpec().setLeftMargin(leftMarginSpinner.getSelection());
    } else if (e.getSource() == rightMarginSpinner) {
      reportDesigner.getReportSpec().setRightMargin(rightMarginSpinner.getSelection());
    } else if (e.getSource() == topMarginSpinner) {
      reportDesigner.getReportSpec().setTopMargin(topMarginSpinner.getSelection());
    } else if (e.getSource() == bottomMarginSpinner) {
      reportDesigner.getReportSpec().setBottomMargin(bottomMarginSpinner.getSelection());
    } else if (e.getSource() == standardPageButton) {
      SWTUtility.setEnabled(customPageComposite, false);
      pageFormatCombo.setEnabled(true);
      reportDesigner.getReportSpec().setUseCustomPageFormat(false);
    } else if (e.getSource() == customPageButton) {
      SWTUtility.setEnabled(customPageComposite, true);
      pageFormatCombo.setEnabled(false);
      reportDesigner.getReportSpec().setUseCustomPageFormat(true);
    } else if (e.getSource() == customHeightSpinner) {
      reportDesigner.getReportSpec().setCustomPageFormatHeight(customHeightSpinner.getSelection());
    } else if (e.getSource() == customWidthSpinner) {
      reportDesigner.getReportSpec().setCustomPageFormatWidth(customWidthSpinner.getSelection());
    } else if (e.getSource() == useWatermarkButton) {
      if (reportDesigner.getReportSpec().getWatermark() != null) {
        reportDesigner.getReportSpec().getWatermark().setUseWatermark(useWatermarkButton.getSelection());
      }
    }
    super.widgetSelected(e);
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
    if (reportDesigner.getReportSpec().getUseCustomPageFormat()) {
      pageFormatCombo.setEnabled(false);
      SWTUtility.setEnabled(customPageComposite, true);
      customPageButton.setSelection(true);
      standardPageButton.setSelection(false);
    } else {
      pageFormatCombo.setEnabled(true);
      SWTUtility.setEnabled(customPageComposite, false);
      customPageButton.setSelection(false);
      standardPageButton.setSelection(true);
    }
  }

  /**
   * 
   * 
   * @param reportSpec
   */
  public void initWizardPanel(ReportSpec reportSpec) {
    try {
      int leftMargin = reportSpec.getLeftMargin();
      int rightMargin = reportSpec.getRightMargin();
      int topMargin = reportSpec.getTopMargin();
      int bottomMargin = reportSpec.getBottomMargin();
      leftMarginSpinner.setSelection(leftMargin);
      rightMarginSpinner.setSelection(rightMargin);
      topMarginSpinner.setSelection(topMargin);
      bottomMarginSpinner.setSelection(bottomMargin);
      if (reportSpec.getPageFormat() != null) {
        int pageFormatIndex = pageFormatCombo.indexOf(reportSpec.getPageFormat());
        pageFormatCombo.select(pageFormatIndex);
      }
      if (reportSpec.getUseCustomPageFormat()) {
        pageFormatCombo.setEnabled(false);
        SWTUtility.setEnabled(customPageComposite, true);
      } else {
        pageFormatCombo.setEnabled(true);
        SWTUtility.setEnabled(customPageComposite, false);
      }
      customHeightSpinner.setSelection(reportDesigner.getReportSpec().getCustomPageFormatHeight());
      customWidthSpinner.setSelection(reportDesigner.getReportSpec().getCustomPageFormatWidth());
      if (reportSpec.getOrientation().equals("portrait")) { //$NON-NLS-1$
        pageOrientation.setSelection(true);
        landscapeOrientation.setSelection(false);
      } else {
        landscapeOrientation.setSelection(true);
        pageOrientation.setSelection(false);
      }
      if (reportSpec.getWatermark() != null) {
        Watermark wm = reportSpec.getWatermark();
        imageURLText.setText(wm.getSrc());
        xSpinner.setSelection(wm.getX());
        ySpinner.setSelection(wm.getY());
        widthSpinner.setSelection(wm.getWidth());
        heightSpinner.setSelection(wm.getHeight());
        scaleButton.setSelection(wm.getScale());
        keepAspectRatioButton.setSelection(wm.getKeepAspectRatio());
        useWatermarkButton.setSelection(reportDesigner.getReportSpec().getWatermark().getUseWatermark());
      } else {
        useWatermarkButton.setSelection(true);
        widthSpinner.setSelection(100);
        heightSpinner.setSelection(100);
      }
    } catch (Exception e) {
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

  public void modifyText(ModifyEvent e) {
    stateChanged = true;
    dirty = true;
    fireDirtyEvent();
    wizardManager.update();
  }
}
