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
 * WordBreakIterator.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.util;

/**
 * Behaves similiar to BreakIterator.getWordInstance() but handles line break delimeters
 * as simple whitespaces.
 * <p/>
 * This class is not synchronized.
 *
 * @author Thomas Morgner.
 * @deprecated This functionality is now provided by LibFonts.
 */
public class WordBreakIterator
{
  /**
   * A useful constant.
   */
  public static final int DONE = -1;

  /**
   * The current position.
   */
  private int position;

  /**
   * Storage for characters.
   */
  private char[] text;

  /**
   * Creates a new iterator.
   *
   * @param text the text to break.
   */
  public WordBreakIterator (final String text)
  {
    setText(text);
  }

  /**
   * Returns the next word boundary.
   *
   * @return The index of the next word boundary.
   */
  public int next ()
  {
    if (position == DONE)
    {
      return DONE;
    }
    if (text == null)
    {
      return DONE;
    }
    if (position == text.length)
    {
      return DONE;
    }

    //lastFound = position;

    if (Character.isWhitespace(text[position]))
    {
      // search the first non whitespace character ..., this is the beginning of the word
      while ((position < text.length) && (Character.isWhitespace(text[position])))
      {
        position++;
      }
      return position;
    }
    else
    {
      // now search the first whitespace character ..., this is the end of the word
      while ((position < text.length) && (Character.isWhitespace(text[position]) == false))
      {
        position++;
      }
      return position;
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
    if (pos == DONE || pos == text.length)
    {
      return DONE;
    }
    final int retval = next();
    if (retval == DONE)
    {
      return text.length;
    }
    return retval;
  }

  /**
   * Returns the position of the previous break.
   *
   * @return The index.
   */
  public int previous ()
  {
    //return lastFound;

    if (position == 0)
    {
      return 0;
    }
    if (text == null)
    {
      return DONE;
    }
    if (position == DONE)
    {
      position = text.length;
      return position;
    }
    //lastFound = position;

    if (Character.isWhitespace(text[position - 1]))
    {
      // search the first non whitespace character ..., this is the beginning of the word
      while ((position > 0) && (Character.isWhitespace(text[position - 1])))
      {
        position--;
      }
      return position;
    }
    else
    {
      // now search the first whitespace character ..., this is the end of the word
      while ((position > 0) && (Character.isWhitespace(text[position - 1]) == false))
      {
        position--;
      }
      return position;
    }

  }

  /**
   * Returns the text to be broken up.
   *
   * @return the text.
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
    //lastFound = 0;
    this.text = text.toCharArray();
  }

  /**
   * Returns the current parsing position of this iterator.
   *
   * @return returns the current parsing position of this iterator.
   */
  public int getPosition ()
  {
    return position;
  }

  /**
   * Defines the current parse position for the word break iterator. The position must be
   * positive and within the range of the current text.
   *
   * @param position the position.
   */
  public void setPosition (final int position)
  {
    if (position < 0)
    {
      throw new IndexOutOfBoundsException("Position < 0");
    }
    if (position > text.length)
    {
      throw new IndexOutOfBoundsException("Position > text.length");
    }
    this.position = position;
  }
}
