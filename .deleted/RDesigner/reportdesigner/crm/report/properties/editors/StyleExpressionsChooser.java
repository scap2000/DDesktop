package org.pentaho.reportdesigner.crm.report.properties.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jfree.report.style.StyleKey;
import org.pentaho.reportdesigner.crm.report.model.StyleExpression;
import org.pentaho.reportdesigner.crm.report.model.StyleExpressions;
import org.pentaho.reportdesigner.crm.report.properties.PropertyTable;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 01.11.2005
 * Time: 10:23:10
 */
public class StyleExpressionsChooser
{

    private StyleExpressionsChooser()
    {
    }


    @NotNull
    public static StyleExpressions showStyleExpressionsChooser(@NotNull PropertyTable parent, @NotNull String title, @NotNull StyleExpressions origStyleExpressions)
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

        StyleExpressionsPanel centerPanel = new StyleExpressionsPanel(origStyleExpressions);

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.pack();
        GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 500, 400);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return centerPanel.getStyleExpressions();
        }
        //return font;
        return origStyleExpressions;
    }


    private static class StyleExpressionsPanel extends JPanel
    {
        @NotNull
        private StyleExpressions styleExpressions;


        private StyleExpressionsPanel(@NotNull StyleExpressions origStyleExpressions)
        {
            this.styleExpressions = new StyleExpressions(origStyleExpressions);

            FormLayout formLayout = new FormLayout("4dlu, fill:default:grow, 4dlu", "4dlu, pref, 4dlu, fill:default:grow, 4dlu");//NON-NLS
            @NonNls
            CellConstraints cc = new CellConstraints();

            setLayout(formLayout);

            JPanel buttonPanel = new JPanel(new GridLayout(1, 0));

            JButton addButton = new JButton(TranslationManager.getInstance().getTranslation("R", "StyleExpressionsChooser.AddButton"));
            final JButton removeButton = new JButton(TranslationManager.getInstance().getTranslation("R", "StyleExpressionsChooser.RemoveButton"));
            removeButton.setEnabled(false);

            buttonPanel.add(addButton);
            buttonPanel.add(removeButton);

            add(buttonPanel, cc.xy(2, 2, "left, fill"));

            final DefaultListModel listModel = new DefaultListModel();
            for (StyleExpression styleExpression : styleExpressions.getStyleExpressions())
            {
                listModel.addElement(styleExpression);
            }

            final JList styleKeyList = new JList(listModel);
            styleKeyList.setPrototypeCellValue(new StyleExpression("prototype cell value", ""));//NON-NLS

            final JTextArea expressionTextArea = new JTextArea();
            expressionTextArea.setEnabled(false);
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(styleKeyList), new JScrollPane(expressionTextArea));

            add(splitPane, cc.xy(2, 4));

            styleKeyList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(@NotNull ListSelectionEvent e)
                {
                    if (!e.getValueIsAdjusting())
                    {
                        int index = styleKeyList.getSelectedIndex();
                        if (index != -1)
                        {
                            expressionTextArea.setEnabled(true);
                            removeButton.setEnabled(true);

                            StyleExpression styleExpression = (StyleExpression) listModel.get(index);
                            expressionTextArea.setText(styleExpression.getExpression());
                        }
                        else
                        {
                            expressionTextArea.setEnabled(false);
                            removeButton.setEnabled(false);

                            expressionTextArea.setText("");
                        }
                    }
                }
            });

            TextComponentHelper.installDefaultPopupMenu(expressionTextArea);
            UndoHelper.installUndoSupport(expressionTextArea);

            expressionTextArea.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(@NotNull DocumentEvent e)
                {
                    updateStyleExpressions(styleKeyList, listModel, expressionTextArea);
                }


                public void removeUpdate(@NotNull DocumentEvent e)
                {
                    updateStyleExpressions(styleKeyList, listModel, expressionTextArea);
                }


                public void changedUpdate(@NotNull DocumentEvent e)
                {
                    updateStyleExpressions(styleKeyList, listModel, expressionTextArea);
                }
            });


            addButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    LinkedHashSet<String> possibleKeys = new LinkedHashSet<String>();
                    StyleKey[] styleKeys = StyleKey.getDefinedStyleKeys();
                    for (StyleKey styleKey : styleKeys)
                    {
                        possibleKeys.add(styleKey.getName());
                    }
                    for (StyleExpression styleExpression : styleExpressions.getStyleExpressions())
                    {
                        possibleKeys.remove(styleExpression.getStyleKeyName());
                    }
                    String[] strings = possibleKeys.toArray(new String[possibleKeys.size()]);
                    Collator collator = Collator.getInstance();
                    collator.setStrength(Collator.PRIMARY);
                    Arrays.sort(strings, collator);

                    String s = ArrayElementChooser.showOneElementChooser(StyleExpressionsPanel.this, strings, TranslationManager.getInstance().getTranslation("R", "StyleExpressionChooser.StyleKeyChooser.Title"));
                    if (s != null)
                    {
                        StyleExpression styleExpression = new StyleExpression(s, "");
                        styleExpressions = styleExpressions.addStyleExpression(styleExpression);

                        int newIndex = listModel.getSize();
                        listModel.add(newIndex, styleExpression);
                        styleKeyList.setSelectedIndex(newIndex);
                    }
                }
            });

            removeButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    StyleExpression styleExpression = (StyleExpression) listModel.get(styleKeyList.getSelectedIndex());
                    styleExpressions = styleExpressions.removeStyleExpression(styleExpression);
                    listModel.removeElement(styleExpression);
                }
            });
        }


        private void updateStyleExpressions(@NotNull JList styleKeyList, @NotNull DefaultListModel listModel, @NotNull JTextArea expressionTextArea)
        {
            int index = styleKeyList.getSelectedIndex();
            if (index != -1)
            {
                StyleExpression styleExpression = (StyleExpression) listModel.get(index);
                styleExpressions = styleExpressions.removeStyleExpression(styleExpression);

                StyleExpression newStyleExpression = new StyleExpression(styleExpression.getStyleKeyName(), expressionTextArea.getText());
                styleExpressions = styleExpressions.addStyleExpression(newStyleExpression);
                listModel.set(index, newStyleExpression);
            }
        }


        @NotNull
        public StyleExpressions getStyleExpressions()
        {
            return styleExpressions;
        }
    }

}
