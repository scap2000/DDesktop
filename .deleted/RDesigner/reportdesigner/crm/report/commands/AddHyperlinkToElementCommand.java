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
import org.jfree.report.function.CreateHyperLinksFunction;
import org.jfree.report.function.TextFormatExpression;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.model.BandReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.EllipseReportElement;
import org.pentaho.reportdesigner.crm.report.model.LineReportElement;
import org.pentaho.reportdesigner.crm.report.model.RectangleReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.properties.editors.StringArrayChooser;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * User: Martin
 * Date: 08.08.2006
 * Time: 20:00:21
 */
public class AddHyperlinkToElementCommand extends AbstractCommand
{
    @NotNull
    private static final String[] EMPTY_STRING_ARRAY = new String[0];


    public AddHyperlinkToElementCommand()
    {
        super("AddHyperlinkToElementCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "AddHyperlinkToElementCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "AddHyperlinkToElementCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "AddHyperlinkToElementCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "AddHyperlinkToElementCommand.Text"));

        //getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getAddHyperlinkToElementIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "AddHyperlinkToElementCommand.Accelerator")));
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
            final CenterPanelDialog centerPanelDialog = CenterPanelDialog.createDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "AddHyperlinkToElementCommand.Text"), true);
            @NonNls
            FormLayout formLayout = new FormLayout("0dlu, max(30dlu;default), 4dlu, fill:default:grow, 4dlu, pref, 0dlu",
                                                   "0dlu, pref, " +
                                                   "4dlu, pref, " +
                                                   "4dlu, pref, " +
                                                   "4dlu, pref, " +
                                                   "4dlu, pref, " +
                                                   "0dlu");

            final JTextField elementNameTextField = new JTextField(element.getName());
            JLabel elementNameLabel = ComponentFactory.createLabel("R", "AddHyperlinkToElementDialog.ElementNameLabel", elementNameTextField);

            final JTextField functionHyperlinkNameTextField = new JTextField(TranslationManager.getInstance().getTranslation("R", "AddHyperlinkToElementDialog.FunctionHyperlinkNameTextField", element.getName()));
            JLabel functionHyperlinkNameLabel = ComponentFactory.createLabel("R", "AddHyperlinkToElementDialog.FunctionHyperlinkNameLabel", functionHyperlinkNameTextField);

            final JTextField functionTextFormatNameTextField = new JTextField(TranslationManager.getInstance().getTranslation("R", "AddHyperlinkToElementDialog.FunctionTextFormatNameTextField", element.getName()));
            JLabel functionTextFormatNameLabel = ComponentFactory.createLabel("R", "AddHyperlinkToElementDialog.FunctionTextFormatNameLabel", functionTextFormatNameTextField);

            final JTextField fieldsTextField = new JTextField();
            final JButton chooseFieldsButton = new JButton("...");
            chooseFieldsButton.setMargin(new Insets(1, 1, 1, 1));
            chooseFieldsButton.setDefaultCapable(false);
            final String[][] fields = new String[][]{EMPTY_STRING_ARRAY};
            if (element instanceof TextFieldReportElement)
            {
                TextFieldReportElement textFieldReportElement = (TextFieldReportElement) element;
                fields[0] = new String[]{textFieldReportElement.getFieldName()};
                fieldsTextField.setText("[" + fields[0][0] + "]");
            }
            fieldsTextField.setEditable(false);

            JLabel fieldLabel = ComponentFactory.createLabel("R", "AddHyperlinkToElementDialog.FieldLabel", chooseFieldsButton);

            final JTextField patternTextField = new JTextField("http://images.google.com/images?q={0}");//NON-NLS
            JLabel patternLabel = ComponentFactory.createLabel("R", "AddHyperlinkToElementDialog.PatternLabel", patternTextField);

            TextComponentHelper.installDefaultPopupMenu(elementNameTextField);
            UndoHelper.installUndoSupport(elementNameTextField);

            TextComponentHelper.installDefaultPopupMenu(functionHyperlinkNameTextField);
            UndoHelper.installUndoSupport(functionHyperlinkNameTextField);

            TextComponentHelper.installDefaultPopupMenu(functionTextFormatNameTextField);
            UndoHelper.installUndoSupport(functionTextFormatNameTextField);

            TextComponentHelper.installDefaultPopupMenu(patternTextField);
            UndoHelper.installUndoSupport(patternTextField);

            final boolean[] hyperlinkModified = new boolean[]{false};
            final boolean[] textFormatModified = new boolean[]{false};

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
                    if (!hyperlinkModified[0])
                    {
                        functionHyperlinkNameTextField.setText(TranslationManager.getInstance().getTranslation("R", "AddHyperlinkToElementDialog.FunctionHyperlinkNameTextField", elementNameTextField.getText()));
                        hyperlinkModified[0] = false;
                    }
                    if (!textFormatModified[0])
                    {
                        functionTextFormatNameTextField.setText(TranslationManager.getInstance().getTranslation("R", "AddHyperlinkToElementDialog.FunctionTextFormatNameTextField", elementNameTextField.getText()));
                        textFormatModified[0] = false;
                    }
                }
            });

            functionHyperlinkNameTextField.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(@NotNull DocumentEvent e)
                {
                    hyperlinkModified[0] = true;
                }


                public void removeUpdate(@NotNull DocumentEvent e)
                {
                    hyperlinkModified[0] = true;
                }


                public void changedUpdate(@NotNull DocumentEvent e)
                {
                    hyperlinkModified[0] = true;
                }
            });

            functionTextFormatNameTextField.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(@NotNull DocumentEvent e)
                {
                    textFormatModified[0] = true;
                }


                public void removeUpdate(@NotNull DocumentEvent e)
                {
                    textFormatModified[0] = true;
                }


                public void changedUpdate(@NotNull DocumentEvent e)
                {
                    textFormatModified[0] = true;
                }
            });

            chooseFieldsButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    fields[0] = StringArrayChooser.showStringArrayChooser(StringArrayChooser.Type.FIELD,
                                                                          report,
                                                                          chooseFieldsButton,
                                                                          TranslationManager.getInstance().getTranslation("R", "PropertyEditor.StringArray.Title"),
                                                                          TranslationManager.getInstance().getTranslation("R", "AddHyperlinkToElementDialog.LabelPrefix"),
                                                                          fields[0]);

                    StringBuilder sb = new StringBuilder("[");
                    for (int i = 0; i < fields[0].length; i++)
                    {
                        String s = fields[0][i];
                        sb.append(s);
                        if (i < fields[0].length - 1)
                        {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                    fieldsTextField.setText(sb.toString());
                }
            });

            JPanel panel = new JPanel(formLayout);

            @NonNls
            CellConstraints cc = new CellConstraints();
            panel.add(elementNameLabel, cc.xy(2, 2));
            panel.add(elementNameTextField, cc.xy(4, 2));

            panel.add(functionHyperlinkNameLabel, cc.xy(2, 4));
            panel.add(functionHyperlinkNameTextField, cc.xy(4, 4));

            panel.add(functionTextFormatNameLabel, cc.xy(2, 6));
            panel.add(functionTextFormatNameTextField, cc.xy(4, 6));

            panel.add(fieldLabel, cc.xy(2, 8));
            panel.add(fieldsTextField, cc.xy(4, 8));
            panel.add(chooseFieldsButton, cc.xy(6, 8));

            panel.add(patternLabel, cc.xy(2, 10));
            panel.add(patternTextField, cc.xy(4, 10));

            centerPanelDialog.setCenterPanel(panel);

            JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
            JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));

            okButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    TextFormatExpression textFormatExpression = new TextFormatExpression();
                    textFormatExpression.setName(functionTextFormatNameTextField.getText());
                    textFormatExpression.setField(fields[0]);
                    textFormatExpression.setPattern(patternTextField.getText());
                    textFormatExpression.setUrlEncodeValues(true);

                    CreateHyperLinksFunction createHyperLinksFunction = new CreateHyperLinksFunction();
                    createHyperLinksFunction.setName(functionHyperlinkNameTextField.getText());
                    createHyperLinksFunction.setField(textFormatExpression.getName());
                    createHyperLinksFunction.setElement(elementNameTextField.getText());

                    ReportFunctionElement textFormatElement = ExpressionRegistry.getInstance().createWrapperInstance(textFormatExpression);
                    ReportFunctionElement hyperlinkElement = ExpressionRegistry.getInstance().createWrapperInstance(createHyperLinksFunction);

                    Undo undo = reportDialog.getUndo();
                    ReportElementSelectionModel elementModel = reportDialog.getReportElementModel();

                    if (elementModel != null)
                    {
                        undo.startTransaction(UndoConstants.HIDE_ELEMENT);
                        element.setName(elementNameTextField.getText());
                        report.getReportFunctions().addChild(textFormatElement);
                        report.getReportFunctions().addChild(hyperlinkElement);
                        elementModel.setSelection(Arrays.asList(textFormatElement, hyperlinkElement));

                        undo.endTransaction();
                    }

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

}
