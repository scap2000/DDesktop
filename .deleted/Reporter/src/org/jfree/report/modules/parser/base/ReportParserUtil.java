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
 * ReportParserUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.lang.reflect.Field;

import org.jfree.report.ElementAlignment;
import org.jfree.report.style.VerticalTextAlign;
import org.jfree.report.util.StrokeUtility;
import org.jfree.xmlns.common.ParserUtil;
import org.jfree.xmlns.parser.ParseException;
import org.jfree.xmlns.parser.RootXmlReadHandler;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A helper class to make parsing the xml files a lot easier.
 *
 * @author Thomas Morgner
 */
public final class ReportParserUtil
{
  public static final String INCLUDE_PARSING_KEY = "::Include-parser";
  public static final Object INCLUDE_PARSING_VALUE = Boolean.TRUE;
  public static final String HELPER_OBJ_REPORT_NAME = "::Report";

  /**
   * DefaultConstructor.
   */
  private ReportParserUtil()
  {
  }


  /**
   * Checks whether this report is a included report and not the main report definition.
   *
   * @return true, if the report is included, false otherwise.
   */
  public static boolean isIncluded(final RootXmlReadHandler rootXmlReadHandler)
  {
    return INCLUDE_PARSING_VALUE.equals
        (rootXmlReadHandler.getHelperObject(INCLUDE_PARSING_KEY));
  }

  /**
   * Parses a vertical alignment value.
   *
   * @param value the text to parse.
   * @return the element alignment.
   * @throws ParseException if the alignment value is not recognised.
   */
  public static ElementAlignment parseVerticalElementAlignment (final String value, final Locator locator)
      throws ParseException
  {
    if (value == null)
    {
      return null;
    }

    if ("top".equals(value))
    {
      return ElementAlignment.TOP;
    }
    if ("middle".equals(value))
    {
      return ElementAlignment.MIDDLE;
    }
    if ("bottom".equals(value))
    {
      return ElementAlignment.BOTTOM;
    }
    throw new ParseException("Invalid vertical alignment", locator);
  }

  /**
   * Parses a horizontal alignment value.
   *
   * @param value the text to parse.
   * @return the element alignment.
   * @throws org.xml.sax.SAXException if the alignment value is not recognised.
   */
  public static ElementAlignment parseHorizontalElementAlignment
      (final String value,
       final Locator locator)
      throws ParseException
  {
    if (value == null)
    {
      return null;
    }

    if ("left".equals(value))
    {
      return ElementAlignment.LEFT;
    }
    if ("center".equals(value))
    {
      return ElementAlignment.CENTER;
    }
    if ("right".equals(value))
    {
      return ElementAlignment.RIGHT;
    }
    throw new ParseException("Invalid horizontal alignment", locator);
  }


  /**
   * Creates a basic stroke given the width contained as float in the given string. If the string could not be parsed
   * into a float, a basic stroke with the width of 1 is returned.
   *
   * @param weight a string containing a number (the stroke weight).
   * @return the stroke.
   */
  public static Stroke parseStroke(final String weight)
  {
    try
    {
      if (weight != null)
      {
        final Float w = new Float(weight);
        return new BasicStroke(w.floatValue());
      }
    }
    catch (NumberFormatException nfe)
    {
      //Log.warn("Invalid weight for stroke", nfe);
    }
    return new BasicStroke(1);
  }


  /**
   * Reads an attribute as float and returns <code>def</code> if that fails.
   *
   * @param value the attribute value.
   * @return the float value.
   * @throws ParseException if an parse error occured.
   */
  public static Float parseFloat(final String value, final Locator locator)
      throws ParseException
  {
    if (value == null)
    {
      return null;
    }
    try
    {
      return new Float(value);
    }
    catch (Exception ex)
    {
      throw new ParseException("Failed to parse value", locator);
    }
  }


  /**
   * Reads an attribute as float and returns <code>def</code> if that fails.
   *
   * @param value the attribute value.
   * @return the float value.
   * @throws ParseException if an parse error occured.
   */
  public static Integer parseInteger(final String value,
                                     final Locator locator)
      throws ParseException
  {
    if (value == null)
    {
      return null;
    }
    try
    {
      return new Integer(value);
    }
    catch (Exception ex)
    {
      throw new ParseException("Failed to parse value", locator);
    }
  }

  /**
   * Parses a color entry. If the entry is in hexadecimal or ocal notation, the color is created using Color.decode().
   * If the string denotes a constant name of on of the color constants defined in java.awt.Color, this constant is
   * used.
   * <p/>
   * As fallback the color black is returned if no color can be parsed.
   *
   * @param color the color (as a string).
   * @return the paint.
   */
  public static Color parseColor(final String color)
  {
    return parseColor(color, Color.black);
  }

  /**
   * Parses a color entry. If the entry is in hexadecimal or octal notation, the color is created using Color.decode().
   * If the string denotes a constant name of one of the color constants defined in java.awt.Color, this constant is
   * used.
   * <p/>
   * As fallback the supplied default value is returned if no color can be parsed.
   *
   * @param color        the color (as a string).
   * @param defaultValue the default value (returned if no color can be parsed).
   * @return the paint.
   */
  public static Color parseColor(final String color, final Color defaultValue)
  {
    if (color == null)
    {
      return defaultValue;
    }
    try
    {
      // get color by hex or octal value
      return Color.decode(color);
    }
    catch (NumberFormatException nfe)
    {
      // if we can't decode lets try to get it by name
      try
      {
        // try to get a color by name using reflection
        // black is used for an instance and not for the color itselfs
        final Field f = Color.class.getField(color);

        return (Color) f.get(null);
      }
      catch (Exception ce)
      {
        //Log.warn("No such Color : " + color);
        // if we can't get any color return black
        return defaultValue;
      }
    }
  }


  /**
   * Parses a position of an element. If a relative postion is given, the returnvalue is a negative number between 0 and
   * -100.
   *
   * @param value            the value.
   * @param exceptionMessage the exception message.
   * @return the float value.
   * @throws SAXException if there is a problem parsing the string.
   */
  public static float parseRelativeFloat(final String value,
                                         final String exceptionMessage,
                                         final Locator locator)
      throws ParseException
  {
    if (value == null)
    {
      throw new ParseException(exceptionMessage, locator);
    }
    final String tvalue = value.trim();
    if (tvalue.length() > 0 && tvalue.charAt(tvalue.length() - 1) == '%')
    {
      final String number = tvalue.substring(0, tvalue.length() - 1);
      return ParserUtil.parseFloat(number, exceptionMessage, locator) * -1.0f;
    }
    else
    {
      return ParserUtil.parseFloat(tvalue, exceptionMessage, locator);
    }
  }

  /**
     * Parses an element position. The position is stored in the attributes "minX", "minY", "width" and "height". The attributes
     * are allowed to have relative notion.
     *
     * @param atts the attributes.
     * @return the element position.
     * @throws ParseException if there is a problem getting the element position.
     */
  public static Rectangle2D getElementPosition(final String uri,
                                               final Attributes atts,
                                               final Locator locator)
      throws ParseException
  {
    final float x = parseRelativeFloat(atts.getValue(uri, "x"),
        "Element x not specified", locator);
    final float y = parseRelativeFloat(atts.getValue(uri, "y"),
        "Element y not specified", locator);
    final float w = parseRelativeFloat(atts.getValue(uri, "width"),
        "Element width not specified", locator);
    final float h = parseRelativeFloat(atts.getValue(uri, "height"),
        "Element height not specified", locator);
    return new Rectangle2D.Float(x, y, w, h);
  }


  public static Stroke parseStroke(final String strokeStyle, final float weight)
  {
    // "dashed | solid | dotted | dot-dot-dash | dot-dash"
    if ("dashed".equalsIgnoreCase(strokeStyle))
    {
      return StrokeUtility.createStroke(StrokeUtility.STROKE_DASHED, weight);
    }
    else if ("dotted".equalsIgnoreCase(strokeStyle))
    {
      return StrokeUtility.createStroke(StrokeUtility.STROKE_DOTTED, weight);
    }
    else if ("dot-dot-dash".equalsIgnoreCase(strokeStyle))
    {
      return StrokeUtility.createStroke(StrokeUtility.STROKE_DOT_DOT_DASH, weight);
    }
    else if ("dot-dash".equalsIgnoreCase(strokeStyle))
    {
      return StrokeUtility.createStroke(StrokeUtility.STROKE_DOT_DASH, weight);
    }
    else
    {
      return StrokeUtility.createStroke(StrokeUtility.STROKE_SOLID, weight);
    }
  }

  public static VerticalTextAlign parseVerticalTextElementAlignment(final String value, final Locator locator)
      throws ParseException
  {
    if (value == null)
    {
      return null;
    }

    if ("top".equals(value))
    {
      return VerticalTextAlign.TOP;
    }
    if ("middle".equals(value))
    {
      return VerticalTextAlign.MIDDLE;
    }
    if ("bottom".equals(value))
    {
      return VerticalTextAlign.BOTTOM;
    }
    if ("baseline".equals(value))
    {
      return VerticalTextAlign.BASELINE;
    }
    if ("central".equals(value))
    {
      return VerticalTextAlign.CENTRAL;
    }
    if ("sub".equals(value))
    {
      return VerticalTextAlign.SUB;
    }
    if ("super".equals(value))
    {
      return VerticalTextAlign.SUPER;
    }
    if ("text-bottom".equals(value))
    {
      return VerticalTextAlign.TEXT_BOTTOM;
    }
    if ("text-top".equals(value))
    {
      return VerticalTextAlign.TEXT_TOP;
    }
    if ("use-script".equals(value))
    {
      return VerticalTextAlign.USE_SCRIPT;
    }

    throw new ParseException("Invalid vertical alignment", locator);
  }
}
