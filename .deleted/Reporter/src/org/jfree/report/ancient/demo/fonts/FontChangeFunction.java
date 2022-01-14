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
 * FontChangeFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.fonts;

import java.awt.Font;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.TextElement;
import org.jfree.report.function.AbstractElementFormatFunction;
import org.jfree.report.style.FontDefinition;

/**
 * This is a function used in report4-demo. The function demonstrates how to alter an
 * elements property during the report generation. The elements font is changed base on
 * the data provided in the reports datasource.
 * <p/>
 * For every new item row in the report, the font for that row is changed to the fontname
 * specified in the second destColumn of the report data source.
 * <p/>
 * Parameters:<br> The function expects the name of a field in the item band in the
 * parameter "element". This functions value will always be null.
 *
 * @author Thomas Morgner
 */
public class FontChangeFunction extends AbstractElementFormatFunction
{
  private String field;
  /**
   * DefaultConstructor.
   */
  public FontChangeFunction ()
  {
  }

  public String getField()
  {
    return field;
  }

  public void setField(final String field)
  {
    this.field = field;
  }

  protected void processRootBand(Band b)
  {
    // Try to get the name of the font to be set.
    // If the name is null, return without an excpetion, just do nothing.
    final String fontname = (String) getDataRow().get(getField());
    if (fontname == null)
    {
      return;
    }

    // Lookup the element by name. If there no element found, the getElement function
    // returns null, so we have to check this case.
    final Element e = b.getElement(getElement());

    // set the font if an element was found.
    if (e != null && (e instanceof TextElement))
    {
      final TextElement tx = (TextElement) e;
      tx.getStyle().setFontDefinitionProperty(new FontDefinition(new Font(fontname, Font.PLAIN, 10)));
    }
  }
}
