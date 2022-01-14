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
 * SerializerHelper.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.report.util.serializers.BasicStrokeSerializer;
import org.jfree.report.util.serializers.ColorSerializer;
import org.jfree.report.util.serializers.Dimension2DSerializer;
import org.jfree.report.util.serializers.Ellipse2DSerializer;
import org.jfree.report.util.serializers.GeneralPathSerializer;
import org.jfree.report.util.serializers.Line2DSerializer;
import org.jfree.report.util.serializers.PageFormatSerializer;
import org.jfree.report.util.serializers.Point2DSerializer;
import org.jfree.report.util.serializers.Rectangle2DSerializer;
import org.jfree.util.ClassComparator;

/**
 * The SerializeHelper is used to make implementing custom serialization handlers easier.
 * Handlers for certain object types need to be added to this helper before this
 * implementation is usable.
 *
 * @author Thomas Morgner
 * @deprecated Use JCommon-Serializer directly. This class will be removed.
 */
public class SerializerHelper
{
  /**
   * The singleton instance of the serialize helper.
   */
  private static SerializerHelper singleton;

  /**
   * Returns or creates a new SerializerHelper. When a new instance is created by this
   * method, all known SerializeMethods are registered.
   *
   * @return the SerializerHelper singleton instance.
   */
  public static synchronized SerializerHelper getInstance ()
  {
    if (singleton == null)
    {
      singleton = new SerializerHelper();
      singleton.registerMethod(new BasicStrokeSerializer());
      singleton.registerMethod(new ColorSerializer());
      singleton.registerMethod(new Dimension2DSerializer());
      singleton.registerMethod(new Ellipse2DSerializer());
      singleton.registerMethod(new Line2DSerializer());
      singleton.registerMethod(new Point2DSerializer());
      singleton.registerMethod(new Rectangle2DSerializer());
      //singleton.registerMethod(new BandLayoutManagerSerializer());
      singleton.registerMethod(new PageFormatSerializer());
      singleton.registerMethod(new GeneralPathSerializer());
    }
    return singleton;
  }

  /**
   * This method can be used to replace the singleton instance of this helper.
   *
   * @param helper the new instance of the serialize helper.
   */
  protected static void setInstance (final SerializerHelper helper)
  {
    singleton = helper;
  }

  /**
   * A collection of the serializer methods.
   */
  private final HashMap methods;

  /**
   * A class comparator for searching the super class of an certain class.
   */
  private final ClassComparator comparator;

  /**
   * Creates a new SerializerHelper.
   */
  protected SerializerHelper ()
  {
    this.comparator = new ClassComparator();
    this.methods = new HashMap();
  }

  /**
   * Registers a new SerializeMethod with this SerializerHelper.
   *
   * @param method the method that should be registered.
   */
  public void registerMethod (final SerializeMethod method)
  {
    this.methods.put(method.getObjectClass(), method);
  }

  /**
   * Deregisters a new SerializeMethod with this SerializerHelper.
   *
   * @param method the method that should be deregistered.
   */
  public void unregisterMethod (final SerializeMethod method)
  {
    this.methods.remove(method.getObjectClass());
  }

  /**
   * Returns the collection of all registered serialize methods.
   *
   * @return a collection of the registered serialize methods.
   */
  protected HashMap getMethods ()
  {
    return methods;
  }

  /**
   * Returns the class comparator instance used to find correct super classes.
   *
   * @return the class comparator.
   */
  protected ClassComparator getComparator ()
  {
    return comparator;
  }

  /**
   * Looks up the SerializeMethod for the given class or null if there is no
   * SerializeMethod for the given class.
   *
   * @param c the class for which we want to lookup a serialize method.
   * @return the method or null, if there is no registered method for the class.
   */
  protected SerializeMethod getSerializer (final Class c)
  {
    final SerializeMethod sm = (SerializeMethod) methods.get(c);
    if (sm != null)
    {
      return sm;
    }
    return getSuperClassObjectDescription(c, null);
  }

  /**
   * Looks up the SerializeMethod for the given class or null if there is no
   * SerializeMethod for the given class. This method searches all superclasses.
   *
   * @param d               the class for which we want to lookup a serialize method.
   * @param knownSuperClass the known super class, if any or null.
   * @return the method or null, if there is no registered method for the class.
   */
  protected SerializeMethod getSuperClassObjectDescription
          (final Class d, SerializeMethod knownSuperClass)
  {
    final Iterator keys = methods.keySet().iterator();
    while (keys.hasNext())
    {
      final Class keyClass = (Class) keys.next();
      if (keyClass.isAssignableFrom(d))
      {
        final SerializeMethod od = (SerializeMethod) methods.get(keyClass);
        if (knownSuperClass == null)
        {
          knownSuperClass = od;
        }
        else
        {
          if (comparator.isComparable
                  (knownSuperClass.getObjectClass(), od.getObjectClass()))
          {
            if (comparator.compare
                    (knownSuperClass.getObjectClass(), od.getObjectClass()) < 0)
            {
              knownSuperClass = od;
            }
          }
        }
      }
    }
    return knownSuperClass;
  }


  /**
   * Writes a serializable object description to the given object output stream. This
   * method selects the best serialize helper method for the given object.
   *
   * @param o   the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject (final Object o, final ObjectOutputStream out)
          throws IOException
  {
    if (o == null)
    {
      out.writeByte(0);
      return;
    }
    if (o instanceof Serializable)
    {
      out.writeByte(1);
      out.writeObject(o);
      return;
    }

    final SerializeMethod m = getSerializer(o.getClass());
    if (m == null)
    {
      throw new NotSerializableException(o.getClass().getName());
    }
    out.writeByte(2);
    out.writeObject(m.getObjectClass());
    m.writeObject(o, out);
  }

  /**
   * Reads the object from the object input stream. This object selects the best
   * serializer to read the object.
   * <p/>
   * Make sure, that you use the same configuration (library and class versions,
   * registered methods in the SerializerHelper) for reading as you used for writing.
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
    final int type = in.readByte();
    if (type == 0)
    {
      return null;
    }
    if (type == 1)
    {
      return in.readObject();
    }
    final Class c = (Class) in.readObject();
    final SerializeMethod m = getSerializer(c);
    if (m == null)
    {
      throw new NotSerializableException(c.getName());
    }
    return m.readObject(in);
  }
}
