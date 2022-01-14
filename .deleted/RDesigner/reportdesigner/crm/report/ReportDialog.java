/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report;

import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.windows.WindowsLookAndFeel;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.JFreeReportCoreModule;
import org.pentaho.jfreereport.wizard.utility.PentahoUtility;
import org.pentaho.reportdesigner.crm.report.commands.AboutCommand;
import org.pentaho.reportdesigner.crm.report.commands.AddGroupCommand;
import org.pentaho.reportdesigner.crm.report.commands.AddHyperlinkToElementCommand;
import org.pentaho.reportdesigner.crm.report.commands.AddPropertiesDataSetCommand;
import org.pentaho.reportdesigner.crm.report.commands.AddTableModelDataSetCommand;
import org.pentaho.reportdesigner.crm.report.commands.AdjustBandSizeCommand;
import org.pentaho.reportdesigner.crm.report.commands.CloseReportCommand;
import org.pentaho.reportdesigner.crm.report.commands.CopyCommand;
import org.pentaho.reportdesigner.crm.report.commands.CreateReportCommand;
import org.pentaho.reportdesigner.crm.report.commands.CutCommand;
import org.pentaho.reportdesigner.crm.report.commands.DeleteCommand;
import org.pentaho.reportdesigner.crm.report.commands.DeselectAllCommand;
import org.pentaho.reportdesigner.crm.report.commands.DrawSelectionTypeClampCommand;
import org.pentaho.reportdesigner.crm.report.commands.DrawSelectionTypeOutlineCommand;
import org.pentaho.reportdesigner.crm.report.commands.EditSettingsCommand;
import org.pentaho.reportdesigner.crm.report.commands.ExitCommand;
import org.pentaho.reportdesigner.crm.report.commands.GoToDesignViewCommand;
import org.pentaho.reportdesigner.crm.report.commands.GoToPreviewCSVCommand;
import org.pentaho.reportdesigner.crm.report.commands.GoToPreviewHTMLCommand;
import org.pentaho.reportdesigner.crm.report.commands.GoToPreviewPDFCommand;
import org.pentaho.reportdesigner.crm.report.commands.GoToPreviewRTFCommand;
import org.pentaho.reportdesigner.crm.report.commands.GoToPreviewViewCommand;
import org.pentaho.reportdesigner.crm.report.commands.GoToPreviewXLSCommand;
import org.pentaho.reportdesigner.crm.report.commands.GoToToolWindowMessagesCommand;
import org.pentaho.reportdesigner.crm.report.commands.GoToToolWindowPropertyCommand;
import org.pentaho.reportdesigner.crm.report.commands.GoToToolWindowReportTreeCommand;
import org.pentaho.reportdesigner.crm.report.commands.HideElementCommand;
import org.pentaho.reportdesigner.crm.report.commands.ImportReportDefinitionAsXMLCommand;
import org.pentaho.reportdesigner.crm.report.commands.LayerDownCommand;
import org.pentaho.reportdesigner.crm.report.commands.LayerUpCommand;
import org.pentaho.reportdesigner.crm.report.commands.MergeCommand;
import org.pentaho.reportdesigner.crm.report.commands.MoveDownCommand;
import org.pentaho.reportdesigner.crm.report.commands.MoveDownOneCommand;
import org.pentaho.reportdesigner.crm.report.commands.MoveLeftCommand;
import org.pentaho.reportdesigner.crm.report.commands.MoveLeftOneCommand;
import org.pentaho.reportdesigner.crm.report.commands.MoveRightCommand;
import org.pentaho.reportdesigner.crm.report.commands.MoveRightOneCommand;
import org.pentaho.reportdesigner.crm.report.commands.MoveUpCommand;
import org.pentaho.reportdesigner.crm.report.commands.MoveUpOneCommand;
import org.pentaho.reportdesigner.crm.report.commands.NewReportCommand;
import org.pentaho.reportdesigner.crm.report.commands.NewSubReportCommand;
import org.pentaho.reportdesigner.crm.report.commands.OpenReportCommand;
import org.pentaho.reportdesigner.crm.report.commands.PasteCommand;
import org.pentaho.reportdesigner.crm.report.commands.PentahoWizardCommand;
import org.pentaho.reportdesigner.crm.report.commands.PublishToLocationCommand;
import org.pentaho.reportdesigner.crm.report.commands.PublishToServerCommand;
import org.pentaho.reportdesigner.crm.report.commands.RedoCommand;
import org.pentaho.reportdesigner.crm.report.commands.SaveAsReportCommand;
import org.pentaho.reportdesigner.crm.report.commands.SaveReportCommand;
import org.pentaho.reportdesigner.crm.report.commands.SelectAllCommand;
import org.pentaho.reportdesigner.crm.report.commands.SetUnitCommand;
import org.pentaho.reportdesigner.crm.report.commands.UndoCommand;
import org.pentaho.reportdesigner.crm.report.commands.VisitOnlineForumCommand;
import org.pentaho.reportdesigner.crm.report.commands.ZoomCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.AlignBottomCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.AlignCenterCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.AlignLeftCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.AlignMiddleCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.AlignRightCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.AlignTopCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.DistributeBottomCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.DistributeCenterCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.DistributeGapsHorizontalCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.DistributeGapsVerticalCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.DistributeLeftCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.DistributeMiddleCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.DistributeRightCommand;
import org.pentaho.reportdesigner.crm.report.commands.align.DistributeTopCommand;
import org.pentaho.reportdesigner.crm.report.commands.debug.FindTranslationsCommand;
import org.pentaho.reportdesigner.crm.report.commands.debug.PrintUndoInfosCommand;
import org.pentaho.reportdesigner.crm.report.commands.debug.ShowLogCommand;
import org.pentaho.reportdesigner.crm.report.commands.morph.MorphIntoDateFieldCommand;
import org.pentaho.reportdesigner.crm.report.commands.morph.MorphIntoDrawableFieldCommand;
import org.pentaho.reportdesigner.crm.report.commands.morph.MorphIntoImageFieldCommand;
import org.pentaho.reportdesigner.crm.report.commands.morph.MorphIntoImageURLFieldCommand;
import org.pentaho.reportdesigner.crm.report.commands.morph.MorphIntoMessageFieldCommand;
import org.pentaho.reportdesigner.crm.report.commands.morph.MorphIntoNumberFieldCommand;
import org.pentaho.reportdesigner.crm.report.commands.morph.MorphIntoResourceFieldCommand;
import org.pentaho.reportdesigner.crm.report.commands.morph.MorphIntoTextFieldCommand;
import org.pentaho.reportdesigner.crm.report.components.DiscreteTabbedPane;
import org.pentaho.reportdesigner.crm.report.components.InternalWindow;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataSetPlugin;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataSetPluginRegistry;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataSetPluginRegistryListener;
import org.pentaho.reportdesigner.crm.report.datasetplugin.MultiDataSetPlugin;
import org.pentaho.reportdesigner.crm.report.datasetplugin.StaticFactoryDataSetPlugin;
import org.pentaho.reportdesigner.crm.report.datasetplugin.sampledb.SampleDB;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionGadget;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.LabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFactory;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.palette.ReportElementPalette;
import org.pentaho.reportdesigner.crm.report.preview.PreviewComponent;
import org.pentaho.reportdesigner.crm.report.properties.PropertyEditorPanel;
import org.pentaho.reportdesigner.crm.report.settings.ApplicationSettings;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.crm.report.templateplugin.IndentedLayoutPlugin;
import org.pentaho.reportdesigner.crm.report.templateplugin.LayoutPluginRegistry;
import org.pentaho.reportdesigner.crm.report.templateplugin.StructuredLayoutPlugin;
import org.pentaho.reportdesigner.crm.report.tree.ReportTree;
import org.pentaho.reportdesigner.crm.report.util.ReportResizingHelper;
import org.pentaho.reportdesigner.crm.report.util.SplashScreen;
import org.pentaho.reportdesigner.crm.report.util.TranslationUtil;
import org.pentaho.reportdesigner.crm.report.util.VersionHelper;
import org.pentaho.reportdesigner.crm.report.zoom.ZoomModel;
import org.pentaho.reportdesigner.crm.report.zoom.ZoomModelListener;
import org.pentaho.reportdesigner.lib.client.commands.Command;
import org.pentaho.reportdesigner.lib.client.commands.CommandApplicationRoot;
import org.pentaho.reportdesigner.lib.client.commands.CommandConstraint;
import org.pentaho.reportdesigner.lib.client.commands.CommandGroup;
import org.pentaho.reportdesigner.lib.client.commands.CommandManager;
import org.pentaho.reportdesigner.lib.client.commands.CommandMenuBar;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.commands.CommandToolBar;
import org.pentaho.reportdesigner.lib.client.commands.DefaultCommandGroup;
import org.pentaho.reportdesigner.lib.client.commands.SeparatorCommand;
import org.pentaho.reportdesigner.lib.client.components.Category;
import org.pentaho.reportdesigner.lib.client.components.docking.DockingPane;
import org.pentaho.reportdesigner.lib.client.components.docking.ToolWindow;
import org.pentaho.reportdesigner.lib.client.i18n.BundleAlreadyExistsException;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoListener;
import org.pentaho.reportdesigner.lib.client.util.CooltipManager;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.client.util.MathUtils;
import org.pentaho.reportdesigner.lib.client.util.SingleApplicationLock;
import org.pentaho.reportdesigner.lib.client.util.ThrowableHandler;
import org.pentaho.reportdesigner.lib.client.util.UIConstants;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.common.util.LoggerUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 06.10.2005
 * Time: 13:11:57
 */
public class ReportDialog extends JFrame implements PropertyChangeListener, CommandApplicationRoot
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ReportDialog.class.getName());

    public static final long STARTUP_SLEEP = 2000;
    private static final int PLUGIN_SCAN_INTERVAL_MILLIS = 60000;


    @NotNull
    private static VersionHelper loadVersionNumber()
    {
        InputStream resourceAsStream = null;
        VersionHelper version = null;

        try
        {
            resourceAsStream = ReportDialog.class.getResourceAsStream("/version.properties");//NON-NLS

            if (resourceAsStream == null)
            {
                resourceAsStream = ReportDialog.class.getResourceAsStream("/build-res/version.properties");//NON-NLS
            }

            if (resourceAsStream != null)
            {
                version = new VersionHelper(resourceAsStream);
                VersionHelper.setInstance(version);
            }
        }
        catch (IOException e)
        {
            //ok
        }
        finally
        {
            IOUtil.closeStream(resourceAsStream);
        }

        if (version == null)
        {
            version = new VersionHelper();
        }

        return version;
    }


    public static void main(@NotNull String[] args)
    {
        LoggerUtil.initLogger();

        System.setProperty(UIConstants.SUN_AWT_EXCEPTION_HANDLER, ThrowableHandler.class.getName());
        Thread.setDefaultUncaughtExceptionHandler(ThrowableHandler.getInstance());

        loadVersionNumber();

        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null)
        {
            try
            {
                //System.setSecurityManager(new LoggingSecurityManager(securityManager));
                System.setSecurityManager(null);
            }
            catch (Throwable e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.main ", e);
                JOptionPane.showMessageDialog(null,
                                              TranslationManager.getInstance().getTranslation("R", "ReportDialog.DisableSecurityManager.Message"),
                                              TranslationManager.getInstance().getTranslation("R", "ReportDialog.DisableSecurityManager.Title"),
                                              JOptionPane.WARNING_MESSAGE);
            }
        }

        final SplashScreen splashScreen = new SplashScreen(ReportDialog.class.getResource("/res/icons/SplashScreen.png"));//NON-NLS
        splashScreen.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                splashScreen.setVisible(false);
            }
        });

        splashScreen.setVisible(true);

        // hack to get splash screen to appear
        try
        {
            if (EventQueue.isDispatchThread())
            {
                if (splashScreen.getGraphics() != null)
                {
                    splashScreen.paint(splashScreen.getGraphics());
                }
            }
        }
        catch (Exception e)
        {
            // ignore
        }

        Runnable runnable = new Runnable()
        {
            public void run()
            {
                setupLookAndFeel();
                initGUI();

                final Thread t = new Thread()
                {
                    public void run()
                    {
                        preload();
                    }
                };

                t.setDaemon(true);
                t.setPriority(Thread.MIN_PRIORITY);

                t.start();

                splashScreen.dispose();
            }
        };

        EventQueue.invokeLater(runnable);

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                try
                {
                    ReportDesignerWindows.getInstance().saveApplicationSettings();
                    ReportDesignerWindows.getInstance().saveWorkspaceSettings();
                }
                catch (Throwable e)
                {
                    if (LOG.isLoggable(Level.WARNING)) LOG.log(Level.WARNING, "ReportDialog.run shutdownhook e = " + e);
                }


                Collection<String> missingKeysList = TranslationManager.getInstance().getMissingKeysList();
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "MISSING KEYS");
                for (String s : missingKeysList)
                {
                    //noinspection UseOfSystemOutOrSystemErr
                    System.out.println(s);
                }
            }
        });
    }


    private static void initGUI()
    {
        if (!WorkspaceSettings.getInstance().isLoaded())
        {
            File file = new File(System.getProperty(ReportDialogConstants.USER_HOME), ReportDialogConstants.REPORT_DIRECTORY);
            File workspaceSettingsFile = new File(file, ReportDialogConstants.WORKSPACE_SETTINGS_XML);
            WorkspaceSettings.getInstance().loadFromFile(workspaceSettingsFile);
        }

        if (!ApplicationSettings.getInstance().isLoaded())
        {
            File file = new File(System.getProperty(ReportDialogConstants.USER_HOME), ReportDialogConstants.REPORT_DIRECTORY);
            File applicationSettingsFile = new File(file, ReportDialogConstants.APPLICATION_SETTINGS_XML);
            ApplicationSettings.getInstance().loadFromFile(applicationSettingsFile);
        }

        ReportDesignerWindows.getInstance().addReportDesignerWindowsListener(new ReportDesignerWindowsListener()
        {
            public void reportDialogOpened(@NotNull ReportDialog reportDialog)
            {
            }


            public void reportDialogClosed(@NotNull ReportDialog reportDialog)
            {
                if (ReportDesignerWindows.getInstance().getReportDialogs().isEmpty())
                {
                    System.exit(0);
                }
            }
        });

        try
        {
            TranslationManager.getInstance().addBundle("R", ResourceBundle.getBundle("res.Translations", ApplicationSettings.getInstance().getLanguage()));//NON-NLS
            TranslationManager.getInstance().addBundle(TranslationManager.COMMON_BUNDLE_PREFIX, ResourceBundle.getBundle("res.Translations", ApplicationSettings.getInstance().getLanguage()));//NON-NLS

            List<Locale> locales = TranslationUtil.findLocales();
            for (Locale locale : locales)
            {
                TranslationManager.getInstance().addSupportedLocale(locale);
            }
        }
        catch (BundleAlreadyExistsException e)
        {
            if (LOG.isLoggable(Level.SEVERE)) LOG.log(Level.SEVERE, "ReportDialog.run ", e);
        }

        checkSingleInstance();

        //DataSetPluginRegistry.getInstance().registerDataSetPlugin(new SampleDBDataSetPlugin());
        //DataSetPluginRegistry.getInstance().registerDataSetPlugin(new JDBCDataSetPlugin());
        DataSetPluginRegistry.getInstance().registerDataSetPlugin(new MultiDataSetPlugin());
        DataSetPluginRegistry.getInstance().registerDataSetPlugin(new StaticFactoryDataSetPlugin());

        LayoutPluginRegistry.getInstance().registerLayoutPlugin(new StructuredLayoutPlugin());
        LayoutPluginRegistry.getInstance().registerLayoutPlugin(new IndentedLayoutPlugin());

        Report report = ReportFactory.createEmptyReport();
        ReportDesignerWindows.getInstance().createNewReportDialog(report);
    }


    private static void preload()
    {
        try
        {
            PentahoUtility.setSolutionRoot("resources/solutions");//NON-NLS
            PentahoUtility.startup();
        }
        catch (Throwable e)
        {
            if (LOG.isLoggable(Level.WARNING)) LOG.log(Level.WARNING, "ReportDialog.run ", e);
        }

        try
        {
            Toolkit.getDefaultToolkit().getSystemClipboard().getAvailableDataFlavors();
            //almost not disastrous, just for caching
        }
        catch (HeadlessException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.run ", e);
        }

        //noinspection ProhibitedExceptionCaught
        try
        {
            JComponent dummy = new JComponent()
            {
            };
            new TransferHandler("border").exportAsDrag(dummy, new MouseEvent(dummy, 0, 0, 0, 0, 0, 0, false), DnDConstants.ACTION_COPY);//NON-NLS
        }
        catch (NullPointerException e)
        {
            //ok, will happen for sure->ignore
        }

        try
        {
            SampleDB.initSampleDB();

            Thread.sleep(STARTUP_SLEEP);
        }
        catch (Throwable e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.run ", e);
        }

        //noinspection ResultOfObjectAllocationIgnored
        new JFileChooser();
        //noinspection ResultOfObjectAllocationIgnored
        new JTextArea();

        try
        {
            JFreeReportBoot.getInstance().start();
            //noinspection ResultOfObjectAllocationIgnored
            new JFreeReport();//preloads classes
        }
        catch (IndexOutOfBoundsException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.run ", e);
            JFreeReportBoot.getInstance().getEditableConfig().setConfigProperty(JFreeReportCoreModule.NO_PRINTER_AVAILABLE_KEY, Boolean.TRUE.toString());
        }

        //fill introspector cache
        try
        {
            Introspector.getBeanInfo(Report.class);
            Introspector.getBeanInfo(LabelReportElement.class);
            Introspector.getBeanInfo(MessageFieldReportElement.class);
            Introspector.getBeanInfo(StaticImageReportElement.class);
        }
        catch (IntrospectionException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.run ", e);
            //ok, just for caching
        }

        try
        {
            //mark old files for deletion
            File reportDirectory = new File(System.getProperty(ReportDialogConstants.USER_HOME), ReportDialogConstants.REPORT_DIRECTORY);
            File previewDirectory = new File(reportDirectory, ReportDialogConstants.PREVIEW_DIRECTORY);
            File[] oldPreviewFiles = previewDirectory.listFiles();
            for (File oldPreviewFile : oldPreviewFiles)
            {
                if (oldPreviewFile.getName().startsWith(ReportDialogConstants.PREVIEW_FILEPREFIX))
                {
                    oldPreviewFile.deleteOnExit();
                }
            }
        }
        catch (Exception e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.run ", e);
        }
    }


    private static void checkSingleInstance()
    {
        SingleApplicationLock singleApplicationLock = new SingleApplicationLock(System.getProperty(ReportDialogConstants.USER_HOME),
                                                                                ReportDialogConstants.REPORT_DIRECTORY,
                                                                                ReportDialogConstants.LOCK_FILE);
        if (singleApplicationLock.isAppActive())
        {
            JOptionPane.showMessageDialog(null, TranslationManager.getInstance().getTranslation("R", "ReportDialog.JustOneInstance"));
            System.exit(0);
        }
    }


    public static void setupLookAndFeel()
    {
        try
        {
            UIManager.put("swing.boldMetal", Boolean.FALSE);//NON-NLS

            String osName = System.getProperty("os.name");//NON-NLS
            if (osName != null && (osName.toLowerCase().contains("windows"))) //NON-NLS
            {
                UIManager.put("ClassLoader", LookUtils.class.getClassLoader());//NON-NLS
                UIManager.put("jgoodies.popupDropShadowEnabled", Boolean.FALSE);//NON-NLS
                UIManager.setLookAndFeel(new WindowsLookAndFeel());
            }
            else if (osName != null && (osName.toLowerCase().contains("mac")))//NON-NLS
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            else
            {
                UIManager.put("ClassLoader", LookUtils.class.getClassLoader());//NON-NLS
                UIManager.put("jgoodies.popupDropShadowEnabled", Boolean.FALSE);//NON-NLS
                UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
            }

            Toolkit.getDefaultToolkit().setDynamicLayout(true);

            UIManager.put(UIConstants.SPLIT_PANE_BORDER, new EmptyBorder(0, 0, 0, 0));
            UIManager.put(UIConstants.SPLIT_PANE_DIVIDER_BORDER, new EmptyBorder(0, 0, 0, 0));
        }
        catch (Exception e)
        {
            if (LOG.isLoggable(Level.WARNING)) LOG.log(Level.WARNING, "ReportDialog.setupLookAndFeel ", e);
        }
    }


    private boolean modified;

    @NotNull
    private DiscreteTabbedPane tabbedPane;

    @Nullable
    private PropertyEditorPanel propertyEditorPanel;
    @NotNull
    private ArrayList<ReportBorderPanel> bands;
    @Nullable
    private JPanel bandsPanel;
    @Nullable
    private Report report;
    @Nullable
    private ReportElementSelectionModel reportElementSelectionModel;

    @NotNull
    private Undo undo;

    @NotNull
    private CommandToolBar commandToolBar;

    @NotNull
    private CommandMenuBar commandMenuBar;

    @Nullable
    private SideLinealComponent sideLinealComponent;

    @Nullable
    private InspectionGadget inspectionGadget;

    @NotNull
    private StatusBar statusBar;
    @Nullable
    private ZoomModel zoomModel;

    @Nullable
    private DockingPane dockingPane;

    @NotNull
    private ArrayList<InternalWindow> internalWindows;

    @Nullable
    private File currentReportFile;
    @NotNull
    private PreviewComponent previewComponent;

    @NotNull
    private ReportElementPalette reportElementPalette;

    @NotNull
    private ToolWindow paletteToolWindow;
    @NotNull
    private ToolWindow reportTreeToolWindow;
    @NotNull
    private ToolWindow propertyToolWindow;
    @NotNull
    private ToolWindow inspectionsToolWindow;

    private boolean debugEnabled;

    @NotNull
    private String windowTitle;


    @NotNull
    private Long applicationID;

    private boolean swtLoaded;

    public enum DrawSelectionType
    {
        @NotNull OUTLINE,
        @NotNull CLAMP,
    }

    @NotNull
    private DrawSelectionType drawSelectionType = DrawSelectionType.OUTLINE;


    protected ReportDialog(@NotNull Long applicationID, @Nullable final Report report)
    {
        this.applicationID = applicationID;

        VersionHelper version = VersionHelper.getInstance();

        debugEnabled = false;
        undo = new Undo();

        statusBar = new StatusBar();
        UncaughtExcpetionsModel.getInstance().addUncaughtExceptionModelListener(statusBar);//as early as possible

        windowTitle = TranslationManager.getInstance().getTranslation("R", "ReportDialog.Title",
                                                                      version.getMajorRelease(), version.getMinorRelease(), version.getMilestoneRelease());
        setTitle(windowTitle);
        setIconImage(IconLoader.getInstance().getReportFrameIcon().getImage());

        tabbedPane = new DiscreteTabbedPane();
        bands = new ArrayList<ReportBorderPanel>();
        previewComponent = new PreviewComponent();
        reportElementPalette = new ReportElementPalette();
        paletteToolWindow = new ToolWindow(new Category<JLabel>("", IconLoader.getInstance().getAboutIcon(), IconLoader.getInstance().getAboutIcon(), "", null));
        reportTreeToolWindow = new ToolWindow(new Category<JLabel>("", IconLoader.getInstance().getAboutIcon(), IconLoader.getInstance().getAboutIcon(), "", null));
        propertyToolWindow = new ToolWindow(new Category<JLabel>("", IconLoader.getInstance().getAboutIcon(), IconLoader.getInstance().getAboutIcon(), "", null));
        inspectionsToolWindow = new ToolWindow(new Category<JLabel>("", IconLoader.getInstance().getAboutIcon(), IconLoader.getInstance().getAboutIcon(), "", null));

        initPlugins();

        //restore dialogbounds
        if (!getWorkspaceSettings().restoreFrameBounds(this, "ReportDialog"))
        {
            setSize(1000, 800);
            invalidate();
            validate();
            repaint();
        }

        internalWindows = new ArrayList<InternalWindow>();

        setReport(report);

        getContentPane().add(statusBar, BorderLayout.SOUTH);

        initCommandManagerStuff();

        //KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(new PropertyChangeListener()
        //{
        //    public void propertyChange(@NotNull PropertyChangeEvent evt)
        //    {
        //        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.propertyChange evt = " + evt);
        //        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.propertyChange evt.getOldValue() = " + evt.getOldValue());
        //        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.propertyChange evt.getNewValue() = " + evt.getNewValue());
        //    }
        //});
    }


    private void installReport(@NotNull Report report, boolean fromSWT)
    {
        undo = new Undo();

        report.setUndo(undo);

        reportElementSelectionModel = new ReportElementSelectionModel(this, report);
        final ReportElementSelectionModel resm = reportElementSelectionModel;

        ReportTree reportTree = new ReportTree(this, reportElementSelectionModel);
        JScrollPane reportTreeComponent = new JScrollPane(reportTree);

        // due to a bug in SWT + Linux, the CooltipManager will cause a seg fault and crash the process
        // so we will conditionally use the CooltipManager..
        if (!isSwtLoaded())
        {
            setSwtLoaded(fromSWT);
        }
        boolean safeToLoadCooltipManager = false;
        if (!isSwtLoaded())
        {
            safeToLoadCooltipManager = true;
        }
        else if (isSwtLoaded())
        {
            String osName = System.getProperty("os.name");//NON-NLS
            if (osName != null && !osName.toLowerCase().contains("linux"))//NON-NLS
            {
                safeToLoadCooltipManager = true;
            }
        }
        if (safeToLoadCooltipManager)
        {
            CooltipManager cooltipManager = new CooltipManager();
            cooltipManager.registerComponent(reportTree);
        }
        this.report = report;

        bands = new ArrayList<ReportBorderPanel>();
        bandsPanel = new JPanel(new BorderLayout());
        JPanel bp = bandsPanel;
        sideLinealComponent = new SideLinealComponent(this, report.getHorizontalLinealModel());
        bp.add(sideLinealComponent, BorderLayout.CENTER);

        initApplicationSettings();

        rebuildBandsPanel(report, resm);

        tabbedPane = new DiscreteTabbedPane();
        tabbedPane.addTab(TranslationManager.getInstance().getTranslation("R", "ReportDialog.Tab.Design"), bp);

        previewComponent = new PreviewComponent();
        if (!(report instanceof SubReport))
        {
            final JToggleButton previewButton = tabbedPane.addTab(TranslationManager.getInstance().getTranslation("R", "ReportDialog.Tab.Preview"), previewComponent);
            previewButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    if (previewButton.isSelected())
                    {
                        previewComponent.updatePreview(ReportDialog.this);
                    }
                }
            });
        }

        showDesignView();

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tabbedPane, BorderLayout.CENTER);

        createZoomModel();

        propertyEditorPanel = new PropertyEditorPanel(this, resm);

        final PropertyEditorPanel pep = propertyEditorPanel;

        resm.addBandElementModelListener(new BandElementModelAdapter()
        {
            public void selectionChanged()
            {
                ArrayList<ReportElement> selectedElementInfos = resm.getSelectedElementInfos();
                pep.setBeans(selectedElementInfos.toArray(new ReportElement[selectedElementInfos.size()]));
            }
        });

        InternalWindow propertyEditorPanelInternalWindow = new InternalWindow(new Category<JComponent>("PropertyEditorPanel",
                                                                                                       IconLoader.getInstance().getPropertyTableIcon(),
                                                                                                       IconLoader.getInstance().getPropertyTableIcon(),
                                                                                                       TranslationManager.getInstance().getTranslation("R", "PropertyEditorPanel.Title"),
                                                                                                       null));
        internalWindows.add(propertyEditorPanelInternalWindow);
        propertyEditorPanelInternalWindow.add(propertyEditorPanel, BorderLayout.CENTER);


        reportTreeComponent.setBorder(BorderFactory.createEmptyBorder());
        InternalWindow reportTreeInternalWindow = new InternalWindow(new Category<JComponent>("PropertyTree",
                                                                                              IconLoader.getInstance().getReportTreeIcon(),
                                                                                              IconLoader.getInstance().getReportTreeIcon(),
                                                                                              TranslationManager.getInstance().getTranslation("R", "PropertyTree.Title"),
                                                                                              null));
        internalWindows.add(reportTreeInternalWindow);
        reportTreeInternalWindow.add(reportTreeComponent, BorderLayout.CENTER);


        reportElementPalette = new ReportElementPalette();
        InternalWindow reportElementPaletteInternalWindow = new InternalWindow(new Category<JComponent>("ReportElementPalette",
                                                                                                        IconLoader.getInstance().getPaletteIcon(),
                                                                                                        IconLoader.getInstance().getPaletteIcon(),
                                                                                                        TranslationManager.getInstance().getTranslation("R", "ReportElementPalette.Title"),
                                                                                                        null));
        internalWindows.add(reportElementPaletteInternalWindow);
        JScrollPane paletteScrollPane = new JScrollPane(reportElementPalette);
        //paletteScrollPane.getViewport().setBackground(Color.WHITE);

        paletteScrollPane.setBorder(BorderFactory.createEmptyBorder());
        reportElementPaletteInternalWindow.add(paletteScrollPane, BorderLayout.CENTER);

        inspectionGadget = new InspectionGadget(this);
        InternalWindow inspectionGadgetInternalWindow = new InternalWindow(new Category<JComponent>("InspectionGadget",
                                                                                                    IconLoader.getInstance().getMessagesIcon(),
                                                                                                    IconLoader.getInstance().getMessagesIcon(),
                                                                                                    TranslationManager.getInstance().getTranslation("R", "InspectionGadget.Title"),
                                                                                                    null));
        internalWindows.add(inspectionGadgetInternalWindow);
        inspectionGadgetInternalWindow.add(inspectionGadget, BorderLayout.CENTER);

        undo.addUndoListener(new UndoListener()
        {
            public void stateChanged()
            {
                CommandManager.dataProviderChanged();
            }
        });

        dockingPane = new DockingPane();
        DockingPane dockingPane = this.dockingPane;

        dockingPane.setCenterComponent(centerPanel);

        paletteToolWindow = new ToolWindow(new Category<InternalWindow>("Palette",
                                                                        IconLoader.getInstance().getPaletteIcon(),
                                                                        IconLoader.getInstance().getPaletteIcon(),
                                                                        TranslationManager.getInstance().getTranslation("R", "Palette.Title"),
                                                                        reportElementPaletteInternalWindow));
        dockingPane.addToolWindow(paletteToolWindow);
        paletteToolWindow.setAlignment(ToolWindow.Alignment.LEFT);
        paletteToolWindow.setSizeState(ToolWindow.SizeState.NORMAL);


        reportTreeToolWindow = new ToolWindow(new Category<InternalWindow>("ReportTree",
                                                                           IconLoader.getInstance().getReportTreeIcon(),
                                                                           IconLoader.getInstance().getReportTreeIcon(),
                                                                           TranslationManager.getInstance().getTranslation("R", "ReportTree.Title"),
                                                                           reportTreeInternalWindow));
        dockingPane.addToolWindow(reportTreeToolWindow);
        reportTreeToolWindow.setAlignment(ToolWindow.Alignment.RIGHT);
        reportTreeToolWindow.setSizeState(ToolWindow.SizeState.NORMAL);


        propertyToolWindow = new ToolWindow(new Category<InternalWindow>("Property",
                                                                         IconLoader.getInstance().getPropertyTableIcon(),
                                                                         IconLoader.getInstance().getPropertyTableIcon(),
                                                                         TranslationManager.getInstance().getTranslation("R", "Property.Title"),
                                                                         propertyEditorPanelInternalWindow));
        dockingPane.addToolWindow(propertyToolWindow);
        propertyToolWindow.setAlignment(ToolWindow.Alignment.RIGHT);
        propertyToolWindow.setSizeState(ToolWindow.SizeState.NORMAL);


        inspectionsToolWindow = new ToolWindow(new Category<InternalWindow>("Messages",
                                                                            IconLoader.getInstance().getMessagesIcon(),
                                                                            IconLoader.getInstance().getMessagesIcon(),
                                                                            TranslationManager.getInstance().getTranslation("R", "Messages.Title"),
                                                                            inspectionGadgetInternalWindow));
        dockingPane.addToolWindow(inspectionsToolWindow);
        inspectionsToolWindow.setAlignment(ToolWindow.Alignment.BOTTOM);
        inspectionsToolWindow.setSizeState(ToolWindow.SizeState.NORMAL);


        paletteToolWindow.setSize(new Dimension(140, 140));
        reportTreeToolWindow.setSize(new Dimension(200, 200));
        propertyToolWindow.setSize(new Dimension(300, 200));
        inspectionsToolWindow.setSize(new Dimension(300, 100));

        getContentPane().add(dockingPane, BorderLayout.CENTER);

        attachPropertyChangeListener(report);

        setModified(false);
    }


    private void uninstallReport()
    {
        Report report = this.report;
        if (report != null)
        {
            if (dockingPane != null)
            {
                getContentPane().remove(dockingPane);//remove the old dockingpane
                dockingPane = null;
            }

            if (inspectionGadget != null)
            {
                inspectionGadget.dispose();
                inspectionGadget = null;
            }

            detachPropertyChangeListener(report);

            report.setUndo(null);

            this.report = null;

            for (InternalWindow internalWindow : internalWindows)
            {
                internalWindow.dispose();
            }
            internalWindows.clear();

            reportElementSelectionModel = null;
            bands.clear();
            bandsPanel = null;
            sideLinealComponent = null;
            propertyEditorPanel = null;
            undo = new Undo();

            zoomModel = null;

            requestFocusInWindow();

            invalidate();
            validate();
            repaint();
        }

        setModified(false);
    }


    public void setReport(@Nullable Report report)
    {
        setReport(report, false);
    }


    public void setReport(@Nullable Report report, boolean fromSWT)
    {
        uninstallReport();
        if (report != null)
        {
            installReport(report, fromSWT);
        }
    }


    @Nullable
    public File getCurrentReportFile()
    {
        return currentReportFile;
    }


    public void setCurrentReportFile(@Nullable File currentReportFile)
    {
        this.currentReportFile = currentReportFile;
        updateTitle();
    }


    public void showPreviewView()
    {
        tabbedPane.showTab(TranslationManager.getInstance().getTranslation("R", "ReportDialog.Tab.Preview"));
    }


    public void showDesignView()
    {
        tabbedPane.showTab(TranslationManager.getInstance().getTranslation("R", "ReportDialog.Tab.Design"));
        if (sideLinealComponent != null)
        {
            sideLinealComponent.getBandsComponent().requestFocusInWindow();
        }
    }


    @Nullable
    public ZoomModel getZoomModel()
    {
        return zoomModel;
    }


    @Nullable
    public ReportElementPalette getReportElementPalette()
    {
        return reportElementPalette;
    }


    @Nullable
    public InspectionGadget getInspectionGadget()
    {
        return inspectionGadget;
    }


    @Nullable
    public PropertyEditorPanel getPropertyEditorPanel()
    {
        return propertyEditorPanel;
    }


    private void createZoomModel()
    {
        ZoomModel zoomModel = new ZoomModel();
        this.zoomModel = zoomModel;

        zoomModel.addZoomModelListener(new ZoomModelListener()
        {
            public void zoomFactorChanged(int oldFactor, int newFactor)
            {
                SideLinealComponent slc = sideLinealComponent;
                if (slc != null)
                {
                    for (ReportBorderPanel reportBorderPanel : bands)
                    {
                        reportBorderPanel.getReportPanel().setScaleFactor(newFactor / 1000.);
                        slc.setScaleFactor(newFactor);

                        reportBorderPanel.revalidate();
                        reportBorderPanel.repaint();
                    }

                    slc.revalidate();
                    slc.repaint();
                }

                JPanel bp = bandsPanel;
                if (bp != null)
                {
                    bp.revalidate();
                    bp.repaint();
                }

                if (previewComponent.isShowing())
                {
                    previewComponent.updatePreview(ReportDialog.this);
                }
            }
        });

        zoomModel.setZoomFactor(1000);
    }


    @NotNull
    public WorkspaceSettings getWorkspaceSettings()
    {
        return WorkspaceSettings.getInstance();
    }


    @NotNull
    public ApplicationSettings getApplicationSettings()
    {
        return ApplicationSettings.getInstance();
    }


    @NotNull
    public DrawSelectionType getDrawSelectionType()
    {
        return drawSelectionType;
    }


    public void setDrawSelectionType(@NotNull DrawSelectionType drawSelectionType)
    {
        this.drawSelectionType = drawSelectionType;
        if (sideLinealComponent != null)
        {
            sideLinealComponent.repaint();
        }
    }


    @Nullable
    public ArrayList<ReportBorderPanel> getBands()
    {
        return new ArrayList<ReportBorderPanel>(bands);
    }


    @Nullable
    public Report getReport()
    {
        return report;
    }


    @Nullable
    public ReportElementSelectionModel getReportElementModel()
    {
        return reportElementSelectionModel;
    }


    @NotNull
    public Undo getUndo()
    {
        return undo;
    }


    private void rebuildBandsPanel(@NotNull Report report, @NotNull ReportElementSelectionModel reportElementSelectionModel)
    {
        ArrayList<ReportBorderPanel> oldReportBorderPanels = new ArrayList<ReportBorderPanel>(bands);
        bands.clear();

        ReportBorderPanel reportHeaderBorderPanel = getReportBorderPanel(oldReportBorderPanels, report, reportElementSelectionModel, report.getReportHeaderBand(), false, false);
        ReportBorderPanel pageHeaderBorderPanel = getReportBorderPanel(oldReportBorderPanels, report, reportElementSelectionModel, report.getPageHeaderBand(), true, false);
        ReportBorderPanel itemBandBorderPanel = getReportBorderPanel(oldReportBorderPanels, report, reportElementSelectionModel, report.getItemBand(), false, false);
        ReportBorderPanel pageFooterBorderPanel = getReportBorderPanel(oldReportBorderPanels, report, reportElementSelectionModel, report.getPageFooterBand(), false, true);
        ReportBorderPanel reportFooterBorderPanel = getReportBorderPanel(oldReportBorderPanels, report, reportElementSelectionModel, report.getReportFooterBand(), false, false);
        ReportBorderPanel watermarkBorderPanel = getReportBorderPanel(oldReportBorderPanels, report, reportElementSelectionModel, report.getWatermarkBand(), true, true);
        ReportBorderPanel noDataBorderPanel = getReportBorderPanel(oldReportBorderPanels, report, reportElementSelectionModel, report.getNoDataBand(), false, false);

        ArrayList<ReportBorderPanel> reportBorderPanels = new ArrayList<ReportBorderPanel>();
        reportBorderPanels.add(pageHeaderBorderPanel);
        bands.add(pageHeaderBorderPanel);
        reportBorderPanels.add(reportHeaderBorderPanel);
        bands.add(reportHeaderBorderPanel);

        if (!report.getReportGroups().getChildren().isEmpty())
        {
            addGroupHeaders(oldReportBorderPanels, report, reportElementSelectionModel, reportBorderPanels, (ReportGroup) report.getReportGroups().getChildren().get(0), itemBandBorderPanel);
        }
        else
        {
            reportBorderPanels.add(itemBandBorderPanel);
            bands.add(itemBandBorderPanel);
        }

        reportBorderPanels.add(reportFooterBorderPanel);
        bands.add(reportFooterBorderPanel);
        reportBorderPanels.add(pageFooterBorderPanel);
        bands.add(pageFooterBorderPanel);
        reportBorderPanels.add(watermarkBorderPanel);
        bands.add(watermarkBorderPanel);
        reportBorderPanels.add(noDataBorderPanel);
        bands.add(noDataBorderPanel);

        ArrayList<ReportBorderPanel> activePanels = new ArrayList<ReportBorderPanel>();
        for (ReportBorderPanel reportBorderPanel : bands)
        {
            if (reportBorderPanel.getReportPanel().getBandToplevelReportElement().isShowInLayoutGUI())
            {
                activePanels.add(reportBorderPanel);
            }
        }

        JPanel bp = bandsPanel;
        if (sideLinealComponent != null && bp != null)
        {
            sideLinealComponent.updateReportBorderPanels(activePanels);
            bp.revalidate();
            bp.repaint();
        }

    }


    @Nullable
    public SideLinealComponent getSideLinealComponent()
    {
        return sideLinealComponent;
    }


    @NotNull
    private ReportBorderPanel getReportBorderPanel(@NotNull ArrayList<ReportBorderPanel> oldReportBorderPanels,
                                                   @NotNull Report report,
                                                   @NotNull ReportElementSelectionModel reportElementSelectionModel,
                                                   @NotNull BandToplevelReportElement reportHeaderBand,
                                                   boolean showTopBorder,
                                                   boolean showBottomBorder)
    {
        ReportBorderPanel reportBorderPanel = containsReportBorderPanel(oldReportBorderPanels, reportHeaderBand);
        if (reportBorderPanel == null)
        {
            ReportPanel reportPanel = new ReportPanel(this, report, reportHeaderBand, reportElementSelectionModel);
            reportBorderPanel = new ReportBorderPanel(reportPanel, showTopBorder, showBottomBorder);
        }
        return reportBorderPanel;
    }


    @Nullable
    private ReportBorderPanel containsReportBorderPanel(@NotNull ArrayList<ReportBorderPanel> reportBorderPanels,
                                                        @NotNull BandToplevelReportElement toplevelReportElement)
    {
        for (ReportBorderPanel borderPanel : reportBorderPanels)
        {
            //noinspection ObjectEquality
            if (borderPanel.getReportPanel().getBandToplevelReportElement() == toplevelReportElement)
            {
                return borderPanel;
            }
        }

        return null;
    }


    private void addGroupHeaders(@NotNull ArrayList<ReportBorderPanel> oldReportBorderPanels,
                                 @NotNull Report report,
                                 @NotNull ReportElementSelectionModel reportElementSelectionModel,
                                 @NotNull ArrayList<ReportBorderPanel> reportBorderPanels,
                                 @NotNull ReportGroup reportGroup,
                                 @NotNull ReportBorderPanel itemBandPanel)
    {
        ReportBorderPanel reportBorderPanel = getReportBorderPanel(oldReportBorderPanels,
                                                                   report,
                                                                   reportElementSelectionModel,
                                                                   reportGroup.getGroupHeader(),
                                                                   reportGroup.getGroupHeader().isPageBreakBefore(),
                                                                   reportGroup.getGroupHeader().isPageBreakAfter());
        reportBorderPanels.add(reportBorderPanel);
        bands.add(reportBorderPanel);

        if (reportGroup.getChildren().size() == 3)
        {
            addGroupHeaders(oldReportBorderPanels, report, reportElementSelectionModel, reportBorderPanels, (ReportGroup) reportGroup.getChildren().get(1), itemBandPanel);
        }
        if (reportGroup.getChildren().size() == 2)
        {
            reportBorderPanels.add(itemBandPanel);
            bands.add(itemBandPanel);
        }

        ReportBorderPanel footerBorderPanel = getReportBorderPanel(oldReportBorderPanels, report, reportElementSelectionModel, reportGroup.getGroupFooter(), reportGroup.getGroupFooter().isPageBreakBefore(), reportGroup.getGroupFooter().isPageBreakAfter());
        reportBorderPanels.add(footerBorderPanel);
        bands.add(footerBorderPanel);
    }


    private void attachPropertyChangeListener(@NotNull ReportElement reportElement)
    {
        reportElement.addPropertyChangeListener(this);

        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement element : children)
        {
            attachPropertyChangeListener(element);
        }
    }


    private void detachPropertyChangeListener(@NotNull ReportElement reportElement)
    {
        reportElement.removePropertyChangeListener(this);

        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement element : children)
        {
            detachPropertyChangeListener(element);
        }
    }


    public boolean isModified()
    {
        return modified;
    }


    public void setModified(boolean modified)
    {
        this.modified = modified;
        updateTitle();
    }


    private void updateTitle()
    {
        String file = "";
        String fileName = "";

        File currentReportFile = this.currentReportFile;
        if (currentReportFile != null)
        {
            try
            {
                file = currentReportFile.getCanonicalPath();
                fileName = currentReportFile.getName();
                if (file.length() > 50)
                {
                    file = file.substring(file.length() - 50);
                }
                if (fileName.length() > 50)
                {
                    fileName = fileName.substring(fileName.length() - 50);
                }
            }
            catch (IOException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
        }
        setTitle(fileName + (isModified() ? "*" : "") + (file.length() > 0 ? " - " : "") + file + (file.length() > 0 ? " - " : "") + windowTitle);
    }


    public void propertyChange(@NotNull PropertyChangeEvent evt)
    {
        setModified(true);

        if (evt.getSource() instanceof ReportElement)
        {
            ReportElement reportElement = (ReportElement) evt.getSource();

            if (PropertyKeys.CHILD_ADDED.equals(evt.getPropertyName()) || PropertyKeys.CHILD_REMOVED.equals(evt.getPropertyName()))
            {
                detachPropertyChangeListener(reportElement);
                attachPropertyChangeListener(reportElement);

                if (evt.getNewValue() instanceof BandToplevelReportElement)
                {
                    if (report != null && reportElementSelectionModel != null)
                    {
                        rebuildBandsPanel(report, reportElementSelectionModel);
                    }
                }
                else if (evt.getNewValue() instanceof ReportGroup)
                {
                    if (report != null && reportElementSelectionModel != null)
                    {
                        rebuildBandsPanel(report, reportElementSelectionModel);
                    }
                }
            }
            else if (PropertyKeys.SHOW_IN_LAYOUT_GUI.equals(evt.getPropertyName()))
            {
                if (report != null && reportElementSelectionModel != null)
                {
                    rebuildBandsPanel(report, reportElementSelectionModel);
                }
            }
            else if (PropertyKeys.PAGE_DEFINITION.equals(evt.getPropertyName()))
            {
                PageDefinition oldPageDefinition = (PageDefinition) evt.getOldValue();
                PageDefinition newPageDefinition = (PageDefinition) evt.getNewValue();

                if (!undo.isInProgress())
                {
                    adjustPageSize(newPageDefinition, oldPageDefinition);
                }

                if (report != null && reportElementSelectionModel != null)
                {
                    rebuildBandsPanel(report, reportElementSelectionModel);
                }
            }
        }
    }


    private void adjustPageSize(@NotNull PageDefinition newPageDefinition,
                                @NotNull PageDefinition oldPageDefinition)
    {
        double newWidth = newPageDefinition.getInnerPageSize().getWidth();
        double oldWidth = oldPageDefinition.getInnerPageSize().getWidth();

        if (MathUtils.approxEquals(newWidth, oldWidth))
        {
            //dimensions ~same as before -> nothing needs to be done
        }
        else
        {
            Report report = this.report;
            if (report != null)
            {
                if (newWidth < oldWidth)
                {
                    ReportResizingHelper.adjustWidthProportional(oldWidth, newWidth, report);
                    if (reportElementSelectionModel != null)
                    {
                        reportElementSelectionModel.refresh();
                    }
                }
                else //if(newWidth>oldWidth)
                {
                    //options: 1. enlarge by %, 2. keep the same
                    int option = JOptionPane.showOptionDialog(this,
                                                              TranslationManager.getInstance().getTranslation("R", "ResizeReportOptionPane.Message"),
                                                              TranslationManager.getInstance().getTranslation("R", "ResizeReportOptionPane.Title"),
                                                              JOptionPane.OK_CANCEL_OPTION,
                                                              JOptionPane.QUESTION_MESSAGE,
                                                              null,
                                                              new String[]{TranslationManager.getInstance().getTranslation("R", "ResizeReportOptionPane.OptionResizeProportional"),
                                                                           TranslationManager.getInstance().getTranslation("R", "ResizeReportOptionPane.OptionAlignLeft"),
                                                                           TranslationManager.getInstance().getTranslation("R", "ResizeReportOptionPane.OptionAlignCenter"),
                                                                           TranslationManager.getInstance().getTranslation("R", "ResizeReportOptionPane.OptionAlignRight")},
                                                              TranslationManager.getInstance().getTranslation("R", "ResizeReportOptionPane.OptionResizeProportional"));
                    if (option != JOptionPane.CLOSED_OPTION)
                    {
                        if (option == 0)
                        {
                            ReportResizingHelper.adjustWidthProportional(oldWidth, newWidth, report);
                        }
                        else if (option == 1)
                        {
                            ReportResizingHelper.increaseWidthAlign(oldWidth, newWidth, report, ReportResizingHelper.Alignment.LEFT);
                        }
                        else if (option == 2)
                        {
                            ReportResizingHelper.increaseWidthAlign(oldWidth, newWidth, report, ReportResizingHelper.Alignment.CENTER);
                        }
                        else
                        {
                            ReportResizingHelper.increaseWidthAlign(oldWidth, newWidth, report, ReportResizingHelper.Alignment.RIGHT);
                        }
                    }

                    if (reportElementSelectionModel != null)
                    {
                        reportElementSelectionModel.refresh();
                    }
                }
            }
        }
    }


    public boolean close()
    {
        if (isModified())
        {
            toFront();

            int option = JOptionPane.showConfirmDialog(this,
                                                       TranslationManager.getInstance().getTranslation("R", "ReportModifiedCloseWarning.Message"),
                                                       TranslationManager.getInstance().getTranslation("R", "ReportModifiedCloseWarning.Title"),
                                                       JOptionPane.YES_NO_CANCEL_OPTION,
                                                       JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION)
            {
                boolean saved = SaveReportCommand.save(false, this, true) != null;
                if (saved)
                {
                    getWorkspaceSettings().storeFrameBounds(this, "ReportDialog");
                    dispose();
                }
                return saved;
            }
            else if (option == JOptionPane.NO_OPTION)
            {
                getWorkspaceSettings().storeFrameBounds(this, "ReportDialog");
                dispose();
                return true;
            }
            else
            {
                return false;
            }
        }

        getWorkspaceSettings().storeFrameBounds(this, "ReportDialog");

        dispose();

        return true;
    }


    private void initPlugins()
    {
        //create the empty plugin folder if necessary
        File file = new File(System.getProperty(ReportDialogConstants.USER_HOME), ReportDialogConstants.REPORT_DIRECTORY);
        File pluginFolder = new File(file, ReportDialogConstants.PLUGIN_FOLDER);
        pluginFolder.mkdir();

        if (PluginWatchDog.getInstance().getPluginWatchers().isEmpty())
        {
            PluginWatchDog.getInstance().setWatchFolder(this, pluginFolder, PLUGIN_SCAN_INTERVAL_MILLIS);
        }
    }


    private void initApplicationSettings()
    {
        ApplicationSettings.getInstance().addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                SideLinealComponent slc = sideLinealComponent;
                if (slc != null)
                {
                    slc.revalidate();
                    slc.repaint();
                }
            }
        });
    }


    private void initCommandManagerStuff()
    {
        //just to be verbose
        CommandSettings.getInstance().setCategoriesIconKey(CommandSettings.SIZE_16);
        CommandSettings.getInstance().setMenuIconKey(CommandSettings.SIZE_16);
        CommandSettings.getInstance().setPopupmenuIconKey(CommandSettings.SIZE_16);
        CommandSettings.getInstance().setToolbarIconKey(CommandSettings.SIZE_16);

        DefaultCommandGroup applicationCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "ApplicationCommandGroup.Text"), TranslationManager.getInstance().getTranslation("R", "ApplicationCommandGroup.Description"), "ApplicationCommandGroup", true);
        applicationCommandGroup.getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "ApplicationCommandGroup.Text"));
        applicationCommandGroup.getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "ApplicationCommandGroup.Text"));

        CommandManager.registerCommandGroup(this, "ReportDialog.Menubar.Application", applicationCommandGroup);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new PentahoWizardCommand(), CommandConstraint.LAST);
        //CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new ReportGenerateWizardCommand(), CommandConstraint.LAST);
//        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new NewReportCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new NewSubReportCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new OpenReportCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new MergeCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new ImportReportDefinitionAsXMLCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new CloseReportCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new SaveReportCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new SaveAsReportCommand(), CommandConstraint.LAST);
        //CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new CloseReportCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new CreateReportCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new PublishToLocationCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new PublishToServerCommand(), CommandConstraint.LAST);
        //CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new CreateReportOnServerCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new EditSettingsCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Application", new ExitCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar", applicationCommandGroup, CommandConstraint.LAST);

        DefaultCommandGroup editCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "EditCommandGroup.Text"), TranslationManager.getInstance().getTranslation("R", "EditCommandGroup.Description"), "EditCommandGroup", true);
        editCommandGroup.getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "EditCommandGroup.Text"));
        editCommandGroup.getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "EditCommandGroup.Text"));

        CommandManager.registerCommandGroup(this, "ReportDialog.Menubar.Edit", editCommandGroup);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new UndoCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new RedoCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new CutCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new CopyCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new PasteCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new DeleteCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new SeparatorCommand(), CommandConstraint.LAST);
        DefaultCommandGroup editMoveCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "MoveCommandGroup.Text"), TranslationManager.getInstance().getTranslation("R", "MoveCommandGroup.Description"), "MoveCommandGroup", true);
        editMoveCommandGroup.setType(CommandGroup.Type.POPUP);
        editMoveCommandGroup.getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "MoveCommandGroup.Text"));
        editMoveCommandGroup.getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "MoveCommandGroup.Text"));
        CommandManager.registerCommandGroup(this, "ReportDialog.Menubar.Edit.Move", editMoveCommandGroup);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", editMoveCommandGroup, CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Move", new MoveLeftCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Move", new MoveRightCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Move", new MoveUpCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Move", new MoveDownCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Move", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Move", new MoveLeftOneCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Move", new MoveRightOneCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Move", new MoveUpOneCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Move", new MoveDownOneCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new LayerUpCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new LayerDownCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new SelectAllCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new DeselectAllCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new SeparatorCommand(), CommandConstraint.LAST);

        DefaultCommandGroup editMorphCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "MorphCommandGroup.Text"), TranslationManager.getInstance().getTranslation("R", "MorphCommandGroup.Description"), "MorphCommandGroup", true);
        editMorphCommandGroup.setType(CommandGroup.Type.POPUP);
        editMorphCommandGroup.getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "MorphCommandGroup.Text"));
        editMorphCommandGroup.getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "MorphCommandGroup.Text"));
        CommandManager.registerCommandGroup(this, "ReportDialog.Menubar.Edit.Morph", editMorphCommandGroup);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", editMorphCommandGroup, CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Morph", new MorphIntoTextFieldCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Morph", new MorphIntoDateFieldCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Morph", new MorphIntoNumberFieldCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Morph", new MorphIntoImageFieldCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Morph", new MorphIntoImageURLFieldCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Morph", new MorphIntoResourceFieldCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Morph", new MorphIntoDrawableFieldCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Morph", new MorphIntoMessageFieldCommand(), CommandConstraint.LAST);

        DefaultCommandGroup editAlignCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "AlignCommandGroup.Text"), TranslationManager.getInstance().getTranslation("R", "AlignCommandGroup.Description"), "AlignCommandGroup", true);
        editAlignCommandGroup.setType(CommandGroup.Type.POPUP);
        editAlignCommandGroup.getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "AlignCommandGroup.Text"));
        editAlignCommandGroup.getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "AlignCommandGroup.Text"));
        CommandManager.registerCommandGroup(this, "ReportDialog.Menubar.Edit.Align", editAlignCommandGroup);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", editAlignCommandGroup, CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Align", new AlignLeftCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Align", new AlignCenterCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Align", new AlignRightCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Align", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Align", new AlignTopCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Align", new AlignMiddleCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Align", new AlignBottomCommand(), CommandConstraint.LAST);

        DefaultCommandGroup editDistributeCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "DistributeCommandGroup.Text"), TranslationManager.getInstance().getTranslation("R", "DistributeCommandGroup.Description"), "DistributeCommandGroup", true);
        editDistributeCommandGroup.setType(CommandGroup.Type.POPUP);
        editDistributeCommandGroup.getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "DistributeCommandGroup.Text"));
        editDistributeCommandGroup.getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "DistributeCommandGroup.Text"));
        CommandManager.registerCommandGroup(this, "ReportDialog.Menubar.Edit.Distribute", editDistributeCommandGroup);
        //CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit", editDistributeCommandGroup, CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Distribute", new DistributeLeftCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Distribute", new DistributeCenterCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Distribute", new DistributeRightCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Distribute", new DistributeGapsHorizontalCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Distribute", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Distribute", new DistributeTopCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Distribute", new DistributeMiddleCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Distribute", new DistributeBottomCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Edit.Distribute", new DistributeGapsVerticalCommand(), CommandConstraint.LAST);

        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar", editCommandGroup, CommandConstraint.LAST);

        CommandManager.addCommandToGroup(this, "ReportTree", new AddPropertiesDataSetCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportTree", new AddGroupCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportTree", new CutCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportTree", new CopyCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportTree", new PasteCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportTree", new DeleteCommand(), CommandConstraint.LAST);

        LinkedHashSet<DataSetPlugin> dataSetPlugins = DataSetPluginRegistry.getInstance().getDataSetPlugins();
        final HashMap<DataSetPlugin, Command> dataSetCommands = new HashMap<DataSetPlugin, Command>();
        for (DataSetPlugin dataSetPlugin : dataSetPlugins)
        {
            AddTableModelDataSetCommand command = new AddTableModelDataSetCommand(dataSetPlugin);
            dataSetCommands.put(dataSetPlugin, command);
            CommandManager.addCommandToGroup(this, "ReportTree", command, new CommandConstraint(CommandConstraint.ConstraintType.BEFORE, "AddPropertiesDataSetCommand"));
        }

        DataSetPluginRegistry.getInstance().addDataSetPluginRegistryListener(new DataSetPluginRegistryListener()
        {
            public void pluginAdded(@NotNull DataSetPlugin dataSetPlugin)
            {
                AddTableModelDataSetCommand command = new AddTableModelDataSetCommand(dataSetPlugin);
                dataSetCommands.put(dataSetPlugin, command);
                CommandManager.addCommandToGroup(ReportDialog.this, "ReportTree", command, new CommandConstraint(CommandConstraint.ConstraintType.BEFORE, "AddPropertiesDataSetCommand"));
            }


            public void pluginRemoved(@NotNull DataSetPlugin dataSetPlugin)
            {
                Command command = dataSetCommands.remove(dataSetPlugin);
                if (command != null)
                {
                    CommandManager.removeCommandFromGroup(ReportDialog.this, "ReportTree", command);
                }
            }
        });

        CommandManager.addCommandToGroup(this, "ReportPanel", new CutCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", new CopyCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", new PasteCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", new DeleteCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", editMorphCommandGroup, CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", editAlignCommandGroup, CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", editDistributeCommandGroup, CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", new HideElementCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportPanel", new AddHyperlinkToElementCommand(), CommandConstraint.LAST);

        DefaultCommandGroup viewCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "ViewCommandGroup.Text"), TranslationManager.getInstance().getTranslation("R", "ViewCommandGroup.Description"), "ViewCommandGroup", true);
        viewCommandGroup.getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "ViewCommandGroup.Text"));
        viewCommandGroup.getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "ViewCommandGroup.Text"));
        CommandManager.registerCommandGroup(this, "ReportDialog.Menubar.View", viewCommandGroup);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new AdjustBandSizeCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new DrawSelectionTypeOutlineCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new DrawSelectionTypeClampCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new ZoomCommand(50), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new ZoomCommand(100), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new ZoomCommand(200), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new ZoomCommand(400), CommandConstraint.LAST);

        DefaultCommandGroup linealUnitCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "LinealUnitGroup.Text"), TranslationManager.getInstance().getTranslation("R", "LinealUnitGroup.Description"), "LinealUnitGroup", true);
        linealUnitCommandGroup.getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "LinealUnitGroup.Text"));
        linealUnitCommandGroup.getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "LinealUnitGroup.Text"));
        linealUnitCommandGroup.setType(CommandGroup.Type.POPUP);
        CommandManager.registerCommandGroup(this, "ReportDialog.Menubar.View.LinealUnit", linealUnitCommandGroup);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View.LinealUnit", new SetUnitCommand(Unit.CM), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View.LinealUnit", new SetUnitCommand(Unit.MM), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View.LinealUnit", new SetUnitCommand(Unit.INCH), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View.LinealUnit", new SetUnitCommand(Unit.PICA), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View.LinealUnit", new SetUnitCommand(Unit.POINTS), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", linealUnitCommandGroup, CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToDesignViewCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToPreviewViewCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToPreviewPDFCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToPreviewHTMLCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToPreviewXLSCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToPreviewRTFCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToPreviewCSVCommand(), CommandConstraint.LAST);
        //CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToPreviewXMLCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToToolWindowMessagesCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToToolWindowPropertyCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.View", new GoToToolWindowReportTreeCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar", viewCommandGroup, CommandConstraint.LAST);

        DefaultCommandGroup helpCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "HelpCommandGroup.Text"), TranslationManager.getInstance().getTranslation("R", "HelpCommandGroup.Description"), "HelpCommandGroup", true);
        helpCommandGroup.getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "HelpCommandGroup.Text"));
        helpCommandGroup.getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "HelpCommandGroup.Text"));
        CommandManager.registerCommandGroup(this, "ReportDialog.Menubar.Help", helpCommandGroup);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar", helpCommandGroup, CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Help", new VisitOnlineForumCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Help", new SeparatorCommand(), CommandConstraint.LAST);
//        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Help", new VisitOnlineHelpHTMLCommand(), CommandConstraint.LAST);
//        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Help", new VisitOnlineHelpPDFCommand(), CommandConstraint.LAST);
//        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Help", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Menubar.Help", new AboutCommand(), CommandConstraint.LAST);

        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new OpenReportCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new SaveReportCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new UndoCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new RedoCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new DeleteCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new CutCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new CopyCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new PasteCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new SeparatorCommand(), CommandConstraint.LAST);

        DefaultCommandGroup previewCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "PreviewCommandGroup.Text"), TranslationManager.getInstance().getTranslation("R", "PreviewCommandGroup.Description"), "PreviewCommandGroup", true);
        previewCommandGroup.setType(CommandGroup.Type.POPUP);
        previewCommandGroup.addChild(new GoToPreviewPDFCommand(), CommandConstraint.LAST);
        previewCommandGroup.addChild(new GoToPreviewHTMLCommand(), CommandConstraint.LAST);
        previewCommandGroup.addChild(new GoToPreviewRTFCommand(), CommandConstraint.LAST);
        previewCommandGroup.addChild(new GoToPreviewXLSCommand(), CommandConstraint.LAST);
        previewCommandGroup.addChild(new GoToPreviewCSVCommand(), CommandConstraint.LAST);
        //previewCommandGroup.addChild(new GoToPreviewXMLCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", previewCommandGroup, CommandConstraint.LAST);

        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new SeparatorCommand(), CommandConstraint.LAST);
        CommandManager.addCommandToGroup(this, "ReportDialog.Toolbar", new CreateReportCommand(), CommandConstraint.LAST);

        CommandManager.initAWTListeners();

        commandToolBar = CommandManager.createCommandToolBar(this, "ReportDialog.Toolbar");
        getContentPane().add(commandToolBar.getToolBar(), BorderLayout.NORTH);

        commandMenuBar = CommandManager.createCommandMenuBar(this, "ReportDialog.Menubar");
        setJMenuBar(commandMenuBar.getJMenuBar());

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl alt shift L"), "turnOnLogging");//NON-NLS
        getRootPane().getActionMap().put("turnOnLogging", new AbstractAction()//NON-NLS
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Logger.getLogger("org.pentaho.reportdesigner").setLevel(Level.ALL);//NON-NLS
                Logger.getLogger("org.pentaho.reportdesigner.lib.client.plugin").setLevel(Level.ALL);//NON-NLS
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "Debug Logging enabled");
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl alt shift E"), "throwUncatchedException");//NON-NLS
        getRootPane().getActionMap().put("throwUncatchedException", new AbstractAction()//NON-NLS
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                //noinspection ProhibitedExceptionThrown
                throw new RuntimeException("kaboom");
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl alt shift 8"), "resizeTo800_600");//NON-NLS
        getRootPane().getActionMap().put("resizeTo800_600", new AbstractAction()//NON-NLS
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                setSize(800, 600);
                invalidate();
                validate();
                repaint();
            }
        });


        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl alt shift D"), "enableDebugStuff");//NON-NLS
        getRootPane().getActionMap().put("enableDebugStuff", new AbstractAction()//NON-NLS
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (!debugEnabled)
                {
                    Logger.getLogger("org.pentaho.reportdesigner").setLevel(Level.ALL);//NON-NLS
                    Logger.getLogger("org.pentaho.reportdesigner.lib.client.plugin").setLevel(Level.ALL);//NON-NLS
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "Debug Logging enabled");

                    DefaultCommandGroup debugCommandGroup = new DefaultCommandGroup(TranslationManager.getInstance().getTranslation("R", "DebugCommandGroup.Text"), TranslationManager.getInstance().getTranslation("R", "DebugCommandGroup.Description"), "DebugCommandGroup", true);
                    debugCommandGroup.getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "DebugCommandGroup.Text"));
                    debugCommandGroup.getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "DebugCommandGroup.Text"));
                    CommandManager.registerCommandGroup(ReportDialog.this, "ReportDialog.Menubar.Debug", debugCommandGroup);
                    CommandManager.addCommandToGroup(ReportDialog.this, "ReportDialog.Menubar", debugCommandGroup, CommandConstraint.LAST);
                    //CommandManager.addCommandToGroup(ReportDialog.this, "ReportDialog.Menubar.Debug", new PrintCommandManagerStatisticsCommand(), CommandConstraint.LAST);
                    //CommandManager.addCommandToGroup(ReportDialog.this, "ReportDialog.Menubar.Debug", new ResetCommandManagerStatisticsCommand(), CommandConstraint.LAST);
                    CommandManager.addCommandToGroup(ReportDialog.this, "ReportDialog.Menubar.Debug", new PrintUndoInfosCommand(), CommandConstraint.LAST);
                    CommandManager.addCommandToGroup(ReportDialog.this, "ReportDialog.Menubar.Debug", new ShowLogCommand(), CommandConstraint.LAST);
                    CommandManager.addCommandToGroup(ReportDialog.this, "ReportDialog.Menubar.Debug", new FindTranslationsCommand(), CommandConstraint.LAST);
                    debugEnabled = true;

                    CommandManager.dataProviderChanged();//trigger update
                }
            }
        });

        //getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl alt shift N"), "nullDataSet");//NON-NLS
        //getRootPane().getActionMap().put("nullDataSet", new AbstractAction()//NON-NLS
        //{
        //    public void actionPerformed(@NotNull ActionEvent e)
        //    {
        //        MultiDataSetReportElement mdre = (MultiDataSetReportElement) report.getDataSetsReportElement().getChildren().get(0);
        //        mdre.setSelectedJNDIDataSource(null);
        //        //mdre.setConnectionType(MultiDataSetReportElement.ConnectionType.XQuery);
        //        //mdre.setXQueryDataFile(null);
        //    }
        //});
    }


    @NotNull
    public Long getApplicationID()
    {
        return applicationID;
    }


    public void clearStatusText()
    {
        statusBar.setGeneralInfoText(" ");
    }


    public void setStatusText(@NotNull String description)
    {
        statusBar.setGeneralInfoText(description);
    }


    @NotNull
    public JComponent getRootJComponent()
    {
        return getRootPane();
    }


    @Nullable
    public ToolWindow getPaletteToolWindow()
    {
        return paletteToolWindow;
    }


    @Nullable
    public ToolWindow getReportTreeToolWindow()
    {
        return reportTreeToolWindow;
    }


    @Nullable
    public ToolWindow getPropertyToolWindow()
    {
        return propertyToolWindow;
    }


    @Nullable
    public ToolWindow getInspectionsToolWindow()
    {
        return inspectionsToolWindow;
    }


    public boolean isSwtLoaded()
    {
        return swtLoaded;
    }


    public void setSwtLoaded(boolean swtLoaded)
    {
        this.swtLoaded = swtLoaded;
    }

}
