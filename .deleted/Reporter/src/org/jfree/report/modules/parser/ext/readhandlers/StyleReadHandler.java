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
 * StyleReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.ReportDefinition;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.xmlns.parser.ParseException;
import org.jfree.xmlns.parser.RootXmlReadHandler;
import org.jfree.xmlns.parser.XmlReadHandler;

import org.xml.sax.SAXException;

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
 * StyleReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class StyleReadHandler extends CompoundObjectReadHandler
{

  private StyleSheetCollection styleSheetCollection;
  private ElementStyleSheet styleSheet;
  private boolean createStyle;

  public StyleReadHandler ()
  {
    this(null);
  }

  public StyleReadHandler (final ElementStyleSheet styleSheet)
  {
    super(new ElementStyleSheetObjectDescription());
    this.styleSheet = styleSheet;
    this.createStyle = (styleSheet == null);
  }

  /**
   * Initialises the handler.
   *
   * @param rootHandler the root handler.
   * @param tagName     the tag name.
   */
  public void init (final RootXmlReadHandler rootHandler,
                    final String uri,
                    final String tagName)
  {
    super.init(rootHandler, uri, tagName);
    final ReportDefinition report = (ReportDefinition)
            rootHandler.getHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME);
    styleSheetCollection = report.getStyleSheetCollection();
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
    if (createStyle)
    {
      final String name = attrs.getValue(getUri(), "name");
      if (name == null)
      {
        throw new ParseException
                ("Required attribute 'name' is missing.", getLocator());
      }
      styleSheet = styleSheetCollection.createStyleSheet(name);
    }

    final ElementStyleSheetObjectDescription objectDescription =
            (ElementStyleSheetObjectDescription) getObjectDescription();
    objectDescription.init(getRootHandler(), styleSheet);
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

    if ("extends".equals(tagName))
    {
      return new StyleExtendsReadHandler(styleSheetCollection, styleSheet);
    }
    else if ("basic-key".equals(tagName))
    {
      return handleBasicObject(atts);
    }
    else if ("compound-key".equals(tagName))
    {
      return handleCompoundObject(atts);
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
    return styleSheet;
  }
}
