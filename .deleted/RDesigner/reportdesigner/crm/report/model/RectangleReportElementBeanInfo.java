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
public class RectangleReportElementBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public RectangleReportElementBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor nameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NAME, PropertyKeys.GROUP_ID, false, 1, RectangleReportElement.class);

        GroupingPropertyDescriptor positionContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.POSITION, PropertyKeys.GROUP_SPATIAL, false, 1, RectangleReportElement.class);
        GroupingPropertyDescriptor minimumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MINIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 2, RectangleReportElement.class);
        GroupingPropertyDescriptor maximumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MAXIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 3, RectangleReportElement.class);

        GroupingPropertyDescriptor backgroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.COLOR, PropertyKeys.GROUP_APPEARANCE, true, 1, RectangleReportElement.class);
        GroupingPropertyDescriptor fillDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FILL, PropertyKeys.GROUP_APPEARANCE, true, 2, RectangleReportElement.class);
        GroupingPropertyDescriptor drawDescriptor = new GroupingPropertyDescriptor(PropertyKeys.DRAW_BORDER, PropertyKeys.GROUP_APPEARANCE, true, 3, RectangleReportElement.class);
        //GroupingPropertyDescriptor lineWidthDescriptor = new GroupingPropertyDescriptor(PropertyKeys.LINE_WIDTH, PropertyKeys.GROUP_APPEARANCE, true, 4, RectangleReportElement.class);
        GroupingPropertyDescriptor borderDefinitionDescriptor = new GroupingPropertyDescriptor(PropertyKeys.BORDER_DEFINITION, PropertyKeys.GROUP_APPEARANCE, true, 4, RectangleReportElement.class);
        GroupingPropertyDescriptor arcWidthDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ARC_WIDTH, PropertyKeys.GROUP_APPEARANCE, true, 5, RectangleReportElement.class);
        GroupingPropertyDescriptor arcHeightDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ARC_HEIGHT, PropertyKeys.GROUP_APPEARANCE, true, 6, RectangleReportElement.class);
        GroupingPropertyDescriptor styleExpressionsDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STYLE_EXPRESSIONS, PropertyKeys.GROUP_APPEARANCE, false, 7, RectangleReportElement.class);
        GroupingPropertyDescriptor paddingContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PADDING, PropertyKeys.GROUP_APPEARANCE, false, 8, RectangleReportElement.class);
        //GroupingPropertyDescriptor elementBorderDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ELEMENT_BORDER, PropertyKeys.GROUP_APPEARANCE, false, 9, RectangleReportElement.class);

        propertyDescriptors = new PropertyDescriptor[]{nameDescriptor,
                                                       styleExpressionsDescriptor,
                                                       positionContentDescriptor,
                                                       minimumSizeContentDescriptor,
                                                       maximumSizeContentDescriptor,
                                                       backgroundDescriptor,
                                                       fillDescriptor,
                                                       drawDescriptor,
                                                       borderDefinitionDescriptor,
                                                       arcWidthDescriptor,
                                                       arcHeightDescriptor,
                                                       paddingContentDescriptor
        };


    }


    @NotNull
    public BeanDescriptor getBeanDescriptor()
    {
        return new BeanDescriptor(RectangleReportElement.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}
