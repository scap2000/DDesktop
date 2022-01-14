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
 * BandElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.elementfactory;

import org.jfree.report.Band;
import org.jfree.report.Element;

/**
 * A element factory that can be used to configure bands. Unlike with the other factories, this factory does not
 * generate new bands on each call to 'createElement'.
 *
 * @author Thomas Morgner
 */
public class BandElementFactory extends TextElementFactory
{
  /**
   * The band that is being configured.
   */
  private Band band;

  /**
   * Default Constructor that constructs generic bands.
   */
  public BandElementFactory()
  {
    this(new Band());
  }

  /**
   * Default Constructor that configures the given band implementation.
   *
   * @param band the band that is being configured. Cannot be null.
   */
  public BandElementFactory(final Band band)
  {
    if (band == null)
    {
      throw new NullPointerException();
    }
    this.band = band;
  }

  /**
   * Returns the created band or the band that has been specified for configuration.
   *
   * @return the band.
   */
  public Element createElement()
  {
    applyElementName(band);
    applyStyle(band.getStyle());
    return band;
  }
}
