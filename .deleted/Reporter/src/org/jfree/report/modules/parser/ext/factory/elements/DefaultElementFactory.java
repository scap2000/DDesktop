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
 * DefaultElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.elements;

import org.jfree.report.AnchorElement;
import org.jfree.report.DrawableElement;
import org.jfree.report.ImageElement;
import org.jfree.report.ShapeElement;
import org.jfree.report.TextElement;

/**
 * A default implementation of the {@link ElementFactory} interface.
 *
 * @author Thomas Morgner
 */
public class DefaultElementFactory extends AbstractElementFactory
{
  /**
   * Creates a new element factory.
   */
  public DefaultElementFactory ()
  {
    registerElement(new AnchorElement());
    registerElement(new TextElement());
    registerElement(new ShapeElement());
    registerElement(new ImageElement());
    registerElement(new DrawableElement());
  }
}
