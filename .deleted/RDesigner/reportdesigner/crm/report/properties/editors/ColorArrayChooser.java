package org.pentaho.reportdesigner.crm.report.properties.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.util.ColorIcon;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 11.01.2006
 * Time: 11:02:54
 */
public class ColorArrayChooser
{
    private ColorArrayChooser()
    {
    }


    @Nullable
    public static Color[] showColorArrayChooser(@NotNull JComponent parent, @NotNull String title, @NotNull final String labelPrefix, @Nullable Color[] colorArray)
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
        final FormLayout formLayout = new FormLayout("4dlu, pref, 4dlu, fill:default:grow, 4dlu, pref, 4dlu", "4dlu");
        final JPanel centerPanel = new JPanel(formLayout);

        @NonNls
        FormLayout fl = new FormLayout("4dlu, fill:pref:grow, 4dlu", "4dlu, fill:default, 4dlu, pref, 4dlu:grow");
        @NonNls
        final CellConstraints cc = new CellConstraints();
        final JPanel panel = new JPanel(fl);
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, cc.xy(2, 2));

        final ArrayList<Row> rows = new ArrayList<ColorArrayChooser.Row>();

        JButton addButton = new JButton(TranslationManager.getInstance().getTranslation("R", "StringArrayChooser.AddButton"));
        panel.add(addButton, cc.xy(2, 4, "right, center"));

        if (colorArray != null)
        {
            for (int i = 0; i < colorArray.length; i++)
            {
                Color color = colorArray[i];
                JLabel label = new JLabel(labelPrefix + "[" + i + "]");
                ColorButton colorButton = new ColorButton(color);

                JButton removeButton = new JButton("-");
                removeButton.setMargin(new Insets(1, 1, 1, 1));
                final ColorArrayChooser.Row row = new ColorArrayChooser.Row(i, label, colorButton, removeButton);
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


    private static void addRow(@NotNull final String labelPrefix, @NotNull final ArrayList<ColorArrayChooser.Row> rows, @NotNull final JPanel centerPanel, @NotNull final JPanel panel)
    {
        JLabel label = new JLabel(labelPrefix);
        ColorButton colorButton = new ColorButton(Color.WHITE);

        JButton removeButton = new JButton("-");
        removeButton.setMargin(new Insets(1, 1, 1, 1));

        final ColorArrayChooser.Row row = new ColorArrayChooser.Row(rows.size(), label, colorButton, removeButton);
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

        colorButton.requestFocusInWindow();

        panel.revalidate();
        panel.repaint();
    }


    @NotNull
    private static Color[] getValues(@NotNull ArrayList<ColorArrayChooser.Row> rows)
    {
        Color[] array = new Color[rows.size()];
        for (int i = 0; i < rows.size(); i++)
        {
            ColorArrayChooser.Row row = rows.get(i);
            array[i] = row.getColorButton().getColor();
        }

        return array;
    }


    private static void rebuildPanel(@NotNull ArrayList<ColorArrayChooser.Row> rows, @NotNull String labelPrefix, @NotNull JPanel panel)
    {
        panel.removeAll();

        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, pref, 4dlu, fill:default:grow, 4dlu, pref, 4dlu", "4dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();

        panel.setLayout(formLayout);

        int currentRow = 1;
        for (int i = 0; i < rows.size(); i++)
        {
            ColorArrayChooser.Row row = rows.get(i);

            formLayout.appendRow(new RowSpec("pref"));//NON-NLS
            currentRow++;
            row.setCurrentRow(i);
            row.getLabel().setText(labelPrefix + "[" + i + "]");
            panel.add(row.getLabel(), cc.xy(2, currentRow));
            panel.add(row.getColorButton(), cc.xy(4, currentRow));
            panel.add(row.getRemoveButton(), cc.xy(6, currentRow));
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
        private JLabel label;
        @NotNull
        private ColorButton colorButton;
        @NotNull
        private JButton removeButton;


        private Row(int currentRow, @NotNull JLabel label, @NotNull ColorButton colorButton, @NotNull JButton removeButton)
        {
            this.currentRow = currentRow;
            this.label = label;
            this.colorButton = colorButton;
            this.removeButton = removeButton;
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
        public JLabel getLabel()
        {
            return label;
        }


        @NotNull
        public ColorButton getColorButton()
        {
            return colorButton;
        }


        @NotNull
        public JButton getRemoveButton()
        {
            return removeButton;
        }

    }

    private static class ColorButton extends JButton
    {
        @NotNull
        private Color color;


        private ColorButton(@NotNull Color color)
        {
            //noinspection ConstantConditions
            if (color == null)
            {
                throw new IllegalArgumentException("color must not be null");
            }

            this.color = color;
            setColor(color);

            addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    Color newColor = JColorChooser.showDialog(ColorButton.this, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Color.Title"), ColorButton.this.color);
                    if (newColor != null)
                    {
                        setColor(newColor);
                    }
                }
            });
        }


        @NotNull
        public Color getColor()
        {
            return color;
        }


        public void setColor(@NotNull Color color)
        {
            this.color = color;
            setIcon(new ColorIcon(Color.BLACK, color, 100, 16, true));
        }
    }
}
