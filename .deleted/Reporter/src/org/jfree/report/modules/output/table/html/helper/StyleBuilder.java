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
 * StyleBuilder.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Locale;

import org.jfree.report.layout.model.BorderEdge;
import org.jfree.report.modules.output.table.html.util.HtmlColors;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.StringUtils;

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
 * StyleBuilder.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public final class StyleBuilder
{
  private static class StyleCarrier
  {
    private String key;
    private String value;
    private String unit;

    protected StyleCarrier(final String key, final String value, final String unit)
    {
      if (key == null)
      {
        throw new NullPointerException();
      }
      this.key = key;
      this.value = value;
      this.unit = unit;
    }

    public String getUnit()
    {
      return unit;
    }

    public String getKey()
    {
      return key;
    }

    public String getValue()
    {
      return value;
    }


    public boolean equals(final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      final StyleCarrier that = (StyleCarrier) o;

      if (!key.equals(that.key))
      {
        return false;
      }

      return true;
    }

    public int hashCode()
    {
      return key.hashCode();
    }
  }

  private static final String INDENT = "    ";

  private ArrayList styles;
  private String lineSeparator;
  private StringBuffer buffer;

  private NumberFormat pointConverter;
  private NumberFormat pointIntConverter;
  
  public StyleBuilder()
  {
    this.lineSeparator = StringUtils.getLineSeparator();
    this.styles = new ArrayList();
    this.buffer = new StringBuffer(100);

    pointConverter = new DecimalFormat("0.####", new DecimalFormatSymbols(Locale.US));
    pointIntConverter = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));

  }

  public void clear()
  {
    this.styles.clear();
  }

  public void append (final String key, final String value)
  {
    final StyleCarrier newCarrier = new StyleCarrier(key, value, null);
    styles.remove(newCarrier);
    styles.add(newCarrier);
  }

  public void append (final String key, final String value, final String unit)
  {
    final StyleCarrier newCarrier = new StyleCarrier(key, value, unit);
    styles.remove(newCarrier);
    styles.add(newCarrier);
  }

  /**
   * Appends the style to the list. If the replace value if <code>false</code>
   * and the list already contains the key, it will not be replaced.
   */
  public void append (final String key, final String value, final boolean replace)
  {
    final StyleCarrier newCarrier = new StyleCarrier(key, value, null);
    if (replace || !styles.contains(newCarrier))
    {
      styles.remove(newCarrier);
      styles.add(newCarrier);
    }
  }

  /**
   * Appends the style to the list. If the replace value if <code>false</code>
   * and the list already contains the key, it will not be replaced.
   */
  public void append (final String key, final String value, final String unit, final boolean replace)
  {
    final StyleCarrier newCarrier = new StyleCarrier(key, value, unit);
    if (replace || !styles.contains(newCarrier))
    {
      styles.remove(newCarrier);
      styles.add(newCarrier);
    }
  }

  public String toString ()
  {
    return toString(true);
  }

  public String toString (final boolean compact)
  {
    buffer.delete(0, buffer.length());
    for (int i = 0; i < styles.size(); i++)
    {
      if (i > 0)
      {
        buffer.append("; ");
      }

      final StyleCarrier sc = (StyleCarrier) styles.get(i);
      if (compact == false)
      {
        if (i != 0)
        {
          buffer.append(lineSeparator);
        }
        buffer.append(StyleBuilder.INDENT);
      }

      buffer.append(sc.getKey());
      buffer.append(": ");
      buffer.append(sc.getValue());
      final String unit = sc.getUnit();
      if (unit != null)
      {
        buffer.append(unit);
      }
    }
    return buffer.toString();
  }


  public String printEdgeAsCSS(final BorderEdge edge)
  {
    final BorderStyle borderStyle = edge.getBorderStyle();
    final long width = edge.getWidth();
    if (BorderStyle.NONE.equals(borderStyle) || width <= 0)
    {
      return "none";
    }
    return (pointConverter.format(StrictGeomUtility.toExternalValue(width))) + "pt " +
        borderStyle.toString() + ' ' + HtmlColors.getColorString(edge.getColor());
  }

  public NumberFormat getPointConverter()
  {
    return pointConverter;
  }

  public NumberFormat getPointIntConverter()
  {
    return pointIntConverter;
  }

  
}
