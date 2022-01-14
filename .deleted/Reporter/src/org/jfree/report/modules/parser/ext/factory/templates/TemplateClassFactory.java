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
 * TemplateClassFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.templates;

import org.jfree.report.modules.parser.ext.factory.base.ClassFactoryImpl;

/**
 * A template class factory.
 *
 * @author Thomas Morgner
 */
public class TemplateClassFactory extends ClassFactoryImpl
{
  /**
   * Creates a new factory.
   */
  public TemplateClassFactory ()
  {
    addTemplate(new AnchorFieldTemplateDescription("anchor-field"));
    addTemplate(new DateFieldTemplateDescription("date-field"));
    addTemplate(new ComponentFieldTemplateDescription("component-field"));
    addTemplate(new DrawableFieldTemplateDescription("drawable-field"));
    addTemplate(new DrawableURLElementTemplateDescription("drawable-url-element"));
    addTemplate(new DrawableURLFieldTemplateDescription("drawable-url-field"));
    addTemplate(new EllipseTemplateDescription("ellipse"));
    addTemplate(new HorizontalLineTemplateDescription("horizontal-line"));
    addTemplate(new ImageFieldTemplateDescription("image-field"));
    addTemplate(new ImageURLElementTemplateDescription("image-url-element"));
    addTemplate(new ImageURLFieldTemplateDescription("image-url-field"));
    addTemplate(new LabelTemplateDescription("label"));
    addTemplate(new MessageFieldTemplateDescription("message-field"));
    addTemplate(new NumberFieldTemplateDescription("number-field"));
    addTemplate(new RectangleTemplateDescription("rectangle"));
    addTemplate(new RoundRectangleTemplateDescription("round-rectangle"));
    addTemplate(new ResourceFieldTemplateDescription("resource-field"));
    addTemplate(new ResourceLabelTemplateDescription("resource-label"));
    addTemplate(new ResourceMessageTemplateDescription("resource-message"));
    addTemplate(new ShapeFieldTemplateDescription("shape-field"));
    addTemplate(new StringFieldTemplateDescription("string-field"));
    addTemplate(new VerticalLineTemplateDescription("vertical-line"));
  }

  /**
   * Adds a template.
   *
   * @param td the template description.
   */
  private void addTemplate (final TemplateDescription td)
  {
    registerClass(td.getObjectClass(), td);
  }
}
