package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NotNull;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 26.01.2007
 * Time: 12:57:50
 */
@SuppressWarnings({"ALL"})
public class ChartPropertyLister
{
    @NotNull
    private static final HashSet<String> exludedExpressionProperties;


    static
    {
        exludedExpressionProperties = new HashSet<String>();
        exludedExpressionProperties.add("value");//NON-NLS
        exludedExpressionProperties.add("active");//NON-NLS
        exludedExpressionProperties.add("dataRow");//NON-NLS
        exludedExpressionProperties.add("instance");//NON-NLS
    }


    public static void main(@NotNull String[] args) throws ClassNotFoundException, IntrospectionException
    {
        listProperties("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression");
        listProperties("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression");
        listProperties("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction");
        listProperties("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression");
        listProperties("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression");
        listProperties("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression");
        listProperties("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction");
        listProperties("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression");
        listProperties("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions");
    }


    private static void listProperties(@NotNull String classname) throws ClassNotFoundException, IntrospectionException
    {
        BeanInfo beanInfo = Introspector.getBeanInfo(Class.forName(classname));
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        //sort alphabetically, the file name (from superclass) will be the first.
        Arrays.sort(propertyDescriptors, new Comparator<PropertyDescriptor>()
        {
            public int compare(@NotNull PropertyDescriptor o1, @NotNull PropertyDescriptor o2)
            {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
        {
            if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null && !exludedExpressionProperties.contains(propertyDescriptor.getName()))
            {
                System.out.println("propertyInfos.put(\"" + classname + "." + propertyDescriptor.getName() + "\", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, -1));//NON-NLS");
            }
        }

        System.out.println();
    }
}
