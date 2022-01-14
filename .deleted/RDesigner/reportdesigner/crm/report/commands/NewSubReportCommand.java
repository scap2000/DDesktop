package org.pentaho.reportdesigner.crm.report.commands;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDesignerUtils;
import org.pentaho.reportdesigner.crm.report.ReportDesignerWindows;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportFactory;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class NewSubReportCommand extends AbstractCommand
{

    public NewSubReportCommand()
    {
        super("NewSubReportCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "NewSubReportCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "NewSubReportCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "NewSubReportCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "NewSubReportCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getNewIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "NewSubReportCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        File f = reportDialog.getCurrentReportFile();
        JFileChooser fileChooser;
        if (f != null && f.canRead())
        {
            fileChooser = new JFileChooser(f);
            fileChooser.setSelectedFile(f);
        }
        else
        {
            fileChooser = new JFileChooser();
        }

        fileChooser.setDialogTitle(TranslationManager.getInstance().getTranslation("R", "NewSubReportCommand.SelectMainreport"));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter()
        {
            public boolean accept(@NotNull File f)
            {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(ReportDialogConstants.REPORT_FILE_ENDING);
            }


            @NotNull
            public String getDescription()
            {
                return TranslationManager.getInstance().getTranslation("R", "FileChooser.ReportFiles.Description");
            }
        });

        fileChooser.setFileView(new ReportFileView());

        File currentReportFile;
        int option = fileChooser.showOpenDialog(reportDialog);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            currentReportFile = fileChooser.getSelectedFile();
        }
        else
        {
            return;
        }

        SubReport subReport = ReportFactory.createEmptySubReport();
        subReport.getSubReportDataElement().setCurrentMainReport(currentReportFile);

        Report report = ReportDesignerUtils.getReport(currentReportFile);
        String[] pq = ReportDesignerUtils.getPossibleQueriesForSubReport(report);
        if (pq.length > 0)
        {
            //select first query not named "default"
            String defaultSelection = pq[0];
            for (String aPq : pq)
            {
                if (!ReportDialogConstants.DEFAULT_DATA_FACTORY.equalsIgnoreCase(aPq))
                {
                    defaultSelection = aPq;
                    break;
                }
            }
            Object s = JOptionPane.showInputDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "NewSubReportCommand.SelectTheQueryToUseInTheSubreport"), TranslationManager.getInstance().getTranslation("R", "NewSubReportCommand.SubreportQuery"), JOptionPane.QUESTION_MESSAGE, null, pq, defaultSelection);
            if (s != null)
            {
                subReport.setQuery(s.toString());
            }
        }

        ReportDesignerWindows.getInstance().createNewReportDialog(subReport);
    }


}
