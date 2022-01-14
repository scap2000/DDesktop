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
package org.pentaho.reportdesigner.lib.common.xml;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.FilePath;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 06.02.2006
 * Time: 13:41:27
 */
public class ObjectConverterFactory
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ObjectConverterFactory.class.getName());

    @NotNull
    private static final ObjectConverterFactory instance = new ObjectConverterFactory();


    @NotNull
    public static ObjectConverterFactory getInstance()
    {
        return instance;
    }


    @NotNull
    private HashMap<Class<?>, ObjectConverter> converters;

    @NotNull
    private DoubleDimensionConverter doubleDimensionConverter;
    @NotNull
    private Point2DConverter point2DConverter;
    @NotNull
    private Rectangle2DConverter rectangle2DConverter;
    @NotNull
    private LocaleConverter localeConverter;
    @NotNull
    private ColorConverter colorConverter;
    @NotNull
    private URLConverter urlConverter;
    @NotNull
    private FontConverter fontConverter;
    @NotNull
    private FilePathConverter filePathConverter;


    private ObjectConverterFactory()
    {
        doubleDimensionConverter = new DoubleDimensionConverter();
        point2DConverter = new Point2DConverter();
        rectangle2DConverter = new Rectangle2DConverter();
        localeConverter = new LocaleConverter();
        colorConverter = new ColorConverter();
        urlConverter = new URLConverter();
        fontConverter = new FontConverter();
        filePathConverter = new FilePathConverter();

        converters = new HashMap<Class<?>, ObjectConverter>();
        converters.put(DoubleDimension.class, doubleDimensionConverter);
        converters.put(Point2D.Double.class, point2DConverter);
        converters.put(Rectangle2D.Double.class, rectangle2DConverter);
        converters.put(Locale.class, localeConverter);
        converters.put(Color.class, colorConverter);
        converters.put(URL.class, urlConverter);
        converters.put(FilePath.class, filePathConverter);

        converters.put(Double.class, new DoubleConverter());
        converters.put(Double.TYPE, new DoubleConverter());
        converters.put(Number.class, new DoubleConverter());
        converters.put(Integer.class, new IntegerConverter());
        converters.put(Integer.TYPE, new IntegerConverter());
        converters.put(Float.class, new FloatConverter());
        converters.put(Float.TYPE, new FloatConverter());
        converters.put(Boolean.class, new BooleanConverter());
        converters.put(Boolean.TYPE, new BooleanConverter());
        converters.put(String.class, new StringConverter());
        converters.put(Long.class, new LongConverter());
        converters.put(Long.TYPE, new LongConverter());
        converters.put(TimeZone.class, new TimeZoneConverter());
        converters.put(Date.class, new DateConverter());
    }


    @NotNull
    public DoubleDimensionConverter getDoubleDimensionConverter()
    {
        return doubleDimensionConverter;
    }


    @NotNull
    public Point2DConverter getPoint2DConverter()
    {
        return point2DConverter;
    }


    @NotNull
    public Rectangle2DConverter getRectangle2DConverter()
    {
        return rectangle2DConverter;
    }


    @NotNull
    public LocaleConverter getLocaleConverter()
    {
        return localeConverter;
    }


    @NotNull
    public ColorConverter getColorConverter()
    {
        return colorConverter;
    }


    @NotNull
    public URLConverter getURLConverter(@NotNull XMLContext xmlContext)
    {
        return getURLConverter(xmlContext, null);
    }


    @NotNull
    public URLConverter getURLConverter(@NotNull XMLContext xmlContext, @Nullable Field field)
    {
        urlConverter.configure(xmlContext, field);
        return urlConverter;
    }


    @NotNull
    public FilePathConverter getFilePathConverter(@NotNull XMLContext xmlContext)
    {
        return getFilePathConverter(xmlContext, null);
    }


    @NotNull
    public FilePathConverter getFilePathConverter(@NotNull XMLContext xmlContext, @Nullable Field field)
    {
        filePathConverter.configure(xmlContext, field);
        return filePathConverter;
    }


    @Nullable
    public ObjectConverter getConverter(@NotNull Class<?> clazz, @Nullable Field field, @NotNull XMLContext xmlContext)
    {
        if (clazz.isArray())
        {
            ObjectConverter converter = getConverter(clazz.getComponentType(), field, xmlContext);
            if (converter == null)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ObjectConverterFactory.getConverter no array converter found for component type " + clazz.getName());
                return null;
            }
            return new ArrayConverter(clazz, converter);
        }
        else
        {
            ObjectConverter converter = converters.get(clazz);
            if (converter == null)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ObjectConverterFactory.getConverter no converter found for component type " + clazz.getName());
            }
            else
            {
                converter.configure(xmlContext, field);
            }
            return converter;
        }
    }


    @NotNull
    public FontConverter getFontConverter()
    {
        return fontConverter;
    }
}
