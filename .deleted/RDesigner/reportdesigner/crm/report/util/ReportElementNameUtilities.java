package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 16.03.2007
 * Time: 18:48:47
 */
public class ReportElementNameUtilities
{
    private ReportElementNameUtilities()
    {
    }


    @NotNull
    public static HashSet<ElementNameDefinition> getElementNameDefinitions(@NotNull ReportElement reportElement)
    {
        //noinspection ConstantConditions
        if (reportElement == null)
        {
            throw new IllegalArgumentException("reportElement must not be null");
        }

        HashSet<ElementNameDefinition> hashSets = new HashSet<ElementNameDefinition>();
        collectNameDefinitions(reportElement, hashSets);
        return hashSets;
    }


    private static void collectNameDefinitions(@NotNull ReportElement reportElement, @NotNull HashSet<ElementNameDefinition> elementNameDefinitions)
    {
        elementNameDefinitions.add(new ElementNameDefinition(reportElement, reportElement.getName()));

        ArrayList<ReportElement> list = reportElement.getChildren();
        for (ReportElement element : list)
        {
            collectNameDefinitions(element, elementNameDefinitions);
        }
    }
}
