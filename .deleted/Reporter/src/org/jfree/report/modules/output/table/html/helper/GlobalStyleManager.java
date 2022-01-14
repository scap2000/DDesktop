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
 * GlobalStyleManager.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html.helper;

import java.io.IOException;
import java.io.Writer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.jfree.report.modules.output.table.html.HtmlPrinter;
import org.jfree.util.StringUtils;
import org.jfree.xmlns.common.AttributeList;

/**
 * Creation-Date: 06.05.2007, 19:26:00
 *
 * @author Thomas Morgner
 */
public class GlobalStyleManager implements StyleManager
{
  private static class EntryComparator implements Comparator
  {

    public int compare(final Object o1, final Object o2)
    {
      final Map.Entry e1 = (Map.Entry) o1;
      final Map.Entry e2 = (Map.Entry) o2;
      return String.valueOf(e1.getValue()).compareTo(String.valueOf(e2.getValue()));
    }
  }

  private static final EntryComparator comparator = new EntryComparator();
  
  private HashMap styles;
  private int nameCounter;
  private String lineSeparator;

  public GlobalStyleManager()
  {
    this.styles = new HashMap();
    this.lineSeparator = StringUtils.getLineSeparator();
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
    final String value = styleBuilder.toString(false);
    String styleName = (String) styles.get(value);
    if (styleName == null)
    {
      styleName = "style-" + nameCounter;
      styles.put (value, styleName);
      nameCounter += 1;
    }

    final String attribute = attributeList.getAttribute(HtmlPrinter.XHTML_NAMESPACE, "class");
    if (attribute != null)
    {
      attributeList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "class", attribute + ' ' + styleName);
    }
    else
    {
      attributeList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "class", styleName);
    }
    return attributeList;
  }

  /**
   * Returns the global stylesheet, or null, if no global stylesheet was built.
   *
   * @return
   */
  public String getGlobalStyleSheet()
  {

    final StringBuffer b = new StringBuffer(8192);
    final Map.Entry[] keys = (Map.Entry[]) styles.entrySet().toArray(new Map.Entry[styles.size()]);
    Arrays.sort(keys, comparator);
    for (int i = 0; i < keys.length; i++)
    {
      final Map.Entry entry = keys[i];
      final String style = (String) entry.getKey();
      final String name = (String) entry.getValue();

      b.append('.');
      b.append(name);
      b.append(" {");
      b.append(lineSeparator);
      b.append(style);
      b.append(lineSeparator);
      b.append('}');
      b.append(lineSeparator);
      b.append(lineSeparator);
    }
    return b.toString();
  }


  public void write(final Writer writer) throws IOException
  {
    final Map.Entry[] keys = (Map.Entry[]) styles.entrySet().toArray(new Map.Entry[styles.size()]);
    Arrays.sort(keys, comparator);

    for (int i = 0; i < keys.length; i++)
    {
      final Map.Entry entry = keys[i];
      final String style = (String) entry.getKey();
      final String name = (String) entry.getValue();

      writer.write('.');
      writer.write(name);
      writer.write(" {");
      writer.write(lineSeparator);
      writer.write(style);
      writer.write(lineSeparator);
      writer.write('}');
      writer.write(lineSeparator);
      writer.write(lineSeparator);
    }
  }
}
