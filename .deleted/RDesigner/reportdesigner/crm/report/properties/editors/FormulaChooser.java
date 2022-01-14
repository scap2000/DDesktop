package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.NotNull;
import org.jfree.formula.parser.ParseException;
import org.jfree.formula.parser.TokenMgrError;
import org.pentaho.reportdesigner.crm.report.model.Formula;
import org.pentaho.reportdesigner.crm.report.properties.PropertyTable;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Martin
 * Date: 01.11.2005
 * Time: 10:23:10
 */
public class FormulaChooser
{

    private FormulaChooser()
    {
    }


    @NotNull
    public static Formula showFormulaChooser(@NotNull PropertyTable parent, @NotNull String title, @NotNull Formula origFormula)
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

        final JTextArea formulaTextArea = new JTextArea(origFormula.getText());
        final JLabel parseStatusLine = new JLabel(" ");
        parseStatusLine.setPreferredSize(new Dimension(300, parseStatusLine.getPreferredSize().height));
        parseStatusLine.setForeground(Color.RED);
        formulaTextArea.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(@NotNull DocumentEvent e)
            {
                updateFormulaTextArea(formulaTextArea, parseStatusLine);
            }


            public void removeUpdate(@NotNull DocumentEvent e)
            {
                updateFormulaTextArea(formulaTextArea, parseStatusLine);
            }


            public void changedUpdate(@NotNull DocumentEvent e)
            {
                updateFormulaTextArea(formulaTextArea, parseStatusLine);
            }
        });

        updateFormulaTextArea(formulaTextArea, parseStatusLine);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(formulaTextArea), BorderLayout.CENTER);
        centerPanel.add(parseStatusLine, BorderLayout.SOUTH);
        centerPanelDialog.setCenterPanel(centerPanel);

        centerPanelDialog.pack();
        GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 500, 400);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return new Formula(formulaTextArea.getText());
        }
        //return font;
        return origFormula;
    }


    private static void updateFormulaTextArea(@NotNull JTextArea formulaTextArea, @NotNull JLabel parseStatusLine)
    {
        //noinspection ErrorNotRethrown
        try
        {
            String s = formulaTextArea.getText();
            int i = s.indexOf(':');
            if (i != -1)
            {
                s = s.substring(i + 1);
            }

            //noinspection ResultOfObjectAllocationIgnored
            new org.jfree.formula.Formula(s);
            parseStatusLine.setText(" ");
        }
        catch (TokenMgrError e)
        {
            parseStatusLine.setText(e.getMessage());
        }
        catch (ParseException e)
        {
            parseStatusLine.setText(e.getMessage());
        }
    }

}
