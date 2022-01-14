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
 * StyleKey.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.style;

import java.io.ObjectStreamException;
import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.report.JFreeReportBoot;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * A style key represents a (key, class) pair.  Style keys are used to access style
 * attributes defined in a <code>BandStyleSheet</code> or an <code>ElementStyleSheet</code>
 * <p/>
 * Note that this class also defines a static Hashtable in which all defined keys are
 * stored.
 *
 * @author Thomas Morgner
 * @see BandStyleKeys
 * @see ElementStyleSheet
 */
public final class StyleKey implements Serializable, Cloneable
{
  /**
   * Shared storage for the defined keys.
   */
  private static HashMap definedKeys;
  private static int definedKeySize;

  /**
   * The name of the style key.
   */
  private String name;

  /**
   * The class of the value.
   */
  private Class valueType;

  /**
   * A unique int-key for the stylekey.
   */
  private int identifier;

  /**
   * Whether this stylekey is transient. Transient keys will not be written when
   * serializing a report.
   */
  private boolean trans;

  /**
   * Whether this stylekey is inheritable.
   */
  private boolean inheritable;

  /**
   * Creates a new style key.
   *
   * @param name      the name (never null).
   * @param valueType the class of the value for this key (never null).
   */
  private StyleKey (final String name,
                    final Class valueType,
                    final boolean trans,
                    final boolean inheritable)
  {
    setName(name);
    setValueType(valueType);
    this.trans = trans;
    this.inheritable = inheritable;
  }

  /**
   * Returns the name of the key.
   *
   * @return the name.
   */
  public String getName ()
  {
    return name;
  }

  /**
   * Sets the name of the key.
   *
   * @param name the name (null not permitted).
   */
  private void setName (final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("StyleKey.setName(...): null not permitted.");
    }
    this.name = name;
    this.identifier = definedKeys.size();
  }

  /**
   * Returns the class of the value for this key.
   *
   * @return the class.
   */
  public Class getValueType ()
  {
    return valueType;
  }

  /**
   * Sets the class of the value for this key.
   *
   * @param valueType the class.
   */
  private void setValueType (final Class valueType)
  {
    if (valueType == null)
    {
      throw new NullPointerException("ValueType must not be null");
    }
    this.valueType = valueType;
  }

  /**
   * Returns the key with the specified name. The given type is not checked against a
   * possibly alredy defined definition, it is assumed that the type is only given for a
   * new key definition.
   *
   * @param name      the name.
   * @param valueType the class.
   * @return the style key.
   */
  public static StyleKey getStyleKey (final String name, final Class valueType)
  {
    return getStyleKey(name, valueType, false, true);
  }

  /**
   * Returns the key with the specified name. The given type is not checked against a
   * possibly alredy defined definition, it is assumed that the type is only given for a
   * new key definition.
   *
   * @param name      the name.
   * @param valueType the class.
   * @return the style key.
   */
  public static synchronized StyleKey getStyleKey (final String name,
                                                   final Class valueType,
                                                   final boolean trans,
                                                   final boolean inheritable)
  {
    if (definedKeys == null)
    {
      definedKeys = new HashMap();
      definedKeySize = 0;
    }
    StyleKey key = (StyleKey) definedKeys.get(name);
    if (key == null)
    {
      key = new StyleKey(name, valueType, trans, inheritable);
      definedKeys.put(name, key);
      definedKeySize = definedKeys.size();
    }
    return key;
  }

  /**
   * Returns the key with the specified name.
   *
   * @param name the name.
   * @return the style key.
   */
  public static synchronized StyleKey getStyleKey (final String name)
  {
    if (definedKeys == null)
    {
      return null;
    }
    else
    {
      return (StyleKey) definedKeys.get(name);
    }
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param o the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof StyleKey))
    {
      return false;
    }

    final StyleKey key = (StyleKey) o;

    if (!name.equals(key.name))
    {
      return false;
    }
    if (!valueType.equals(key.valueType))
    {
      return false;
    }

    return true;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the benefit of
   * hashtables such as those provided by <code>java.util.Hashtable</code>.
   * <p/>
   *
   * @return a hash code value for this object.
   */
  public int hashCode ()
  {
    return identifier;
  }

  /**
   * Replaces the automaticly generated instance with one of the defined stylekey
   * instances or creates a new stylekey.
   *
   * @return the resolved element
   *
   * @throws ObjectStreamException if the element could not be resolved.
   */
  private Object readResolve ()
          throws ObjectStreamException
  {
    synchronized (StyleKey.class)
    {
      final StyleKey key = getStyleKey(name);
      if (key != null)
      {
        return key;
      }
      return getStyleKey(name, valueType, trans, inheritable);
    }
  }

  public boolean isTransient ()
  {
    return trans;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object.
   */
  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("StyleKey={name='");
    b.append(getName());
    b.append("', valueType='");
    b.append(getValueType());
    b.append("'}");
    return b.toString();
  }

  public Object clone ()
          throws CloneNotSupportedException
  {
    return super.clone();
  }

  public boolean isInheritable ()
  {
    return inheritable;
  }

  public int getIdentifier ()
  {
    return identifier;
  }

  public static int getDefinedStyleKeyCount ()
  {
    return definedKeySize;
  }

  public static synchronized  StyleKey[] getDefinedStyleKeys ()
  {
    if (definedKeys == null)
    {
      Log.warn ("The engine has not been booted and the default keys have no been registered yet.");
      registerDefaults();
    }
    return (StyleKey[]) definedKeys.values().toArray (new StyleKey[definedKeys.size()]);
  }


  public static synchronized void registerDefaults()
  {
    final Configuration config = JFreeReportBoot.getInstance().getGlobalConfig();
    final Iterator it = config.findPropertyKeys("org.jfree.report.stylekeys.");
    final ClassLoader classLoader =ObjectUtilities.getClassLoader(StyleKey.class);

    while (it.hasNext())
    {
      final String key = (String) it.next();
      final String keyClass = config.getConfigProperty(key);
      try
      {
        final Class c = classLoader.loadClass(keyClass);
        registerClass(c);
      }
      catch (ClassNotFoundException e)
      {
        // ignore that class
        Log.warn ("Unable to register keys from " + keyClass);
      }
      catch (NullPointerException e)
      {
        // ignore invalid values as well.
        Log.warn ("Unable to register keys from " + keyClass);
      }
    }

  }

  public static synchronized void registerClass(final Class c)
  {
    // Log.debug ("Registering stylekeys from " + c);
    try
    {
      final Field[] fields = c.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        final Field field = fields[i];
        final int modifiers = field.getModifiers();
        if (Modifier.isPublic(modifiers) &&
            Modifier.isStatic(modifiers))
        {
          if (Modifier.isFinal(modifiers) == false)
          {
            Log.warn ("Invalid implementation: StyleKeys should be 'public static final': " + c);
          }
          if (field.getType().isAssignableFrom(StyleKey.class))
          {
            final StyleKey value = (StyleKey) field.get(null);
            // ignore the returned value, all we want is to trigger the key
            // creation
            // Log.debug ("Loaded key " + value);
          }
        }
      }
    }
    catch (IllegalAccessException e)
    {
      // wont happen, we've checked it..
      Log.warn ("Unable to register keys from " + c.getName());
    }
  }

}
