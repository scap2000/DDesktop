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
 * ColorSerializer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util.serializers;

import java.awt.Color;
import java.awt.color.ColorSpace;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.report.util.SerializeMethod;

/**
 * A SerializeMethod implementation that handles Colors.
 *
 * @author Thomas Morgner
 * @see Color
 * @deprecated This class was never needed.
 */
public class ColorSerializer implements SerializeMethod
{
  /**
   * Default Constructor.
   */
  public ColorSerializer ()
  {
  }

  /**
   * Writes a serializable object description to the given object output stream. This
   * writes the color components, the alpha channel and the color space.
   *
   * @param o   the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject (final Object o, final ObjectOutputStream out)
          throws IOException
  {
    final Color c = (Color) o;
    final float[] components = c.getColorComponents(null);
    final float alpha = c.getAlpha() / 255.0f;
    out.writeObject(c.getColorSpace());
    out.writeObject(components);
    out.writeFloat(alpha);
  }

  /**
   * Reads the object from the object input stream. This reads the color components, the
   * alpha channel and the color space and constructs a new java.awt.Color instance.
   *
   * @param in the object input stream from where to read the serialized data.
   * @return the generated object.
   *
   * @throws IOException            if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public Object readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    final ColorSpace csp = (ColorSpace) in.readObject();
    final float[] components = (float[]) in.readObject();
    final float alpha = in.readFloat();
    return new Color(csp, components, alpha);
  }

  /**
   * Returns the class of the object, which this object can serialize.
   *
   * @return the class of java.awt.Color.
   */
  public Class getObjectClass ()
  {
    return Color.class;
  }
}
