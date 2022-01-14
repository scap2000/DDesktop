package org.pentaho.reportdesigner.crm.report.properties.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.configuration.ReportConfiguration;
import org.pentaho.reportdesigner.crm.report.configuration.ReportConfigurationEditor;
import org.pentaho.reportdesigner.crm.report.util.JFreeReportBootingHelper;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Martin
 * Date: 17.03.2007
 * Time: 11:46:14
 */
public class ReportConfigurationChooser
{
    private ReportConfigurationChooser()
    {
    }


    @NotNull
    public static ReportConfiguration showReportConfigurationChooser(@NotNull JComponent parent, @NotNull String title, @NotNull ReportConfiguration origReportConfiguration)
    {
        //noinspection ConstantConditions
        if (parent == null)
        {
            throw new IllegalArgumentException("parent must not be null");
        }
        //noinspection ConstantConditions
        if (title == null)
        {
            throw new IllegalArgumentException("title must not be null");
        }
        //noinspection ConstantConditions
        if (origReportConfiguration == null)
        {
            throw new IllegalArgumentException("origReportConfiguration must not be null");
        }

        JFreeReportBootingHelper.boot(parent);

        final CenterPanelDialog centerPanelDialog;
        Window windowAncestor = SwingUtilities.getWindowAncestor(parent);

        if (windowAncestor instanceof Dialog)
        {
            centerPanelDialog = new CenterPanelDialog((Dialog) windowAncestor, title, true);
        }
        else
        {
            centerPanelDialog = new CenterPanelDialog((Frame) windowAncestor, title, true);
        }

        @NonNls
        final FormLayout formLayout = new FormLayout("4dlu, fill:default:grow, 4dlu", "4dlu, fill:default:grow, 4dlu");
        final JPanel centerPanel = new JPanel(formLayout);

        final CellConstraints cc = new CellConstraints();

        ReportConfigurationEditor configurationEditor;
        try
        {
            configurationEditor = new ReportConfigurationEditor(origReportConfiguration);
            centerPanelDialog.dispose();
            centerPanel.add(configurationEditor, cc.xy(2, 2));
        }
        catch (Throwable e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
            return origReportConfiguration;
        }


        final boolean[] action = new boolean[]{false};

        JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                action[0] = true;
                centerPanelDialog.dispose();
            }
        });

        JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                action[0] = false;
                centerPanelDialog.dispose();
            }
        });

        centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.pack();
        centerPanelDialog.setSize(800, 600);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return configurationEditor.getReportConfiguration();
        }
        return origReportConfiguration;
    }
}
