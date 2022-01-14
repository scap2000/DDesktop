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
 * ReportDrawable.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.util;

import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.style.StyleSheet;
import org.jfree.ui.Drawable;
import org.jfree.util.Configuration;

/**
 * A report drawable receives context-information from the report processor that may allow the implementation to return
 * better results. Implement this interface to get some extra hints for your drawing task.
 *
 * @author Thomas Morgner
 */
public interface ReportDrawable extends Drawable
{
  /**
   * Provides the Layout-Support of the current report processor to the drawable. The layout-support can be used to
   * compute text-layouts.
   *
   * @param layoutSupport the layout support.
   */
  public void setLayoutSupport(LayoutSupport layoutSupport);

  /**
   * Provides the current report configuration of the current report process to the drawable. The report configuration
   * can be used to configure the drawing process through the report.
   *
   * @param config the report configuration.
   */
  public void setConfiguration(Configuration config);

  /**
   * Provides the computed stylesheet of the report element that contained this drawable. The stylesheet is immutable.
   *
   * @param style the stylesheet.
   */
  public void setStyleSheet(StyleSheet style);

  /**
   * Defines the resource-bundle factory that can be used to localize the drawing process.
   *
   * @param bundleFactory the resource-bundle factory.
   */
  public void setResourceBundleFactory(final ResourceBundleFactory bundleFactory);
}
