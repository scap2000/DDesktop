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
package org.pentaho.jfreereport.wizard;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jfree.report.JFreeReportBoot;
import org.pentaho.commons.mql.ui.mqldesigner.CWMStartup;
import org.pentaho.core.admin.datasources.DataSourceInfo;
import org.pentaho.core.admin.datasources.StandaloneSimpleJNDIDatasourceAdmin;
import org.pentaho.core.admin.datasources.jboss.JBossDatasourceAdmin;
import org.pentaho.core.connection.DataUtilities;
import org.pentaho.core.connection.IPentahoConnection;
import org.pentaho.core.connection.IPentahoResultSet;
import org.pentaho.core.repository.ISolutionRepository;
import org.pentaho.core.runtime.IRuntimeContext;
import org.pentaho.core.session.StandaloneSession;
import org.pentaho.core.solution.IActionCompleteListener;
import org.pentaho.core.util.PublisherUtil;
import org.pentaho.data.connection.mdx.MDXMetaData;
import org.pentaho.jfreereport.castormodel.reportspec.Field;
import org.pentaho.jfreereport.castormodel.reportspec.FieldMapping;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpecChoice;
import org.pentaho.jfreereport.castormodel.reportspec.Watermark;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.IDirtyListener;
import org.pentaho.jfreereport.wizard.ui.IWizardListener;
import org.pentaho.jfreereport.wizard.ui.NavigationPanel;
import org.pentaho.jfreereport.wizard.ui.WizardPanel;
import org.pentaho.jfreereport.wizard.ui.dialog.PentahoSWTMessageBox;
import org.pentaho.jfreereport.wizard.ui.dialog.PublishDialog;
import org.pentaho.jfreereport.wizard.ui.step.FieldSetupPanel;
import org.pentaho.jfreereport.wizard.ui.step.GroupAndDetailPanel;
import org.pentaho.jfreereport.wizard.ui.step.MappingPanel;
import org.pentaho.jfreereport.wizard.ui.step.PageSetupPanel;
import org.pentaho.jfreereport.wizard.ui.step.QueryPanel;
import org.pentaho.jfreereport.wizard.ui.step.ReportDefinitionPanel;
import org.pentaho.jfreereport.wizard.ui.step.ReportSetupPanel;
import org.pentaho.jfreereport.wizard.utility.ActionSequenceUtility;
import org.pentaho.jfreereport.wizard.utility.CastorUtility;
import org.pentaho.jfreereport.wizard.utility.PentahoUtility;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;
import org.pentaho.jfreereport.wizard.utility.TinyHTTPd;
import org.pentaho.jfreereport.wizard.utility.connection.ConnectionUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportGenerationUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportSpecUtility;
import org.pentaho.util.logging.ILogger;

public class ReportWizard implements IActionCompleteListener, IWizardListener, IDirtyListener {
  public static final String PREVIEW_TYPE_PDF = "pdf"; //$NON-NLS-1$

  public static final String PREVIEW_TYPE_HTML = "html"; //$NON-NLS-1$

  public static final String PREVIEW_TYPE_XLS = "xls"; //$NON-NLS-1$

  public static final int TAB_TEMPLATE = 0;

  public static final int TAB_QUERY = 1;

  public static final int TAB_MAPPING = 2;

  public static final int TAB_LAYOUT = 3;

  public static final int TAB_FORMAT = 4;

  public static final int TAB_PAGE = 5;

  public static final int TAB_REPORT = 6;

  public static int MODE_APPLICATION = 0;

  public static int MODE_DIALOG = 1;

  public static int MODE_EMBEDDED = 2;

  public static int applicationMode = MODE_APPLICATION;

  public static final Color background = new Color(Display.getDefault(), 0xf2, 0xf2, 0xf2);

  public static StandaloneSimpleJNDIDatasourceAdmin dataSourceAdmin;

  QueryPanel queryPanel = null;

  GroupAndDetailPanel groupAndDetailPanel = null;

  NavigationPanel navigationPanel = null;

  FieldSetupPanel fieldSetupPanel = null;

  PageSetupPanel pageSetupPanel = null;

  ReportSetupPanel reportSetupPanel = null;

  ReportDefinitionPanel templatePanel = null;

  MappingPanel mappingPanel = null;

  Browser browser = null;

  Display display = null;

  Shell detachedPreviewShell;

  StackLayout stackLayout;

  int previousSelectedTabIndex;

  int maxSelectedTabIndex = 2;

  public WizardManager wizardManager = null;

  IPentahoConnection pentahoConnection = null;

  HashMap typeMap = new HashMap();

  boolean ignoreWizardEvents = false;

  public String reportSpecStartupFilePath = null;

  public String reportSpecFilePath = null;

  public String templateZipPath = null;

  ReportSpec reportSpec = new ReportSpec();

  Composite container = null;

  Composite parentContainer = null;

  StackLayout mainStack;

  String previewType = "pdf"; //$NON-NLS-1$

  public String defaultNumericFunction = "sum"; //$NON-NLS-1$

  String exportPath = null;

  Composite dialogBase;

  Composite splashScreen;

  Label splashLabel;

  int currentTabIndex;

  boolean handlingTab = false;

  MenuItem saveItem = null;

  boolean isSaved = false;

  String startupQuery = null;

  HashMap templateImageMap = new HashMap();

  public static String simpleJNDIPath = "/system/simple-jndi/jdbc.properties"; //$NON-NLS-1$

  public String jfreeReportOutputPath = "resources/solutions/samples/reporting/"; //$NON-NLS-1$

  public String templatePath = "templates"; //$NON-NLS-1$

  public String solutionRootPath = "resources/solutions"; //$NON-NLS-1$

  public String rdwRoot = "/"; //$NON-NLS-1$
  public String workingDir = "/"; //$NON-NLS-1$

  protected Properties settings = new Properties();

  String browseFolder = "./"; //$NON-NLS-1$

  MenuItem htmlPreviewTypeMenuItem = null;

  MenuItem pdfPreviewTypeMenuItem = null;

  MenuItem xlsPreviewTypeMenuItem = null;

  public boolean removeCrlfFromQuery = false;

  public String lastCommand = null;
  static {

    String javaVersion = System.getProperty("java.version"); //$NON-NLS-1$
    if (javaVersion.startsWith("1.0") || javaVersion.startsWith("1.1") || javaVersion.startsWith("1.2") || javaVersion.startsWith("1.3")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      System.out.println(Messages.getString("ReportWizard.33")); //$NON-NLS-1$
      JOptionPane.showMessageDialog(null, Messages.getString("ReportWizard.34")); //$NON-NLS-1$
      System.exit(0);
    }
  }

  public ReportWizard(String startupFile, String startupQuery, String rdwRoot, String browseFolder, Composite parentContainer, int appMode) {
    this(startupFile, startupQuery, rdwRoot, rdwRoot, browseFolder, parentContainer, appMode);
  }

  public ReportWizard(final String startupFile, final String startupQuery, String rdwRoot, String workingDir, String browseFolder, Composite inParentContainer, int appMode) {

    InputStream iconRes = ReportWizard.class.getClassLoader().getResourceAsStream("pentaho_icon.ico"); //$NON-NLS-1$
    Image icon = new Image(Display.getDefault(), iconRes);
    SWTUtility.setIcon(icon);

    ReportWizard.applicationMode = appMode;
    if (inParentContainer == null) {
      if (appMode == MODE_APPLICATION) {
        inParentContainer = SWTUtility.createShell(1024, 768, new FillLayout(), Messages.getString("ReportWizard.USER_PRODUCT_TITLE"), SWT.SHELL_TRIM); //$NON-NLS-1$
      } else if (appMode == MODE_DIALOG || appMode == MODE_EMBEDDED) {
        inParentContainer = SWTUtility.createShell(1024, 768, new FillLayout(), Messages.getString("ReportWizard.USER_PRODUCT_TITLE"), SWT.DIALOG_TRIM); //$NON-NLS-1$
      }
    }
    this.rdwRoot = rdwRoot;
    this.workingDir = workingDir;
    // force driver load/discovery
    ConnectionUtility.getDrivers(rdwRoot);
    this.parentContainer = inParentContainer;

    if (parentContainer instanceof Shell) {
      ((Shell) parentContainer).open();
      final Cursor cursor = new Cursor(display, SWT.CURSOR_APPSTARTING);
      parentContainer.setCursor(cursor);
    }

    this.container = new Composite(parentContainer, SWT.NONE);
    this.browseFolder = browseFolder;
    loadSettings();
    wizardManager = new WizardManager();
    wizardManager.addWizardListener(this);
    try {
      display = Display.getDefault();
    } catch (Throwable t) {
      // TODO something nice here
      System.err.println(Messages.getString("ReportWizard.ERROR_0001_COULD_NOT_CONFIGURE_GRAPHICS") + t.getMessage()); //$NON-NLS-1$
      return;
    }
    container.setBackground(background);
    if (ReportWizard.applicationMode == ReportWizard.MODE_APPLICATION) {
      createMenuBar();
      setDefaultOutputTypeMenuItem();
    }
    mainStack = new StackLayout();
    container.setLayout(mainStack);
    dialogBase = new Composite(container, SWT.NONE);
    dialogBase.setBackground(background);
    dialogBase.setVisible(false);
    createSplash(container);
    mainStack.topControl = splashScreen;
    splashScreen.setVisible(true);
    GridLayout gridLayout = new GridLayout(1, false);
    gridLayout.marginHeight = 0;
    gridLayout.marginWidth = 0;
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    dialogBase.setLayout(gridLayout);
    dialogBase.setLayoutData(gridData);
    navigationPanel = new NavigationPanel(dialogBase, SWT.NONE, this);
    gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
    gridData.heightHint = 80;
    navigationPanel.setLayoutData(gridData);
    navigationPanel.setBackground(background);
    Composite main = new Composite(dialogBase, SWT.NONE);
    GridLayout mainGridLayout = new GridLayout();
    mainGridLayout.numColumns = 2;
    mainGridLayout.marginHeight = 0;
    mainGridLayout.marginWidth = 0;
    main.setLayout(mainGridLayout);
    gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    main.setLayoutData(gridData);
    main.setBackground(background);
    Composite client = new Composite(main, SWT.NONE);
    stackLayout = new StackLayout();
    client.setLayout(stackLayout);
    client.setBackground(background);
    client.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    templatePanel = new ReportDefinitionPanel(client, SWT.NONE, wizardManager, this);
    templatePanel.setBackground(background);
    templatePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    templatePanel.addDirtyListener(this);
    wizardManager.addStep(templatePanel);
    wizardManager.setCurrentStep(templatePanel);
    queryPanel = new QueryPanel(client, SWT.NONE, wizardManager, this);
    queryPanel.setBackground(background);
    queryPanel.addDirtyListener(this);
    wizardManager.addStep(queryPanel);
    wizardManager.setCurrentStep(queryPanel);
    mappingPanel = new MappingPanel(client, SWT.NONE, wizardManager, this);
    mappingPanel.setBackground(background);
    mappingPanel.addDirtyListener(this);
    wizardManager.addStep(mappingPanel);
    groupAndDetailPanel = new GroupAndDetailPanel(client, SWT.FLAT, wizardManager, this);
    groupAndDetailPanel.setBackground(background);
    groupAndDetailPanel.addDirtyListener(this);
    wizardManager.addStep(groupAndDetailPanel);
    fieldSetupPanel = new FieldSetupPanel(client, SWT.FLAT, wizardManager, this);
    fieldSetupPanel.setBackground(background);
    fieldSetupPanel.addDirtyListener(this);
    wizardManager.addStep(fieldSetupPanel);
    pageSetupPanel = new PageSetupPanel(client, SWT.FLAT, wizardManager, this);
    pageSetupPanel.setBackground(background);
    pageSetupPanel.addDirtyListener(this);
    wizardManager.addStep(pageSetupPanel);
    reportSetupPanel = new ReportSetupPanel(client, SWT.FLAT, wizardManager, this);
    reportSetupPanel.setBackground(background);
    reportSetupPanel.addDirtyListener(this);
    wizardManager.addStep(reportSetupPanel);
    String browserErrorString = "";
    try {
      browser = new Browser(client, SWT.BORDER | SWT.SHADOW_OUT);
    } catch (SWTError e) {
      /*
       * The Browser widget throws an SWTError if it fails to instantiate properly. Application code should catch this SWTError and disable any feature requiring the Browser widget. Platform requirements for the SWT Browser widget are
       * available from the SWT FAQ website.
       */
      browserErrorString = e.getMessage();
    }
    String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
    boolean isLinux = osName.indexOf("linux") >= 0; //$NON-NLS-1$ //$NON-NLS-2$
    if (!isLinux && browser == null) {
      JOptionPane.showMessageDialog(null, "Preview not available due to SWT configuration.  Consult SWT documentation for more information.\n" + browserErrorString);
    }
    queryPanel.setVisible(true);
    Runnable r = new Runnable() {
      public void run() {
        init();
        JFreeReportBoot.getInstance().start();
        PentahoUtility.startup();
        StandaloneSession session = new StandaloneSession("Datasource-Session"); //$NON-NLS-1$
        session.setLoggingLevel(ILogger.ERROR);
        dataSourceAdmin = new StandaloneSimpleJNDIDatasourceAdmin(ReportWizard.simpleJNDIPath, session);
        if (startupFile != null) {
          reportSpecFilePath = startupFile;
          reportSpecStartupFilePath = startupFile;
          doOpen(startupFile, true);
        } else {
          doNew(false);
        }
        splashScreen.setVisible(false);
        dialogBase.setVisible(true);
        mainStack.topControl = dialogBase;
        stackLayout.topControl = queryPanel;
        splashScreen.addMouseListener(new MouseAdapter() {
          public void mouseUp(MouseEvent e) {
            mainStack.topControl = dialogBase;
            splashScreen.setVisible(false);
            dialogBase.setVisible(true);
          }
        });
        handleTabSelection(TAB_TEMPLATE);
        ReportWizard.this.startupQuery = startupQuery;
        final Cursor cursor = new Cursor(display, SWT.CURSOR_ARROW);
        if (parentContainer != null) {
          parentContainer.setCursor(cursor);
          parentContainer.redraw();
          parentContainer.update();
        }
        container.redraw();
        container.update();
      }
    };
    if (ReportWizard.applicationMode == ReportWizard.MODE_APPLICATION) {
      try {
        Display.getCurrent().asyncExec(r);
//        Thread.sleep(3000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      Display.getCurrent().asyncExec(r);
    }
  }

  private void createSplash(Composite parent) {
    splashScreen = new Composite(parent, SWT.NONE);
    GridLayout splashLayout = new GridLayout(1, true);
    splashLayout.marginHeight = 0;
    splashLayout.marginWidth = 0;
    splashScreen.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    splashScreen.setLayout(splashLayout);
    InputStream splashStream = getClass().getResourceAsStream("/images/reporting_splash.jpg"); //$NON-NLS-1$
    final Image splashImage = new Image(display, splashStream); //$NON-NLS-1$
    splashScreen.addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        GC gc = e.gc;
        gc.drawImage(splashImage, 0, 0);
        gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
        gc.drawText(SWTUtility.COPYRIGHTMESSAGE, 350, 500, true);
        Font titleFont = new Font(display, gc.getFont().getFontData()[0].getName(), 18, SWT.NORMAL); //$NON-NLS-1$
        gc.setFont(titleFont);
        String version = org.pentaho.messages.Messages.getString("PentahoSystem.SYSTEM_PLATFORM_VERSION"); //$NON-NLS-1$
        Properties versionProps = new Properties();
        try {
          File versionFile = new File("version.properties");
          if (!versionFile.exists()) {
            versionFile = new File("src/build-res/version.properties");
            if (!versionFile.exists()) {
              versionFile = new File("bin/build-res/version.properties");
            }
          }
          versionProps.load(new FileInputStream(versionFile));
          version = versionProps.getProperty("release.major.number") + "." + versionProps.getProperty("release.minor.number") + "." + versionProps.getProperty("release.milestone.number");
        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        String versionText = Messages.getString("ReportWizard.USER_VERSION_FORMAT", version); //$NON-NLS-1$
        String productTitle = Messages.getString("ReportWizard.USER_PRODUCT_TITLE"); //$NON-NLS-1$ //$NON-NLS-2$
        gc.drawText(productTitle, 350, 260, true);
        Font versionFont = new Font(display, gc.getFont().getFontData()[0].getName(), 13, SWT.NORMAL); //$NON-NLS-1$
        gc.setFont(versionFont);
        gc.drawText(versionText, 350, 470, true);
        gc.dispose();
      }
    });
  }

  public void detachPreview() {
    if (detachedPreviewShell == null || detachedPreviewShell.isDisposed()) {
      detachedPreviewShell = SWTUtility.createShell(800, 600, new FillLayout(), Messages.getString("ReportWizard.USER_PREVIEW"), SWT.MIN | SWT.MAX | SWT.CLOSE | SWT.RESIZE); //$NON-NLS-1$
      try {
        browser = new Browser(detachedPreviewShell, SWT.BORDER);
      } catch (SWTError e) {
      }
      detachedPreviewShell.open();
    }
  }

  public void destroy() {
    PentahoUtility.shutdown();
    TinyHTTPd.stopServer();
  }

  public void init() {
//    loadSettings();
    PentahoUtility.setSolutionRoot(solutionRootPath);
    int port = 1024 + (new Random(System.currentTimeMillis())).nextInt(30720);
    PentahoUtility.setBaseURL("http://localhost:" + port + "/"); //$NON-NLS-1$ //$NON-NLS-2$
    TinyHTTPd.startServer(solutionRootPath, port);
    CWMStartup.loadCWMInstance("resources/metadata/repository.properties", "resources/metadata/PentahoCWM.xml");
  }

  public void setDefaultOutputTypeMenuItem() {
    if (previewType != null) {
      if (previewType.equalsIgnoreCase("html")) { //$NON-NLS-1$
        htmlPreviewTypeMenuItem.setSelection(true);
      } else if (previewType.equalsIgnoreCase("pdf")) { //$NON-NLS-1$
        pdfPreviewTypeMenuItem.setSelection(true);
      } else if (previewType.equalsIgnoreCase("xls")) { //$NON-NLS-1$
        xlsPreviewTypeMenuItem.setSelection(true);
      }
    }
  }

  public void loadSettings() {
    try {
      InputStream settingsStream = getClass().getClassLoader().getResourceAsStream("settings.properties"); //$NON-NLS-1$
      settings.load(settingsStream);
      settingsStream.close();
      solutionRootPath = rdwRoot + settings.getProperty("solutionRoot"); //$NON-NLS-1$
      String tempPath = workingDir + "/temp"; //$NON-NLS-1$
      File tempDir = new File(tempPath);
      tempDir.mkdirs();
      jfreeReportOutputPath = solutionRootPath + "/samples/reporting/"; //$NON-NLS-1$
      exportPath = settings.getProperty("publish.location"); //$NON-NLS-1$
      ConnectionUtility.setRowLimit(Integer.parseInt(settings.getProperty("rowLimit"))); //$NON-NLS-1$
      if (settings.getProperty("report.output.type") != null && !"".equals(settings.getProperty("report.output.type"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        previewType = settings.getProperty("report.output.type"); //$NON-NLS-1$
      }
      if (settings.getProperty("templatePath") != null && !settings.getProperty("templatePath").equals("")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        templatePath = rdwRoot + settings.getProperty("templatePath"); //$NON-NLS-1$
      }
      if (!settings.getProperty("default.numeric.function").equals("")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        defaultNumericFunction = settings.getProperty("default.numeric.function"); //$NON-NLS-1$
      }
      if (!settings.getProperty("simpleJNDIPath").equals("")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        simpleJNDIPath = settings.getProperty("simpleJNDIPath"); //$NON-NLS-1$
      }

      removeCrlfFromQuery = "true".equals(settings.getProperty("removeCRLFInQuery")); //$NON-NLS-1$

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void handleTabSelection(int index) {
    try {
      Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
      container.setCursor(cursor);
      handlingTab = true;
      Composite newPanel = null;
      switch (index) {
      case TAB_TEMPLATE:
        newPanel = templatePanel;
        break;
      case TAB_QUERY:
        newPanel = queryPanel;
        break;
      case TAB_LAYOUT:
        newPanel = groupAndDetailPanel;
        break;
      case TAB_MAPPING:
        newPanel = mappingPanel;
        break;
      case TAB_FORMAT:
        newPanel = fieldSetupPanel;
        break;
      case TAB_PAGE:
        newPanel = pageSetupPanel;
        break;
      case TAB_REPORT:
        newPanel = reportSetupPanel;
        break;
      }

      if (newPanel instanceof WizardPanel) {
        WizardPanel wp = (WizardPanel) newPanel;
        int myIndex = wizardManager.getSteps().indexOf(wp);

        boolean validStep = true;

        for (int i = 0; i < myIndex; i++) {
          WizardPanel step = (WizardPanel) wizardManager.getSteps().get(i);
          if (step.isContinueAllowed()) {
            ignoreWizardEvents = true;
            wizardManager.setCurrentStep(step);
            wizardManager.next();
            ignoreWizardEvents = false;
          } else {
            // uh oh, they can't come here
            validStep = false;
            MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.OK);
            mb.setMessage("You must complete prior steps before proceeding.");
            mb.setText("Error");
            mb.open();
          }
        }
        if (validStep) {
          navigationPanel.setIndex(index);
          templatePanel.setVisible(index == TAB_TEMPLATE);
          queryPanel.setVisible(index == TAB_QUERY);
          mappingPanel.setVisible(index == TAB_MAPPING);
          fieldSetupPanel.setVisible(index == TAB_FORMAT);
          groupAndDetailPanel.setVisible(index == TAB_LAYOUT);
          pageSetupPanel.setVisible(index == TAB_PAGE);
          reportSetupPanel.setVisible(index == TAB_REPORT);
          if (newPanel != null) {
            stackLayout.topControl = newPanel;
          }
          currentTabIndex = index;
          wizardManager.setCurrentStep(wp);
          wizardManager.update();
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      handlingTab = false;
      Cursor cursor = new Cursor(display, SWT.CURSOR_ARROW);
      container.setCursor(cursor);
    }
  }

  public void createMenuBar() {
    if (parentContainer instanceof Shell) {
      Menu bar = new Menu((Shell) parentContainer, SWT.BAR);
      ((Shell) parentContainer).setMenuBar(bar);
      MenuItem fileItem = new MenuItem(bar, SWT.CASCADE);
      fileItem.setText(Messages.getString("ReportWizard.USER_FILE")); //$NON-NLS-1$
      fileItem.setAccelerator(SWT.CTRL + 'F');
      Menu fileSubMenu = new Menu((Shell) parentContainer, SWT.DROP_DOWN);
      fileItem.setMenu(fileSubMenu);
      // MenuItem editItem = new MenuItem(bar, SWT.CASCADE);
      // editItem.setText(Messages.getString("ReportWizard.USER_EDIT")); //$NON-NLS-1$
      // editItem.setAccelerator(SWT.CTRL + 'E');
      // Menu editSubMenu = new Menu(container, SWT.DROP_DOWN);
      // editItem.setMenu(editSubMenu);
      MenuItem viewItem = new MenuItem(bar, SWT.CASCADE);
      viewItem.setText(Messages.getString("ReportWizard.USER_VIEW")); //$NON-NLS-1$
      Menu viewSubMenu = new Menu((Shell) parentContainer, SWT.DROP_DOWN);
      viewItem.setMenu(viewSubMenu);
      MenuItem item = new MenuItem(viewSubMenu, SWT.PUSH);
      item.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          // Runnable r = new Runnable() {
          // public void run() {
          generateReportOutput(reportSpec, jfreeReportOutputPath, previewType, true);
          // }
          // };
          // Thread t = new Thread(r);
          // t.start();
        }
      });
      item.setText(Messages.getString("ReportWizard.USER_GENERATE_PREVIEW")); //$NON-NLS-1$
      MenuItem previewTypeMenu = new MenuItem(viewSubMenu, SWT.CASCADE);
      previewTypeMenu.setText(Messages.getString("ReportWizard.USER_PREVIEW_TYPE")); //$NON-NLS-1$
      Menu previewTypeSubMenu = new Menu(previewTypeMenu);
      previewTypeMenu.setMenu(previewTypeSubMenu);
      htmlPreviewTypeMenuItem = new MenuItem(previewTypeSubMenu, SWT.RADIO);
      htmlPreviewTypeMenuItem.setText(Messages.getString("ReportWizard.USER_HTML")); //$NON-NLS-1$
      htmlPreviewTypeMenuItem.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          super.widgetSelected(e);
          previewType = "html"; //$NON-NLS-1$
        }
      });
      pdfPreviewTypeMenuItem = new MenuItem(previewTypeSubMenu, SWT.RADIO);
      pdfPreviewTypeMenuItem.setText(Messages.getString("ReportWizard.USER_PDF")); //$NON-NLS-1$
      pdfPreviewTypeMenuItem.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          super.widgetSelected(e);
          previewType = "pdf"; //$NON-NLS-1$
        }
      });
      xlsPreviewTypeMenuItem = new MenuItem(previewTypeSubMenu, SWT.RADIO);
      xlsPreviewTypeMenuItem.setText(Messages.getString("ReportWizard.USER_XLS")); //$NON-NLS-1$
      xlsPreviewTypeMenuItem.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          super.widgetSelected(e);
          previewType = "xls"; //$NON-NLS-1$
        }
      });
      MenuItem helpItem = new MenuItem(bar, SWT.CASCADE);
      helpItem.setText(Messages.getString("ReportWizard.USER_HELP")); //$NON-NLS-1$
      Menu helpSubMenu = new Menu((Shell) parentContainer, SWT.DROP_DOWN);
      helpItem.setMenu(helpSubMenu);
      item = new MenuItem(fileSubMenu, SWT.PUSH);
      item.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.YES | SWT.NO);
          mb.setText(Messages.getString("ReportWizard.21")); //$NON-NLS-1$
          mb.setMessage(Messages.getString("ReportWizard.23")); //$NON-NLS-1$
          if (mb.open() == SWT.YES) {
            doSaveAs();
          }
          doNew();
        }
      });
      item.setText(Messages.getString("ReportWizard.USER_NEW")); //$NON-NLS-1$
      item.setAccelerator(SWT.CTRL + 'N');
      item = new MenuItem(fileSubMenu, SWT.PUSH);
      item.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          doOpen();
        }
      });
      item.setText(Messages.getString("ReportWizard.USER_OPEN")); //$NON-NLS-1$
      item.setAccelerator(SWT.CTRL + 'O');
      saveItem = new MenuItem(fileSubMenu, SWT.PUSH);
      saveItem.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          doSave();
        }
      });
      saveItem.setText(Messages.getString("ReportWizard.USER_SAVE")); //$NON-NLS-1$
      saveItem.setAccelerator(SWT.CTRL + 'S');
      saveItem.setEnabled(isSaved);
      item = new MenuItem(fileSubMenu, SWT.PUSH);
      item.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          doSaveAs();
        }
      });
      item.setText(Messages.getString("ReportWizard.USER_SAVE_AS")); //$NON-NLS-1$
      item = new MenuItem(fileSubMenu, SWT.SEPARATOR);
      item = new MenuItem(fileSubMenu, SWT.PUSH);
      item.setText(Messages.getString("ReportWizard.70")); //$NON-NLS-1$
      item.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          doPublishReport();
        }
      });
      item = new MenuItem(fileSubMenu, SWT.SEPARATOR);
      item = new MenuItem(fileSubMenu, SWT.PUSH);
      item.setText(Messages.getString("ReportWizard.2")); //$NON-NLS-1$
      item.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          FileOutputStream fos = null;
          try {
            FileDialog fileDialog = new FileDialog(parentContainer.getShell(), SWT.SAVE);
            String path = fileDialog.open();
            if (path != null) {
              File f = new File(path);
              int result = SWT.YES;
              if (f.exists()) {
                // prompt to overwrite
                MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.YES | SWT.NO);
                mb.setText(Messages.getString("ReportWizard.0")); //$NON-NLS-1$
                mb.setMessage(Messages.getString("ReportWizard.1")); //$NON-NLS-1$
                result = mb.open();
              }
              if (result == SWT.YES) {
                fos = new FileOutputStream(f);
                IPentahoResultSet resultSet = (IPentahoResultSet) ConnectionUtility.queryResultSetMap.get(reportSpec.getQuery());
                try {
                  resultSet.beforeFirst();
                } catch (Exception ex) {
                  ex.printStackTrace();
                }
                String xmlString = DataUtilities.getXMLString(resultSet);
                fos.write(xmlString.getBytes());
                fos.close();
              }
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          } finally {
            try {
              fos.close();
            } catch (Exception ex) {
            }
          }
        }
      });
      item = new MenuItem(fileSubMenu, SWT.SEPARATOR);
      item = new MenuItem(fileSubMenu, SWT.PUSH);
      item.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.YES | SWT.NO);
          mb.setText(Messages.getString("ReportWizard.21")); //$NON-NLS-1$
          mb.setMessage(Messages.getString("ReportWizard.22")); //$NON-NLS-1$
          if (mb.open() == SWT.YES) {
            doSaveAs();
          }
          parentContainer.dispose();
        }
      });
      item.setText(Messages.getString("ReportWizard.USER_EXIT")); //$NON-NLS-1$
      item = new MenuItem(viewSubMenu, SWT.SEPARATOR);
      item = new MenuItem(viewSubMenu, SWT.PUSH);
      item.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          wizardManager.preview();
          // generateReportOutput(reportSpec, jfreeReportOutputPath, "pdf", true); //$NON-NLS-1$
        }
      });
      item.setText(Messages.getString("ReportWizard.USER_PREVIEW_PDF")); //$NON-NLS-1$
      item = new MenuItem(viewSubMenu, SWT.PUSH);
      item.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          generateReportOutput(reportSpec, jfreeReportOutputPath, "html", true); //$NON-NLS-1$
        }
      });
      item.setText(Messages.getString("ReportWizard.USER_PREVIEW_HTML")); //$NON-NLS-1$
      item = new MenuItem(viewSubMenu, SWT.PUSH);
      item.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          generateReportOutput(reportSpec, jfreeReportOutputPath, "xls", true); //$NON-NLS-1$
        }
      });
      item.setText(Messages.getString("ReportWizard.USER_PREVIEW_EXCEL")); //$NON-NLS-1$
      item = new MenuItem(helpSubMenu, SWT.PUSH);
      item.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
          splashScreen.setVisible(true);
          dialogBase.setVisible(false);
          mainStack.topControl = splashScreen;
          /*
           * Shell container = SWTUtility.createModalDialogShell(450, 180, "About"); container.setLayout(new FillLayout()); Text text = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL); text.setText(SWTUtility.COPYRIGHTMESSAGE);
           * container.open();
           */
        }
      });
      item.setText(Messages.getString("ReportWizard.USER_ABOUT")); //$NON-NLS-1$
    }
  }

  public void doPublishReport() {
    PublishDialog publishDialog = new PublishDialog(Messages.getString("ReportWizard.44"), reportSpec, settings); //$NON-NLS-1$
    if (publishDialog.open()) {
      try {
        if (!publishDialog.publishToServer) {
          String oldName = reportSpec.getReportName();
          String reportName = publishDialog.name;
          reportSpec.setReportName(reportName);
          System.out.println(Messages.getString("ReportWizard.DEBUG_WRITING", jfreeReportOutputPath + reportSpec.getReportName() + ".xml")); //$NON-NLS-1$//$NON-NLS-2$
          // report name
          String watermarkSrc = reportSpec.getWatermark() != null ? reportSpec.getWatermark().getSrc() : null;
          // need to copy jfreereport
          String jfreeFile = publishDialog.location + File.separator + reportName + ".xml"; //$NON-NLS-1$
          String jfreeDestDir = publishDialog.location + File.separator;
          if (reportSpec.getTemplateSrc() != null) {
            doExportImagesToLocation(jfreeDestDir, templateImageMap);
          }
          if (watermarkSrc != null) {
            watermarkSrc = new File(reportSpec.getWatermark().getSrc()).getName();
            String watermarkFileName = jfreeDestDir + "/" + watermarkSrc; //$NON-NLS-1$ //$NON-NLS-2$
            ReportSpecUtility.copyFile(reportSpec.getWatermark().getSrc(), watermarkFileName);
            reportSpec.getWatermark().setSrc(watermarkFileName);
          }
          String includeSrc = reportSpec.getIncludeSrc();
          if (includeSrc != null && !includeSrc.equalsIgnoreCase("")) { //$NON-NLS-1$
            includeSrc = new File(includeSrc).getName();
            String includeFileName = jfreeDestDir + "/" + includeSrc; //$NON-NLS-1$ //$NON-NLS-2$
            ReportSpecUtility.copyFile(reportSpec.getIncludeSrc(), includeFileName);
            reportSpec.setIncludeSrc(includeFileName);
          }
          if (reportSpec.getTemplateSrc() != null) {
            FileOutputStream outStream = new FileOutputStream(publishDialog.location + File.separator + reportSpec.getReportName() + "_generated.xml"); //$NON-NLS-1$
            ReportGenerationUtility.createJFreeReportXML(reportSpec, outStream, 0, 0, false, "", 0, 0); //$NON-NLS-1$
            FileOutputStream mergeStream = new FileOutputStream(jfreeFile); //$NON-NLS-1$
            File templateFile = new File(reportSpec.getTemplateSrc());
            ReportGenerationUtility.mergeTemplate(templateFile, new File(publishDialog.location + File.separator + reportSpec.getReportName() + "_generated.xml"), mergeStream); //$NON-NLS-1$
            generateReportOutput(reportSpec, publishDialog.location + File.separator, publishDialog.reportType, false);
          } else {
            // need to generate jfreereport xml
            FileOutputStream outStream = new FileOutputStream(jfreeFile); //$NON-NLS-1$
            ReportGenerationUtility.createJFreeReportXML(reportSpec, outStream, 0, 0, false, "", 0, 0); //$NON-NLS-1$
            generateReportOutput(reportSpec, publishDialog.location + File.separator, publishDialog.reportType, false); //$NON-NLS-1$
          }
          if (jfreeDestDir != null && !jfreeDestDir.equals("") && reportName != null && !reportName.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
            String jfreeDestFile = jfreeDestDir + "/" + reportName + ".xml"; //$NON-NLS-1$ //$NON-NLS-2$
            ReportSpecUtility.copyFile(jfreeFile, jfreeDestFile);
            String propertyDestFile = jfreeDestDir + "/" + reportName + ".properties"; //$NON-NLS-1$ //$NON-NLS-2$
            FileOutputStream propertyFileOutputStream = new FileOutputStream(propertyDestFile);
            String nameProp = "title=" + reportName + "\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
            String descProp = "description=" + reportSpec.getReportDesc() + "\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
            propertyFileOutputStream.write(nameProp.getBytes());
            propertyFileOutputStream.write(descProp.getBytes());
            propertyFileOutputStream.flush();
            propertyFileOutputStream.close();
            String actionSequenceSrcFile = jfreeReportOutputPath + reportName + ".xaction"; //$NON-NLS-1$
            String actionSequenceDestFile = jfreeDestDir + "/" + reportName + ".xaction"; //$NON-NLS-1$ //$NON-NLS-2$
            ReportSpecUtility.copyFile(actionSequenceSrcFile, actionSequenceDestFile);
            String reportSpecDestFile = jfreeDestDir + "/" + reportName + ".xreportspec"; //$NON-NLS-1$ //$NON-NLS-2$
            if (reportSpecFilePath == null) {
              // write reportSpec out
              File tempFile = File.createTempFile("report", ".xreportsec", new File("./temp")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
              tempFile.deleteOnExit();
              CastorUtility.getInstance().writeCastorObject(reportSpec, tempFile.getAbsolutePath());
              ReportSpecUtility.copyFile(tempFile.getAbsolutePath(), reportSpecDestFile);
            } else {
              ReportSpecUtility.copyFile(reportSpecFilePath, reportSpecDestFile);
            }
            ReportSpecUtility.copyFile(jfreeReportOutputPath + "PentahoReporting.png", jfreeDestDir + "/PentahoReporting.png"); //$NON-NLS-1$ //$NON-NLS-2$
            String cubeDefPath = reportSpec.getMondrianCubeDefinitionPath();
            if (cubeDefPath != null && !cubeDefPath.equals("")) { //$NON-NLS-1$
              File cubePathFile = new File(cubeDefPath);
              ReportSpecUtility.copyFile(cubeDefPath, jfreeDestDir + "/" + cubePathFile.getName()); //$NON-NLS-1$
            }
            ReportSpecChoice choice = reportSpec.getReportSpecChoice();
            if (choice == null) {
              // TODO log this
            } else {
              String xqueryPath = choice.getXqueryUrl();
              if (xqueryPath != null && !xqueryPath.equals("")) { //$NON-NLS-1$
                File xqueryPathFile = new File(xqueryPath);
                ReportSpecUtility.copyFile(xqueryPath, jfreeDestDir + "/" + xqueryPathFile.getName()); //$NON-NLS-1$
              }
            }
          }
          if (publishDialog.createDataSource) {
            String jbossDeployDir = publishDialog.deployDir;
            String webAppName = publishDialog.webAppName;
            String jndi = reportSpec.getReportSpecChoice().getJndiSource();
            if (jndi != null) {
              JBossDatasourceAdmin jbossDSAdmin = new JBossDatasourceAdmin();
              jbossDSAdmin.setApplicationRoot(jbossDeployDir);
              jbossDSAdmin.setWebApplicationName(webAppName);
              Iterator keyIterator = jbossDSAdmin.listContainerDataSources().keySet().iterator();
              boolean exists = false;
              while (keyIterator.hasNext()) {
                String key = (String) keyIterator.next();
                if (jndi.equals(key)) {
                  exists = true;
                  break;
                }
              }
              if (!exists) {
                MessageBox mb = new MessageBox(container.getShell(), SWT.YES | SWT.NO);
                mb.setText(Messages.getString("ReportWizard.57")); //$NON-NLS-1$
                mb.setMessage(Messages.getString("ReportWizard.66") + jndi + Messages.getString("ReportWizard.58")); //$NON-NLS-1$ //$NON-NLS-2$
                int result = mb.open();
                if (result == SWT.YES) {
                  DataSourceInfo simpleJNDIDS = dataSourceAdmin.getDataSourceInfo(jndi);
                  jbossDSAdmin.saveDataSource(simpleJNDIDS, false);
                }
              }
            }
          }
          reportSpec.setReportName(oldName);
          MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.OK);
          mb.setText(Messages.getString("ReportWizard.USER_PUBLISH_TITLE")); //$NON-NLS-1$
          mb.setMessage(Messages.getString("ReportWizard.USER_PUBLISH_SUCCESSFUL")); //$NON-NLS-1$
          mb.open();
        } else {
          String oldName = reportSpec.getReportName();
          String reportName = publishDialog.name;
          reportSpec.setReportName(reportName);
          System.out.println(Messages.getString("ReportWizard.DEBUG_WRITING", jfreeReportOutputPath + reportSpec.getReportName() + ".xml")); //$NON-NLS-1$//$NON-NLS-2$
          // report name
          String watermarkSrc = reportSpec.getWatermark() != null ? reportSpec.getWatermark().getSrc() : null;
          // need to copy jfreereport
          String publishPath = publishDialog.location;
          publishDialog.location = File.createTempFile(Messages.getString("ReportWizard.45"), "tmp").getParent() + File.separator + "exportDir" + (new Random(System.currentTimeMillis()).nextLong()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          File locDir = new File(publishDialog.location);
          locDir.mkdirs();
          String jfreeFile = publishDialog.location + File.separator + reportName + ".xml"; //$NON-NLS-1$
          String jfreeDestDir = publishDialog.location + File.separator;
          if (reportSpec.getTemplateSrc() != null) {
            doExportImagesToLocation(jfreeDestDir, templateImageMap);
          }
          if (watermarkSrc != null) {
            watermarkSrc = new File(reportSpec.getWatermark().getSrc()).getName();
            String watermarkFileName = jfreeDestDir + "/" + watermarkSrc; //$NON-NLS-1$ //$NON-NLS-2$
            ReportSpecUtility.copyFile(reportSpec.getWatermark().getSrc(), watermarkFileName);
            reportSpec.getWatermark().setSrc(watermarkFileName);
          }
          String includeSrc = reportSpec.getIncludeSrc();
          if (includeSrc != null && !includeSrc.equalsIgnoreCase("")) { //$NON-NLS-1$
            includeSrc = new File(includeSrc).getName();
            String includeFileName = jfreeDestDir + "/" + includeSrc; //$NON-NLS-1$ //$NON-NLS-2$
            ReportSpecUtility.copyFile(reportSpec.getIncludeSrc(), includeFileName);
            reportSpec.setIncludeSrc(includeFileName);
          }
          if (reportSpec.getTemplateSrc() != null) {
            FileOutputStream outStream = new FileOutputStream(publishDialog.location + File.separator + reportSpec.getReportName() + "_generated.xml"); //$NON-NLS-1$
            ReportGenerationUtility.createJFreeReportXML(reportSpec, outStream, 0, 0, false, "", 0, 0); //$NON-NLS-1$
            FileOutputStream mergeStream = new FileOutputStream(jfreeFile); //$NON-NLS-1$
            File templateFile = new File(reportSpec.getTemplateSrc());
            ReportGenerationUtility.mergeTemplate(templateFile, new File(publishDialog.location + File.separator + reportSpec.getReportName() + "_generated.xml"), mergeStream); //$NON-NLS-1$
            generateReportOutput(reportSpec, publishDialog.location + File.separator, publishDialog.reportType, false);
          } else {
            // need to generate jfreereport xml
            FileOutputStream outStream = new FileOutputStream(jfreeFile); //$NON-NLS-1$
            ReportGenerationUtility.createJFreeReportXML(reportSpec, outStream, 0, 0, false, "", 0, 0); //$NON-NLS-1$
            generateReportOutput(reportSpec, publishDialog.location + File.separator, publishDialog.reportType, false); //$NON-NLS-1$
          }
          if (jfreeDestDir != null && !jfreeDestDir.equals("") && reportName != null && !reportName.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
            String jfreeDestFile = jfreeDestDir + "/" + reportName + ".xml"; //$NON-NLS-1$ //$NON-NLS-2$
            ReportSpecUtility.copyFile(jfreeFile, jfreeDestFile);
            String propertyDestFile = jfreeDestDir + "/" + reportName + ".properties"; //$NON-NLS-1$ //$NON-NLS-2$
            FileOutputStream propertyFileOutputStream = new FileOutputStream(propertyDestFile);
            String nameProp = "title=" + reportName + "\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
            String descProp = "description=" + reportSpec.getReportDesc() + "\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
            propertyFileOutputStream.write(nameProp.getBytes());
            propertyFileOutputStream.write(descProp.getBytes());
            propertyFileOutputStream.flush();
            propertyFileOutputStream.close();
            String actionSequenceSrcFile = jfreeReportOutputPath + reportName + ".xaction"; //$NON-NLS-1$
            String actionSequenceDestFile = jfreeDestDir + "/" + reportName + ".xaction"; //$NON-NLS-1$ //$NON-NLS-2$
            ReportSpecUtility.copyFile(actionSequenceSrcFile, actionSequenceDestFile);
            String reportSpecDestFile = jfreeDestDir + "/" + reportName + ".xreportspec"; //$NON-NLS-1$ //$NON-NLS-2$
            if (reportSpecFilePath == null) {
              // write reportSpec out
              File tempFile = File.createTempFile("report", ".xreportsec", new File("./temp")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
              tempFile.deleteOnExit();
              CastorUtility.getInstance().writeCastorObject(reportSpec, tempFile.getAbsolutePath());
              ReportSpecUtility.copyFile(tempFile.getAbsolutePath(), reportSpecDestFile);
            } else {
              ReportSpecUtility.copyFile(reportSpecFilePath, reportSpecDestFile);
            }
            String cubeDefPath = reportSpec.getMondrianCubeDefinitionPath();
            if (cubeDefPath != null && !cubeDefPath.equals("")) { //$NON-NLS-1$
              File cubePathFile = new File(cubeDefPath);
              ReportSpecUtility.copyFile(cubeDefPath, jfreeDestDir + "/" + cubePathFile.getName()); //$NON-NLS-1$
            }
            ReportSpecChoice choice = reportSpec.getReportSpecChoice();
            if (choice == null) {
              // TODO log this
            } else {
              String xqueryPath = choice.getXqueryUrl();
              if (xqueryPath != null && !xqueryPath.equals("")) { //$NON-NLS-1$
                File xqueryPathFile = new File(xqueryPath);
                ReportSpecUtility.copyFile(xqueryPath, jfreeDestDir + "/" + xqueryPathFile.getName()); //$NON-NLS-1$
              }
            }
          }
          reportSpec.setReportName(oldName);
          if (publishPath.startsWith(File.separator)) {
            publishPath = publishPath.substring(1);
          }
          if (!publishPath.endsWith(File.separator)) {
            publishPath = publishPath + File.separator;
          }
          int rtn = ISolutionRepository.FILE_ADD_SUCCESSFUL;
          if (reportSpec.getReportSpecChoice() != null && reportSpec.getReportSpecChoice().getJndiSource() != null) {
            rtn = PublisherUtil.publish(publishDialog.publishURL, publishPath, locDir.listFiles(), dataSourceAdmin.getDataSourceInfo(reportSpec.getReportSpecChoice().getJndiSource()), publishDialog.publishPW, publishDialog.serverUID,
                publishDialog.serverPW, false);
          } else {
            rtn = PublisherUtil.publish(publishDialog.publishURL, publishPath, locDir.listFiles(), publishDialog.publishPW, publishDialog.serverUID, publishDialog.serverPW, false);
          }
          MessageBox mb = null;
          int whichBtns = (rtn == ISolutionRepository.FILE_EXISTS) ? SWT.YES | SWT.NO : SWT.OK;
          mb = new MessageBox(parentContainer.getShell(), whichBtns);
          String extraMsg = "";
          if (rtn == ISolutionRepository.FILE_ADD_FAILED) {
            extraMsg = " " + Messages.getString("ReportWizard.SEE_SERVER_LOGS");
          } else if (rtn == ISolutionRepository.FILE_EXISTS) {
            extraMsg = " " + Messages.getString("ReportWizard.LIKE_T0_OVERWRITE");
          }

          String msg = ISolutionRepository.FILE_STATUS_MSG[rtn];
          mb.setMessage(msg + extraMsg);

          mb.setText(Messages.getString("ReportWizard.USER_PUBLISH_TITLE")); //$NON-NLS-1$
          int promptStatus = mb.open();
          if (promptStatus == SWT.YES) {
            if (reportSpec.getReportSpecChoice().getJndiSource() != null) {
              rtn = PublisherUtil.publish(publishDialog.publishURL, publishPath, locDir.listFiles(), dataSourceAdmin.getDataSourceInfo(reportSpec.getReportSpecChoice().getJndiSource()), publishDialog.publishPW, publishDialog.serverUID,
                  publishDialog.serverPW, true);
            } else {
              rtn = PublisherUtil.publish(publishDialog.publishURL, publishPath, locDir.listFiles(), publishDialog.publishPW, publishDialog.serverUID, publishDialog.serverPW, true);
            }

            extraMsg = "";
            if (rtn == ISolutionRepository.FILE_ADD_FAILED) {
              extraMsg = " " + Messages.getString("ReportWizard.SEE_SERVER_LOGS");
            } else if (rtn == ISolutionRepository.FILE_EXISTS) {
              extraMsg = " " + Messages.getString("ReportWizard.CANNOT_OVERWRITE");
            }

            msg = ISolutionRepository.FILE_STATUS_MSG[rtn];
            mb = new MessageBox(parentContainer.getShell(), SWT.OK);
            mb.setMessage(msg + extraMsg);
            mb.open();
          }
          // publish the PentahoReporting.png, fail silently as this file is not critical
          File files[] = locDir.listFiles();
          for (int i = 0; i < files.length; i++) {
            files[i].delete();
          }
          ReportSpecUtility.copyFile(jfreeReportOutputPath + "PentahoReporting.png", jfreeDestDir + "/PentahoReporting.png"); //$NON-NLS-1$ //$NON-NLS-2$
          PublisherUtil.publish(publishDialog.publishURL, publishPath, locDir.listFiles(), publishDialog.publishPW, publishDialog.serverUID, publishDialog.serverPW, false);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void doExportImagesToLocation(String jfreeDestDir, HashMap images) {
    try {
      String templateSrc = reportSpec.getTemplateSrc();
      FileInputStream fis = new FileInputStream(templateSrc);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte bytes[] = new byte[1024];
      int numRead = 0;
      while ((numRead = fis.read(bytes)) != -1) {
        if (numRead > 0) {
          baos.write(bytes, 0, numRead);
        }
      }
      String templateStr = baos.toString();
      Iterator keys = images.keySet().iterator();
      while (keys.hasNext()) {
        String key = (String) keys.next();
        String value = (String) images.get(key);
        // value is path to image
        URL fileURL = new URL(value);
        InputStream is = fileURL.openStream();
        String name = jfreeDestDir + "/" + (new File(key)).getName(); //$NON-NLS-1$
        String outFilePath = (new File(name)).getCanonicalPath();
        FileOutputStream fos = new FileOutputStream(name);
        while ((numRead = is.read(bytes)) != -1) {
          if (numRead > 0) {
            fos.write(bytes, 0, numRead);
          }
        }
        fos.close();
        templateStr = templateStr.replaceAll(value, "file://" + outFilePath.replace('\\', '/')); //$NON-NLS-1$
      }
      FileOutputStream fos = new FileOutputStream(templateSrc);
      fos.write(templateStr.getBytes());
      fos.close();
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public void doNew(boolean setStep) {
    // prompt to be sure
    reportSpec = new ReportSpec();
    if (startupQuery != null) {
      reportSpec.setQuery(startupQuery);
    }
    reportSpecFilePath = null;
    templateZipPath = null;
    pentahoConnection = null;
    initWizardPanels();
    if (setStep) {
      handleTabSelection(TAB_TEMPLATE);
    }
    wizardManager.update();
    getTypeMap().clear();
    ConnectionUtility.queryColumnMap.clear();
    ConnectionUtility.queryResultSetMap.clear();
  }

  public void doNew() {
    doNew(true);
  }

  public void doOpenTemplate(String path, boolean setStep) {
    // clear all settings
//    doNew(false);
    try {
      // crack archive, pull out thumbnail.png (or jpg)
      ZipFile zipFile = new ZipFile(path);
      // ZipEntry thumbnailEntry = zipFile.getEntry("thumbnail.png");
      // thumbnailImage = new Image(getDisplay(),
      // zipFile.getInputStream(thumbnailEntry));
      ZipEntry reportSpecEntry = zipFile.getEntry("report.xreportspec"); //$NON-NLS-1$
      File tempFile = File.createTempFile("report", ".xreportsec", new File("./temp")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      tempFile.deleteOnExit();
      FileOutputStream fos = new FileOutputStream(tempFile);
      InputStream inputStream = zipFile.getInputStream(reportSpecEntry);
      byte[] bytes = new byte[32768];
      int numRead = 0;
      while ((numRead = inputStream.read(bytes)) != -1) {
        if (numRead > 0) {
          fos.write(bytes, 0, numRead);
        }
      }
      fos.close();
      reportSpecFilePath = tempFile.getAbsolutePath();
      doOpen(reportSpecFilePath, false);
      ZipEntry jfreereportTemplateEntry = zipFile.getEntry("jfreereport-template.xml"); //$NON-NLS-1$
      tempFile = File.createTempFile("jfreereport-template", ".xml", new File("./temp")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      tempFile.deleteOnExit();
      fos = new FileOutputStream(tempFile);
      inputStream = zipFile.getInputStream(jfreereportTemplateEntry);
      numRead = 0;
      while ((numRead = inputStream.read(bytes)) != -1) {
        if (numRead > 0) {
          fos.write(bytes, 0, numRead);
        }
      }
      fos.close();
      reportSpec.setTemplateSrc(tempFile.getAbsolutePath());
      // merge parser-config properties into report-spec (so we have defaults to work with)
      FieldMapping[] fieldMappings = reportSpec.getFieldMapping();
      List parserConfigElements = ReportSpecUtility.getParserConfigElements(reportSpec.getTemplateSrc());
      for (int i = 0; i < parserConfigElements.size(); i++) {
        Element property = (Element) parserConfigElements.get(i);
        Node commentNode = property.selectSingleNode("comment()"); //$NON-NLS-1$
        FieldMapping fm = new FieldMapping();
        fm.setKey(property.attributeValue("name")); //$NON-NLS-1$
        fm.setValue(property.getText());
        if (commentNode != null) {
          String type = commentNode.getText().trim();
          fm.setType(type);
        }
        boolean mapExists = false;
        for (int j = 0; fieldMappings != null && j < fieldMappings.length; j++) {
          FieldMapping fmtmp = fieldMappings[j];
          if (fmtmp.getKey().equals(fm.getKey())) {
            // we already have a fieldMapping, use value from report-spec not template
            mapExists = true;
            break;
          }
        }
        if (!mapExists) {
          // create one, add it
          reportSpec.addFieldMapping(fm);
        }
      }
      ZipEntry xqueryDataEntry = zipFile.getEntry("data.xml"); //$NON-NLS-1$
      if (xqueryDataEntry != null) {
        tempFile = File.createTempFile("data", ".xml", new File("./temp")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        tempFile.deleteOnExit();
        fos = new FileOutputStream(tempFile);
        inputStream = zipFile.getInputStream(xqueryDataEntry);
        numRead = 0;
        while ((numRead = inputStream.read(bytes)) != -1) {
          if (numRead > 0) {
            fos.write(bytes, 0, numRead);
          }
        }
        reportSpec.getReportSpecChoice().setXqueryUrl(tempFile.getAbsolutePath());
        fos.close();
      }
      Enumeration entries = zipFile.entries();
      templateImageMap.clear();
      if (entries != null) {
        while (entries.hasMoreElements()) {
          ZipEntry entry = (ZipEntry) entries.nextElement();
          if (!entry.isDirectory() && entry.getName().startsWith("images/")) { //$NON-NLS-1$
            tempFile = File.createTempFile(entry.getName().substring(entry.getName().indexOf('/'), entry.getName().indexOf('.')), entry.getName().substring(entry.getName().indexOf('.')), new File("./temp")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            tempFile.deleteOnExit();
            fos = new FileOutputStream(tempFile);
            inputStream = zipFile.getInputStream(entry);
            numRead = 0;
            if (entry.getName().indexOf("watermark") != -1) { //$NON-NLS-1$
              String watermarkFileName = tempFile.getCanonicalPath();
              // update watermark
              Watermark wm = reportSpec.getWatermark();
              if (wm == null) {
                wm = new Watermark();
              }
              wm.setSrc(watermarkFileName);
              reportSpec.setWatermark(wm);
            }
            while ((numRead = inputStream.read(bytes)) != -1) {
              if (numRead > 0) {
                fos.write(bytes, 0, numRead);
              }
            }
            // map original name to new name
            templateImageMap.put(entry.getName(), "file://" + tempFile.getCanonicalPath().replace('\\', '/')); //$NON-NLS-1$
            fos.close();
          }
        }
      }
      // update jfreereport xml definition with new image paths
      updateJFreeReportImages(reportSpec.getTemplateSrc(), templateImageMap);
      zipFile.close();
      // reconcile reportSpec with query (JDBC)
      reconcileReportSpec(reportSpec);
      initWizardPanels();
      isSaved = true;
      if (saveItem != null) {
        saveItem.setEnabled(isSaved);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    templateZipPath = path;
  }

  public void updateJFreeReportImages(String templateSrc, HashMap templateImageMap) {
    // read in templateSrc
    try {
      FileInputStream inputStream = new FileInputStream(templateSrc);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte bytes[] = new byte[1024];
      int numRead = 0;
      while ((numRead = inputStream.read(bytes)) != -1) {
        if (numRead > 0) {
          baos.write(bytes, 0, numRead);
        }
      }
      String templateStr = baos.toString();
      Iterator keys = templateImageMap.keySet().iterator();
      // replace all references to the keys with the templateImageMaps values in the JFreeReport XML
      while (keys.hasNext()) {
        String key = (String) keys.next();
        String value = (String) templateImageMap.get(key);
        templateStr = templateStr.replaceAll(key, value);
      }
      inputStream.close();
      FileOutputStream fos = new FileOutputStream(templateSrc);
      fos.write(templateStr.getBytes());
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void doOpen(final String path, final boolean setStep) {
    // clear all settings
    try {
      doNew(setStep);
      reportSpecFilePath = path;
      isSaved = true;
      if (ReportWizard.applicationMode == ReportWizard.MODE_APPLICATION) {
        saveItem.setEnabled(isSaved);
      }
      Runnable r = new Runnable() {
        public void run() {
          boolean isZIPReportSpec = false;
          try {
            ZipFile zipFile = new ZipFile(reportSpecFilePath);
            if (zipFile.getEntry("jfreereport-template.xml") != null) { //$NON-NLS-1$
              doOpenTemplate(path, setStep);
              return;
            }
            ZipEntry reportSpecEntry = zipFile.getEntry("report.xreportspec"); //$NON-NLS-1$          
            File tempFile = File.createTempFile("report", ".xreportspec", new File(workingDir + "/temp")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            tempFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempFile);
            InputStream inputStream = zipFile.getInputStream(reportSpecEntry);
            int numRead = 0;
            byte bytes[] = new byte[16384];
            while ((numRead = inputStream.read(bytes)) != -1) {
              if (numRead > 0) {
                fos.write(bytes, 0, numRead);
              }
            }
            fos.close();
            reportSpec = (ReportSpec) CastorUtility.getInstance().readCastorObject(tempFile.getAbsolutePath(), ReportSpec.class);
            isZIPReportSpec = true;
          } catch (Exception e) {
            // e.printStackTrace(System.err);
          }
          if (!isZIPReportSpec) {
            if (reportSpecFilePath != null) {
              reportSpec = (ReportSpec) CastorUtility.getInstance().readCastorObject(reportSpecFilePath, ReportSpec.class);
            }
          }
          try {
            if (startupQuery != null) {
              reportSpec.setQuery(startupQuery);
            }
            startupQuery = null;
            if (reportSpec.getReportSpecChoice() != null) {
              if (reportSpec.getReportSpecChoice().getJndiSource() != null) {
                setPentahoConnection(ConnectionUtility.getConnection(reportSpec.getReportSpecChoice().getJndiSource(), reportSpec.getMondrianCubeDefinitionPath(),
                    reportSpec.getMondrianCubeDefinitionPath() == null ? IPentahoConnection.SQL_DATASOURCE : IPentahoConnection.MDX_DATASOURCE));
              } else if (reportSpec.getReportSpecChoice().getXqueryUrl() != null && !reportSpec.getReportSpecChoice().getXqueryUrl().equals("")) { //$NON-NLS-1$
                setPentahoConnection(ConnectionUtility.getConnection(reportSpec.getReportSpecChoice().getXqueryUrl(), null, IPentahoConnection.XML_DATASOURCE));
              }
            } else if (reportSpec.getIsMQL()) {
              System.out.println("reportSpec.getXmiPath() = " + reportSpec.getXmiPath());
              CWMStartup.loadMetadata(reportSpec.getXmiPath(), "samples");
              setPentahoConnection(ConnectionUtility.getMQLConnection(reportSpec));
            }
            if (reportSpec != null) {
              List groups = new ArrayList();
              List details = new ArrayList();
              Field fields[] = reportSpec.getField();
              String columns[] = new String[fields.length];
              for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                columns[i] = field.getName();
                getTypeMap().put(field.getName(), new Integer(field.getType()));
                if (field.getIsDetail()) {
                  details.add(field.getName());
                } else {
                  groups.add(field.getName());
                }
              }
            }
            reportSpec.setTemplateSrc(null);
            initWizardPanels();
          } catch (Exception e) {
            e.printStackTrace(System.err);
            MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.OK);
            mb.setMessage(Messages.getString("ReportWizard.ERROR_0002_OPEN_REPORT_FAILED") + e.toString()); //$NON-NLS-1$
            mb.setText(Messages.getString("ReportWizard.USER_ERROR")); //$NON-NLS-1$
            mb.open();
          }
        }
      };
      if (setStep) {
        handleTabSelection(TAB_TEMPLATE);
      }
      BusyIndicator.showWhile(display, r);
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public void reconcileReportSpec(ReportSpec reportSpec) {
    Object[] cols = null;
    if (reportSpec.getReportSpecChoice() != null) {
      try {
        if (reportSpec.getReportSpecChoice().getJndiSource() != null) {
          if (reportSpec.getIsMDX()) {
            setPentahoConnection(ConnectionUtility.getConnection(reportSpec.getReportSpecChoice().getJndiSource(), reportSpec.getMondrianCubeDefinitionPath(), IPentahoConnection.MDX_DATASOURCE));
            IPentahoConnection connection = getPentahoConnection();
            cols = ConnectionUtility.getColumns(connection, reportSpec.getQuery(), reportSpec.getReportSpecChoice().getXqueryUrl());
          } else {
            setPentahoConnection(ConnectionUtility.getConnection(reportSpec.getReportSpecChoice().getJndiSource()));
            IPentahoConnection connection = getPentahoConnection();
            cols = ConnectionUtility.getColumns(connection, reportSpec.getQuery(), reportSpec.getReportSpecChoice().getXqueryUrl());
          }
        } else if (reportSpec.getReportSpecChoice().getKettleUrl() != null) {
          setPentahoConnection(null);
          cols = ConnectionUtility.getKettleColumns(reportSpec, reportSpec.getReportSpecChoice().getKettleUrl());
        } else {
          setPentahoConnection(ConnectionUtility.getConnection(reportSpec.getReportSpecChoice().getXqueryUrl(), null, IPentahoConnection.XML_DATASOURCE));
          IPentahoConnection connection = getPentahoConnection();
          cols = ConnectionUtility.getColumns(connection, reportSpec.getQuery(), reportSpec.getReportSpecChoice().getXqueryUrl());
        }
      } catch (Exception ex) {
        MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.OK);
        mb.setMessage(ex.getMessage());
        mb.setText("Error");
        mb.open();
      }
    }
    if (cols == null) {
      return;
    }
    // if columns from the query perfectly match the reportSpec, prompt to ask
    // if to overwrite
    boolean perfectMatch = reportSpec.getFieldCount() > 0;
    for (int i = 0; i < reportSpec.getFieldCount(); i++) {
      Field f = reportSpec.getField(i);
      boolean found = false;
      for (int j = 0; j < cols.length; j++) {
        if (f.getName().equalsIgnoreCase(cols[j].toString())) {
          found = true;
        }
      }
      if (!found) {
        perfectMatch = false;
        break;
      }
    }
    if (!perfectMatch && reportSpec.getFieldCount() > 0) {
      // prompt
      MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.YES | SWT.NO);
      mb.setText(Messages.getString("ReportWizard.14")); //$NON-NLS-1$
      mb.setMessage(Messages.getString("ReportWizard.15")); //$NON-NLS-1$
      int openStatus = mb.open();
      if (openStatus == SWT.NO) {
        return;
      }
      updateFields(reportSpec, cols);
    } else if (reportSpec.getFieldCount() == 0) {
      updateFields(reportSpec, cols);
    }
  }

  public void updateFields(ReportSpec reportSpec, Object[] cols) {
    reportSpec.removeAllField();
    IPentahoResultSet resultSet = ((IPentahoResultSet) ConnectionUtility.queryResultSetMap.get(reportSpec.getQuery()));
    try {
      resultSet.beforeFirst();
    } catch (Exception e) {
      e.printStackTrace();
    }
    Object[][] rowHeaders = resultSet.getMetaData().getRowHeaders();
    if (rowHeaders != null) {
      for (int x = 0; x < rowHeaders[0].length - 1; x++) {
        // System.out.println("[" + 0 + "][" + x + "] = " + rowHeaders[0][x]);
        Field f = new Field();
        f.setName(Messages.getString("QueryPanel.51") + (x + 1)); //$NON-NLS-1$
        if (resultSet.getMetaData() instanceof MDXMetaData) {
          f.setName(((MDXMetaData) resultSet.getMetaData()).getColumnName(x));
        }
        f.setDisplayName(f.getName());
        f.setIsDetail(true);
        f.setHorizontalAlignment("left"); //$NON-NLS-1$
        f.setType(Types.VARCHAR);
        f.setIsRowHeader(true);
        f.setUseItemHide(true);
        getTypeMap().put(f.getName(), new Integer(Types.VARCHAR));
        reportSpec.addField(f);
      }
    }
    String columns[] = null;
    if (cols != null) {
      columns = new String[cols.length];
      for (int i = 0; i < cols.length; i++) {
        columns[i] = (String) cols[i];
        Field f = new Field();
        f.setName(columns[i]);
        f.setDisplayName(columns[i]);
        f.setIsDetail(true);
        f.setHorizontalAlignment("left"); //$NON-NLS-1$
        f.setType(Types.VARCHAR);
        f.setIsRowHeader(false);
        getTypeMap().put(f.getName(), new Integer(Types.VARCHAR));
        reportSpec.addField(f);
      }
    }
    Object row[] = resultSet.next();
    if (row == null) {
      ConnectionUtility.queryResultSetMap.clear();
      return;
    }
    for (int i = 0; i < row.length; i++) {
      // System.out.println("row[" + i + "] value = " + row[i] + " == type = " +
      // row[i].getClass().getName());
      if (row[i] instanceof String) {
        getTypeMap().put(columns[i], new Integer(Types.VARCHAR));
        Field f = ReportSpecUtility.getField(reportSpec.getField(), columns[i], true);
        f.setType(Types.VARCHAR);
        f.setHorizontalAlignment("left"); //$NON-NLS-1$
      } else if (row[i] instanceof BigDecimal || row[i] instanceof Integer || row[i] instanceof Double || row[i] instanceof Long) {
        getTypeMap().put(columns[i], new Integer(Types.NUMERIC));
        Field f = ReportSpecUtility.getField(reportSpec.getField(), columns[i], true);
        f.setType(Types.NUMERIC);
        f.setUseItemHide(false);
        f.setHorizontalAlignment("right"); //$NON-NLS-1$
        f.setExpression(defaultNumericFunction); //$NON-NLS-1$
      } else if (row[i] instanceof Date || row[i] instanceof Timestamp) {
        getTypeMap().put(columns[i], new Integer(Types.DATE));
        Field f = ReportSpecUtility.getField(reportSpec.getField(), columns[i], true);
        f.setUseItemHide(false);
        f.setType(Types.DATE);
      }
    }
  }

  public void initWizardPanels() {
    templatePanel.initWizardPanel(reportSpec);
    queryPanel.initWizardPanel(reportSpec);
    mappingPanel.initWizardPanel(reportSpec);
    fieldSetupPanel.initWizardPanel(reportSpec);
    pageSetupPanel.initWizardPanel(reportSpec);
    reportSetupPanel.initWizardPanel(reportSpec);
    groupAndDetailPanel.initWizardPanel(reportSpec);
    wizardManager.update();
  }

  public void doOpen() {
    FileDialog dialog = new FileDialog(parentContainer.getShell(), SWT.OPEN);
    dialog.setFilterNames(new String[] { Messages.getString("ReportWizard.USER_REPORT_SPEC_DOCUMENTS"), Messages.getString("ReportWizard.USER_ALL_XML_FILES"), Messages.getString("ReportWizard.USER_TEMPLATE_DOCUMENTS") }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    dialog.setFilterExtensions(SWTUtility.REPORT_SPEC_FILTERS);
    dialog.setFilterPath(browseFolder);
    String openFileName = dialog.open();
    if (openFileName != null) {
      reportSpecFilePath = openFileName;
      doOpen(reportSpecFilePath, true);
    }
  }

  public void doSave() {
    if (reportSpecStartupFilePath != null && !reportSpecStartupFilePath.equalsIgnoreCase("")) { //$NON-NLS-1$
      reportSpecFilePath = reportSpecStartupFilePath;
    }
    if (reportSpecFilePath != null) {
      if (reportSpec.getTemplateSrc() != null) {
        // handle template save
        try {
          byte data[] = new byte[16384];
          // write reportSpec out
          File tempFile = File.createTempFile("report", ".xreportarc", new File(workingDir + "/temp")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          CastorUtility.getInstance().writeCastorObject(reportSpec, tempFile.getAbsolutePath());
          ZipFile zipFile = new ZipFile(templateZipPath);
          File reportSpecFile = new File(reportSpecFilePath);
          reportSpecFile.delete();
          Enumeration zipEnum = zipFile.entries();
          FileOutputStream dest = new FileOutputStream(reportSpecFilePath);
          ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
          while (zipEnum.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) zipEnum.nextElement();
            if (!zipEntry.getName().equals("report.xreportspec")) { //$NON-NLS-1$
              InputStream is = zipFile.getInputStream(zipEntry);
              int count = 0;
              ZipEntry newEntry = new ZipEntry(zipEntry.getName());
              out.putNextEntry(newEntry);
              while ((count = is.read(data)) != -1) {
                out.write(data, 0, count);
              }
            }
          }
          ZipEntry reportSpecEntry = new ZipEntry("report.xreportspec"); //$NON-NLS-1$
          out.putNextEntry(reportSpecEntry);
          int count = 0;
          FileInputStream fi = new FileInputStream(tempFile);
          BufferedInputStream origin = new BufferedInputStream(fi, 16384);
          while ((count = origin.read(data)) != -1) {
            out.write(data, 0, count);
          }
          origin.close();
          out.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      } else {
        try {
          File tempFile = File.createTempFile("report", ".xreportspec", new File(workingDir + "/temp")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          // create reportSpec if needed
          if (reportSpec == null) {
            reportSpec = createReportSpec();
          }
          // write reportSpec out
          CastorUtility.getInstance().writeCastorObject(reportSpec, tempFile.getAbsolutePath());
          // create 'zip' of reportspec
          FileOutputStream dest = new FileOutputStream(reportSpecFilePath);
          ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
          byte data[] = new byte[16384];
          // get a list of files from current directory
          FileInputStream fi = new FileInputStream(tempFile);
          BufferedInputStream origin = new BufferedInputStream(fi, 16384);
          ZipEntry reportSpecEntry = new ZipEntry("report.xreportspec"); //$NON-NLS-1$
          out.putNextEntry(reportSpecEntry);
          int count = 0;
          while ((count = origin.read(data)) != -1) {
            out.write(data, 0, count);
          }
          origin.close();
          out.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    isSaved = true;
    if (saveItem != null) {
      saveItem.setEnabled(isSaved);
    }
  }

  public void doSaveAs(String filePath) {
    reportSpecFilePath = filePath;
    doSave();
  }

  public void doSaveAs() {
    FileDialog dialog = new FileDialog(parentContainer.getShell(), SWT.SAVE);
    dialog.setFilterNames(new String[] { Messages.getString("ReportWizard.USER_REPORT_SPEC_DOCUMENTS"), Messages.getString("ReportWizard.USER_ALL_XML_FILES") }); //$NON-NLS-1$ //$NON-NLS-2$
    dialog.setFilterExtensions(SWTUtility.REPORT_SPEC_FILTERS); // Windows
    dialog.setFilterPath(browseFolder);
    reportSpecFilePath = dialog.open();
    if (reportSpecFilePath != null && !reportSpecFilePath.endsWith(".xreportspec")) {
      reportSpecFilePath += ".xreportspec";
    }
    doSave();
  }

  public boolean generateReportOutput(ReportSpec spec, String jfreeReportOutputPath, final String outputType, boolean execute) {
    boolean status = true;
    try {
      jfreeReportOutputPath += spec.getReportName() + ".xml"; //$NON-NLS-1$
      String solutionRootPath = PentahoUtility.getSolutionRoot();
      String solution = "samples"; //$NON-NLS-1$
      String path = "reporting"; //$NON-NLS-1$
      String query = spec.getQuery();
      if (spec.getIsMQL()) {
        query = spec.getMqlQuery();
      }
      String jndiSource = null;
      String xQuerySource = null;
      String kettleSource = null;
      if (spec.getReportSpecChoice() != null) {
        jndiSource = spec.getReportSpecChoice().getJndiSource();
        xQuerySource = spec.getReportSpecChoice().getXqueryUrl();
        kettleSource = spec.getReportSpecChoice().getKettleUrl();
      }
      String driver = null;
      String connectInfo = null;
      String username = null;
      String password = null;
      if (jndiSource != null && !"".equals(jndiSource)) { //$NON-NLS-1$
        DataSourceInfo info = dataSourceAdmin.getDataSourceInfo(jndiSource);
        driver = info.getDriver();
        connectInfo = info.getUrl();
        username = info.getUserId();
        password = info.getPassword();
      }
      String actionSequenceTemplateName = "JFree_template.xaction"; //$NON-NLS-1$
      if (xQuerySource != null && !"".equals(xQuerySource)) { //$NON-NLS-1$
        actionSequenceTemplateName = "JFree_XQuery_template.xaction"; //$NON-NLS-1$
      } else if (kettleSource != null && !"".equals(kettleSource)) {
        actionSequenceTemplateName = "JFree_KettleTransformation.xaction"; //$NON-NLS-1$
      } else if (reportSpec.getIsMDX()) {
        actionSequenceTemplateName = "JFree_Mondrian_template.xaction"; //$NON-NLS-1$
      } else if (reportSpec.getIsMQL()) {
        actionSequenceTemplateName = "JFree_MQL_template.xaction"; //$NON-NLS-1$
      }
      String actionSequenceName = spec.getReportName() + ".xaction"; //$NON-NLS-1$
      String actionSeqDocPath = solutionRootPath + "/" + solution + "/" + path + "/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      String actionSeqDocInput = actionSeqDocPath + actionSequenceTemplateName;
      String actionSeqDocOutput = actionSeqDocPath + actionSequenceName;
      if (reportSpec.getWatermark() != null && !StringUtils.isEmpty(reportSpec.getWatermark().getSrc())) {
        String waterMarkSrc = new File(reportSpec.getWatermark().getSrc()).getName();
        String watermarkFileName = actionSeqDocPath + "/" + waterMarkSrc; //$NON-NLS-1$ //$NON-NLS-2$
        ReportSpecUtility.copyFile(reportSpec.getWatermark().getSrc(), watermarkFileName);
      }
      String xactionPropertiesDestination = actionSeqDocPath + reportSpec.getReportName() + ".properties"; //$NON-NLS-1$
      FileOutputStream propertyFileOutputStream = new FileOutputStream(xactionPropertiesDestination);
      String nameProp = "title=" + reportSpec.getReportName() + "\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
      String descProp = "description=" + reportSpec.getReportDesc() + "\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
      propertyFileOutputStream.write(nameProp.getBytes());
      propertyFileOutputStream.write(descProp.getBytes());
      propertyFileOutputStream.flush();
      propertyFileOutputStream.close();
      if (!execute) {
        if (xQuerySource != null && !"".equals(xQuerySource)) { //$NON-NLS-1$
          // copy xQuerySource file to actionSeqDocPath
          File f = new File(xQuerySource);
          xQuerySource = f.getName();
          String xQueryDestFilePath = actionSeqDocPath + "/" + xQuerySource; //$NON-NLS-1$ //$NON-NLS-2$
          ReportSpecUtility.copyFile(f.getAbsolutePath(), xQueryDestFilePath);
          ActionSequenceUtility.getInstance().createXQueryActionSequence(actionSeqDocInput, actionSeqDocOutput, outputType, jfreeReportOutputPath, xQuerySource, query);
        } else if (kettleSource != null && !"".equals(kettleSource)) {
          // copy xQuerySource file to actionSeqDocPath
          File f = new File(kettleSource);
          kettleSource = f.getName();
          String kettleDestFilePath = actionSeqDocPath + "/" + kettleSource; //$NON-NLS-1$ //$NON-NLS-2$
          ReportSpecUtility.copyFile(f.getAbsolutePath(), kettleDestFilePath);
          ActionSequenceUtility.getInstance().createKettleActionSequence(actionSeqDocInput, actionSeqDocOutput, kettleSource, reportSpec.getKettleStep());
        } else {
          String source = "sql"; //$NON-NLS-1$
          if (reportSpec.getIsMDX()) {
            source = "mdx"; //$NON-NLS-1$
          } else if (reportSpec.getIsMQL()) {
            source = "mql"; //$NON-NLS-1$
          }
          String cubeDefDestFilePath = null;
          if (reportSpec.getMondrianCubeDefinitionPath() != null && !"".equals(reportSpec.getMondrianCubeDefinitionPath())) { //$NON-NLS-1$
            // copy mondrian cube definition file to actionSeqDocPath
            File f = new File(reportSpec.getMondrianCubeDefinitionPath());
            String cubeDefPath = f.getName();
            cubeDefDestFilePath = actionSeqDocPath + "/" + cubeDefPath; //$NON-NLS-1$ //$NON-NLS-2$
            ReportSpecUtility.copyFile(f.getAbsolutePath(), cubeDefDestFilePath);
          }
          ActionSequenceUtility.getInstance().createActionSequence(actionSeqDocInput, actionSeqDocOutput, outputType, jfreeReportOutputPath, cubeDefDestFilePath, source, "true", jndiSource, driver, connectInfo, username, password, query); //$NON-NLS-1$ //$NON-NLS-2$
        }
      } else {
        IPentahoResultSet resultSet = null;
        try {
          resultSet = ConnectionUtility.executeQuery(getPentahoConnection(), reportSpec.getQuery(), xQuerySource);
        } catch (Exception e) {
          MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.OK);
          mb.setMessage(e.getMessage());
          mb.setText("Error");
          mb.open();
          return false;
        }
        if (resultSet == null || resultSet.getRowCount() == 0) {
          status = false;
          PentahoSWTMessageBox mb = new PentahoSWTMessageBox("Error", Messages.getString("QueryPanel.40"), 360, 140); //$NON-NLS-1$ //$NON-NLS-2$
          mb.open();
        } else {
          actionSequenceTemplateName = "JFree_ResultSet_template.xaction"; //$NON-NLS-1$
          actionSequenceName = spec.getReportName() + ".xaction"; //$NON-NLS-1$
          actionSeqDocPath = solutionRootPath + "/" + solution + "/" + path + "/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          actionSeqDocInput = actionSeqDocPath + actionSequenceTemplateName;
          actionSeqDocOutput = actionSeqDocPath + actionSequenceName;
          ActionSequenceUtility.getInstance().createResultSetActionSequence(actionSeqDocInput, actionSeqDocOutput, outputType, jfreeReportOutputPath, resultSet); //$NON-NLS-1$ //$NON-NLS-2$
          final File generatedReportFile = ActionSequenceUtility.getInstance().executeActionSequence(solutionRootPath, solution, path, actionSequenceName, outputType, resultSet, this);
          if (generatedReportFile == null) {
            display.asyncExec(new Runnable() {
              public void run() {
                MessageBox messageBox = new MessageBox((Shell) container, SWT.NONE);
                messageBox.setMessage(Messages.getString("ReportWizard.USER_REPORT_GENEREATE_FAILED")); //$NON-NLS-1$
                messageBox.setText(Messages.getString("ReportWizard.USER_ERROR")); //$NON-NLS-1$
                messageBox.open();
              }
            });
          } else {
            display.asyncExec(new Runnable() {
              public void run() {
                try {

                  if (outputType.equals("html")) {
                    IOUtils.write(IOUtils.toString(new FileInputStream(generatedReportFile)).replaceFirst("<link type=\"text/css\" rel=\"stylesheet\" href=\"style",
                        "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + PentahoUtility.getBaseURL() + "style"), new FileOutputStream(generatedReportFile));
                  }

                  String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$

                  boolean isLinux = osName.indexOf("linux") >= 0; //$NON-NLS-1$ //$NON-NLS-2$
                  if (isLinux) {
                    if (outputType.equals("pdf")) { //$NON-NLS-1$
                      try {
                        Runtime.getRuntime().exec("acroread " + generatedReportFile.getAbsolutePath()); //$NON-NLS-1$
                      } catch (Throwable t) {
                        try {
                          Runtime.getRuntime().exec("evince " + generatedReportFile.getAbsolutePath()); //$NON-NLS-1$
                        } catch (Throwable tt) {
                        }
                      }
                    } else if (outputType.equals("html")) {
                      try {
                        Runtime.getRuntime().exec("firefox " + generatedReportFile.getAbsolutePath()); //$NON-NLS-1$
                      } catch (Throwable t) {
                      }
                    } else {
                      try {
                        Runtime.getRuntime().exec("oocalc " + generatedReportFile.getAbsolutePath()); //$NON-NLS-1$
                      } catch (Throwable t) {
                      }
                    }
                    if (detachedPreviewShell != null && !detachedPreviewShell.isDisposed()) {
                      detachedPreviewShell.close();
                      detachedPreviewShell.dispose();
                    }
                  } else if (osName.equals("mac os x")) { //$NON-NLS-1$
                    Runtime.getRuntime().exec("open " + generatedReportFile.getAbsolutePath()); //$NON-NLS-1$
                    if (detachedPreviewShell != null && !detachedPreviewShell.isDisposed()) {
                      detachedPreviewShell.close();
                      detachedPreviewShell.dispose();
                    }
                  } else {
                    System.out.println(Messages.getString("ReportWizard.65") + generatedReportFile.getAbsolutePath()); //$NON-NLS-1$
                    if (detachedPreviewShell != null && !detachedPreviewShell.isDisposed()) {
                      detachedPreviewShell.close();
                      detachedPreviewShell.dispose();
                    }
                    if (outputType.equals("html")) {
                      // try {
                      // Runtime.getRuntime().exec("cmd /c start firefox.exe " + generatedReportFile.getAbsolutePath()); //$NON-NLS-1$
                      // } catch (Throwable t) {
                      Runtime.getRuntime().exec("cmd /c start iexplore.exe " + generatedReportFile.getAbsolutePath()); //$NON-NLS-1$
                      // }
                      // detachPreview();
                      // browser.setUrl(generatedReportFile.getAbsolutePath());
                      // detachedPreviewShell.moveAbove(null);
                    } else {
                      Runtime.getRuntime().exec("cmd /c start " + generatedReportFile.getAbsolutePath()); //$NON-NLS-1$
                    }
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
          }
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
      display.asyncExec(new Runnable() {
        public void run() {
          MessageBox messageBox = new MessageBox(parentContainer.getShell(), SWT.NONE);
          messageBox.setMessage(Messages.getString("ReportWizard.USER_REPORT_GENEREATE_FAILED") + e.toString()); //$NON-NLS-1$
          messageBox.setText(Messages.getString("ReportWizard.USER_ERROR")); //$NON-NLS-1$
          messageBox.open();
        }
      });
    }
    return status;
  }

  public void actionComplete(IRuntimeContext arg0) {
  }

  public static void main(String args[]) {
    InputStream iconRes = ReportWizard.class.getClassLoader().getResourceAsStream("pentaho_icon.ico"); //$NON-NLS-1$
    Image icon = new Image(Display.getDefault(), iconRes);
    SWTUtility.setIcon(icon);

    Shell shell = SWTUtility.createShell(1024, 768, new FillLayout(), Messages.getString("ReportWizard.USER_PRODUCT_TITLE"), SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.RESIZE); //$NON-NLS-1$
    ReportWizard designer = null;
    if (args.length > 0) {
      if (args[0].equalsIgnoreCase("file")) { //$NON-NLS-1$
        designer = new ReportWizard(args[1], args.length == 3 ? args[2] : null, ".", ".", shell, MODE_APPLICATION); //$NON-NLS-1$ //$NON-NLS-2$
      } else if (args[0].equalsIgnoreCase("query")) { //$NON-NLS-1$
        designer = new ReportWizard(null, args[1], ".", ".", shell, MODE_APPLICATION); //$NON-NLS-1$ //$NON-NLS-2$
      }
    } else {
      designer = new ReportWizard(null, null, ".", ".", shell, MODE_APPLICATION); //$NON-NLS-1$ //$NON-NLS-2$
    }
    if (designer != null) {
      while (!shell.isDisposed()) {
        try {
          if (!shell.getDisplay().readAndDispatch())
            shell.getDisplay().sleep();
        } catch (SWTException e) {
          // probably some stupid DND error, ignore it, we're not disposed, so continue
        }
      }
    }
  }

  public ReportSpec createReportSpec() {
    ReportSpec spec = new ReportSpec();
    return spec;
  }

  public boolean nextFired(WizardPanel source) {
    if (ignoreWizardEvents) {
      return true;
    }
    // skip 'mapping' step if not using templates
    if (source == queryPanel && reportSpec.getTemplateSrc() == null) {
      handleTabSelection(currentTabIndex + 2);
    } else {
      handleTabSelection(currentTabIndex + 1);
    }
    // tabFolder.setSelection(tabFolder.getSelectionIndex() + 1);
    return true;
  }

  public void backFired(WizardPanel source) {
    if (ignoreWizardEvents) {
      return;
    }
    if (source == groupAndDetailPanel && reportSpec.getTemplateSrc() == null) {
      handleTabSelection(currentTabIndex - 2);
    } else {
      handleTabSelection(currentTabIndex - 1);
    }
    // tabFolder.setSelection(tabFolder.getSelectionIndex() - 1);
  }

  public boolean previewFired(WizardPanel source) {
    if (ignoreWizardEvents) {
      return true;
    }
    Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
    container.setCursor(cursor);
    // check reportSpec state....
    try {
      System.out.println(Messages.getString("ReportWizard.DEBUG_WRITING", jfreeReportOutputPath + reportSpec.getReportName() + ".xml")); //$NON-NLS-1$//$NON-NLS-2$
      // ReportGenerationUtility.createJFreeReportXML(reportSpec, outStream, 0, 0,
      // false, "", 0, 0); //$NON-NLS-1$
      // outStream = new FileOutputStream(jfreeReportOutputPath +
      // reportSpec.getReportName() + ".xml"); //$NON-NLS-1$
      boolean success = false;
      if (reportSpec.getTemplateSrc() != null) {
        FileOutputStream outStream = new FileOutputStream(jfreeReportOutputPath + reportSpec.getReportName() + "_generated.xml"); //$NON-NLS-1$
        ReportGenerationUtility.createJFreeReportXML(reportSpec, outStream, 0, 0, false, "", 0, 0); //$NON-NLS-1$
        FileOutputStream mergeStream = new FileOutputStream(jfreeReportOutputPath + reportSpec.getReportName() + ".xml"); //$NON-NLS-1$
        File templateFile = new File(reportSpec.getTemplateSrc());
        ReportGenerationUtility.mergeTemplate(templateFile, new File(jfreeReportOutputPath + reportSpec.getReportName() + "_generated.xml"), mergeStream); //$NON-NLS-1$
        success = generateReportOutput(reportSpec, jfreeReportOutputPath, previewType, true);
      } else {
        FileOutputStream outStream = new FileOutputStream(jfreeReportOutputPath + reportSpec.getReportName() + ".xml"); //$NON-NLS-1$
        ReportGenerationUtility.createJFreeReportXML(reportSpec, outStream, 0, 0, false, "", 0, 0); //$NON-NLS-1$
        success = generateReportOutput(reportSpec, jfreeReportOutputPath, previewType, true);
      }
      if (success) {
        handlingTab = true;
        detachPreview();
        handlingTab = false;
      }
      // tabFolder.setSelection(tabPreview);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      cursor = new Cursor(display, SWT.CURSOR_ARROW);
      container.setCursor(cursor);
    }
    return true;
  }

  public void publishFired(WizardPanel source) {
    doPublishReport();
  }

  public void finishFired(WizardPanel source) {
  }

  public boolean cancelFired(WizardPanel source) {
    if (ignoreWizardEvents) {
      return true;
    }
    if (ReportWizard.applicationMode == ReportWizard.MODE_DIALOG) {
      MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.YES | SWT.NO);
      mb.setText("Confirm Cancel");
      mb.setMessage("Are you sure you want to cancel?"); //$NON-NLS-1$
      if (mb.open() == SWT.YES) {
//        parentContainer.dispose();
      } else {
        return false;
      }
    }
    if (ReportWizard.applicationMode == ReportWizard.MODE_APPLICATION) {
      MessageBox mb = new MessageBox(parentContainer.getShell(), SWT.YES | SWT.NO);
      mb.setText(Messages.getString("ReportWizard.21")); //$NON-NLS-1$
      mb.setMessage(Messages.getString("ReportWizard.22")); //$NON-NLS-1$
      if (mb.open() == SWT.YES) {
        doSaveAs();
      }
      container.dispose();
      System.exit(0);
    }
    return true;
  }

  /**
   * @return Returns the pentahoConnection.
   */
  public IPentahoConnection getPentahoConnection() {
    return pentahoConnection;
  }

  /**
   * @param pentahoConnection
   *          The pentahoConnection to set.
   */
  public void setPentahoConnection(IPentahoConnection pentahoConnection) {
    this.pentahoConnection = pentahoConnection;
  }

  public HashMap getTypeMap() {
    return typeMap;
  }

  public void setTypeMap(HashMap typeMap) {
    this.typeMap = typeMap;
  }

  /**
   * @return Returns the reportSpec.
   */
  public ReportSpec getReportSpec() {
    return reportSpec;
  }

  /**
   * @param reportSpec
   *          The reportSpec to set.
   */
  public void setReportSpec(ReportSpec reportSpec) {
    this.reportSpec = reportSpec;
  }

  List dirtyListeners = new LinkedList();

  // only use this method for external use
  public void addDirtyListener(IDirtyListener dirtyListener) {
    dirtyListeners.add(dirtyListener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.jfreereport.wizard.IDirtyListener#dirtyFired(boolean)
   */
  public void dirtyFired(boolean dirty) {
    for (int i = 0; i < dirtyListeners.size(); i++) {
      IDirtyListener dirtyListener = (IDirtyListener) dirtyListeners.get(i);
      dirtyListener.dirtyFired(dirty);
    }
  }

  public String getPreviewType() {
    return previewType;
  }

  public void setPreviewType(String previewType) {
    this.previewType = previewType;
  }
}
