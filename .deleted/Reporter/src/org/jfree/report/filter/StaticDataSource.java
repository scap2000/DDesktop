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
 * StaticDataSource.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.jfree.report.function.ExpressionRuntime;
import org.jfree.serializer.SerializerHelper;

/**
 * A data source that returns a constant value.  An example is a label on a report.
 *
 * @author Thomas Morgner
 */
public class StaticDataSource implements DataSource
{
  /**
   * The value.
   */
  private transient Object value;

  /**
   * Default constructor.
   */
  public StaticDataSource ()
  {
  }

  /**
   * Constructs a new static data source.
   *
   * @param o The value.
   */
  public StaticDataSource (final Object o)
  {
    setValue(o);
  }

  /**
   * Sets the value of the data source.
   *
   * @param o The value.
   */
  public void setValue (final Object o)
  {
    this.value = o;
  }

  /**
   * Returns the value set in this datasource. This method exists to make the value-property beanified.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return value;
  }

  /**
   * Returns the value of the data source.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return The value.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    return value;
  }

  /**
   * Clones the data source, although the enclosed 'static' value is not cloned.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Helper method for serialization.
   *
   * @param out the output stream where to write the object.
   * @throws IOException if errors occur while writing the stream.
   */
  private void writeObject (final ObjectOutputStream out)
          throws IOException
  {
    out.defaultWriteObject();
    SerializerHelper.getInstance().writeObject(value, out);
  }

  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws IOException            when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object could
   *                                not be found.
   */
  private void readObject (final java.io.ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    value = SerializerHelper.getInstance().readObject(in);
  }
}
