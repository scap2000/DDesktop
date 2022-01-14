package org.pentaho.reportdesigner.crm.report.properties.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.ReportDesignerUtils;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * User: Martin
 * Date: 11.01.2006
 * Time: 11:02:54
 */
public class QueryChooser
{
    @NotNull
    private static final String[] EMPTY_STRING_ARRAY = new String[0];


    private QueryChooser()
    {
    }


    @Nullable
    public static String showQueryChooser(@Nullable ReportElement reportElement, @NotNull JComponent parent, @NotNull String title, @Nullable String value)
    {
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

        String[] queries = EMPTY_STRING_ARRAY;
        if (reportElement != null)
        {
            queries = ReportDesignerUtils.getPossibleQueriesForSubReport(reportElement.getReport());
        }
        Arrays.sort(queries, String.CASE_INSENSITIVE_ORDER);

        JList list = new JList(queries);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        centerPanel.add(new JScrollPane(list), cc.xy(2, 2));

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

        list.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                if (e.getClickCount() > 1)
                {
                    action[0] = true;
                    centerPanelDialog.dispose();
                }
            }
        });

        for (int i = 0; i < queries.length; i++)
        {
            String query = queries[i];
            if (query.equalsIgnoreCase(value))
            {
                list.setSelectedIndex(i);
                break;
            }
        }

        centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.pack();
        centerPanelDialog.setSize(300, 300);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            String query = (String) list.getSelectedValue();
            if (query != null)
            {
                return query;
            }
        }
        return value;
    }

}