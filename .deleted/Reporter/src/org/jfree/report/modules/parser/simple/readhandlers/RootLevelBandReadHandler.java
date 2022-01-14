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
 * RootLevelBandReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.util.ArrayList;

import org.jfree.report.AbstractRootLevelBand;
import org.jfree.report.Band;
import org.jfree.report.SubReport;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.common.IncludeSubReportReadHandler;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.xmlns.common.ParserUtil;
import org.jfree.xmlns.parser.ParseException;
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
 * RootLevelBandReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class RootLevelBandReadHandler extends BandReadHandler
{
  /**
   * Literal text for an XML attribute.
   */
  public static final String FIXED_POSITION_ATTRIBUTE = "fixed-position";

  /**
   * Literal text for an XML attribute.
   */
  public static final String PAGEBREAK_BEFORE_ATTR = "pagebreak-before-print";

  /**
   * Literal text for an XML attribute.
   */
  public static final String HEIGHT_ATTRIBUTE = "height";

  /**
   * Literal text for an XML attribute.
   */
  public static final String PAGEBREAK_AFTER_ATTRIBUTE = "pagebreak-after-print";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ALIGNMENT_ATT = "alignment";

  /**
   * Literal text for an XML attribute.
   */
  public static final String VALIGNMENT_ATT = "vertical-alignment";

  private ArrayList subReportHandlers;

  public RootLevelBandReadHandler (final Band band)
  {
    super(band);
    subReportHandlers = new ArrayList();
  }

  /**
   * Starts parsing.
   *
   * @param attr the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attr)
          throws SAXException
  {
    super.startParsing(attr);
    handleHeight(attr);
    handleFixedPosition(attr);

    if (isManualBreakAllowed())
    {
      handleBreakAfter(attr);
      handleBreakBefore(attr);
    }
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final PropertyAttributes atts)
      throws SAXException
  {
    if (isSameNamespace(uri))
    {
      if ("sub-report".equals(tagName))
      {
        final IncludeSubReportReadHandler subReportReadHandler =
            new IncludeSubReportReadHandler();
        subReportHandlers.add(subReportReadHandler);
        return subReportReadHandler;
      }
    }
    return super.getHandlerForChild(uri, tagName, atts);
  }



  protected boolean isManualBreakAllowed ()
  {
    return true;
  }

  private void handleFixedPosition (final PropertyAttributes attr)
          throws SAXException
  {
    final String fixedPos = attr.getValue(getUri(), FIXED_POSITION_ATTRIBUTE);
    if (fixedPos != null)
    {
      final float fixedPosValue = ParserUtil.parseFloat
              (fixedPos, "FixedPosition is invalid!", getLocator());
      getBand().getStyle().setStyleProperty(BandStyleKeys.FIXED_POSITION,
              new Float(fixedPosValue));
    }
  }

  private void handleHeight (final PropertyAttributes attr)
      throws ParseException
  {
    final String height = attr.getValue(getUri(), HEIGHT_ATTRIBUTE);
    if (height != null)
    {
      final float heightValue = ParserUtil.parseFloat
          (height, "Height is invalid.", getLocator());
      getBand().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, new Float(heightValue));
    }
  }

  private void handleBreakBefore (final PropertyAttributes attr)
      throws SAXException
  {
    final String breakBeforeAttr = attr.getValue(getUri(), PAGEBREAK_BEFORE_ATTR);
    final Boolean breakBefore = ParserUtil.parseBoolean(breakBeforeAttr, getLocator());
    getBand().getStyle().setStyleProperty
            (BandStyleKeys.PAGEBREAK_BEFORE, breakBefore);
  }

  private void handleBreakAfter (final PropertyAttributes attr)
      throws SAXException
  {
    final String breakAfterAttr = attr.getValue(getUri(), PAGEBREAK_AFTER_ATTRIBUTE);
    if (breakAfterAttr != null)
    {
      final Boolean breakAfter = ParserUtil.parseBoolean(breakAfterAttr, getLocator());
      getBand().getStyle().setStyleProperty
              (BandStyleKeys.PAGEBREAK_AFTER, breakAfter);
    }
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    super.doneParsing();
    final Band band = getBand();
    if (band instanceof AbstractRootLevelBand)
    {
      final AbstractRootLevelBand arlb = (AbstractRootLevelBand) band;
      for (int i = 0; i < subReportHandlers.size(); i++)
      {
        final IncludeSubReportReadHandler handler =
            (IncludeSubReportReadHandler) subReportHandlers.get(i);
        arlb.addSubReport((SubReport) handler.getObject());
      }
    }
  }
}
