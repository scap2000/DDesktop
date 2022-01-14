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
 * ExpressionReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.base.common;

import java.beans.IntrospectionException;

import org.jfree.report.function.Expression;
import org.jfree.report.function.FormulaExpression;
import org.jfree.report.function.FormulaFunction;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.common.ParserUtil;
import org.jfree.xmlns.parser.ParseException;
import org.jfree.xmlns.parser.XmlReadHandler;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
 * ExpressionReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class ExpressionReadHandler extends AbstractPropertyXmlReadHandler
{
  /**
   * The dependency level attribute.
   */
  public static final String DEPENCY_LEVEL_ATT = "deplevel";

  private Expression expression;

  public ExpressionReadHandler ()
  {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException
  {
    final int depLevel = ParserUtil.parseInt(attrs.getValue(getUri(), DEPENCY_LEVEL_ATT), 0);
    final String expressionName = attrs.getValue(getUri(), "name");
    if (expressionName == null)
    {
      throw new SAXException("Required attribute 'name' is missing.");
    }

    final String className = attrs.getValue(getUri(), "class");
    final String formula = attrs.getValue(getUri(), "formula");
    if (className == null)
    {
      if (formula != null)
      {
        final String initial = attrs.getValue(getUri(), "initial");
        if (initial != null)
        {
          final FormulaFunction function = new FormulaFunction();
          function.setInitial(initial);
          function.setFormula(formula);
          this.expression = function;
          this.expression.setName(expressionName);
          this.expression.setDependencyLevel(depLevel);
        }
        else
        {
          final FormulaExpression expression = new FormulaExpression();
          expression.setFormula(formula);
          this.expression = expression;
          this.expression.setName(expressionName);
          this.expression.setDependencyLevel(depLevel);
        }
      }
      else
      {
        throw new ParseException("Required attribute 'class' is missing.",
            getRootHandler().getDocumentLocator());
      }
    }

    if (expression == null)
    {

      try
      {
        final Class fnC = ObjectUtilities.getClassLoader(getClass()).loadClass(className);
        expression = (Expression) fnC.newInstance();
        expression.setName(expressionName);
        expression.setDependencyLevel(depLevel);
      }
      catch (ClassNotFoundException e)
      {
        throw new SAXParseException("Expression '" + className +
                "' is not valid. The specified class was not found.",
                getRootHandler().getDocumentLocator(), e);
      }
      catch (IllegalAccessException e)
      {
        throw new SAXParseException("Expression " + className +
                "' is not valid. The specified class does not define a public default constructor.",
                getRootHandler().getDocumentLocator(), e);
      }
      catch (InstantiationException e)
      {
        throw new SAXParseException("Expression '" + className +
                "' is not valid. The specified class cannot be instantiated.",
                getRootHandler().getDocumentLocator(), e);
      }
      catch (ClassCastException e)
      {
        throw new SAXParseException("Expression '" + className +
                "' is not valid. The specified class is not an expression or function.",
                getRootHandler().getDocumentLocator(), e);
      }
    }

  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild (final String uri,
                                               final String tagName,
                                               final PropertyAttributes atts)
          throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    if ("properties".equals(tagName))
    {
      try
      {
        return new ExpressionPropertiesReadHandler(expression);
      }
      catch (IntrospectionException e)
      {
        throw new SAXException
                ("Unable to create Introspector for the specified expression.");
      }
    }
    return null;
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
  {
    return expression;
  }
}
