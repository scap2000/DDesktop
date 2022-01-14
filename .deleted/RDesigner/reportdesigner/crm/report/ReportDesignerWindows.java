package org.pentaho.reportdesigner.crm.report;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.settings.ApplicationSettings;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * User: Martin
 * Date: 01.01.2007
 * Time: 17:39:10
 */
public class ReportDesignerWindows
{

    @NotNull
    private static final ReportDesignerWindows instance = new ReportDesignerWindows();


    @NotNull
    public static ReportDesignerWindows getInstance()
    {
        return instance;
    }


    private long nextAppicationID = 1;

    @NotNull
    private LinkedList<ReportDesignerWindowsListener> reportDesignerWindowsListeners = new LinkedList<ReportDesignerWindowsListener>();

    @NotNull
    private LinkedList<ReportDialog> reportDialogs = new LinkedList<ReportDialog>();


    private ReportDesignerWindows()
    {
    }


    @NotNull
    public Long getNextAppicationID()
    {
        nextAppicationID++;
        return new Long(nextAppicationID);
    }


    @NotNull
    public ReportDialog createNewReportDialog(@Nullable Report report)
    {
        final ReportDialog newReportDialog = new ReportDialog(getNextAppicationID(), report);
        newReportDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        newReportDialog.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(@NotNull WindowEvent e)
            {
                boolean closed = newReportDialog.close();
                if (closed)
                {
                    unregisterReportDialog(newReportDialog);
                }
            }
        });

        newReportDialog.setVisible(true);
        registerReportDialog(newReportDialog);

        return newReportDialog;
    }


    @NotNull
    public LinkedList<ReportDialog> getReportDialogs()
    {
        return new LinkedList<ReportDialog>(reportDialogs);
    }


    @Nullable
    public ReportDialog getReportDialog(@NotNull Report report)
    {
        for (ReportDialog reportDialog : reportDialogs)
        {
            //noinspection ObjectEquality
            if (reportDialog.getReport() == report)
            {
                return reportDialog;
            }
        }
        return null;
    }


    public void registerReportDialog(@NotNull ReportDialog reportDialog)
    {
        if (!reportDialogs.contains(reportDialog))
        {
            reportDialogs.add(reportDialog);

            LinkedList<ReportDesignerWindowsListener> reportDesignerWindowsListeners = new LinkedList<ReportDesignerWindowsListener>(this.reportDesignerWindowsListeners);
            for (ReportDesignerWindowsListener reportDesignerWindowsListener : reportDesignerWindowsListeners)
            {
                reportDesignerWindowsListener.reportDialogOpened(reportDialog);
            }
        }
    }


    public void unregisterReportDialog(@NotNull ReportDialog reportDialog)
    {
        if (reportDialogs.contains(reportDialog))
        {
            reportDialogs.remove(reportDialog);

            LinkedList<ReportDesignerWindowsListener> reportDesignerWindowsListeners = new LinkedList<ReportDesignerWindowsListener>(this.reportDesignerWindowsListeners);
            for (ReportDesignerWindowsListener reportDesignerWindowsListener : reportDesignerWindowsListeners)
            {
                reportDesignerWindowsListener.reportDialogClosed(reportDialog);
            }
        }
    }


    public void addReportDesignerWindowsListener(@NotNull ReportDesignerWindowsListener reportDesignerWindowsListener)
    {
        if (!reportDesignerWindowsListeners.contains(reportDesignerWindowsListener))
        {
            reportDesignerWindowsListeners.add(reportDesignerWindowsListener);
        }
    }


    public void removeReportDesignerWindowsListener(@NotNull ReportDesignerWindowsListener reportDesignerWindowsListener)
    {
        reportDesignerWindowsListeners.remove(reportDesignerWindowsListener);
    }


    public void saveApplicationSettings() throws IOException
    {
        File file = new File(System.getProperty(ReportDialogConstants.USER_HOME), ReportDialogConstants.REPORT_DIRECTORY);
        file.mkdirs();
        File workspaceSettingsFile = new File(file, ReportDialogConstants.APPLICATION_SETTINGS_XML);

        ApplicationSettings.getInstance().writeSettings(workspaceSettingsFile);
    }


    public void saveWorkspaceSettings() throws IOException
    {
        File file = new File(System.getProperty(ReportDialogConstants.USER_HOME), ReportDialogConstants.REPORT_DIRECTORY);
        file.mkdirs();
        File workspaceSettingsFile = new File(file, ReportDialogConstants.WORKSPACE_SETTINGS_XML);

        WorkspaceSettings.getInstance().writeSettings(workspaceSettingsFile);
    }

}
