/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * JavaBaseClassFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.factory.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.jfree.ui.FloatDimension;

/**
 * A default factory for all commonly used java base classes from java.lang, java.awt
 * etc.
 *
 * @author Thomas Morgner
 */
public class JavaBaseClassFactory extends ClassFactoryImpl {

    /**
     * DefaultConstructor. Creates the object factory for all java base classes.
     */
    public JavaBaseClassFactory() {
        registerClass(Dimension.class, new DimensionObjectDescription());
        registerClass(Dimension2D.class, new Dimension2DObjectDescription());
        registerClass(FloatDimension.class, new BeanObjectDescription(FloatDimension.class));
        registerClass(Date.class, new DateObjectDescription());
        registerClass(Boolean.TYPE, new BooleanObjectDescription());
        registerClass(Byte.TYPE, new ByteObjectDescription());
        registerClass(Double.TYPE, new DoubleObjectDescription());
        registerClass(Float.TYPE, new FloatObjectDescription());
        registerClass(Integer.TYPE, new IntegerObjectDescription());
        registerClass(Long.TYPE, new LongObjectDescription());
        registerClass(Short.TYPE, new ShortObjectDescription());
        registerClass(Character.TYPE, new CharacterObjectDescription());
        registerClass(Character.class, new CharacterObjectDescription());
        registerClass(Boolean.class, new BooleanObjectDescription());
        registerClass(Byte.class, new ByteObjectDescription());
        registerClass(Double.class, new DoubleObjectDescription());
        registerClass(Float.class, new FloatObjectDescription());
        registerClass(Integer.class, new IntegerObjectDescription());
        registerClass(Long.class, new LongObjectDescription());
        registerClass(Short.class, new ShortObjectDescription());
        registerClass(Line2D.class, new Line2DObjectDescription());
        registerClass(Point2D.class, new Point2DObjectDescription());
        registerClass(Rectangle2D.class, new Rectangle2DObjectDescription());
        registerClass(String.class, new StringObjectDescription());
        registerClass(Color.class, new ColorObjectDescription());
        registerClass(BasicStroke.class, new BasicStrokeObjectDescription());
        registerClass(Object.class, new ClassLoaderObjectDescription());

        registerClass(Format.class, new ClassLoaderObjectDescription());
        registerClass(NumberFormat.class, createNumberFormatDescription());
        registerClass(DecimalFormat.class, new DecimalFormatObjectDescription());
        registerClass(DecimalFormatSymbols.class, createDecimalFormatSymbols());
        registerClass(DateFormat.class, new ClassLoaderObjectDescription());
        registerClass(SimpleDateFormat.class, new SimpleDateFormatObjectDescription());
        registerClass(DateFormatSymbols.class, new ClassLoaderObjectDescription());

        registerClass(ArrayList.class, new CollectionObjectDescription(ArrayList.class));
        registerClass(Vector.class, new CollectionObjectDescription(Vector.class));
        registerClass(HashSet.class, new CollectionObjectDescription(HashSet.class));
        registerClass(TreeSet.class, new CollectionObjectDescription(TreeSet.class));
        registerClass(Set.class, new CollectionObjectDescription(HashSet.class));
        registerClass(List.class, new CollectionObjectDescription(ArrayList.class));
        registerClass(Collection.class, new CollectionObjectDescription(ArrayList.class));
    }

    private ObjectDescription createNumberFormatDescription () {
        final BeanObjectDescription nfDesc =
            new BeanObjectDescription(NumberFormat.class, false);
        nfDesc.setParameterDefinition("groupingUsed", Boolean.TYPE);
        nfDesc.setParameterDefinition("maximumFractionDigits", Integer.TYPE);
        nfDesc.setParameterDefinition("minimumFractionDigits", Integer.TYPE);
        nfDesc.setParameterDefinition("maximumIntegerDigits", Integer.TYPE);
        nfDesc.setParameterDefinition("minimumIntegerDigits", Integer.TYPE);
        nfDesc.setParameterDefinition("parseIntegerOnly", Boolean.TYPE);
        return nfDesc;
    }

    private ObjectDescription createDecimalFormatSymbols() {
        final BeanObjectDescription dfsDesc =
            new BeanObjectDescription(DecimalFormatSymbols.class, false);
        dfsDesc.setParameterDefinition("currencySymbol", String.class);
        dfsDesc.setParameterDefinition("decimalSeparator", Character.TYPE);
        dfsDesc.setParameterDefinition("digit", Character.TYPE);
        dfsDesc.setParameterDefinition("groupingSeparator", Character.TYPE);
        dfsDesc.setParameterDefinition("infinity", String.class);
        dfsDesc.setParameterDefinition("internationalCurrencySymbol", String.class);
        dfsDesc.setParameterDefinition("minusSign", Character.TYPE);
        dfsDesc.setParameterDefinition("monetaryDecimalSeparator", Character.TYPE);
        dfsDesc.setParameterDefinition("naN", String.class);
        dfsDesc.setParameterDefinition("patternSeparator", Character.TYPE);
        dfsDesc.setParameterDefinition("perMill", Character.TYPE);
        dfsDesc.setParameterDefinition("percent", Character.TYPE);
        dfsDesc.setParameterDefinition("zeroDigit", Character.TYPE);
        return dfsDesc;

    }
}
