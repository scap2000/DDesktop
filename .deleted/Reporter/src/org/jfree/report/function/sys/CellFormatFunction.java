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
 * CellFormatFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function.sys;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.FormatSpecification;
import org.jfree.report.filter.RawDataSource;
import org.jfree.report.function.AbstractElementFormatFunction;
import org.jfree.report.function.Expression;
import org.jfree.report.function.StructureFunction;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.ElementStyleSheet;

/**
 * Todo: Document me!
 *
 * @author : Thomas Morgner
 */
public class CellFormatFunction  extends AbstractElementFormatFunction implements StructureFunction
{
  private FormatSpecification formatSpecification;

  public CellFormatFunction ()
  {
  }

  protected void processRootBand(final Band b)
  {
    final int elementCount = b.getElementCount();
    for (int i = 0; i < elementCount; i++)
    {
      final Element element = b.getElement(i);
      final DataSource source = element.getDataSource();
      if (source instanceof RawDataSource)
      {
        final ElementStyleSheet style = element.getStyle();
        final String oldFormat = (String)
            style.getStyleProperty(ElementStyleKeys.EXCEL_DATA_FORMAT_STRING);
        if (oldFormat != null && oldFormat.length() > 0)
        {
          continue;
        }
        final RawDataSource rds = (RawDataSource) source;
        formatSpecification = rds.getFormatString(getRuntime(), formatSpecification);
        if (formatSpecification.getType() == FormatSpecification.TYPE_DATE_FORMAT ||
            formatSpecification.getType() == FormatSpecification.TYPE_DECIMAL_FORMAT)
        {
          style.setStyleProperty
              (ElementStyleKeys.EXCEL_DATA_FORMAT_STRING, formatSpecification.getFormatString());
        }
      }
      if (element instanceof Band)
      {
        processRootBand((Band) element);
      }
    }
  }

  public Expression getInstance()
  {
    final CellFormatFunction instance = (CellFormatFunction) super.getInstance();
    instance.formatSpecification = null;
    return instance;
  }
}
