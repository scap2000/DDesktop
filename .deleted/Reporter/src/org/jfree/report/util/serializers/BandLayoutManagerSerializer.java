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
 * BandLayoutManagerSerializer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util.serializers;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.report.layout.BandLayoutManager;
import org.jfree.serializer.SerializeMethod;
import org.jfree.util.ObjectUtilities;

/**
 * A SerializeMethod implementation that handles BandLayoutManagers.
 *
 * @author Thomas Morgner
 * @see org.jfree.report.layout.BandLayoutManager
 */
public class BandLayoutManagerSerializer implements SerializeMethod
{
  /**
   * Default Constructor.
   */
  public BandLayoutManagerSerializer ()
  {
  }

  /**
   * Writes a serializable object description to the given object output stream. As
   * bandlayoutmanagers need to be instantiable by their default constructor, it is
   * sufficient to write the class of the layout manager.
   *
   * @param o   the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject (final Object o, final ObjectOutputStream out)
          throws IOException
  {
    out.writeObject(o.getClass().getName());
  }

  /**
   * Reads the object from the object input stream. This will read a serialized class name
   * of the BandLayoutManager. The specified class is then instantiated using its default
   * constructor.
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
    final String cn = (String) in.readObject();

    try
    {
      return ObjectUtilities.loadAndInstantiate
              (cn, BandLayoutManagerSerializer.class);
    }
    catch (Exception e)
    {
      throw new NotSerializableException(cn);
    }
  }

  /**
   * The class of the object, which this object can serialize.
   *
   * @return the class <code>org.jfree.report.layout.BandLayoutManager</code>.
   */
  public Class getObjectClass ()
  {
    return BandLayoutManager.class;
  }
}
