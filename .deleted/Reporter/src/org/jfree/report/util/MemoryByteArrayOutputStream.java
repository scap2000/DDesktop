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
 * MemoryByteArrayOutputStream.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util;

import java.io.IOException;
import java.io.OutputStream;

import org.jfree.util.Log;

/**
 * A string writer that is able to write large amounts of data. The original StringWriter contained in Java doubles
 * its buffersize everytime the buffer overflows. This is nice with small amounts of data, but awfull for huge
 * buffers.
 *
 * @author Thomas Morgner
 */
public class MemoryByteArrayOutputStream extends OutputStream
{
  private int maximumBufferIncrement;
  private int cursor;
  private byte[] buffer;
  private byte[] singleIntArray;

  /**
   * Create a new character-stream writer whose critical sections will synchronize on the writer itself.
   */
  public MemoryByteArrayOutputStream()
  {
    this(4096, 65536);
  }

  /**
   * Create a new character-stream writer whose critical sections will synchronize on the writer itself.
   */
  public MemoryByteArrayOutputStream(final int bufferSize, final int maximumBufferIncrement)
  {
    this.maximumBufferIncrement = maximumBufferIncrement;
    this.buffer = new byte[bufferSize];
    this.singleIntArray = new byte[1];
  }


  /**
   * Write a portion of an array of characters.
   *
   * @param cbuf Array of characters
   * @param off  Offset from which to start writing characters
   * @param len  Number of characters to write
   * @throws java.io.IOException If an I/O error occurs
   */
  public synchronized void write(final byte[] cbuf, final int off, final int len) throws IOException
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

  /**
   * Writes <code>b.length</code> bytes from the specified byte array to this output stream. The general contract for
   * <code>write(b)</code> is that it should have exactly the same effect as the call <code>write(b, 0,
   * b.length)</code>.
   *
   * @param b the data.
   * @throws java.io.IOException if an I/O error occurs.
   * @see java.io.OutputStream#write(byte[], int, int)
   */
  public void write(final byte[] b) throws IOException
  {
    write(b, 0, b.length);
  }

  /**
   * Writes the specified byte to this output stream. The general contract for <code>write</code> is that one byte is
   * written to the output stream. The byte to be written is the eight low-order bits of the argument <code>b</code>.
   * The 24 high-order bits of <code>b</code> are ignored.
   * <p/>
   * Subclasses of <code>OutputStream</code> must provide an implementation for this method.
   *
   * @param b the <code>byte</code>.
   * @throws java.io.IOException if an I/O error occurs. In particular, an <code>IOException</code> may be thrown if the
   *                             output stream has been closed.
   */
  public synchronized void write(final int b) throws IOException
  {
    this.singleIntArray[0] = (byte) (0xFF & b);
    write(singleIntArray, 0, 1);
  }

  private void ensureSize(final int size)
  {
    if (this.buffer.length >= size)
    {
      return;
    }

    final int computedSize = (int) Math.min ((this.buffer.length + 1) * 1.5, this.buffer.length + maximumBufferIncrement);
    final int newSize = Math.max (size, computedSize);
    final byte[] newBuffer = new byte[newSize];
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
    if ((buffer.length - cursor) > 50000)
    {
      Log.debug("WASTED: " + (buffer.length - cursor));
    }
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

  public synchronized byte[] toByteArray()
  {
    final byte[] retval = new byte[cursor];
    System.arraycopy(buffer, 0, retval, 0, cursor);
    return retval;
  }

  public int getLength()
  {
    return cursor;
  }

  public byte[] getRaw()
  {
    if ((buffer.length - cursor) > 50000)
    {
      Log.debug("WASTED: " + (buffer.length - cursor) + " Length: " + buffer.length);
    }
    return buffer;
  }
}
