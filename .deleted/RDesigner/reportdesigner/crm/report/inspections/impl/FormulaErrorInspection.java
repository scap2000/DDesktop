package org.pentaho.reportdesigner.crm.report.inspections.impl;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jfree.formula.Formula;
import org.jfree.formula.parser.ParseException;
import org.jfree.formula.parser.TokenMgrError;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.inspections.LocationInfo;
import org.pentaho.reportdesigner.crm.report.model.DrawableFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageURLFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 19:24:51
 */
public class FormulaErrorInspection extends AbstractElementTestInspection
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(FormulaErrorInspection.class.getName());


    protected void inspectElement(@NotNull ReportElement reportElement, @NotNull ArrayList<InspectionResult> inspectionResults)
    {
        if (reportElement instanceof TextFieldReportElement)
        {
            TextFieldReportElement textFieldReportElement = (TextFieldReportElement) reportElement;
            String formula = textFieldReportElement.getFormula().getText();
            checkFormula(formula, reportElement, inspectionResults);
        }
        else if (reportElement instanceof DrawableFieldReportElement)
        {
            DrawableFieldReportElement drawableFieldReportElement = (DrawableFieldReportElement) reportElement;
            String formula = drawableFieldReportElement.getFormula().getText();
            checkFormula(formula, reportElement, inspectionResults);
        }
        else if (reportElement instanceof ImageFieldReportElement)
        {
            ImageFieldReportElement imageFieldReportElement = (ImageFieldReportElement) reportElement;
            String formula = imageFieldReportElement.getFormula().getText();
            checkFormula(formula, reportElement, inspectionResults);
        }
        else if (reportElement instanceof ImageURLFieldReportElement)
        {
            ImageURLFieldReportElement imageFieldReportElement = (ImageURLFieldReportElement) reportElement;
            String formula = imageFieldReportElement.getFormula().getText();
            checkFormula(formula, reportElement, inspectionResults);
        }
        else if (reportElement instanceof MessageFieldReportElement)
        {
            MessageFieldReportElement messageFieldReportElement = (MessageFieldReportElement) reportElement;
            String formula = messageFieldReportElement.getFormula().getText();
            checkFormula(formula, reportElement, inspectionResults);
        }
    }


    private void checkFormula(@NotNull String formula, @NotNull ReportElement reportElement, @NotNull ArrayList<InspectionResult> inspectionResults)
    {
        if (!"".equals(formula))
        {
            if (hasError(formula))
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.FORMULA));
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "FormulaErrorInspection.Summary"),
                                                           TranslationManager.getInstance().getTranslation("R", "FormulaErrorInspection.Description"),
                                                           null, locationInfos));
            }
        }
    }


    private boolean hasError(@NotNull String formula)
    {
        //noinspection ErrorNotRethrown
        try
        {
            int i = formula.indexOf(':');
            if (i != -1)
            {
                formula = formula.substring(i + 1);
            }
            //noinspection ResultOfObjectAllocationIgnored
            new Formula(formula);
        }
        catch (TokenMgrError e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FormulaErrorInspection.hasError ", e);
            return true;
        }
        catch (ParseException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FormulaErrorInspection.hasError ", e);
            return true;
        }

        return false;
    }


}
