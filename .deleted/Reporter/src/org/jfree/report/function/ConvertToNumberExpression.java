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
 * ConvertToNumberExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import java.lang.reflect.Method;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import java.util.Locale;

import org.jfree.report.DataRow;
import org.jfree.util.ObjectUtilities;

/**
 * Parses a string into a number using the given decimal-format.
 *
 * @author Thomas Morgner
 * @see java.text.DecimalFormat
 */
public class ConvertToNumberExpression extends AbstractExpression
{
  /**
   * The default pattern if no other format-string was given. This parses decimal integer numbers with the
   * highest precision. This pattern was the default for JDK 1.4 and below, but changed in JDK 1.5. We stick
   * with the original pattern here.
   */
  private static final String DECIMALFORMAT_DEFAULT_PATTERN =
      "#,###.###################################################" +
          "#########################################################" +
          "#########################################################" +
          "#########################################################" +
          "#########################################################" +
          "#########################################################" +
          "####";

  /**
   * A constant for the numeric value zero.
   */
  private static final BigDecimal ZERO = new BigDecimal(0);

  /**
     * The name of the data-row destColumn from where to read the string that should be parsed.
     */
  private String field;
  /**
   * The date-format that is used for the parsing.
   */
  private String format;
  /**
   * The locale. If undefined, the report's locale is used.
   */
  private Locale locale;
  /**
   * A variable that caches the last locale object used.
   */
  private Locale lastLocale;
  /**
   * A variable that caches the last number-format object used.
   */
  private DecimalFormat decimalFormat;

  /**
   * Default Constructor.
   */
  public ConvertToNumberExpression()
  {
  }

  /**
     * Returns the name of the data-row destColumn from where to read the string that should be parsed.
     *
     * @return the field name.
     */
  public String getField()
  {
    return field;
  }

  /**
     * Defines the name of the data-row destColumn from where to read the string that should be parsed.
     *
     * @param field the name of the field.
     */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Returns the SimpleDateFormat pattern that is used for the parsing.
   *
   * @return the pattern string.
   * @see java.text.SimpleDateFormat
   */
  public String getFormat()
  {
    return format;
  }

  /**
   * Defines the SimpleDateFormat pattern that is used for the parsing.
   *
   * @param format the pattern string.
   * @see java.text.SimpleDateFormat
   */
  public void setFormat(final String format)
  {
    this.format = format;
    this.lastLocale = null;
    this.decimalFormat = null;
  }

  /**
   * Returns the locale that is used for the parsing.
   *
   * @return the locale.
   */
  public Locale getLocale()
  {
    return locale;
  }

  /**
   * Defines the locale that is used for the parsing.
   *
   * @param locale the locale.
   */
  public void setLocale(final Locale locale)
  {
    this.locale = locale;
    this.lastLocale = null;
    this.decimalFormat = null;
  }

  /**
     * Parses the value read from the destColumn specified by the given field-name and tries to parse it into a Number
     * using the given DecimalFormat-pattern.
     *
     * @return the value of the function.
     */
  public Object getValue()
  {
    final DataRow dataRow = getDataRow();
    // get the row directly as a Number
    final Object o = dataRow.get(field);
    // check if that thing is a Number
    if (o instanceof Number)
    {
      return o;
    }

    // get a string and convert
    final String formatString = getFormat();
    try
    {
      Locale localeUsed = locale;
      if (localeUsed == null)
      {
        localeUsed = getResourceBundleFactory().getLocale();
      }

      if (decimalFormat == null || ObjectUtilities.equal(lastLocale, localeUsed) == false)
      {
        final String effectiveFormatString;
        if (formatString == null || formatString.length() == 0)
        {
          // this is a workaround for a bug in JDK 1.5
          effectiveFormatString = DECIMALFORMAT_DEFAULT_PATTERN;
        }
        else
        {
          effectiveFormatString = formatString;
        }
        lastLocale = localeUsed;
        decimalFormat = new DecimalFormat(effectiveFormatString);
        activateBigDecimalMode(decimalFormat);
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(localeUsed));
      }

      return decimalFormat.parse(String.valueOf(o));
    }
    catch (ParseException e)
    {
      return ZERO;
    }
  }

  /**
   * Uses reflection to enable the use of BigDecimals in the parsing. This produces better results, but only
   * works on JDK 1.4 or higher.
   *
   * @param format the format object that should be patched.
   */
  private void activateBigDecimalMode(final DecimalFormat format)
  {
    if (ObjectUtilities.isJDK14())
    {
      try
      {
        final Method method = DecimalFormat.class.getMethod("setParseBigDecimal", new Class[]{Boolean.TYPE});
        method.invoke(format, new Object[]{Boolean.TRUE});
      }
      catch (Exception e)
      {
        // ignore it, as it will always fail on JDK 1.4 or lower ..
      }
    }
  }

}
