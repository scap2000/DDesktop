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
 * BandReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.util.ArrayList;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.ReportDefinition;
import org.jfree.report.elementfactory.BandElementFactory;
import org.jfree.report.elementfactory.TextElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportElementReadHandler;
import org.jfree.report.modules.parser.base.ReportElementReadHandlerFactory;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.common.StyleExpressionHandler;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.util.Log;
import org.jfree.xmlns.parser.XmlReadHandler;

import org.xml.sax.Attributes;
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
 * BandReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class BandReadHandler extends AbstractTextElementReadHandler
{
  /**
   * Literal text for an XML report element.
   */
  public static final String ROUND_RECTANGLE_TAG = "round-rectangle";
  /**
   * Literal text for an XML report element.
   */
  public static final String LABEL_TAG = "label";

  /**
   * Literal text for an XML report element.
   */
  public static final String STRING_FIELD_TAG = "string-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String NUMBER_FIELD_TAG = "number-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String DATE_FIELD_TAG = "date-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String IMAGEREF_TAG = "imageref";

  /**
   * Literal text for an XML report element.
   */
  public static final String IMAGEFIELD_TAG = "image-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String IMAGEURLFIELD_TAG = "imageurl-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String RECTANGLE_TAG = "rectangle";

  /**
   * Literal text for an XML report element.
   */
  public static final String RESOURCELABEL_TAG = "resource-label";

  /**
   * Literal text for an XML report element.
   */
  public static final String RESOURCEFIELD_TAG = "resource-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String RESOURCEMESSAGE_TAG = "resource-message";

  /**
   * Literal text for an XML report element.
   */
  public static final String COMPONENTFIELD_TAG = "component-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String LINE_TAG = "line";

  /**
   * Literal text for an XML report element.
   */
  public static final String DRAWABLE_FIELD_TAG = "drawable-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String SHAPE_FIELD_TAG = "shape-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String BAND_TAG = "band";

  /**
   * Literal text for an XML report element.
   */
  public static final String MESSAGE_FIELD_TAG = "message-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String ANCHOR_FIELD_TAG = "anchor-field";


  /**
   * Literal text for an XML attribute value.
   */
  private static final String LAYOUT_ATT = "layout";


  private BandElementFactory bandFactory;
  private Band band;
  private ArrayList elementHandlers;
  private ArrayList styleExpressionHandlers;

  public BandReadHandler()
  {
    this(new Band());
  }

  protected BandReadHandler(final Band band)
  {
    if (band == null)
    {
      throw new NullPointerException();
    }
    this.band = band;
    this.bandFactory = new BandElementFactory(band);
    this.elementHandlers = new ArrayList();
    styleExpressionHandlers = new ArrayList();
  }

  protected TextElementFactory getTextElementFactory()
  {
    return bandFactory;
  }

  /**
   * Starts parsing.
   *
   * @param attr the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing(final PropertyAttributes attr)
      throws SAXException
  {
    super.startParsing(attr);
    handleStyleClass(attr);
    handleLayout(attr);
  }

  public Band getBand()
  {
    return band;
  }

  private void handleStyleClass(final PropertyAttributes attr)
  {
    final String styleClass = attr.getValue(getUri(), STYLE_CLASS_ATT);
    if (styleClass != null)
    {
      final ReportDefinition report = (ReportDefinition) getRootHandler().getHelperObject
          (ReportParserUtil.HELPER_OBJ_REPORT_NAME);
      final ElementStyleSheet existingStyleSheet =
          report.getStyleSheetCollection().getStyleSheet(styleClass);
      if (existingStyleSheet != null)
      {
        getBand().getStyle().addParent(existingStyleSheet);
      }
      else
      {
        Log.warn("The specified stylesheet '" + styleClass + "' is not defined - creating a new instance.");
        getBand().getStyle().addParent
            (report.getStyleSheetCollection().createStyleSheet(styleClass));
      }
    }
  }

  private void handleLayout(final Attributes attr)
  {
    final String layoutManagerName = attr.getValue(getUri(), LAYOUT_ATT);
    if (layoutManagerName != null)
    {
      if ("org.jfree.report.layout.StaticLayoutManager".equals(layoutManagerName))
      {
        getBand().getStyle().setStyleProperty(BandStyleKeys.LAYOUT, "canvas");
      }
      else if ("org.jfree.report.layout.StackedLayoutManager".equals(layoutManagerName))
      {
        getBand().getStyle().setStyleProperty(BandStyleKeys.LAYOUT, "block");
      }
      else
      {
        getBand().getStyle().setStyleProperty(BandStyleKeys.LAYOUT, layoutManagerName);
      }
    }
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final PropertyAttributes atts)
      throws SAXException
  {
    final ReportElementReadHandlerFactory factory =
        ReportElementReadHandlerFactory.getInstance();
    final ReportElementReadHandler handler =
        (ReportElementReadHandler) factory.getHandler(uri, tagName);
    if (handler != null)
    {
      elementHandlers.add(handler);
      return handler;
    }


    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    if ("style-expression".equals(tagName))
    {
      final StyleExpressionHandler stylehandler = new StyleExpressionHandler();
      styleExpressionHandlers.add(stylehandler);
      return stylehandler;
    }
    return null;
  }


  /**
   * Done parsing.
   *
   * @throws SAXException if there is a parsing error.
   */
  protected void doneParsing()
      throws SAXException
  {
    for (int i = 0; i < elementHandlers.size(); i++)
    {
      final XmlReadHandler readHandler = (XmlReadHandler) elementHandlers.get(i);
      final Element e = (Element) readHandler.getObject();
      band.addElement(e);
    }

    for (int i = 0; i < styleExpressionHandlers.size(); i++)
    {
      final StyleExpressionHandler handler =
          (StyleExpressionHandler) styleExpressionHandlers.get(i);
      band.setStyleExpression(handler.getKey(), handler.getExpression());
    }
    super.doneParsing();
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   */
  public Object getObject()
  {
    return band;
  }
}
