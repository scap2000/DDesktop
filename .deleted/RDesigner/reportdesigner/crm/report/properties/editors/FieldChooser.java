package org.pentaho.reportdesigner.crm.report.properties.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfo;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.util.FieldDefinition;
import org.pentaho.reportdesigner.crm.report.util.ReportFieldUtilities;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 11.01.2006
 * Time: 11:02:54
 */
public class FieldChooser
{
    private FieldChooser()
    {
    }


    @Nullable
    public static String showFieldChooser(@Nullable ReportElement reportElement, @NotNull JComponent parent, @NotNull String title, @Nullable String value)
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

        HashSet<FieldDefinition> hashSet = new HashSet<FieldDefinition>();
        if (reportElement != null)
        {
            hashSet = ReportFieldUtilities.getFieldDefinitions(reportElement);
        }
        FieldDefinition[] fieldDefinitions = hashSet.toArray(new FieldDefinition[hashSet.size()]);
        Arrays.sort(fieldDefinitions, new Comparator<FieldDefinition>()
        {
            public int compare(@NotNull FieldDefinition o1, @NotNull FieldDefinition o2)
            {
                return o1.getField().compareToIgnoreCase(o2.getField());
            }
        });

        JList list = new JList(fieldDefinitions);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.setCellRenderer(new DefaultListCellRenderer()
        {
            @NotNull
            public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                if (value instanceof FieldDefinition)
                {
                    FieldDefinition fieldDefinition = (FieldDefinition) value;
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, fieldDefinition.getField(), index, isSelected, cellHasFocus);

                    ReportElementInfo info = ReportElementInfoFactory.getInstance().getReportElementInfo(fieldDefinition.getDefiningElement());
                    label.setIcon(info.getIcon());

                    return label;
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });


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

        for (int i = 0; i < fieldDefinitions.length; i++)
        {
            FieldDefinition fieldDefinition = fieldDefinitions[i];
            if (fieldDefinition.getField().equalsIgnoreCase(value))
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
            FieldDefinition fieldDefinition = (FieldDefinition) list.getSelectedValue();
            if (fieldDefinition != null)
            {
                return fieldDefinition.getField();
            }
        }
        return value;
    }

}
