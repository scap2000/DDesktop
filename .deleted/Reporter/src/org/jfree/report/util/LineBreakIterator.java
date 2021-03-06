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
 * LineBreakIterator.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.util;

import java.util.Iterator;

/**
 * Same as BufferedReader.readLine();
 *
 * @author Thomas Morgner
 * @deprecated The same class is contained in JCommon.
 */
public class LineBreakIterator implements Iterator
{
  /**
   * A useful constant.
   */
  public static final int DONE = -1;

  /**
   * Storage for the text.
   */
  private char[] text;

  /**
   * The current position.
   */
  private int position;

  /**
   * Default constructor.
   */
  public LineBreakIterator ()
  {
    setText("");
  }

  /**
   * Creates a new line break iterator.
   *
   * @param text the text to be broken up.
   */
  public LineBreakIterator (final String text)
  {
    setText(text);
  }

  /**
   * Returns the position of the next break.
   *
   * @return A position.
   */
  public synchronized int nextPosition ()
  {
    if (text == null)
    {
      return DONE;
    }
    if (position == DONE)
    {
      return DONE;
    }

    // recognize \n, \r, \r\n

    final int nChars = text.length;
    int nextChar = position;

    while (true)
    {
      if (nextChar >= nChars)
      {
        /* End of text reached */
        position = DONE;
        return DONE;
      }

      boolean eol = false;
      char c = 0;
      int i;

      // search the next line break, either \n or \r
      for (i = nextChar; i < nChars; i++)
      {
        c = text[i];
        if ((c == '\n') || (c == '\r'))
        {
          eol = true;
          break;
        }
      }

      nextChar = i;
      if (eol)
      {
        nextChar++;
        if (c == '\r')
        {
          if ((nextChar < nChars) && (text[nextChar] == '\n'))
          {
            nextChar++;
          }
        }
        position = nextChar;
        return (position);
      }
    }
  }

  /**
   * Same like next(), but returns the End-Of-Text as if there was a linebreak added
   * (Reader.readLine() compatible)
   *
   * @return The next position.
   */
  public int nextWithEnd ()
  {
    final int pos = position;
    if (pos == DONE)
    {
      return DONE;
    }
    if (pos == text.length)
    {
      position = DONE;
      return DONE;
    }
    final int retval = nextPosition();
    if (retval == DONE)
    {
      return text.length;
    }
    return retval;
  }

  /**
   * Returns the text to be broken up.
   *
   * @return The text.
   */
  public String getText ()
  {
    return new String(text);
  }

  /**
   * Sets the text to be broken up.
   *
   * @param text the text.
   */
  public void setText (final String text)
  {
    position = 0;
    this.text = text.toCharArray();
  }

  /**
   * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns
   * <tt>true</tt> if <tt>next</tt> would return an element rather than throwing an
   * exception.)
   *
   * @return <tt>true</tt> if the iterator has more elements.
   */
  public boolean hasNext ()
  {
    return (position != DONE);
  }

  /**
   * Returns the next element in the iteration.
   *
   * @return the next element in the iteration.
   */
  public Object next ()
  {
    if (position == DONE)
    {
      // allready at the end ...
      return null;
    }

    final int lastFound = position;
    int pos = nextWithEnd();
    if (pos == DONE)
    {
      // the end of the text has been reached ...
      return new String(text, lastFound, text.length - lastFound);
    }

    // step one char back
    if (pos > 0)
    {
      for (; pos > lastFound && (text[pos - 1] == '\n' || text[pos - 1] == '\r'); pos--)
      {
        // search the end of the current linebreak sequence ..
      }
    }
    return new String(text, lastFound, pos - lastFound);
  }

  /**
   * Removes from the underlying collection the last element returned by the iterator
   * (optional operation).  This method can be called only once per call to <tt>next</tt>.
   *  The behavior of an iterator is unspecified if the underlying collection is modified
   * while the iteration is in progress in any way other than by calling this method.
   *
   * @throws UnsupportedOperationException if the <tt>remove</tt> operation is not
   *                                       supported by this Iterator.
   * @throws IllegalStateException         if the <tt>next</tt> method has not yet been
   *                                       called, or the <tt>remove</tt> method has
   *                                       already been called after the last call to the
   *                                       <tt>next</tt> method.
   */
  public void remove ()
  {
    throw new UnsupportedOperationException();
  }
}
