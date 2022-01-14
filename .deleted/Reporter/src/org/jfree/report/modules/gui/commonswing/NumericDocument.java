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
 * NumericDocument.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.commonswing;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Creation-Date: 29.10.2007, 18:44:24
 *
 * @author Thomas Morgner
 */
public class NumericDocument extends PlainDocument
{
  private StringBuffer buffer;

  public NumericDocument()
  {
    this.buffer = new StringBuffer();
  }

  public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException
  {
    buffer.delete(0, buffer.length());
    final char[] chars = str.toCharArray();
    for (int i = 0; i < chars.length; i++)
    {
      final char aChar = chars[i];
      if (Character.isDigit(aChar))
      {
        buffer.append(aChar);
      }
    }
    if (buffer.length() > 0)
    {
      super.insertString(offs, buffer.toString(), a);
    }
  }


  public void replace(final int offset, final int length, final String text, final AttributeSet attrs)
      throws BadLocationException
  {
    buffer.delete(0, buffer.length());
    final char[] chars = text.toCharArray();
    for (int i = 0; i < chars.length; i++)
    {
      final char aChar = chars[i];
      if (Character.isDigit(aChar))
      {
        buffer.append(aChar);
      }
    }
    if (buffer.length() > 0)
    {
      super.replace(offset, length, buffer.toString(), attrs);
    }
  }
}
