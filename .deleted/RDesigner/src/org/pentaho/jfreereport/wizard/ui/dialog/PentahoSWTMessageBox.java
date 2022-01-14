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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;

public class PentahoSWTMessageBox {
  String title;
  String message;
  int width;
  int height;
  Shell dialog;

  public PentahoSWTMessageBox(String title, String message, int width, int height) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.message = message;
    init();
  }

  public void open() {
    dialog.open();
    while (!dialog.isDisposed()) {
      if (!dialog.getDisplay().readAndDispatch())
        dialog.getDisplay().sleep();
    }
  }

  public void init() {
    dialog = SWTUtility.createModalDialogShell(width, height, title);
    dialog.setLayout(new GridLayout(3, false));
    
    Label label = new Label(dialog, SWT.WRAP | SWT.NONE);
    GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
    gridData.horizontalSpan = 3;
    label.setLayoutData(gridData);
    label.setText(message);
    
    Label left = new Label(dialog, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
    left.setLayoutData(gridData);
    gridData = new GridData(SWT.RIGHT, SWT.NONE, false, false);
    final PentahoSWTButton ok = new PentahoSWTButton(dialog, SWT.PUSH, gridData, PentahoSWTButton.NORMAL, Messages.getString("PentahoSWTMessageBox.0")); //$NON-NLS-1$
    ok.setLayoutData(gridData);
    ok.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        dialog.close();
      }
    });
    
    SWTUtility.setBackground(dialog, ReportWizard.background);
    dialog.pack();
  }
}
