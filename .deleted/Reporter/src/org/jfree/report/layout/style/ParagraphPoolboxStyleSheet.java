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
 * ParagraphPoolboxStyleSheet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.style;

import org.jfree.report.style.AbstractStyleSheet;
import org.jfree.report.style.BandDefaultStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;

/**
 * Creation-Date: 05.08.2007, 13:23:36
 *
 * @author Thomas Morgner
 */
public class ParagraphPoolboxStyleSheet extends AbstractStyleSheet
{
  private StyleSheet parentStyleSheet;
  private StyleSheet defaultStyleSheet;

  public ParagraphPoolboxStyleSheet(final StyleSheet parentStyleSheet)
  {
    if (parentStyleSheet == null)
    {
      throw new NullPointerException();
    }
    this.parentStyleSheet = parentStyleSheet;
    this.defaultStyleSheet = BandDefaultStyleSheet.getDefaultStyle();
  }

  public Object getStyleProperty(final StyleKey key, final Object defaultValue)
  {
    if (key.isInheritable())
    {
      return parentStyleSheet.getStyleProperty(key, defaultValue);
    }
    return defaultStyleSheet.getStyleProperty(key, defaultValue);
  }
}
