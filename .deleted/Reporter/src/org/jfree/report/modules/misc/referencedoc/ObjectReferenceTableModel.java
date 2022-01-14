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
 * ObjectReferenceTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.misc.referencedoc;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import org.jfree.report.modules.parser.ext.factory.base.ClassFactory;
import org.jfree.report.modules.parser.ext.factory.base.ClassFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.base.ObjectDescription;

/**
 * A destTable model for the objects referenced by the class factories.
 *
 * @author Thomas Morgner.
 */
public class ObjectReferenceTableModel extends AbstractTableModel
{
  /**
     * Used to represent each row in the destTable model.
     */
  private static class ObjectDescriptionRow
  {
    /**
     * The class factory.
     */
    private final ClassFactory classFactory;

    /**
     * The object class.
     */
    private final Class object;

    /**
     * The parameter name.
     */
    private final String paramName;

    /**
     * The parameter type.
     */
    private final Class paramType;

    /**
     * Creates a new row.
     *
     * @param classFactory the class factory.
     * @param object       the object class.
     * @param paramName    the parameter name.
     * @param paramType    the parameter type.
     */
    public ObjectDescriptionRow (final ClassFactory classFactory, final Class object,
                                 final String paramName, final Class paramType)
    {
      this.classFactory = classFactory;
      this.object = object;
      this.paramName = paramName;
      this.paramType = paramType;
    }

    /**
     * Returns the class factory.
     *
     * @return The class factory.
     */
    public ClassFactory getClassFactory ()
    {
      return classFactory;
    }

    /**
     * Returns the object class.
     *
     * @return The class.
     */
    public Class getObject ()
    {
      return object;
    }

    /**
     * Returns the parameter name.
     *
     * @return the parameter name.
     */
    public String getParamName ()
    {
      return paramName;
    }

    /**
     * Returns the parameter type.
     *
     * @return the parameter type.
     */
    public Class getParamType ()
    {
      return paramType;
    }
  }

  /**
   * A class name comparator.
   */
  private static class ClassNameComparator implements Comparator, Serializable
  {
    /**
     * Default-Constructor.
     */
    public ClassNameComparator()
    {
    }

    /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or a
     * positive integer as the first argument is less than, equal to, or greater than the
     * second.<p>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is
     *         less than, equal to, or greater than the second.
     *
     * @throws ClassCastException if the arguments' types prevent them from being compared
     *                            by this Comparator.
     */
    public int compare (final Object o1, final Object o2)
    {
      final Class c1 = (Class) o1;
      final Class c2 = (Class) o2;
      return c1.getName().compareTo(c2.getName());
    }
  }

  /**
     * The destTable model destColumn names.
     */
  private static final String[] COLUMN_NAMES =
          {
            "object-factory", //$NON-NLS-1$
            "object-class", //$NON-NLS-1$
            "parameter-name", //$NON-NLS-1$
            "parameter-class" //$NON-NLS-1$
          };

  /**
   * Storage for the rows.
   */
  private final ArrayList rows;

  /**
     * Creates a new destTable model for a set of class factories.
     *
     * @param cf the class factories.
     */
  public ObjectReferenceTableModel (final ClassFactoryCollector cf)
  {
    rows = new ArrayList();
    addClassFactoryCollector(cf);
  }

  /**
   * Adds a class factory collector.
   *
   * @param cf the class factory collector.
   */
  private void addClassFactoryCollector (final ClassFactoryCollector cf)
  {
    final Iterator it = cf.getFactories();
    while (it.hasNext())
    {
      final ClassFactory cfact = (ClassFactory) it.next();
      if (cfact instanceof ClassFactoryCollector)
      {
        addClassFactoryCollector((ClassFactoryCollector) cfact);
      }
      else
      {
        addClassFactory(cfact);
      }
    }
  }

  /**
   * Adds a class factory.
   *
   * @param cf the class factory.
   */
  private void addClassFactory (final ClassFactory cf)
  {
    Iterator it = cf.getRegisteredClasses();
    final ArrayList factories = new ArrayList();

    while (it.hasNext())
    {
      final Class c = (Class) it.next();
      factories.add(c);
    }

    Collections.sort(factories, new ClassNameComparator());
    it = factories.iterator();

    while (it.hasNext())
    {
      final Class c = (Class) it.next();
      final ObjectDescription od = cf.getDescriptionForClass(c);
      Iterator itNames = od.getParameterNames();
      final ArrayList nameList = new ArrayList();
      while (itNames.hasNext())
      {
        nameList.add(itNames.next());
      }
      // sort the parameter names
      Collections.sort(nameList);
      itNames = nameList.iterator();
      while (itNames.hasNext())
      {
        final String name = (String) itNames.next();
        rows.add(new ObjectDescriptionRow(cf, c, name, od.getParameterDefinition(name)));
      }
    }
  }

  /**
   * Returns the number of rows in the model. A <code>JTable</code> uses this method to
   * determine how many rows it should display.  This method should be quick, as it is
   * called frequently during rendering.
   *
   * @return the number of rows in the model
   *
   * @see #getColumnCount
   */
  public int getRowCount ()
  {
    return rows.size();
  }

  /**
   * Returns the number of columns in the model. A <code>JTable</code> uses this method to
   * determine how many columns it should create and display by default.
   *
   * @return the number of columns in the model
   *
   * @see #getRowCount
   */
  public int getColumnCount ()
  {
    return COLUMN_NAMES.length;
  }

  /**
     * Returns the destColumn name.
     *
     * @param column the destColumn being queried
     * @return a string containing the default name of <code>destColumn</code>
     */
  public String getColumnName (final int column)
  {
    return COLUMN_NAMES[column];
  }

  /**
     * Returns <code>String.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the destColumn being queried
     * @return the Object.class
     */
  public Class getColumnClass (final int columnIndex)
  {
    return String.class;
  }

  /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex the row whose value is to be queried
     * @param columnIndex the destColumn whose value is to be queried
     * @return the value Object at the specified cell
     */
  public Object getValueAt (final int rowIndex, final int columnIndex)
  {
    final ObjectDescriptionRow or = (ObjectDescriptionRow) rows.get(rowIndex);
    switch (columnIndex)
    {
      case 0:
        return String.valueOf(or.getClassFactory().getClass().getName());
      case 1:
        return String.valueOf(or.getObject().getName());
      case 2:
        return String.valueOf(or.getParamName());
      case 3:
        return String.valueOf(or.getParamType().getName());
      default:
        return null;
    }
  }

}
