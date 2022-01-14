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
 * MessageFormatSupport.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.filter;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.jfree.report.DataRow;
import org.jfree.report.util.CSVTokenizer;
import org.jfree.report.util.PropertyLookupParser;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * The message format support class helps to translate named references to fields in a message format string into
 * numeric index positions. With the help of this mapping, we can use a standard Java MessageFormat object to reference
 * fields by their name instead of an arbitrary index position.
 * <p/>
 * A field is referenced by the pattern "$(fieldname)". For additional formatting, all MessageFormat format options are
 * available using the format "$(fieldname, &lt;message option&gt;)". To format a date field with the default short date
 * format, one would use the pattern $(datefield,date,short).
 *
 * @author Thomas Morgner
 */
public class MessageFormatSupport implements Serializable, Cloneable
{
  /**
   * The message compiler maps all named references into numeric references.
   */
  protected static class MessageCompiler extends PropertyLookupParser
  {
    /**
     * The list of fields that have been encountered during the compile process.
     */
    private ArrayList fields;

    /**
     * Default Constructor.
     */
    public MessageCompiler()
    {
      this.fields = new ArrayList();
      setMarkerChar('$');
      setOpeningBraceChar('(');
      setClosingBraceChar(')');
    }

    /**
     * Looks up the property with the given name. This replaces the name with the current index position.
     *
     * @param name the name of the property to look up.
     * @return the translated value.
     */
    protected String lookupVariable(final String name)
    {
      final CSVTokenizer tokenizer = new CSVTokenizer(name, ",", "\"");
      if (tokenizer.hasMoreTokens() == false)
      {
        return null;
      }
      final String varName = tokenizer.nextToken();

      final StringBuffer b = new StringBuffer();
      b.append('{');
      b.append(String.valueOf(fields.size()));
      while (tokenizer.hasMoreTokens())
      {
        b.append(',');
        b.append(tokenizer.nextToken());
      }
      b.append('}');
      final String formatString = b.toString();
      fields.add(varName);
      return formatString;
    }

    /**
     * Returns the collected fields as string-array. The order of the array contents matches the order of the
     * index-position references in the translated message format.
     *
     * @return the fields as array.
     */
    public String[] getFields()
    {
      return (String[]) fields.toArray(new String[fields.size()]);
    }
  }

  /**
   * The fields that have been collected during the compile process. The array also acts as mapping of index positions
   * to field names.
   */
  private String[] fields;
  /**
   * The message-format object that is used to format the text.
   */
  private MessageFormat format;
  /**
   * The original format string.
   */
  private String formatString;
  /**
   * The translated message format string. All named references have been resolved to numeric index positions.
   */
  private String compiledFormat;
  /**
   * The replacement text that is used if one of the referenced message parameters is null.
   */
  private String nullString;

  /**
   * The current locale of the message format.
   */
  private transient Locale locale;

  /**
   * An internal status array. It contains flags on whether the internal formatter has been replaced due to a type
   * mismatch.
   */
  private transient boolean[] replaced;
  private transient boolean[] oldReplaced;
  /**
   * An internal list of formats used by the MessageFormat object.
   */
  private transient Format[] formats;
  /**
   * An internal list of all parameters.
   */
  private transient Object[] parameters;
  private transient Object[] oldParameters;
  private String cachedValue;

  /**
   * Default Constructor.
   */
  public MessageFormatSupport()
  {
  }

  /**
   * Returns the original format string that is used to format the fields. This format string contains named
   * references.
   *
   * @return the format string.
   */
  public String getFormatString()
  {
    return formatString;
  }

  /**
   * Updates the named format string and compiles a new field list and message-format string.
   *
   * @param formatString the format string.
   */
  public void setFormatString(final String formatString)
  {
    if (formatString == null)
    {
      throw new NullPointerException("Format must not be null");
    }

    if (ObjectUtilities.equal(formatString, this.formatString))
    {
      return;
    }

    final MessageCompiler compiler = new MessageCompiler();
    this.compiledFormat = compiler.translateAndLookup(formatString);
    this.fields = compiler.getFields();

    if (fields.length > 0)
    {
      this.format = new MessageFormat(this.compiledFormat);
    }
    else
    {
      this.format = null;
    }
    this.formatString = formatString;
    this.formats = null;
    this.parameters = null;
    this.replaced = null;

    this.oldParameters = null;
    this.oldReplaced = null;
    this.cachedValue = null;
  }

  /**
   * Formats the message using the fields from the given data-row as values for the parameters.
   *
   * @param dataRow the data row.
   * @return the formated message.
   */
  public String performFormat(final DataRow dataRow)
  {
    if (fields == null)
    {
      return null;
    }
    if (fields.length == 0)
    {
      return formatString;
    }
    
    final boolean fastProcessingPossible = (nullString == null);

    if (formats == null)
    {
      formats = format.getFormats();
    }
    if (fields.length != formats.length)
    {
      Log.warn("There is an error in the format string: " + formatString);
      return null;
    }
    if (parameters == null)
    {
      parameters = new Object[fields.length];
    }
    if (replaced == null)
    {
      replaced = new boolean[fields.length];
    }

    if (oldParameters == null)
    {
      oldParameters = new Object[fields.length];
    }
    else
    {
      System.arraycopy(parameters, 0, oldParameters, 0, parameters.length);
    }
    
    if (oldReplaced == null)
    {
      oldReplaced = new boolean[fields.length];
    }
    else
    {
      System.arraycopy(replaced, 0, oldReplaced, 0, replaced.length);
    }

    boolean fastProcessing = true;
    for (int i = 0; i < parameters.length; i++)
    {
      final Object value = dataRow.get(fields[i]);
      final Format currentFormat = formats[i];
      if (value == null)
      {
        parameters[i] = nullString;
        replaced[i] = currentFormat != null;
        fastProcessing = (fastProcessing && fastProcessingPossible && replaced[i] == false);
      }
      else
      {
        if (currentFormat instanceof DateFormat)
        {
          if (value instanceof Date)
          {
            parameters[i] = value;
            replaced[i] = false;
          }
          else
          {
            parameters[i] = nullString;
            replaced[i] = true;
            fastProcessing = (fastProcessing && fastProcessingPossible && replaced[i] == false);
          }
        }
        else if (currentFormat instanceof NumberFormat)
        {
          if (value instanceof Number)
          {
            parameters[i] = value;
            replaced[i] = false;
          }
          else
          {
            parameters[i] = nullString;
            replaced[i] = true;
            fastProcessing = (fastProcessing && fastProcessingPossible && replaced[i] == false);
          }
        }
        else
        {
          parameters[i] = value;
          replaced[i] = false;
        }
      }
    }

    if (cachedValue != null &&
        Arrays.equals(replaced, oldReplaced) && Arrays.equals(parameters, oldParameters))
    {
      return cachedValue;
    }

    if (fastProcessing)
    {
      cachedValue = format.format(parameters);
      return cachedValue;
    }

    final MessageFormat effectiveFormat = (MessageFormat) format.clone();
    for (int i = 0; i < replaced.length; i++)
    {
      final boolean b = replaced[i];
      if (b)
      {
        effectiveFormat.setFormat(i, null);
      }
    }
    cachedValue = effectiveFormat.format(parameters);
    return cachedValue;
  }

  /**
   * Returns the compiled message format string.
   *
   * @return the compiled message format string.
   */
  public String getCompiledFormat()
  {
    return compiledFormat;
  }

  /**
   * Returns the locale that is used to format the messages.
   *
   * @return the locale in the message format.
   */
  public Locale getLocale()
  {
    return format.getLocale();
  }

  /**
   * Updates the locale that is used to format the messages.
   *
   * @param locale the locale in the message format.
   */
  public void setLocale(final Locale locale)
  {
    if (ObjectUtilities.equal(locale, this.locale))
    {
      return;
    }
    this.locale = locale;
    if (format != null)
    {
      this.format.setLocale(locale);
      this.format.applyPattern(compiledFormat);
    }

    this.formats = null;
    this.parameters = null;
    this.replaced = null;

    this.oldParameters = null;
    this.oldReplaced = null;
    this.cachedValue = null;
  }

  /**
   * Returns the replacement text that is used if one of the referenced message parameters is null.
   *
   * @return the replacement text for null-values.
   */
  public String getNullString()
  {
    return nullString;
  }

  /**
   * Defines the replacement text that is used if one of the referenced message parameters is null.
   *
   * @param nullString the replacement text for null-values.
   */
  public void setNullString(final String nullString)
  {
    if (ObjectUtilities.equal(nullString, this.nullString) == false)
    {
      this.nullString = nullString;

      this.oldParameters = null;
      this.oldReplaced = null;
      this.cachedValue = null;
    }
  }

  /**
   * Creates a copy of this message format support object.
   *
   * @return the copy.
   * @throws CloneNotSupportedException if an error occured.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    final MessageFormatSupport support = (MessageFormatSupport) super.clone();
    if (format != null)
    {
      support.format = (MessageFormat) format.clone();
    }
    if (formats != null)
    {
      support.formats = (Format[]) formats.clone();
    }
    if (replaced != null)
    {
      support.replaced = (boolean[]) replaced.clone();
    }
    if (parameters != null)
    {
      support.parameters = (Object[]) parameters.clone();
    }

    this.oldParameters = null;
    this.oldReplaced = null;
    this.cachedValue = null;
    return support;
  }
}
