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
 * CreateHyperLinksFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.style.ElementStyleKeys;

/**
 * Adds hyperlinks to all elements with the name specified in 'element'. The link target is read from a specified field.
 * The destColumn referenced by this field should contain URLs or Strings.
 *
 * @author Thomas Morgner
 * @deprecated add style expressions to the 'href-target' and 'href-window' instead. It is much easier and less
 * confusing.
 */
public class CreateHyperLinksFunction extends AbstractElementFormatFunction
{
  /**
   * The field name from where to read the hyper-link target.
   */
  private String field;
  /**
   * The field from where to read the link target-window specifier.
   */
  private String windowField;
  /**
   * The constant target window. This property is used if no window field was set.
   */
  private String target;

  /**
   * Default Constructor.
   */
  public CreateHyperLinksFunction()
  {
  }

  /**
   * Returns the field name from where to read the hyper-link target.
   *
   * @return the name of the field.
   */
  public String getField()
  {
    return field;
  }

  /**
   * Defines the field name from where to read the hyper-link target.
   *
   * @param field a field name.
   */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Returns the target window. This property is used if no window field was set. This is only meaningful for HTML
   * exports.
   *
   * @return the target window string.
   */
  public String getTarget()
  {
    return target;
  }

  /**
   * Defines the target window. This property is used if no window field was set. This is only meaningful for HTML
   * exports.
   *
   * @param target the target window string.
   */
  public void setTarget(final String target)
  {
    this.target = target;
  }

  /**
   * Returns the datarow-field from where to read the target window. This is only meaningful for HTML
   * exports.
   *
   * @return the fieldname from where to read the target window string.
   */
  public String getWindowField()
  {
    return windowField;
  }

  /**
   * Defines the datarow-field from where to read the target window. This is only meaningful for HTML
   * exports.
   *
   * @param windowField the fieldname from where to read the target window string.
   */
  public void setWindowField(final String windowField)
  {
    this.windowField = windowField;
  }

  /**
   * Updates the root band and adds a hyperlink to all elements that have the specified name.
   * @param b the root band.
   */
  protected void processRootBand(final Band b)
  {
    String hrefLinkTarget = null;
    final Object targetRaw = getDataRow().get(getField());
    if (targetRaw != null)
    {
      hrefLinkTarget = String.valueOf(targetRaw);
    }

    if (hrefLinkTarget == null)
    {
      return;
    }

    final String windowField = getWindowField();
    final String window;
    if (windowField != null)
    {
      final Object windowRaw = getDataRow().get(windowField);
      if (windowRaw != null)
      {
        window = String.valueOf(windowRaw);
      }
      else
      {
        window = null;
      }
    }
    else
    {
      window = getTarget();
    }

    final Element[] elements = FunctionUtilities.findAllElements(b, getElement());
    for (int i = 0; i < elements.length; i++)
    {
      elements[i].setHRefTarget(hrefLinkTarget);
      if (window != null)
      {
        elements[i].getStyle().setStyleProperty(ElementStyleKeys.HREF_WINDOW, window);
      }
    }
  }
}
