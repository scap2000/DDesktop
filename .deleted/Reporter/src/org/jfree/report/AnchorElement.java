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
 * AnchorElement.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report;

/**
 * The anchor element creates targets for hyperlinks.
 *
 * @author Thomas Morgner
 * @deprecated to create anchors, use the style-key 'Anchor-Target' instead.
 */
public class AnchorElement extends Element
{
  /**
   * The content type.
   */
  public static final String CONTENT_TYPE = "X-Anchor";

  /**
   * Creates a new anchor element.
   */
  public AnchorElement ()
  {
  }

  /**
   * Defines the content-type for this element. The content-type is used as a hint how to
   * process the contents of this element. An element implementation should restrict
   * itself to the content-type set here, or the reportprocessing may fail or the element
   * may not be printed.
   * <p/>
   * An element is not allowed to change its content-type after ther report processing has
   * started.
   * <p/>
   * If an content-type is unknown to the output-target, the processor should ignore the
   * content or clearly document its internal reprocessing. Ignoring is preferred.
   *
   * @return the content-type as string.
   * @deprecated we no longer make use of the content type.
   */
  public String getContentType ()
  {
    return AnchorElement.CONTENT_TYPE;
  }
}
