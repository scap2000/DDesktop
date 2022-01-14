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
package org.pentaho.jfreereport.wizard.ui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.jfreereport.castormodel.reportspec.Field;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTButton;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;

public class TrafficLightDialog implements KeyListener, SelectionListener, MouseListener, MouseTrackListener {
  Shell dialog;
  boolean okPressed = false;
  SWTButton redColorButton = null;
  SWTButton yellowColorButton = null;
  SWTButton greenColorButton = null;
//  Text redValueText = null;
  Text yellowValueText = null;
  Text greenValueText = null;
  Button useAbsoluteValueButton = null;
  Button useOppositeLogicButton = null;
  int width = 320;
  int height = 300;
  String title = Messages.getString("TrafficLightDialog.0"); //$NON-NLS-1$
  Field passedField = null;
  Field field = new Field();

  public TrafficLightDialog(int width, int height, String title, Field passedField) {
    super();
    this.width = width;
    this.height = height;
    this.title = title;
    this.passedField = passedField;
    init();
  }

  public Field getFieldWithTrafficLightSettings() {
    return field;
  }

  public boolean open() {
    dialog.open();
    return okPressed;
  }

  public void init() {
    dialog = SWTUtility.createShell(width, height, new GridLayout(2, false), title, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    FontData fd = new FontData();
    fd.setName(dialog.getFont().getFontData()[0].getName());
    fd.setHeight(9);
    fd.setLocale(dialog.getFont().getFontData()[0].getLocale());
    fd.setStyle(SWT.NORMAL);
    Font font = new Font(dialog.getDisplay(), fd);
    
    Label greenLabel = new Label(dialog, SWT.NONE);
    greenLabel.setText(Messages.getString("TrafficLightDialog.1")); //$NON-NLS-1$
    greenLabel.setAlignment(SWT.RIGHT);
    GridData gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    greenLabel.setLayoutData(gridData);
    greenLabel.setFont(font);
    greenColorButton = new SWTButton(dialog, SWT.NONE);
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    gridData.heightHint = 15;
    gridData.widthHint = 15;
    greenColorButton.setLayoutData(gridData);
    greenColorButton.setBackground(new Color(dialog.getDisplay(), SWTUtility.getRGB(passedField.getTrafficLightingGreenColor())));
    greenColorButton.addMouseListener(this);
    greenColorButton.addMouseTrackListener(this);

    Label greenValueLabel = new Label(dialog, SWT.NONE);
    greenValueLabel.setText(Messages.getString("TrafficLightDialog.2")); //$NON-NLS-1$
    greenValueLabel.setAlignment(SWT.RIGHT);
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    greenValueLabel.setLayoutData(gridData);
    greenValueLabel.setFont(font);
    greenValueText = new Text(dialog, SWT.BORDER);
    gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
    greenValueText.setLayoutData(gridData);
    greenValueText.setText(passedField.getTrafficLightingGreenValue() + ""); //$NON-NLS-1$
    
    Label yellowLabel = new Label(dialog, SWT.NONE);
    yellowLabel.setAlignment(SWT.RIGHT);
    yellowLabel.setText(Messages.getString("TrafficLightDialog.4")); //$NON-NLS-1$
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    yellowLabel.setLayoutData(gridData);
    yellowLabel.setFont(font);
    yellowColorButton = new SWTButton(dialog, SWT.NONE);
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    gridData.heightHint = 15;
    gridData.widthHint = 15;
    yellowColorButton.setLayoutData(gridData);
    yellowColorButton.setBackground(new Color(dialog.getDisplay(), SWTUtility.getRGB(passedField.getTrafficLightingYellowColor())));
    yellowColorButton.addMouseListener(this);
    yellowColorButton.addMouseTrackListener(this);

    Label yellowValueLabel = new Label(dialog, SWT.NONE);
    yellowValueLabel.setText("Yellow Value");
    yellowValueLabel.setAlignment(SWT.RIGHT);
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    yellowValueLabel.setLayoutData(gridData);
    yellowValueLabel.setFont(font);
    yellowValueText = new Text(dialog, SWT.BORDER);
    gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
    yellowValueText.setLayoutData(gridData);
    yellowValueText.setText(passedField.getTrafficLightingYellowValue() + ""); //$NON-NLS-1$
    
    
    Label redLabel = new Label(dialog, SWT.RIGHT);
    redLabel.setText(Messages.getString("TrafficLightDialog.5")); //$NON-NLS-1$
    redLabel.setAlignment(SWT.RIGHT);
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    redLabel.setLayoutData(gridData);
    redLabel.setFont(font);
    redColorButton = new SWTButton(dialog, SWT.NONE);
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    gridData.heightHint = 15;
    gridData.widthHint = 15;
    redColorButton.setLayoutData(gridData);
    redColorButton.setBackground(new Color(dialog.getDisplay(), SWTUtility.getRGB(passedField.getTrafficLightingRedColor())));
    redColorButton.addMouseListener(this);
    redColorButton.addMouseTrackListener(this);
    
//    Label redValueLabel = new Label(dialog, SWT.NONE);
//    redValueLabel.setText(Messages.getString("TrafficLightDialog.6")); //$NON-NLS-1$
//    redValueLabel.setAlignment(SWT.RIGHT);
//    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
//    redValueLabel.setLayoutData(gridData);
//    redValueLabel.setFont(font);
//    redValueText = new Text(dialog, SWT.BORDER);
//    gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
//    redValueText.setLayoutData(gridData);
//    redValueText.setText(passedField.getTrafficLightingRedValue() + ""); //$NON-NLS-1$
    

    
    useAbsoluteValueButton = new Button(dialog, SWT.CHECK);
    useAbsoluteValueButton.setText(Messages.getString("TrafficLightDialog.8")); //$NON-NLS-1$
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    gridData.horizontalSpan = 2;
    useAbsoluteValueButton.setLayoutData(gridData);
    useAbsoluteValueButton.setSelection(passedField.getTrafficLightingUseAbsoluteValue());

    useOppositeLogicButton = new Button(dialog, SWT.CHECK);
    useOppositeLogicButton.setText(Messages.getString("TrafficLightDialog.9")); //$NON-NLS-1$
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    gridData.horizontalSpan = 2;
    useOppositeLogicButton.setLayoutData(gridData);
    useOppositeLogicButton.setSelection(passedField.getTrafficLightingUseOppositeLogic());
    
    gridData = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
    final PentahoSWTButton ok = new PentahoSWTButton(dialog, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("ListInputDialog.0")); //$NON-NLS-1$
    ok.setLayoutData(gridData);
    ok.setFont(font);
    gridData = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
    PentahoSWTButton cancel = new PentahoSWTButton(dialog, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("ListInputDialog.1")); //$NON-NLS-1$
    cancel.setLayoutData(gridData);
    SelectionListener listener = new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        if (e.getSource() == ok) {
          okPressed = true;
          passedField.setUseTrafficLighting(true);
          passedField.setTrafficLightingRedColor(SWTUtility.getJFreeColorString(redColorButton.getBackground().getRGB()));
          passedField.setTrafficLightingYellowColor(SWTUtility.getJFreeColorString(yellowColorButton.getBackground().getRGB()));
          passedField.setTrafficLightingGreenColor(SWTUtility.getJFreeColorString(greenColorButton.getBackground().getRGB()));
//          field.setTrafficLightingRedValue(Double.parseDouble(redValueText.getText()));
          passedField.setTrafficLightingYellowValue(Double.parseDouble(yellowValueText.getText()));
          passedField.setTrafficLightingGreenValue(Double.parseDouble(greenValueText.getText()));
          passedField.setTrafficLightingUseOppositeLogic(useOppositeLogicButton.getSelection());
          passedField.setTrafficLightingUseAbsoluteValue(useAbsoluteValueButton.getSelection());
        } else {
          okPressed = false;
        }
        dialog.close();
      }

      public void widgetDefaultSelected(SelectionEvent e) {
      }
    };
    ok.addSelectionListener(listener);
    cancel.addSelectionListener(listener);
    cancel.setFont(font);
    SWTUtility.setBackground(dialog, ReportWizard.background);
    dialog.pack();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
   */
  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
   */
  public void keyReleased(KeyEvent e) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetSelected(SelectionEvent e) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetDefaultSelected(SelectionEvent e) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDoubleClick(MouseEvent arg0) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDown(MouseEvent arg0) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseUp(MouseEvent e) {
    ColorDialog colorDialog = new ColorDialog(dialog);
    if (e.widget == redColorButton) {
      colorDialog.setRGB(SWTUtility.getRGB(passedField.getTrafficLightingRedColor()));
    } else if (e.widget == yellowColorButton) {
      colorDialog.setRGB(SWTUtility.getRGB(passedField.getTrafficLightingYellowColor()));
    } else if (e.widget == greenColorButton) {
      colorDialog.setRGB(SWTUtility.getRGB(passedField.getTrafficLightingGreenColor()));
    }
    RGB pickedColor = colorDialog.open();
    if (pickedColor != null) {
      ((SWTButton) e.widget).setBackground(new Color(dialog.getDisplay(), pickedColor));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseEnter(MouseEvent arg0) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseExit(MouseEvent arg0) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseHover(MouseEvent arg0) {
    // TODO Auto-generated method stub
  }
}
