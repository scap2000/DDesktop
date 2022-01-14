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
 * SubReport.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.util.HashMap;
import java.util.Map;

/**
 * A subreport element. A subreport can be attached to a root-level band and will be printed afterwards. Subreports have
 * their own tablemodel (queried with the sub-reports's defined labelQuery and the master report's data-factory).
 * <p/>
 * A sub-report that has been added to a root-level band will always be printed below the root-level band.
 * <p/>
 * Sub-reports can have import and export parameters. The parameter mapping can be defined freely, so a subreport is not
 * required to use the same destColumn names as the parent report.
 * <p/>
 * If a global import or export is defined (by adding the parameter mapping "*" => "*") the other defined parameter
 * mappings will be ignored.
 *
 * @author Thomas Morgner
 */
public class SubReport extends AbstractReportDefinition
{
  /**
   * A mapping of export parameters.
   */
  private HashMap exportParameters;
  /**
   * A mapping of import parameters.
   */
  private HashMap inputParameters;

  /**
   * Creates a new subreport instance.
   */
  public SubReport()
  {
    exportParameters = new HashMap();
    inputParameters = new HashMap();
  }

  /**
   * Returns the page definition assigned to the report definition. The page definition defines the report area and how
   * the report is subdivided by the child pages.
   *
   * @return null, as subreports have no page-definition at all.
   */
  public PageDefinition getPageDefinition()
  {
    return null;
  }

  /**
   * Clones the report.
   *
   * @return the clone.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final SubReport o = (SubReport) super.clone();
    o.exportParameters = (HashMap) exportParameters.clone();
    o.inputParameters = (HashMap) inputParameters.clone();
    return o;
  }

  /**
     * Adds an export-parameter mapping to the subreport. The parameter specified by 'sourceColumn' will be made available
     * with the name 'outerName' in the parent report.
     *
     * @param outerName the name the parameter will get in the master report.
     * @param sourceColumn the source-destColumn in the sub-report.
     */
  public void addExportParameter(final String outerName, final String sourceColumn)
  {
    if (outerName == null)
    {
      throw new NullPointerException();
    }
    if (sourceColumn == null)
    {
      throw new NullPointerException();
    }
    exportParameters.put(outerName, sourceColumn);
  }

  /**
   * Removes the export parameter from the mapping.
   *
   * @param outerName the name of the parameter as it is known in the master report.
   */
  public void removeExportParameter(final String outerName)
  {
    if (outerName == null)
    {
      throw new NullPointerException();
    }
    exportParameters.remove(outerName);
  }

  /**
   * Returns the parameter mappings for the subreport. The parameter mappings define how columns of the sub-report get
   * mapped into the master report.
   *
   * @return the parameter mappings array.
   */
  public ParameterMapping[] getExportMappings()
  {
    final Map.Entry[] inputEntries = (Map.Entry[])
        exportParameters.entrySet().toArray(new Map.Entry[exportParameters.size()]);
    final ParameterMapping[] mapping = new ParameterMapping[exportParameters.size()];

    for (int i = 0; i < inputEntries.length; i++)
    {
      final Map.Entry entry = inputEntries[i];
      final String name = (String) entry.getKey();
      final String alias = (String) entry.getValue();
      mapping[i] = new ParameterMapping(name, alias);
    }
    return mapping;
  }

  /**
     * Adds an input-parameter mapping to the subreport. Input parameters define how columns that exist in the parent
     * report get mapped into the subreport.
     * <p/>
     * Input parameter mapping happens only once, so after the report has been started, changes to the parameters will not
     * pass through to the subreport.
     *
     * @param outerName the name of the parent report's destColumn that provides the data.
     * @param sourceColumn the name under which the parameter will be available in the subreport.
     */
  public void addInputParameter(final String outerName, final String sourceColumn)
  {
    inputParameters.put(sourceColumn, outerName);
  }

  /**
     * Removes the input parameter from the parameter mapping.
     *
     * @param sourceColumn the name of the destColumn of the subreport report that acts as source for the input parameter.
     */
  public void removeInputParameter(final String sourceColumn)
  {
    inputParameters.remove(sourceColumn);
  }

  /**
   * Returns the input mappings defined for this subreport.
   *
   * @return the input mappings, never null.
   */
  public ParameterMapping[] getInputMappings()
  {
    final Map.Entry[] inputEntries = (Map.Entry[])
        inputParameters.entrySet().toArray(new Map.Entry[inputParameters.size()]);
    final ParameterMapping[] mapping = new ParameterMapping[inputParameters.size()];

    for (int i = 0; i < inputEntries.length; i++)
    {
      final Map.Entry entry = inputEntries[i];
      final String alias = (String) entry.getKey();
      final String name = (String) entry.getValue();
      mapping[i] = new ParameterMapping(name, alias);
    }
    return mapping;
  }

  /**
   * Checks, whether a global import is defined. A global import effectly overrides all other imports.
   *
   * @return true, if there is a global import defined, false otherwise.
   */
  public boolean isGlobalImport()
  {
    return "*".equals(inputParameters.get("*"));
  }

  /**
   * Checks, whether a global export is defined. A global export effectly overrides all other export mappings.
   *
   * @return true, if there is a global export defined, false otherwise.
   */
  public boolean isGlobalExport()
  {
    return "*".equals(exportParameters.get("*"));
  }
}
