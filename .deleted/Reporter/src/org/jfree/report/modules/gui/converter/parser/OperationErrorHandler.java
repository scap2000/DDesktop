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
 * OperationErrorHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.converter.parser;

import java.util.ArrayList;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Creation-Date: Dec 17, 2006, 11:21:14 AM
 *
 * @author Thomas Morgner
 */
public class OperationErrorHandler implements ErrorHandler
{
  private ArrayList errors;

  public OperationErrorHandler()
  {
    errors = new ArrayList();
  }

  /**
   * Receive notification of a warning.
   * <p/>
   * <p>SAX parsers will use this method to report conditions that are not
   * errors or fatal errors as defined by the XML recommendation.  The default
   * behaviour is to take no action.</p>
   * <p/>
   * <p>The SAX parser must continue to provide normal parsing events after
   * invoking this method: it should still be possible for the application to
   * process the document through to the end.</p>
   * <p/>
   * <p>Filters may use this method to report other, non-XML warnings as
   * well.</p>
   *
   * @param exception The warning information encapsulated in a SAX parse
   *                  exception.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly wrapping
   *                                  another exception.
   * @see org.xml.sax.SAXParseException
   */
  public void warning(final SAXParseException exception) throws SAXException
  {
    final int column = exception.getColumnNumber();
    final int line = exception.getLineNumber();
    final OperationResult result = new OperationResult
            (exception.getMessage(), SeverityLevel.WARNING, line, column);
    errors.add(result);
  }

  /**
   * Receive notification of a recoverable error.
   * <p/>
   * <p>This corresponds to the definition of "error" in section 1.2 of the W3C
   * XML 1.0 Recommendation.  For example, a validating parser would use this
   * callback to report the violation of a validity constraint.  The default
   * behaviour is to take no action.</p>
   * <p/>
   * <p>The SAX parser must continue to provide normal parsing events after
   * invoking this method: it should still be possible for the application to
   * process the document through to the end. If the application cannot do so,
   * then the parser should report a fatal error even if the XML recommendation
   * does not require it to do so.</p>
   * <p/>
   * <p>Filters may use this method to report other, non-XML errors as
   * well.</p>
   *
   * @param exception The error information encapsulated in a SAX parse
   *                  exception.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly wrapping
   *                                  another exception.
   * @see org.xml.sax.SAXParseException
   */
  public void error(final SAXParseException exception) throws SAXException
  {
    final int column = exception.getColumnNumber();
    final int line = exception.getLineNumber();
    final OperationResult result = new OperationResult
            (exception.getMessage(), SeverityLevel.ERROR, line, column);
    errors.add(result);
  }

  /**
   * Receive notification of a non-recoverable error.
   * <p/>
   * <p><strong>There is an apparent contradiction between the documentation for
   * this method and the documentation for {@link org.xml.sax.ContentHandler#endDocument}.
   *  Until this ambiguity is resolved in a future major release, clients should
   * make no assumptions about whether endDocument() will or will not be invoked
   * when the parser has reported a fatalError() or thrown an
   * exception.</strong></p>
   * <p/>
   * <p>This corresponds to the definition of "fatal error" in section 1.2 of
   * the W3C XML 1.0 Recommendation.  For example, a parser would use this
   * callback to report the violation of a well-formedness constraint.</p>
   * <p/>
   * <p>The application must assume that the document is unusable after the
   * parser has invoked this method, and should continue (if at all) only for
   * the sake of collecting additional error messages: in fact, SAX parsers are
   * free to stop reporting any other events once this method has been
   * invoked.</p>
   *
   * @param exception The error information encapsulated in a SAX parse
   *                  exception.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly wrapping
   *                                  another exception.
   * @see org.xml.sax.SAXParseException
   */
  public void fatalError(final SAXParseException exception) throws SAXException
  {
    final int column = exception.getColumnNumber();
    final int line = exception.getLineNumber();
    final OperationResult result = new OperationResult
            (exception.getMessage(), SeverityLevel.FATAL_ERROR, line, column);
    errors.add(result);
  }

  public OperationResult[] getErrors ()
  {
    return (OperationResult[]) errors.toArray(new OperationResult[errors.size()]);
  }
}
