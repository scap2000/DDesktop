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
public class ReportBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public ReportBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor nameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NAME, PropertyKeys.GROUP_ID, false, 1, Report.class);

        GroupingPropertyDescriptor pageDefinitionDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PAGE_DEFINITION, PropertyKeys.GROUP_APPEARANCE, true, 1, Report.class);
        GroupingPropertyDescriptor defaultLocaleDescriptor = new GroupingPropertyDescriptor(PropertyKeys.DEFAULT_LOCALE, PropertyKeys.GROUP_APPEARANCE, true, 2, Report.class);
        GroupingPropertyDescriptor resourceBundleClasspathDescriptor = new GroupingPropertyDescriptor(PropertyKeys.RESOURCE_BUNDLE_CLASSPATH, PropertyKeys.GROUP_APPEARANCE, true, 3, Report.class);
        GroupingPropertyDescriptor reportConfigurationDescriptor = new GroupingPropertyDescriptor(PropertyKeys.REPORT_CONFIGURATION, PropertyKeys.GROUP_APPEARANCE, true, 4, Report.class);
        GroupingPropertyDescriptor useMaxCharBoundsDescriptor = new GroupingPropertyDescriptor(PropertyKeys.USE_MAX_CHAR_BOUNDS, PropertyKeys.GROUP_APPEARANCE, true, 5, Report.class);

        propertyDescriptors = new PropertyDescriptor[]{nameDescriptor,
                                                       pageDefinitionDescriptor,
                                                       defaultLocaleDescriptor,
                                                       resourceBundleClasspathDescriptor,
                                                       reportConfigurationDescriptor,
                                                       useMaxCharBoundsDescriptor};
    }


    @NotNull
    public BeanDescriptor getBeanDescriptor()
    {
        return new BeanDescriptor(Report.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}
