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
 * LocalImageContainer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.awt.Image;

/**
 * The LocalImageContainer makes the image available as 'java.awt.Image' instance.
 * This way, the image can be included in the local content creation process.
 *
 * @author Thomas Morgner
 */
public interface LocalImageContainer extends ImageContainer
{
  /**
   * Returns the image instance for this image container.
   * This method might return <code>null</code>, if the image is
   * not available.
   *
   * @return the image data.
   */
  public Image getImage ();

  /**
   * Returns the name of this image reference. The name returned should be unique.
   *
   * @return the name.
   */
  public String getName ();

  /**
   * Checks, whether this image has a assigned identity. Two identities should be equal,
   * if the image contents are equal.
   *
   * @return true, if that image contains contains identity information, false otherwise.
   */
  public boolean isIdentifiable ();

  /**
   * Returns the identity information.
   *
   * @return the image identifier.
   */
  public Object getIdentity ();
}
