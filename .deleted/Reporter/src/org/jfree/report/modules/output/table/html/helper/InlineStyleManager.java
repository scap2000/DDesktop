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
 * InlineStyleManager.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html.helper;

import java.io.IOException;
import java.io.Writer;

import org.jfree.report.modules.output.table.html.HtmlPrinter;
import org.jfree.xmlns.common.AttributeList;

/**
 * Creation-Date: 06.05.2007, 19:26:00
 *
 * @author Thomas Morgner
 */
public class InlineStyleManager implements StyleManager
{
  public InlineStyleManager()
  {
  }

  /**
   * Updates the given attribute-List according to the current style rules.
   *
   * @param styleBuilder
   * @param attributeList
   * @return the modified attribute list.
   */
  public AttributeList updateStyle(final StyleBuilder styleBuilder, final AttributeList attributeList)
  {
    final String style = attributeList.getAttribute(HtmlPrinter.XHTML_NAMESPACE, "style");
    if (style != null)
    {
      final String trimmedStyle = style.trim();
      if (trimmedStyle.length() > 0 && trimmedStyle.charAt(trimmedStyle.length() - 1) == ';')
      {
        attributeList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "style", style + ' ' + styleBuilder.toString());
      }
      else
      {
        attributeList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "style", style + "; " + styleBuilder.toString());
      }
      return attributeList;
    }

    attributeList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "style", styleBuilder.toString());
    return attributeList;
  }

  /**
   * Returns the global stylesheet, or null, if no global stylesheet was built.
   *
   * @return
   */
  public String getGlobalStyleSheet()
  {
    return null;
  }


  public void write(final Writer writer) throws IOException
  {
    throw new IOException("InlineStyleManager cannot generate a global style");
  }
}
