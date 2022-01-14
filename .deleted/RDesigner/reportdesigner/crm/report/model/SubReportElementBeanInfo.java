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
public class SubReportElementBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public SubReportElementBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor nameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NAME, PropertyKeys.GROUP_ID, false, 1, SubReportElement.class);
        GroupingPropertyDescriptor fieldNameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FILE_PATH, PropertyKeys.GROUP_APPEARANCE, true, 1, SubReportElement.class);
        GroupingPropertyDescriptor queryDescriptor = new GroupingPropertyDescriptor(PropertyKeys.QUERY, PropertyKeys.GROUP_APPEARANCE, false, 2, SubReportElement.class);
        GroupingPropertyDescriptor parametersDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PARAMETERS, PropertyKeys.GROUP_APPEARANCE, true, 3, SubReportElement.class);
        GroupingPropertyDescriptor positionContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.POSITION, PropertyKeys.GROUP_SPATIAL, false, 1, SubReportElement.class);
        GroupingPropertyDescriptor minimumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MINIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 2, SubReportElement.class);

        propertyDescriptors = new PropertyDescriptor[]{nameDescriptor, queryDescriptor, parametersDescriptor, fieldNameDescriptor, positionContentDescriptor, minimumSizeContentDescriptor};
    }


    @NotNull
    public BeanDescriptor getBeanDescriptor()
    {
        return new BeanDescriptor(SubReportElement.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}
