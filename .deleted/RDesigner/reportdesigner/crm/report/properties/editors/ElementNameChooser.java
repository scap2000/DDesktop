package org.pentaho.reportdesigner.crm.report.properties.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfo;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.util.ElementNameDefinition;
import org.pentaho.reportdesigner.crm.report.util.ReportElementNameUtilities;
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
import java.util.Iterator;

/**
 * User: Martin
 * Date: 11.01.2006
 * Time: 11:02:54
 */
public class ElementNameChooser
{
    private ElementNameChooser()
    {
    }


    @Nullable
    public static String showElementChooser(@Nullable final ReportElement reportElement, @NotNull JComponent parent, @NotNull String title, @Nullable final String value)
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
        final FormLayout formLayout = new FormLayout("4dlu, fill:default:grow, 4dlu", "4dlu, fill:default:grow, 4dlu, default, 4dlu, default, 4dlu");
        final JPanel centerPanel = new JPanel(formLayout);

        final CellConstraints cc = new CellConstraints();

        final JList list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.setCellRenderer(new DefaultListCellRenderer()
        {
            @NotNull
            public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                if (value instanceof ElementNameDefinition)
                {
                    ElementNameDefinition nameDefinition = (ElementNameDefinition) value;
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, nameDefinition.getElementName(), index, isSelected, cellHasFocus);

                    ReportElementInfo info = ReportElementInfoFactory.getInstance().getReportElementInfo(nameDefinition.getDefiningElement());
                    label.setIcon(info.getIcon());

                    return label;
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });


        final JCheckBox hideDefaultElementsCheckBox = new JCheckBox(TranslationManager.getInstance().getTranslation("R", "ElementNameChooser.HideDefaultElements"));
        final JCheckBox hideSpecialElementsCheckBox = new JCheckBox(TranslationManager.getInstance().getTranslation("R", "ElementNameChooser.HideSpecialElements"));

        hideDefaultElementsCheckBox.setSelected(true);
        hideSpecialElementsCheckBox.setSelected(true);

        refillList(list, reportElement, hideDefaultElementsCheckBox.isSelected(), hideSpecialElementsCheckBox.isSelected(), value);

        hideSpecialElementsCheckBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                refillList(list, reportElement, hideDefaultElementsCheckBox.isSelected(), hideSpecialElementsCheckBox.isSelected(), value);
            }
        });

        hideDefaultElementsCheckBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                refillList(list, reportElement, hideDefaultElementsCheckBox.isSelected(), hideSpecialElementsCheckBox.isSelected(), value);
            }
        });

        centerPanel.add(new JScrollPane(list), cc.xy(2, 2));
        centerPanel.add(hideDefaultElementsCheckBox, cc.xy(2, 4));
        centerPanel.add(hideSpecialElementsCheckBox, cc.xy(2, 6));

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


        centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.pack();
        centerPanelDialog.setSize(300, 300);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            ElementNameDefinition elementNameDefinition = (ElementNameDefinition) list.getSelectedValue();
            if (elementNameDefinition != null)
            {
                return elementNameDefinition.getElementName();
            }
        }
        return value;
    }


    private static void refillList(@NotNull JList list, @Nullable ReportElement reportElement, boolean hideDefault, boolean hideSpecial, @Nullable String value)
    {
        HashSet<ElementNameDefinition> hashSet = new HashSet<ElementNameDefinition>();
        if (reportElement != null)
        {
            hashSet = ReportElementNameUtilities.getElementNameDefinitions(reportElement);
        }

        if (hideDefault)
        {
            for (Iterator it = hashSet.iterator(); it.hasNext();)
            {
                ElementNameDefinition elementNameDefinition = (ElementNameDefinition) it.next();
                if (elementNameDefinition.getElementName().contains("@"))
                {
                    it.remove();
                }
            }
        }

        if (hideSpecial)
        {
            for (Iterator it = hashSet.iterator(); it.hasNext();)
            {
                ElementNameDefinition elementNameDefinition = (ElementNameDefinition) it.next();
                if (elementNameDefinition.getDefiningElement() instanceof Report)
                {
                    it.remove();
                }
                else if (elementNameDefinition.getDefiningElement() instanceof BandToplevelReportElement)
                {
                    it.remove();
                }
            }
        }

        for (Iterator it = hashSet.iterator(); it.hasNext();)
        {
            ElementNameDefinition elementNameDefinition = (ElementNameDefinition) it.next();
            if (elementNameDefinition.getDefiningElement() instanceof DataSetsReportElement)
            {
                it.remove();
            }
            else if (elementNameDefinition.getDefiningElement() instanceof DataSetReportElement)
            {
                it.remove();
            }
            else if (elementNameDefinition.getDefiningElement() instanceof ReportGroup)
            {
                it.remove();
            }
        }

        final ElementNameDefinition[] elementNameDefinitions = hashSet.toArray(new ElementNameDefinition[hashSet.size()]);
        Arrays.sort(elementNameDefinitions, new Comparator<ElementNameDefinition>()
        {
            public int compare(@NotNull ElementNameDefinition o1, @NotNull ElementNameDefinition o2)
            {
                return o1.getElementName().compareToIgnoreCase(o2.getElementName());
            }
        });

        list.setModel(new AbstractListModel()
        {
            public int getSize()
            {
                return elementNameDefinitions.length;
            }


            @NotNull
            public Object getElementAt(int i)
            {
                return elementNameDefinitions[i];
            }
        });

        for (int i = 0; i < elementNameDefinitions.length; i++)
        {
            ElementNameDefinition elementNameDefinition = elementNameDefinitions[i];
            if (elementNameDefinition.getElementName().equalsIgnoreCase(value))
            {
                list.setSelectedIndex(i);
                break;
            }
        }


    }

}
