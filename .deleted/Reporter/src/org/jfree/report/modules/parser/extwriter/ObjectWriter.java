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
 * ObjectWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jfree.report.modules.parser.ext.ExtParserModule;
import org.jfree.report.modules.parser.ext.factory.base.ClassFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.base.ObjectDescription;
import org.jfree.report.modules.parser.ext.factory.base.ObjectFactoryException;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * A writer.
 *
 * @author Thomas Morgner.
 */
public class ObjectWriter extends AbstractXMLDefinitionWriter
{
  /**
   * The object description.
   */
  private ObjectDescription objectDescription;

  /**
   * The object factory.
   */
  private ClassFactoryCollector cc;


  /**
   * Creates a new writer.
   *
   * @param reportWriter      the report writer.
   * @param baseObject        the base object (<code>null</code> not permitted).
   * @param objectDescription the object description (<code>null</code> not permitted) for
   *                          the to be written object. The base object will be used to
   *                          fill the object description parameters.
   * @param indentLevel       the current indention level.
   * @param commentHintPath   the comment hint path used to store additional comments.
   * @throws ReportWriterException if no writer could be found for the given baseObject.
   */
  public ObjectWriter (final ReportWriterContext reportWriter,
                       final Object baseObject,
                       final ObjectDescription objectDescription,
                       final XmlWriter indentLevel)
          throws ReportWriterException
  {
    this(reportWriter, objectDescription, indentLevel);
    if (baseObject == null)
    {
      throw new NullPointerException("BaseObject is null");
    }
    try
    {
      objectDescription.setParameterFromObject(baseObject);
    }
    catch (ObjectFactoryException ofe)
    {
      throw new ReportWriterException("Failed to fill ObjectDescription", ofe);
    }
  }

  /**
   * Creates a new object writer for the given object description.
   *
   * @param reportWriter      the report writer used to write the generated description.
   * @param objectDescription the object description that should be written. It is
   *                          assumed, that the object description is completly
   *                          initialized for writing.
   * @param indentLevel       the current code indention level
   * @param commentHintPath   the comment hint path used to write the comments from the
   *                          ext-parser.
   */
  public ObjectWriter (final ReportWriterContext reportWriter,
                       final ObjectDescription objectDescription,
                       final XmlWriter indentLevel)
  {

    super(reportWriter, indentLevel);
    if (objectDescription == null)
    {
      throw new NullPointerException("ObjectDescription is null");
    }

    this.objectDescription = objectDescription;
    cc = getReportWriter().getClassFactoryCollector();
  }

  /**
   * Returns the object description.
   *
   * @return The object description.
   */
  public ObjectDescription getObjectDescription ()
  {
    return objectDescription;
  }

  /**
   * Returns the object factory.
   *
   * @return The object factory.
   */
  public ClassFactoryCollector getClassFactoryCollector ()
  {
    return cc;
  }

  /**
   * Writes the description.
   *
   * @param writer the writer.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if the object could not be written.
   */
  public void write ()
          throws IOException, ReportWriterException
  {
    final Iterator names = objectDescription.getParameterNames();
    while (names.hasNext())
    {
      final String name = (String) names.next();
      writeParameter(name);
    }
  }

  /**
   * Returns a description of a parameter.
   *
   * @param name the parameter name.
   * @return The description.
   */
  protected ObjectDescription getParameterDescription (final String name)
  {

    // Try to find the object description directly ...
    // by looking at the given object. This is the most accurate
    // option ...
    ObjectDescription parameterDescription;
    final Object o = objectDescription.getParameter(name);
    if (o != null)
    {
      parameterDescription = cc.getDescriptionForClass(o.getClass());
      if (parameterDescription == null)
      {
        parameterDescription = cc.getSuperClassObjectDescription(o.getClass(), null);
      }
      else
      {
        return parameterDescription;
      }
    }
    else
    {
      final Class parameterClass = objectDescription.getParameterDefinition(name);
      parameterDescription = cc.getDescriptionForClass(parameterClass);

      if (parameterDescription != null)
      {
        return parameterDescription;
      }

      // try to find the super class of the parameter object.
      // maybe this can be used to save the object....
      parameterDescription = cc.getSuperClassObjectDescription(parameterClass, null);
    }

    if (parameterDescription == null)
    {
      Log.info("Unable to get parameter description for parameter: " + name);
    }
    return parameterDescription;
  }

  /**
   * Writes a parameter to XML.
   *
   * @param writer        the writer.
   * @param parameterName the parameter name.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if transforming the report into a stream failed.
   */
  protected void writeParameter (final String parameterName)
          throws IOException, ReportWriterException
  {
    final Object parameterValue = getObjectDescription().getParameter(parameterName);
    if (parameterValue == null)
    {
      // Log.info ("Parameter '" + parameterName + "' is null. The Parameter will not be defined.");
      return;
    }

    final Class parameterDefinition = getObjectDescription().getParameterDefinition(parameterName);
    final ObjectDescription parameterDescription = getParameterDescription(parameterName);
    if (parameterDescription == null)
    {
      throw new ReportWriterException("Unable to get Parameter description for "
              + getObjectDescription().getObjectClass() + " Parameter: " + parameterName);
    }

    try
    {
      parameterDescription.setParameterFromObject(parameterValue);
    }
    catch (ObjectFactoryException ofe)
    {
      throw new ReportWriterException
          ("Unable to fill parameter object:" + parameterName, ofe);
    }

    final List parameterNames = getParameterNames(parameterDescription);
    if (parameterNames.isEmpty())
    {
      return;
    }

    final AttributeList p = new AttributeList();
    p.setAttribute(ExtParserModule.NAMESPACE, "name", parameterName);
    if (isUseParameterObjectDescription(parameterDefinition, parameterValue) == false)
    {
      p.setAttribute(ExtParserModule.NAMESPACE, "class", parameterValue.getClass().getName());
    }

    final XmlWriter writer = getXmlWriter();
    if (isBasicObject(parameterNames, parameterDescription))
    {
      writer.writeTag(ExtParserModule.NAMESPACE, BASIC_OBJECT_TAG, p, XmlWriter.OPEN);
      final String valueString = (String) parameterDescription.getParameter("value");
      writer.writeText(XmlWriter.normalize(valueString, false));
      writer.writeCloseTag();
    }
    else
    {
      writer.writeTag(ExtParserModule.NAMESPACE, COMPOUND_OBJECT_TAG, p, XmlWriter.OPEN);

      final ObjectWriter objWriter = new ObjectWriter(getReportWriter(), parameterValue,
              parameterDescription, writer);
      objWriter.write();
      writer.writeCloseTag();
    }

  }

  /**
   * Checks, whether the writer would use the default object type for the given parameter
   * type and value.
   *
   * @param parameter the defined parameter base type
   * @param o         the parameter value to test
   * @return true, if the default parameter description would be used, false otherwise.
   */
  private boolean isUseParameterObjectDescription (final Class parameter, final Object o)
  {
    final ClassFactoryCollector cc = getReportWriter().getClassFactoryCollector();
    ObjectDescription odObject = cc.getDescriptionForClass(o.getClass());
    ObjectDescription odParameter = cc.getDescriptionForClass(parameter);

    // search the most suitable super class object description ...
    if (odObject == null)
    {
      odObject = cc.getSuperClassObjectDescription(o.getClass(), odObject);
    }
    if (odParameter == null)
    {
      odParameter = cc.getSuperClassObjectDescription(parameter, odParameter);
    }
    return ObjectUtilities.equal(odParameter, odObject);
  }


  /**
   * Returns <code>true</code> if this is a basic object, and <code>false</code>
   * otherwise.
   *
   * @param parameters the parameter.
   * @param od         the descriptions.
   * @return A boolean.
   */
  protected static boolean isBasicObject (final List parameters,
                                          final ObjectDescription od)
  {
    if (od == null)
    {
      throw new NullPointerException();
    }

    if (parameters.size() == 1)
    {
      final String param = (String) parameters.get(0);
      if ("value".equals(param))
      {
        if (od.getParameterDefinition("value").equals(String.class))
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns a list of parameter names.
   *
   * @param d the description.
   * @return The list.
   */
  protected static ArrayList getParameterNames (final ObjectDescription d)
  {
    final ArrayList list = new ArrayList();

    final Iterator it = d.getParameterNames();
    while (it.hasNext())
    {
      final String name = (String) it.next();
      if (d.getParameter(name) != null)
      {
        list.add(name);
      }
    }
    return list;
  }
}
