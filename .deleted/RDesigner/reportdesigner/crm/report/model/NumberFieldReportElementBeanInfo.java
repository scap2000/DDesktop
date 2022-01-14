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
public class NumberFieldReportElementBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public NumberFieldReportElementBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor nameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NAME, PropertyKeys.GROUP_ID, false, 1, NumberFieldReportElement.class);

        GroupingPropertyDescriptor positionContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.POSITION, PropertyKeys.GROUP_SPATIAL, false, 1, NumberFieldReportElement.class);
        GroupingPropertyDescriptor minimumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MINIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 2, NumberFieldReportElement.class);
        GroupingPropertyDescriptor maximumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MAXIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 3, NumberFieldReportElement.class);

        GroupingPropertyDescriptor formatStringDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FORMAT, PropertyKeys.GROUP_APPEARANCE, true, 1, NumberFieldReportElement.class);
        GroupingPropertyDescriptor fieldNameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FIELD_NAME, PropertyKeys.GROUP_APPEARANCE, true, 2, NumberFieldReportElement.class);
        GroupingPropertyDescriptor nullStringDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NULL_STRING, PropertyKeys.GROUP_APPEARANCE, true, 3, NumberFieldReportElement.class);
        GroupingPropertyDescriptor formulaDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FORMULA, PropertyKeys.GROUP_APPEARANCE, false, 4, NumberFieldReportElement.class);
        GroupingPropertyDescriptor styleExpressionsDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STYLE_EXPRESSIONS, PropertyKeys.GROUP_APPEARANCE, false, 5, NumberFieldReportElement.class);
        GroupingPropertyDescriptor paddingContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PADDING, PropertyKeys.GROUP_APPEARANCE, false, 6, NumberFieldReportElement.class);
        GroupingPropertyDescriptor elementBorderDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ELEMENT_BORDER, PropertyKeys.GROUP_APPEARANCE, false, 7, NumberFieldReportElement.class);

        GroupingPropertyDescriptor fontDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FONT, PropertyKeys.GROUP_APPEARANCE, false, 101, NumberFieldReportElement.class);
        GroupingPropertyDescriptor underlineDescriptor = new GroupingPropertyDescriptor(PropertyKeys.UNDERLINE, PropertyKeys.GROUP_APPEARANCE, false, 102, NumberFieldReportElement.class);
        GroupingPropertyDescriptor strikethroughDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STRIKETHROUGH, PropertyKeys.GROUP_APPEARANCE, false, 103, NumberFieldReportElement.class);
        GroupingPropertyDescriptor lineHeightDescriptor = new GroupingPropertyDescriptor(PropertyKeys.LINE_HEIGHT, PropertyKeys.GROUP_APPEARANCE, false, 104, NumberFieldReportElement.class);
        GroupingPropertyDescriptor backgroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.BACKGROUND, PropertyKeys.GROUP_APPEARANCE, false, 105, NumberFieldReportElement.class);
        GroupingPropertyDescriptor foregroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FOREGROUND, PropertyKeys.GROUP_APPEARANCE, false, 106, NumberFieldReportElement.class);
        GroupingPropertyDescriptor horizontalAlignmentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.HORIZONTAL_ALIGNMENT, PropertyKeys.GROUP_APPEARANCE, false, 107, NumberFieldReportElement.class);
        GroupingPropertyDescriptor verticalAlignmentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.VERTICAL_ALIGNMENT, PropertyKeys.GROUP_APPEARANCE, false, 108, NumberFieldReportElement.class);
        GroupingPropertyDescriptor reservedLiteralDescriptor = new GroupingPropertyDescriptor(PropertyKeys.RESERVED_LITERAL, PropertyKeys.GROUP_APPEARANCE, false, 109, NumberFieldReportElement.class);
        GroupingPropertyDescriptor trimTextContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.TRIM_TEXT_CONTENT, PropertyKeys.GROUP_APPEARANCE, false, 110, NumberFieldReportElement.class);
        GroupingPropertyDescriptor dynamicContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.DYNAMIC_CONTENT, PropertyKeys.GROUP_APPEARANCE, false, 111, NumberFieldReportElement.class);

        GroupingPropertyDescriptor excelNumberFormatDescriptor = new GroupingPropertyDescriptor(PropertyKeys.EXCEL_NUMBER_FORMAT, PropertyKeys.GROUP_OUTPUT, true, 1, NumberFieldReportElement.class);
        GroupingPropertyDescriptor wrapTextInExcelDescriptor = new GroupingPropertyDescriptor(PropertyKeys.WRAP_TEXT_IN_EXCEL, PropertyKeys.GROUP_OUTPUT, false, 2, NumberFieldReportElement.class);
        GroupingPropertyDescriptor encodingDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ENCODING, PropertyKeys.GROUP_OUTPUT, false, 3, NumberFieldReportElement.class);
        GroupingPropertyDescriptor embedFontDescriptor = new GroupingPropertyDescriptor(PropertyKeys.EMBED_FONT, PropertyKeys.GROUP_OUTPUT, false, 4, NumberFieldReportElement.class);

        propertyDescriptors = new PropertyDescriptor[]{nameDescriptor,
                                                       styleExpressionsDescriptor,
                                                       formulaDescriptor,
                                                       paddingContentDescriptor,
                                                       positionContentDescriptor,
                                                       elementBorderDescriptor,
                                                       minimumSizeContentDescriptor,
                                                       maximumSizeContentDescriptor,
                                                       formatStringDescriptor,
                                                       excelNumberFormatDescriptor,
                                                       fieldNameDescriptor,
                                                       nullStringDescriptor,
                                                       fontDescriptor,
                                                       underlineDescriptor,
                                                       strikethroughDescriptor,
                                                       lineHeightDescriptor,
                                                       backgroundDescriptor,
                                                       foregroundDescriptor,
                                                       dynamicContentDescriptor,
                                                       horizontalAlignmentDescriptor,
                                                       verticalAlignmentDescriptor,
                                                       reservedLiteralDescriptor,
                                                       trimTextContentDescriptor,
                                                       wrapTextInExcelDescriptor,
                                                       encodingDescriptor,
                                                       embedFontDescriptor
        };

    }


    @NotNull
    public BeanDescriptor getBeanDescriptor()
    {
        return new BeanDescriptor(NumberFieldReportElement.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}
