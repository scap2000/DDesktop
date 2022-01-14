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
package org.pentaho.reportdesigner.crm.report.commands.morph;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.DateFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.DrawableFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageURLFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.NumberFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ResourceFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElement;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 20.02.2006
 * Time: 11:08:09
 */
public class MorphingHelper
{

    @NotNull
    private static final MorphingHelper instance = new MorphingHelper();


    @NotNull
    public static MorphingHelper getInstance()
    {
        return instance;
    }


    @NotNull
    private HashMap<ClassPair, ReportElementMorpher> morpher;


    private MorphingHelper()
    {
        morpher = new HashMap<ClassPair, ReportElementMorpher>();

        addTextFieldMorphers();
        addDateFieldMorphers();
        addNumberFieldMorphers();
        addDrawableFieldMorphers();
        addImageFieldMorphers();
        addImageURLFieldMorphers();
        addMessageFieldMorphers();
        addResourceFieldMorphers();
    }


    private void addTextFieldMorphers()
    {
        morpher.put(new ClassPair(TextFieldReportElement.class, TextFieldReportElement.class), new ReportElementMorpher<TextFieldReportElement, TextFieldReportElement>()
        {
            @NotNull
            public TextFieldReportElement getMorphedReportElement(@NotNull TextFieldReportElement reportElement)
            {
                TextFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(TextFieldReportElement.class, DateFieldReportElement.class), new ReportElementMorpher<TextFieldReportElement, DateFieldReportElement>()
        {
            @NotNull
            public DateFieldReportElement getMorphedReportElement(@NotNull TextFieldReportElement reportElement)
            {
                DateFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(TextFieldReportElement.class, NumberFieldReportElement.class), new ReportElementMorpher<TextFieldReportElement, NumberFieldReportElement>()
        {
            @NotNull
            public NumberFieldReportElement getMorphedReportElement(@NotNull TextFieldReportElement reportElement)
            {
                NumberFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getNumberFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(TextFieldReportElement.class, ResourceFieldReportElement.class), new ReportElementMorpher<TextFieldReportElement, ResourceFieldReportElement>()
        {
            @NotNull
            public ResourceFieldReportElement getMorphedReportElement(@NotNull TextFieldReportElement reportElement)
            {
                ResourceFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getResourceFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(TextFieldReportElement.class, MessageFieldReportElement.class), new ReportElementMorpher<TextFieldReportElement, MessageFieldReportElement>()
        {
            @NotNull
            public MessageFieldReportElement getMorphedReportElement(@NotNull TextFieldReportElement reportElement)
            {
                MessageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFormatString("$(" + reportElement.getFieldName() + ")");
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(TextFieldReportElement.class, ImageFieldReportElement.class), new ReportElementMorpher<TextFieldReportElement, ImageFieldReportElement>()
        {
            @NotNull
            public ImageFieldReportElement getMorphedReportElement(@NotNull TextFieldReportElement reportElement)
            {
                ImageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(TextFieldReportElement.class, ImageURLFieldReportElement.class), new ReportElementMorpher<TextFieldReportElement, ImageURLFieldReportElement>()
        {
            @NotNull
            public ImageURLFieldReportElement getMorphedReportElement(@NotNull TextFieldReportElement reportElement)
            {
                ImageURLFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageURLFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(TextFieldReportElement.class, DrawableFieldReportElement.class), new ReportElementMorpher<TextFieldReportElement, DrawableFieldReportElement>()
        {
            @NotNull
            public DrawableFieldReportElement getMorphedReportElement(@NotNull TextFieldReportElement reportElement)
            {
                DrawableFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDrawableFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });
    }


    private void addResourceFieldMorphers()
    {
        morpher.put(new ClassPair(ResourceFieldReportElement.class, TextFieldReportElement.class), new ReportElementMorpher<ResourceFieldReportElement, TextFieldReportElement>()
        {
            @NotNull
            public TextFieldReportElement getMorphedReportElement(@NotNull ResourceFieldReportElement reportElement)
            {
                TextFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ResourceFieldReportElement.class, DateFieldReportElement.class), new ReportElementMorpher<ResourceFieldReportElement, DateFieldReportElement>()
        {
            @NotNull
            public DateFieldReportElement getMorphedReportElement(@NotNull ResourceFieldReportElement reportElement)
            {
                DateFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ResourceFieldReportElement.class, NumberFieldReportElement.class), new ReportElementMorpher<ResourceFieldReportElement, NumberFieldReportElement>()
        {
            @NotNull
            public NumberFieldReportElement getMorphedReportElement(@NotNull ResourceFieldReportElement reportElement)
            {
                NumberFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getNumberFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ResourceFieldReportElement.class, ResourceFieldReportElement.class), new ReportElementMorpher<ResourceFieldReportElement, ResourceFieldReportElement>()
        {
            @NotNull
            public ResourceFieldReportElement getMorphedReportElement(@NotNull ResourceFieldReportElement reportElement)
            {
                ResourceFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getResourceFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ResourceFieldReportElement.class, MessageFieldReportElement.class), new ReportElementMorpher<ResourceFieldReportElement, MessageFieldReportElement>()
        {
            @NotNull
            public MessageFieldReportElement getMorphedReportElement(@NotNull ResourceFieldReportElement reportElement)
            {
                MessageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFormatString("$(" + reportElement.getFieldName() + ")");
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ResourceFieldReportElement.class, ImageFieldReportElement.class), new ReportElementMorpher<ResourceFieldReportElement, ImageFieldReportElement>()
        {
            @NotNull
            public ImageFieldReportElement getMorphedReportElement(@NotNull ResourceFieldReportElement reportElement)
            {
                ImageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ResourceFieldReportElement.class, ImageURLFieldReportElement.class), new ReportElementMorpher<ResourceFieldReportElement, ImageURLFieldReportElement>()
        {
            @NotNull
            public ImageURLFieldReportElement getMorphedReportElement(@NotNull ResourceFieldReportElement reportElement)
            {
                ImageURLFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageURLFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ResourceFieldReportElement.class, DrawableFieldReportElement.class), new ReportElementMorpher<ResourceFieldReportElement, DrawableFieldReportElement>()
        {
            @NotNull
            public DrawableFieldReportElement getMorphedReportElement(@NotNull ResourceFieldReportElement reportElement)
            {
                DrawableFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDrawableFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });
    }


    private void addDateFieldMorphers()
    {
        morpher.put(new ClassPair(DateFieldReportElement.class, TextFieldReportElement.class), new ReportElementMorpher<DateFieldReportElement, TextFieldReportElement>()
        {
            @NotNull
            public TextFieldReportElement getMorphedReportElement(@NotNull DateFieldReportElement reportElement)
            {
                TextFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DateFieldReportElement.class, DateFieldReportElement.class), new ReportElementMorpher<DateFieldReportElement, DateFieldReportElement>()
        {
            @NotNull
            public DateFieldReportElement getMorphedReportElement(@NotNull DateFieldReportElement reportElement)
            {
                DateFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DateFieldReportElement.class, NumberFieldReportElement.class), new ReportElementMorpher<DateFieldReportElement, NumberFieldReportElement>()
        {
            @NotNull
            public NumberFieldReportElement getMorphedReportElement(@NotNull DateFieldReportElement reportElement)
            {
                NumberFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getNumberFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DateFieldReportElement.class, ResourceFieldReportElement.class), new ReportElementMorpher<DateFieldReportElement, ResourceFieldReportElement>()
        {
            @NotNull
            public ResourceFieldReportElement getMorphedReportElement(@NotNull DateFieldReportElement reportElement)
            {
                ResourceFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getResourceFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DateFieldReportElement.class, MessageFieldReportElement.class), new ReportElementMorpher<DateFieldReportElement, MessageFieldReportElement>()
        {
            @NotNull
            public MessageFieldReportElement getMorphedReportElement(@NotNull DateFieldReportElement reportElement)
            {
                MessageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFormatString("$(" + reportElement.getFieldName() + ", date, " + reportElement.getFormat().toPattern() + ")");//NON-NLS
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DateFieldReportElement.class, ImageFieldReportElement.class), new ReportElementMorpher<DateFieldReportElement, ImageFieldReportElement>()
        {
            @NotNull
            public ImageFieldReportElement getMorphedReportElement(@NotNull DateFieldReportElement reportElement)
            {
                ImageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DateFieldReportElement.class, ImageURLFieldReportElement.class), new ReportElementMorpher<DateFieldReportElement, ImageURLFieldReportElement>()
        {
            @NotNull
            public ImageURLFieldReportElement getMorphedReportElement(@NotNull DateFieldReportElement reportElement)
            {
                ImageURLFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageURLFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DateFieldReportElement.class, DrawableFieldReportElement.class), new ReportElementMorpher<DateFieldReportElement, DrawableFieldReportElement>()
        {
            @NotNull
            public DrawableFieldReportElement getMorphedReportElement(@NotNull DateFieldReportElement reportElement)
            {
                DrawableFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDrawableFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });
    }


    private void addNumberFieldMorphers()
    {
        morpher.put(new ClassPair(NumberFieldReportElement.class, TextFieldReportElement.class), new ReportElementMorpher<NumberFieldReportElement, TextFieldReportElement>()
        {
            @NotNull
            public TextFieldReportElement getMorphedReportElement(@NotNull NumberFieldReportElement reportElement)
            {
                TextFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(NumberFieldReportElement.class, DateFieldReportElement.class), new ReportElementMorpher<NumberFieldReportElement, DateFieldReportElement>()
        {
            @NotNull
            public DateFieldReportElement getMorphedReportElement(@NotNull NumberFieldReportElement reportElement)
            {
                DateFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(NumberFieldReportElement.class, NumberFieldReportElement.class), new ReportElementMorpher<NumberFieldReportElement, NumberFieldReportElement>()
        {
            @NotNull
            public NumberFieldReportElement getMorphedReportElement(@NotNull NumberFieldReportElement reportElement)
            {
                NumberFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getNumberFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(NumberFieldReportElement.class, ResourceFieldReportElement.class), new ReportElementMorpher<NumberFieldReportElement, ResourceFieldReportElement>()
        {
            @NotNull
            public ResourceFieldReportElement getMorphedReportElement(@NotNull NumberFieldReportElement reportElement)
            {
                ResourceFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getResourceFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(NumberFieldReportElement.class, MessageFieldReportElement.class), new ReportElementMorpher<NumberFieldReportElement, MessageFieldReportElement>()
        {
            @NotNull
            public MessageFieldReportElement getMorphedReportElement(@NotNull NumberFieldReportElement reportElement)
            {
                MessageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFormatString("$(" + reportElement.getFieldName() + ", number, " + ((DecimalFormat) reportElement.getFormat()).toPattern() + ")");//NON-NLS
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(NumberFieldReportElement.class, ImageFieldReportElement.class), new ReportElementMorpher<NumberFieldReportElement, ImageFieldReportElement>()
        {
            @NotNull
            public ImageFieldReportElement getMorphedReportElement(@NotNull NumberFieldReportElement reportElement)
            {
                ImageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(NumberFieldReportElement.class, ImageURLFieldReportElement.class), new ReportElementMorpher<NumberFieldReportElement, ImageURLFieldReportElement>()
        {
            @NotNull
            public ImageURLFieldReportElement getMorphedReportElement(@NotNull NumberFieldReportElement reportElement)
            {
                ImageURLFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageURLFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(NumberFieldReportElement.class, DrawableFieldReportElement.class), new ReportElementMorpher<NumberFieldReportElement, DrawableFieldReportElement>()
        {
            @NotNull
            public DrawableFieldReportElement getMorphedReportElement(@NotNull NumberFieldReportElement reportElement)
            {
                DrawableFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDrawableFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });
    }


    private void addDrawableFieldMorphers()
    {
        morpher.put(new ClassPair(DrawableFieldReportElement.class, TextFieldReportElement.class), new ReportElementMorpher<DrawableFieldReportElement, TextFieldReportElement>()
        {
            @NotNull
            public TextFieldReportElement getMorphedReportElement(@NotNull DrawableFieldReportElement reportElement)
            {
                TextFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DrawableFieldReportElement.class, DateFieldReportElement.class), new ReportElementMorpher<DrawableFieldReportElement, DateFieldReportElement>()
        {
            @NotNull
            public DateFieldReportElement getMorphedReportElement(@NotNull DrawableFieldReportElement reportElement)
            {
                DateFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DrawableFieldReportElement.class, NumberFieldReportElement.class), new ReportElementMorpher<DrawableFieldReportElement, NumberFieldReportElement>()
        {
            @NotNull
            public NumberFieldReportElement getMorphedReportElement(@NotNull DrawableFieldReportElement reportElement)
            {
                NumberFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getNumberFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DrawableFieldReportElement.class, ResourceFieldReportElement.class), new ReportElementMorpher<DrawableFieldReportElement, ResourceFieldReportElement>()
        {
            @NotNull
            public ResourceFieldReportElement getMorphedReportElement(@NotNull DrawableFieldReportElement reportElement)
            {
                ResourceFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getResourceFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DrawableFieldReportElement.class, MessageFieldReportElement.class), new ReportElementMorpher<DrawableFieldReportElement, MessageFieldReportElement>()
        {
            @NotNull
            public MessageFieldReportElement getMorphedReportElement(@NotNull DrawableFieldReportElement reportElement)
            {
                MessageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFormatString("$(" + reportElement.getFieldName() + ")");
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DrawableFieldReportElement.class, ImageFieldReportElement.class), new ReportElementMorpher<DrawableFieldReportElement, ImageFieldReportElement>()
        {
            @NotNull
            public ImageFieldReportElement getMorphedReportElement(@NotNull DrawableFieldReportElement reportElement)
            {
                ImageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DrawableFieldReportElement.class, ImageURLFieldReportElement.class), new ReportElementMorpher<DrawableFieldReportElement, ImageURLFieldReportElement>()
        {
            @NotNull
            public ImageURLFieldReportElement getMorphedReportElement(@NotNull DrawableFieldReportElement reportElement)
            {
                ImageURLFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageURLFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(DrawableFieldReportElement.class, DrawableFieldReportElement.class), new ReportElementMorpher<DrawableFieldReportElement, DrawableFieldReportElement>()
        {
            @NotNull
            public DrawableFieldReportElement getMorphedReportElement(@NotNull DrawableFieldReportElement reportElement)
            {
                DrawableFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDrawableFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });
    }


    private void addImageFieldMorphers()
    {
        morpher.put(new ClassPair(ImageFieldReportElement.class, TextFieldReportElement.class), new ReportElementMorpher<ImageFieldReportElement, TextFieldReportElement>()
        {
            @NotNull
            public TextFieldReportElement getMorphedReportElement(@NotNull ImageFieldReportElement reportElement)
            {
                TextFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageFieldReportElement.class, DateFieldReportElement.class), new ReportElementMorpher<ImageFieldReportElement, DateFieldReportElement>()
        {
            @NotNull
            public DateFieldReportElement getMorphedReportElement(@NotNull ImageFieldReportElement reportElement)
            {
                DateFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageFieldReportElement.class, NumberFieldReportElement.class), new ReportElementMorpher<ImageFieldReportElement, NumberFieldReportElement>()
        {
            @NotNull
            public NumberFieldReportElement getMorphedReportElement(@NotNull ImageFieldReportElement reportElement)
            {
                NumberFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getNumberFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageFieldReportElement.class, ResourceFieldReportElement.class), new ReportElementMorpher<ImageFieldReportElement, ResourceFieldReportElement>()
        {
            @NotNull
            public ResourceFieldReportElement getMorphedReportElement(@NotNull ImageFieldReportElement reportElement)
            {
                ResourceFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getResourceFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageFieldReportElement.class, MessageFieldReportElement.class), new ReportElementMorpher<ImageFieldReportElement, MessageFieldReportElement>()
        {
            @NotNull
            public MessageFieldReportElement getMorphedReportElement(@NotNull ImageFieldReportElement reportElement)
            {
                MessageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFormatString("$(" + reportElement.getFieldName() + ")");
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageFieldReportElement.class, ImageFieldReportElement.class), new ReportElementMorpher<ImageFieldReportElement, ImageFieldReportElement>()
        {
            @NotNull
            public ImageFieldReportElement getMorphedReportElement(@NotNull ImageFieldReportElement reportElement)
            {
                ImageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageFieldReportElement.class, DrawableFieldReportElement.class), new ReportElementMorpher<ImageFieldReportElement, DrawableFieldReportElement>()
        {
            @NotNull
            public DrawableFieldReportElement getMorphedReportElement(@NotNull ImageFieldReportElement reportElement)
            {
                DrawableFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDrawableFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageFieldReportElement.class, ImageURLFieldReportElement.class), new ReportElementMorpher<ImageFieldReportElement, ImageURLFieldReportElement>()
        {
            @NotNull
            public ImageURLFieldReportElement getMorphedReportElement(@NotNull ImageFieldReportElement reportElement)
            {
                ImageURLFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageURLFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });
    }private void addImageURLFieldMorphers()
    {
        morpher.put(new ClassPair(ImageURLFieldReportElement.class, TextFieldReportElement.class), new ReportElementMorpher<ImageURLFieldReportElement, TextFieldReportElement>()
        {
            @NotNull
            public TextFieldReportElement getMorphedReportElement(@NotNull ImageURLFieldReportElement reportElement)
            {
                TextFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageURLFieldReportElement.class, DateFieldReportElement.class), new ReportElementMorpher<ImageURLFieldReportElement, DateFieldReportElement>()
        {
            @NotNull
            public DateFieldReportElement getMorphedReportElement(@NotNull ImageURLFieldReportElement reportElement)
            {
                DateFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageURLFieldReportElement.class, NumberFieldReportElement.class), new ReportElementMorpher<ImageURLFieldReportElement, NumberFieldReportElement>()
        {
            @NotNull
            public NumberFieldReportElement getMorphedReportElement(@NotNull ImageURLFieldReportElement reportElement)
            {
                NumberFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getNumberFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageURLFieldReportElement.class, ResourceFieldReportElement.class), new ReportElementMorpher<ImageURLFieldReportElement, ResourceFieldReportElement>()
        {
            @NotNull
            public ResourceFieldReportElement getMorphedReportElement(@NotNull ImageURLFieldReportElement reportElement)
            {
                ResourceFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getResourceFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageURLFieldReportElement.class, MessageFieldReportElement.class), new ReportElementMorpher<ImageURLFieldReportElement, MessageFieldReportElement>()
        {
            @NotNull
            public MessageFieldReportElement getMorphedReportElement(@NotNull ImageURLFieldReportElement reportElement)
            {
                MessageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFormatString("$(" + reportElement.getFieldName() + ")");
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageURLFieldReportElement.class, ImageURLFieldReportElement.class), new ReportElementMorpher<ImageURLFieldReportElement, ImageURLFieldReportElement>()
        {
            @NotNull
            public ImageURLFieldReportElement getMorphedReportElement(@NotNull ImageURLFieldReportElement reportElement)
            {
                ImageURLFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageURLFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageURLFieldReportElement.class, DrawableFieldReportElement.class), new ReportElementMorpher<ImageURLFieldReportElement, DrawableFieldReportElement>()
        {
            @NotNull
            public DrawableFieldReportElement getMorphedReportElement(@NotNull ImageURLFieldReportElement reportElement)
            {
                DrawableFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDrawableFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(ImageURLFieldReportElement.class, ImageFieldReportElement.class), new ReportElementMorpher<ImageURLFieldReportElement, ImageFieldReportElement>()
        {
            @NotNull
            public ImageFieldReportElement getMorphedReportElement(@NotNull ImageURLFieldReportElement reportElement)
            {
                ImageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(reportElement.getFieldName());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });
    }


    private void addMessageFieldMorphers()
    {
        morpher.put(new ClassPair(MessageFieldReportElement.class, TextFieldReportElement.class), new ReportElementMorpher<MessageFieldReportElement, TextFieldReportElement>()
        {
            @NotNull
            public TextFieldReportElement getMorphedReportElement(@NotNull MessageFieldReportElement reportElement)
            {
                TextFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(analyzeMessagePattern(reportElement.getFormatString()));
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(MessageFieldReportElement.class, DateFieldReportElement.class), new ReportElementMorpher<MessageFieldReportElement, DateFieldReportElement>()
        {
            @NotNull
            public DateFieldReportElement getMorphedReportElement(@NotNull MessageFieldReportElement reportElement)
            {
                DateFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(analyzeMessagePattern(reportElement.getFormatString()));
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(MessageFieldReportElement.class, NumberFieldReportElement.class), new ReportElementMorpher<MessageFieldReportElement, NumberFieldReportElement>()
        {
            @NotNull
            public NumberFieldReportElement getMorphedReportElement(@NotNull MessageFieldReportElement reportElement)
            {
                NumberFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getNumberFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(analyzeMessagePattern(reportElement.getFormatString()));
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(MessageFieldReportElement.class, ResourceFieldReportElement.class), new ReportElementMorpher<MessageFieldReportElement, ResourceFieldReportElement>()
        {
            @NotNull
            public ResourceFieldReportElement getMorphedReportElement(@NotNull MessageFieldReportElement reportElement)
            {
                ResourceFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getResourceFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(analyzeMessagePattern(reportElement.getFormatString()));
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(MessageFieldReportElement.class, MessageFieldReportElement.class), new ReportElementMorpher<MessageFieldReportElement, MessageFieldReportElement>()
        {
            @NotNull
            public MessageFieldReportElement getMorphedReportElement(@NotNull MessageFieldReportElement reportElement)
            {
                MessageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFormatString(reportElement.getFormatString());
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(MessageFieldReportElement.class, ImageFieldReportElement.class), new ReportElementMorpher<MessageFieldReportElement, ImageFieldReportElement>()
        {
            @NotNull
            public ImageFieldReportElement getMorphedReportElement(@NotNull MessageFieldReportElement reportElement)
            {
                ImageFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(analyzeMessagePattern(reportElement.getFormatString()));
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(MessageFieldReportElement.class, ImageURLFieldReportElement.class), new ReportElementMorpher<MessageFieldReportElement, ImageURLFieldReportElement>()
        {
            @NotNull
            public ImageURLFieldReportElement getMorphedReportElement(@NotNull MessageFieldReportElement reportElement)
            {
                ImageURLFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getImageURLFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(analyzeMessagePattern(reportElement.getFormatString()));
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });

        morpher.put(new ClassPair(MessageFieldReportElement.class, DrawableFieldReportElement.class), new ReportElementMorpher<MessageFieldReportElement, DrawableFieldReportElement>()
        {
            @NotNull
            public DrawableFieldReportElement getMorphedReportElement(@NotNull MessageFieldReportElement reportElement)
            {
                DrawableFieldReportElement dateFieldReportElement = ReportElementInfoFactory.getInstance().getDrawableFieldReportElementInfo().createReportElement();
                dateFieldReportElement.setFieldName(analyzeMessagePattern(reportElement.getFormatString()));
                applyStandardAttributes(reportElement, dateFieldReportElement);
                return dateFieldReportElement;
            }
        });
    }


    @NotNull
    private static String analyzeMessagePattern(@NotNull String pattern)
    {
        int start = -1;
        while ((start = pattern.indexOf("$(", start + 1)) != -1)
        {
            int endComma = pattern.indexOf(',', start + 1);
            int endParan = pattern.indexOf(')', start + 1);
            if (endComma == -1)
            {
                endComma = Integer.MAX_VALUE;
            }
            int end = endParan;
            if (endComma < endParan)
            {
                end = endComma;
            }
            if (end != -1)
            {
                String s = pattern.substring(start + 2, end);
                return s;
            }
        }
        return "";
    }


    public boolean hasMorpher(@NotNull Class class1, @NotNull Class class2)
    {
        return morpher.containsKey(new ClassPair(class1, class2));
    }


    @Nullable
    public ReportElementMorpher getMorpher(@NotNull Class class1, @NotNull Class class2)
    {
        return morpher.get(new ClassPair(class1, class2));
    }


    private static class ClassPair
    {
        @NotNull
        private Class class1;
        @NotNull
        private Class class2;


        private ClassPair(@NotNull Class class1, @NotNull Class class2)
        {
            this.class1 = class1;
            this.class2 = class2;
        }


        public boolean equals(@Nullable Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final ClassPair classPair = (ClassPair) o;

            if (!class1.equals(classPair.class1)) return false;
            return class2.equals(classPair.class2);

        }


        public int hashCode()
        {
            int result;
            result = class1.hashCode();
            result = 29 * result + class2.hashCode();
            return result;
        }
    }

    public abstract static class ReportElementMorpher<T extends ReportElement, E extends ReportElement>
    {
        @NotNull
        public abstract E getMorphedReportElement(@NotNull T reportElement);


        public void applyStandardAttributes(@NotNull T source, @NotNull E target)
        {
            Point2D.Double position = new Point2D.Double();
            position.setLocation(source.getPosition());
            target.setPosition(position);

            target.setMinimumSize(new DoubleDimension(source.getMinimumSize()));
            target.setPreferredSize(new DoubleDimension(source.getPreferredSize()));
            target.setMaximumSize(new DoubleDimension(source.getMaximumSize()));

            target.setBackground(source.getBackground());
            target.setDynamicContent(source.isDynamicContent());
            target.setName(source.getName());

            applyTextReportElementAttributes(source, target);
        }


        private void applyTextReportElementAttributes(@NotNull T source, @NotNull E target)
        {
            if (source instanceof TextReportElement && target instanceof TextReportElement)
            {
                //noinspection unchecked
                TextReportElement s = (TextReportElement) source;
                //noinspection unchecked
                TextReportElement t = (TextReportElement) target;

                t.setEmbedFont(s.isEmbedFont());
                t.setEncoding(s.getEncoding());
                t.setFont(s.getFont());
                t.setForeground(s.getForeground());
                t.setHorizontalAlignment(s.getHorizontalAlignment());
                t.setLineHeight(s.getLineHeight());
                t.setReservedLiteral(s.getReservedLiteral());
                t.setStrikethrough(s.isStrikethrough());
                t.setTrimTextContent(s.isTrimTextContent());
                t.setUnderline(s.isUnderline());
                t.setVerticalAlignment(s.getVerticalAlignment());
                t.setWrapTextInExcel(s.isWrapTextInExcel());
            }

        }


    }

}
