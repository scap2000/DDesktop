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
 * MemoryStringWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util;

import java.io.IOException;
import java.io.Writer;

/**
 * A string writer that is able to write large amounts of data. The original StringWriter contained in Java doubles
 * its buffersize everytime the buffer overflows. This is nice with small amounts of data, but awfull for huge
 * buffers.
 *
 * @author Thomas Morgner
 */
public class MemoryStringWriter extends Writer
{
  private int bufferIncrement;
  private int cursor;
  private char[] buffer;
  private int maximumBufferIncrement;

  /**
   * Create a new character-stream writer whose critical sections will synchronize on the writer itself.
   */
  public MemoryStringWriter()
  {
    this(4096);
  }

  /**
   * Create a new character-stream writer whose critical sections will synchronize on the writer itself.
   */
  public MemoryStringWriter(final int bufferSize)
  {
    this(bufferSize, bufferSize * 4);
  }


  public MemoryStringWriter(final int bufferSize, final int maximumBufferIncrement)
  {
    this.maximumBufferIncrement = maximumBufferIncrement;
    this.bufferIncrement = bufferSize;
    this.buffer = new char[bufferSize];
  }

  /**
   * Write a portion of an array of characters.
   *
   * @param cbuf Array of characters
   * @param off  Offset from which to start writing characters
   * @param len  Number of characters to write
   * @throws java.io.IOException If an I/O error occurs
   */
  public synchronized void write(final char[] cbuf, final int off, final int len) throws IOException
  {
    if (len < 0)
    {
      throw new IllegalArgumentException();
    }
    if (off < 0)
    {
      throw new IndexOutOfBoundsException();
    }
    if (cbuf == null)
    {
      throw new NullPointerException();
    }
    if ((len + off) > cbuf.length)
    {
      throw new IndexOutOfBoundsException();
    }

    ensureSize (cursor + len);

    System.arraycopy(cbuf, off, this.buffer, cursor, len);
    cursor += len;
  }

  private void ensureSize(final int size)
  {
    if (this.buffer.length >= size)
    {
      return;
    }

    final int computedSize = (int) Math.min ((this.buffer.length + 1) * 1.5, this.buffer.length + maximumBufferIncrement);
    final int newSize = Math.max (size, computedSize);
    final char[] newBuffer = new char[newSize];
    System.arraycopy(this.buffer, 0, newBuffer, 0, cursor);
    this.buffer = newBuffer;
  }

  /**
   * Flush the stream.  If the stream has saved any characters from the various write() methods in a buffer, write them
   * immediately to their intended destination.  Then, if that destination is another character or byte stream, flush
   * it.  Thus one flush() invocation will flush all the buffers in a chain of Writers and OutputStreams.
   * <p/>
   * If the intended destination of this stream is an abstraction provided by the underlying operating system, for
   * example a file, then flushing the stream guarantees only that bytes previously written to the stream are passed to
   * the operating system for writing; it does not guarantee that they are actually written to a physical device such as
   * a disk drive.
   *
   * @throws java.io.IOException If an I/O error occurs
   */
  public void flush() throws IOException
  {

  }

  /**
   * Close the stream, flushing it first.  Once a stream has been closed, further write() or flush() invocations will
   * cause an IOException to be thrown.  Closing a previously-closed stream, however, has no effect.
   *
   * @throws java.io.IOException If an I/O error occurs
   */
  public void close() throws IOException
  {
  }

  public int getCursor()
  {
    return cursor;
  }

  public String toString ()
  {
    return new String (buffer, 0, cursor);
  }
}
