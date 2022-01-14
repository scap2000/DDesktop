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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;

public class ListInputDialog implements MouseListener {
  String title;
  int width;
  int height;
  Shell dialog;
  boolean okPressed = false;
  List list = null;
  String[] items = null;
  String[] selectedItems = null;

  public ListInputDialog(String title, String[] items, int width, int height) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.items = items;
    init();
  }

  public String[] open() {
    dialog.open();
    while (!dialog.isDisposed()) {
      if (!dialog.getDisplay().readAndDispatch())
        dialog.getDisplay().sleep();
    }
    String returnValue[] = null;
    if (okPressed) {
      returnValue = selectedItems;
    }
    dialog.dispose();
    return returnValue;
  }

  public void init() {
    dialog = SWTUtility.createModalDialogShell(width, height, title);
    dialog.setLayout(new GridLayout(4, false));
    Composite content = new Composite(dialog, SWT.NONE);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.horizontalSpan = 4;
    content.setLayoutData(gridData);
    content.setLayout(new GridLayout(1, false));
    list = new List(content, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    list.setLayoutData(gridData);
    list.setItems(items);
    list.addMouseListener(this);
    Label left = new Label(dialog, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    left.setLayoutData(gridData);
    final Button ok = new Button(dialog, SWT.PUSH);
    gridData = new GridData(SWT.RIGHT, SWT.FILL, false, false);
    ok.setLayoutData(gridData);
    ok.setText(Messages.getString("ListInputDialog.0")); //$NON-NLS-1$
    Button cancel = new Button(dialog, SWT.PUSH);
    gridData = new GridData(SWT.RIGHT, SWT.FILL, false, false);
    cancel.setLayoutData(gridData);
    cancel.setText(Messages.getString("ListInputDialog.1")); //$NON-NLS-1$
    SelectionListener listener = new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        if (e.getSource() == ok) {
          okPressed = true;
        } else {
          okPressed = false;
        }
        selectedItems = list.getSelection();
        dialog.close();
      }

      public void widgetDefaultSelected(SelectionEvent e) {
      }
    };
    ok.addSelectionListener(listener);
    cancel.addSelectionListener(listener);
  }

  public void mouseDoubleClick(MouseEvent e) {
    okPressed = true;
    selectedItems = list.getSelection();
    dialog.close();
  }

  public void mouseDown(MouseEvent e) {
  }

  public void mouseUp(MouseEvent e) {
  }
}
