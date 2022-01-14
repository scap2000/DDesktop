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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.core.admin.datasources.DataSourceInfo;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;
import org.pentaho.jfreereport.wizard.utility.connection.ConnectionUtility;

public class JDBCPanel implements KeyListener, SelectionListener {

  Shell dialog;
  boolean okPressed = false;
  Text jdbcUsername = null;
  Text jdbcPassword = null;
  Combo jdbcConnectStringCombo = null;
  Combo jdbcDriverCombo = null;
  int width = 320;
  int height = 300;
  String title = Messages.getString("JDBCPanel.0"); //$NON-NLS-1$
  Text jndiName = null;
  DataSourceInfo dsi = null;
  String rdwRoot = "."; //$NON-NLS-1$

  public JDBCPanel(int width, int height, String title, String rdwRoot) {
    this(width, height, title, rdwRoot, null);
  }

  public JDBCPanel(int width, int height, String title, String rdwRoot, DataSourceInfo dsi) {
    super();
    this.dsi = dsi;
    this.width = width;
    this.height = height;
    this.title = title;
    this.rdwRoot = rdwRoot;
    init();
  }

  public boolean open() {
    dialog.open();
    while (!dialog.isDisposed()) {
      try {
        if (!dialog.getDisplay().readAndDispatch())
          dialog.getDisplay().sleep();
      } catch (Throwable t) {
        // on some platforms, we may get a disposal exception
        // which prevents us from returning "okPressed"
        // this is harmless so we are intentionally trapping
        // the SWT exception
      }
    }
    return okPressed;
  }

  public DataSourceInfo getDataSourceInfo() {
    return dsi;
  }

  public void init() {
    dialog = SWTUtility.createShell(width, height, new GridLayout(4, false), title, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    FontData fd = new FontData();
    fd.setName(dialog.getFont().getFontData()[0].getName());
    fd.setHeight(9);
    fd.setLocale(dialog.getFont().getFontData()[0].getLocale());
    fd.setStyle(SWT.NORMAL);
    Font font = new Font(dialog.getDisplay(), fd);
    Label nameLabel = new Label(dialog, SWT.NONE);
    nameLabel.setText(Messages.getString("JDBCPanel.1")); //$NON-NLS-1$
    GridData gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    gridData.horizontalSpan = 4;
    nameLabel.setLayoutData(gridData);
    nameLabel.setFont(font);
    jndiName = new Text(dialog, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
    gridData.horizontalSpan = 4;
    jndiName.setLayoutData(gridData);
    jndiName.addKeyListener(this);
    jndiName.setFont(font);
    if (dsi != null && dsi.getName() != null) {
      jndiName.setText(dsi.getName());
    }
    Label driverLabel = new Label(dialog, SWT.NONE);
    driverLabel.setText(Messages.getString("QueryPanel.6")); //$NON-NLS-1$
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    gridData.horizontalSpan = 4;
    driverLabel.setLayoutData(gridData);
    driverLabel.setFont(font);
    jdbcDriverCombo = new Combo(dialog, SWT.NONE);
    jdbcDriverCombo.setItems(ConnectionUtility.getDrivers(rdwRoot));
    gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
    gridData.horizontalSpan = 4;
    jdbcDriverCombo.setLayoutData(gridData);
    jdbcDriverCombo.addKeyListener(this);
    jdbcDriverCombo.addSelectionListener(this);
    jdbcDriverCombo.setFont(font);
    if (dsi != null && dsi.getDriver() != null) {
      int index = jdbcDriverCombo.indexOf(dsi.getDriver());
      if (index >= 0) {
        jdbcDriverCombo.select(index);
      } else {
        jdbcDriverCombo.setText(dsi.getDriver());
      }
    }
    Label connectStringLabel = new Label(dialog, SWT.NONE);
    connectStringLabel.setText(Messages.getString("QueryPanel.7")); //$NON-NLS-1$
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    gridData.horizontalSpan = 4;
    connectStringLabel.setLayoutData(gridData);
    connectStringLabel.setFont(font);
    jdbcConnectStringCombo = new Combo(dialog, SWT.NONE);
    jdbcConnectStringCombo.setItems(ConnectionUtility.getConnectStrings());
    gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
    gridData.horizontalSpan = 4;
    jdbcConnectStringCombo.setLayoutData(gridData);
    jdbcConnectStringCombo.addKeyListener(this);
    jdbcConnectStringCombo.addSelectionListener(this);
    jdbcConnectStringCombo.setFont(font);
    if (dsi != null && dsi.getUrl() != null) {
      int index = jdbcConnectStringCombo.indexOf(dsi.getUrl());
      if (index >= 0) {
        jdbcConnectStringCombo.select(index);
      } else {
        jdbcConnectStringCombo.setText(dsi.getUrl());
      }
    }
    Label usernameLabel = new Label(dialog, SWT.NONE);
    usernameLabel.setText(Messages.getString("QueryPanel.9")); //$NON-NLS-1$
    gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    gridData.horizontalSpan = 4;
    usernameLabel.setLayoutData(gridData);
    usernameLabel.setFont(font);
    jdbcUsername = new Text(dialog, SWT.BORDER | SWT.SINGLE);
    gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
    gridData.horizontalSpan = 4;
    jdbcUsername.setLayoutData(gridData);
    jdbcUsername.addKeyListener(this);
    jdbcUsername.addSelectionListener(this);
    jdbcUsername.setFont(font);
    if (dsi != null && dsi.getUserId() != null) {
      jdbcUsername.setText(dsi.getUserId());
    }
    Label passwordLabel = new Label(dialog, SWT.NONE);
    passwordLabel.setText(Messages.getString("QueryPanel.10")); //$NON-NLS-1$
    gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    gridData.horizontalSpan = 4;
    passwordLabel.setLayoutData(gridData);
    passwordLabel.setFont(font);
    jdbcPassword = new Text(dialog, SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);
    gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
    gridData.horizontalSpan = 4;
    jdbcPassword.setLayoutData(gridData);
    jdbcPassword.addKeyListener(this);
    jdbcPassword.addSelectionListener(this);
    jdbcPassword.setFont(font);
    if (dsi != null && dsi.getPassword() != null) {
      jdbcPassword.setText(dsi.getPassword());
    }
    gridData = new GridData(SWT.CENTER, SWT.NONE, true, false);
    gridData.horizontalSpan = 4;
    final PentahoSWTButton testConnection = new PentahoSWTButton(dialog, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("JDBCPanel.2")); //$NON-NLS-1$
    testConnection.setLayoutData(gridData);
    testConnection.addSelectionListener(new SelectionAdapter() {

      public void widgetSelected(SelectionEvent e) {
        dsi = new DataSourceInfo(jndiName.getText(), "", "javax.sql.DataSource"); //$NON-NLS-1$ //$NON-NLS-2$
        dsi.setDriver(jdbcDriverCombo.getText());
        dsi.setUrl(jdbcConnectStringCombo.getText());
        dsi.setUserId(jdbcUsername.getText());
        dsi.setPassword(jdbcPassword.getText());
        String status = ReportWizard.dataSourceAdmin.testDataSource(dsi);
        PentahoSWTMessageBox mb = new PentahoSWTMessageBox(Messages.getString("QueryPanel.31"), status, 360, 140); //$NON-NLS-1$
        mb.open();
      }
    });
    Label left = new Label(dialog, SWT.NONE);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    left.setLayoutData(gridData);
    gridData = new GridData(SWT.RIGHT, SWT.BOTTOM, false, true);
    final PentahoSWTButton ok = new PentahoSWTButton(dialog, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("ListInputDialog.0")); //$NON-NLS-1$
    ok.setLayoutData(gridData);
    ok.setFont(font);
    gridData = new GridData(SWT.RIGHT, SWT.BOTTOM, false, true);
    PentahoSWTButton cancel = new PentahoSWTButton(dialog, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("ListInputDialog.1")); //$NON-NLS-1$
    cancel.setLayoutData(gridData);
    SelectionListener listener = new SelectionListener() {

      public void widgetSelected(SelectionEvent e) {
        if (e.getSource() == ok) {
          okPressed = true;
          dsi = new DataSourceInfo(jndiName.getText().replaceAll(" ", "_"), "", "javax.sql.DataSource"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
          dsi.setDriver(jdbcDriverCombo.getText());
          dsi.setUrl(jdbcConnectStringCombo.getText());
          dsi.setUserId(jdbcUsername.getText());
          dsi.setPassword(jdbcPassword.getText());
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
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetDefaultSelected(SelectionEvent e) {
    // TODO Auto-generated method stub
  }
}
