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
 * FunctionsWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;

import java.util.Iterator;

import org.jfree.report.AbstractReportDefinition;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionCollection;
import org.jfree.report.function.FormulaExpression;
import org.jfree.report.function.FormulaFunction;
import org.jfree.report.modules.parser.ext.ExtParserModule;
import org.jfree.report.modules.parser.ext.factory.base.ClassFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.base.ObjectDescription;
import org.jfree.report.style.StyleKey;
import org.jfree.report.util.ReportProperties;
import org.jfree.report.util.beans.BeanException;
import org.jfree.report.util.beans.BeanUtility;
import org.jfree.report.util.beans.ConverterRegistry;
import org.jfree.util.Log;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * An XML definition writer that outputs the functions.
 *
 * @author Thomas Morgner.
 */
public class FunctionsWriter extends AbstractXMLDefinitionWriter
{
  /**
   * The name of the function tag.
   */
  public static final String FUNCTION_TAG = "function";

  /**
   * The name of the expression tag.
   */
  public static final String EXPRESSION_TAG = "expression";
  public static final String STYLE_EXPRESSION_TAG = "style-expression";

  /**
   * The name of the 'property-ref' tag.
   */
  public static final String PROPERTY_REF_TAG = "property-ref";

  /**
   * The object factory.
   */
  private final ClassFactoryCollector cfc;

  /**
   * Creates a new writer.
   *
   * @param reportWriter the report writer.
   * @param indentLevel  the current indention level.
   */
  public FunctionsWriter(final ReportWriterContext reportWriter,
                         final XmlWriter indentLevel)
  {
    super(reportWriter, indentLevel);
    cfc = getReportWriter().getClassFactoryCollector();
  }

  /**
   * Writes the functions to XML.
   *
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if the report function definition could not
   *                               be written.
   */
  public void write()
      throws IOException, ReportWriterException
  {
    if (shouldWriteFunctions())
    {
      final XmlWriter writer = getXmlWriter();
      writer.writeTag(ExtParserModule.NAMESPACE, FUNCTIONS_TAG, XmlWriter.OPEN);

      writePropertyRefs();
      writeExpressions(getReport().getExpressions());

      writer.writeCloseTag();
    }
  }

  /**
   * Tests, whether to start a functions section.
   *
   * @return true, if there are functions, marked properties or expressions
   *         defined, false otherwise.
   */
  private boolean shouldWriteFunctions()
  {
    final AbstractReportDefinition report = getReport();
    if (report.getProperties().containsMarkedProperties())
    {
      return true;
    }
    return report.getExpressions().size() != 0;
  }

  /**
   * Writes a collection of functions/expressions to XML.
   *
   * @param exp    the collection.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeExpressions(final ExpressionCollection exp)
      throws IOException
  {
    for (int i = 0; i < exp.size(); i++)
    {
      final Expression expression = exp.getExpression(i);
      writeExpression(expression);
    }
  }

  private void writeExpression(final Expression expression)
      throws IOException
  {
    final XmlWriter writer = getXmlWriter();
    if (expression instanceof FormulaExpression)
    {
      final FormulaExpression fe = (FormulaExpression) expression;
      final AttributeList properties = new AttributeList();
      properties.setAttribute(ExtParserModule.NAMESPACE, "name", expression.getName());
      properties.setAttribute(ExtParserModule.NAMESPACE, "formula", fe.getFormula());
      if (expression.getDependencyLevel() > 0)
      {
        properties.setAttribute(ExtParserModule.NAMESPACE, "deplevel", String.valueOf(
            expression.getDependencyLevel()));
      }
      writer.writeTag(ExtParserModule.NAMESPACE, EXPRESSION_TAG, properties, XmlWriter.CLOSE);
      return;
    }

    if (expression instanceof FormulaFunction)
    {
      final FormulaFunction fe = (FormulaFunction) expression;
      final AttributeList properties = new AttributeList();
      properties.setAttribute(ExtParserModule.NAMESPACE, "name", expression.getName());
      properties.setAttribute(ExtParserModule.NAMESPACE, "formula", fe.getFormula());
      properties.setAttribute(ExtParserModule.NAMESPACE, "initial", fe.getInitial());
      if (expression.getDependencyLevel() > 0)
      {
        properties.setAttribute(ExtParserModule.NAMESPACE, "deplevel", String.valueOf(
            expression.getDependencyLevel()));
      }
      writer.writeTag(ExtParserModule.NAMESPACE, EXPRESSION_TAG, properties, XmlWriter.CLOSE);
      return;
    }

    try
    {
      final BeanUtility bu = new BeanUtility(expression);
      final String[] propertyNames = bu.getProperties();
      if (propertyNames.length == 0)
      {
        final AttributeList properties = new AttributeList();
        properties.setAttribute(ExtParserModule.NAMESPACE, "name", expression.getName());
        properties.setAttribute(ExtParserModule.NAMESPACE, "class", expression.getClass().getName());
        if (expression.getDependencyLevel() > 0)
        {
          properties.setAttribute(ExtParserModule.NAMESPACE, "deplevel", String.valueOf(
              expression.getDependencyLevel()));
        }
        writer.writeTag(ExtParserModule.NAMESPACE, EXPRESSION_TAG, properties, XmlWriter.CLOSE);
      }
      else
      {
        final AttributeList properties = new AttributeList();
        properties.setAttribute(ExtParserModule.NAMESPACE, "name", expression.getName());
        properties.setAttribute(ExtParserModule.NAMESPACE, "class", expression.getClass().getName());
        if (expression.getDependencyLevel() > 0)
        {
          properties.setAttribute(ExtParserModule.NAMESPACE, "deplevel", String.valueOf(
              expression.getDependencyLevel()));
        }
        writer.writeTag(ExtParserModule.NAMESPACE, EXPRESSION_TAG, properties, XmlWriter.OPEN);

        writeExpressionParameters(propertyNames, bu);

        writer.writeCloseTag();
      }

    }
    catch (Exception e)
    {
      Log.error ("Failed to write the expression", e);
      throw new IOException("Unable to extract or write properties.");
    }
  }


  public void writeStyleExpression(final Expression expression,
                                   final StyleKey styleKey)
      throws IOException
  {
    if (expression instanceof FormulaExpression)
    {
      final FormulaExpression fe = (FormulaExpression) expression;
      final AttributeList properties = new AttributeList();
      properties.setAttribute(ExtParserModule.NAMESPACE, "style-key", styleKey.getName());
      properties.setAttribute(ExtParserModule.NAMESPACE, "formula", fe.getFormula());
      if (expression.getDependencyLevel() > 0)
      {
        properties.setAttribute(ExtParserModule.NAMESPACE, "deplevel", String.valueOf(
            expression.getDependencyLevel()));
      }
      getXmlWriter().writeTag(ExtParserModule.NAMESPACE, STYLE_EXPRESSION_TAG, properties, XmlWriter.CLOSE);
      return;
    }

    if (expression instanceof FormulaFunction)
    {
      final FormulaFunction fe = (FormulaFunction) expression;
      final AttributeList properties = new AttributeList();
      properties.setAttribute(ExtParserModule.NAMESPACE, "style-key", styleKey.getName());
      properties.setAttribute(ExtParserModule.NAMESPACE, "formula", fe.getFormula());
      properties.setAttribute(ExtParserModule.NAMESPACE, "initial", fe.getInitial());
      if (expression.getDependencyLevel() > 0)
      {
        properties.setAttribute(ExtParserModule.NAMESPACE, "deplevel", String.valueOf(
            expression.getDependencyLevel()));
      }
      getXmlWriter().writeTag(ExtParserModule.NAMESPACE, STYLE_EXPRESSION_TAG, properties, XmlWriter.CLOSE);
      return;
    }

    try
    {
      final BeanUtility bu = new BeanUtility(expression);
      final String[] propertyNames = bu.getProperties();
      if (propertyNames.length == 0)
      {
        final AttributeList properties = new AttributeList();
        properties.setAttribute(ExtParserModule.NAMESPACE, "style-key", styleKey.getName());
        properties.setAttribute(ExtParserModule.NAMESPACE, "class", expression.getClass().getName());
        if (expression.getDependencyLevel() > 0)
        {
          properties.setAttribute(ExtParserModule.NAMESPACE, "deplevel", String.valueOf(
              expression.getDependencyLevel()));
        }
        getXmlWriter().writeTag(ExtParserModule.NAMESPACE, STYLE_EXPRESSION_TAG, properties, XmlWriter.CLOSE);
      }
      else
      {
        final AttributeList properties = new AttributeList();
        properties.setAttribute(ExtParserModule.NAMESPACE, "style-key", styleKey.getName());
        properties.setAttribute(ExtParserModule.NAMESPACE, "class", expression.getClass().getName());
        if (expression.getDependencyLevel() > 0)
        {
          properties.setAttribute(ExtParserModule.NAMESPACE, "deplevel", String.valueOf(
              expression.getDependencyLevel()));
        }
        getXmlWriter().writeTag(ExtParserModule.NAMESPACE, STYLE_EXPRESSION_TAG, properties, XmlWriter.OPEN);

        writeExpressionParameters(propertyNames, bu);

        getXmlWriter().writeCloseTag();
      }

    }
    catch (Exception e)
    {
      throw new IOException("Unable to extract or write properties.");
    }
  }

  /**
   * Writes the parameters for an expression or function.
   *
   * @param propertyNames the names of the properties.
   * @param beanUtility   the bean utility containing the expression bean.
   * @throws IOException   if an IO error occurs.
   * @throws BeanException if a bean error occured.
   */
  private void writeExpressionParameters
      (final String[] propertyNames,
       final BeanUtility beanUtility)
      throws IOException, BeanException
  {
    final XmlWriter writer = getXmlWriter();
    writer.writeTag(ExtParserModule.NAMESPACE, PROPERTIES_TAG, XmlWriter.OPEN);

    for (int i = 0; i < propertyNames.length; i++)
    {
      final String key = propertyNames[i];
      // filter some of the standard properties. These are system-properties
      // and are set elsewhere
      if ("name".equals(key))
      {
        continue;
      }
      if ("dependencyLevel".equals(key))
      {
        continue;
      }
      if ("runtime".equals(key))
      {
        continue;
      }
      if ("active".equals(key))
      {
        continue;
      }
      if ("preserve".equals(key))
      {
        continue;
      }

      final Object property = beanUtility.getProperty(key);
      final Class propertyType = beanUtility.getPropertyType(key);
      final String value = beanUtility.getPropertyAsString(key);
      if (value != null && property != null)
      {
        final AttributeList attList = new AttributeList();
        attList.setAttribute(ExtParserModule.NAMESPACE, "name", key);
        if (propertyType.equals(property.getClass()) == false)
        {
          attList.setAttribute(ExtParserModule.NAMESPACE, "class", property.getClass().getName());
        }
        writer.writeTag(ExtParserModule.NAMESPACE, "property", attList, XmlWriter.OPEN);
        writer.writeText(XmlWriter.normalize(value, false));
        writer.writeCloseTag();
      }
    }

    writer.writeCloseTag();
  }

  /**
   * Writes the property references to XML.
   *
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if the report definition could not be
   *                               written.
   */
  private void writePropertyRefs()
      throws IOException, ReportWriterException
  {
    final ReportProperties reportProperties = getReport().getProperties();
    final Iterator keys = reportProperties.keys();
    final XmlWriter writer = getXmlWriter();
    while (keys.hasNext())
    {
      final String name = (String) keys.next();
      final Object value = reportProperties.get(name);

      if (value == null)
      {
        writer.writeTag(ExtParserModule.NAMESPACE, PROPERTY_REF_TAG, "name", name, XmlWriter.CLOSE);
      }
      else
      {
        String svalue = null;
        try
        {
          svalue = ConverterRegistry.toAttributeValue(value);
        }
        catch (BeanException e)
        {
          Log.warn ("Unable to convert the given property value into a simple bean for property '" + name + "'");
        }
        if (svalue == null)
        {
          writer.writeTag(ExtParserModule.NAMESPACE, PROPERTY_REF_TAG, "name", name, XmlWriter.CLOSE);
        }
        else
        {
          final AttributeList properties = new AttributeList();
          properties.setAttribute(ExtParserModule.NAMESPACE, "name", name);
          properties.setAttribute(ExtParserModule.NAMESPACE, "class", value.getClass().getName());

          writer.writeTag(ExtParserModule.NAMESPACE, PROPERTY_REF_TAG, properties, XmlWriter.OPEN);
          writer.writeText(XmlWriter.normalize(svalue, false));
          writer.writeCloseTag();
        }
      }
    }
  }

  /**
   * Writes an object description to XML.
   *
   * @param od     the object description.
   * @param o      the object.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if the report definition could not be
   *                               written.
   */
  private void writeObjectDescription(final ObjectDescription od,
                                      final Object o)
      throws IOException, ReportWriterException
  {
    try
    {
      od.setParameterFromObject(o);
    }
    catch (Exception e)
    {
      throw new ReportWriterException(
          "Unable to write the report property reference", e);
    }

    if (isBasicObject(od))
    {
      final String value = (String) od.getParameter("value");
      if (value != null)
      {
        final XmlWriter writer = getXmlWriter();
        writer.writeText(XmlWriter.normalize(value, false));
      }
    }
    else
    {
      final ObjectWriter objectWriter =
          new ObjectWriter(getReportWriter(), o, od, getXmlWriter());
      objectWriter.write();
    }

  }

  /**
   * Returns <code>true</code> if the object description is for a basic object,
   * and <code>false</code> otherwise.
   *
   * @param od the object description.
   * @return <code>true</code> or <code>false</code>.
   */
  private boolean isBasicObject(final ObjectDescription od)
  {
    final Iterator odNames = od.getParameterNames();
    if (odNames.hasNext() == false)
    {
      return false;
    }

    final String param = (String) odNames.next();
    if (odNames.hasNext() == true)
    {
      return false;
    }

    if ("value".equals(param))
    {
      if (od.getParameterDefinition("value").equals(String.class))
      {
        return true;
      }
    }
    return false;
  }
}
