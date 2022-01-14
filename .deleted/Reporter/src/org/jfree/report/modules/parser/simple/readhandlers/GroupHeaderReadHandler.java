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
 * GroupHeaderReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.Band;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.util.Log;
import org.jfree.xmlns.common.ParserUtil;

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
 * GroupHeaderReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class GroupHeaderReadHandler extends RootLevelBandReadHandler
{
  public GroupHeaderReadHandler (final Band band)
  {
    super(band);
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
    handlePagebreakAttr(attr);
    handleRepeat(attr);
    handleSticky(attr);
  }

  private void handleSticky (final PropertyAttributes attr) throws SAXException
  {
    final String repeat = attr.getValue(getUri(), "sticky");
    final Boolean repeatVal = ParserUtil.parseBoolean(repeat, getLocator());
    getBand().getStyle().setStyleProperty(BandStyleKeys.STICKY, repeatVal);
  }

  private void handleRepeat (final PropertyAttributes attr) throws SAXException
  {
    final String repeat = attr.getValue(getUri(), "repeat");
    final Boolean repeatVal = ParserUtil.parseBoolean(repeat, getLocator());
    getBand().getStyle().setStyleProperty(BandStyleKeys.REPEAT_HEADER, repeatVal);
  }

  private void handlePagebreakAttr (final PropertyAttributes attr)
      throws SAXException
  {
    final String ownPageAttr = attr.getValue(getUri(), "pagebreak");
    if (ownPageAttr != null)
    {
      Log.warn("The 'pagebreak' attribute of the <group-header> tag is deprecated. " +
              "Use the 'pagebreak-before' attribute instead.");
      final Boolean ownPage = ParserUtil.parseBoolean(ownPageAttr, getLocator());
      getBand().getStyle().setStyleProperty(BandStyleKeys.PAGEBREAK_BEFORE, ownPage);
    }
  }
}
