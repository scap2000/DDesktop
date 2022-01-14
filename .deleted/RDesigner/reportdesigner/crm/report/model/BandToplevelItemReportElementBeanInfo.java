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
public class BandToplevelItemReportElementBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public BandToplevelItemReportElementBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor pageBreakBeforeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PAGE_BREAK_BEFORE, PropertyKeys.GROUP_APPEARANCE, false, 1, BandToplevelItemReportElement.class);
        GroupingPropertyDescriptor pageBreakAfterContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PAGE_BREAK_AFTER, PropertyKeys.GROUP_APPEARANCE, false, 2, BandToplevelItemReportElement.class);
        GroupingPropertyDescriptor visualHeightDescriptor = new GroupingPropertyDescriptor(PropertyKeys.VISUAL_HEIGHT, PropertyKeys.GROUP_APPEARANCE, false, 3, BandToplevelItemReportElement.class);
        GroupingPropertyDescriptor showInLayoutGUIDescriptor = new GroupingPropertyDescriptor(PropertyKeys.SHOW_IN_LAYOUT_GUI, PropertyKeys.GROUP_APPEARANCE, false, 4, BandToplevelItemReportElement.class);
        GroupingPropertyDescriptor rowBandingDefinitionDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ROW_BANDING_DEFINITION, PropertyKeys.GROUP_APPEARANCE, false, 5, BandToplevelItemReportElement.class);
        GroupingPropertyDescriptor styleExpressionsDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STYLE_EXPRESSIONS, PropertyKeys.GROUP_APPEARANCE, false, 6, BandToplevelItemReportElement.class);
        GroupingPropertyDescriptor backgroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.BACKGROUND, PropertyKeys.GROUP_APPEARANCE, false, 7, BandToplevelItemReportElement.class);

        GroupingPropertyDescriptor minimumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MINIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 2, BandToplevelItemReportElement.class);
        GroupingPropertyDescriptor maximumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MAXIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 3, BandToplevelItemReportElement.class);

        propertyDescriptors = new PropertyDescriptor[]{pageBreakBeforeContentDescriptor,
                                                       pageBreakAfterContentDescriptor,
                                                       backgroundDescriptor,
                                                       styleExpressionsDescriptor,
                                                       rowBandingDefinitionDescriptor,
                                                       visualHeightDescriptor,
                                                       showInLayoutGUIDescriptor,
                                                       minimumSizeContentDescriptor,
                                                       maximumSizeContentDescriptor};
    }


    @NotNull
    public BeanDescriptor getBeanDescriptor()
    {
        return new BeanDescriptor(BandToplevelItemReportElement.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}
