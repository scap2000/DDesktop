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
package org.pentaho.reportdesigner.crm.report.model.functions;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.function.Expression;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 27.07.2006
 * Time: 09:56:42
 */
public class ExpressionRegistry
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ExpressionRegistry.class.getName());
    @NotNull
    private static final String FUNCTIONS_FILENAME = "./resources/functions.txt";


    public static void main(@NotNull String[] args)
    {
        ExpressionRegistry instance = ExpressionRegistry.getInstance();
        for (Class<?> jFreeReportExpressionClass : instance.getJFreeReportExpressionToWrapperClassesMap().keySet())
        {
            Class<?> wrapperClass = instance.getJFreeReportExpressionToWrapperClassesMap().get(jFreeReportExpressionClass);
            //noinspection UseOfSystemOutOrSystemErr
            System.out.println(jFreeReportExpressionClass.getName() + " <---> " + wrapperClass.getName());
        }
    }


    @NotNull
    private static final HashSet<String> exludedExpressionProperties;


    static
    {
        exludedExpressionProperties = new HashSet<String>();
        exludedExpressionProperties.add("value");//NON-NLS
        exludedExpressionProperties.add("active");//NON-NLS
        exludedExpressionProperties.add("dataRow");//NON-NLS
        exludedExpressionProperties.add("instance");//NON-NLS
        exludedExpressionProperties.add("cacheKey");//NON-NLS
        exludedExpressionProperties.add("runtime");//NON-NLS
    }


    @NotNull
    private static final ExpressionRegistry registry = new ExpressionRegistry();


    @NotNull
    public static ExpressionRegistry getInstance()
    {
        return registry;
    }


    @NotNull
    private HashMap<String, String> jFreeReportExpressionToTreePathMap;
    @NotNull
    private HashMap<Class<?>, Class<?>> jFreeReportExpressionToWrapperClassesMap;
    @NotNull
    private HashMap<Class<?>, Class<?>> wrapperToJFreeReportExpressionClassesMap;
    @NotNull
    private HashMap<String, Class<?>> simpleNameToWrapperClassesMap;

    @NotNull
    private HashMap<String, PropertyInfo> propertyInfos;


    private ExpressionRegistry()
    {
        //MARKED we should directly access JFreeReports function registry as soon as available

        jFreeReportExpressionToTreePathMap = new HashMap<String, String>();
        jFreeReportExpressionToWrapperClassesMap = new HashMap<Class<?>, Class<?>>();
        simpleNameToWrapperClassesMap = new HashMap<String, Class<?>>();

        propertyInfos = new HashMap<String, PropertyInfo>();
        AdditionalPropertyInfos.initExternalPropertyInfos(propertyInfos);
        AdditionalPropertyInfos.initTreePathInfos(jFreeReportExpressionToTreePathMap);

        generateAndAddClassIfPossible("org.jfree.report.function.AverageExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ColumnAverageExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ColumnDifferenceExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ColumnDivisionExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ColumnMaximumExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ColumnMinimumExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ColumnMultiplyExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ColumnSumExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.CompareFieldsExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ConvertToDateExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ConvertToNumberExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.CountDistinctFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.CreateGroupAnchorsFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.CreateHyperLinksFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.DateCutExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ElementColorFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.ElementTrafficLightFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.ElementVisibilityFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.ElementVisibilitySwitchFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.EventMonitorFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.GroupCountFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.HideElementByNameFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.HideElementIfDataAvailableExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.HideNullValuesFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.HidePageBandForTableExportFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.IsEmptyExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.IsNullExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ItemAvgFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.ItemColumnQuotientExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ItemCountFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.ItemHideFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.ItemMaxFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.ItemMinFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.ItemPercentageFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.ItemSumFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.NegativeNumberPaintChangeFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.PageFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.PageItemCountFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.PageItemSumFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.PageOfPagesFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.PageTotalFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.PaintComponentFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.PaintDynamicComponentFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.PercentageExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.ShowElementByNameFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.ShowElementIfDataAvailableExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.TextFormatExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.TotalCalculationFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.TotalGroupCountFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.TotalGroupSumFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.TotalGroupSumQuotientFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.TotalGroupSumQuotientPercentFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.TotalItemCountFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.TriggerPageFooterFunction");
        generateAndAddClassIfPossible("org.jfree.report.function.bool.AndExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.bool.IsEmptyDataExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.bool.OrExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.date.CompareDateExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.date.DateExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.date.DateSpanExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.date.VariableDateExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.numeric.CompareNumberExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.numeric.IsNegativeExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.numeric.IsPositiveExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.CapitalizeStringExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.CompareStringExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.MapIndirectExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.MapStringExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.ResourceBundleLookupExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.ResourceMesssageFormatExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.SubStringExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.ToLowerCaseStringExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.ToUpperCaseStringExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.TokenizeStringExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.strings.URLEncodeExpression");
        generateAndAddClassIfPossible("org.jfree.report.function.sys.IsExportTypeExpression");
        generateAndAddClassIfPossible("org.jfree.report.modules.misc.beanshell.BSHExpression");
        //generateAndAddClassIfPossible("org.jfree.report.modules.misc.bsf.BSFExpression");
        generateAndAddClassIfPossible("org.jfree.report.modules.misc.survey.SurveyScaleExpression");

        generateAndAddClassIfPossible("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression");
        generateAndAddClassIfPossible("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression");
        generateAndAddClassIfPossible("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction");
        generateAndAddClassIfPossible("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression");
        generateAndAddClassIfPossible("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression");
        generateAndAddClassIfPossible("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression");
        generateAndAddClassIfPossible("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction");
        generateAndAddClassIfPossible("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression");
        generateAndAddClassIfPossible("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions");


        List<String> functionList = readFunctions();
        for (String function : functionList)
        {
            generateAndAddClassIfPossible(function.trim());
        }


        wrapperToJFreeReportExpressionClassesMap = new HashMap<Class<?>, Class<?>>();
        for (Class<?> jFreeClass : jFreeReportExpressionToWrapperClassesMap.keySet())
        {
            @Nullable
            Class<?> wrapperClass = jFreeReportExpressionToWrapperClassesMap.get(jFreeClass);
            wrapperToJFreeReportExpressionClassesMap.put(wrapperClass, jFreeClass);
        }
    }


    @NotNull
    private List<String> readFunctions()
    {
        List<String> functionList = new LinkedList<String>();

        FileInputStream fis = null;
        try
        {
            //noinspection IOResourceOpenedButNotSafelyClosed
            fis = new FileInputStream(FUNCTIONS_FILENAME);
            StringBuilder sb = new StringBuilder(1000);
            byte[] bytes = new byte[1024];
            int numRead;
            while ((numRead = fis.read(bytes)) != -1)
            {
                sb.append(new String(bytes, 0, numRead));
            }

            StringTokenizer st = new StringTokenizer(sb.toString(), ", ");
            while (st.hasMoreTokens())
            {
                String function = st.nextToken();
                functionList.add(function);
            }
        }
        catch (Exception e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExpressionRegistry.readFunctions ", e);
        }
        finally
        {
            IOUtil.closeStream(fis);
        }

        return functionList;
    }


    private void generateAndAddClassIfPossible(@NotNull String jFreeReportClassName)
    {
        try
        {
            Class<?> jFreeReportClass = Class.forName(jFreeReportClassName);
            if (!Expression.class.isAssignableFrom(jFreeReportClass))
            {
                return;
            }
            if (Modifier.isAbstract(jFreeReportClass.getModifiers()))
            {
                return;
            }
            if (!Modifier.isPublic(jFreeReportClass.getModifiers()))
            {
                return;
            }

            BeanInfo beanInfo = Introspector.getBeanInfo(jFreeReportClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            //sort alphabetically, the file name (from superclass) will be the first.
            Arrays.sort(propertyDescriptors, new Comparator<PropertyDescriptor>()
            {
                public int compare(@NotNull PropertyDescriptor o1, @NotNull PropertyDescriptor o2)
                {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            FunctionGenerator functionGenerator = new FunctionGenerator(FunctionGenerator.PACKAGE_PREFIX + jFreeReportClassName + "_DesignerWrapper");//NON-NLS
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
            {
                if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null && !exludedExpressionProperties.contains(propertyDescriptor.getName()))
                {
                    functionGenerator.addProperty(createProperty(jFreeReportClassName, propertyDescriptor));
                }
            }
            Class<ReportFunctionElement> wrapperClass = FunctionGenerator.defineClass(functionGenerator);
            jFreeReportExpressionToWrapperClassesMap.put(jFreeReportClass, wrapperClass);

            simpleNameToWrapperClassesMap.put(jFreeReportClass.getSimpleName(), wrapperClass);
        }
        catch (Throwable e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExpressionRegistry.generateAndAddClassIfPossible jFreeReportClassName = " + jFreeReportClassName + " is missing");
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExpressionRegistry.generateAndAddClassIfPossible ", e);
        }
    }


    @NotNull
    public HashMap<Class<?>, Class<?>> getJFreeReportExpressionToWrapperClassesMap()
    {
        return jFreeReportExpressionToWrapperClassesMap;
    }


    @NotNull
    public String getTreePath(@NotNull Class<?> jFreeClass)
    {
        String path = jFreeReportExpressionToTreePathMap.get(jFreeClass.getName());
        if (path != null)
        {
            return path;
        }
        else
        {
            return "";
        }
    }


    @NotNull
    public HashMap<Class<?>, Class<?>> getWrapperToJFreeReportExpressionClassesMap()
    {
        return wrapperToJFreeReportExpressionClassesMap;
    }


    @Nullable
    public ReportFunctionElement createWrapperInstance(@NotNull String className)
    {
        try
        {
            Class<?> wrapperClass = Class.forName(className);
            @Nullable
            Class<?> jFreeClass = wrapperToJFreeReportExpressionClassesMap.get(wrapperClass);

            ReportFunctionElement wrapperInstance = (ReportFunctionElement) wrapperClass.newInstance();
            if (jFreeClass == null)
            {
                return null;
            }
            Expression jFreeExpression = (Expression) jFreeClass.newInstance();

            fillWrapper(wrapperInstance, jFreeExpression);

            return wrapperInstance;
        }
        catch (Throwable e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExpressionRegistry.createWrapperInstance ", e);
            return null;
        }
    }


    @NotNull
    public ReportFunctionElement createWrapperInstance(@NotNull Expression jFreeExpression)
    {

        Class<?> jFreeClass = jFreeExpression.getClass();
        @Nullable
        Class<?> wrapperClass = jFreeReportExpressionToWrapperClassesMap.get(jFreeClass);

        if (wrapperClass == null)
        {
            wrapperClass = getWrapperClassForOldFunction(jFreeClass.getSimpleName());

            if (wrapperClass == null)
            {
                throw new RuntimeException("Could not create expression instance for class " + jFreeExpression.getClass().getName());
            }
        }
        try
        {
            ReportFunctionElement wrapperInstance = (ReportFunctionElement) wrapperClass.newInstance();

            fillWrapper(wrapperInstance, jFreeExpression);

            return wrapperInstance;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not create expression instance for class " + jFreeExpression.getClass().getName(), e);
        }
    }


    public void fillWrapper(@NotNull ReportFunctionElement wrapperInstance, @NotNull Expression jFreeExpression) throws IntrospectionException, IllegalAccessException, InvocationTargetException
    {
        //copy values from jFreeExpression to wrapper (to not loose initialisations)
        BeanInfo beanInfo = Introspector.getBeanInfo(jFreeExpression.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
        {
            if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null && !"active".equals(propertyDescriptor.getName()))//NON-NLS
            {
                try
                {
                    Method method = wrapperInstance.getClass().getMethod(propertyDescriptor.getWriteMethod().getName(), propertyDescriptor.getWriteMethod().getParameterTypes()[0]);
                    Object args = propertyDescriptor.getReadMethod().invoke(jFreeExpression);
                    method.invoke(wrapperInstance, args);
                }
                catch (NoSuchMethodException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExpressionCreator.createReportFunctionElement ", e);
                    //shit happens
                }
            }
        }
    }


    @NotNull
    public Expression createJFreeReportExpressionInstance(@NotNull ReportFunctionElement reportFunctionElement) throws ReportCreationException
    {
        try
        {
            @Nullable
            Class<?> freeClass = wrapperToJFreeReportExpressionClassesMap.get(reportFunctionElement.getClass());

            if (freeClass != null)
            {
                Expression obj = (Expression) freeClass.newInstance();
                BeanInfo beanInfo = Introspector.getBeanInfo(reportFunctionElement.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
                {
                    if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null)
                    {
                        Class<?> parameterTypes = propertyDescriptor.getWriteMethod().getParameterTypes()[0];
                        Method method = obj.getClass().getMethod(propertyDescriptor.getWriteMethod().getName(), parameterTypes);
                        method.invoke(obj, propertyDescriptor.getReadMethod().invoke(reportFunctionElement));
                    }
                }

                return obj;
            }
        }
        catch (Exception e)
        {
            throw new ReportCreationException("Could not create expression", reportFunctionElement, e);
        }
        throw new ReportCreationException("Could not create expression freeClass not found", reportFunctionElement);
    }


    @NotNull
    public String getLocalizedExpressionName(@NotNull Class<?> clazz)
    {
        String key1 = "Expressions." + clazz.getSimpleName() + ".Name";//NON-NLS
        String functionName1 = TranslationManager.getInstance().getTranslation("R", key1);
        if (key1.equals(functionName1))
        {
            functionName1 = clazz.getSimpleName();
        }
        return functionName1;
    }


    @NotNull
    public String getLocalizedDescription(@NotNull Class<?> clazz)
    {
        String key1 = "Expressions." + clazz.getSimpleName() + ".Description";//NON-NLS
        String functionName1 = TranslationManager.getInstance().getTranslation("R", key1);
        if (key1.equals(functionName1))
        {
            return "";
        }
        return functionName1;
    }


    @Nullable
    public Class<?> getWrapperClassForOldFunction(@NotNull String simpleClassName)
    {
        return simpleNameToWrapperClassesMap.get(simpleClassName);
    }


    @NotNull
    private Property createProperty(@NotNull String jFreeReportClassName, @NotNull PropertyDescriptor propertyDescriptor)
    {
        PropertyInfo propertyInfo = propertyInfos.get(jFreeReportClassName + "." + propertyDescriptor.getName());
        if (propertyInfo != null)
        {
            return new Property(propertyDescriptor.getPropertyType(), propertyDescriptor.getName(), propertyInfo.getGroup(), propertyInfo.getSortingID());
        }
        return new Property(propertyDescriptor.getPropertyType(), propertyDescriptor.getName(), PropertyKeys.GROUP_UNKNOWN, -1);
    }
}
