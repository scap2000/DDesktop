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
 * NodeLayoutProperties.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model.context;

import java.io.Serializable;

import org.jfree.report.ElementAlignment;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.style.VerticalTextAlign;
import org.jfree.report.util.InstanceID;

/**
 * A static properties collection. That one is static; once computed it does
 * not change anymore. It does not (under no thinkable circumstances) depend
 * on the given content. It may depend on static content of the parent.
 *
 * @author Thomas Morgner
 */
public final class NodeLayoutProperties implements Serializable, Cloneable
{
  // ComputedMetrics:

  // Fully static properties ...
  private VerticalTextAlign verticalTextAlign;
  private int majorAxis;
  private int minorAxis;
  private StyleSheet styleSheet;
  private InstanceID instanceId;

  public NodeLayoutProperties(final StyleSheet styleSheet)
  {
    this.instanceId = new InstanceID();
    this.styleSheet = styleSheet;

    final Object vTextAlign = styleSheet.getStyleProperty(TextStyleKeys.VERTICAL_TEXT_ALIGNMENT);
    if (vTextAlign != null)
    {
      verticalTextAlign = (VerticalTextAlign) vTextAlign;
    }
    else
    {
      verticalTextAlign = VerticalTextAlign.BASELINE;
    }
  }

  public VerticalTextAlign getVerticalTextAlign()
  {
    return verticalTextAlign;
  }

  public StyleSheet getStyleSheet()
  {
    return styleSheet;
  }

  public InstanceID getInstanceId()
  {
    return instanceId;
  }

  public int getMajorAxis()
  {
    return majorAxis;
  }

  public void setMajorAxis(final int majorAxis)
  {
    this.majorAxis = majorAxis;
  }

  public int getMinorAxis()
  {
    return minorAxis;
  }

  public void setMinorAxis(final int minorAxis)
  {
    this.minorAxis = minorAxis;
  }

  public Object clone () throws CloneNotSupportedException
  {
    return super.clone();
  }

  public ElementAlignment getVerticalAlignment()
  {
    return (ElementAlignment) getStyleSheet().getStyleProperty(ElementStyleKeys.VALIGNMENT);
  }

  public String toString()
  {
    return "NodeLayoutProperties{" +
        "verticalAlignment=" + getVerticalAlignment() +
        ", majorAxis=" + majorAxis +
        ", minorAxis=" + minorAxis +
        '}';
  }
}
