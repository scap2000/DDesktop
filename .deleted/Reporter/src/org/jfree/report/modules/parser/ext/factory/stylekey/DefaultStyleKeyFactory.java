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
 * DefaultStyleKeyFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.stylekey;

import org.jfree.report.ShapeElement;
import org.jfree.report.layout.BandLayoutManager;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.TextStyleKeys;

/**
 * A default implementation of the {@link StyleKeyFactory} interface. This implementation
 * contains all stylekeys from the ElementStyleSheet, the BandStyleSheet and the
 * ShapeElement stylesheet.
 * <p/>
 * If available, the excel stylesheets will also be loaded.
 *
 * @author Thomas Morgner
 */
public class DefaultStyleKeyFactory extends AbstractStyleKeyFactory
{
  /**
   * Creates a new factory.
   */
  public DefaultStyleKeyFactory ()
  {
    loadFromClass(ElementStyleKeys.class);
    loadFromClass(TextStyleKeys.class);
    loadFromClass(BandStyleKeys.class);
    loadFromClass(ShapeElement.class);
    // These two classes also define stylekeys ..
    loadFromClass(BandLayoutManager.class);
    loadFromClass(StaticLayoutManager.class);
  }


}
