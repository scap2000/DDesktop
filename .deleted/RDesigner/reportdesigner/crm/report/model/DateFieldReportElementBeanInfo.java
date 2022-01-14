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
public class DateFieldReportElementBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public DateFieldReportElementBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor nameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NAME, PropertyKeys.GROUP_ID, false, 1, DateFieldReportElement.class);

        GroupingPropertyDescriptor positionContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.POSITION, PropertyKeys.GROUP_SPATIAL, false, 1, DateFieldReportElement.class);
        GroupingPropertyDescriptor minimumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MINIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 2, DateFieldReportElement.class);
        GroupingPropertyDescriptor maximumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MAXIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 3, DateFieldReportElement.class);

        GroupingPropertyDescriptor formatDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FORMAT, PropertyKeys.GROUP_APPEARANCE, true, 1, DateFieldReportElement.class);
        GroupingPropertyDescriptor fieldNameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FIELD_NAME, PropertyKeys.GROUP_APPEARANCE, true, 2, DateFieldReportElement.class);
        GroupingPropertyDescriptor formulaDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FORMULA, PropertyKeys.GROUP_APPEARANCE, false, 3, DateFieldReportElement.class);
        GroupingPropertyDescriptor styleExpressionsDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STYLE_EXPRESSIONS, PropertyKeys.GROUP_APPEARANCE, false, 4, DateFieldReportElement.class);
        GroupingPropertyDescriptor nullStringDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NULL_STRING, PropertyKeys.GROUP_APPEARANCE, true, 4, DateFieldReportElement.class);
        GroupingPropertyDescriptor fontDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FONT, PropertyKeys.GROUP_APPEARANCE, false, 101, DateFieldReportElement.class);
        GroupingPropertyDescriptor underlineDescriptor = new GroupingPropertyDescriptor(PropertyKeys.UNDERLINE, PropertyKeys.GROUP_APPEARANCE, false, 102, DateFieldReportElement.class);
        GroupingPropertyDescriptor strikethroughDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STRIKETHROUGH, PropertyKeys.GROUP_APPEARANCE, false, 103, DateFieldReportElement.class);
        GroupingPropertyDescriptor lineHeightDescriptor = new GroupingPropertyDescriptor(PropertyKeys.LINE_HEIGHT, PropertyKeys.GROUP_APPEARANCE, false, 104, DateFieldReportElement.class);
        GroupingPropertyDescriptor backgroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.BACKGROUND, PropertyKeys.GROUP_APPEARANCE, false, 105, DateFieldReportElement.class);
        GroupingPropertyDescriptor foregroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FOREGROUND, PropertyKeys.GROUP_APPEARANCE, false, 106, DateFieldReportElement.class);
        GroupingPropertyDescriptor horizontalAlignmentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.HORIZONTAL_ALIGNMENT, PropertyKeys.GROUP_APPEARANCE, false, 107, DateFieldReportElement.class);
        GroupingPropertyDescriptor verticalAlignmentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.VERTICAL_ALIGNMENT, PropertyKeys.GROUP_APPEARANCE, false, 108, DateFieldReportElement.class);
        GroupingPropertyDescriptor reservedLiteralDescriptor = new GroupingPropertyDescriptor(PropertyKeys.RESERVED_LITERAL, PropertyKeys.GROUP_APPEARANCE, false, 109, DateFieldReportElement.class);
        GroupingPropertyDescriptor trimTextContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.TRIM_TEXT_CONTENT, PropertyKeys.GROUP_APPEARANCE, false, 110, DateFieldReportElement.class);
        GroupingPropertyDescriptor dynamicContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.DYNAMIC_CONTENT, PropertyKeys.GROUP_APPEARANCE, false, 111, DateFieldReportElement.class);
        GroupingPropertyDescriptor paddingContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PADDING, PropertyKeys.GROUP_APPEARANCE, false, 112, DateFieldReportElement.class);
        //GroupingPropertyDescriptor elementBorderDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ELEMENT_BORDER, PropertyKeys.GROUP_APPEARANCE, false, 113, DateFieldReportElement.class);

        GroupingPropertyDescriptor excelDateFormatDescriptor = new GroupingPropertyDescriptor(PropertyKeys.EXCEL_DATE_FORMAT, PropertyKeys.GROUP_OUTPUT, true, 1, DateFieldReportElement.class);
        GroupingPropertyDescriptor wrapTextInExcelDescriptor = new GroupingPropertyDescriptor(PropertyKeys.WRAP_TEXT_IN_EXCEL, PropertyKeys.GROUP_OUTPUT, false, 2, DateFieldReportElement.class);
        GroupingPropertyDescriptor encodingDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ENCODING, PropertyKeys.GROUP_OUTPUT, false, 3, DateFieldReportElement.class);


        propertyDescriptors = new PropertyDescriptor[]{nameDescriptor,
                                                       styleExpressionsDescriptor,
                                                       formulaDescriptor,
                                                       positionContentDescriptor,
                                                       minimumSizeContentDescriptor,
                                                       maximumSizeContentDescriptor,
                                                       excelDateFormatDescriptor,
                                                       formatDescriptor,
                                                       fieldNameDescriptor,
                                                       nullStringDescriptor,
                                                       fontDescriptor,
                                                       underlineDescriptor,
                                                       strikethroughDescriptor,
                                                       lineHeightDescriptor,
                                                       backgroundDescriptor,
                                                       foregroundDescriptor,
                                                       dynamicContentDescriptor,
                                                       paddingContentDescriptor,
                                                       horizontalAlignmentDescriptor,
                                                       verticalAlignmentDescriptor,
                                                       reservedLiteralDescriptor,
                                                       trimTextContentDescriptor,
                                                       wrapTextInExcelDescriptor,
                                                       encodingDescriptor
        };
    }


    @NotNull
    public BeanDescriptor getBeanDescriptor()
    {
        return new BeanDescriptor(DateFieldReportElement.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}
