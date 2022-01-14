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
 * HidePageBandForTableExportFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import org.jfree.report.Group;
import org.jfree.report.ReportDefinition;
import org.jfree.report.event.ReportEvent;

/**
 * Hides the page header and footer if the export type is not pageable. Repeated groupheaders can be disabled by this
 * function as well.
 *
 * @author Thomas Morgner
 */
public class HidePageBandForTableExportFunction extends AbstractFunction
{
  /**
   * A flag indicating whether page bands should be hidden.
   */
  private boolean hidePageBands;
  /**
   * A flag indicating whether repeating group header and footer should made non-repeating. 
   */
  private boolean disableRepeatingHeader;

  /**
   * Default Constructor.
   */
  public HidePageBandForTableExportFunction()
  {
    hidePageBands = true;
  }

  /**
   * Applies the defined flags to the report.
   *
   * @param event the report event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    final boolean isTable = getRuntime().getExportDescriptor().startsWith("pageable") == false;

    final ReportDefinition report = event.getReport();
    if (isHidePageBands())
    {
      report.getPageHeader().setVisible(isTable == false);
      report.getPageFooter().setVisible(isTable == false);
    }
    if (isDisableRepeatingHeader())
    {
      final int gc = report.getGroupCount();
      for (int i = 0; i < gc; i++)
      {
        final Group g = report.getGroup(i);
        if (g.getHeader().isRepeat())
        {
          g.getHeader().setRepeat(isTable == false);
        }
      }
    }
  }

  /**
   * Returns whether page bands should be hidden.
   *
   * @return true, if page bands should be hidden, false otherwise.
   */
  public boolean isHidePageBands()
  {
    return hidePageBands;
  }

  /**
   * Defines whether page bands should be hidden.
   *
   * @param hidePageBands true, if page bands should be hidden, false otherwise.
   */
  public void setHidePageBands(final boolean hidePageBands)
  {
    this.hidePageBands = hidePageBands;
  }

  /**
   * Returns whether repeating group header and footer should made non-repeating.
   *
   * @return true, if repeating header and footer will be disabled, false otherwise. 
   */
  public boolean isDisableRepeatingHeader()
  {
    return disableRepeatingHeader;
  }

  /**
   * Defines whether repeating group header and footer should made non-repeating.
   *
   * @param disableRepeatingHeader true, if repeating header and footer will be disabled, false otherwise.
   */
  public void setDisableRepeatingHeader(final boolean disableRepeatingHeader)
  {
    this.disableRepeatingHeader = disableRepeatingHeader;
  }

  /**
   * This method returns null, as formatting functions have no computed return value.
   * @return null.
   */
  public Object getValue()
  {
    return null;
  }
}
