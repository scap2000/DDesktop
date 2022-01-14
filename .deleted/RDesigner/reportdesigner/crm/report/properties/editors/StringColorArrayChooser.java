package org.pentaho.reportdesigner.crm.report.properties.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.util.ColorHelper;
import org.pentaho.reportdesigner.crm.report.util.ColorIcon;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 11.01.2006
 * Time: 11:02:54
 */
public class StringColorArrayChooser
{
    private StringColorArrayChooser()
    {
    }


    @Nullable
    public static String[] showColorArrayChooser(@NotNull JComponent parent, @NotNull String title, @NotNull final String labelPrefix, @Nullable String[] colorArray)
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
        final FormLayout formLayout = new FormLayout("4dlu, pref, 4dlu, fill:default:grow, 4dlu, default, 4dlu, pref, 4dlu", "4dlu");
        final JPanel centerPanel = new JPanel(formLayout);

        @NonNls
        FormLayout fl = new FormLayout("4dlu, fill:pref:grow, 4dlu", "4dlu, fill:default, 4dlu, pref, 4dlu:grow");
        @NonNls
        final CellConstraints cc = new CellConstraints();
        final JPanel panel = new JPanel(fl);
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, cc.xy(2, 2));

        final ArrayList<Row> rows = new ArrayList<StringColorArrayChooser.Row>();

        JButton addButton = new JButton(TranslationManager.getInstance().getTranslation("R", "StringArrayChooser.AddButton"));
        panel.add(addButton, cc.xy(2, 4, "right, center"));

        if (colorArray != null)
        {
            for (int i = 0; i < colorArray.length; i++)
            {
                String color = colorArray[i];
                JLabel label = new JLabel(labelPrefix + "[" + i + "]");

                JTextField colorTextField = ComponentFactory.createTextField(true, true);
                StringColorButton stringColorButton = new StringColorButton(color);

                JButton removeButton = new JButton("-");
                removeButton.setMargin(new Insets(1, 1, 1, 1));
                final StringColorArrayChooser.Row row = new StringColorArrayChooser.Row(i, color, label, colorTextField, stringColorButton, removeButton);
                rows.add(row);
                removeButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        rows.remove(row);
                        rebuildPanel(rows, labelPrefix, centerPanel);

                        panel.revalidate();
                        panel.repaint();
                    }
                });
            }

            rebuildPanel(rows, labelPrefix, centerPanel);

            panel.revalidate();
            panel.repaint();
        }

        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                addRow(labelPrefix, rows, centerPanel, panel);
            }
        });

        if (colorArray == null || colorArray.length == 0)
        {
            addRow(labelPrefix, rows, centerPanel, panel);
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

        centerPanelDialog.setCenterPanel(panel);
        centerPanelDialog.pack();
        centerPanelDialog.setSize(300, 300);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return getValues(rows);
        }
        return colorArray;
    }


    private static void addRow(@NotNull final String labelPrefix, @NotNull final ArrayList<StringColorArrayChooser.Row> rows, @NotNull final JPanel centerPanel, @NotNull final JPanel panel)
    {
        JLabel label = new JLabel(labelPrefix);
        JTextField colorTextField = ComponentFactory.createTextField(true, true);
        StringColorButton stringColorButton = new StringColorButton("");

        JButton removeButton = new JButton("-");
        removeButton.setMargin(new Insets(1, 1, 1, 1));

        final StringColorArrayChooser.Row row = new StringColorArrayChooser.Row(rows.size(), "", label, colorTextField, stringColorButton, removeButton);
        rows.add(row);

        removeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                rows.remove(row);
                rebuildPanel(rows, labelPrefix, centerPanel);

                panel.revalidate();
                panel.repaint();
            }
        });
        rebuildPanel(rows, labelPrefix, centerPanel);

        stringColorButton.requestFocusInWindow();

        panel.revalidate();
        panel.repaint();
    }


    @NotNull
    private static String[] getValues(@NotNull ArrayList<StringColorArrayChooser.Row> rows)
    {
        String[] array = new String[rows.size()];
        for (int i = 0; i < rows.size(); i++)
        {
            StringColorArrayChooser.Row row = rows.get(i);
            array[i] = row.getColorTextField().getText();
        }

        return array;
    }


    private static void rebuildPanel(@NotNull ArrayList<StringColorArrayChooser.Row> rows, @NotNull String labelPrefix, @NotNull JPanel panel)
    {
        panel.removeAll();

        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, pref, 4dlu, fill:default:grow, 4dlu, default, 4dlu, pref, 4dlu", "4dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();

        panel.setLayout(formLayout);

        int currentRow = 1;
        for (int i = 0; i < rows.size(); i++)
        {
            StringColorArrayChooser.Row row = rows.get(i);

            formLayout.appendRow(new RowSpec("pref"));//NON-NLS
            currentRow++;
            row.setCurrentRow(i);
            row.getLabel().setText(labelPrefix + "[" + i + "]");
            panel.add(row.getLabel(), cc.xy(2, currentRow));
            panel.add(row.getColorTextField(), cc.xy(4, currentRow));
            panel.add(row.getColorButton(), cc.xy(6, currentRow));
            panel.add(row.getRemoveButton(), cc.xy(8, currentRow));
            formLayout.appendRow(new RowSpec("4dlu"));//NON-NLS
            currentRow++;
        }

        panel.revalidate();
        panel.repaint();
    }


    private static class Row
    {
        private int currentRow;
        @NotNull
        private String color;
        @NotNull
        private JLabel label;
        @NotNull
        private JTextField colorTextField;
        @NotNull
        private StringColorButton stringColorButton;
        @NotNull
        private JButton removeButton;


        private Row(int currentRow, @NotNull String color, @NotNull JLabel label, @NotNull final JTextField colorTextField, @NotNull final StringColorButton stringColorButton, @NotNull JButton removeButton)
        {
            this.currentRow = currentRow;
            this.color = color;
            this.label = label;
            this.colorTextField = colorTextField;
            this.stringColorButton = stringColorButton;
            this.removeButton = removeButton;

            colorTextField.setText(color);
            stringColorButton.setColor(color);

            stringColorButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    Color newColor = JColorChooser.showDialog(stringColorButton, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Color.Title"), ColorHelper.lookupColor(Row.this.color));
                    if (newColor != null)
                    {
                        Row.this.color = "#" + Integer.toHexString(newColor.getRGB() & 0x00FFFFFF).toUpperCase();
                        stringColorButton.setColor(Row.this.color);
                        colorTextField.setText(Row.this.color);
                    }
                }
            });

            colorTextField.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(@NotNull DocumentEvent e)
                {
                    update();
                }


                public void removeUpdate(@NotNull DocumentEvent e)
                {
                    update();
                }


                public void changedUpdate(@NotNull DocumentEvent e)
                {
                    update();
                }


                private void update()
                {
                    Row.this.color = colorTextField.getText();
                    stringColorButton.setColor(Row.this.color);
                }
            });
        }


        public int getCurrentRow()
        {
            return currentRow;
        }


        public void setCurrentRow(int currentRow)
        {
            this.currentRow = currentRow;
        }


        @NotNull
        public JTextField getColorTextField()
        {
            return colorTextField;
        }


        @NotNull
        public JLabel getLabel()
        {
            return label;
        }


        @NotNull
        public StringColorButton getColorButton()
        {
            return stringColorButton;
        }


        @NotNull
        public JButton getRemoveButton()
        {
            return removeButton;
        }

    }

    private static class StringColorButton extends JButton
    {


        private StringColorButton(@Nullable String stringColor)
        {
            setColor(stringColor);
        }


        public void setColor(@Nullable String stringColor)
        {
            Color color = ColorHelper.lookupColor(stringColor);
            if (color != null)
            {
                setIcon(new ColorIcon(Color.BLACK, color, 20, 16, true));
            }
            else
            {
                setIcon(new ColorIcon(Color.RED, new Color(0, 0, 0, 0), 20, 16, false));
            }
        }
    }
}