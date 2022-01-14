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
 * DefaultFontMapper.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.plaintext.driver;

import java.util.HashMap;

import org.jfree.fonts.FontMappingUtility;

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
 * DefaultFontMapper.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class DefaultFontMapper implements FontMapper
{
  private HashMap fontMapping;
  private byte defaultFont;

  public DefaultFontMapper ()
  {
    fontMapping = new HashMap();
    defaultFont = PrinterDriverCommands.SELECT_FONT_ROMAN;
  }

  public void addFontMapping (final String fontName, final byte printerCode)
  {
    if (fontName == null)
    {
      throw new NullPointerException();
    }
    fontMapping.put(fontName, new Byte(printerCode));
  }

  public void removeFontMapping (final String fontName)
  {
    fontMapping.remove(fontName);
  }

  public byte getPrinterFont (final String fontName)
  {
    final Byte b = (Byte) fontMapping.get(fontName);
    if (b != null)
    {
      return b.byteValue();
    }
    return handleDefault(fontName);
  }

  protected byte handleDefault (final String fd)
  {
    if (FontMappingUtility.isCourier(fd))
    {
      return PrinterDriverCommands.SELECT_FONT_COURIER;
    }
    if (FontMappingUtility.isSerif(fd))
    {
      return PrinterDriverCommands.SELECT_FONT_ROMAN;
    }
    if (FontMappingUtility.isSansSerif(fd))
    {
      return PrinterDriverCommands.SELECT_FONT_OCR_A;
    }
    return defaultFont;
  }

  public byte getDefaultFont ()
  {
    return defaultFont;
  }

  public void setDefaultFont (final byte defaultFont)
  {
    this.defaultFont = defaultFont;
  }
}
