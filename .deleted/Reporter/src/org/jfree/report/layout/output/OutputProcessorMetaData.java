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
 * OutputProcessorMetaData.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import org.jfree.fonts.registry.FontMetrics;
import org.jfree.report.style.StyleSheet;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 04.04.2007, 14:36:57
 *
 * @author Thomas Morgner
 */
public interface OutputProcessorMetaData
{
  public boolean isFeatureSupported (OutputProcessorFeature.BooleanOutputProcessorFeature feature);
  public double getNumericFeatureValue (OutputProcessorFeature.NumericOutputProcessorFeature feature);

  public boolean isContentSupported (Object content);
  public FontMetrics getFontMetrics (StyleSheet styleSheet);
  public String getNormalizedFontFamilyName (final String name);

  /**
   * The export descriptor is a string that describes the output characteristics.
   * For libLayout outputs, it should start with the output class (one of
   * 'pageable', 'flow' or 'stream'), followed by '/liblayout/' and finally
   * followed by the output type (ie. PDF, Print, etc).
   *
   * @return the export descriptor.
   */
  public String getExportDescriptor();

  public Configuration getConfiguration();
}
