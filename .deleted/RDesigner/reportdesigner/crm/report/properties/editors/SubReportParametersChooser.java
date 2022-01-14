package org.pentaho.reportdesigner.crm.report.properties.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.SubReportParameter;
import org.pentaho.reportdesigner.crm.report.model.SubReportParameters;
import org.pentaho.reportdesigner.crm.report.properties.PropertyTable;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Martin
 * Date: 01.11.2005
 * Time: 10:23:10
 */
public class SubReportParametersChooser
{

    private SubReportParametersChooser()
    {
    }


    @NotNull
    public static SubReportParameters showSubReportParametersChooser(@NotNull PropertyTable parent, @NotNull String title, @NotNull SubReportParameters origParameters)
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


        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, fill:default:grow, 4dlu",
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "fill:10dlu:grow, " +
                                               "10dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "fill:10dlu:grow," +
                                               "4dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();

        JPanel centerPanel = new JPanel(formLayout);

        JLabel importLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "SubReportParameterChooser.ImportParameters"));
        JLabel exportLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "SubReportParameterChooser.ExportParameters"));
        ParameterPanel importParameterPanel = new ParameterPanel(TranslationManager.getInstance().getTranslation("R", "SubReportParameterChooser.GlobaleImport"), origParameters.isGlobalImport(), new ArrayList<SubReportParameter>(origParameters.getImportParameters().values()));
        ParameterPanel exportParameterPanel = new ParameterPanel(TranslationManager.getInstance().getTranslation("R", "SubReportParameterChooser.GlobaleExport"), origParameters.isGlobalExport(), new ArrayList<SubReportParameter>(origParameters.getExportParameters().values()));

        centerPanel.add(importLabel, cc.xy(2, 2));
        centerPanel.add(importParameterPanel, cc.xy(2, 4));
        centerPanel.add(exportLabel, cc.xy(2, 6));
        centerPanel.add(exportParameterPanel, cc.xy(2, 8));

        centerPanelDialog.setCenterPanel(centerPanel);

        centerPanelDialog.pack();
        GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 500, 600);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return new SubReportParameters(importParameterPanel.isGlobalValue(),
                                           importParameterPanel.getSubReportParameters(),
                                           exportParameterPanel.isGlobalValue(),
                                           exportParameterPanel.getSubReportParameters());
        }

        return origParameters;
    }


    private static class ParameterPanel extends JPanel
    {
        @NotNull
        private JCheckBox globalValueCheckBox;
        @NotNull
        private JTable subReportParametersTable;
        @NotNull
        private JButton addEntryButton;
        @NotNull
        private JButton removeEntryButton;
        @NotNull
        private SubReportParametersTableModel subReportParametersTableModel;


        private ParameterPanel(@NotNull String globaleLabel, boolean globalValue, @NotNull ArrayList<SubReportParameter> subReportParameters)
        {
            @NonNls
            FormLayout formLayout = new FormLayout("pref, 4dlu, fill:default:grow, 4dlu, pref, 4dlu, pref",
                                                   "pref, 4dlu, fill:default:grow, 4dlu, pref");

            formLayout.setColumnGroups(new int[][]{{5, 7}});

            @NonNls
            CellConstraints cc = new CellConstraints();

            setLayout(formLayout);

            globalValueCheckBox = new JCheckBox(globaleLabel, globalValue);
            subReportParametersTableModel = new SubReportParametersTableModel(subReportParameters);
            subReportParametersTable = new JTable(subReportParametersTableModel);
            addEntryButton = new JButton(TranslationManager.getInstance().getTranslation("R", "ParameterPanel.ButtonAdd"));
            removeEntryButton = new JButton(TranslationManager.getInstance().getTranslation("R", "ParameterPanel.ButtonRemove"));

            add(globalValueCheckBox, cc.xyw(1, 1, 7));
            add(new JScrollPane(subReportParametersTable), cc.xyw(1, 3, 7));
            add(addEntryButton, cc.xy(5, 5));
            add(removeEntryButton, cc.xy(7, 5));

            globalValueCheckBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    updateState();
                }
            });

            subReportParametersTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);//NON-NLS

            subReportParametersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(@NotNull ListSelectionEvent e)
                {
                    updateState();
                }
            });

            addEntryButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    SubReportParameter subReportParameter = new SubReportParameter("", "");
                    subReportParametersTableModel.addEntry(subReportParameter);
                    int index = subReportParametersTableModel.getRowCount() - 1;
                    subReportParametersTable.getSelectionModel().setSelectionInterval(index, index);
                }
            });

            removeEntryButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    int[] rows = subReportParametersTable.getSelectedRows();
                    Arrays.sort(rows);
                    for (int i = rows.length - 1; i >= 0; i--)
                    {
                        int row = rows[i];
                        subReportParametersTableModel.removeEntry(row);
                    }
                    int rowCount = subReportParametersTable.getRowCount();
                    if (rowCount > 0)
                    {
                        subReportParametersTable.getSelectionModel().setSelectionInterval(rowCount - 1, rowCount - 1);
                    }
                }
            });

            updateState();
        }


        private void updateState()
        {
            subReportParametersTable.setEnabled(!globalValueCheckBox.isSelected());
            addEntryButton.setEnabled(!globalValueCheckBox.isSelected());
            removeEntryButton.setEnabled(!globalValueCheckBox.isSelected() && subReportParametersTable.getSelectedRow() != -1);
        }


        public boolean isGlobalValue()
        {
            return globalValueCheckBox.isSelected();
        }


        @NotNull
        public ArrayList<SubReportParameter> getSubReportParameters()
        {
            return subReportParametersTableModel.getSubReportParameters();
        }
    }

    private static class SubReportParametersTableModel extends AbstractTableModel
    {
        @NotNull
        private ArrayList<SubReportParameter> subReportParameters;


        private SubReportParametersTableModel(@NotNull ArrayList<SubReportParameter> subReportParameters)
        {
            //noinspection ConstantConditions
            if (subReportParameters == null)
            {
                throw new IllegalArgumentException("subReportParameters must not be null");
            }

            this.subReportParameters = subReportParameters;
        }


        @NotNull
        public ArrayList<SubReportParameter> getSubReportParameters()
        {
            return subReportParameters;
        }


        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return true;
        }


        @NotNull
        public Class<?> getColumnClass(int columnIndex)
        {
            return String.class;
        }


        @NotNull
        public String getColumnName(int columnIndex)
        {
            if (columnIndex == 0)
            {
                return TranslationManager.getInstance().getTranslation("R", "SubReportParameter.Key");
            }
            else if (columnIndex == 1)
            {
                return TranslationManager.getInstance().getTranslation("R", "SubReportParameter.Value");
            }
            return "ERROR";//NON-NLS
        }


        public int getColumnCount()
        {
            return 2;
        }


        public int getRowCount()
        {
            return subReportParameters.size();
        }


        @Nullable
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            if (columnIndex == 0)
            {
                return subReportParameters.get(rowIndex).getKey();
            }
            else if (columnIndex == 1)
            {
                return subReportParameters.get(rowIndex).getValue();
            }
            return null;
        }


        public void setValueAt(@NotNull Object aValue, int rowIndex, int columnIndex)
        {
            if (columnIndex == 0)
            {
                SubReportParameter subReportParameter = subReportParameters.remove(rowIndex);
                SubReportParameter newParameter = new SubReportParameter(aValue.toString(), subReportParameter.getValue());
                subReportParameters.add(rowIndex, newParameter);
            }
            else if (columnIndex == 1)
            {
                SubReportParameter subReportParameter = subReportParameters.remove(rowIndex);
                SubReportParameter newParameter = new SubReportParameter(subReportParameter.getKey(), aValue.toString());
                subReportParameters.add(rowIndex, newParameter);
            }
            fireTableCellUpdated(rowIndex, columnIndex);
        }


        public void addEntry(@NotNull SubReportParameter subReportParameter)
        {
            //noinspection ConstantConditions
            if (subReportParameter == null)
            {
                throw new IllegalArgumentException("subReportParameter must not be null");
            }
            int index = subReportParameters.size();
            subReportParameters.add(subReportParameter);
            fireTableRowsInserted(index, index);
        }


        public void removeEntry(int index)
        {
            subReportParameters.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

}
