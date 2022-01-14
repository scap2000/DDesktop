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
 * AbstractElementReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.Color;

import java.util.ArrayList;

import org.jfree.report.Element;
import org.jfree.report.ReportDefinition;
import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.function.Expression;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportElementReadHandler;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.common.StyleExpressionHandler;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.util.Log;
import org.jfree.xmlns.common.ParserUtil;
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
 * AbstractElementReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public abstract class AbstractElementReadHandler
    extends AbstractPropertyXmlReadHandler implements ReportElementReadHandler
{

  /**
   * Literal text for an XML attribute.
   */
  public static final String FONT_NAME_ATT = "fontname";

  /**
   * Literal text for an XML attribute.
   */
  public static final String FONT_STYLE_ATT = "fontstyle";

  /**
   * Literal text for an XML attribute.
   */
  public static final String FONT_SIZE_ATT = "fontsize";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_BOLD = "fsbold";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_ITALIC = "fsitalic";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_UNDERLINE = "fsunderline";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_STRIKETHR = "fsstrikethr";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_EMBEDDED = "font-embedded";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_ENCODING = "font-encoding";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String LINEHEIGHT = "line-height";

  /**
   * Literal text for an XML attribute.
   */
  public static final String NAME_ATT = "name";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ALIGNMENT_ATT = "alignment";

  /**
   * Literal text for an XML attribute.
   */
  public static final String VALIGNMENT_ATT = "vertical-alignment";

  /**
   * Literal text for an XML attribute.
   */
  public static final String COLOR_ATT = "color";

  /**
   * Literal text for an XML attribute.
   */
  public static final String FIELDNAME_ATT = "fieldname";

  /**
   * Literal text for an XML attribute.
   */
  public static final String FUNCTIONNAME_ATT = "function";

  /**
   * Literal text for an XML attribute.
   */
  public static final String NULLSTRING_ATT = "nullstring";
  private static final String DYNAMIC_ATT = "dynamic";
  private static final String VISIBLE_ATT = "visible";
  private static final String HREF_ATT = "href";
  private static final String HREF_WINDOW_ATT = "href-window";
  public static final String STYLE_CLASS_ATT = "styleClass";

  private Element element;
  private String styleClass;
  private ArrayList styleExpressionHandlers;

  protected AbstractElementReadHandler()
  {
    styleExpressionHandlers = new ArrayList();
  }

  protected abstract ElementFactory getElementFactory();

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing(final PropertyAttributes atts)
      throws SAXException
  {
    final ElementFactory factory = getElementFactory();
    factory.setName(atts.getValue(getUri(), NAME_ATT));
    styleClass = atts.getValue(getUri(), STYLE_CLASS_ATT);

    final Color color = ReportParserUtil.parseColor(atts.getValue(getUri(), COLOR_ATT), null);
    factory.setColor(color);

    final String posX = atts.getValue(getUri(), "x");
    if (posX != null)
    {
      factory.setX(new Float(ReportParserUtil.parseRelativeFloat
          (posX, "Attribute 'x' not valid", getLocator())));
    }

    final String posY = atts.getValue(getUri(), "y");
    if (posY != null)
    {
      factory.setY(new Float(ReportParserUtil.parseRelativeFloat
          (posY, "Attribute 'y' not valid", getLocator())));
    }

    final String width = atts.getValue(getUri(), "width");
    if (width != null)
    {
      factory.setMinimumWidth(new Float(ReportParserUtil.parseRelativeFloat
          (width, "Attribute 'width' not valid", getLocator())));
    }

    final String height = atts.getValue(getUri(), "height");
    if (height != null)
    {
      factory.setMinimumHeight(new Float(ReportParserUtil.parseRelativeFloat
          (height, "Attribute 'height' not valid", getLocator())));
    }

    final String dynamicValue = atts.getValue(getUri(), DYNAMIC_ATT);
    if (dynamicValue != null)
    {
      final Boolean dynamic = ParserUtil.parseBoolean(dynamicValue, getLocator());
      factory.setDynamicHeight(dynamic);
    }

    final String visibleValue = atts.getValue(getUri(), VISIBLE_ATT);
    if (visibleValue != null)
    {
      final boolean value = ParserUtil.parseBoolean(visibleValue, true);
      factory.setVisible((value) ? Boolean.TRUE : Boolean.FALSE);
    }

    final String href = atts.getValue(getUri(), HREF_ATT);
    if (href != null)
    {
      factory.setHRefTarget(href);
    }

    final String hrefWindow = atts.getValue(getUri(), HREF_WINDOW_ATT);
    if (hrefWindow != null)
    {
      factory.setHRefWindow(hrefWindow);
    }
    final String hrefTitle = atts.getValue(getUri(), "href-title");
    if (hrefTitle != null)
    {
      factory.setHRefTitle(hrefTitle);
    }

    final String avoidBreaks = atts.getValue(getUri(), "avoid-pagebreaks");
    if (avoidBreaks != null)
    {
      final boolean value = ParserUtil.parseBoolean(avoidBreaks, true);
      factory.setAvoidPagebreaks((value) ? Boolean.TRUE : Boolean.FALSE);
    }

    final String overflowX = atts.getValue(getUri(), "overflow-x");
    if (overflowX != null)
    {
      final boolean value = ParserUtil.parseBoolean(overflowX, false);
      factory.setOverflowX((value) ? Boolean.TRUE : Boolean.FALSE);
    }

    final String overflowY = atts.getValue(getUri(), "overflow-y");
    if (overflowY != null)
    {
      final boolean value = ParserUtil.parseBoolean(overflowY, false);
      factory.setOverflowY((value) ? Boolean.TRUE : Boolean.FALSE);
    }


    final String widows = atts.getValue(getUri(), "widows");
    if (widows != null)
    {
      factory.setWidows(ReportParserUtil.parseInteger(widows, getLocator()));
    }

    final String orphans = atts.getValue(getUri(), "orpans");
    if (orphans != null)
    {
      factory.setOrphans(ReportParserUtil.parseInteger(orphans, getLocator()));
    }

    final String padding = atts.getValue(getUri(), "padding");
    if (padding != null)
    {
      factory.setPadding(ReportParserUtil.parseFloat(padding, getLocator()));
    }

    final String paddingTop = atts.getValue(getUri(), "padding-top");
    if (paddingTop != null)
    {
      factory.setPaddingTop(ReportParserUtil.parseFloat(paddingTop, getLocator()));
    }

    final String paddingLeft = atts.getValue(getUri(), "padding-left");
    if (paddingLeft != null)
    {
      factory.setPaddingLeft(ReportParserUtil.parseFloat(paddingLeft, getLocator()));
    }

    final String paddingBottom = atts.getValue(getUri(), "padding-bottom");
    if (paddingBottom != null)
    {
      factory.setPaddingBottom(ReportParserUtil.parseFloat(paddingBottom, getLocator()));
    }

    final String paddingRight = atts.getValue(getUri(), "padding-right");
    if (paddingRight != null)
    {
      factory.setPaddingRight(ReportParserUtil.parseFloat(paddingRight, getLocator()));
    }

    final String attr = atts.getValue(getUri(), "background-color");
    factory.setBackgroundColor(ReportParserUtil.parseColor(attr, null));

    final String borderColor = atts.getValue(getUri(), "border-color");
    factory.setBorderColor(ReportParserUtil.parseColor(borderColor, null));
    final String borderColorTop = atts.getValue(getUri(), "border-top-color");
    factory.setBorderTopColor(ReportParserUtil.parseColor(borderColorTop, null));
    final String borderColorLeft = atts.getValue(getUri(), "border-left-color");
    factory.setBorderLeftColor(ReportParserUtil.parseColor(borderColorLeft, null));
    final String borderColorBottom = atts.getValue(getUri(), "border-bottom-color");
    factory.setBorderBottomColor(ReportParserUtil.parseColor(borderColorBottom, null));
    final String borderColorRight = atts.getValue(getUri(), "border-right-color");
    factory.setBorderRightColor(ReportParserUtil.parseColor(borderColorRight, null));
    final String borderColorBreak = atts.getValue(getUri(), "border-break-color");
    factory.setBorderBreakColor(ReportParserUtil.parseColor(borderColorBreak, null));

    final String borderWidth = atts.getValue(getUri(), "border-width");
    if (borderWidth != null)
    {
      factory.setBorderWidth(ReportParserUtil.parseFloat(borderWidth, getLocator()));
    }
    final String borderWidthTop = atts.getValue(getUri(), "border-top-width");
    if (borderWidthTop != null)
    {
      factory.setBorderTopWidth(ReportParserUtil.parseFloat(borderWidthTop, getLocator()));
    }

    final String borderWidthLeft = atts.getValue(getUri(), "border-left-width");
    if (borderWidthLeft != null)
    {
      factory.setBorderLeftWidth(ReportParserUtil.parseFloat(borderWidthLeft, getLocator()));
    }

    final String borderWidthBottom = atts.getValue(getUri(), "border-bottom-width");
    if (borderWidthBottom != null)
    {
      factory.setBorderBottomWidth(ReportParserUtil.parseFloat(borderWidthBottom, getLocator()));
    }

    final String borderWidthRight = atts.getValue(getUri(), "border-right-width");
    if (borderWidthRight != null)
    {
      factory.setBorderRightWidth(ReportParserUtil.parseFloat(borderWidthRight, getLocator()));
    }
    final String borderWidthBreak = atts.getValue(getUri(), "border-break-width");
    if (borderWidthBreak != null)
    {
      factory.setBorderBreakWidth(ReportParserUtil.parseFloat(borderWidthBreak, getLocator()));
    }

    final String borderStyle = atts.getValue(getUri(), "border-style");
    factory.setBorderStyle(parseBorderStyle(borderStyle));
    final String borderStyleTop = atts.getValue(getUri(), "border-top-style");
    factory.setBorderTopStyle(parseBorderStyle(borderStyleTop));
    final String borderStyleLeft = atts.getValue(getUri(), "border-left-style");
    factory.setBorderLeftStyle(parseBorderStyle(borderStyleLeft));
    final String borderStyleBottom = atts.getValue(getUri(), "border-bottom-style");
    factory.setBorderBottomStyle(parseBorderStyle(borderStyleBottom));
    final String borderStyleRight = atts.getValue(getUri(), "border-right-style");
    factory.setBorderRightStyle(parseBorderStyle(borderStyleRight));
    final String borderStyleBreak = atts.getValue(getUri(), "border-break-style");
    factory.setBorderBreakStyle(parseBorderStyle(borderStyleBreak));


    factory.setHorizontalAlignment(ReportParserUtil.parseHorizontalElementAlignment
        (atts.getValue(getUri(), ALIGNMENT_ATT), getLocator()));
    factory.setVerticalAlignment(ReportParserUtil.parseVerticalElementAlignment
        (atts.getValue(getUri(), VALIGNMENT_ATT), getLocator()));

    final String borderRadiusWidth = atts.getValue(getUri(), "border-radius-width");
    if (borderRadiusWidth != null)
    {
      factory.setBorderRadiusWidth(ReportParserUtil.parseFloat(borderRadiusWidth, getLocator()));
    }
    final String borderRadiusHeight = atts.getValue(getUri(), "border-radius-height");
    if (borderRadiusHeight != null)
    {
      factory.setBorderRadiusHeight(ReportParserUtil.parseFloat(borderRadiusHeight, getLocator()));
    }
    final String borderTopLeftRadiusWidth = atts.getValue(getUri(), "border-top-left-radius-width");
    if (borderTopLeftRadiusWidth != null)
    {
      factory.setBorderTopLeftRadiusWidth(ReportParserUtil.parseFloat(borderTopLeftRadiusWidth, getLocator()));
    }
    final String borderTopLeftRadiusHeight = atts.getValue(getUri(), "border-top-left-radius-height");
    if (borderTopLeftRadiusHeight != null)
    {
      factory.setBorderTopLeftRadiusHeight(ReportParserUtil.parseFloat(borderTopLeftRadiusHeight, getLocator()));
    }
    final String borderTopRightRadiusWidth = atts.getValue(getUri(), "border-top-right-radius-width");
    if (borderTopRightRadiusWidth != null)
    {
      factory.setBorderTopRightRadiusWidth(ReportParserUtil.parseFloat(borderTopRightRadiusWidth, getLocator()));
    }
    final String borderTopRightRadiusHeight = atts.getValue(getUri(), "border-top-right-radius-height");
    if (borderTopRightRadiusHeight != null)
    {
      factory.setBorderTopRightRadiusHeight(ReportParserUtil.parseFloat(borderTopRightRadiusHeight, getLocator()));
    }
    final String borderBottomLeftRadiusWidth = atts.getValue(getUri(), "border-bottom-left-radius-width");
    if (borderBottomLeftRadiusWidth != null)
    {
      factory.setBorderBottomLeftRadiusWidth(ReportParserUtil.parseFloat(borderBottomLeftRadiusWidth, getLocator()));
    }
    final String borderBottomLeftRadiusHeight = atts.getValue(getUri(), "border-bottom-left-radius-height");
    if (borderBottomLeftRadiusHeight != null)
    {
      factory.setBorderBottomLeftRadiusHeight(ReportParserUtil.parseFloat(borderBottomLeftRadiusHeight, getLocator()));
    }
    final String borderBottomRightRadiusWidth = atts.getValue(getUri(), "border-bottom-right-radius-width");
    if (borderBottomRightRadiusWidth != null)
    {
      factory.setBorderBottomRightRadiusWidth(ReportParserUtil.parseFloat(borderBottomRightRadiusWidth, getLocator()));
    }
    final String borderBottomRightRadiusHeight = atts.getValue(getUri(), "border-bottom-right-radius-height");
    if (borderBottomRightRadiusHeight != null)
    {
      factory.setBorderBottomRightRadiusHeight(ReportParserUtil.parseFloat(borderBottomRightRadiusHeight, getLocator()));
    }
  }

  private BorderStyle parseBorderStyle(final String value)
  {
    if (BorderStyle.DASHED.toString().equals(value))
    {
      return BorderStyle.DASHED;
    }
    if (BorderStyle.DOT_DASH.toString().equals(value))
    {
      return BorderStyle.DOT_DASH;
    }
    if (BorderStyle.DOT_DOT_DASH.toString().equals(value))
    {
      return BorderStyle.DOT_DOT_DASH;
    }
    if (BorderStyle.DOTTED.toString().equals(value))
    {
      return BorderStyle.DOTTED;
    }
    if (BorderStyle.DOUBLE.toString().equals(value))
    {
      return BorderStyle.DOUBLE;
    }
    if (BorderStyle.GROOVE.toString().equals(value))
    {
      return BorderStyle.GROOVE;
    }
    if (BorderStyle.HIDDEN.toString().equals(value))
    {
      return BorderStyle.HIDDEN;
    }
    if (BorderStyle.INSET.toString().equals(value))
    {
      return BorderStyle.INSET;
    }
    if (BorderStyle.OUTSET.toString().equals(value))
    {
      return BorderStyle.OUTSET;
    }
    if (BorderStyle.RIDGE.toString().equals(value))
    {
      return BorderStyle.RIDGE;
    }
    if (BorderStyle.SOLID.toString().equals(value))
    {
      return BorderStyle.SOLID;
    }
    if (BorderStyle.NONE.toString().equals(value))
    {
      return BorderStyle.NONE;
    }
    return null;
  }

  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final PropertyAttributes attrs)
      throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    if ("style-expression".equals(tagName))
    {
      final StyleExpressionHandler handler = new StyleExpressionHandler();
      styleExpressionHandlers.add(handler);
      return handler;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    element = getElementFactory().createElement();
    if (styleClass != null)
    {
      final ReportDefinition report = (ReportDefinition) getRootHandler().getHelperObject
          (ReportParserUtil.HELPER_OBJ_REPORT_NAME);
      final ElementStyleSheet existingStyleSheet =
          report.getStyleSheetCollection().getStyleSheet(styleClass);
      if (existingStyleSheet != null)
      {
        element.getStyle().addParent(existingStyleSheet);
      }
      else
      {
        Log.warn("The specified stylesheet '" + styleClass + "' is not defined - creating a new instance.");
        element.getStyle().addParent
            (report.getStyleSheetCollection().createStyleSheet(styleClass));
      }
    }

    for (int i = 0; i < styleExpressionHandlers.size(); i++)
    {
      final StyleExpressionHandler handler =
          (StyleExpressionHandler) styleExpressionHandlers.get(i);
      final StyleKey key = handler.getKey();
      final Expression expression = handler.getExpression();
      element.setStyleExpression(key, expression);
    }
    super.doneParsing();
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   */
  public Object getObject()
  {
    return element;
  }
}
