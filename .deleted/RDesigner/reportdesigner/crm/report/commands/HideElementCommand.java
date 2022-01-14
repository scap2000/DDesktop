/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.commands;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.function.HideElementByNameFunction;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.crm.report.model.BandReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.EllipseReportElement;
import org.pentaho.reportdesigner.crm.report.model.LineReportElement;
import org.pentaho.reportdesigner.crm.report.model.RectangleReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * User: Martin
 * Date: 08.08.2006
 * Time: 20:00:21
 */
public class HideElementCommand extends AbstractCommand
{
    public HideElementCommand()
    {
        super("HideElementCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "HideElementCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "HideElementCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "HideElementCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "HideElementCommand.Text"));

        //getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getHideElementIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "HideElementCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        event.getPresentation().setEnabled(reportDialog.getReport() != null && getSupported(reportElements) != null);
    }


    public void execute(@NotNull CommandEvent event)
    {
        final ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        final Report report = reportDialog.getReport();
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        final ReportElement element = getSupported(reportElements);

        if (report != null && element != null)
        {
            final CenterPanelDialog centerPanelDialog = CenterPanelDialog.createDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "HideElementCommand.Text"), true);
            @NonNls
            FormLayout formLayout = new FormLayout("0dlu, max(30dlu;default), 4dlu, fill:default:grow, 0dlu",
                                                   "0dlu, pref, " +
                                                   "4dlu, pref, " +
                                                   "4dlu, pref, " +
                                                   "0dlu");

            final JTextField elementNameTextField = new JTextField(element.getName());
            JLabel elementNameLabel = ComponentFactory.createLabel("R", "HideElementDialog.ElementNameLabel", elementNameTextField);

            final JTextField functionNameTextField = new JTextField(TranslationManager.getInstance().getTranslation("R", "HideElementDialog.FunctionNameTextField", element.getName()));
            JLabel functionNameLabel = ComponentFactory.createLabel("R", "HideElementDialog.FunctionNameLabel", functionNameTextField);


            TreeSet<String> fields = new TreeSet<String>(getDefinedFields(report));
            final JComboBox fieldComboBox = new JComboBox(fields.toArray(new String[fields.size()]));
            fieldComboBox.setEditable(true);
            fieldComboBox.setSelectedItem("");
            JLabel fieldLabel = ComponentFactory.createLabel("R", "HideElementDialog.FieldLabel", fieldComboBox);

            TextComponentHelper.installDefaultPopupMenu(elementNameTextField);
            UndoHelper.installUndoSupport(elementNameTextField);

            TextComponentHelper.installDefaultPopupMenu(functionNameTextField);
            UndoHelper.installUndoSupport(functionNameTextField);

            if (fieldComboBox.getEditor().getEditorComponent() instanceof JTextComponent)
            {
                JTextComponent editor = (JTextComponent) fieldComboBox.getEditor().getEditorComponent();
                TextComponentHelper.installDefaultPopupMenu(editor);
                UndoHelper.installUndoSupport(editor);
            }

            final boolean[] modified = new boolean[]{false};

            elementNameTextField.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(@NotNull DocumentEvent e)
                {
                    updateFunctionNameTextField();
                }


                public void removeUpdate(@NotNull DocumentEvent e)
                {
                    updateFunctionNameTextField();
                }


                public void changedUpdate(@NotNull DocumentEvent e)
                {
                    updateFunctionNameTextField();
                }


                private void updateFunctionNameTextField()
                {
                    if (!modified[0])
                    {
                        functionNameTextField.setText(TranslationManager.getInstance().getTranslation("R", "HideElementDialog.FunctionNameTextField", elementNameTextField.getText()));
                        modified[0] = false;
                    }
                }
            });

            functionNameTextField.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(@NotNull DocumentEvent e)
                {
                    modified[0] = true;
                }


                public void removeUpdate(@NotNull DocumentEvent e)
                {
                    modified[0] = true;
                }


                public void changedUpdate(@NotNull DocumentEvent e)
                {
                    modified[0] = true;
                }
            });

            JPanel panel = new JPanel(formLayout);

            @NonNls
            CellConstraints cc = new CellConstraints();
            panel.add(elementNameLabel, cc.xy(2, 2));
            panel.add(elementNameTextField, cc.xy(4, 2));

            panel.add(functionNameLabel, cc.xy(2, 4));
            panel.add(functionNameTextField, cc.xy(4, 4));

            panel.add(fieldLabel, cc.xy(2, 6));
            panel.add(fieldComboBox, cc.xy(4, 6));

            centerPanelDialog.setCenterPanel(panel);

            JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
            JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));

            okButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    HideElementByNameFunction hideElementByNameFunction = new HideElementByNameFunction();
                    hideElementByNameFunction.setName(functionNameTextField.getText());
                    Object o = fieldComboBox.getSelectedItem();
                    if (o != null)
                    {
                        hideElementByNameFunction.setField(o.toString());
                    }
                    hideElementByNameFunction.setElement(elementNameTextField.getText());
                    ReportFunctionElement reportFunctionElement = ExpressionRegistry.getInstance().createWrapperInstance(hideElementByNameFunction);

                    reportDialog.getUndo().startTransaction(UndoConstants.HIDE_ELEMENT);
                    element.setName(elementNameTextField.getText());
                    report.getReportFunctions().addChild(reportFunctionElement);
                    ReportElementSelectionModel elementModel = reportDialog.getReportElementModel();
                    if (elementModel != null)
                    {
                        elementModel.setSelection(Arrays.asList(reportFunctionElement));
                    }
                    reportDialog.getUndo().endTransaction();

                    centerPanelDialog.dispose();
                }
            });

            cancelButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    centerPanelDialog.dispose();
                }
            });

            centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

            centerPanelDialog.pack();
            GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 400, 200);
            WindowUtils.setLocationRelativeTo(centerPanelDialog, reportDialog);
            centerPanelDialog.setVisible(true);
        }
    }


    @Nullable
    private ReportElement getSupported(@Nullable ReportElement[] reportElements)
    {
        if (reportElements != null && reportElements.length == 1 && reportElements[0] != null)
        {
            if (reportElements[0] instanceof BandReportElement ||
                reportElements[0] instanceof BandToplevelReportElement ||
                reportElements[0] instanceof EllipseReportElement ||
                reportElements[0] instanceof LineReportElement ||
                reportElements[0] instanceof StaticImageReportElement ||
                reportElements[0] instanceof RectangleReportElement ||
                reportElements[0] instanceof TextReportElement)
            {
                return reportElements[0];
            }
        }
        return null;
    }


    @NotNull
    private HashSet<String> getDefinedFields(@NotNull ReportElement reportElement)
    {
        HashSet<String> definedFields;
        if (reportElement instanceof DataSetReportElement)
        {
            DataSetReportElement dataSetReportElement = (DataSetReportElement) reportElement;
            definedFields = dataSetReportElement.getDefinedFields();
        }
        else
        {
            definedFields = new HashSet<String>();
        }

        for (ReportElement child : reportElement.getChildren())
        {
            definedFields.addAll(getDefinedFields(child));
        }
        return definedFields;
    }
}
