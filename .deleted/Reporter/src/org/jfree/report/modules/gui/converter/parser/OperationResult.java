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
 * OperationResult.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.converter.parser;

import java.io.Serializable;

import org.jfree.report.modules.gui.config.ConfigGUIModule;
import org.jfree.report.util.i18n.Messages;

/**
 * The OperationResult class provides the possibility to monitor the result of the parsing
 * progress and to handle warnings and errors.
 *
 * @author Thomas Morgner
 */
public class OperationResult implements Serializable
{
  /**
   * Provides access to externalized Strings
   */
  private static final Messages messages = new Messages(ConfigGUIModule.BUNDLE_NAME);
  /**
   * The message of the result object.
   */
  private String message;
  /**
   * The severity level of the message.
   */
  private SeverityLevel severity;
  /**
     * The destColumn in the xml file where the result was generated.
     */
  private int column;
  /**
   * The line in the xml file where the result was generated.
   */
  private int line;

  /**
   * Creates a new operation result with the given message and severity.
   *
   * @param message  the message of this result object.
   * @param severity the assigned severity.
   * @throws NullPointerException if one of the parameters is null.
   */
  public OperationResult (final String message, final SeverityLevel severity)
  {
    this(message, severity, -1, -1);
  }

  /**
     * Creates a new operation result with the given message, severity and parse position.
     *
     * @param message the message of this result object.
     * @param severity the assigned severity.
     * @param column the destColumn of the parse position
     * @param line the line of the parse position.
     * @throws NullPointerException if one of the parameters is null.
     */
  public OperationResult (final String message, final SeverityLevel severity,
                          final int column, final int line)
  {
    if (message == null)
    {
      throw new NullPointerException(messages.getErrorString("OperationResult.ERROR_0001_NULL_MESSAGE")); //$NON-NLS-1$
    }
    if (severity == null)
    {
      throw new NullPointerException(messages.getErrorString("OperationResult.ERROR_0002_NULL_SEVERITY")); //$NON-NLS-1$
    }
    this.message = message;
    this.severity = severity;
    this.column = column;
    this.line = line;
  }

  /**
   * Returns the message of the operation result. The message is never null.
   *
   * @return the message.
   */
  public String getMessage ()
  {
    return message;
  }

  /**
   * Returns the severity of the message. The severity is never null.
   *
   * @return the severity.
   */
  public SeverityLevel getSeverity ()
  {
    return severity;
  }

  /**
     * Returns the destColumn of the parse position where this message occured.
     *
     * @return the destColumn of the parse position.
     */
  public int getColumn ()
  {
    return column;
  }

  /**
   * Returns the line of the parse position where this message occured.
   *
   * @return the line of the parse position.
   */
  public int getLine ()
  {
    return line;
  }
}
