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
 * AbstractTemplateDescription.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.templates;

import org.jfree.report.filter.templates.Template;
import org.jfree.report.modules.parser.ext.factory.base.BeanObjectDescription;

/**
 * An abstract class for implementing the {@link TemplateDescription} interface.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractTemplateDescription
        extends BeanObjectDescription implements TemplateDescription
{
  /**
   * The name.
   */
  private String name;

  /**
   * Creates a new description.
   *
   * @param name     the name.
   * @param template the template class.
   * @param init     initialise?
   */
  protected AbstractTemplateDescription (final String name, final Class template,
                                      final boolean init)
  {
    super(template, init);
    this.name = name;
  }

  /**
   * Returns the name.
   *
   * @return The name.
   */
  public String getName ()
  {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the name (<code>null</code> not allowed).
   */
  public void setName (final String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    this.name = name;
  }

  /**
   * Creates a template.
   *
   * @return The template.
   */
  public Template createTemplate ()
  {
    return (Template) createObject();
  }

  /**
   * Indicated whether an other object is equal to this one.
   *
   * @param o the other object.
   * @return true, if the object is equal, false otherwise.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof AbstractTemplateDescription))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }

    final AbstractTemplateDescription abstractTemplateDescription = (AbstractTemplateDescription) o;

    if (name != null)
    {
      if (!name.equals(abstractTemplateDescription.name))
      {
        return false;
      }
    }
    else
    {
      if (abstractTemplateDescription.name != null)
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Computes an hashcode for this factory.
   *
   * @return the hashcode.
   *
   * @see java.lang.Object#hashCode()
   */
  public int hashCode ()
  {
    int result = super.hashCode();
    result = 29 * result + (name != null ? name.hashCode() : 0);
    return result;
  }
}
