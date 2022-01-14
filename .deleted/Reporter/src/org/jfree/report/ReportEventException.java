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
 * ReportEventException.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.io.PrintStream;
import java.io.PrintWriter;

import java.util.List;

/**
 * The ReportEventException is thrown, if there were unrecoverable exceptions during the
 * report processing.
 *
 * @author Thomas Morgner
 */
public class ReportEventException extends ReportProcessingException
{
  /**
   * the collected child exceptions.
   */
  private List childExceptions;

  /**
   * Creates an ReportEventException to handle exceptions, that occured during the event
   * dispatching.
   *
   * @param message         the exception message.
   * @param childExceptions the collected exceptions.
   */
  public ReportEventException (final String message, final List childExceptions)
  {
    super(message);
    if (childExceptions == null)
    {
      throw new NullPointerException();
    }
    this.childExceptions = childExceptions;
  }

  /**
   * Gets the collected child exceptions, that occured during the event dispatching.
   *
   * @return the collected child exceptions.
   */
  public List getChildExceptions ()
  {
    return childExceptions;
  }

  /**
   * Returns the errort message string of this throwable object.
   *
   * @return the error message string of this <code>Throwable</code> object if it was
   *         created with an error message string; or <code>null</code> if it was created
   *         with no error message.
   */
  public String getMessage ()
  {
    return super.getMessage() + ": " + childExceptions.size() + " exceptions occured.";
  }

  /**
   * Prints the stack trace to the specified writer.
   *
   * @param writer the writer.
   */
  public void printStackTrace (final PrintWriter writer)
  {
    super.printStackTrace(writer);
    for (int i = 0; i < childExceptions.size(); i++)
    {
      writer.print("Exception #");
      writer.println(i);
      final Exception ex = (Exception) childExceptions.get(i);
      if (ex != null)
      {
        ex.printStackTrace(writer);
      }
      else
      {
        writer.println("<not defined>");
      }
    }
  }

  /**
   * Prints the stack trace to the specified stream.
   *
   * @param stream the output stream.
   */
  public void printStackTrace (final PrintStream stream)
  {
    super.printStackTrace(stream);
    for (int i = 0; i < childExceptions.size(); i++)
    {
      stream.print("Exception #");
      stream.println(i);
      final Exception ex = (Exception) childExceptions.get(i);
      if (ex != null)
      {
        ex.printStackTrace(stream);
      }
      else
      {
        stream.println("<not defined>");
      }
    }
  }
}
