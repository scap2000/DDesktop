package org.pentaho.reportdesigner.crm.report.model;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 07:11:26
 */
public class SubReportBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public SubReportBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor nameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NAME, PropertyKeys.GROUP_ID, false, 1, SubReport.class);

        GroupingPropertyDescriptor pageDefinitionDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PAGE_DEFINITION, PropertyKeys.GROUP_APPEARANCE, false, 1, SubReport.class);
        GroupingPropertyDescriptor defaultLocaleDescriptor = new GroupingPropertyDescriptor(PropertyKeys.DEFAULT_LOCALE, PropertyKeys.GROUP_APPEARANCE, false, 2, SubReport.class);
        GroupingPropertyDescriptor resourceBundleClasspathDescriptor = new GroupingPropertyDescriptor(PropertyKeys.RESOURCE_BUNDLE_CLASSPATH, PropertyKeys.GROUP_APPEARANCE, false, 3, SubReport.class);
        GroupingPropertyDescriptor useMaxCharBoundsDescriptor = new GroupingPropertyDescriptor(PropertyKeys.USE_MAX_CHAR_BOUNDS, PropertyKeys.GROUP_APPEARANCE, false, 4, SubReport.class);
        GroupingPropertyDescriptor queryDescriptor = new GroupingPropertyDescriptor(PropertyKeys.QUERY, PropertyKeys.GROUP_APPEARANCE, true, 5, SubReport.class);
        GroupingPropertyDescriptor parametersDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PARAMETERS, PropertyKeys.GROUP_APPEARANCE, true, 6, SubReport.class);

        propertyDescriptors = new PropertyDescriptor[]{nameDescriptor,
                                                       pageDefinitionDescriptor,
                                                       defaultLocaleDescriptor,
                                                       resourceBundleClasspathDescriptor,
                                                       queryDescriptor,
                                                       useMaxCharBoundsDescriptor,
                                                       parametersDescriptor
        };
    }


    @NotNull
    public BeanDescriptor getBeanDescriptor()
    {
        return new BeanDescriptor(SubReport.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}
