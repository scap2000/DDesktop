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
 * ParameterMapping.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.io.Serializable;

/**
 * A parameter mapping defines an aliasing rule for incoming and outgoing
 * sub-report parameters.
 *
 * @author Thomas Morgner
 */
public class ParameterMapping implements Serializable
{
  private String name;
  private String alias;
  private static final long serialVersionUID = -8790399939032695626L;

  /**
   * Creates a new parameter mapping for the given parameter. The parameter will
   * be made available using the given 'alias' name. If the alias is null,
   * the name will not be changed during the mapping.
   *
   * @param name  the name.
   * @param alias the alias.
   */
  public ParameterMapping(final String name, final String alias)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    this.name = name;
    if (alias == null)
    {
      this.alias = name;
    }
    else
    {
      this.alias = alias;
    }
  }

  /**
   * Returns the source parameter name.
   *
   * @return the source name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the alias parameter name.
   *
   * @return the alias name.
   */
  public String getAlias()
  {
    return alias;
  }
}
