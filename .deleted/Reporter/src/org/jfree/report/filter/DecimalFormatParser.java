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
 * DecimalFormatParser.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter;

import java.lang.reflect.Method;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;

import java.util.Locale;

import org.jfree.report.function.ExpressionRuntime;
import org.jfree.util.ObjectUtilities;

/**
 * A filter that parses string values from a data source to a number using the decimal
 * numeric system as base.
 * <p/>
 * This filter will parse strings using a java.text.DecimalFormat to create the numeric
 * value for the string from the datasource. If the datasource does not return a string,
 * the required string is formed by applying String.valueOf (Object).
 * <p/>
 * If the string read from the datasource could not be parsed into a number, the NullValue
 * defined by setNullValue(Object) is returned.
 *
 * @author Thomas Morgner
 * @see java.text.NumberFormat
 * @see java.lang.Number
 */
public class DecimalFormatParser extends NumberFormatParser
{
  /** The last locale used to convert numbers. */
  private Locale lastLocale;
  /** A flag indicating whether this filter should try to detect locales changes. */
  private boolean keepState;

  /**
   * DefaultConstructor, this object is initialized using a DecimalFormat with the default
   * pattern for this locale.
   */
  public DecimalFormatParser ()
  {
    setFormatter(new DecimalFormat());
  }

  /**
   * Returns the format for the filter. The DecimalFormatParser has only DecimalFormat
   * objects assigned.
   *
   * @return the formatter.
   */
  public DecimalFormat getDecimalFormat ()
  {
    return (DecimalFormat) getFormatter();
  }

  /**
   * Sets the format for the filter.
   *
   * @param format the format.
   * @throws NullPointerException if the given format is null.
   */
  public void setDecimalFormat (final DecimalFormat format)
  {
    setFormatter(format);
  }

  /**
   * Sets the format for the filter. If the given format is no Decimal format, a
   * ClassCastException is thrown
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   * @throws ClassCastException   if the format is no decimal format
   */
  public void setFormatter (final Format format)
  {
    if (format == null)
    {
      throw new NullPointerException("The number format given must not be null.");
    }
    final DecimalFormat dfmt = (DecimalFormat) format;
    activateBigDecimalMode(dfmt);
    super.setFormatter(dfmt);
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

  /**
   * Synthesizes a pattern string that represents the current state of this Format
   * object.
   *
   * @return the pattern string of the format object contained in this filter.
   */
  public String getFormatString ()
  {
    return getDecimalFormat().toPattern();
  }

  /**
   * Applies a format string to the internal <code>DecimalFormat</code> instance.
   *
   * @param format the format string.
   */
  public void setFormatString (final String format)
  {
    getDecimalFormat().applyPattern(format);
  }

  /**
   * Synthesizes a localized pattern string that represents the current state of this
   * Format object.
   *
   * @return the localized pattern string of the format-object.
   */
  public String getLocalizedFormatString ()
  {
    return getDecimalFormat().toLocalizedPattern();
  }

  /**
   * Applies a localised format string to the internal <code>DecimalFormat</code>
   * instance.
   *
   * @param format the format string.
   */
  public void setLocalizedFormatString (final String format)
  {
    getDecimalFormat().applyLocalizedPattern(format);
  }


  /**
   * Defines, whether the filter should keep its state, if a locale
   * change is detected. This will effectivly disable the locale update.
   *
   * @return true, if the locale should not update the DateSymbols, false otherwise.
   */
  public boolean isKeepState ()
  {
    return keepState;
  }

  /**
   * Defines, whether the filter should keep its state, if a locale
   * change is detected. This will effectivly disable the locale update.
   *
   * @param keepState set to true, if the locale should not update the DateSymbols, false otherwise.
   */
  public void setKeepState (final boolean keepState)
  {
    this.keepState = keepState;
  }

  /**
   * Returns the formatted string. The value is read using the data source given and
   * formated using the formatter of this object. The formating is guaranteed to completly
   * form the object to an string or to return the defined NullValue.
   * <p/>
   * If format, datasource or object are null, the NullValue is returned.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return The formatted value.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    if (keepState == false && runtime != null)
    {
      final Locale locale = runtime.getResourceBundleFactory().getLocale();
      if (locale != lastLocale)
      {
        lastLocale = locale;
        getDecimalFormat().setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
      }
    }
    return super.getValue(runtime);
  }
}
