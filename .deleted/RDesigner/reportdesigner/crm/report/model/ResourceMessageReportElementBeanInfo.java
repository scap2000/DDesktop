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
public class ResourceMessageReportElementBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public ResourceMessageReportElementBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor nameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NAME, PropertyKeys.GROUP_ID, false, 1, ResourceMessageReportElement.class);

        GroupingPropertyDescriptor positionContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.POSITION, PropertyKeys.GROUP_SPATIAL, false, 1, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor minimumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MINIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 2, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor maximumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MAXIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 3, ResourceMessageReportElement.class);

        GroupingPropertyDescriptor resourceBaseStringDescriptor = new GroupingPropertyDescriptor(PropertyKeys.RESOURCE_BASE, PropertyKeys.GROUP_APPEARANCE, true, 1, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor fieldNameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FORMAT_KEY, PropertyKeys.GROUP_APPEARANCE, true, 2, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor nullStringDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NULL_STRING, PropertyKeys.GROUP_APPEARANCE, true, 3, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor styleExpressionsDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STYLE_EXPRESSIONS, PropertyKeys.GROUP_APPEARANCE, false, 5, ResourceMessageReportElement.class);

        GroupingPropertyDescriptor fontDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FONT, PropertyKeys.GROUP_APPEARANCE, false, 101, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor underlineDescriptor = new GroupingPropertyDescriptor(PropertyKeys.UNDERLINE, PropertyKeys.GROUP_APPEARANCE, false, 102, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor strikethroughDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STRIKETHROUGH, PropertyKeys.GROUP_APPEARANCE, false, 103, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor lineHeightDescriptor = new GroupingPropertyDescriptor(PropertyKeys.LINE_HEIGHT, PropertyKeys.GROUP_APPEARANCE, false, 104, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor backgroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.BACKGROUND, PropertyKeys.GROUP_APPEARANCE, false, 105, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor foregroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FOREGROUND, PropertyKeys.GROUP_APPEARANCE, false, 106, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor horizontalAlignmentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.HORIZONTAL_ALIGNMENT, PropertyKeys.GROUP_APPEARANCE, false, 107, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor verticalAlignmentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.VERTICAL_ALIGNMENT, PropertyKeys.GROUP_APPEARANCE, false, 108, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor reservedLiteralDescriptor = new GroupingPropertyDescriptor(PropertyKeys.RESERVED_LITERAL, PropertyKeys.GROUP_APPEARANCE, false, 109, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor trimTextContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.TRIM_TEXT_CONTENT, PropertyKeys.GROUP_APPEARANCE, false, 110, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor dynamicContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.DYNAMIC_CONTENT, PropertyKeys.GROUP_APPEARANCE, false, 111, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor paddingContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PADDING, PropertyKeys.GROUP_APPEARANCE, false, 112, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor elementBorderDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ELEMENT_BORDER, PropertyKeys.GROUP_APPEARANCE, false, 113, ResourceMessageReportElement.class);

        GroupingPropertyDescriptor wrapTextInExcelDescriptor = new GroupingPropertyDescriptor(PropertyKeys.WRAP_TEXT_IN_EXCEL, PropertyKeys.GROUP_OUTPUT, false, 1, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor encodingDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ENCODING, PropertyKeys.GROUP_OUTPUT, false, 2, ResourceMessageReportElement.class);
        GroupingPropertyDescriptor embedFontDescriptor = new GroupingPropertyDescriptor(PropertyKeys.EMBED_FONT, PropertyKeys.GROUP_OUTPUT, false, 3, ResourceMessageReportElement.class);

        propertyDescriptors = new PropertyDescriptor[]{nameDescriptor,
                                                       styleExpressionsDescriptor,
                                                       positionContentDescriptor,
                                                       paddingContentDescriptor,
                                                       elementBorderDescriptor,
                                                       minimumSizeContentDescriptor,
                                                       maximumSizeContentDescriptor,
                                                       resourceBaseStringDescriptor,
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
        return new BeanDescriptor(ResourceMessageReportElement.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}
