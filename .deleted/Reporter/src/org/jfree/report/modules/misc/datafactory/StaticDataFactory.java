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
 * StaticDataFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.misc.datafactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.swing.table.TableModel;

import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.util.CSVTokenizer;
import org.jfree.util.ObjectUtilities;

/**
 * This report data factory uses introspection to search for a report data
 * source. The labelQuery can have the following formats:
 * <p/>
 * &lt;full-qualified-classname&gt;#methodName(Parameters)
 * &lt;full-qualified-classname&gt;(constructorparams)#methodName(Parameters)
 * &lt;full-qualified-classname&gt;(constructorparams)
 *
 * @author Thomas Morgner
 */
public class StaticDataFactory implements DataFactory, Cloneable
{
  private static final String[] EMPTY_PARAMS = new String[0];

  /**
   * DefaultConstructor.
   */
  public StaticDataFactory()
  {
  }

  /**
     * Queries a datasource. The string 'labelQuery' defines the name of the labelQuery. The
     * Parameterset given here may contain more data than actually needed.
     * <p/>
     * The dataset may change between two calls, do not assume anything!
     *
     * @param query the method call.
     * @param parameters the set of parameters.
     * @return the tablemodel from the executed method call, never null.
     */
  public TableModel queryData(final String query, final DataRow parameters)
      throws ReportDataFactoryException
  {
    final int methodSeparatorIdx = query.indexOf('#');

    if ((methodSeparatorIdx + 1) >= query.length())
    {
      // If we have a method separator, then it cant be at the end of the text.
      throw new ReportDataFactoryException("Malformed query: " + query); //$NON-NLS-1$
    }

    if (methodSeparatorIdx == -1)
    {
      // we have no method. So this query must be a reference to a tablemodel
      // instance.
      final int parameterStartIdx = query.indexOf('(');
      final String[] parameterNames;
      final String constructorName;
      if (parameterStartIdx == -1)
      {
        parameterNames = EMPTY_PARAMS;
        constructorName = query;
      }
      else
      {
        parameterNames = createParameterList(query, parameterStartIdx);
        constructorName = query.substring(0, parameterStartIdx);
      }

      try
      {
        final Constructor c = findDirectConstructor(constructorName, parameterNames.length);

        final Object[] params = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++)
        {
          final String name = parameterNames[i];
          params[i] = parameters.get(name);
        }
        return (TableModel) c.newInstance(params);
      }
      catch (Exception e)
      {
        throw new ReportDataFactoryException
            ("Unable to instantiate class for non static call.", e); //$NON-NLS-1$
      }
    }

    return createComplexTableModel
        (query, methodSeparatorIdx, parameters);
  }

  /**
     * Performs a complex labelQuery, where the tablemodel is retrieved from an method
     * that was instantiated using parameters.
     *
     * @param query the labelQuery-string that contains the method to call.
     * @param methodSeparatorIdx the position where the method specification starts.
     * @param parameters the set of parameters.
     * @return the resulting tablemodel, never null.
     * @throws ReportDataFactoryException if something goes wrong.
     */
  private TableModel createComplexTableModel(final String query,
                                             final int methodSeparatorIdx,
                                             final DataRow parameters)
      throws ReportDataFactoryException
  {
    final String constructorSpec = query.substring(0, methodSeparatorIdx);
    final int constParamIdx = constructorSpec.indexOf('(');
    if (constParamIdx == -1)
    {
      // Either a static call or a default constructor call..
      return loadFromDefaultConstructor(query, methodSeparatorIdx, parameters);
    }

    // We have to find a suitable constructor ..
    final String className = query.substring(0, constParamIdx);
    final String[] parameterNames = createParameterList(constructorSpec, constParamIdx);
    final Constructor c = findIndirectConstructor(className, parameterNames.length);

    final String methodQuery = query.substring(methodSeparatorIdx + 1);
    final String[] methodParameterNames;
    final String methodName;
    final int parameterStartIdx = methodQuery.indexOf('(');
    if (parameterStartIdx == -1)
    {
      // no parameters. Nice.
      methodParameterNames = EMPTY_PARAMS;
      methodName = methodQuery;
    }
    else
    {
      methodName = methodQuery.substring(0, parameterStartIdx);
      methodParameterNames = createParameterList(methodQuery, parameterStartIdx);
    }
    final Method m = findCallableMethod(className, methodName, methodParameterNames.length);

    try
    {
      final Object[] constrParams = new Object[parameterNames.length];
      for (int i = 0; i < parameterNames.length; i++)
      {
        final String name = parameterNames[i];
        constrParams[i] = parameters.get(name);
      }
      final Object o = c.newInstance(constrParams);

      final Object[] methodParams = new Object[methodParameterNames.length];
      for (int i = 0; i < methodParameterNames.length; i++)
      {
        final String name = methodParameterNames[i];
        methodParams[i] = parameters.get(name);
      }
      final Object data = m.invoke(o, methodParams);
      return (TableModel) data;
    }
    catch (Exception e)
    {
      throw new ReportDataFactoryException
          ("Unable to instantiate class for non static call."); //$NON-NLS-1$
    }
  }

  /**
     * Loads a tablemodel from a parameterless class or method. Call does not
     * use any parameters.
     *
     * @param query the labelQuery-string that contains the method to call.
     * @param methodSeparatorIdx the position where the method specification starts.
     * @param parameters the set of parameters.
     * @return the resulting tablemodel, never null.
     * @throws ReportDataFactoryException if something goes wrong.
     */
  private TableModel loadFromDefaultConstructor(final String query,
                                                final int methodSeparatorIdx,
                                                final DataRow parameters)
      throws ReportDataFactoryException
  {
    final String className = query.substring(0, methodSeparatorIdx);

    final String methodSpec = query.substring(methodSeparatorIdx + 1);
    final String methodName;
    final String[] parameterNames;
    final int parameterStartIdx = methodSpec.indexOf('(');
    if (parameterStartIdx == -1)
    {
      // no parameters. Nice.
      parameterNames = EMPTY_PARAMS;
      methodName = methodSpec;
    }
    else
    {
      parameterNames = createParameterList(methodSpec, parameterStartIdx);
      methodName = methodSpec.substring(0, parameterStartIdx);
    }

    try
    {
      final Method m = findCallableMethod(className, methodName, parameterNames.length);
      final Object[] params = new Object[parameterNames.length];
      for (int i = 0; i < parameterNames.length; i++)
      {
        final String name = parameterNames[i];
        params[i] = parameters.get(name);
      }

      if (Modifier.isStatic(m.getModifiers()))
      {
        final Object o = m.invoke(null, params);
        return (TableModel) o;
      }

      final ClassLoader classLoader = getClassLoader();
      final Class c = classLoader.loadClass(className);
      final Object o = c.newInstance();
      if (o == null)
      {
        throw new ReportDataFactoryException
            ("Unable to instantiate class for non static call."); //$NON-NLS-1$
      }
      final Object data = m.invoke(o, params);
      return (TableModel) data;
    }
    catch (ReportDataFactoryException rdfe)
    {
      throw rdfe;
    }
    catch (Exception e)
    {
      throw new ReportDataFactoryException
          ("Something went terribly wrong: ", e); //$NON-NLS-1$
    }
  }

  /**
     * Creates the list of destColumn names that should be mapped into the method
     * or constructor parameters.
     *
     * @param query the labelQuery-string.
     * @param parameterStartIdx the index from where to read the parameter list.
     * @return an array with destColumn names.
     * @throws ReportDataFactoryException if something goes wrong.
     */
  private String[] createParameterList(final String query,
                                       final int parameterStartIdx)
      throws ReportDataFactoryException
  {
    final int parameterEndIdx = query.lastIndexOf(')');
    if (parameterEndIdx < parameterStartIdx)
    {
      throw new ReportDataFactoryException("Malformed query: " + query); //$NON-NLS-1$
    }
    final String parameterText =
        query.substring(parameterStartIdx + 1, parameterEndIdx);
    final CSVTokenizer tokenizer = new CSVTokenizer(parameterText);
    final int size = tokenizer.countTokens();
    final String[] parameterNames = new String[size];
    int i = 0;
    while (tokenizer.hasMoreTokens())
    {
      parameterNames[i] = tokenizer.nextToken();
      i += 1;
    }
    return parameterNames;
  }

  /**
   * Returns the current classloader.
   *
   * @return the current classloader.
   */
  protected ClassLoader getClassLoader()
  {
    return ObjectUtilities.getClassLoader(StaticDataFactory.class);
  }

  /**
   * Tries to locate a method-object for the call. This method will
   * throw an Exception if the method was not found or not public.
   *
   * @param className  the name of the class where to seek the method.
   * @param methodName the name of the method.
   * @param paramCount the parameter count of the method we seek.
   * @return the method object.
   * @throws ReportDataFactoryException if something goes wrong.
   */
  private Method findCallableMethod(final String className,
                                    final String methodName,
                                    final int paramCount)
      throws ReportDataFactoryException
  {
    final ClassLoader classLoader = getClassLoader();

    if (classLoader == null)
    {
      throw new ReportDataFactoryException("No classloader!"); //$NON-NLS-1$
    }
    try
    {
      final Class c = classLoader.loadClass(className);
      if (Modifier.isAbstract(c.getModifiers()))
      {
        throw new ReportDataFactoryException("Abstract class cannot be handled!"); //$NON-NLS-1$
      }

      final Method[] methods = c.getMethods();
      for (int i = 0; i < methods.length; i++)
      {
        final Method method = methods[i];
        if (Modifier.isPublic(method.getModifiers()) == false)
        {
          continue;
        }
        if (method.getName().equals(methodName) == false)
        {
          continue;
        }
        final Class returnType = method.getReturnType();
        if (TableModel.class.isAssignableFrom(returnType) == false)
        {
          continue;
        }
        if (method.getParameterTypes().length != paramCount)
        {
          continue;
        }
        return method;
      }
    }
    catch (ClassNotFoundException e)
    {
      throw new ReportDataFactoryException("No such Class", e); //$NON-NLS-1$
    }
    throw new ReportDataFactoryException("No such Method: " + className + '#' + methodName); //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Tries to locate a suitable public constructor for the number of parameters.
   * This will return the first constructor that matches, no matter whether the
   * parameter types will match too.
   *
   * The Class that is referenced must be a Tablemodel implementation.
   *
   * @param className  the classname on where to find the constructor.
   * @param paramCount the number of parameters expected in the constructor.
   * @return the Constructor object, never null.
   * @throws ReportDataFactoryException if the constructor could not be found
   *                                    or something went wrong.
   */
  private Constructor findDirectConstructor(final String className,
                                            final int paramCount)
      throws ReportDataFactoryException
  {
    final ClassLoader classLoader = getClassLoader();
    if (classLoader == null)
    {
      throw new ReportDataFactoryException("No classloader!"); //$NON-NLS-1$
    }

    try
    {
      final Class c = classLoader.loadClass(className);
      if (TableModel.class.isAssignableFrom(c) == false)
      {
        throw new ReportDataFactoryException("The specified class must be either a TableModel or a ReportData implementation."); //$NON-NLS-1$
      }
      if (Modifier.isAbstract(c.getModifiers()))
      {
        throw new ReportDataFactoryException("The specified class cannot be instantiated: it is abstract."); //$NON-NLS-1$
      }

      final Constructor[] methods = c.getConstructors();
      for (int i = 0; i < methods.length; i++)
      {
        final Constructor method = methods[i];
        if (Modifier.isPublic(method.getModifiers()) == false)
        {
          continue;
        }
        if (method.getParameterTypes().length != paramCount)
        {
          continue;
        }
        return method;
      }
    }
    catch (ClassNotFoundException e)
    {
      throw new ReportDataFactoryException("No such Class", e); //$NON-NLS-1$
    }
    throw new ReportDataFactoryException
        ("There is no constructor in class " + className + //$NON-NLS-1$
            " that accepts " + paramCount + " parameters."); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * Tries to locate a constructor that accepts the specified number of
   * parameters. The referenced class can be of any type, as we will call
   * a method on that class that will return the tablemodel for us.
   *
   * @param className the classname of the class where to search the constructor.
   * @param paramCount the numbers of parameters expected.
   * @return the constructor object, never null.
   * @throws ReportDataFactoryException if the constructor could not be found
   *                                    or something went wrong.
   */
  private Constructor findIndirectConstructor(final String className,
                                              final int paramCount)
      throws ReportDataFactoryException
  {
    final ClassLoader classLoader = getClassLoader();
    if (classLoader == null)
    {
      throw new ReportDataFactoryException("No classloader!"); //$NON-NLS-1$
    }

    try
    {
      final Class c = classLoader.loadClass(className);
      if (Modifier.isAbstract(c.getModifiers()))
      {
        throw new ReportDataFactoryException("The specified class cannot be instantiated: it is abstract."); //$NON-NLS-1$
      }

      final Constructor[] methods = c.getConstructors();
      for (int i = 0; i < methods.length; i++)
      {
        final Constructor method = methods[i];
        if (Modifier.isPublic(method.getModifiers()) == false)
        {
          continue;
        }
        if (method.getParameterTypes().length != paramCount)
        {
          continue;
        }
        return method;
      }
    }
    catch (ClassNotFoundException e)
    {
      throw new ReportDataFactoryException("No such Class", e); //$NON-NLS-1$
    }
    throw new ReportDataFactoryException
        ("There is no constructor in class " + className + //$NON-NLS-1$
            " that accepts " + paramCount + " parameters."); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * Derives the factory. In fact, this returns the this-reference, as
   * this factory is a stateless class.
   *
   * @return the derived factory.
   * @throws ReportDataFactoryException in case something goes wrong.
   */
  public DataFactory derive() throws ReportDataFactoryException
  {
    return this;
  }


  /**
   * Opens the data factory. This initializes everything. Performing queries
   * on data factories which have not yet been opened will result in exceptions.
   *
   * This method does nothing at all.
   */
  public void open()
  {

  }

  /**
   * Closes the data factory and frees all resources held by this instance.
   *
   * This method is empty.
   */
  public void close()
  {

  }

  /**
   * Returns a clone of the factory.
   *
   * @return the clone.
   * @throws CloneNotSupportedException if cloning failed.
   */
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
