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
 * PrinterDriver.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.plaintext.driver;

import java.awt.print.Paper;

import java.io.IOException;

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
 * PrinterDriver.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public interface PrinterDriver
{

  /**
   * Gets the default character width in CPI.
   *
   * @return the default character width in CPI.
   */
  public float getCharactersPerInch ();

  /**
   * Gets the default line height.
   *
   * @return the default line height.
   */
  public float getLinesPerInch ();

  /**
   * Resets the printer and starts a new page. Prints the top border lines (if
   * necessary).
   *
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  void startPage (final Paper paper, final String encoding)
          throws IOException;

  /**
   * Ends the current page. Should print empty lines or an FORM_FEED command.
   *
   * @param overflow
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  void endPage (boolean overflow)
          throws IOException;

  /**
   * Starts a new line.
   *
   * @throws java.io.IOException if an IOError occures.
   */
  void startLine ()
          throws IOException;

  /**
   * Ends a new line.
   *
   * @param overflow
   * @throws java.io.IOException if an IOError occures.
   */
  void endLine (boolean overflow)
          throws IOException;

  /**
   * Prints a single text chunk at the given position on the current line. The chunk
   * should not be printed, if an previous chunk overlays this chunk.
   *
   * @param chunk the chunk that should be written
   * @throws java.io.IOException if an IO error occured.
   */
  public void printChunk (PlaintextDataChunk chunk)
          throws IOException;

  /**
   * Prints an empty chunk. This is called for all undefined chunk-cells. The last defined
   * font is used to print that empty text.
   *
   * @throws java.io.IOException if an IOError occured.
   */
  public void printEmptyChunk (int count)
          throws IOException;

  /**
   * Flushes the output stream.
   *
   * @throws java.io.IOException if an IOError occured.
   */
  public void flush ()
          throws IOException;

  /**
   * Prints some raw content. This content is not processed in any way, so be very
   * carefull.
   *
   * @param out the content that should be printed.
   */
  public void printRaw (byte[] out)
          throws IOException;

}
