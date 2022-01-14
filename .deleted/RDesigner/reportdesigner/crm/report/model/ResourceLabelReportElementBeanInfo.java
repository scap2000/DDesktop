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
public class ResourceLabelReportElementBeanInfo extends SimpleBeanInfo
{
    @NotNull
    private PropertyDescriptor[] propertyDescriptors;


    public ResourceLabelReportElementBeanInfo() throws IntrospectionException
    {
        GroupingPropertyDescriptor nameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NAME, PropertyKeys.GROUP_ID, false, 1, ResourceLabelReportElement.class);

        GroupingPropertyDescriptor positionContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.POSITION, PropertyKeys.GROUP_SPATIAL, false, 1, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor minimumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MINIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 2, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor maximumSizeContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.MAXIMUM_SIZE, PropertyKeys.GROUP_SPATIAL, false, 3, ResourceLabelReportElement.class);

        GroupingPropertyDescriptor resourceBaseStringDescriptor = new GroupingPropertyDescriptor(PropertyKeys.RESOURCE_BASE, PropertyKeys.GROUP_APPEARANCE, true, 1, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor fieldNameDescriptor = new GroupingPropertyDescriptor(PropertyKeys.RESOURCE_KEY, PropertyKeys.GROUP_APPEARANCE, true, 2, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor nullStringDescriptor = new GroupingPropertyDescriptor(PropertyKeys.NULL_STRING, PropertyKeys.GROUP_APPEARANCE, true, 3, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor styleExpressionsDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STYLE_EXPRESSIONS, PropertyKeys.GROUP_APPEARANCE, false, 5, ResourceLabelReportElement.class);

        GroupingPropertyDescriptor fontDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FONT, PropertyKeys.GROUP_APPEARANCE, false, 101, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor underlineDescriptor = new GroupingPropertyDescriptor(PropertyKeys.UNDERLINE, PropertyKeys.GROUP_APPEARANCE, false, 102, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor strikethroughDescriptor = new GroupingPropertyDescriptor(PropertyKeys.STRIKETHROUGH, PropertyKeys.GROUP_APPEARANCE, false, 103, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor lineHeightDescriptor = new GroupingPropertyDescriptor(PropertyKeys.LINE_HEIGHT, PropertyKeys.GROUP_APPEARANCE, false, 104, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor backgroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.BACKGROUND, PropertyKeys.GROUP_APPEARANCE, false, 105, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor foregroundDescriptor = new GroupingPropertyDescriptor(PropertyKeys.FOREGROUND, PropertyKeys.GROUP_APPEARANCE, false, 106, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor horizontalAlignmentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.HORIZONTAL_ALIGNMENT, PropertyKeys.GROUP_APPEARANCE, false, 107, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor verticalAlignmentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.VERTICAL_ALIGNMENT, PropertyKeys.GROUP_APPEARANCE, false, 108, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor reservedLiteralDescriptor = new GroupingPropertyDescriptor(PropertyKeys.RESERVED_LITERAL, PropertyKeys.GROUP_APPEARANCE, false, 109, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor trimTextContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.TRIM_TEXT_CONTENT, PropertyKeys.GROUP_APPEARANCE, false, 110, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor dynamicContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.DYNAMIC_CONTENT, PropertyKeys.GROUP_APPEARANCE, false, 111, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor paddingContentDescriptor = new GroupingPropertyDescriptor(PropertyKeys.PADDING, PropertyKeys.GROUP_APPEARANCE, false, 112, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor elementBorderDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ELEMENT_BORDER, PropertyKeys.GROUP_APPEARANCE, false, 113, ResourceLabelReportElement.class);

        GroupingPropertyDescriptor wrapTextInExcelDescriptor = new GroupingPropertyDescriptor(PropertyKeys.WRAP_TEXT_IN_EXCEL, PropertyKeys.GROUP_OUTPUT, false, 1, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor encodingDescriptor = new GroupingPropertyDescriptor(PropertyKeys.ENCODING, PropertyKeys.GROUP_OUTPUT, false, 2, ResourceLabelReportElement.class);
        GroupingPropertyDescriptor embedFontDescriptor = new GroupingPropertyDescriptor(PropertyKeys.EMBED_FONT, PropertyKeys.GROUP_OUTPUT, false, 3, ResourceLabelReportElement.class);

        propertyDescriptors = new PropertyDescriptor[]{nameDescriptor,
                                                       styleExpressionsDescriptor,
                                                       positionContentDescriptor,
                                                       paddingContentDescriptor,
                                                       minimumSizeContentDescriptor,
                                                       maximumSizeContentDescriptor,
                                                       resourceBaseStringDescriptor,
                                                       fieldNameDescriptor,
                                                       elementBorderDescriptor,
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
        return new BeanDescriptor(ResourceLabelReportElement.class);
    }


    @NotNull
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


}
