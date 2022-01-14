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
 * DefaultClassFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.objects;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.jfree.report.ElementAlignment;
import org.jfree.report.modules.parser.ext.factory.base.JavaBaseClassFactory;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.style.BoxSizing;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.style.FontSmooth;
import org.jfree.report.style.TextWrap;
import org.jfree.report.style.VerticalTextAlign;
import org.jfree.report.style.WhitespaceCollapse;

/**
 * A default implementation of the {@link org.jfree.xml.factory.objects.ClassFactory}
 * interface.
 *
 * @author Thomas Morgner
 */
public class DefaultClassFactory extends JavaBaseClassFactory
{
  /**
   * Creates a new factory.
   */
  public DefaultClassFactory ()
  {
    registerClass(BorderStyle.class, new BorderStyleObjectDescription());
    registerClass(VerticalTextAlign.class, new VerticalAlignmentObjectDescription());
    registerClass(ElementAlignment.class, new AlignmentObjectDescription());
    registerClass(FontDefinition.class, new FontDefinitionObjectDescription());
    registerClass(PathIteratorSegment.class, new PathIteratorSegmentObjectDescription());
    registerClass(Shape.class, new GeneralPathObjectDescription(Shape.class));
    registerClass(GeneralPath.class, new GeneralPathObjectDescription());
    registerClass(FontSmooth.class, new FontSmoothObjectDescription());
    registerClass(TextWrap.class, new TextWrapObjectDescription());
    registerClass(BoxSizing.class, new BoxSizingObjectDescription());
    registerClass(WhitespaceCollapse.class, new WhitespaceCollapseObjectDescription());
  }
}
