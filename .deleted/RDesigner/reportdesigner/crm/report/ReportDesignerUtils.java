package org.pentaho.reportdesigner.crm.report;

import org.gjt.xpp.XmlPullNode;
import org.gjt.xpp.XmlPullParser;
import org.gjt.xpp.XmlPullParserFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.FilePath;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFactory;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.crm.report.util.XMLContextKeys;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.ExceptionUtils;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 11.02.2007
 * Time: 12:00:29
 */
public class ReportDesignerUtils
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ReportDesignerUtils.class.getName());


    private ReportDesignerUtils()
    {
    }


    public static boolean isModifiedOrNotEmpty(@NotNull ReportDialog reportDialog)
    {
        //noinspection ConstantConditions
        if (reportDialog == null)
        {
            throw new IllegalArgumentException("reportDialog must not be null");
        }

        if (reportDialog.isModified())
        {
            return true;
        }

        Report report = reportDialog.getReport();
        if (report != null)
        {
            ArrayList<ReportElement> children = report.getChildren();

            for (ReportElement child : children)
            {
                if (child instanceof BandToplevelReportElement)
                {
                    BandToplevelReportElement bandToplevelReportElement = (BandToplevelReportElement) child;
                    if (!bandToplevelReportElement.getChildren().isEmpty())
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    @Nullable
    public static ReportDialog getOpenReportDialog(@NotNull File f)
    {
        //noinspection ConstantConditions
        if (f == null)
        {
            throw new IllegalArgumentException("f must not be null");
        }

        //test if this file is already open
        LinkedList<ReportDialog> reportDialogs = ReportDesignerWindows.getInstance().getReportDialogs();
        for (ReportDialog dialog : reportDialogs)
        {
            try
            {
                File file = dialog.getCurrentReportFile();
                if (file != null && file.getCanonicalPath().equals(f.getCanonicalPath()))
                {
                    return dialog;
                }
            }
            catch (IOException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "OpenReportCommand.execute ", e);
            }
        }
        return null;
    }


    public static void openReport(@NotNull File currentReportFile, @NotNull final ReportDialog reportDialog, @Nullable final File mainReportFile)
    {
        try
        {
            long n1 = System.nanoTime();

            final File currentReportFile1 = currentReportFile;

            final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(reportDialog,
                                                                                      TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.ProgressDialog.Title"),
                                                                                      "");

            //final InternalProgressDialog progressDialog = new InternalProgressDialog(reportDialog,
            //                                                                          TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.ProgressDialog.Title"),
            //                                                                          "");

            Thread t = new Thread()
            {
                public void run()
                {
                    BufferedReader bufferedReader = null;
                    try
                    {
                        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                        XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                        //noinspection IOResourceOpenedButNotSafelyClosed
                        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(currentReportFile1), XMLConstants.ENCODING));
                        xmlPullParser.setInput(bufferedReader);

                        XMLContext xmlContext = new XMLContext();
                        XMLContextKeys.CONTEXT_PATH.putObject(xmlContext, currentReportFile1.getParentFile());

                        XmlPullNode node;
                        Report report = null;
                        try
                        {
                            xmlPullParser.next(); // get first start tag
                            node = xmlPullParserFactory.newPullNode(xmlPullParser);

                            if (XMLConstants.REPORT.equals(node.getRawName()))
                            {
                                report = ReportFactory.createEmptyReport();
                                report.readObject(node, xmlContext);
                            }
                            else if (XMLConstants.SUBREPORT.equals(node.getRawName()))
                            {
                                SubReport subReport = ReportFactory.createEmptySubReport();
                                report = subReport;

                                report.readObject(node, xmlContext);
                            }

                            node.resetPullNode();
                        }
                        catch (Exception e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "OpenReportCommand.run ", e);
                            ExceptionUtils.disposeDialogInEDT(progressDialog);
                            EventQueue.invokeLater(new Runnable()
                            {
                                public void run()
                                {
                                    JOptionPane.showMessageDialog(reportDialog,
                                                                  TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.InvalidFile.Message"),
                                                                  TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.InvalidFile.Title"), JOptionPane.ERROR_MESSAGE);
                                }
                            });

                            return;
                        }

                        final String canonicalPath = currentReportFile1.getCanonicalPath();

                        final Report report1 = report;
                        EventQueue.invokeAndWait(new Runnable()
                        {
                            public void run()
                            {
                                reportDialog.setReport(report1);
                                reportDialog.getWorkspaceSettings().put(WorkspaceSettings.LAST_ACCESSED_REPORT_FILE, canonicalPath);
                                reportDialog.setCurrentReportFile(currentReportFile1);
                                reportDialog.setModified(false);

                                if (report1 instanceof SubReport)
                                {
                                    SubReport subReport = (SubReport) report1;
                                    subReport.getSubReportDataElement().setCurrentMainReport(mainReportFile);
                                }

                                progressDialog.dispose();
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "OpenReportCommand.run ", e);
                        ExceptionUtils.disposeDialogInEDT(progressDialog);
                        UncaughtExcpetionsModel.getInstance().addException(e);
                    }
                    finally
                    {
                        IOUtil.closeStream(bufferedReader);
                    }
                }
            };

            t.setDaemon(true);
            t.setPriority(Thread.NORM_PRIORITY - 1);
            t.start();

            if (t.isAlive())
            {
                progressDialog.setVisible(true);
            }


            long n2 = System.nanoTime();
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "OpenReportCommand.openReport " + (n2 - n1) / (1000. * 1000.) + " ms");
        }
        catch (Exception e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
    }


    @Nullable
    public static Report openReport(@NotNull File file) throws Exception
    {
        BufferedReader bufferedReader = null;
        try
        {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            //noinspection IOResourceOpenedButNotSafelyClosed
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), XMLConstants.ENCODING));
            xmlPullParser.setInput(bufferedReader);

            XMLContext xmlContext = new XMLContext();
            XMLContextKeys.CONTEXT_PATH.putObject(xmlContext, file.getParentFile());

            XmlPullNode node;
            Report report = null;

            xmlPullParser.next(); // get first start tag
            node = xmlPullParserFactory.newPullNode(xmlPullParser);

            if (XMLConstants.REPORT.equals(node.getRawName()))
            {
                report = ReportFactory.createEmptyReport();
                report.readObject(node, xmlContext);
            }
            else if (XMLConstants.SUBREPORT.equals(node.getRawName()))
            {
                report = ReportFactory.createEmptySubReport();
                report.readObject(node, xmlContext);
            }

            node.resetPullNode();

            return report;
        }
        finally
        {
            IOUtil.closeStream(bufferedReader);
        }
    }


    @Nullable
    public static SubReport getSubReport(@NotNull SubReportElement subReportElement) throws Exception
    {
        //noinspection ConstantConditions
        if (subReportElement == null)
        {
            throw new IllegalArgumentException("subReportElement must not be null");
        }

        //perhaps it is already open
        FilePath filePath = subReportElement.getFilePath();
        ReportDialog subReportDialog = getOpenReportDialog(new File(filePath.getPath()));
        if (subReportDialog != null)
        {
            Report report = subReportDialog.getReport();
            if (report instanceof SubReport)
            {
                SubReport subReport = (SubReport) report;
                return subReport;
            }
        }

        //try to load from disk
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();

        BufferedReader bufferedReader = null;
        try
        {
            //noinspection IOResourceOpenedButNotSafelyClosed
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.getPath()), XMLConstants.ENCODING));
            xmlPullParser.setInput(bufferedReader);

            XmlPullNode node;

            XMLContext xmlContext = new XMLContext();
            XMLContextKeys.CONTEXT_PATH.putObject(xmlContext, new File(filePath.getPath()).getParentFile());

            xmlPullParser.next();//get first start tag
            node = xmlPullParserFactory.newPullNode(xmlPullParser);

            if (XMLConstants.SUBREPORT.equals(node.getRawName()))
            {
                SubReport subReport = ReportFactory.createEmptySubReport();
                subReport.readObject(node, xmlContext);
                return subReport;
            }
        }
        finally
        {
            IOUtil.closeStream(bufferedReader);
        }

        return null;
    }


    @Nullable
    public static Report getReport(@NotNull File file)
    {
        ReportDialog dialog = getOpenReportDialog(file);
        if (dialog != null)
        {
            return dialog.getReport();
        }
        else
        {
            try
            {
                return openReport(file);
            }
            catch (Exception e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDesignerUtils.getReport ", e);
                return null;
            }
        }
    }


    @NotNull
    public static String[] getPossibleQueriesForSubReport(@Nullable Report mainReport)
    {
        ArrayList<String> possibleQueries = new ArrayList<String>();
        if (mainReport != null)
        {
            DataSetsReportElement dataSetsReportElement = mainReport.getDataSetsReportElement();
            for (ReportElement reportElement : dataSetsReportElement.getChildren())
            {
                if (reportElement instanceof MultiDataSetReportElement)
                {
                    MultiDataSetReportElement multiDataSetReportElement = (MultiDataSetReportElement) reportElement;
                    String queryName = multiDataSetReportElement.getQueryName();
                    possibleQueries.add(queryName);
                }
            }
        }

        String[] pq = possibleQueries.toArray(new String[possibleQueries.size()]);
        return pq;
    }
}
