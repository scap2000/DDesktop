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

import java.io.File;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTLine;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;

public class PublishDialog implements SelectionListener, KeyListener, MouseListener, MouseTrackListener {
  public static String types[] = new String[] { "pdf", "html", "xls", "csv", "rtf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
  String title;
  Shell dialog;
  boolean okPressed = false;
  Properties settings = null;
  ReportSpec reportSpec = null;
  Text locationText = null;
  Text publishURLText = null;
  Text publishPWText;
  Text serverUIDText;
  Text serverPWText;
  Label jbossSetupLabel = null;
  Text deployText = null;
  Label deployLabel = null;
  Text webappText = null;
  Label webappLabel = null;
  Button createJBossDataSourceButton = null;
  Text nameText = null;
  Combo reportTypeCombo = null;
  public String deployDir = null;
  public String webAppName = null;
  public boolean createDataSource = false;
  public String name = null;
  public String location = null;
  public String reportType = null;
  public String publishURL = null;
  public String publishPW;
  public String serverUID;
  public String serverPW;
  public boolean publishToServer = true;
  Button locationButton = null;
  Button deployButton = null;
  Button publishToServerButton = null;
  Button publishToLocationButton = null;

  public PublishDialog(String title, ReportSpec reportSpec, Properties settings) {
    this.settings = settings;
    this.title = title;
    this.reportSpec = reportSpec;
    init();
  }

  public boolean open() {
    dialog.open();
    try {
      while (!dialog.isDisposed()) {
        if (!dialog.getDisplay().readAndDispatch())
          dialog.getDisplay().sleep();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return okPressed;
  }

  public void enableJBossDataSourceGUI(boolean enable) {
    try {
      deployButton.setEnabled(enable);
      deployText.setEnabled(enable);
      deployLabel.setEnabled(enable);
      webappText.setEnabled(enable);
      webappLabel.setEnabled(enable);
    } catch (Exception e) {
    }
  }

  public void init() {
    if (dialog != null) {
      SWTUtility.removeChildren(dialog);
    }
    boolean enabledDataSourceCreate = reportSpec.getReportSpecChoice() == null ? true : reportSpec.getReportSpecChoice().getJndiSource() != null;
    if (dialog == null) {
      dialog = SWTUtility.createShell(550, 640, new GridLayout(2, false), title, SWT.SHELL_TRIM);
    }
    Composite contentPanel = new Composite(dialog, SWT.NONE);
    contentPanel.setBackground(ReportWizard.background);
    contentPanel.setLayout(new GridLayout(2, false));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    contentPanel.setLayoutData(gridData);
    FontData fd = new FontData();
    fd.setName(dialog.getFont().getFontData()[0].getName());
    fd.setHeight(10);
    fd.setLocale(dialog.getFont().getFontData()[0].getLocale());
    fd.setStyle(SWT.BOLD);
    Font labelFont = new Font(dialog.getDisplay(), fd);
    Label publishToLabel = new Label(contentPanel, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.BOTTOM, false, false);
    gridData.horizontalSpan = 2;
    publishToLabel.setText(Messages.getString("PublishDialog.4")); //$NON-NLS-1$
    publishToLabel.setLayoutData(gridData);
    publishToLabel.setFont(labelFont);
    SWTLine publishToLine = new SWTLine(contentPanel, SWT.NONE);
    publishToLine.setEtchedColors(new Color(dialog.getDisplay(), new RGB(128, 128, 128)), new Color(dialog.getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.heightHint = 8;
    gridData.horizontalSpan = 2;
    publishToLine.setLayoutData(gridData);
    publishToServerButton = new Button(contentPanel, SWT.RADIO);
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    publishToServerButton.setLayoutData(gridData);
    publishToServerButton.setText(Messages.getString("PublishDialog.3")); //$NON-NLS-1$
    publishToServerButton.setSelection(publishToServer);
    publishToServerButton.addSelectionListener(this);
    publishToLocationButton = new Button(contentPanel, SWT.RADIO);
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    publishToLocationButton.setLayoutData(gridData);
    publishToLocationButton.setText(Messages.getString("PublishDialog.2")); //$NON-NLS-1$
    publishToLocationButton.setSelection(!publishToServer);
    publishToLocationButton.addSelectionListener(this);
    Label publishInfoLabel = new Label(contentPanel, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.BOTTOM, false, false);
    gridData.horizontalSpan = 2;
    publishInfoLabel.setText(Messages.getString("PublishDialog.5")); //$NON-NLS-1$
    publishInfoLabel.setLayoutData(gridData);
    publishInfoLabel.setFont(labelFont);
    SWTLine publishInfoLine = new SWTLine(contentPanel, SWT.NONE);
    publishInfoLine.setEtchedColors(new Color(dialog.getDisplay(), new RGB(128, 128, 128)), new Color(dialog.getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.heightHint = 8;
    gridData.horizontalSpan = 2;
    publishInfoLine.setLayoutData(gridData);
    Label nameLabel = new Label(contentPanel, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    nameLabel.setText(Messages.getString("PublishDialog.6")); //$NON-NLS-1$
    nameLabel.setLayoutData(gridData);
    nameText = new Text(contentPanel, SWT.BORDER | SWT.LEFT);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
    gridData.horizontalSpan = 2;
    gridData.widthHint = 200;
    nameText.setLayoutData(gridData);
    nameText.setText(reportSpec.getReportName());
    // location
    Label locationLabel = new Label(contentPanel, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    locationLabel.setText(Messages.getString("PublishDialog.7")); //$NON-NLS-1$
    locationLabel.setLayoutData(gridData);
    locationText = new Text(contentPanel, SWT.BORDER | SWT.LEFT);
    gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    if (publishToServer) {
      locationText.setText(settings.getProperty("publish.http.solution.location")); //$NON-NLS-1$
      gridData.horizontalSpan = 2;
      locationText.setLayoutData(gridData);
    } else {
      locationText.setText(settings.getProperty("publish.solution.location")); //$NON-NLS-1$
      locationText.setLayoutData(gridData);
      locationButton = new Button(contentPanel, SWT.PUSH);
      locationButton.setText(Messages.getString("PublishDialog.10")); //$NON-NLS-1$
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      locationButton.setLayoutData(gridData);
      locationButton.addSelectionListener(this);
    }
    // default report type
    Label typeLabel = new Label(contentPanel, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    typeLabel.setText(Messages.getString("PublishDialog.11")); //$NON-NLS-1$
    typeLabel.setLayoutData(gridData);
    reportTypeCombo = new Combo(contentPanel, SWT.BORDER | SWT.READ_ONLY);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.widthHint = 120;
    gridData.horizontalSpan = 2;
    reportTypeCombo.setLayoutData(gridData);
    reportTypeCombo.setItems(types);
    reportTypeCombo.select(reportTypeCombo.indexOf(settings.getProperty("report.output.type"))); //$NON-NLS-1$
    // jboss-deploy
    Label spacerLabel = new Label(contentPanel, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    gridData.heightHint = 10;
    spacerLabel.setLayoutData(gridData);
    spacerLabel.setFont(labelFont);
    jbossSetupLabel = new Label(contentPanel, SWT.NONE);
    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    gridData.horizontalSpan = 2;
    jbossSetupLabel.setText(Messages.getString("PublishDialog.13")); //$NON-NLS-1$
    jbossSetupLabel.setLayoutData(gridData);
    jbossSetupLabel.setFont(labelFont);
    SWTLine jbossSetupLine = new SWTLine(contentPanel, SWT.NONE);
    jbossSetupLine.setEtchedColors(new Color(dialog.getDisplay(), new RGB(128, 128, 128)), new Color(dialog.getDisplay(), new RGB(255, 255, 255)));
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.heightHint = 8;
    gridData.horizontalSpan = 2;
    jbossSetupLine.setLayoutData(gridData);
    if (publishToServer) {
      Label publishURLLabel = new Label(contentPanel, SWT.NONE);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.horizontalSpan = 2;
      publishURLLabel.setText(Messages.getString("PublishDialog.14")); //$NON-NLS-1$
      publishURLLabel.setLayoutData(gridData);
      publishURLText = new Text(contentPanel, SWT.BORDER | SWT.LEFT);
      gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
      publishURLText.setText(settings.getProperty("publish.http.url")); //$NON-NLS-1$
      publishURLText.setLayoutData(gridData);

      // Add publish password to panel
      Label publishPWLabel = new Label(contentPanel, SWT.NONE);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.horizontalSpan = 2;
      publishPWLabel.setText(Messages.getString("PublishDialog.24")); //$NON-NLS-1$
      publishPWLabel.setLayoutData(gridData);

      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.widthHint = 200;
      gridData.horizontalSpan = 2;
      publishPWText = new Text(contentPanel, SWT.BORDER | SWT.LEFT | SWT.PASSWORD);
      String savedPW = settings.getProperty("publish.http.password"); //$NON-NLS-1$
      if (savedPW == null) {
        savedPW = ""; //$NON-NLS-1$
      }
      publishPWText.setText(savedPW);
      publishPWText.setLayoutData(gridData);
      publishPWText.addFocusListener(new FocusListener() {

        public void focusGained(FocusEvent arg0) {
          publishPWText.selectAll();
          publishPWText.setSelection(0, publishPWText.getText().length());
        }

        public void focusLost(FocusEvent arg0) {
        }

      });
      publishPWText.addMouseListener(new MouseListener() {

        public void mouseDoubleClick(MouseEvent arg0) {
        }

        public void mouseDown(MouseEvent arg0) {
        }

        public void mouseUp(MouseEvent arg0) {
          publishPWText.selectAll();
          publishPWText.setSelection(0, publishPWText.getText().length());
        }

      });

      // Add server userid to panel
      Label serverUIDLabel = new Label(contentPanel, SWT.NONE);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.horizontalSpan = 2;
      serverUIDLabel.setText(Messages.getString("PublishDialog.27")); //$NON-NLS-1$
      serverUIDLabel.setLayoutData(gridData);

      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.widthHint = 200;
      gridData.horizontalSpan = 2;
      serverUIDText = new Text(contentPanel, SWT.BORDER | SWT.LEFT);
      String savedServerUID = settings.getProperty("publish.http.server.userid"); //$NON-NLS-1$
      if (savedServerUID == null) {
        savedServerUID = ""; //$NON-NLS-1$
      }
      serverUIDText.setText(savedServerUID);
      serverUIDText.setLayoutData(gridData);

      // Add server Password to panel
      Label serverPWLabel = new Label(contentPanel, SWT.NONE);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.horizontalSpan = 2;
      serverPWLabel.setText(Messages.getString("PublishDialog.28")); //$NON-NLS-1$
      serverPWLabel.setLayoutData(gridData);

      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.horizontalSpan = 2;
      gridData.widthHint = 200;
      serverPWText = new Text(contentPanel, SWT.BORDER | SWT.LEFT | SWT.PASSWORD);
      String savedServerPW = settings.getProperty("publish.http.server.password"); //$NON-NLS-1$
      if (savedServerPW == null) {
        savedServerPW = ""; //$NON-NLS-1$
      }
      serverPWText.setText(savedServerPW);
      serverPWText.setLayoutData(gridData);

      serverPWText.addFocusListener(new FocusListener() {

        public void focusGained(FocusEvent arg0) {
          serverPWText.selectAll();
          serverPWText.setSelection(0, serverPWText.getText().length());
        }

        public void focusLost(FocusEvent arg0) {
        }

      });
      serverPWText.addMouseListener(new MouseListener() {

        public void mouseDoubleClick(MouseEvent arg0) {
        }

        public void mouseDown(MouseEvent arg0) {
        }

        public void mouseUp(MouseEvent arg0) {
          serverPWText.selectAll();
          serverPWText.setSelection(0, serverPWText.getText().length());
        }

      });

    }
    createJBossDataSourceButton = new Button(contentPanel, SWT.CHECK);
    createJBossDataSourceButton.setText(Messages.getString("PublishDialog.16")); //$NON-NLS-1$
    createJBossDataSourceButton.setSelection(true);
    createJBossDataSourceButton.setEnabled(enabledDataSourceCreate);
    createJBossDataSourceButton.addSelectionListener(this);
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.horizontalSpan = 2;
    createJBossDataSourceButton.setLayoutData(gridData);
    if (!publishToServer) {
      // web application name
      webappLabel = new Label(contentPanel, SWT.NONE);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.horizontalSpan = 2;
      webappLabel.setText(Messages.getString("PublishDialog.17")); //$NON-NLS-1$
      webappLabel.setLayoutData(gridData);
      webappText = new Text(contentPanel, SWT.BORDER | SWT.LEFT);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.widthHint = 200;
      gridData.horizontalSpan = 2;
      webappText.setLayoutData(gridData);
      webappText.setText(settings.getProperty(Messages.getString("PublishDialog.18"))); //$NON-NLS-1$
      // deployment dir
      deployLabel = new Label(contentPanel, SWT.NONE);
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      gridData.horizontalSpan = 2;
      deployLabel.setText(Messages.getString("PublishDialog.19")); //$NON-NLS-1$
      deployLabel.setLayoutData(gridData);
      deployText = new Text(contentPanel, SWT.BORDER | SWT.LEFT);
      gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
      deployText.setLayoutData(gridData);
      deployText.setText(settings.getProperty("jboss.deploy.dir")); //$NON-NLS-1$
      deployButton = new Button(contentPanel, SWT.PUSH);
      deployButton.setText(Messages.getString("PublishDialog.21")); //$NON-NLS-1$
      gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
      deployButton.setLayoutData(gridData);
      deployButton.addSelectionListener(this);
    }
    // button panel (ok/cancel)
    Composite buttonPanel = new Composite(dialog, SWT.NONE);
    buttonPanel.setLayout(new GridLayout(4, false));
    gridData = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
    gridData.horizontalSpan = 2;
    buttonPanel.setLayoutData(gridData);
    fillButtonPanel(buttonPanel);
    enableJBossDataSourceGUI(enabledDataSourceCreate);
    dialog.pack();
  }

  public void fillButtonPanel(Composite buttonPanel) {
    // left spacer for ok button
    Label left = new Label(buttonPanel, SWT.NONE);
    GridData gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
    left.setLayoutData(gridData);
    gridData = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
    final PentahoSWTButton ok = new PentahoSWTButton(buttonPanel, SWT.PUSH, gridData, PentahoSWTButton.NORMAL, Messages.getString("PublishDialog.22")); //$NON-NLS-1$
    ok.setLayoutData(gridData);
    gridData = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
    final PentahoSWTButton cancel = new PentahoSWTButton(buttonPanel, SWT.PUSH, gridData, PentahoSWTButton.NORMAL, Messages.getString("PublishDialog.23")); //$NON-NLS-1$
    cancel.setLayoutData(gridData);
    SelectionListener listener = new SelectionListener() {

      public void widgetSelected(SelectionEvent e) {
        boolean disposeOK = true;
        if (e.getSource() == ok) {
          try {
            okPressed = true;
            name = nameText.getText();
            createDataSource = createJBossDataSourceButton.getSelection();
            if (deployText != null) {
              deployDir = deployText.getText();
            }
            if (webappText != null) {
              webAppName = webappText.getText();
            }
            if (locationText != null) {
              location = locationText.getText();
            }
            if (publishURLText != null && !publishURLText.isDisposed()) {
              publishURL = publishURLText.getText();
            }
            if (publishPWText != null && !publishPWText.isDisposed()) {
              publishPW = publishPWText.getText();
              settings.setProperty("publish.http.password", publishPW); //$NON-NLS-1$
            }
            if (serverUIDText != null && !serverUIDText.isDisposed()) {
              serverUID = serverUIDText.getText();
              settings.setProperty("publish.http.server.userid", serverUID); //$NON-NLS-1$
            }
            if (serverPWText != null && !serverPWText.isDisposed()) {
              serverPW = serverPWText.getText();
              settings.setProperty("publish.http.server.password", serverPW); //$NON-NLS-1$
            }

            if (publishToServer) {
              if ((publishPW == null) || (publishPW.length() == 0)) {
                disposeOK = false;
                MessageBox mb = new MessageBox(dialog, SWT.OK);
                mb.setText(Messages.getString("PublishDialog.26")); //$NON-NLS-1$
                mb.setMessage(Messages.getString("PublishDialog.25")); //$NON-NLS-1$
                mb.open();
              }
            }
            reportType = reportTypeCombo.getText();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        } else {
          okPressed = false;
        }
        if (disposeOK) {
          dialog.dispose();
        }
      }

      public void widgetDefaultSelected(SelectionEvent e) {
      }
    };

    ok.addSelectionListener(listener);
    cancel.addSelectionListener(listener);
    SWTUtility.setBackground(dialog, ReportWizard.background);
    dialog.layout();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetSelected(SelectionEvent e) {
    if (e.widget == createJBossDataSourceButton) {
      enableJBossDataSourceGUI(createJBossDataSourceButton.getSelection());
    } else if (e.widget == deployButton) {
      DirectoryDialog dirDialog = new DirectoryDialog(dialog, SWT.OPEN);
      dirDialog.setFilterPath(deployText.getText());
      String path = dirDialog.open();
      if (path != null) {
        File f = new File(path);
        if (f.exists() && f.isDirectory()) {
          deployDir = f.getAbsolutePath();
          deployText.setText(deployDir);
          settings.setProperty("jboss.deploy.dir", deployDir); //$NON-NLS-1$
        }
      }
    } else if (e.widget == locationButton) {
      DirectoryDialog dirDialog = new DirectoryDialog(dialog, SWT.OPEN);
      dirDialog.setFilterPath(locationText.getText());
      String path = dirDialog.open();
      if (path != null) {
        File f = new File(path);
        if (f.exists() && f.isDirectory()) {
          location = f.getAbsolutePath();
          locationText.setText(location);
          settings.setProperty("publish.solution.location", location); //$NON-NLS-1$
        }
      }
    } else if (e.widget == publishToLocationButton) {
      if (!publishToLocationButton.getSelection()) {
        return;
      }
      publishToServer = false;
      init();
      dialog.update();
      dialog.redraw();
      dialog.update();
    } else if (e.widget == publishToServerButton) {
      if (!publishToServerButton.getSelection()) {
        return;
      }
      publishToServer = true;
      init();
      dialog.redraw();
      dialog.update();
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
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDoubleClick(MouseEvent e) {
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
