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
public class ImageURLFieldReportElementBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public ImageURLFieldReportElementBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor nameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NAME, PropertyKeys.GROUP_ID, false, 1, ImageURLFieldReportElement.class);

        GroupingPropertyDescriptor positionContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.POSITION, PropertyKeys.GROUP_SPATIAL, false, 1, ImageURLFieldReportElement.class);
        GroupingPropertyDescriptor minimumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MINIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 2, ImageURLFieldReportElement.class);
        GroupingPropertyDescriptor maximumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MAXIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 3, ImageURLFieldReportElement.class);

        GroupingPropertyDescriptor fieldNameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FIELD_NAME, PropertyKeys.GROUP_APPEARANCE, true, 1, ImageURLFieldReportElement.class);
        GroupingPropertyDescriptor keepAspectDescriptor = new GroupingPropertyDescriptor(PropertyKeys.KEEP_ASPECT, PropertyKeys.GROUP_APPEARANCE, false, 2, ImageURLFieldReportElement.class);
        GroupingPropertyDescriptor formulaDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FORMULA, PropertyKeys.GROUP_APPEARANCE, false, 3, ImageURLFieldReportElement.class);
        GroupingPropertyDescriptor paddingContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PADDING, PropertyKeys.GROUP_APPEARANCE, false, 4, ImageURLFieldReportElement.class);
        GroupingPropertyDescriptor elementBorderDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ELEMENT_BORDER, PropertyKeys.GROUP_APPEARANCE, false, 5, ImageURLFieldReportElement.class);

        propertyDescriptors = new PropertyDescriptor[]{nameDescriptor,
                                                       positionContentDescriptor,
                                                       formulaDescriptor,
                                                       elementBorderDescriptor,
                                                       minimumSizeContentDescriptor,
                                                       maximumSizeContentDescriptor,
                                                       fieldNameDescriptor,
                                                       keepAspectDescriptor,
                                                       paddingContentDescriptor
        };
    }


    @NotNull
    public BeanDescriptor getBeanDescriptor()
    {
        return new BeanDescriptor(ImageURLFieldReportElement.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}