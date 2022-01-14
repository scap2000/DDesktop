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
public class BandToplevelPageReportElementBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public BandToplevelPageReportElementBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor displayOnFirstPageDescriptor = new GroupingPropertyDescriptor(PropertyKeys.DISPLAY_ON_FIRST_PAGE, PropertyKeys.GROUP_APPEARANCE, true, 1, BandToplevelPageReportElement.class);
        GroupingPropertyDescriptor displayOnLastPageDescriptor = new GroupingPropertyDescriptor(PropertyKeys.DISPLAY_ON_LAST_PAGE, PropertyKeys.GROUP_APPEARANCE, true, 2, BandToplevelPageReportElement.class);
        GroupingPropertyDescriptor visualHeightDescriptor = new GroupingPropertyDescriptor(PropertyKeys.VISUAL_HEIGHT, PropertyKeys.GROUP_APPEARANCE, false, 3, BandToplevelPageReportElement.class);
        GroupingPropertyDescriptor showInLayoutGUIDescriptor = new GroupingPropertyDescriptor(PropertyKeys.SHOW_IN_LAYOUT_GUI, PropertyKeys.GROUP_APPEARANCE, false, 4, BandToplevelPageReportElement.class);
        GroupingPropertyDescriptor styleExpressionsDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STYLE_EXPRESSIONS, PropertyKeys.GROUP_APPEARANCE, false, 5, BandToplevelPageReportElement.class);
        GroupingPropertyDescriptor backgroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.BACKGROUND, PropertyKeys.GROUP_APPEARANCE, false, 6, BandToplevelPageReportElement.class);

        GroupingPropertyDescriptor minimumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MINIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 2, BandToplevelPageReportElement.class);
        GroupingPropertyDescriptor maximumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MAXIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 3, BandToplevelPageReportElement.class);

        propertyDescriptors = new PropertyDescriptor[]{displayOnFirstPageDescriptor,
                                                       displayOnLastPageDescriptor,
                                                       backgroundDescriptor,
                                                       styleExpressionsDescriptor,
                                                       visualHeightDescriptor,
                                                       showInLayoutGUIDescriptor,
                                                       minimumSizeContentDescriptor,
                                                       maximumSizeContentDescriptor};
    }


    @NotNull
    public BeanDescriptor getBeanDescriptor()
    {
        return new BeanDescriptor(BandToplevelGroupReportElement.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}
